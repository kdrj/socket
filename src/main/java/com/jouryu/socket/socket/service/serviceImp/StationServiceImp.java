package com.jouryu.socket.socket.service.serviceImp;

import com.jouryu.socket.socket.mapper.StationMapper;
import com.jouryu.socket.socket.model.Station;
import com.jouryu.socket.socket.service.StationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: socket
 * @description: 查询站点
 * @author: kdrj
 * @date: 2019-09-05 13:52
 **/
@Service
public class StationServiceImp  implements StationService {

    private static final Logger logger=LoggerFactory.getLogger(StationServiceImp.class);

    @Autowired
    private StationMapper stationMapper;

    @Override
    public List<Station> getStations() {
//        logger.info("查询站点操作");
        return stationMapper.getStations();
    }
}
