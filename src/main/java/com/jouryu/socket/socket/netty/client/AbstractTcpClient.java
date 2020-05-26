package com.jouryu.socket.socket.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * @program: socket
 * @description: 模拟客户端的抽象类
 * @author: kdrj
 * @date: 2019-09-8 08:40
 **/
public abstract class AbstractTcpClient implements Runnable  {

    private static final Logger loggert=LoggerFactory.getLogger(AbstractTcpClient.class);

    protected String configName;

    private Properties props;

    private String host;

    private Integer port;

    private void startInit(){
        props=new Properties();
        try {
            props.load(AbstractTcpClient.class.getClassLoader().getResourceAsStream("nettyserver.properties"));
        } catch (IOException e) {
            loggert.info(e.getMessage());
        }
        host=getProperty("host");
        port=Integer.valueOf(getProperty("port"));
    }
    private String getProperty(String name){
        return props.getProperty(configName+"."+name);
    }

    @Override
    public void run() {
        try {
            startClient();
        } catch (InterruptedException e) {
            loggert.info(e.getMessage());
        }
    }

    /**
     * 启动客户端
     * @throws InterruptedException
     */
    public void startClient() throws InterruptedException {
        startInit();
        EventLoopGroup group=new NioEventLoopGroup();
        try {
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new CustomChannelInitializer());
            //异步连接服务器
            ChannelFuture cf=bootstrap.connect().sync();
            //异步等待关闭channel
            cf.channel().closeFuture().sync();
            loggert.warn("关闭客户端");
        }finally {
            //释放线程池资源
            group.shutdownGracefully().sync();
        }

    }

    //客户端实现初始化channel类
    protected abstract void channelInit(SocketChannel socketChannel);

    /**
     * channel初始化类
     */
    private class CustomChannelInitializer extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            channelInit(socketChannel);
            loggert.warn("客户端连接成功");
        }
    }
}
