package com.xxy.wallet.service;

import com.xxy.wallet.commen.bean.AccountAndCard;
import com.xxy.wallet.commen.bean.AccountDetailModel;
import com.xxy.wallet.commen.constant.WalletConstant;
import com.xxy.wallet.dao.WalletAccountDao;
import com.xxy.wallet.pojo.WalletAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 11:55 2020/9/13
 */
@Service
public class WalletAccountService extends BaseService{
    Logger logger = LoggerFactory.getLogger(WalletAccountService.class);
    @Autowired
    private WalletAccountDao walletAccountDao;

    /**
     * 获取账户余额以及银行卡信息
     * @param userId 用户ID
     * @return accountAndCard
     */
    public AccountAndCard accountAndCard(Integer userId) {
        AccountAndCard accountAndCard = new AccountAndCard();
        accountAndCard.setAccount(walletAccountDao.getAccount(userId));
        accountAndCard.setCard(walletAccountDao.getCards(userId));
        return accountAndCard;
    }

    /**
     * 账户减钱
     * @param detailModel
     * @return
     */
    public boolean subMoney(AccountDetailModel detailModel) {
        try{
            //需要操作得金额
            BigDecimal operationMoney = new BigDecimal(detailModel.getMoney());
            Date now = new Date();
            //获取账户信息
            WalletAccount account = walletAccountDao.getAccount(detailModel.getUserId());
            //如果账户金额不足或账户为空
            if (null == account || operationMoney.compareTo(account.getBalance()) > 0){
                logger.info("Sub money, remaining sum, user id is: " + detailModel.getUserId());
                return false;
            }
            //开始减钱
            BigDecimal realBalance = account.getBalance().subtract(operationMoney);
            Integer res =  walletAccountDao.updateAccout(realBalance, now, account.getId());
            if(res != 1){
                rollback();
                logger.error("User sub money, update account fail, user id is: " + detailModel.getUserId());
                return false;
            }
            // //根据交易类型新增交易明细，发红包和提现
            Integer res1 = walletAccountDao.insertAccountDetail(account.getId(), detailModel.getUserId(), detailModel.getWalletId(), operationMoney, detailModel.getDealType(), now);
            if(res1 != 1){
                rollback();
                logger.error("User sub money, update account detail fail, user id is: " + detailModel.getUserId());
                return false;
            }
            }catch (Exception e){
                rollback();
                logger.error("User sub money fail, user id is: " + detailModel.getUserId());
                return false;
        }
        return true;
    }

    /**
     * 账户加钱
     * @param detailModel
     * @return
     */
    public boolean addMoney(AccountDetailModel detailModel) {
        try {
            if (! checkParam(detailModel)){
                logger.warn("add money fail");
                return false;
            }
            //需要操作得金额
            BigDecimal operationMoney = new BigDecimal(detailModel.getMoney());
            BigDecimal zeroMoney = new BigDecimal(0);
            Date now = new Date();
            //获取用户信息
            WalletAccount walletAccount = walletAccountDao.getAccount(detailModel.getUserId());
            //如果账户为空则证明此用户还没有开通账户，默认给开通一个账户，如果有，则追加金额
            if (null == walletAccount){
            Integer res = walletAccountDao.insertAccount(detailModel.getUserId(),zeroMoney, WalletConstant.AccountStatus.YES, now);
                if(res != 1){
                    rollback();
                    logger.error("User add money, insert account fail, user id is: " + detailModel.getUserId());
                    return false;
                }
                walletAccount = walletAccountDao.getAccount(detailModel.getUserId());
            }
            //增加金额
            BigDecimal realBalance = walletAccount.getBalance().add(operationMoney);
            //更新余额
            Integer res = walletAccountDao.updateAccout(realBalance, now, walletAccount.getId());
            if (res != 1){
                rollback();
                logger.error("User add money, update account fail, user id is: " + detailModel.getUserId());
                return false;
            }
            //根据交易类型新增交易明细，
          Integer result = walletAccountDao.insertAccountDetail(walletAccount.getId(), detailModel.getUserId(), detailModel.getWalletId(), operationMoney, detailModel.getDealType(), now);
            if (result != 1) {
                rollback();
                logger.error("User add money, update account detail fail, user id is: " + detailModel.getUserId());
                return false;
            }
        }catch (Exception e){
            rollback();
            logger.error("User add money fail, user id is: " + detailModel.getUserId());
            return false;
        }
        return true;
    }

    private Boolean checkParam(AccountDetailModel detailModel) {
        //校验参数
        if(null == detailModel.getUserId()){
            logger.info("Add or sub money, user id is null");
            return false;
        }
        if(null == detailModel.getDealType()){
            logger.info("Add or sub, user id is: " + detailModel.getUserId() + " deal type is null");
            return false;
        }
        if(StringUtils.isEmpty(detailModel.getMoney())){
            logger.info("Add or sub, user id is: " + detailModel.getUserId() + " deal money is null");
            return false;
        }
        return true;
    }

}
