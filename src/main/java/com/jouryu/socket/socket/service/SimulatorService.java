package com.jouryu.socket.socket.service;

import com.jouryu.socket.socket.mapper.SimulatorMapper;
import com.jouryu.socket.socket.model.Simulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: socket
 * @description: 测试数据service
 * @author: kdrj
 * @date: 2019-09-05 11:56
 **/

@Service
public class SimulatorService {
    @Autowired
    private SimulatorMapper simulatorMapper;

    public List<Simulator> getSimulator() {
        return simulatorMapper.getSimulator();
    }
}
