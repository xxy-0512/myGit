package com.xxy.wallet.service;

import com.github.pagehelper.PageHelper;
import com.xxy.wallet.commen.bean.*;
import com.xxy.wallet.commen.constant.WalletConstant;
import com.xxy.wallet.commen.exception.ParamterErrorExcepiton;
import com.xxy.wallet.dao.WalletDetailsDao;
import com.xxy.wallet.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 17:34 2020/9/16
 */
@Service
public class WalletDetailService {

    Logger logger = LoggerFactory.getLogger(WalletDetailService.class);

    @Autowired
    private WalletDetailsDao walletDetailsDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 查询个人余额
     * @param userId 用户ID
     * @return
     */
    public WalletAccount getWallet(Integer userId) {
        //MySql数据库查询用户个人余额
        WalletAccount result =  walletDetailsDao.getWallet(userId);
        return result;
    }



    /**
     * 获取红包状态以及个人是否领取
     * @param walletObjId 红包ID
     * @param sendUserId 发送红包用户ID
     * @return
     */
    public WalletStatus walletStatus(Integer walletObjId, Integer sendUserId, HttpServletRequest request, Integer userId) {
        //根据红包ID、发送红包用户ID 查询红包状态
        WalletObject walletObject = walletDetailsDao.walletObject(walletObjId, sendUserId);
        //获取红包状态信息
        Integer walletStatus = walletObject.getWalletStatus();
        //将红包对象信息赋给WalletStatusModel
        WalletStatus status = new WalletStatus();
        status.setWalletObjStatus(walletStatus);
        //根据红包对象ID查询用户领取红包状态
        WalletObjectDetail walletObjectDetail = walletDetailsDao.getHasWallet(walletObjId, userId);
        if (walletObjectDetail.getUserId().equals(userId)){
            status.setHaveGet(WalletConstant.HadStatus.HAD_WALLET);
        }else {
            status.setHaveGet(WalletConstant.HadStatus.NOT_HAD_WALLET);
        }
        return status;
    }


    /**
     * 查询用户余额明细
     * @param userId 用户Id
     * @return
     */
    public List<WalletAccountDetail> walletDetail(Integer userId) throws ParamterErrorExcepiton {
        //根据用户ID查询用户余额明细
        List<WalletAccountDetail> walletAccountDetail =  walletDetailsDao.walletDetail(userId);
       if (walletAccountDetail == null){
           logger.info("User Account Detail is null user id is" + userId);
           throw new ParamterErrorExcepiton();
       }
        return walletAccountDetail;
    }

    /**
     * 查询原心个人余额明细列表
     * @param userId
     * @return
     */
    public List<WalletAccountDetail> walletDetails(Integer userId, Integer page, Integer limit) {
        //根据用户ID查询用户详细信息
        PageHelper.startPage(page, limit);
        return walletDetailsDao.walletDetails(userId);
    }

    /**
     * 获取个人发出的红包
     * @param userId 用户iD
     * @param page 分页、页码 默认1
     * @param limit 每页条数 默认20
     * @return
     */
    public PesonSendWallet sendWallet(Integer userId, Integer page, Integer limit) {
        PesonSendWallet pesonSendWallet = new PesonSendWallet();
        //根据用户ID获取红包总金额以及发出的个数
        PageHelper.startPage(page, limit);
        List<WalletObject> list = walletDetailsDao.sendWallet(userId);
        //获取发送红包总个数
        Integer sumTotal = list.size();
        BigDecimal sumMoney = new BigDecimal("0.00");
        List<SendWalletDetail> sendWalletDetails = new ArrayList<>();;
        //获取发送红包总金额
        for(WalletObject walletObject : list){
            SendWalletDetail sendWalletDetail = new SendWalletDetail();
            BigDecimal money = walletObject.getMoney();
            sumMoney = sumMoney.add(money);
            //获取已抢红包个数
            String redisKey = String.format(Commens.REDIS_SPENDING_WALLET, walletObject.getUserId(), walletObject.getLinkId(), walletObject.getId());
            Long size = redisTemplate.opsForList().size(redisKey);
            Integer haveTotal = size.intValue();
            sendWalletDetail.setWalletObjId(walletObject.getId());
            sendWalletDetail.setMoney(walletObject.getMoney());
            sendWalletDetail.setTotal(walletObject.getTotal());
            sendWalletDetail.setHaveTotal(haveTotal);
            sendWalletDetail.setType(walletObject.getType());
            sendWalletDetail.setSendUserId(walletObject.getUserId());
            sendWalletDetail.setCreateTime(walletObject.getCreateDate());
            sendWalletDetails.add(sendWalletDetail);
        }
        pesonSendWallet.setSumMoney(sumMoney);
        pesonSendWallet.setSumTotal(sumTotal);
        pesonSendWallet.setSendWalletDetail(sendWalletDetails);
        return pesonSendWallet;
    }

