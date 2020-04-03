package com.lucheng.mydubbo_3.client;

import com.lucheng.mydubbo_3.bean.RequestMessage;
import com.lucheng.mydubbo_3.bean.ResponseMessage;
import com.lucheng.mydubbo_3.exception.RpcException;
import com.lucheng.mydubbo_3.future.MsgFuture;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 客户端传输层
 * 用来同步调用、异步调用、处理服务端返回得结果
 * @date 2020-03-26
 * @author lucheng
 */
@Slf4j
@Data
public class ClientTransport {
    /**
     * 客户端 channel
     */
    private Channel channel;

    /**
     * 同步调用时
     * requestId 与 msgFuture之间的映射
     */
    private static final Map<String, MsgFuture> MSG_FUTURE_MAP = new ConcurrentHashMap<>();

    /**
     * 异步调用时
     * requestId 与 completeFuture之间的映射
     */
    private static final Map<String, CompletableFuture> MSG_COMPLETABLE_FUTURE_MAP = new ConcurrentHashMap<>();

    /**
     * 是否异步调用
     */
    private Boolean async;

    /**
     * 同步调用
     * @param msg
     * @return
     */
    public ResponseMessage sendMsg(Object msg,Integer timeOut) throws InterruptedException, ExecutionException, TimeoutException {
        if(msg == null){
            throw new RpcException("msg cannot be null");
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
    public CompletableFuture<Object> sendAsync(Object msg){
        if(msg == null){
            throw new RpcException("msg cannot be null");
        }
        CompletableFuture<Object> result = new CompletableFuture<>();
        RequestMessage requestMessage = (RequestMessage) msg;
        MSG_COMPLETABLE_FUTURE_MAP.put(requestMessage.getRequestId(),result);
        channel.writeAndFlush(requestMessage);
        log.error("rpc调用端请求发送完毕");
        return result;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void receiveResponse(ResponseMessage msg) throws ExecutionException, InterruptedException {
        String msgId = msg.getRequestId();
        if(async){
            //异步调用结果处理
            CompletableFuture resultFuture = MSG_COMPLETABLE_FUTURE_MAP.remove(msgId);
            if(resultFuture == null){
                log.error("未发现异步调用future，可能因超时被移除...");
                throw new RpcException("未发现异步调用future，可能因超时被移除...");
            }
            CompletableFuture<Object> result = (CompletableFuture<Object>) msg.getResult();
            resultFuture.complete(result.get());
            log.error("rpc调用获取到异步调用结果");
            return;
        }
        MsgFuture msgFuture = MSG_FUTURE_MAP.remove(msgId);
        if(msgFuture == null){
            log.error("没有发现相关联的future,可能因为超时已被删除");
            throw new RpcException("没有发现相关联的future,可能因为超时已被删除");
        }
        msgFuture.setResult(msg);
    }

}
