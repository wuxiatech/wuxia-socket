/*
* Created on :2017年9月26日
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 wuxia.gd.cn All right reserved.
*/
package cn.wuxia.socket;

import java.util.Properties;

import org.apache.commons.lang3.BooleanUtils;

import cn.wuxia.common.util.PropertiesUtils;

public class NettyConstants {
    static Properties property = PropertiesUtils.loadProperties("netty.config.properties");

    public static int CLIENT_PORT = Integer.valueOf(property.getProperty("netty.client.port"));

    public static String CLIENT_ADDRESS = property.getProperty("netty.client.ip");

    public static int SERVER_PORT = Integer.valueOf(property.getProperty("netty.server.port"));

    public static String SERVER_ADDRESS = property.getProperty("netty.server.ip");

    public static String CLIENT_CHANNELTYPE = property.getProperty("netty.client.channelType");

    public static String SERVER_CHANNELTYPE = property.getProperty("netty.server.channelType");

    public static boolean SSL = BooleanUtils.toBoolean(property.getProperty("netty.ssl"));
}