    /**
     * 获取个人抢到的红包
     * @param userId 用户ID
     * @param page 页码
     * @param limit 每页条数
     * @return
     */
    public ReceivedWalletDetail receivedWallet(Integer userId, Integer page, Integer limit) {
        //大红包ID
        Integer walletObjectId = null;
        BigDecimal sumMoney = new BigDecimal("0.00");
        ReceiveWalletObjectDetails receiveResult = null;
        List<ReceiveWalletObjectDetails> details = new ArrayList<>();
        ReceivedWalletDetail receivedWalletDetail = new ReceivedWalletDetail();
        //通过UserID查询 红包状态 红包ID 发送者ID 发送者姓名
        PageHelper.startPage(page, limit);
        List<WalletObjectDetail> walletObjectDetailList =  walletDetailsDao.getWalletDetail(userId);
        //已抢过的红包个数
        Integer sumTotal = walletObjectDetailList.size();
        for (WalletObjectDetail walletObjectDetail : walletObjectDetailList){
            receiveResult = new ReceiveWalletObjectDetails();
            //红包ID
            walletObjectId = walletObjectDetail.getWalletObjectId();
            receiveResult.setWalletObjectId(walletObjectId);
            //每个红包的金额
            receiveResult.setMoney(walletObjectDetail.getMoney());
            //总金额
            sumMoney = sumMoney.add(walletObjectDetail.getMoney());
            //手气最佳
            receiveResult.setIsLuckGuy(walletObjectDetail.getIsLuckyGuy());
            Integer sumIsLuckyGuy = 0;
            if (walletObjectDetail.getIsLuckyGuy() == 1){
                sumIsLuckyGuy += 1;
            }
            receivedWalletDetail.setIsLuckGuy(sumIsLuckyGuy);
            //修改时间
            receiveResult.setUpdateTime(walletObjectDetail.getUpdateDate());
            //根据大红包对象查询红包状态，发送着ID， 发送者姓名,
            List<WalletObject> result = walletDetailsDao.getWalletStatus(walletObjectId);
            Integer id = null;
            for (WalletObject walletObject : result){
                //发送红包用户ID
                id = walletObject.getUserId();
                receiveResult.setSendUserId(id);
                receiveResult.setWalletObjectId(walletObject.getId());
                receiveResult.setWalletObjectStatus(walletObject.getType());
                //红包类型
                receiveResult.setType(walletObject.getType());
            }
            //根据用户ID查询用户真实姓名
            List<User> list =  walletDetailsDao.getRealName(id);
            for (User user : list){
                receiveResult.setSendUserName(user.getRealName());
            }
            details.add(receiveResult);
        }
        receivedWalletDetail.setSumMoney(sumMoney);
        receivedWalletDetail.setSumTotal(sumTotal);
        receivedWalletDetail.setReceiveWalletObjectDetailsList(details);
        return receivedWalletDetail;
    }
}
