package com.xxy.wallet.commen.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 13:44 2020/9/20
 */
public class ReceiveWalletObjectDetails {

    /**
     * 红包ID
     */
    private Integer walletObjectId;

    /**
     * 红包状态
     */
    private Integer walletObjectStatus;

    /**
     * 发红红包用户ID
     */
    private Integer sendUserId;

    /**
     * 发送者姓名
     */
    private String sendUserName;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 红包类型 1、普通红包 2、拼手气红包
     */
    private Integer type;

    /**
     * 是否手气最佳
     */
    private Integer isLuckGuy;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Integer getWalletObjectId() {
        return walletObjectId;
    }

    public void setWalletObjectId(Integer walletObjectId) {
        this.walletObjectId = walletObjectId;
    }

    public Integer getWalletObjectStatus() {
        return walletObjectStatus;
    }

    public void setWalletObjectStatus(Integer walletObjectStatus) {
        this.walletObjectStatus = walletObjectStatus;
    }

    public Integer getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Integer sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsLuckGuy() {
        return isLuckGuy;
    }

    public void setIsLuckGuy(Integer isLuckGuy) {
        this.isLuckGuy = isLuckGuy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
