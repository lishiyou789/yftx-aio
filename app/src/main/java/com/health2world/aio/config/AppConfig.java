package com.health2world.aio.config;

import com.health2world.aio.BuildConfig;
import com.health2world.aio.MyApplication;

import aio.health2world.utils.DeviceUtil;

/**
 * 医服程序常量类
 * Created by lishiyou on 2017/7/20 0020.
 */

public class AppConfig {

    //是否为调试模式
    public static final boolean isDebug = BuildConfig.BUILD_TYPE.equals("debug");
    /**
     * 应用服务器默认地址
     */
    //测试版环境 http://test.health2world.com
    //融合版环境 http://fd.health2world.com
    //预发布环境 http://health.health2world.com
    //开发环境 http://192.168.18.240:8480
    //正式环境 http://machine.health2world.com
    public static final String DEFAULT_SERVER_URL = "http://test.health2world.com";
    //正式环境
    public static final String RELEASE_SERVER_URL = "http://machine.health2world.com";
    //默认测量项的开关状态 1 开启  0 关闭
    public static final String DEFAULT_MEASURE_CONFIG = "10111110000000000000000";
    //默认设备编号
    public static final String DEFAULT_DEVICE_CODE = DeviceUtil.getAndroidId(MyApplication.getInstance());


    /**
     * 程序升级  1:一体机 2:医生端 3:患者家属 4公卫App  5特定版本  6-85测量库 7-86测量库 8-DeviceManager
     */
    public static final int APP_SOFTWARE = 1;
    public static final int APP_PUBLIC_HEALTH = 4;
    public static final int APP_DEVICE_85 = 6;
    public static final int APP_DEVICE_86 = 7;
    public static final int APP_DEVICE_MANAGER = 8;
    /**
     * android 系统  85---4.4.2  86---5.1
     */
    public static final String SYSTEM_VERSION_4_4_2 = "4.4.2";
    public static final String SYSTEM_VERSION_5_1 = "5.1";

    /**
     * 极光推送相关
     */
    //扫码登录指令
    public static final int MSG_ACTION_SCAN = 0;
    //新的任务
    public static final int MSG_ACTION_TASK = 2;
    //蓝牙连接状态发生变化
    public static final int MSG_CONNECTION_CHANGED = 61;
    //蓝牙连接失败
    public static final int MSG_CONNECTION_FAIL = 62;
    //蓝牙扫描完成
    public static final int MSG_SCAN_FINISH = 63;
    //发送测量数据
    public static final int MSG_MEASURE_DATA = 64;
    //发送打印机状态
    public static final int MSG_PRINTER_STATUS = 65;
    //ping网络打印机状态
    public static final int MSG_NET_PRINTER_STATUS = 651;
    //发送DeviceManager心跳
    public static final int MSG_DEVICE_MANAGER_HB = 66;
    //更新测量数据弹窗
    public static final int MSG_UPDATE_MEASURE_DATA = 67;
    //接收到打印客户端发送过来的IP
    public static final int MSG_RECEIVER_PRINTERIP = 68;
    //应用程序被卸载替换安装的广播
    public static final int MSG_PACKAGE_REPLACED = 69;

    /**
     * 蓝牙相关
     */
    //大树血脂蓝牙设备名称（DS100A开头）
    public static final String DS100A_BLUETOOTH = "DS100A";
    public static final String DS100A_DATA_HEAD = "AA5AA5";
    //白细胞蓝牙设备名称（KS-WBC开头）
    public static final String WBC_BLUETOOTH = "KS-WBC";
    public static final String WBC_DATA_HEAD = "A555";
    //大树血脂
    public static final String DS100A_UUID_SERVICE = "0003cdd0-0000-1000-8000-00805f9b0131";
    public static final String DS100A_UUID_NOTIFY = "0003cdd1-0000-1000-8000-00805f9b0131";
    //康尚白细胞
    public static final String WBC_UUID_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static final String WBC_UUID_NOTIFY = "0000ffe4-0000-1000-8000-00805f9b34fb";
    /**
     * 打印机权限
     */
    public static String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    public static short USB_PRINT_VID = 19267;
    public static short USB_PRINT_PID = 13624;
    //已连接打印机的地址
    public static int PRINTER_PORT = 2999;
    final public static String PRINTER_USB = "UsbPrinter";
    final public static String PRINTER_WIFI = "WifiPrinter";

    /**
     * 腾讯Bugly
     */
    public static final String ID_BUGLY = "f3df4ff540";
    /**
     * 系统程序相关
     */
    //用户配置的地址字段
    public static final String SERVER_URL = "server_url";
    //请求成功的标识
    public static final String SUCCESS = "000";
    //请求成功的标识
    public static final String SUCCESS_AIO = "0000";
    //每页十条数据
    public static final int PAGE_SIZE = 7;
    //token
    public static final String TOKEN_ID = "token_id";
    //测量模式
    public static final String MEASURE_MODE = "measure_mode";
    //测量项目配置
    public static final String MEASURE_CONFIG = "measure_config";
    //测量驱动配置（支持哪种测量设备）
    public static final String DEVICE_CONFIG = "device_config";
    //记录用户选中的是哪一个设备
    public static final String DEVICE_GLU = "device_glu";
    //设备编号
    public static final String DEVICE_CODE = "device_code";
    //当前用户的身份证号码
    public static final String IDENTITY_CARD = "current_id_card";
    //医生ID
    public static final String DOCTOR_ID = "doctor_id";
    //医生手机号
    public static final String DOCTOR_PHONE = "doctor_phone";
    //机构id
    public static final String ORG_ID = "org_id";
    //医生登录密码
    public static final String DOCTOR_PWD = "doctor_pwd";
    //标记当前程序是否登录
    public static final String IS_LOGIN = "is_login";
    //厂家维护
    public static final String FACTORY_MAINTENANCE = "FACTORY_MAINTENANCE";


}
