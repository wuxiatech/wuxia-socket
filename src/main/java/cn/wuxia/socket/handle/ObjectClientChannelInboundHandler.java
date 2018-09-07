package cn.wuxia.socket.handle;

import cn.wuxia.socket.common.Cache;
import cn.wuxia.socket.common.ExContext;
import cn.wuxia.socket.model.ExResultVo;
import cn.wuxia.socket.model.SendVo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ObjectClientChannelInboundHandler extends ChannelInboundHandlerAdapter{
	
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		 cause.printStackTrace();  
	     ctx.close();
	}
	 
	 
	@Override
    public void channelActive(ChannelHandlerContext ctx) {

		ExContext ex = getExcontext();
//		//设置参数
//		if(ex.getAttributeKeys() !=null){
//			Map<AttributeKey<String>,Object> map = ex.getAttributeKeys();
//			for(Map.Entry<AttributeKey<String>,Object> entry:map.entrySet()){
//				ctx.attr(entry.getKey()).set((String)entry.getValue());
//			   
//			}
//		}
		//设置发送对象
		if(ex.getExchangeObject()!=null){
			SendVo sendVo = new SendVo();
			sendVo.setExchangeObject(ex.getExchangeObject());
			sendVo.setParam(ex.getParam());
			ctx.writeAndFlush(sendVo);
		}else{
			 ctx.writeAndFlush(null);
		}

       
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	  if(msg instanceof ExResultVo){
        	  ExResultVo vo = (ExResultVo)msg;
        	  System.out.println("Client received: " + vo);
        	  ExContext ex = getExcontext();
        	  ex.setExResultVo(vo);
        	  Cache.getExMap().put(ex.getId(), ex);    		  
    	  }

    	  
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {	
       ctx.flush();
    }



    
    
    
}
