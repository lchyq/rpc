package com.lucheng.mydubbo_3.client;

import com.lucheng.mydubbo_3.bean.RequestMessage;
import com.lucheng.mydubbo_3.bean.ResponseMessage;
import com.lucheng.mydubbo_3.future.MsgFuture;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

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
    public CompletableFuture<Object> sendAysc(Object msg){
        CompletableFuture<Object> result = new CompletableFuture<>();
        CompletableFuture<ResponseMessage> future = CompletableFuture.supplyAsync(new Supplier<ResponseMessage>() {
            @Override
            public ResponseMessage get() {
                RequestMessage requestMessage = (RequestMessage) msg;
                MsgFuture<ResponseMessage> msgFuture = new MsgFuture<>();
                MSG_FUTURE_MAP.put(requestMessage.getRequestId(),msgFuture);
                channel.writeAndFlush(requestMessage);
                msgFuture.setSendTime(System.currentTimeMillis());
                try {
                    log.error("开始处理调用");
                    return msgFuture.get(2000, TimeUnit.MILLISECONDS);
                } catch(Exception e){
                    log.error("rpc异步调用失败 e:{}",e);
                }
                return new ResponseMessage();
            }
        });
        future.whenComplete(new BiConsumer<ResponseMessage, Throwable>() {
            @Override
            public void accept(ResponseMessage responseMessage, Throwable throwable) {
                log.error("response：{}",responseMessage);
                CompletableFuture<String> completableFuture = (CompletableFuture<String>) responseMessage.getResult();
                try {
                    result.complete(completableFuture.get());
                } catch (Exception e) {
                    log.error("异步调用失败-- 78 e:{}",e);
                }
            }
        });
        return result;
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
