package com.jouryu.socket.socket.netty;


import com.jouryu.socket.socket.common.CatcheData;
import com.jouryu.socket.socket.netty.client.TcpClient;
import com.jouryu.socket.socket.netty.server.TCPServer;
import com.jouryu.socket.socket.netty.server.WebSocketServer;
import com.jouryu.socket.socket.service.SimulatorService;
import com.jouryu.socket.socket.service.StationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * @program: socket
 * @description: netty启动类
 * @author: kdrj
 * @date: 2019-09-06 15:14
 **/
@Component
public class RunNetty {

    private static final Logger logger=LoggerFactory.getLogger(RunNetty.class);
    @Autowired
    @Qualifier("tcpServer")
    private TCPServer tcpServer;

    @Autowired
    @Qualifier("TcpClient")
    private TcpClient tcpClient;


    @Autowired
    private StationService stationService;

    @Autowired
    private SimulatorService simulatorService;

    public void run() throws Exception {
        init();
        new Thread(tcpServer).start();
        // 启动模拟的传感器客户端
        new Thread(tcpClient).start();  // 莱山水站测试
    }

    /**
     * 初始化服务执行前所需要的所有数据
     */
    private void init() {
        CatcheData.stations = stationService.getStations();
        CatcheData.simulatorList = simulatorService.getSimulator();

    }
}
