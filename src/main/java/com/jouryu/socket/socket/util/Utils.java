package com.jouryu.socket.socket.util;

import java.util.Map;

/**
 * @program: socket
 * @description: 工具类
 * @author: kdrj
 * @date: 2019-09-07 09:56
 **/
public class Utils {

    public static String bytesToHexString(byte[] bytes){
        StringBuilder builder=new StringBuilder();
        if(bytes==null|| bytes.length<0){
            return null;
        }
        for (int i=0;i<bytes.length;i++){
            int v = bytes[i] & 0xFF;
            String hv=Integer.toHexString(v);
            if(hv.length()<2){
                builder.append(0);
            }
            builder.append(hv);

        }
        return builder.toString();
    }

    public static <T, I> T getKeyByValue(Map<T, I> map, I value){
        T ret=null;
        for (T key:map.keySet()) {
            if(map.get(key).equals(value)){
                ret=key;
            }
        }

        return ret;
    }

    public static byte[] hexStringToBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    public static String getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int high,low;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }

        }
        high=CRC%256;
        low=CRC/256;
        int res=high*256+low;
        return Integer.toHexString(res);
    }
}
