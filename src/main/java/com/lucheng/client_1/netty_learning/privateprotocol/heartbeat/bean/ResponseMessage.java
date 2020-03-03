package com.lucheng.client_1.netty_learning.privateprotocol.heartbeat.bean;

import java.io.Serializable;

/**
 * 响应消息
 */
public class ResponseMessage implements Serializable {
    private Header header;
    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
