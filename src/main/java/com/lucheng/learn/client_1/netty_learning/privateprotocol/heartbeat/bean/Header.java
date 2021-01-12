package com.lucheng.learn.client_1.netty_learning.privateprotocol.heartbeat.bean;

import java.io.Serializable;

/**
 * 消息头
 */
public class Header implements Serializable {
    /**
     * 消息类型
     */
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
