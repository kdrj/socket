package com.jouryu.socket.socket.controller;

import com.jouryu.socket.socket.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: socket
 * @description:
 * @author: kdrj
 * @date: 2019-09-05 14:41
 **/
@Controller
public class IndexController {

    @Autowired
    private StationService stationService;

    @GetMapping("/index")
    @ResponseBody
    public String index(){
        return stationService.getStations().toString();
    }
}
