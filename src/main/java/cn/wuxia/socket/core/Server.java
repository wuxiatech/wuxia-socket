package cn.wuxia.socket.core;

import cn.wuxia.socket.model.ExResultVo;

public interface Server {
	
	public void init();

	public void start();

	public void stop();

	public String serverName();
	
	public ExResultVo getExResultVo();
}