package com.jouryu.socket.socket.schedule;

import com.jouryu.socket.socket.common.CatcheData;
import com.jouryu.socket.socket.common.CommondEnumType;
import com.jouryu.socket.socket.model.Sensor;
import com.jouryu.socket.socket.model.Station;
import com.jouryu.socket.socket.netty.server.WebSocketServer;
import com.jouryu.socket.socket.util.Utils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @program: socket
 * @description: 定时并行任务,往传感器发送获取数据的信号
 * @author: kdrj
 * @date: 2019-09-05 15:15
 **/
@Component
@EnableScheduling
public class DataFromSensor implements SchedulingConfigurer {

    private static final Logger logger=LoggerFactory.getLogger(DataFromSensor.class);
    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        ScheduledExecutorService pool=Executors.newScheduledThreadPool(1);
        scheduledTaskRegistrar.setScheduler(pool);
    }
    @Scheduled(fixedRate = 6000)
    public void pushMessage() throws IOException {
//        logger.info("最大内存:["+Runtime.getRuntime().maxMemory()/1048576+"M]"+"  空闲内存:["+Runtime.getRuntime().freeMemory()/1048576+"M]"+"  总内存:["+Runtime.getRuntime().totalMemory()/1048576+"M]");
        if(CatcheData.stationCodeToChannelMap.size()<1){
            logger.info("等待客户端连接");

        }else {
            logger.info("往所有传感器["+String.join(",",CatcheData.getCurrentMonitorList())+"]发送请求数据信号");
        }
        CatcheData.stationCodeToChannelMap.forEach((stationCode,channelId)->{
            CatcheData.stations.forEach(station->{
                if(stationCode.equals(station.getCode())){
                    StationThread stationThread=new StationThread(station,channelId);
                    stationThread.start();
                }
            });
        });
    }

    private class StationThread extends Thread{
        Station station;
        String channelId;
        public StationThread(Station station,String channelId){
            this.station=station;
            this.channelId=channelId;
        }

        @Override
        public void run() {
            Channel channel=CatcheData.channelIdToChannelMap.get(channelId);
            CatcheData.channelIdToChannelMap.get(channelId);
            station.getSensors().forEach(sensor -> {
                byte[] ret=migrateRequestMsg(sensor);
                channel.writeAndFlush(ret);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    logger.info(e.getMessage());
//                }
            });
        }
    }

    private byte[] migrateRequestMsg(Sensor sensor) {
        StringBuilder sb=new StringBuilder();
        sb.append(sensor.getCode());
        sb.append(CommondEnumType.READ.getCommond());
        sb.append("00");
        sb.append(sensor.getRegister());
        sb.append("0001");
        byte[] bytes=Utils.hexStringToBytes(sb.toString());
        sb.append(Utils.getCRC(bytes));
        return Utils.hexStringToBytes(sb.toString());

    }
}
