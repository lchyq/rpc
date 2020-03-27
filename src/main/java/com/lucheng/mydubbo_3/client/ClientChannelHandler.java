package com.lucheng.mydubbo_3.client;

import com.lucheng.mydubbo_3.bean.ResponseMessage;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;


/**
 * @author lucheng28
 * @date 2020-03-16
 * 客户端远程调用 handler
 */
@Slf4j
public class ClientChannelHandler extends ChannelInboundHandlerAdapter {
    /**
     * 客户端传输层
     * 从而实现 tcp长连接 可复用的目的
     */
    private ClientTransport clientTransport;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ResponseMessage responseMessage = (ResponseMessage) msg;
        clientTransport.receiveResponse(responseMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("rpc客户端远程调用失败 e:{}",cause.getMessage());
        ctx.close();
    }

    public ClientTransport getClientTransport() {
        return clientTransport;
    }

    public void setClientTransport(ClientTransport clientTransport) {
        this.clientTransport = clientTransport;
    }
}
