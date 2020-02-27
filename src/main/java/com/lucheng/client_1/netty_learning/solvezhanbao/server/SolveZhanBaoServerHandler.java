package com.lucheng.client_1.netty_learning.solvezhanbao.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class SolveZhanBaoServerHandler extends ChannelInboundHandlerAdapter {
    private int count;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String byteBuf = (String) msg;
        System.out.print(String.format("收到来自客户端的请求：{%s},count：{%s}", byteBuf, ++count));
        if("query time order".equalsIgnoreCase(byteBuf)){
            String response = new Date(System.currentTimeMillis()).toString() + "\n";
            ByteBuf byteBuf1 = Unpooled.copiedBuffer(response.getBytes());
            ctx.writeAndFlush(byteBuf1);
        }else{
            String response = "Bad order" + "\n";
//            ByteBuf byteBuf1 = Unpooled.copiedBuffer(response.getBytes());
            ByteBuf byteBuf1 = Unpooled.buffer(response.getBytes().length);
            byteBuf1.writeBytes(response.getBytes());
            ctx.writeAndFlush(byteBuf1);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
