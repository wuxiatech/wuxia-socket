package cn.wuxia.socket.test;

import cn.wuxia.socket.common.server.Handle;
import cn.wuxia.socket.common.server.NettyServiceRequest;
import cn.wuxia.socket.common.server.resp.Resp;
import cn.wuxia.socket.common.server.resp.RespData;

public class DemoHandle implements Handle {

    public Resp handleRequest(NettyServiceRequest request) {
        Resp resp = new Resp();
        RespData respData = new RespData();
        respData.setCode("200");
        respData.setMsg("正常");
        respData.setContent(null);
        resp.setRespData(respData);
        return resp;
    }

    public static void main(String[] args) {

       

    }

}
