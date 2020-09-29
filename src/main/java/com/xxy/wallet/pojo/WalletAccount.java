package com.xxy.wallet.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 9:34 2020/9/10
 */
public class WalletAccount {

    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal freezeBalance;

    /**
     * 账户状态 1、正常 2、冻结 3、作废
     */
    private Integer status;

    /**
     * 创建账户时间
     */
    private Date createDate;

    /**
     * 修改账户时间
     */
    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(BigDecimal freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
