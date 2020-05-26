package com.jouryu.socket.socket.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.util.Properties;

/**
 * @program: socket
 * @description: 抽象netty
 * @author: kdrj
 * @date: 2019-09-06 15:19
 **/
abstract class AbstractNettyServer implements Runnable {

    private static final Logger logger=LoggerFactory.getLogger(AbstractNettyServer.class);

    protected String configName;

    private Properties configProps;

    private io.netty.channel.Channel serverChannel;

    private InetSocketAddress socketAddress;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;

    private Integer backLog;

    private boolean keepalive;

    private boolean noDelay;

    public void startServer() throws IOException, InterruptedException {
        configProps=new Properties();
        configProps.load(AbstractNettyServer.class.getClassLoader().getResourceAsStream("nettyserver.properties"));
        String host=getProperties("host");
        Integer port=Integer.valueOf(getProperties("port"));
        Integer bossCount=Integer.valueOf(getProperties("bossCount"));
        Integer workerCount=Integer.valueOf(getProperties("workerCount"));

        backLog = Integer.valueOf(getProperties("backlog"));
        keepalive = Boolean.parseBoolean(getProperties("keepalive"));
        noDelay = Boolean.parseBoolean(getProperties("nodelay"));
        socketAddress = new InetSocketAddress(host, port);
        bossGroup = new NioEventLoopGroup(bossCount);
        workerGroup = new NioEventLoopGroup(workerCount);
        bootstrap();
    }
    @PreDestroy
    public void stop() throws Exception {
        serverChannel.close();
        serverChannel.parent().close();
    }
    private void bootstrap() throws InterruptedException {
        ServerBootstrap bootstrap=new ServerBootstrap();

        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,backLog)
                .childOption(ChannelOption.SO_KEEPALIVE,keepalive)
                .childOption(ChannelOption.TCP_NODELAY,noDelay)
                .childHandler(new SocketChannelInitializer());

        serverChannel=bootstrap.bind(socketAddress).sync().channel();
        logger.info("netty server 启动成功！端口:"+socketAddress.getPort());
        serverChannel.closeFuture().sync();
    }

    public abstract void channelHandlerRegister(SocketChannel socketChannel);

    private class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            channelHandlerRegister(socketChannel);
        }
    }

    private String getProperties(String name){
        return configProps.getProperty(configName+"."+name);
    }
}
