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
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

public class ObjectServerInitiailzer extends ChannelInitializer<SocketChannel> {
    ChannelHandlerAdapter serverHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final SslContext sslCtx;
        if (NettyConstants.SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // serverHandler =SpringContextHolder.getBean("serverHandler");
        serverHandler = new ObjectServerChannelInboundHandler();
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        pipeline.addLast(new ReadTimeoutHandler(10));
        pipeline.addLast(new WriteTimeoutHandler(60));
        pipeline.addLast(new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(null)), serverHandler);

    }

    public ChannelHandlerAdapter getServerHandler() {
        return serverHandler;
    }

    public void setServerHandler(ChannelHandlerAdapter serverHandler) {
        this.serverHandler = serverHandler;
    }
}
