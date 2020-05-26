package com.jouryu.socket.socket.service;

import com.jouryu.socket.socket.netty.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @program: socket
 * @description:
 * @author: kdrj
 * @date: 2019-09-06 08:47
 **/
@Component
@ServerEndpoint("/websocket/{sid}")
public class WebSocketServer1 {


    private static final Logger logger=LoggerFactory.getLogger(WebSocketServer.class);

    private static CopyOnWriteArraySet<WebSocketServer1> webSocketServers=new CopyOnWriteArraySet<>();

    private Session session;

    private String sid;

//

    @OnOpen
    public void onOpen(Session session,@PathParam("sid") String sid){
        this.session=session;
        webSocketServers.add(this);
        this.sid=sid;
        logger.info("连接成功");
        try {
            int i;
            for (i = 0; i < 1000; i++) {
                sendMessage(""+i);
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            logger.error("连接异常");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(){
        logger.info("连接关闭");
        webSocketServers.remove(this);
    }

    @OnMessage
    public void onMessage(String message,Session session) throws InterruptedException {
        logger.info("收到来自"+sid+"的消息");
            Thread.sleep(1000);
            for (WebSocketServer1 websocketServer:webSocketServers) {
                try {
                    websocketServer.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }



    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendInfo(String message, @PathParam("sid") String sid){
        logger.info("推送消息到"+sid+"窗口");
        for (WebSocketServer1 item:webSocketServers) {
            try {
                if(item.sid==null){
                    item.sendMessage(message);
                }else if (item.sid.equals(sid)){
                    item.sendMessage(message);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
