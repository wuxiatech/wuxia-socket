package cn.wuxia.socket.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SendVo implements Serializable{
	
	private Object  exchangeObject;
	
	private  Map<String,Object> param = new HashMap<String, Object>();

	public Object getExchangeObject() {
		return exchangeObject;
	}

	public void setExchangeObject(Object exchangeObject) {
		this.exchangeObject = exchangeObject;
	}

	public Map<String, Object> getParam() {
		return param;
	}

	public void setParam(Map<String, Object> param) {
		this.param = param;
	}
	
	

}
