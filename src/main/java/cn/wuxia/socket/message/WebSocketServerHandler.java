/*
* Created on :2017年9月27日
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 wuxia.gd.cn All right reserved.
*/
package cn.wuxia.socket.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);
    
    private IWebSocketService websocketService;
    
    private IHttpService httpService;

    public WebSocketServerHandler(IWebSocketService websocketService, IHttpService httpService) {
        super();
        this.websocketService = websocketService;
        this.httpService = httpService;
    }

    /*
     * @see
     * io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel
     * .ChannelHandlerContext, java.lang.Object)
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            httpService.handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            websocketService.handleFrame(ctx, (WebSocketFrame) msg);
        }
    }
    
    /* 
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelReadComplete(io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
