package cn.wuxia.socket.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cn.wuxia.socket.common.ExContext;
import cn.wuxia.socket.core.BaseClient;
import cn.wuxia.socket.core.BaseServer;
import cn.wuxia.socket.core.DefaultClient;
import cn.wuxia.socket.core.DefaultServer;
import cn.wuxia.socket.model.ExResultVo;

public class NettyTest {

    public static void main(String[] args) {

    }

//    @Test
    public void testServer() {

        BaseServer baseNioServer = new DefaultServer();
        baseNioServer.init();
        ((DefaultServer) baseNioServer).registerHandler("handleRequest", "cn.wuxia.socket.test.DemoHandle", new DemoHandle());
        baseNioServer.start();

    }

//    @Test
    public void testClient() {

        ExContext ex = new ExContext();
        Map<String, Object> attributeKeys = new HashMap<String, Object>();
        attributeKeys.put("className", "cn.wuxia.socket.test.DemoHandle");
        attributeKeys.put("methodName", "handleRequest");
        ex.setParam(attributeKeys);
        ex.setExchangeObject(null);
        BaseClient baseNioClient = new DefaultClient(ex);
        baseNioClient.init();
        baseNioClient.start();
        baseNioClient.stop();
        ExResultVo vo = baseNioClient.getExResultVo();
        System.out.println(vo);

    }
}
