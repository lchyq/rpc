package com.lucheng.learn.server_1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 用于响应的handler
 *
 * ChannelHandlerContext 帮助我们触发各种io事件
 */
public class EchoHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**
         * 此处无需release
         * netty已经帮我们做了处理
         * 单纯的调用 write() 不会将该消息写入到channel上
         * 需要调用 flush() 来强制的输出
         */
        ctx.writeAndFlush(msg);
    }
}
