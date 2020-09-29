package com.xxy.wallet.controller;

import com.xxy.wallet.commen.bean.AccountAndCard;
import com.xxy.wallet.filter.WalletFilter;
import com.xxy.wallet.pojo.Result;
import com.xxy.wallet.service.WalletAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 11:54 2020/9/13
 */
@Controller
public class WalletAccountController {
    Logger logger = LoggerFactory.getLogger(WalletAccountController.class);

    @Autowired
    private WalletAccountService walletAccountService;

    /**
     * 获取账户和银行卡信息
     * @return
     */
    @RequestMapping(value = "/api/xxy/wallet/accounts", method = RequestMethod.GET)
    @ResponseBody
    public Result<AccountAndCard> accountAndCardResult (HttpServletRequest request){
        Integer userId = Integer.valueOf(request.getAttribute(WalletFilter.USER_ID_KEY).toString());
        logger.info("Get account and card , userId is :" + userId);
        AccountAndCard accountAndCard = walletAccountService.accountAndCard(userId);
        return new Result<>(accountAndCard);
    }
}
