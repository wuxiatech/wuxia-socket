package cn.wuxia.socket.core;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wuxia.socket.common.ChannelType;
import cn.wuxia.socket.common.ExContext;
import cn.wuxia.socket.handle.ObjectClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;

public class ClientChannelFactory {
	protected final static Logger logger = LoggerFactory.getLogger("netty");
	protected static ChannelInitializer<SocketChannel> simpleChannelInboundHandler;
    protected static Bootstrap createAcceptorChannel(
            final ChannelType channelType,
            final InetSocketAddress localAddress,
            final ExContext ex
    ) {
        Bootstrap serverBootstrap = ClientBootstrapFactory.createServerBootstrap(channelType);
        //simpleChannelInboundHandler = SpringContextHolder.getBean("clientChannelInboundHandler");
        simpleChannelInboundHandler = new ObjectClientInitializer();
        if(simpleChannelInboundHandler!=null){
        	simpleChannelInboundHandler.setExcontext(ex);
        }
        serverBootstrap  
        		.option(ChannelOption.TCP_NODELAY, true)
                .handler(simpleChannelInboundHandler);
        try {
            ChannelFuture channelFuture = serverBootstrap.connect(
            		localAddress.getHostName(), localAddress.getPort()).sync();
            channelFuture.channel().closeFuture().sync();
            channelFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        // Connection attempt succeeded:
                        // Begin to accept incoming traffic.
                        //inboundChannel.setReadable(true);
                    	logger.debug("Server bound");
                    } else {
                        // Close the connection if the connection attempt has failed.
                        //inboundChannel.close();
                    	logger.debug("bound fail");
                    	future.cause().printStackTrace(); 
                    }
                }
            });
        } catch (Exception e) {
        	logger.error("Failed to create acceptor socket.", e);
        }finally{
        	 serverBootstrap.group().shutdownGracefully();
        }
        return serverBootstrap;
    }
	public static ChannelInitializer<SocketChannel> getSimpleChannelInboundHandler() {
		return simpleChannelInboundHandler;
	}
	public static void setSimpleChannelInboundHandler(
			ChannelInitializer<SocketChannel> simpleChannelInboundHandler) {
		ClientChannelFactory.simpleChannelInboundHandler = simpleChannelInboundHandler;
	}
    
    
}
