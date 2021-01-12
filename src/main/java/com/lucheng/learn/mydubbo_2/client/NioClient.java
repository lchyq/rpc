package com.lucheng.learn.mydubbo_2.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * 基于nio的client
 * nio 实际上是同步非阻塞io
 */
public class NioClient {
    public void send(byte[] request) throws IOException {
        //拿到socketChannel
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        //创建selector
        Selector selector = Selector.open();
        //绑定地址
        socketChannel.bind(new InetSocketAddress("localhost",8088));
        //注册connect 事件
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        while (true){
            int select = selector.select();
            if(select > 0){
                //说明已经有channel链接好了
                Set<SelectionKey> set = selector.selectedKeys();
                for(SelectionKey selectionKey : set){
                    if(!selectionKey.isValid()){
                        continue;
                    }
                    if(selectionKey.isConnectable()){
                        //开始写数据,开始监听读事件
                        selectionKey.channel().register(selector,SelectionKey.OP_READ);
                        write(request,(SocketChannel) selectionKey.channel());
                    }
                    if(selectionKey.isReadable()){
                        //开始读数据
                        read((SocketChannel)selectionKey.channel());
                    }
                }
            }
        }
    }
    private void write(byte[] request,SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(request);//开始是读模式
        byteBuffer.flip();//转换为写模式
        socketChannel.write(byteBuffer);//nio的写操作都是非阻塞的，若一次没有写入成功那么会循环监听写事件
    }
    private void read(SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int len = socketChannel.read(byteBuffer);//nio的读操作也是非阻塞的，若没有读到消息，则直接返回，等待下一次读取消息
        if(len > 0){
            //说明读到数据
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            String msg = new String(bytes);
            System.out.print(String.format("收到来自服务端的消息：{%s}", msg));
        }
    }
}
