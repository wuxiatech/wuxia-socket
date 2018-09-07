package cn.wuxia.socket.handle;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

public class ByteBufClientInitializer extends ChannelInitializer<SocketChannel>{

	
	ChannelHandlerAdapter serverHandler;
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		 ChannelPipeline p = ch.pipeline();
		 //serverHandler = new ByteBufClientChannelInboundHandler();
         p.addLast(serverHandler);
		
	}
	
	public ChannelHandlerAdapter getServerHandler() {
		return serverHandler;
	}

	public void setServerHandler(ChannelHandlerAdapter serverHandler) {
		this.serverHandler = serverHandler;
	}

}
