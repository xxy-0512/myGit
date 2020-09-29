package com.xxy.wallet.commen.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 10:40 2020/9/20
 */
public class SendWalletDetail {
    /**
     * 红包ID
     */
    private Integer walletObjId;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 个数
     */
    private Integer total;

    /**
     * 已抢个数
     */
    private Integer haveTotal;

    /**
     *  红包类型 1、普通红包 2、拼手气红包
     */
    private Integer type;

    /**
     * 发送者ID
     */
    private Integer sendUserId;

    /**
     * 发送红包时间
     */
    private Date createTime;

    public Integer getWalletObjId() {
        return walletObjId;
    }

    public void setWalletObjId(Integer walletObjId) {
        this.walletObjId = walletObjId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getHaveTotal() {
        return haveTotal;
    }

    public void setHaveTotal(Integer haveTotal) {
        this.haveTotal = haveTotal;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Integer sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
