package com.konsung.constant;

/**
 * 配置项的常量
 */

public class Configuration {
    // 设备配置信息
//    public static final int DEVICE_CONFIG = 0x9F;
//    public static final int DEVICE_CONFIG = 0x7FFF;
//    public static final int DEVICE_CONFIG = 0x27e041;
    // 无效趋势值
    public static final int INVALID_DATA = -1000;
    // 网络数据的放大倍数，趋势数据统一为10000
    public static final int TREND_FACTOR = 100;
    public static final int HR_ALARM_HIGH = 160;
    public static final int HR_ALARM_LOW = 40;
    //无效小数值
    public static final float INVALID_DECIMAL_VALUE = -0.1f;
    public static final String APP_CONFIG = "app_config"; //首先项的参数
    public static final String URINETYPE = "urine_type"; //记录尿常规的配置
    public static final String FHR_SELECTED = "fhr_selected";
    public static final String SYS_CONFIG = "sys_config";//首先项的参数
    public static final String BLOGIC_INFO_KEY = "13remixcheckinf";
    public static final byte NET_BLOGIC_CHECK_INFO = (byte) 0x80;//生化仪命令字

    /**
     * 标记btn选中的方法
     */
    public enum BtnFlag {
        lift(0),
        //餐前
        middle(1),
        //随机
        right(2); //餐后
        private int nCode;  // 定义私有变量

        /**
         * 构造函数，枚举类型只能为私有
         *
         * @param code 常量值
         */
        BtnFlag(int code) {
            this.nCode = code;
        }


        public int getCode() {
            return nCode;
        }
    }
}
