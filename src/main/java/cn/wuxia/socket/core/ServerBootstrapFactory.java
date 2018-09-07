package cn.wuxia.socket.core;

import cn.wuxia.socket.common.ChannelType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;


/**
 * 
 * @function:工厂模式  服务器bootstrap创建
 */
public class ServerBootstrapFactory {
    private ServerBootstrapFactory() {
    }
    public static ServerBootstrap createServerBootstrap(final ChannelType channelType) throws UnsupportedOperationException {
    
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        switch (channelType) {
            case NIO:
            	EventLoopGroup bossGroup = new NioEventLoopGroup(1);  
            	EventLoopGroup workerGroup = new NioEventLoopGroup();
                serverBootstrap.group(bossGroup,workerGroup);
                serverBootstrap.channel(NioServerSocketChannel.class);               
                return serverBootstrap;
            case OIO:
            	EventLoopGroup obossGroup = new OioEventLoopGroup(1);  
             	EventLoopGroup oworkerGroup = new OioEventLoopGroup();
             	serverBootstrap.group(obossGroup,oworkerGroup);
             	serverBootstrap.channel(OioServerSocketChannel.class);              
                return serverBootstrap;
            default:
                throw new UnsupportedOperationException("Failed to create ServerBootstrap,  " + channelType + " not supported!");
        }
    }
}
