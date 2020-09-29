package com.xxy.wallet.commen.bean;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @Author: xxy
 * @Description: redis
 * @Date: Created in 14:46 2020/9/15
 */
public class Commens {
    /**
     * redis 红包计数器
     */
    public static final String REDIS_WALLET_NUMBER  = "xxy_wallet:send_user_id:%s_wallet_object_id:%s";

    /**
     * redis 发送红包队列
     */
    public static final String REDIS_PERSON_WALLET = "xxy_wallet:send_user_id:%s_receive_user_id:%s_wallet_object_id:%s";

    /**
     *  redis 未消费红包队列
     */
    public static final String REDIS_NO_SPENDING_WALLET = "xxy_wallet:_noSpengding:send_user_id:%s_link_id:%s_wallet_object_id:%s";

    /**
     * redis 以消费队列
     */
    public static final String REDIS_SPENDING_WALLET = "xxy_wallet_spending:send_user_id:%s_link_id:%s_wallet_object_id:%s";

    /**
     * redis 用户红包队列
     */
    public static final String REDIS_USER_HAD_WALLET = "xxy_wallet_user_has:receive_user_id:%s_wallet_object_id:%s";

    /**
     * redis 分布式事务锁value
     */
    public static final String REDIS_LOCK_VALUE = "xxy_wallet_value";

    /**
     * redis 定时器计算退款key
     */
    public static final String REDIS_RETURN_MONEY = "xxy_wallet_lock_one";

    /**
     * 时间类型连接池存储类
     * 解决多线程中相同变量的访问冲突问题
     * 格式 yyyy-MM-dd
     */
    private static ThreadLocal<SimpleDateFormat> dateTimeLocal = new ThreadLocal<>();

    /**
     * 时间类型连接池存储类
     * 解决多线程中相同变量的访问冲突问题
     * 格式 yyyy-MM-dd HH:mm:ss
     */
    private static ThreadLocal<SimpleDateFormat> dateLocal = new ThreadLocal<>();


    /**
     * 时间格式化
     * @return
     */
    public static SimpleDateFormat dateFormat(){
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = dateTimeLocal.get();
        if (null == simpleDateFormat){
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateLocal.set(simpleDateFormat);
        }
        return simpleDateFormat;
    }

    public static SimpleDateFormat dateTimeFormat(){
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = dateTimeLocal.get();
        if (null == simpleDateFormat){
            simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            dateLocal.set(simpleDateFormat);
        }
        return simpleDateFormat;
    }
}
