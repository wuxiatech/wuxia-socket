package cn.wuxia.socket.test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler2  extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ServerHandler2 receive msg:"+msg.toString());
        ctx.channel().writeAndFlush("this is ServerHandler2 reply msg happend at !"+System.currentTimeMillis());
    }
}
