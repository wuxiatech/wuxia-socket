/*
* Created on :2017年9月27日
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 wuxia.gd.cn All right reserved.
*/
package cn.wuxia.socket.message;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songlin.li
 * @date 2016年9月24日 下午2:08:58
 * 
 */
public class WebSocketServer implements IWebSocketService, IHttpService {

    public static void main(String[] args) {
        new WebSocketServer(9999).start();
    }

    // ----------------------------static fields -----------------------------

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private static final String HN_HTTP_CODEC = "HN_HTTP_CODEC";

    private static final String HN_HTTP_AGGREGATOR = "HN_HTTP_AGGREGATOR";

    private static final String HN_HTTP_CHUNK = "HN_HTTP_CHUNK";

    private static final String HN_SERVER = "HN_LOGIC";

    // handshaker attachment key
    private static final AttributeKey<WebSocketServerHandshaker> ATTR_HANDSHAKER = AttributeKey.newInstance("ATTR_KEY_CHANNELID");

    private static final int MAX_CONTENT_LENGTH = 65536;

    private static final String WEBSOCKET_UPGRADE = "websocket";

    private static final String WEBSOCKET_CONNECTION = "Upgrade";

    private static final String WEBSOCKET_URI_ROOT_PATTERN = "ws://%s:%d";

    // ------------------------ member fields -----------------------

    private String host; // 绑定的地址

    private int port; // 绑定的端口

    /**
     * 保存所有WebSocket连接
     */
    private Map<ChannelId, Channel> channelMap = new ConcurrentHashMap<ChannelId, Channel>();

    private final String WEBSOCKET_URI_ROOT;

    public WebSocketServer(int port) {
        this("localhost", port);
    }

    public WebSocketServer(String host, int port) {
        this.host = host;
        this.port = port;
        WEBSOCKET_URI_ROOT = String.format(WEBSOCKET_URI_ROOT_PATTERN, host, port);
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pl = ch.pipeline();
                // 保存该Channel的引用
                channelMap.put(ch.id(), ch);
                logger.info("new channel {}", ch);
                ch.closeFuture().addListener(new ChannelFutureListener() {

                    public void operationComplete(ChannelFuture future) throws Exception {
                        logger.info("channel close {}", future.channel());
                        // Channel 关闭后不再引用该Channel
                        channelMap.remove(future.channel().id());
                    }
                });

                pl.addLast(HN_HTTP_CODEC, new HttpServerCodec());
                pl.addLast(HN_HTTP_AGGREGATOR, new HttpObjectAggregator(MAX_CONTENT_LENGTH));
                pl.addLast(HN_HTTP_CHUNK, new ChunkedWriteHandler());
                pl.addLast(HN_SERVER, new WebSocketServerHandler(WebSocketServer.this, WebSocketServer.this));
            }

        });

        try {
            // 绑定端口
            ChannelFuture future = b.bind(host, port).addListener(new ChannelFutureListener() {

                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        logger.info("websocket started.");
                    }
                }
            }).sync();

            future.channel().closeFuture().addListener(new ChannelFutureListener() {

                public void operationComplete(ChannelFuture future) throws Exception {
                    logger.info("server channel {} closed.", future.channel());
                }

            }).sync();
        } catch (InterruptedException e) {
            logger.error(e.toString());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        logger.info("websocket server shutdown");
    }

    /* 
     * @see cc.lixiaohui.demo.netty4.websocket.IHttpService#handleHttpRequest(io.netty.handler.codec.http.FullHttpRequest)
     */
    public void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (isWebSocketUpgrade(req)) { // 该请求是不是websocket upgrade请求 
            logger.info("upgrade to websocket protocol");

            String subProtocols = req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);

            WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(WEBSOCKET_URI_ROOT, subProtocols, false);
            WebSocketServerHandshaker handshaker = factory.newHandshaker(req);

            if (handshaker == null) {// 请求头不合法, 导致handshaker没创建成功
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                // 响应该请求
                handshaker.handshake(ctx.channel(), req);
                // 把handshaker 绑定给Channel, 以便后面关闭连接用
                ctx.channel().attr(ATTR_HANDSHAKER).set(handshaker);// attach handshaker to this channel
            }
            return;
        }

        // TODO 忽略普通http请求
        logger.info("ignoring normal http request");
    }

    /*
     * @see
     * cc.lixiaohui.demo.netty4.websocket.IWebSocketService#handleFrame(io.netty
     * .channel.Channel, io.netty.handler.codec.http.websocketx.WebSocketFrame)
     */
    public void handleFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // text frame
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();
            TextWebSocketFrame rspFrame = new TextWebSocketFrame(text);
            logger.info("recieve TextWebSocketFrame from channel {}", ctx.channel());
            // 发给其他所有channel
            for (Channel ch : channelMap.values()) {
                if (ctx.channel().equals(ch)) {
                    continue;
                }
                ch.writeAndFlush(rspFrame);
                logger.info("write text[{}] to channel {}", text, ch);
            }
            return;
        }

        // ping frame, 回复pong frame即可
        if (frame instanceof PingWebSocketFrame) {
            logger.info("recieve PingWebSocketFrame from channel {}", ctx.channel());
            ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (frame instanceof PongWebSocketFrame) {
            logger.info("recieve PongWebSocketFrame from channel {}", ctx.channel());
            return;
        }
        // close frame, 
        if (frame instanceof CloseWebSocketFrame) {
            logger.info("recieve CloseWebSocketFrame from channel {}", ctx.channel());
            WebSocketServerHandshaker handshaker = ctx.channel().attr(ATTR_HANDSHAKER).get();
            if (handshaker == null) {
                logger.error("channel {} have no HandShaker", ctx.channel());
                return;
            }
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 剩下的是binary frame, 忽略
        logger.warn("unhandle binary frame from channel {}", ctx.channel());
    }

    //三者与：1.GET? 2.Upgrade头 包含websocket字符串?  3.Connection头 包含 Upgrade字符串?
    private boolean isWebSocketUpgrade(FullHttpRequest req) {
        HttpHeaders headers = req.headers();
        return req.method().equals(HttpMethod.GET) && headers.get(HttpHeaderNames.UPGRADE).contains(WEBSOCKET_UPGRADE)
                && headers.get(HttpHeaderNames.CONNECTION).contains(WEBSOCKET_CONNECTION);
    }

}
