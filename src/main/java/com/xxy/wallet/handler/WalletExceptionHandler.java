package com.xxy.wallet.handler;

import com.alibaba.fastjson.JSONObject;
import com.xxy.wallet.commen.exception.AccountAbnormalityException;
import com.xxy.wallet.commen.exception.ParamterErrorExcepiton;
import com.xxy.wallet.commen.exception.WalletGetStatusException;
import com.xxy.wallet.commen.exception.WalletNumberException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 16:32 2020/9/11
 */
@ControllerAdvice
public class WalletExceptionHandler {

    Logger logger = LoggerFactory.getLogger(WalletExceptionHandler.class);

    private static final Integer PARAMTER_ERROE = 20004;
    private static final Integer UNKNOW_EXCEPTION = 20000;
    private static final Integer WALLET_NUMBER_ERROR_EXCEPTION = 20001;
    private static final Integer ACCOUNT_ABNORMALITY_EXCEPTION = 20003;
    private static final Integer GET_WALLET_STATUS_EXCEPTION = 20005;

    public JSONObject handler (HttpServletResponse response, Exception e){
        if(e instanceof ParamterErrorExcepiton){
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            JSONObject param_error = getJson("Param error", PARAMTER_ERROE);
            logger.error(param_error.toJSONString());
            return param_error;
        }else if(e instanceof WalletNumberException){
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            //返回错的数据信息
            JSONObject paramter_error = getJson("Wallet number error", WALLET_NUMBER_ERROR_EXCEPTION);
            logger.error(paramter_error.toJSONString());
            return paramter_error;
        }else if(e instanceof AccountAbnormalityException){
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            //返回错的数据信息
            JSONObject paramter_error = getJson("Account abnormality", ACCOUNT_ABNORMALITY_EXCEPTION);
            logger.error(paramter_error.toJSONString());
            return paramter_error;
        } else if(e instanceof WalletGetStatusException){
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            //返回错的数据信息
            JSONObject paramter_error = getJson("Get Wallet Status Exception", GET_WALLET_STATUS_EXCEPTION);
            logger.error(paramter_error.toJSONString());
            return paramter_error;
        }
        logger.error("Unknow exception", e);
        response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        return getJson("Unknow exception", UNKNOW_EXCEPTION);
    }

    /**
     * 生成错误的信息用于返回
     * @param message  信息
     * @param code     状态码
     * @return
     */
    private JSONObject getJson(String message, Integer code){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        jsonObject.put("status_code", code);
        JSONObject result = new JSONObject();
        result.put("errors", jsonObject);
        return result;
    }
}
