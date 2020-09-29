package com.xxy.wallet.commen.bean;

/**
 * @Author: xxy
 * @Description: 红包状态以及个人是否领取
 * @Date: Created in 20:11 2020/9/16
 */
public class WalletStatus {
    /**
     * 红包状态 1 已完成 2 未完成 3 已退回
     */
    private Integer walletObjStatus;

    /**
     * 是否领取  是否已经领取 1、已领取 0、未领取
     */
    private Integer haveGet;

    public Integer getWalletObjStatus() {
        return walletObjStatus;
    }

    public void setWalletObjStatus(Integer walletObjStatus) {
        this.walletObjStatus = walletObjStatus;
    }

    public Integer getHaveGet() {
        return haveGet;
    }

    public void setHaveGet(Integer haveGet) {
        this.haveGet = haveGet;
    }
}
