package com.lucheng.mydubbo_3.client;

import com.lucheng.mydubbo_3.bean.RequestMessage;
import com.lucheng.mydubbo_3.bean.ResponseMessage;
import com.lucheng.mydubbo_3.future.MsgFuture;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 客户端传输层
 * @date 2020-03-26
 * @author lucheng
 */
@Slf4j
public class ClientTransport {
    /**
     * 客户端 channel
     */
    private Channel channel;

    /**
     * requestId 与 msgFuture之间的映射
     */
    private static final Map<String, MsgFuture> MSG_FUTURE_MAP = new ConcurrentHashMap<>();

    /**
     * 同步调用
     * @param msg
     * @return
     */
    public ResponseMessage sendMsg(Object msg,Integer timeOut) throws InterruptedException, ExecutionException, TimeoutException {
        if(msg == null){
            throw new RuntimeException("msg cannot be null");
        }
        RequestMessage requestMessage = (RequestMessage) msg;
        MsgFuture<ResponseMessage> msgFuture = new MsgFuture<>();
        MSG_FUTURE_MAP.put(requestMessage.getRequestId(),msgFuture);
        channel.writeAndFlush(requestMessage);
        msgFuture.setSendTime(System.currentTimeMillis());
        return msgFuture.get(timeOut, TimeUnit.MILLISECONDS);
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

    public void receiveResponse(ResponseMessage msg){
        String msgId = msg.getRequestId();
        MsgFuture msgFuture = MSG_FUTURE_MAP.remove(msgId);
        if(msgFuture == null){
            log.error("没有发现相关联的future,可能因为超时已被删除");
            throw new RuntimeException("没有发现相关联的future,可能因为超时已被删除");
        }
        msgFuture.setResult(msg);
    }
}
