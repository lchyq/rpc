package com.lucheng.client_1.netty_learning.solvezhanbao.delimeter.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DelimiterSolveServerHandler extends ChannelInboundHandlerAdapter {
    private int count;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.print(3);
        String request = (String) msg;
        System.out.print(String.format("收到客户端的请求：{%s},count:{%s}", request,++count));
        String response = "hello client welcome netty world $_";
        ByteBuf byteBuf = Unpooled.copiedBuffer(response.getBytes());
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.print(4);
        cause.printStackTrace();
        ctx.close();
    }
}
