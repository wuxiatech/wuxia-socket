package cn.wuxia.socket.core;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wuxia.socket.common.Cache;
import cn.wuxia.socket.common.ChannelType;
import cn.wuxia.socket.common.ExContext;
import cn.wuxia.socket.common.server.NetworkUtils;
import cn.wuxia.socket.common.server.ServerAddress;
import cn.wuxia.socket.model.ExResultVo;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.util.AttributeKey;

public abstract class BaseClient implements Server {
    //日志
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    //引导
    protected Bootstrap bootstrap = null;

    //常量
    protected int defaultPort = 8007;

    protected String ip = "0.0.0.0";

    protected ChannelType channelType;

    protected String id;

    protected abstract ExContext getExCount();

    public void init() {

    }

    public void start() {
        //设置唯一标记
        id = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + UUID.randomUUID();
        getExCount().setId(id);
        bootstrap = ClientChannelFactory.createAcceptorChannel(ChannelType.NIO,
                InetSocketAddress.createUnresolved(getServerAddress().getHost(), getServerAddress().getPort()), getExCount());
        logger.info("server started at " + getServerAddress());
    }

    public void stop() {
        // Wait until the server socket is closed.
        bootstrap.group().shutdownGracefully();
    }

    protected ServerAddress getServerAddress() {
        int myport = defaultPort;
        return new ServerAddress(ip, myport);
    }

    protected final SocketAddress getSocketAddress() {
        ServerAddress sa = this.getServerAddress();
        if (sa.getHost() == null) {
            return new InetSocketAddress(sa.getPort());
        }
        return new InetSocketAddress(sa.getHost(), sa.getPort());
    }

    public ExResultVo getExResultVo() {
        ExContext context = Cache.getExMap().get(id);
        Cache.getExMap().remove(id);
        if (context != null) {
            return context.getExResultVo();
        }
        return null;
    }

}
