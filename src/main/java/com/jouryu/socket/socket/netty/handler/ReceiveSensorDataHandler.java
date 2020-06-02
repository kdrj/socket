package com.jouryu.socket.socket.netty.handler;

import com.jouryu.socket.socket.common.CatcheData;
import com.jouryu.socket.socket.common.CommondEnumType;
import com.jouryu.socket.socket.util.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @program: socket
 * @description: 处理数据
 * @author: kdrj
 * @date: 2019-09-06 16:32
 **/
@Component
@Qualifier("receiveSensorDataHandler")
public class ReceiveSensorDataHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final Logger logger=LoggerFactory.getLogger(ReceiveSensorDataHandler.class);

    @Autowired
    @Qualifier("sensorHandler")
    private SensorHandler sensorHandler;
    @Override
    public void handlerAdded(ChannelHandlerContext context){
        Channel channel=context.channel();
        CatcheData.channelIdToChannelMap.put(getChannelId(channel), channel);
        logger.info("传感器"+getRemoteAddress(channel)+"已加入队列");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("传感器"+getRemoteAddress(ctx.channel())+"连接成功");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] msg) throws Exception {
        Channel channel=channelHandlerContext.channel();
        String message=Utils.bytesToHexString(msg);
        if(message.startsWith(CommondEnumType.REGISTER.getCommond())){
            logger.info("传感器"+getRemoteAddress(channel)+"发来注册信息");
            registerHandler(channel,message);
        } else if (message.length()>=14){
            logger.info("==============="+message);
            sensorHandler.process(channelHandlerContext,message);
//            String stationCode=Utils.getKeyByValue(CatcheData.stationCodeToChannelMap,getChannelId(channel));
//            sensorHandler.process(channelHandlerContext,message);
//            if (stationCode.equals("0003")){
//
//            }else {
//
//            }
        }

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel=ctx.channel();
        String stationCode=Utils.getKeyByValue(CatcheData.stationCodeToChannelMap,getChannelId(channel));
        //清除stationMap对应的连接映射
        CatcheData.stationCodeToChannelMap.remove(stationCode);
        CatcheData.channelIdToChannelMap.remove(getChannelId(channel));
        logger.info("传感器(" + getRemoteAddress(channel) + ")[" + CatcheData.getStationNameByCode(stationCode) + "]已退出处理队列");
        logger.info("当前传感器列表: [" + String.join(",", CatcheData.getCurrentMonitorList()) + "]");
    }
    /**
     * 错误状态后触发
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String channelId = getChannelId(ctx.channel());
        cause.printStackTrace();
        logger.warn(channelId+"关闭");
        if(ctx.channel().isActive())ctx.close();
        // ctx.close();
    }
    private void registerHandler(Channel channel, String message) {
        String stationCode=message.substring(2,6);
        String channelId=getChannelId(channel);
        logger.info(stationCode.equals("")+"");
        logger.info((CatcheData.containsStationCode(stationCode))+"");
        logger.info(CatcheData.stationCodeToChannelMap.containsKey(stationCode)+"");
        if (!stationCode.equals("")&&CatcheData.containsStationCode(stationCode)&&!CatcheData.stationCodeToChannelMap.containsKey(stationCode)){
            //网客户端发送注册成功的消息
            String command=CommondEnumType.REGISTER_SUCCESS.getCommond();
            channel.writeAndFlush(Utils.hexStringToBytes(command));
            CatcheData.stationCodeToChannelMap.put(stationCode,channelId);
            logger.info("当前传感器列表: [" + String.join(",", CatcheData.getCurrentMonitorList()) + "]");
            // 判断是否是门楼水库站点
            if (stationCode.equals("0003")) {
                try {
                    Thread.sleep(1000); // 等待1000毫秒后, 再发送一条获取IDSN的指令, 这里为了防止粘包
                    channel.writeAndFlush(Utils.hexStringToBytes(CommondEnumType.GET_IDSN.getCommond()));
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    public static String getChannelId(Channel channel){
        return channel.id()+"";
    }
    public static String getRemoteAddress(Channel channel){
        return channel.remoteAddress().toString();
    }
}
