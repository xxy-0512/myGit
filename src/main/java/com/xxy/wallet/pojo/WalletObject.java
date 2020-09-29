package com.xxy.wallet.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 9:39 2020/9/10
 */
public class WalletObject {

    private Integer id;

    /**
     * 金額
     */
    private BigDecimal money;

    /**
     *紅包個數
     */
    private Integer total;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 红包类型 1、普通红包 2、拼手气红包
     */
    private Integer type;

    /**
     * 红包发送位置类型 1、群 2、个人
     */
    private Integer linkType;

    /**
     * 群红包则为群id,个人红包为收红包者userId
     */
    private Integer linkId;

    /**
     * 红包来源类型 1、余额 2、银行卡
     */
    private Integer sourceType;

    /**
     * 红包状态 1、未完成 2、已完成 3、已退回
     */
    private Integer walletStatus;

    /**
     * 祝福语
     */
    private String description;

    private Date createDate;

    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLinkType() {
        return linkType;
    }

    public void setLinkType(Integer linkType) {
        this.linkType = linkType;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(Integer walletStatus) {
        this.walletStatus = walletStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
