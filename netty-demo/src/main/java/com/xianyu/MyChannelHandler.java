package com.xianyu;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.management.BufferPoolMXBean;
import java.nio.charset.StandardCharsets;

public class MyChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务端已经收到消息：--->" + byteBuf.toString(StandardCharsets.UTF_8));
        //通过ctx获取channel
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello client".getBytes(StandardCharsets.UTF_8)));
    }
}
