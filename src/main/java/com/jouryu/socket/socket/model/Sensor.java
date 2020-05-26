package com.jouryu.socket.socket.model;

/**
 * @program: socket
 * @description: 传感器
 * @author: kdrj
 * @date: 2019-09-05 10:12
 **/
public class Sensor {
    private String id;
    private String stationId;
    private String type;
    private String name;
    private String code;
    private double upThreshold;
    private double lowThreshold;
    private int updateInterval;
    private String register;
    private double resolution;
    private Station station;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getUpThreshold() {
        return upThreshold;
    }

    public void setUpThreshold(double upThreshold) {
        this.upThreshold = upThreshold;
    }

    public double getLowThreshold() {
        return lowThreshold;
    }

    public void setLowThreshold(double lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public double getResolution() {
        return resolution;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id='" + id + '\'' +
                ", stationId='" + stationId + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", upThreshold=" + upThreshold +
                ", lowThreshold=" + lowThreshold +
                ", updateInterval=" + updateInterval +
                ", register='" + register + '\'' +
                ", resolution=" + resolution +
                ", station=" + station +
                '}';
    }
}
