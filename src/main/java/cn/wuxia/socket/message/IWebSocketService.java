/*
* Created on :2017年9月27日
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 wuxia.gd.cn All right reserved.
*/
package cn.wuxia.socket.message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface IWebSocketService {
    void handleFrame(ChannelHandlerContext ctx, WebSocketFrame frame); 
}
