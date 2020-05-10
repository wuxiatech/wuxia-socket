package cn.wuxia.socket.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * netty服务端
 */
public class NettyServer {

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer(){
        //1.定义server启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //2.定义工作组:boss分发请求给各个worker:boss负责监听端口请求，worker负责处理请求（读写）
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        //3.定义工作组
        serverBootstrap.group(boss,worker);

        //4.设置通道channel
        serverBootstrap.channel(NioServerSocketChannel.class);//A
        //serverBootstrap.channelFactory(new ReflectiveChannelFactory(NioServerSocketChannel.class));//旧版本的写法，但是此过程在A中有同样过程

        //5.添加handler，管道中的处理器，通过ChannelInitializer来构造
        serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                //此方法每次客户端连接都会调用，是为通道初始化的方法

                //获得通道channel中的管道链（执行链、handler链）
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new StringDecoder());
                pipeline.addLast("serverHandler1",new ServerHandler());
                pipeline.addLast("serverHandler2",new ServerHandler2());
                pipeline.addLast(new StringEncoder());

                System.out.println("success to initHandler!");
            }
        });

        //6.设置参数
        //设置参数，TCP参数
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 2048);         //连接缓冲池的大小
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//维持链接的活跃，清除死链接
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);//关闭延迟发送

        //7.绑定ip和port
        try {
            ChannelFuture channelFuture = serverBootstrap.bind("0.0.0.0", 9099).sync();//Future模式的channel对象
            //7.5.监听关闭
            channelFuture.channel().closeFuture().sync();  //等待服务关闭，关闭后应该释放资源
        } catch (InterruptedException e) {
            System.out.println("server start got exception!");
            e.printStackTrace();
        }finally {
            //8.优雅的关闭资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}

