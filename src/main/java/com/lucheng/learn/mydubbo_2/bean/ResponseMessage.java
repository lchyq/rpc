package com.lucheng.learn.mydubbo_2.bean;

import java.io.Serializable;

/**
 * rpc 响应类
 */
public class ResponseMessage<T> implements Serializable {
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
