package com.konsung.constant;

/**
 * 与appDeivce通信网络常量
 */

public class NetConstant {
    // 网络命令字
    public static final byte PARA_STATUS = 0x12;
    public static final byte NET_TREND = 0x51;
    public static final byte NET_WAVE = 0x52;
    public static final byte NET_POINT = 0x53;
    public static final byte NET_POINT_STATUS = 0x54;
    public static final byte NET_ECG_CONFIG = 0x21;
    public static final byte NET_RESP_CONFIG = 0x22;
    public static final byte NET_TEMP_CONFIG = 0x23;
    public static final byte NET_SPO2_CONFIG = 0x24;
    public static final byte NET_NIBP_CONFIG = 0x25;
    public static final byte NET_FFH_CONFIG = 0x28;
    //胎心监数据
    public static final byte NET_FHR_CONFIG = 0x28;

    public static final byte NET_PATIENT_CONFIG = 0x11;
    public static final byte NET_12LEAD_DIAG_RESULT = 0x60; // 12导心电结果包
    public static final byte NET_DEVICE_CONFIG = 0x70;
    // 网络端口号
    public static final int PORT = 6611;
    //打印通信
    public static final int DM_PORT = 9611;
    public static final byte PRINT_STAY_ALIVE = 0x02;
    public static final byte APP_DEVICE_HEART = 0x03; //APPdevice的心跳
    public static final byte PRINT_CONFIG = (byte) 0x80;
    public static final byte PRINT_DATA = (byte) 0x81;

}
