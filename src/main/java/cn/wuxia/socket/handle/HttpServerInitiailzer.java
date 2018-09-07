package cn.wuxia.socket.handle;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

public class HttpServerInitiailzer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;
    
    ChannelHandlerAdapter serverHandler;

    public HttpServerInitiailzer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
    	
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpServerCodec());
        serverHandler = new HttpServerChannelInboundHandler();
        p.addLast(serverHandler);
    }

	public ChannelHandlerAdapter getServerHandler() {
		return serverHandler;
	}

	public void setServerHandler(ChannelHandlerAdapter serverHandler) {
		this.serverHandler = serverHandler;
	}
    
    
    
}
