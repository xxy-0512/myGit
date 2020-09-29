package com.xxy.wallet.commen.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 13:42 2020/9/20
 */
public class ReceivedWalletDetail {

    /**
     * 获得红包总金额
     */
    private BigDecimal sumMoney;

    /**
     * 获得红包总数
     */
    private Integer sumTotal;

    /**
     * 手气最佳次数
     */
    private Integer isLuckGuy;

    /**
     * 获取红包对象详情
     */
    private List<ReceiveWalletObjectDetails> receiveWalletObjectDetailsList;

    public BigDecimal getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(BigDecimal sumMoney) {
        this.sumMoney = sumMoney;
    }

    public Integer getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(Integer sumTotal) {
        this.sumTotal = sumTotal;
    }

    public Integer getIsLuckGuy() {
        return isLuckGuy;
    }

    public void setIsLuckGuy(Integer isLuckGuy) {
        this.isLuckGuy = isLuckGuy;
    }

    public List<ReceiveWalletObjectDetails> getReceiveWalletObjectDetailsList() {
        return receiveWalletObjectDetailsList;
    }

    public void setReceiveWalletObjectDetailsList(List<ReceiveWalletObjectDetails> receiveWalletObjectDetailsList) {
        this.receiveWalletObjectDetailsList = receiveWalletObjectDetailsList;
    }
}
