package com.jouryu.socket.socket.controller;

import com.jouryu.socket.socket.service.WebSocketServer1;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: socket
 * @description: 提供websocket服务
 * @author: kdrj
 * @date: 2019-09-06 10:24
 **/
@Controller
public class WebSocketController {

    //页面请求
    @GetMapping("/socket/{cid}")
    public String socket( @PathVariable("cid") String cid){
        return "/websocket";
    }

    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public String pushToWeb(@PathVariable String cid,String message){
        WebSocketServer1.sendInfo(message,cid);
        return "发送成功";
    }
}
