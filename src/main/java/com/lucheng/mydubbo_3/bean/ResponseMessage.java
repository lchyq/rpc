package com.lucheng.mydubbo_3.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucheng28
 * @date 2020-03-13
 * 远程调用响应类
 */
@Data
public class ResponseMessage implements Serializable {
    private String requestId;
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
