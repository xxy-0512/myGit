package com.xxy.wallet.commen.bean;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 14:34 2020/9/10
 */
public class AccountDetailModel {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 红包id
     */
    private Integer walletId;

    /**
     * 账簿id
     */
    private Integer cashBookId;

    /**
     * 交易类型
     */
    private Integer dealType;

    /**
     * 交易金额
     */
    private String money;

    public AccountDetailModel(){

    }

    public AccountDetailModel(Integer userId, Integer walletId, Integer cashBookId, Integer dealType, String money) {
        this.userId = userId;
        this.walletId = walletId;
        this.cashBookId = cashBookId;
        this.dealType = dealType;
        this.money = money;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public Integer getDealType() {
        return dealType;
    }

    public void setDealType(Integer dealType) {
        this.dealType = dealType;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Integer getCashBookId() {
        return cashBookId;
    }

    public void setCashBookId(Integer cashBookId) {
        this.cashBookId = cashBookId;
    }
}
