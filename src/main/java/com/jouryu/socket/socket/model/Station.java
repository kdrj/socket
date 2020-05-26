package com.jouryu.socket.socket.model;

import java.util.List;

/**
 * @program: socket
 * @description: 监测站点
 * @author: kdrj
 * @date: 2019-09-05 11:15
 **/
public class Station {
    private String Id;
    private String name;
    private String code;
    private Integer status;
    private List<Sensor> sensors;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public String toString() {
        return "Station{" +
                "Id='" + Id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", status=" + status +
                ", sensors=" + sensors +
                '}';
    }
}
