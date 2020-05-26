package com.jouryu.socket.socket.netty.server;

import com.jouryu.socket.socket.netty.handler.WebSocketFrameHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @program: socket
 * @description: netty实现的websocket
 * @author: kdrj
 * @date: 2019-09-13 09:08
 **/
@Component
@Qualifier("webSocketServer1")
@PropertySource(value= "classpath:/nettyserver.properties")
public class WebSocketServer extends AbstractNettyServer {

    @Value("${socketServer.socketUrlPath}")
    private String webSocketUrlPath;

    @Autowired
    @Qualifier("webSocketFrameHandler")
    private WebSocketFrameHandler webSocketFrameHandler;

    WebSocketServer() {
        super.configName = "socketServer";
    }

    @Override
    public void channelHandlerRegister(SocketChannel socketChannel) {
        ChannelPipeline pipeline=socketChannel.pipeline();
        //往pipeline注册channelHandler
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(64*1024));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(webSocketUrlPath));
        pipeline.addLast(webSocketFrameHandler);
    }

    @Override
    public void run() {
        try {
            startServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
