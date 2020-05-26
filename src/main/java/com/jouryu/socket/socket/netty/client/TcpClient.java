package com.jouryu.socket.socket.netty.client;

import com.jouryu.socket.socket.common.CatcheData;
import com.jouryu.socket.socket.common.CommondEnumType;
import com.jouryu.socket.socket.model.Sensor;
import com.jouryu.socket.socket.model.Simulator;
import com.jouryu.socket.socket.model.Station;
import com.jouryu.socket.socket.util.Utils;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @program: socket
 * @description: tcp测试客户端类
 * @author: kdrj
 * @date: 2020-09-15 09:05
 **/
@Component
@Qualifier("TcpClient")
@PropertySource(value= "classpath:/nettyserver.properties")
public class TcpClient extends AbstractTcpClient {
    private static final Logger logger = LoggerFactory.getLogger(TcpClient.class);

    private String stationCode = "0002";

    private Map<String, List<Double>> simulatorListMap = new HashMap<>();

    private Map<String, Iterator<Double>> simulatorIteratorMap = new HashMap<>();

    TcpClient() {
        super.configName = "tcpServer";
    }

    public void channelInit(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ByteArrayEncoder());
        pipeline.addLast(new ByteArrayDecoder());
        pipeline.addLast(new EchoClientHandler());
    }

    private class EchoClientHandler extends SimpleChannelInboundHandler<byte[]> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            Channel channel = ctx.channel();
            // 第一接入连接时, 给客户端发送一个注册信息, 包含水文站编号
            channel.writeAndFlush(Utils.hexStringToBytes("7f" + stationCode + "00000000"));
            logger.info("发送注册指令到"+channel.remoteAddress());
            // 初始化模拟数据
            initSimulatorData();
            logger.warn("客户端已接入连接");
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
            Channel channel = ctx.channel();
            String message = Utils.bytesToHexString(msg);

            String registerSuccessCommand = CommondEnumType.REGISTER_SUCCESS.getCommond();
            String closeCommand =CommondEnumType.CLOSE.getCommond();
            String readCommand = CommondEnumType.READ.getCommond();

            logger.warn("服务端发来数据: " + message);

            if (message.startsWith(registerSuccessCommand)) {
                logger.warn("服务端初始化信息成功");
            } else if(message.startsWith(closeCommand)) {
                logger.warn("服务端要求关闭客户");
                ctx.close();
            } else if(message.substring(2).startsWith(readCommand)) {
                logger.info("开始读了");
                String sensorCode = message.substring(0, 2);
                Iterator<Double> iterator = simulatorIteratorMap.get(sensorCode);
                if (!iterator.hasNext()) {
                    List<Double> list = simulatorListMap.get(sensorCode);
                    iterator = list.iterator();
                    simulatorIteratorMap.put(sensorCode, iterator);
                }
                Double value = iterator.next();
                byte[] responseMsg = migrateShamMsg(sensorCode, value);
                channel.writeAndFlush(responseMsg);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

        private Station getCurrentStation() {
            for (Station station : CatcheData.stations) {
                if (station.getCode().equals(stationCode)) {
                    return station;
                }
            }
            return null;
        }

        /**
         * 模拟传感器实际数据格式伪造的假数据
         * @param sensorCode
         * @param value
         * @return
         */
        private byte[] migrateShamMsg(String sensorCode, double value) {
            Station station=getCurrentStation();
            Sensor currentSensor = null;

            for (Sensor sensor : station.getSensors()) {
                if (sensor.getCode().equals(sensorCode)) {
                    currentSensor = sensor;
                }
            }
            StringBuffer sb = new StringBuffer();
            sb.append(sensorCode);
            sb.append("0302");
            int number = (int) (value / currentSensor.getResolution());
            String hexStr = Integer.toHexString(number);
            String prefix = "";
            if (hexStr.length() < 4) {
                prefix = String.join("", Collections.nCopies(4 - hexStr.length(), "0"));
            }
            sb.append(prefix);
            sb.append(hexStr);
            sb.append("809d"); // 随便写的4位
            return Utils.hexStringToBytes(sb.toString());
        }

        private void initSimulatorData() {
            List<Double> flowList = new ArrayList<>();
            List<Double> phList = new ArrayList<>();
            List<Double> dogList = new ArrayList<>();
            List<Double> codmnList = new ArrayList<>();
            List<Double> nh3hList = new ArrayList<>();

            for (Simulator simulator : CatcheData.simulatorList) {
                flowList.add(simulator.getFlow());
                phList.add(simulator.getPh());
                dogList.add(simulator.getDog());
                codmnList.add(simulator.getCodmn());
                nh3hList.add(simulator.getNh3h());
            }
            simulatorListMap.put("01", flowList);
            simulatorListMap.put("02", phList);
            simulatorListMap.put("03", dogList);
            simulatorListMap.put("04", codmnList);
            simulatorListMap.put("05", nh3hList);

            simulatorIteratorMap.put("01", flowList.iterator());
            simulatorIteratorMap.put("02", phList.iterator());
            simulatorIteratorMap.put("03", dogList.iterator());
            simulatorIteratorMap.put("04", codmnList.iterator());
            simulatorIteratorMap.put("05", nh3hList.iterator());
        }
    }
}
