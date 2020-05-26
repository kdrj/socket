package com.jouryu.socket.socket.netty.server;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jouryu.socket.socket.netty.handler.ReceiveSensorDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * @program: socket
 * @description: tcp
 * @author: kdrj
 * @date: 2019-09-06 16:26
 **/
@Component
@Qualifier("tcpServer")
public class TCPServer extends AbstractNettyServer {

    private static final Logger logger=LoggerFactory.getLogger(TCPServer.class);

    @Autowired
    @Qualifier("receiveSensorDataHandler")
    private ReceiveSensorDataHandler receiveSensorDataHandler;

    public TCPServer(){
        super.configName="tcpServer";
    }
    @Override
    public void channelHandlerRegister(SocketChannel socketChannel) {
        ChannelPipeline pipeline=socketChannel.pipeline();
        pipeline.addLast(new ByteArrayEncoder());
        pipeline.addLast(new ByteArrayDecoder());
        pipeline.addLast(receiveSensorDataHandler);
    }

    @Override
    public void run() {
        try {
            startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
