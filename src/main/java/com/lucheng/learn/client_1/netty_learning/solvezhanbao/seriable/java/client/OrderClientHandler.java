package com.lucheng.learn.client_1.netty_learning.solvezhanbao.seriable.java.client;

import com.lucheng.learn.client_1.netty_learning.solvezhanbao.seriable.java.bean.OrderRequest;
import com.lucheng.learn.client_1.netty_learning.solvezhanbao.seriable.java.bean.OrderResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class OrderClientHandler extends ChannelInboundHandlerAdapter {
    private int count;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i = 0;i < 10;i++){
            OrderRequest orderRequest = getOrder(i);
            ctx.writeAndFlush(orderRequest);
        }
        System.out.print(1);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.print(2);
        OrderResponse orderResponse = (OrderResponse) msg;
        System.out.print(String.format("收到服务器的订单状态信息：{%s}，count:{%s}", orderResponse, ++count ));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private OrderRequest getOrder(int i){
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAddress("北京市xxxxxxxxxx");
        orderRequest.setOrderId(i);
        orderRequest.setPhoneNumber("157xxxxxxxxx");
        orderRequest.setProName("netty");
        orderRequest.setUserName("lucheng");
        return orderRequest;
    }
}
