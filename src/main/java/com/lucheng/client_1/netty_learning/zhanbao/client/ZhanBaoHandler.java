package com.lucheng.client_1.netty_learning.zhanbao.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ZhanBaoHandler extends ChannelInboundHandlerAdapter {
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
        System.out.print(1);
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String msg1 = new String(bytes,"UTF-8");
        System.out.print(String.format("收到来自服务器的时间：{%s},count:{%s}", msg1.substring(0,msg1.length() - "\n".length()), ++count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.print(2);
        ctx.close();
    }
}
