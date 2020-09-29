package com.xxy.wallet.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 9:36 2020/9/10
 */
public class WalletAccountDetail {

    private Integer id;

    /**
     * 用户账户ID
     */
    private Integer accountId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 红包ID
     */
    private Integer walletId;

    /**
     * 账簿ID
     */
    private Integer cashBookId;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 交易类型：1、发红包 2、抢红包 3、提现 4.退回
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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

    public Integer getCashBookId() {
        return cashBookId;
    }

    public void setCashBookId(Integer cashBookId) {
        this.cashBookId = cashBookId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer type() {
        return type;
    }

    public void type(Integer type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
