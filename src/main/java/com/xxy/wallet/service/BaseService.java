package com.xxy.wallet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @Author: xxy
 * @Description:
 * @Date: Created in 12:09 2020/9/13
 */
@Service
public class BaseService {

    protected  void rollback(){
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
