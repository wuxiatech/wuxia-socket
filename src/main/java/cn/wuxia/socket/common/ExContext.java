package cn.wuxia.socket.common;

import io.netty.util.AttributeKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wuxia.socket.model.ExResultVo;

public class ExContext {

    String id;

    /**
     * 发送给服务端的东西与参数
     */
    private Object exchangeObject;

    private Map<String, Object> param = new HashMap<String, Object>();

    //返回结果
    private ExResultVo exResultVo = new ExResultVo();

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

    public ExResultVo getExResultVo() {
        return exResultVo;
    }

    public void setExResultVo(ExResultVo exResultVo) {
        this.exResultVo = exResultVo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
