package com.konsung.util;

import com.konsung.constant.KParamType;

/**
 * 超出测量范围工具类
 * 会上传 -10，-100
 **/
public class OverCheckUtil {

    public static final int BASE_VALUE = 100;

    //高于测量范围
    public static final int FLAG_OVER_MAX = -100; //大于标识符
    //低于测量范围
    public static final int FLAG_BELOW_MIN = -10; //小于标识符

    public static final String ABOVE = ">";
    public static final String BELOW = "<";

    /**
     * 糖化血红蛋白NGSP标准
     */
    public static final String GHB_HBA1C_NGSP_ABOVE = ">15.0";
    public static final String GHB_HBA1C_NGSP_BELOW = "<3.0";

    /**
     * 糖化血红蛋白IFCC标准
     */
    public static final String GHB_HBA1C_IFCC_ABOVE = ">140.4";
    public static final String GHB_HBA1C_IFCC_BELOW = "<9.3";
    /**
     * 糖化，平均血糖浓度
     */
    public static final String GHB_EAG_ABOVE = ">383.8";
    public static final String GHB_EAG_BELOW = "<39.4";

    /**
     * 来自：艾康血脂说明书
     * 总胆固醇
     */
    public static final String LIPIDS_CHOL_ALARM_ABOVE = ">12.93";
    public static final String LIPIDS_CHOL_ALARM_BELOW = "<2.59";
    /**
     * 甘油三酯
     */
    public static final String LIPIDS_TRIG_ALARM_ABOVE = ">7.34";
    public static final String LIPIDS_TRIG_ALARM_BELOW = "<0.51";
    /**
     * 血清高密度脂蛋白
     */
    public static final String LIPIDS_HDL_ALARM_ABOVE = ">2.59";
    public static final String LIPIDS_HDL_ALARM_BELOW = "<0.39";
    /**
     * 血清低密度脂蛋白
     */
    public static final String LIPIDS_LDL_ALARM_ABOVE = ">3.16";
    public static final String LIPIDS_LDL_ALARM_BELOW = "<1.29";

    /**
     * 获取超出上限显示值
     *
     * @param param 参数
     * @param value 上传值
     * @return 显示值
     */
    public static String getOverMaxString(int param, float value) {
        switch (param) {
            case KParamType.HBA1C_EAG:
                return GHB_EAG_ABOVE;
            case KParamType.HBA1C_NGSP:
                return GHB_HBA1C_NGSP_ABOVE;
            case KParamType.HBA1C_IFCC:
                return GHB_HBA1C_IFCC_ABOVE;
            case KParamType.LIPIDS_TG:
                return LIPIDS_TRIG_ALARM_ABOVE;
            case KParamType.LIPIDS_TC:
                return LIPIDS_CHOL_ALARM_ABOVE;
            case KParamType.LIPIDS_HDL:
                return LIPIDS_HDL_ALARM_ABOVE;
            case KParamType.LIPIDS_LDL:
                return LIPIDS_LDL_ALARM_ABOVE;
            default:
                return String.valueOf(value);
        }
    }

    /**
     * 获取低于下限显示值
     *
     * @param param 参数
     * @param value 上传值
     * @return 显示值
     */
    public static String getOverMinString(int param, float value) {
        switch (param) {
            case KParamType.HBA1C_EAG:
                return GHB_EAG_BELOW;
            case KParamType.HBA1C_NGSP:
                return GHB_HBA1C_NGSP_BELOW;
            case KParamType.HBA1C_IFCC:
                return GHB_HBA1C_IFCC_BELOW;
            case KParamType.LIPIDS_TG:
                return LIPIDS_TRIG_ALARM_BELOW;
            case KParamType.LIPIDS_TC:
                return LIPIDS_CHOL_ALARM_BELOW;
            case KParamType.LIPIDS_HDL:
                return LIPIDS_HDL_ALARM_BELOW;
            case KParamType.LIPIDS_LDL:
                return LIPIDS_LDL_ALARM_BELOW;
            default:
                return String.valueOf(value);
        }
    }

    /**
     * 获取超限的转换值
     *
     * @param param 参数
     * @param value 参数值
     * @return 超限值0
     */
    public static int getOverCheckValue(int param, String value) {
        int result = BASE_VALUE;
        switch (param) {
            case KParamType.HBA1C_EAG:
            case KParamType.HBA1C_NGSP:
            case KParamType.HBA1C_IFCC:
            case KParamType.LIPIDS_TG:
            case KParamType.LIPIDS_TC:
            case KParamType.LIPIDS_HDL:
            case KParamType.LIPIDS_LDL:
                if (value.contains(ABOVE)) {
                    result = FLAG_OVER_MAX;
                } else if (value.contains(BELOW)) {
                    result = FLAG_BELOW_MIN;
                } else {
                    try {
                        result = (int) (Float.valueOf(value) * BASE_VALUE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                try {
                    result = (int) (Float.valueOf(value) * BASE_VALUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return result;
    }
}
