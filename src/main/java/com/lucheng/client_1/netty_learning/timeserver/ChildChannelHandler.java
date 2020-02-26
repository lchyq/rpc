package com.lucheng.client_1.netty_learning.timeserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * channelHandler的适配器，用于处理io请求
 */
public class ChildChannelHandler extends ChannelInboundHandlerAdapter {
    /**
     * 当有消息可读时
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        //拿到可读字节数
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);//将数据写入到byte数组
        String s = new String(bytes,"utf-8");
        System.out.print("收到来自客户端的请求："+s);
        String response = "query time order".equalsIgnoreCase(s) ? new Date(System.currentTimeMillis()).toString() : "bad order";
        ByteBuf resp = Unpooled.copiedBuffer(response.getBytes()); //相当于allocate
        ctx.write(resp);//仅仅只是写入缓冲区，且是异步写入消息数组中
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();//将消息写入socketChannel
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();//关闭链接 回收句柄等资源
    }
}
