package cn.wuxia.socket.core;

import java.net.SocketException;

import cn.wuxia.socket.NettyConstants;
import cn.wuxia.socket.common.ChannelType;
import cn.wuxia.socket.common.server.NetworkUtils;
import cn.wuxia.socket.common.server.ServerAddress;

public class DefaultServer extends BaseServer {

    public DefaultServer() {
        defaultPort = NettyConstants.SERVER_PORT;
        ip = NettyConstants.SERVER_ADDRESS;
        channelType = ChannelType.valueOf(NettyConstants.SERVER_CHANNELTYPE);
    }

    @Override
    protected ServerAddress getServerAddress() {
        String myip = "127.0.0.1";
        int myport = defaultPort;
        try {
            myip = NetworkUtils.getFirstNonLoopbackAddress(NetworkUtils.StackType.IPv4).getHostAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return new ServerAddress(myip, myport);
    }

    public String serverName() {
        return "jdbc" + channelType + "server";
    }

}
