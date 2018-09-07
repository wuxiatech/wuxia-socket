package cn.wuxia.socket.core;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wuxia.socket.common.Cache;
import cn.wuxia.socket.common.ChannelType;
import cn.wuxia.socket.common.ExContext;
import cn.wuxia.socket.common.server.Handle;
import cn.wuxia.socket.common.server.NetworkUtils;
import cn.wuxia.socket.common.server.ServerAddress;
import cn.wuxia.socket.model.ExResultVo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;

public abstract class BaseServer implements Server {
	protected  Logger logger = null;
	protected  Channel allChannels = null;
	protected  ServerBootstrap bootstrap;
	protected  int defaultPort = 8007;
	protected  String ip ="0.0.0.0";
	protected  ChannelType channelType;
	
	



	public void init() {
		logger = LoggerFactory.getLogger(this.serverName());
	}
	
	
	public void start() {
		allChannels =  
				ServerChannelFactory.createAcceptorChannel(
						channelType, InetSocketAddress.createUnresolved(getServerAddress().getHost(), getServerAddress().getPort()));
		logger.info("server started at " + serverName());
	}
	
	
	public void stop() {
        // Wait until the server socket is closed.
		allChannels.close();
		
	}
	

	protected ServerAddress getServerAddress() {
		int myport = defaultPort;
		try {
			ip = NetworkUtils.getFirstNonLoopbackAddress(NetworkUtils.StackType.IPv4).getHostAddress();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return new ServerAddress(ip, myport);
	}
	
	protected final SocketAddress getSocketAddress() {
		ServerAddress sa = this.getServerAddress();
		if(sa.getHost() == null){
			return new InetSocketAddress(sa.getPort());
		}
		return new InetSocketAddress(sa.getHost(), sa.getPort());
	}
	
	
	public ExResultVo getExResultVo() {
		ExContext context = Cache.getExMap().get("");
		Cache.getExMap().remove("");
		if(context!=null){
			return context.getExResultVo();
		}
		return null;
	}
	
	
	public void registerHandler(String methodName, String className, Handle handler) {
		Cache.getHandlers().put(className + methodName, handler);
	}




	
}