package com.lucheng.mydubbo_3.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lucheng28
 * @date 2020-03-13
 * 远程调用请求类
 */
@Data
public class RequestMessage implements Serializable {
    private String requestId;
    private Invoker invoker;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }
}
