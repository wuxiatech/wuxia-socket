package cn.wuxia.socket.common;

import java.util.HashMap;
import java.util.Map;

import cn.wuxia.socket.common.server.Handle;
import cn.wuxia.socket.model.ExResultVo;

public class Cache {
	
	/**
	 * 返回结果集临时存放地 
	 */
	private static Map<String,ExContext> exMap = new HashMap<String, ExContext>();

	public static Map<String, ExContext> getExMap() {
		return exMap;
	}
	
	private static Map<String,Handle> handlers = new HashMap<String,Handle>();

	public static Map<String, Handle> getHandlers() {
		return handlers;
	}


	
	
	
	

}
