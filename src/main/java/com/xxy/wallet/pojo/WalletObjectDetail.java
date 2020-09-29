package com.xxy.wallet.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 9:44 2020/9/10
 */
public class WalletObjectDetail {

    private Integer id;

    private Integer walletObjectId;

    private Integer userId;

    private BigDecimal money;

    private Integer isLuckyGuy;

    private Date createDate;

    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWalletObjectId() {
        return walletObjectId;
    }

    public void setWalletObjectId(Integer walletObjectId) {
        this.walletObjectId = walletObjectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
