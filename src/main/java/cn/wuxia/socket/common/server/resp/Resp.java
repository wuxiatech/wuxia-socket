package cn.wuxia.socket.common.server.resp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Resp implements Serializable {
	
	private static final long serialVersionUID = 34543654757L;
	protected transient List<String> warningList = null;
	private RespData respData = new RespData();
	public Resp() {
	}	
	public Resp(String content) {
		super();
		respData.setContent(content);
	}
	public Resp(RespData data) {
		respData = data;
	}
	
	public RespData getRespData() {
		return respData;
	}
	public void setRespData(RespData respData) {
		this.respData = respData;
	}

	
	public void addWarning(String s) {
		if (s == null || s.trim().equals("")) {
			return;
		}
		if (warningList == null) {
			warningList = new ArrayList<String>(4);
		}
		warningList.add(s);
	}



	 
}