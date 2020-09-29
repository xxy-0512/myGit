package com.xxy.wallet.service.quartz;

import com.alibaba.fastjson.JSONObject;
import com.xxy.wallet.commen.bean.AccountDetailModel;
import com.xxy.wallet.commen.bean.Commens;
import com.xxy.wallet.commen.constant.WalletConstant;
import com.xxy.wallet.dao.WalletObjectDao;
import com.xxy.wallet.pojo.WalletObject;
import com.xxy.wallet.pojo.WalletObjectDetail;
import com.xxy.wallet.service.WalletAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 11:36 2020/9/17
 */
@Service
public class WalletQuartzService {
    /**
     * 开启日志支持
     */
    Logger logger = LoggerFactory.getLogger(WalletQuartzService.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private WalletObjectDao walletObjectDao;

    @Autowired
    private WalletAccountService walletAccountService;

    /**
     *开始定时任务，24小时后计算红包是否需要退款
     */
    @Scheduled(cron = "${xxy.return.money}")
    public void returnMoneySchedule(){
        logger.info("begin");
        System.out.println("定时器");
        boolean lock = false;
        try{
            //分布式事务锁
            lock = redisTemplate.opsForValue().setIfAbsent(Commens.REDIS_RETURN_MONEY, Commens.REDIS_LOCK_VALUE);
            if(lock){
                //查询24小时没有抢完的红包
                List<WalletObject> walletObjectList = walletObjectDao.getWillReturn(WalletConstant.WalletStatus.NO_FINISH);
                if (null != walletObjectList && walletObjectList.size() > 0){
                    walletObjectList.forEach(list -> returnMoney(list));
                }else {
                    logger.info("can not get this lock , no need return money");
                }
            }
        }catch (Exception e){
                redisTemplate.delete(Commens.REDIS_RETURN_MONEY);
                logger.error("Return money fail", e);
        } finally {
               if(lock){
                   redisTemplate.delete(Commens.REDIS_RETURN_MONEY);
                   logger.info("end of lock");
               }else {
                   logger.info("no lock need to delete");
               }
        }
    }


    /**
     * 超过24小时未领取 退款
     * @param walletObject
     */
    public void returnMoney(WalletObject walletObject) {
        //发送红包者用户id
        Integer sendUserId = walletObject.getUserId();
        //红包id
        Integer walletObjId = walletObject.getId();
        String redisKey = "";
        BigDecimal money = new BigDecimal("0.00");
        AccountDetailModel accountDetailModel = null;
        switch (walletObject.getLinkType().toString()){
            case WalletConstant.LinkType.PERSON:
                //获取个人红包key
                redisKey = String.format(Commens.REDIS_PERSON_WALLET, sendUserId, walletObject.getLinkId(), walletObjId);
                String jsonStr = redisTemplate.opsForValue().get(redisKey);
                //转换成红包明细
                WalletObjectDetail walletObjectDetail = JSONObject.parseObject(jsonStr, WalletObjectDetail.class);
                money = walletObjectDetail.getMoney();
                accountDetailModel = new AccountDetailModel(sendUserId,
                        walletObjId,
                        null,
                        WalletConstant.DealType.BACK_WALLET,
                        money.toString());
                //账户加钱
                walletAccountService.addMoney(accountDetailModel);
                //更新红包状态为已退回
                walletObjectDao.updateObjectStatus(WalletConstant.WalletStatus.YES_BACK, walletObjId);
                break;
            case WalletConstant.LinkType.FLOCK:
                //未消费队列
                redisKey = String.format(Commens.REDIS_NO_SPENDING_WALLET, sendUserId, walletObject.getLinkId(), walletObjId);
                List<String> range = redisTemplate.opsForList().range(redisKey, 0, redisTemplate.opsForList().size(redisKey));
                logger.info("Need return wallet detail size is: " + (null == range ? 0 : range.size()));
                if(null != range && range.size() > 0){
                    for(String string : range){
                        accountDetailModel = JSONObject.parseObject(string, AccountDetailModel.class);
                        money = money.add(new BigDecimal(accountDetailModel.getMoney()));
                    }
                    logger.info("Need return money is: " + money);
                    //账户加钱
                    accountDetailModel = new AccountDetailModel(sendUserId,
                            walletObjId,
                            null,
                            WalletConstant.DealType.BACK_WALLET,
                            money.toString());
                    //账户加钱
                    walletAccountService.addMoney(accountDetailModel);
                    //更新红包状态为已退回
                    walletObjectDao.updateObjectStatus(walletObjId, WalletConstant.WalletStatus.YES_BACK);
                }
                break;
            default:
                logger.warn("Unknown wallet link type");
        }
        //删除redis未抢红包队列
        redisTemplate.delete(redisKey);
    }


}
