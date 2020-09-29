package com.xxy.wallet.controller;

import com.xxy.wallet.commen.bean.ResultWalletObject;
import com.xxy.wallet.commen.bean.ReturnWallet;
import com.xxy.wallet.commen.constant.WalletConstant;
import com.xxy.wallet.commen.exception.AccountAbnormalityException;
import com.xxy.wallet.commen.exception.ParamterErrorExcepiton;
import com.xxy.wallet.commen.exception.WalletNumberException;
import com.xxy.wallet.filter.WalletFilter;
import com.xxy.wallet.pojo.Result;
import com.xxy.wallet.pojo.WalletObject;
import com.xxy.wallet.service.WalletObjectService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 12:12 2020/9/13
 */
@Controller
public class WalletObjectController {
    @Autowired
    private WalletObjectService walletObjectService;

    /**
     * 发送红包
     * @param request 请求
     * @param money  红包金额
     * @param total  红包个数
     * @param type   红包类型 1、普通红包 2、拼手气红包
     * @param linkType 红包发送位置类型 1、群 2、个人
     * @param linkId  群红包则为群id,个人红包为收红包者userId
     * @param sourceType 红包来源类型 1、余额 2、银行卡
     * @param description   祝福语
     * @return
     * @throws ParamterErrorExcepiton
     * @throws WalletNumberException
     * @throws AccountAbnormalityException
     */
    @RequestMapping(value = "/api/xxy/wallet/send", method = RequestMethod.POST)
    @ResponseBody
    public Result<ReturnWallet> returnWalletResult(HttpServletRequest request, @RequestParam(required = false, value = "money") String money, @RequestParam(required = false, value = "total") Integer total,
                                                   @RequestParam(required = false, value = "type") Integer type, @RequestParam(required = false, value = "linkType") Integer linkType,
                                                   @RequestParam(required = false, value = "linkId") Integer linkId, @RequestParam(required = false, value = "sourceType") Integer sourceType,
                                                   @RequestParam(required = false, value = "description") String description) throws ParamterErrorExcepiton, WalletNumberException, AccountAbnormalityException, IOException {
        //校验参数
        if (StringUtils.isEmpty(money) || null == total || null == type || null == linkType
        || null == linkId || null == sourceType || StringUtils.isEmpty(description)){
            throw new ParamterErrorExcepiton();
        }
        //发送红包位置
        if (WalletConstant.LinkType.PERSON.equals(linkType) && !total.equals(1)){
            throw new WalletNumberException();
        }
        Integer userId = Integer.valueOf(request.getAttribute(WalletFilter.USER_ID_KEY).toString());
        ReturnWallet returnWallet = walletObjectService.returnWalletResult(userId, money, total, type, linkId, linkType, sourceType, description);
        return new Result<>(returnWallet);
    }

    /**
     * 接受红包
     * @param request  http请求
     * @param walletObjId 红包ID
     * @param sendUserId 发送者ID
     * @param conversationId 回话着ID
     * @return
     * @throws ParamterErrorExcepiton
     */
    @RequestMapping(value = "/api/xxy/wallet/grab", method = RequestMethod.POST)
    @ResponseBody
    public Result<ResultWalletObject> grab(HttpServletRequest request, @RequestParam(required = false, value = "walletObjId") Integer walletObjId,
                                           @RequestParam(required = false, value = "sendUserId") Integer sendUserId, @RequestParam(required = false, value = "conversationId") Integer conversationId) throws ParamterErrorExcepiton, WalletNumberException {
        if(null == walletObjId || null == sendUserId){
            throw new ParamterErrorExcepiton();
        }
        Integer receiveUserId = Integer.valueOf(request.getAttribute(WalletFilter.USER_ID_KEY).toString());
        ResultWalletObject resultWalletObject = walletObjectService.grab(walletObjId, sendUserId, receiveUserId);
        return new Result<>(resultWalletObject);
    }

    /**
     * 红包定时退款
     * @param response
     * @param userId 用户ID
     * @param walletObjectId 红包对象ID
     * @throws ParamterErrorExcepiton
     */
    @RequestMapping(value = "/api/xxy/wallet/return", method = RequestMethod.GET)
    @ResponseBody
    public void returnMoney(HttpServletResponse response, @RequestParam(value = "userId", required = false) Integer userId,
                            @RequestParam(value = "walletObjectId", required = false) Integer walletObjectId) throws ParamterErrorExcepiton {
        if (null == userId || null == walletObjectId){
            throw new ParamterErrorExcepiton();
        }
        Boolean flag = walletObjectService.backMoney(userId, walletObjectId);
        if (flag){
            response.setStatus(HttpStatus.SC_OK);
        }else {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
