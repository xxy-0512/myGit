package com.xxy.wallet.commen.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 14:33 2020/9/15
 */
public class WalletObjDetail {

    /**
     * 红包明细ID
     */
    private Integer walletObjDetailId;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 手机最佳
     */
    private Integer isLuckyGuy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 获取红包用户ID
     */
    private Integer receiveUserId;

    /**
     * 用户信息
     */
    private UserObj user;

    public Integer getWalletObjDetailId() {
        return walletObjDetailId;
    }

    public void setWalletObjDetailId(Integer walletObjDetailId) {
        this.walletObjDetailId = walletObjDetailId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getIsLuckyGuy() {
        return isLuckyGuy;
    }

    public void setIsLuckyGuy(Integer isLuckyGuy) {
        this.isLuckyGuy = isLuckyGuy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Integer receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public UserObj getUser() {
        return user;
    }

    public void setUser(UserObj user) {
        this.user = user;
    }
}
