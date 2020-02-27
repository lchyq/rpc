package com.lucheng.client_1.netty_learning.zhanbao.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class ZhanBaoServerHandler extends ChannelInboundHandlerAdapter {
    private int count;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String s = new String(bytes,"UTF-8");
        s = s.substring(0,s.length() - "\n".length());
        System.out.print(String.format("收到来自客户端的请求：{%s},count：{%s}", s, ++count));
        if("query time order".equalsIgnoreCase(s)){
            System.out.print(3);
            String response = new Date(System.currentTimeMillis()).toString() + "\n";
            ByteBuf byteBuf1 = Unpooled.copiedBuffer(response.getBytes());
            ctx.writeAndFlush(byteBuf1);
        }else{
            System.out.print(4);
            String response = "Bad order" + "\n";
//            ByteBuf byteBuf1 = Unpooled.copiedBuffer(response.getBytes());
            ByteBuf byteBuf1 = Unpooled.buffer(response.getBytes().length);
            byteBuf1.writeBytes(response.getBytes());
            ctx.writeAndFlush(byteBuf1);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
