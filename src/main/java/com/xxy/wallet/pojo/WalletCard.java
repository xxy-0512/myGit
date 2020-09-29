package com.xxy.wallet.pojo;

import java.util.Date;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 9:46 2020/9/10
 */
public class WalletCard {

    private Integer id;

    private Integer userId;

    private String userName;

    private String cardNumber;

    private Integer status;

    private Integer isAuthorization;

    private Integer isChangeFree;

    private Date createDate;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsAuthorization() {
        return isAuthorization;
    }

    public void setIsAuthorization(Integer isAuthorization) {
        this.isAuthorization = isAuthorization;
    }

    public Integer getIsChangeFree() {
        return isChangeFree;
    }

    public void setIsChangeFree(Integer isChangeFree) {
        this.isChangeFree = isChangeFree;
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
