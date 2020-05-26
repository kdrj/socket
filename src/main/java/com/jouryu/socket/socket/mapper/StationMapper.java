package com.jouryu.socket.socket.mapper;

import com.jouryu.socket.socket.model.Station;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface StationMapper {

    List<Station> getStations();
}
