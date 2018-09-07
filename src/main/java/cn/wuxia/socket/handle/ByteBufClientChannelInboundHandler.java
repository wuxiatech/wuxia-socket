package cn.wuxia.socket.handle;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


public class ByteBufClientChannelInboundHandler extends ChannelInboundHandlerAdapter{

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		 cause.printStackTrace();  
	     ctx.close();
	}
	 
	 
	@Override
    public void channelActive(ChannelHandlerContext ctx) {
		  ctx.write(Unpooled.copiedBuffer("Hello Netty!", CharsetUtil.UTF_8));  
	      ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	  ByteBuf buf = ((ByteBuf)msg);
    	  System.out.println("Client received: " + ByteBufUtil.hexDump(buf.readBytes(buf.readableBytes())));  
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
       ctx.flush();
    }



	 
	 
	 
}
