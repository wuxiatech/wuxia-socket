package cn.wuxia.socket.common.server;

import cn.wuxia.socket.common.server.resp.Resp;

public interface Handle {

	public Resp handleRequest(NettyServiceRequest request);
}
