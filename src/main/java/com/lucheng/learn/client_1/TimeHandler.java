package com.lucheng.learn.client_1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 时间响应客户端
 */
public class TimeHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = (ByteBuf) msg;
        try{
            System.out.print(m.toString(CharsetUtil.US_ASCII));
            ctx.close();
        }finally {
            m.release();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf str = ctx.alloc().buffer();
        str.writeBytes("你好啊服务器".getBytes());
        ChannelFuture future = ctx.writeAndFlush(str);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.print("向服务器发送了数据");
            }
        });
    }
}
