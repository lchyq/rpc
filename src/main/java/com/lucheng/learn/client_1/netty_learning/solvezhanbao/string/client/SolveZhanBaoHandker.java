package com.lucheng.learn.client_1.netty_learning.solvezhanbao.string.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SolveZhanBaoHandker extends ChannelInboundHandlerAdapter {
    private int count;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String query = "query time order" + "\n";
        ByteBuf byteBuf = null;
        for(int i = 0; i < 100;i++){
            byteBuf = Unpooled.copiedBuffer(query.getBytes());
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String byteBuf = (String) msg;
        System.out.print(String.format("收到来自服务器的时间：{%s},count:{%s}", byteBuf, ++count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
