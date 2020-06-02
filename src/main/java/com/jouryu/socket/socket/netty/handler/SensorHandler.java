package com.jouryu.socket.socket.netty.handler;

import com.jouryu.socket.socket.common.CatcheData;
import com.jouryu.socket.socket.hbase.HbaseService;
import com.jouryu.socket.socket.model.Sensor;
import com.jouryu.socket.socket.util.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: socket
 * @description: 数据处理，保存数据，消息队列
 * @author: kdrj
 * @date: 2019-09-11 13:51
 **/
@Component
@Qualifier("sensorHandler")
@PropertySource(value= "classpath:/application.properties")
public class SensorHandler {
    private final static Logger logger=LoggerFactory.getLogger(SensorHandler.class);

    @Value("${hbasePrefix}")
    private String hbasePrefix;

    @Value("${kafkaTopicPrefix}")
    private String kafkaPrefix;
    @Autowired
    private HbaseService hbaseService;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void process(ChannelHandlerContext context,String message){
        Channel channel=context.channel();
        String channelId=ReceiveSensorDataHandler.getChannelId(channel);
        long date=new Date().getTime();
        logger.info(CatcheData.getStationNameByChannelId(channelId)+"传感器["+ReceiveSensorDataHandler.getRemoteAddress(channel)+"]"+"发来数据"+getValue(channelId,message));
        hbaseHandler(channelId,message,date);
        kafkaHandeler(channel,message,date);
    }

    private void hbaseHandler(String channelId, String message, long date) {
        String sensorCode=message.substring(0,2);
        String tableName=hbasePrefix+Utils.getKeyByValue(CatcheData.stationCodeToChannelMap,channelId);
        SimpleDateFormat rowTimeFormat=new SimpleDateFormat("yyyyMMDDHH");
        SimpleDateFormat colTimeFormat=new SimpleDateFormat("yyyyMMDDHHmmss");
        String row=rowTimeFormat.format(date);
        String col=colTimeFormat.format(date);
        String value=getValue(channelId,message);

        List<Mutation> datas=new ArrayList<>();
        //row为现在的小时，把一个小时的数据存储在一列
        Put put=new Put(Bytes.toBytes(sensorCode+row));
        put.addColumn(Bytes.toBytes("sensorInfo"),Bytes.toBytes("sensorType"),Bytes.toBytes(sensorCode));
        put.addColumn(Bytes.toBytes("originalData"),Bytes.toBytes(col),Bytes.toBytes(value));
        datas.add(put);
        List<Mutation> result=hbaseService.saveOrUpdate(tableName,datas);
        logger.info(result.toString());
        if(result.size()<1){
            logger.error("hbase写入失败");
        }
    }


    private void kafkaHandeler(Channel channel,String message,Long time){
        String channelId=ReceiveSensorDataHandler.getChannelId(channel);
        String stationCode=Utils.getKeyByValue(CatcheData.stationCodeToChannelMap,channelId);
        String sensorCode=message.substring(0,2);
        String topic=kafkaPrefix+stationCode;
        Sensor sensor=CatcheData.getSensorBySensorCode(channelId,sensorCode);
        String msg=time+":"+sensor.getCode()+":"+getValue(channelId,message);
        logger.info("kafkatopic:["+topic+"] msg:["+msg+"]");
        kafkaTemplate.send(topic,msg);
    }


    private String getValue(String channelId, String message) {
        String sensorCode=message.substring(0,2);
        Sensor sensor=CatcheData.getSensorBySensorCode(channelId,sensorCode);
        String value=message.replace(" ","").substring(6,10);
        if (value==null){
            return "0.00";
        }
        Double number=Integer.parseInt(value,16)*sensor.getResolution();
        return String.format("%.2f",number);
    }

}
