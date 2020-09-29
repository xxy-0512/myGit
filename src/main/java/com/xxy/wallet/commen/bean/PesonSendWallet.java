package com.xxy.wallet.commen.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: xxy
 * @Description: 个人发出红包
 * @Date: Created in 10:34 2020/9/20
 */
public class PesonSendWallet {

    /**
     * 总金额
     */
    private BigDecimal sumMoney;

    /**
     *  发出个数
     */
    private Integer sumTotal;

    /**
     * 红包明细对象
     */
    private List<SendWalletDetail> sendWalletDetail;


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

    public List<SendWalletDetail> getSendWalletDetail() {
        return sendWalletDetail;
    }

    public void setSendWalletDetail(List<SendWalletDetail> sendWalletDetail) {
        this.sendWalletDetail = sendWalletDetail;
    }

}
