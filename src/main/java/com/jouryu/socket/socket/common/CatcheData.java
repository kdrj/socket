package com.jouryu.socket.socket.common;

import com.jouryu.socket.socket.model.Sensor;
import com.jouryu.socket.socket.model.Simulator;
import com.jouryu.socket.socket.model.Station;
import com.jouryu.socket.socket.util.Utils;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: socket
 * @description: 缓存工具
 * @author: kdrj
 * @date: 2019-09-05 15:22
 **/

@Component
public class CatcheData {
    public CatcheData(){}

    //水文站和传感器对象
    public static List<Station> stations;
    //缓存stationcode和channeIdl对应关系
    public static Map<String,String> stationCodeToChannelMap=new ConcurrentHashMap<>();
    //缓存channel对象，key是channelId,value是channel对象
    public static Map<String,Channel> channelIdToChannelMap=new ConcurrentHashMap<>();

    // 莱山水文站模拟数据
    public static  List<Simulator> simulatorList;

    public static String getStationNameByCode(String stationCode){
        String stationName=null;
        for (Station station:stations) {
            if (stationCode.equals(station.getCode())){
                stationName=station.getName();
            }
        }
        return stationName;
    }

    public static boolean containsStationCode(String stationCode) {
        for (Station station:stations) {
            if (stationCode.equals(station.getCode())){
                return true;
            }
        }
        return false;
    }

    public static List<String> getCurrentMonitorList() {
        List<String> list = new ArrayList<>();

        for (String stationCode: stationCodeToChannelMap.keySet()) {
            stations.forEach(station -> {
                if (station.getCode().equals(stationCode)) {
                    list.add(station.getName());
                }
            });
        }
        return list;
    }

    public static String getStationNameByChannelId(String channelId) {
        String code=Utils.getKeyByValue(stationCodeToChannelMap,channelId);
        return getStationNameByCode(code);
    }

    public static Sensor getSensorBySensorCode(String channelId, String sensorCode) {
        String stationCode=Utils.getKeyByValue(stationCodeToChannelMap,channelId);
        for (Station station:stations) {
            if (stationCode.equals(station.getCode())){
                for (Sensor sensor:station.getSensors()) {
                    if (sensorCode.equals(sensor.getCode())){
                        return sensor;
                    }
                }
            }
        }
        return null;
    }
}
