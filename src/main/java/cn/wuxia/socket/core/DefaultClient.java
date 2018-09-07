package cn.wuxia.socket.core;

import cn.wuxia.socket.NettyConstants;
import cn.wuxia.socket.common.ChannelType;
import cn.wuxia.socket.common.ExContext;
import cn.wuxia.socket.common.server.ServerAddress;

public class DefaultClient extends BaseClient {

    private ExContext ex;

    public DefaultClient(ExContext ex) {
        defaultPort = NettyConstants.CLIENT_PORT;
        ip = NettyConstants.CLIENT_ADDRESS;
        channelType = ChannelType.valueOf(NettyConstants.CLIENT_CHANNELTYPE);
        this.ex = ex;
    }

    public DefaultClient(ExContext ex, String ip, int port, String channelType) {
        this.defaultPort = port;
        this.ip = ip;
        this.channelType = ChannelType.valueOf(channelType);
        this.ex = ex;

    }

    @Override
    protected ServerAddress getServerAddress() {
        return new ServerAddress(ip, defaultPort);
    }

    public String serverName() {
        return "jdbc" + channelType + "client";
    }

    @Override
    protected ExContext getExCount() {
        return ex;
    }

}
