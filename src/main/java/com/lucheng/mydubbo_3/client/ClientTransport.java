package com.lucheng.mydubbo_3.client;

import com.lucheng.mydubbo_3.bean.ResponseMessage;
import io.netty.channel.Channel;

/**
 * 客户端传输层
 * @date 2020-03-26
 * @author lucheng
 */
public class ClientTransport {
    /**
     * 客户端 channel
     */
    private Channel channel;

    /**
     * 同步调用
     * @param msg
     * @return
     */
    public ResponseMessage sensMsg(Object msg){
        return new ResponseMessage();
    }

    /**
     * 异步调用
     * @param msg
     * @return
     */
    public ResponseMessage sendAysc(Object msg){
        return new ResponseMessage();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
