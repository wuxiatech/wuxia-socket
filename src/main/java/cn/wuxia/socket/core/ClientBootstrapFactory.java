package cn.wuxia.socket.core;

import cn.wuxia.socket.common.ChannelType;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioDatagramChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;

/**
 * 
 * @function:工厂模式  客户端bootstrap创建
 */
public class ClientBootstrapFactory {
    private ClientBootstrapFactory() {
    }
    public static Bootstrap createServerBootstrap(final ChannelType channelType) throws UnsupportedOperationException {
    
        Bootstrap serverBootstrap = new Bootstrap();
        switch (channelType) {
            case NIO:
            	EventLoopGroup workerGroup = new NioEventLoopGroup();
                serverBootstrap.group(workerGroup);
                serverBootstrap.channel(NioSocketChannel.class);
                return serverBootstrap;
            case OIO:
             EventLoopGroup oworkerGroup = new OioEventLoopGroup();
             serverBootstrap.group(oworkerGroup);
             serverBootstrap.channel(OioSocketChannel.class);                
             return serverBootstrap;
            default:
                throw new UnsupportedOperationException("Failed to create ServerBootstrap,  " + channelType + " not supported!");
        }
    }
}
