package com.health2world.aio.config;

/**
 * 康尚App常量类 相关字段的定义
 * Created by lisiyou on 2018/7/3 0003.
 */

public class KonsungConfig {

    //康尚家庭医生服务器地址
    public static final String PUBLIC_HEALTH_URL = "public_health_url";
    //康尚家庭医生默认服务器地址（丹阳）
    public static final String DEFAULT_PUBLIC_HEALTH_URL = "";
//    public static final String DEFAULT_PUBLIC_HEALTH_URL = "http://222.186.234.9:8787";
    //测量库应用程序包名
    public static final String APPDEVICE_PACKAGE = "org.qtproject.qt5.android.bindings";
    //厂家维护应用程序包名
    public static final String FACTORY_MAIN_PACKAGE = "com.konsung.factorymaintain";
    //公共卫生应用程序包名
    public static final String PUBLIC_HEALTH_PACKAGE = "com.konsung.publichealth";
    //文件管理程序包名
    public static final String FILE_MANAGER_PACKAGE = "com.mediatek.filemanager";
    //DeviceManager包名
    public static final String DEVICE_MANAGER_PACKAGE = "com.konsung.devicemanager";


    /**
     * 内容提供者协议路径
     */
    //设备号
    public static final String URI_DEVICE_NO = "content://com.konsung.factorymaintain/Devices";
    //康尚服务器地址
    public static final String URI_SERVER_URL = "content://com.konsung.softwaresetting/ServiceAddress";
    //康尚测量项配置
    public static final String URI_DEVICE_CONFIG = "content://com.konsung.factorymaintain/DevicesConfig";

}
