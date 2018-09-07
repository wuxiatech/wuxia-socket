package cn.wuxia.socket.core;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wuxia.socket.common.ChannelType;
import cn.wuxia.socket.handle.ObjectServerInitiailzer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;



public class  ServerChannelFactory {
	protected final static Logger logger = LoggerFactory.getLogger("netty");
	protected static ChannelInitializer<SocketChannel> simpleChannelInboundHandler;
    protected static Channel createAcceptorChannel(
            final ChannelType channelType,
            final InetSocketAddress localAddress
    ) {
        ServerBootstrap serverBootstrap = ServerBootstrapFactory.createServerBootstrap(channelType);
        simpleChannelInboundHandler = new ObjectServerInitiailzer();
        //simpleChannelInboundHandler = SpringContextHolder.getBean("serverChannelInboundHandler");
        
        serverBootstrap
               .option(ChannelOption.SO_REUSEADDR, false)
              .option(ChannelOption.SO_KEEPALIVE, true) 
              .option(ChannelOption.SO_BACKLOG, 100)
//              .childOption(ChannelOption.TCP_NODELAY, true)
//                    .childOption(ChannelOption.SO_KEEPALIVE, true)
//                    .childOption(ChannelOption.SO_REUSEADDR, true)     //重用地址
//                    .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))// heap buf 's better
//                    .childOption(ChannelOption.SO_RCVBUF, 1048576)
//                    .childOption(ChannelOption.SO_SNDBUF, 1048576)
              .handler(new LoggingHandler(LogLevel.INFO))
              .childHandler(simpleChannelInboundHandler);
              logger.info("创建ServerBootstrap");
        try {
        	  ChannelFuture channelFuture = serverBootstrap.bind(localAddress.getPort()).sync();
        	  channelFuture.channel().closeFuture().sync();
            if (channelFuture.isSuccess()) {
                return channelFuture.channel();
            } else {
            	logger.warn(String.format("Failed to open socket! Cannot bind to port: %d!",
                        localAddress.getPort()));
            }
        } catch (InterruptedException e) {
        	logger.error("Failed to create acceptor socket.", e);
        }finally{
           serverBootstrap.group().shutdownGracefully();
        }
        return null;
    }
    
    
	public ChannelInitializer<SocketChannel> getSimpleChannelInboundHandler() {
		return simpleChannelInboundHandler;
	}
	public void setSimpleChannelInboundHandler(
			ChannelInitializer<SocketChannel> simpleChannelInboundHandler) {
		this.simpleChannelInboundHandler = simpleChannelInboundHandler;
	}
    
    
    
}
