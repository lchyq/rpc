package com.lucheng.mydubbo_3.future;

import com.lucheng.mydubbo_3.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 自定义future
 * 用来处理服务端的响应结果
 * @author lucheng
 * @date 2020-03-27
 */
@Slf4j
public class MsgFuture<T> implements Future<T> {
    //是否完成
    private boolean isDone = false;
    //是否取消
    private boolean isCancel = false;
    //响应结果
    private T result;
    //发送时间
    private long sendTime;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return isCancel;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        //立即返回结果，不会等待
        if(isDone){
            return result;
        }
        if(isCancel){
            throw new RpcException("future 已失效，获取结果失败");
        }
        if(result != null){
            return result;
        }
        return null;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        //简单模拟超时获取结果
        for(;;){
            long systemTime = System.currentTimeMillis();
            long leftTime = systemTime - sendTime - timeout;
            if(leftTime >= 0){
                if(result != null){
                    return result;
                }
                throw new RpcException("获取服务端响应结果超时");
            }
        }
    }

    /**
     * 设置发送时间
     * @param sendTime
     */
    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 设置返回结果
     * @param result
     */
    public void setResult(T result){
        if(isDone){
            return;
        }
        synchronized (this){
            if(isDone){
                return;
            }
            if(this.result == null){
                this.result = result;
            }
        }
    }
}
