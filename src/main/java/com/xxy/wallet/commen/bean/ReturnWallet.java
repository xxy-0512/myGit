package com.xxy.wallet.commen.bean;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 10:52 2020/9/10
 */
public class ReturnWallet {

    private Integer walletObjId;

    private Integer userId;

    private String sendDate;

    public Integer getWalletObjId() {
        return walletObjId;
    }

    public void setWalletObjId(Integer walletObjId) {
        this.walletObjId = walletObjId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
}
