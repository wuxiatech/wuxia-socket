package cn.wuxia.socket.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.Values;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

public class HttpServerChannelInboundHandler extends ChannelInboundHandlerAdapter{
	   @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) {    	
		   if (msg instanceof HttpRequest) {
	            HttpRequest req = (HttpRequest) msg;
	            if (HttpHeaders.is100ContinueExpected(req)) {
	                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
	            }
	            boolean keepAlive = HttpHeaders.isKeepAlive(req);
	            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.copiedBuffer("test".getBytes()));
	            response.headers().set(CONTENT_TYPE, "text/plain");
	            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

	            if (!keepAlive) {
	                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
	            } else {
	                response.headers().set(CONNECTION, Values.KEEP_ALIVE);
	                ctx.write(response);
	            }
	        }
	    }

	    @Override
	    public void channelReadComplete(ChannelHandlerContext ctx) {
	    	ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);  
	    }

	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	        // Close the connection when an exception is raised.
	        cause.printStackTrace();
	        //ctx.channel().close();
	        ctx.close();
	    }
}

