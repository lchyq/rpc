package com.lucheng.learn.server_1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 连接进来的时候
 * 发送一个时间
 */
public class TimeHandler extends ChannelInboundHandlerAdapter {
    /**
     * 有链接建立的时候被出发调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /**
         * 四个字节的buffer缓冲区
         */
        ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt(111111111);
        /**
         * 开始向管道中写入数据
         * 不一定开始了写入操作
         * 因为操作都是异步的
         */
        ChannelFuture future = ctx.writeAndFlush(time);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.print("服务端向客户端写入了数据");
                ctx.close();
            }
        });
    }
}
