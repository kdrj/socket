package com.jouryu.socket.socket.hbase;

import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import org.apache.hadoop.hbase.client.Mutation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: socket
 * @description: hbaseservice
 * @author: kdrj
 * @date: 2019-09-12 08:56
 **/
@Service
public class HbaseService {

    @Autowired
    HbaseTemplate hbaseTemplate;

    public List<Mutation> saveOrUpdate(String tableName,List<Mutation> datas){
        hbaseTemplate.saveOrUpdates(tableName,datas);
        return datas;
    }

}
