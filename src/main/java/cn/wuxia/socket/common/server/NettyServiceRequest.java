package cn.wuxia.socket.common.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wuxia.socket.model.SendVo;

public  class NettyServiceRequest {
	
	private  Map<String, Object> params;
	private  Object exchangeObject;
	private String path;
	private String className;
	private String methodName;
	private List<String> methodParamsList;
	
	public NettyServiceRequest(SendVo sendVo){
	    params = sendVo.getParam();
	    exchangeObject = sendVo.getExchangeObject();
		if(params!=null){
			if(params.get("className")!=null){
				this.className =params.get("className").toString();
			}
			if(params.get("methodName")!=null){
				this.methodName =params.get("methodName").toString();
			}
			
		}
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public Object getExchangeObject() {
		return exchangeObject;
	}

	public String getPath() {
		return path;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public List<String> getMethodParamsList() {
		return methodParamsList;
	}
	
	
	

}
