package com.xxy.wallet.controller;

import com.xxy.wallet.commen.bean.PesonSendWallet;
import com.xxy.wallet.commen.bean.ReceivedWalletDetail;
import com.xxy.wallet.commen.bean.WalletStatus;
import com.xxy.wallet.commen.exception.ParamterErrorExcepiton;
import com.xxy.wallet.commen.exception.WalletGetStatusException;
import com.xxy.wallet.filter.WalletFilter;
import com.xxy.wallet.pojo.Result;
import com.xxy.wallet.pojo.WalletAccount;
import com.xxy.wallet.pojo.WalletAccountDetail;
import com.xxy.wallet.service.WalletDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 16:59 2020/9/16
 */
@Controller
public class WalletDetailController<walletObject> {

    Logger logger =  LoggerFactory.getLogger(WalletDetailController.class);

    @Autowired
    private WalletDetailService walletDetailService;

    /**
     * 查询用户余额明细
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/xxy/wallet/getWallet", method = RequestMethod.GET)
    @ResponseBody
    public Result<WalletAccount> getWallet(HttpServletRequest request){
        //在请求体中取出Token 获得UserId
        Integer userId = Integer.valueOf(request.getAttribute(WalletFilter.USER_ID_KEY).toString());
        logger.info("user id is" + userId);
        //Mysql 查询用户信息
        WalletAccount walletAccount = walletDetailService.getWallet(userId);
        return  new Result<>(walletAccount);
    }

    /**
     * 获取红包状态信息
     * @param walletObjId 红包状态
     * @param sendUserId  发送红包用户ID
     * @return
     * @throws WalletGetStatusException
     */
    @RequestMapping(value = "/api/xxy/wallet/status",method = RequestMethod.GET)
    @ResponseBody
    public Result<WalletStatus> walletStatus(@RequestParam(value = "walletObjId", required = false) Integer walletObjId,
                                             @RequestParam(value = "sendUserId", required = false) Integer sendUserId,
                                             HttpServletRequest request) throws WalletGetStatusException {

        //判断红包ID与发送红包用户ID 如果为空则抛出异常
        if (null == sendUserId && null == walletObjId) {
            logger.error("get wallet status fail, sendUserId is" + sendUserId + "wallet id is" + walletObjId);
            throw new WalletGetStatusException();
        }
        //接收参数至server层，进行业务处理
        Integer userId = Integer.valueOf(request.getAttribute(WalletFilter.USER_ID_KEY).toString());
        WalletStatus walletStatus = walletDetailService.walletStatus(walletObjId, sendUserId, request, userId);
        return new Result<>(walletStatus);
    }

    /**
     * 查询用户余额明细
     * @param request
     * @return
     * @throws ParamterErrorExcepiton
     */
    @RequestMapping(value = "/api/xxy/wallet/walletDetail",method = RequestMethod.GET)
    @ResponseBody
    public Result<List<WalletAccountDetail>> getAccountDetail(HttpServletRequest request) throws ParamterErrorExcepiton {
        //获取token中用户ID
        Integer userId = Integer.valueOf(request.getAttribute(WalletFilter.USER_ID_KEY).toString());
        //用户ID非null判断如果为空则抛出异常
        if (null == userId){
            logger.warn("user id is null , get wallet detail fail");
            throw new ParamterErrorExcepiton();
        }
       List<WalletAccountDetail> walletAccountDetail =  walletDetailService.walletDetail(userId);
        return new Result<>(walletAccountDetail);
    }

    /**
     * 查询原心个人余额明细列表
     * @return
     */
    @RequestMapping(value = "/api/xxy/wallet/walletDetails", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<WalletAccountDetail>> walletDetails(HttpServletRequest request,
                                                           @RequestParam(value = "page",required = false) Integer page,
                                                           @RequestParam(value = "limit", required = false) Integer limit) throws ParamterErrorExcepiton {
        //getAttribute获取用户ID
        Integer userId = Integer.valueOf(request.getAttribute(WalletFilter.USER_ID_KEY).toString());
        logger.info("user id is :" + userId);
        if (null == userId){
            logger.info("user id is null , get details fail");
            throw new ParamterErrorExcepiton();
        }
        //根据用户ID查询个人余额明细列表
       List<WalletAccountDetail> walletAccountDetailList =  walletDetailService.walletDetails(userId, page, limit);
        return new Result<>(walletAccountDetailList);
    }

    /**
     * 获取个人发出的红包
     * @param request
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/api/xxy/wallet/walletObject/sendWallet", method = RequestMethod.GET)
    @ResponseBody
    public Result<PesonSendWallet> sendWallet(HttpServletRequest request,
                                              @RequestParam(value = "page", required = false) Integer page,
                                              @RequestParam(value = "limit", required = false) Integer limit){
        //根据用户ID获取用户信息
        Integer userId = Integer.valueOf(request.getAttribute(WalletFilter.USER_ID_KEY).toString());
        //接收参数至service层，业务处理
        PesonSendWallet personList = walletDetailService.sendWallet(userId, page, limit);
        return new Result<>(personList);

    }

    /**
     * 获取个人抢到的红包
     * @param request
     * @param page 分页 页码
     * @param limit 每页条数
     * @return
     */
    @RequestMapping(value = "/api/xxy/wallet/walletObject/receivedWallet", method = RequestMethod.GET)
    @ResponseBody
    public Result<ReceivedWalletDetail> receivedWallet(HttpServletRequest request,
                                                       @RequestParam(value = "page", required = false) Integer page,
                                                       @RequestParam(value = "limit", required = false) Integer limit){
        //获取用户ID
        Integer userId = Integer.valueOf(request.getAttribute(WalletFilter.USER_ID_KEY).toString());
        //接收参数至server层，处理业务
        ReceivedWalletDetail receivedWalletDetail = walletDetailService.receivedWallet(userId, page, limit);
        return new Result<>(receivedWalletDetail);
    }
}
