package com.xxy.wallet.commen.bean;

import com.xxy.wallet.pojo.WalletAccount;
import com.xxy.wallet.pojo.WalletCard;

import java.util.List;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 9:34 2020/9/10
 */
public class AccountAndCard {

    private WalletAccount account;;

    private List<WalletCard> card;

    public WalletAccount getAccount() {
        return account;
    }

    public void setAccount(WalletAccount account) {
        this.account = account;
    }

    public List<WalletCard> getCard() {
        return card;
    }

    public void setCard(List<WalletCard> card) {
        this.card = card;
    }
}
