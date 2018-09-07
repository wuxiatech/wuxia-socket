package cn.wuxia.socket.handle;

import cn.wuxia.socket.NettyConstants;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class ObjectClientInitializer extends ChannelInitializer<SocketChannel> {

    ChannelHandlerAdapter serverHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final SslContext sslCtx;
        if (NettyConstants.SSL) {
            sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }
        ChannelPipeline p = ch.pipeline();

        // serverHandler = SpringContextHolder.getBean("clientHandler");
        serverHandler = new ObjectClientChannelInboundHandler();
        if (serverHandler != null) {
            serverHandler.setExcontext(getExcontext());
        }
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(null)), serverHandler);

    }

    public ChannelHandlerAdapter getServerHandler() {
        return serverHandler;
    }

    public void setServerHandler(ChannelHandlerAdapter serverHandler) {
        this.serverHandler = serverHandler;
    }

}
