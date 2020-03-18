package com.lucheng.mydubbo_3.server;

import com.lucheng.mydubbo_1.registercenter.RegisterMap;
import com.lucheng.mydubbo_3.bean.Invoker;
import com.lucheng.mydubbo_3.bean.RequestMessage;
import com.lucheng.mydubbo_3.bean.ResponseMessage;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * server端 事件处理类
 */
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.error("server read");
        RequestMessage requestMessage = (RequestMessage) msg;
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestId(requestMessage.getRequestId());
        Object result = handleRequest(requestMessage);
        responseMessage.setResult(result);
        ctx.writeAndFlush(responseMessage).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("rpc远程调用出现异常e:{}",cause.getMessage());
        ctx.close();
    }

    /**
     * 处理 rpc调用请求
     * @param requestMessage
     * @return
     */
    private Object handleRequest(RequestMessage requestMessage){
        Invoker invoker = requestMessage.getInvoker();
        String interName = invoker.getClassName();
        String methodName = invoker.getMethodName();
        Class[] methodTypes = invoker.getArgsType();
        Object[] methodArgs = invoker.getArgs();
        try{
            Class instance = Class.forName(RegisterMap.getProviderList().get(interName).getClass().getName());
            Method method = instance.getMethod(methodName,methodTypes);
            log.error(method.getName());
            return method.invoke(instance.newInstance(),methodArgs);
        }catch (Exception e){
            log.error(String.format("rpc调用获取类实例失败，className：{%s}", interName),e);
        }
        return null;
    }
}
