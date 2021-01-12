package com.lucheng.learn.server_1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * 服务端用于处理请求的handler
 * 丢弃数据的服务端功能
 *
 * 请记住处理器的职责是释放所有传递到处理器的引用计数对象!
 */
public class MyHandler extends ChannelInboundHandlerAdapter {
    /**
     * 服务端收到数据时该方法被处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**
         * ByteBuf 是一个引用计数器
         * 必须要显示的 调用方法才能释放掉对象
         */
        try{
            ByteBuf ms = (ByteBuf) msg;
            System.out.print(ms.toString(CharsetUtil.UTF_8));
            ((ByteBuf)ms).release();
        }catch (Exception e){
            ReferenceCountUtil.release(msg);
        }
    }
}
