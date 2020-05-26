package com.jouryu.socket.socket.common;

public enum CommondEnumType {
    REGISTER("7f","register"),
    REGISTER_SUCCESS("7f0000","register_success"),
    READ("03","read"),
    CLOSE("8f", "close"), // CLOSE项主要是在本地TestTCPClient测试用, 用来模拟关闭客户端请求
    GET_IDSN("010300090004940B", "get_idsn"), // 蛙视的传感器连接后需要先获取一次idsn
    READ_DATA("01030030002445DE", "read_data"); // 获取蛙视传感器的所有数据
    private String commond;
    private String operate;

    CommondEnumType(String commond,String operate){
        this.commond=commond;
        this.operate=operate;
    }

    public static String getOperate(String commond){
        for (CommondEnumType com:CommondEnumType.values()) {
            if(com.getCommond()==commond){
                return com.operate;
            }
        }
        return null;
    }
    public String getCommond(){
        return commond;
    }
    public String getOperate(){
        return operate;
    }
}
