package com.lucheng.learn.client_1.netty_learning.timeclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerChannelHandler extends ChannelInboundHandlerAdapter {
    private final ByteBuf byteBuf;
    public TimeServerChannelHandler(){
        String msg = "query time order";
        byteBuf = Unpooled.buffer(msg.length());
        byteBuf.writeBytes(msg.getBytes());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String serverMsg = new String(bytes,"utf-8");
        System.out.print(String.format("收到来自服务器的时间应答：{%s}", serverMsg));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
