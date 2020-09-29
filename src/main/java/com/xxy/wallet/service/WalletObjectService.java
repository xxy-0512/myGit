package com.xxy.wallet.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xxy.wallet.commen.bean.*;
import com.xxy.wallet.commen.constant.WalletConstant;
import com.xxy.wallet.commen.exception.AccountAbnormalityException;
import com.xxy.wallet.commen.exception.WalletNumberException;
import com.xxy.wallet.dao.UserDao;
import com.xxy.wallet.dao.WalletObjectDao;
import com.xxy.wallet.pojo.Result;
import com.xxy.wallet.pojo.WalletObject;
import com.xxy.wallet.pojo.WalletObjectDetail;
import com.xxy.wallet.service.quartz.WalletQuartzService;
import com.xxy.wallet.utils.CalculateWallet;
import jdk.nashorn.internal.scripts.JS;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 12:11 2020/9/13
 */
@Service
public class WalletObjectService extends BaseService{
    Logger logger = LoggerFactory.getLogger(WalletObjectService.class);
    @Autowired
    private WalletObjectDao walletObjectDao;

    @Autowired
    private WalletAccountService walletAccountService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EventService eventService;

    @Autowired
    private WalletQuartzService walletQuartzService;
    /**
     * 发送红包
     * @param userId 用户id
     * @param money  金额，当红包类型为普通红包时，金额为单个红包金额；如果为拼手气红包，则金额为总金额
     * @param total  个数
     * @param type   红包类型 1、普通红包 2、拼手气红包
     * @param linkId 群红包则为群id,个人红包为收红包者userId
     * @param linkType 红包发送位置类型 1、群 2、个人
     * @param sourceType   红包来源类型 1、余额 2、银行卡
     * @param description  祝福语
     * @return
     */
    @Transactional
    public ReturnWallet returnWalletResult(Integer userId, String money, Integer total, Integer type, Integer linkId, Integer linkType, Integer sourceType, String description) throws AccountAbnormalityException, IOException {
        //发送红包
        ReturnWallet returnWallet = new ReturnWallet();
        returnWallet.setUserId(userId);
        //总金额
        BigDecimal totalMoney = new BigDecimal(money);
        //时间
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendDate = sdf.format(now);
        returnWallet.setSendDate(sendDate);
        Integer walletObjectId = null;
        //生成红包对象
        if (type.equals(WalletConstant.WalletType.NORMAL_WALLET)){
            totalMoney = totalMoney.multiply(new BigDecimal(total));
        }
        try {
            walletObjectId = insertWalletObject(totalMoney, total, type, userId, linkType, linkId, sourceType, WalletConstant.WalletStatus.NO_FINISH, description, now);
            returnWallet.setWalletObjId(walletObjectId);
        }catch (Exception e){
            logger.info("Insert Wallet Object fail userId is : " + userId);
        }
        //账户支付
        if(WalletConstant.SourceType.BALANCE.equals(sourceType)){
            AccountDetailModel accountDetailModel = new AccountDetailModel(userId, walletObjectId, null, WalletConstant.DealType.SEND_WALLET,totalMoney.toString());
            if (!walletAccountService.subMoney(accountDetailModel)){
                rollback();
                throw new AccountAbnormalityException();
            }
        }
        //判断是个人红包还是群红包
        try {
            WalletObjectDetail detail = new WalletObjectDetail();
            detail.setWalletObjectId(walletObjectId);
            detail.setUserId(null);
            detail.setCreateDate(now);
            detail.setUpdateDate(now);
            switch (linkType.toString()){
                case WalletConstant.LinkType.PERSON:
                    detail.setMoney(totalMoney);
                    detail.setIsLuckyGuy(0);
                    //持久化至数据库
                    walletObjectDao.insertWalletObjDetail(detail);
                    //redis个人红包队列 redis key
                    String redisKey = String.format(Commens.REDIS_PERSON_WALLET, userId, linkId, walletObjectId);
                    //redis value
                    redisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(detail));
                    break;
                case WalletConstant.LinkType.FLOCK:
                    //未消费红包队列 redis key
                    String noSpending = String.format(Commens.REDIS_NO_SPENDING_WALLET, userId, linkId, walletObjectId);
                    //redis 计数器队列 key
                    String numberKey = String.format(Commens.REDIS_WALLET_NUMBER, userId, walletObjectId);
                    //计数器容差值
                    redisTemplate.opsForValue().increment(numberKey, total+10);
                    //普通红包
                    if (type.equals(WalletConstant.WalletType.NORMAL_WALLET)){
                        for (int i = 0; i < total; i++) {
                            detail.setMoney(new BigDecimal(money));
                            detail.setIsLuckyGuy(0);
                            //持久化至数据库
                            walletObjectDao.insertWalletObjDetail(detail);
                            //未消费队列
                            redisTemplate.opsForList().rightPush(noSpending, JSONObject.toJSONString(detail));
                        }
                    }
                    //拼手气红包
                    if (type.equals(WalletConstant.WalletType.SPELL_LUCK_WALLET)){
                        List<BigDecimal> math =  CalculateWallet.math(totalMoney, total);
                        Integer maxIndex = math.indexOf(Collections.max(math));
                        for (int i = 0; i < math.size(); i++) {
                            detail.setMoney(math.get(i));
                            if (i == maxIndex){
                                detail.setIsLuckyGuy(1);
                            }else {
                                detail.setIsLuckyGuy(0);
                            }
                            //持久化至数据库
                            walletObjectDao.insertWalletObjDetail(detail);
                            //未消费队列
                            redisTemplate.opsForList().rightPush(noSpending, JSONObject.toJSONString(detail));
                        }
                    }
                    break;
                default:
                    logger.warn("Unknown wallet link type");
            }
        }catch (Exception e){
            logger.error("Send wallet fail, user id is: " + userId, e);
            //退钱至账户
            AccountDetailModel accountDetailModel = new AccountDetailModel(userId, walletObjectId, null, WalletConstant.DealType.BACK_WALLET, totalMoney.toString());
            if (! walletAccountService.addMoney(accountDetailModel)){
                throw new AccountAbnormalityException();
            }
        }
        //异步注册红包退款任务
        eventService.onEvent(userId, walletObjectId, now);
        logger.info("The wallet jobs is finish");
        return returnWallet;
    }

    /**
     * 获取红包
     * @param walletObjId   红包ID
     * @param sendUserId    发送红包用户ID
     * @param receiveUserId   接收红包用户ID
     * @return
     */
    public ResultWalletObject grab(Integer walletObjId, Integer sendUserId, Integer receiveUserId) throws WalletNumberException {
        logger.info("start grab wallet object ,send user id is "+ sendUserId +"wallet object id is "+ walletObjId + "receive user id is"+ receiveUserId);
        AccountDetailModel accountDetailModel = null;
        //获取红包对象
        WalletObject walletObject = walletObjectDao.getWallet(walletObjId, sendUserId);
        switch (walletObject.getLinkType().toString()){
            case WalletConstant.LinkType.PERSON:
                //查询ID 如红包用户ID和获取红包用户ID不匹配则无法领取红包
                if(receiveUserId.equals(walletObject.getLinkId())){
                    //从个人未消费队列取出红包
                    String redisKey = String.format(Commens.REDIS_SPENDING_WALLET, sendUserId, receiveUserId, walletObjId);
                    String jsonString = redisTemplate.opsForValue().get(redisKey);
                    //将从redis中取出的json字符串转为对象
                    WalletObjectDetail detail = JSONObject.parseObject(jsonString, WalletObjectDetail.class);
                    //取出来的对象不能为空
                    if(null != detail){
                        //跟新红包队userID
                        walletObjectDao.updateDetailById(detail.getId(), receiveUserId);
                        //更新红包状态为已完成
                        walletObjectDao.updateObjectStatus(walletObjId, WalletConstant.WalletStatus.YES_FINISH);
                        //更新账户信息，修改用户金额
                        accountDetailModel = new AccountDetailModel(receiveUserId, walletObjId, null, WalletConstant.DealType.GET_WALLET, detail.getMoney().toString());
                        walletAccountService.addMoney(accountDetailModel);
                        //删除此红包队列
                        redisTemplate.delete(redisKey);
                    }else {
                        logger.info("redis get wallet fail");
                    }
                }else {
                    logger.warn("user can not grab this wallet");
                }
                break;
            case WalletConstant.LinkType.FLOCK:
                //redis 未消费队列
                String noSpengdingKey = String.format(Commens.REDIS_NO_SPENDING_WALLET, sendUserId, walletObject.getLinkId(), walletObjId);
                //计数器
                String numberKey = String.format(Commens.REDIS_WALLET_NUMBER, sendUserId, walletObjId);
                // 用户获取红包队列ID 用于查看该用户是否领取
                String  userWalletKey= String.format(Commens.REDIS_USER_HAD_WALLET, receiveUserId, walletObjId);
                //获取计数器队列
                String number = redisTemplate.opsForValue().get(numberKey);
                logger.info("number is :" + number);
                //如果计数器为空则红包对象用户无法领取，否则用户可以领取
                if(StringUtils.isEmpty(number) && "0".equals(number)){
                    throw new WalletNumberException();
                }
                //根基用户红包队列查询该用户是否领取过红包，如果领取则无法领取
                if(StringUtils.isEmpty(redisTemplate.opsForValue().get(userWalletKey))){
                    Long increment = null;
                    //以消费队列Key
                    String spendingKey = String.format(Commens.REDIS_SPENDING_WALLET, sendUserId, walletObject.getLinkId(), walletObjId);
                    //获取未消费队列，如果未消费队列不为空，则计数器减一
                    Long size = redisTemplate.opsForList().size(noSpengdingKey);
                    if(null != size && 0L !=size){
                        increment =  redisTemplate.opsForValue().increment(numberKey, -1L);
                    }
                    //把未消费队列红包取出来一个放进以消费队列
                    redisTemplate.opsForList().rightPopAndLeftPush(noSpengdingKey, spendingKey);
                    //从以消费队列中去除用户ID更新接受红包的用户ID
                    String jsonString = redisTemplate.opsForList().index(spendingKey, 0);
                    //将redis队列中取出的json字符串转换成响应的实体类对象
                    WalletObjectDetail detail = JSONObject.parseObject(jsonString, WalletObjectDetail.class);
                    detail.setUserId(receiveUserId);
                    detail.setUpdateDate(new Date());
                    //更新以消费队列
                    redisTemplate.opsForList().set(spendingKey, 0, JSONObject.toJSONString(detail));
                    //更新用户已抢过此红包状态
                    redisTemplate.opsForValue().set(userWalletKey, "yes");
                    //将跟新数据持久化到数据库
                    walletObjectDao.updateDetailById(detail.getId(), receiveUserId);
                    //更新用户账户信息
                    accountDetailModel = new AccountDetailModel(receiveUserId, walletObjId, null, WalletConstant.DealType.GET_WALLET, detail.getMoney().toString());
                    //修改用户金额
                    walletAccountService.addMoney(accountDetailModel);
                    //判断计数器队列如果小于等于10，则更新红包状态为已完成
                    if (increment <= 10 && redisTemplate.opsForValue().size(noSpengdingKey) <= 0){
                        //修改红包状态为已完成
                        walletObject.setWalletStatus(WalletConstant.WalletStatus.YES_FINISH);
                        walletObjectDao.updateObjectStatus(walletObjId, WalletConstant.WalletStatus.YES_FINISH);
                        //删除未消费队列和以消费队列hngKey);
                        redisTemplate.delete(spendingKey);
                    }
                }
                break;
            default:
                logger.warn("Unknown wallet link type");
        }
        return resultWalletObject(walletObject, receiveUserId);
    }

    /**
     * 新增红包
     * @param totalMoney 金额
     * @param total 数量
     * @param type  红包类型
     * @param userId 用户ID
     * @param linkType 红包发送位置类型 1、群 2、个人
     * @param linkId 群红包则为群id,个人红包为收红包者userId
     * @param sourceType 红包来源类型 1、余额 2、银行卡
     * @param status  状态
     * @param description 祝福语
     * @param now 时间
     */
    private Integer insertWalletObject(BigDecimal totalMoney, Integer total, Integer type, Integer userId, Integer linkType, Integer linkId, Integer sourceType, Integer status, String description, Date now) {
        WalletObject walletObject = new WalletObject();
        walletObject.setUserId(userId);
        walletObject.setMoney(totalMoney);
        walletObject.setTotal(total);
        walletObject.setType(type);
        walletObject.setLinkId(linkId);
        walletObject.setLinkType(linkType);
        walletObject.setSourceType(sourceType);
        walletObject.setWalletStatus(status);
        walletObject.setDescription(description);
        walletObject.setCreateDate(now);
        walletObject.setUpdateDate(now);
        try {
            walletObjectDao.insertWalletObject(walletObject);
        }catch (Exception e){
            logger.error("Insert wallet object fail, user id is: " + userId, e);
        }
        //返回主键id，mybatis已经帮我们映射到所传递的实体类中。
        return walletObject.getId();
    }

    /**
     * 返回体对象
     * @param walletObject
     * @param receiveUserId
     * @return
     */
    public ResultWalletObject resultWalletObject(WalletObject walletObject, Integer receiveUserId){
        ResultWalletObject resultWalletObject = new ResultWalletObject();
        resultWalletObject.setWalletObjId(walletObject.getId());
        resultWalletObject.setMoney(walletObject.getMoney());
        resultWalletObject.setTotal(walletObject.getTotal());
        resultWalletObject.setWalletType(walletObject.getType());
        resultWalletObject.setWalletStatus(walletObject.getWalletStatus());
        resultWalletObject.getSendUserId(walletObject.getUserId());

        //获取已抢金额明细
        List<WalletObjDetail> details = walletObjectDao.getHasGrab(walletObject.getId());
        BigDecimal totalMoney = new BigDecimal("0.00");
        for (WalletObjDetail walletObjDetail: details){
            totalMoney = totalMoney.add(walletObjDetail.getMoney());
            UserObj user = userDao.getUserInfoById(walletObjDetail.getReceiveUserId());
            user.setUserId(walletObjDetail.getReceiveUserId());
            walletObjDetail.setUser(user);
            //判断是否抢过
            if (receiveUserId.equals(walletObjDetail.getWalletObjDetailId())){
                resultWalletObject.setOwmMoney(walletObjDetail.getMoney());
            }
        }
        resultWalletObject.setDetails(details);
        resultWalletObject.setHasMoney(totalMoney);
        return resultWalletObject;

    }

    /**
     * 红包定时退款
     * @param userId
     * @param walletObjectId
     * @return
     */
    public Boolean backMoney(Integer userId, Integer walletObjectId) {
        try {
            logger.info("start check wallet back money is, user id is :" + userId + " walletObjectId is :" +walletObjectId);
            WalletObject wallet = walletObjectDao.getWallet(walletObjectId , userId);
            if (wallet.getWalletStatus().equals(WalletConstant.WalletStatus.NO_FINISH)){
                walletQuartzService.returnMoney(wallet);
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
