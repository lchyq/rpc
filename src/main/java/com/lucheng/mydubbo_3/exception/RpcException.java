package com.lucheng.mydubbo_3.exception;

/**
 * rpc调用全局异常
 * @author lucheng29
 * @date 2020-04-03
 */
public class RpcException extends RuntimeException {
    public RpcException(String message){
        super(message);
    }
}
