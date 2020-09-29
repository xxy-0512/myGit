package com.xxy.wallet.commen.bean;

import com.xxy.wallet.pojo.WalletObject;
import com.xxy.wallet.pojo.WalletObjectDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 11:33 2020/9/15
 */
public class ResultWalletObject {
    /**
     * 红包ID
     */
    private Integer walletObjId;

    /**
     *  红包金额
     */
    private BigDecimal money;

    /**
     * 已抢红包
     */
    private BigDecimal hasMoney;

    /**
     * 总个数
     */
    private Integer total;

    /**
     * 红包类型 1、普通红包 2、拼手气红包
     */
    private Integer walletType;

    /**
     *  红包状态 1、已完成 2、未完成 3、已退回
     */
    private Integer walletStatus;

    /**
     * 发送者ID
     */
    private Integer sendUserId;

    /**
     * 当前用户已抢金额，没抢则为0
     */
    private BigDecimal owmMoney;

    /**
     *WalletObjDetail List
     */
    private List<WalletObjDetail> details;


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

    public BigDecimal getHasMoney() {
        return hasMoney;
    }

    public void setHasMoney(BigDecimal hasMoney) {
        this.hasMoney = hasMoney;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getWalletType() {
        return walletType;
    }

    public void setWalletType(Integer walletType) {
        this.walletType = walletType;
    }

    public Integer getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(Integer walletStatus) {
        this.walletStatus = walletStatus;
    }

    public Integer getSendUserId(Integer userId) {
        return sendUserId;
    }

    public void setSendUserId(Integer sendUserId) {
        this.sendUserId = sendUserId;
    }

    public BigDecimal getOwmMoney() {
        return owmMoney;
    }

    public void setOwmMoney(BigDecimal owmMoney) {
        this.owmMoney = owmMoney;
    }

    public List<WalletObjDetail> getDetails() {
        return details;
    }

    public void setDetails(List<WalletObjDetail> details) {
        this.details = details;
    }
}
