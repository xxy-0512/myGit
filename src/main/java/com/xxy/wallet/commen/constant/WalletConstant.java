package com.xxy.wallet.commen.constant;

import org.omg.CORBA.INTERNAL;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 16:43 2020/9/11
 */
public class WalletConstant {

    /**
     * 红包发送位置
     */
    public interface  LinkType{
        //群
        String FLOCK = "1";
        //个人
        String PERSON = "2";
    }

    /**
     * 红包类型
     */
    public interface WalletType{
        // 普通红包
        Integer NORMAL_WALLET = 1;
        // 拼手气红包
        Integer SPELL_LUCK_WALLET = 2;
    }

    /**
     * 红包来源类型
     */
    public interface SourceType{
        //余额
        Integer BALANCE = 1;
        //银行卡
        Integer CARD = 2;
    }

    /**
     * 账户状态
     */
    public interface AccountStatus{
        //不可用
        Integer NO = 0;
        //可用
        Integer YES = 1;
    }

    /**
     * 交易类型
     */
    public interface DealType{
        //发红包
        Integer SEND_WALLET = 1;
        //抢红包
        Integer GET_WALLET = 2;
        //提现
        Integer WITHDRAW_DEPOSIT = 3;
        //退回
        Integer BACK_WALLET = 4;
    }

    /**
     * 红包状态
     */
    public interface WalletStatus{
        //已完成
        Integer YES_FINISH = 1;
        //未完成
        Integer NO_FINISH = 2;
        //退回
        Integer YES_BACK = 3;
    }

    /**
     * 领取状态
     */
    public interface HadStatus{
        //已领取
        Integer HAD_WALLET = 1;

        //未领取
        Integer NOT_HAD_WALLET = 0;
    }

    /**
     * 任务调度器状态
     */
    public interface JobsType{
        //单次执行
        Integer ONCE = 1;

        //多次执行
        Integer MORE = 2;
    }
}
