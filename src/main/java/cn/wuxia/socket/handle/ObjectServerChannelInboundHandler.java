package cn.wuxia.socket.handle;


import java.util.Map;

import cn.wuxia.socket.common.Cache;
import cn.wuxia.socket.common.server.Handle;
import cn.wuxia.socket.common.server.NettyServiceRequest;
import cn.wuxia.socket.common.server.resp.Resp;
import cn.wuxia.socket.model.ExResultVo;
import cn.wuxia.socket.model.SendVo;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ObjectServerChannelInboundHandler extends ChannelInboundHandlerAdapter{
	   @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) {
	    	System.out.println("server channelRead..");
	        //接受信息
	    	SendVo context = (SendVo)msg;
	    	Map<String,Object> param = context.getParam();
	    	NettyServiceRequest nettyServiceRequest = new NettyServiceRequest(context);
	    	//获得需要执行的service代码
            Map<String, Handle> handles = Cache.getHandlers();
	    	//根据代码在注册的方法里查找可以调用的方法
            Handle handler = handles.get(nettyServiceRequest.getClassName() + nettyServiceRequest.getMethodName());
            if(handler ==null){
            	ctx.write(null);
            }           
	    	//调用方法
            Resp resp =handler.handleRequest(nettyServiceRequest);
	    	//回复信息
	        ExResultVo resultVo =  new ExResultVo();
	        resultVo.setStatus(String.valueOf(resp.getRespData().getCode()));
	        resultVo.setMsg(resp.getRespData().getMsg());
	        resultVo.setExchangeObject(resp.getRespData().getContent());
	        //ByteBuf resp = Unpooled.copiedBuffer("test".getBytes());
	        ctx.write(resultVo);

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
