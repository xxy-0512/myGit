package com.xxy.wallet.pojo;

/**
 * @author SuZZ on 2018/5/3.
 */
public class Result<T> {

    public Result() {
        super();
    }

    public Result(T data) {
        this.data = data;
    }

    private T data;

    private String msg = "OK";

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}

