package com.jouryu.socket.socket.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * @program: socket
 * @description: webSockethandler
 * @author: kdrj
 * @date: 2019-09-13 09:18
 **/
@Component
@Qualifier("webSocketFrameHandler")
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger logger=LoggerFactory.getLogger(WebSocketFrameHandler.class);

//    private Map<String,String> nameList=new HashMap<>();

    public static ChannelGroup channels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Channel channel=channelHandlerContext.channel();
        logger.info(channel.id()+"发来消息");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel=ctx.channel();
        channels.add(channel);
        logger.info(channel.id()+"加入会话");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel=ctx.channel();
        channels.remove(channel);
        logger.info(channel.id()+"退出会话");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel=ctx.channel();
        logger.info(channel.id()+"成功连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel=ctx.channel();
        logger.info(channel.id()+"终止连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel=ctx.channel();
        logger.info(channel.id()+"发生错误");
    }
}
