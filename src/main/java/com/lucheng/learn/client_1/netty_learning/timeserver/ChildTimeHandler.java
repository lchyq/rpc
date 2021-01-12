package com.lucheng.learn.client_1.netty_learning.timeserver;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * 初始化时用于向 reactor上注册channel 处理网络请求
 */
public class ChildTimeHandler extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new ChildChannelHandler());
    }
}
