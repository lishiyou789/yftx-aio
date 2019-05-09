package com.konsung.constant;

/**
 * 参数类型
 * 这些参数与AppDevice保持一致
 * 参数数值由协议指定
 * 目前只写了程序中使用到的部分趋势子参数的值
 * 后期可以在本类中增加相应的子参数
 * 这些值必须与协议保持一致
 *
 * @author ouyangfan
 * @version 0.0.1
 */

public class KParamType {

    public static final int ECG = 0;
    public static final int ECG_I = 1;
    public static final int ECG_II = 2;
    public static final int ECG_III = 3;
    public static final int ECG_AVR = 4;
    public static final int ECG_AVL = 5;
    public static final int ECG_AVF = 6;
    public static final int ECG_V1 = 7;
    public static final int ECG_V2 = 8;
    public static final int ECG_V3 = 9;
    public static final int ECG_V4 = 10;
    public static final int ECG_V5 = 11;
    public static final int ECG_V6 = 12;

    // ECG趋势子参数
    public static final int ECG_HR = 14;
    public static final int ECG_PVC = 15;
    public static final int ECG_ST = 18;


    // resp趋势子参数
    public static final int RESP_RR = 102;

    // spo2趋势子参数
    public static final int SPO2_WAVE = 201;
    public static final int SPO2_TREND = 202;
    public static final int SPO2_PR = 203;

    // temp趋势子参数
    public static final int TEMP_T1 = 301;
    public static final int TEMP_T2 = 302;
    public static final int TEMP_TD = 303;

    // irTemp趋势子参数
    public static final int IRTEMP_TREND = 401; //红外耳温

    // nibp趋势子参数
    public static final int NIBP_SYS = 501;
    public static final int NIBP_DIA = 502;
    public static final int NIBP_MAP = 503;
    public static final int NIBP_PR = 504;

    // bloodGlu趋势子参数
    public static final int BLOODGLU_BEFORE_MEAL = 901;
    public static final int BLOODGLU_AFTER_MEAL = 902;
    //尿酸
    public static final int URICACID_TREND = 1001;
    //总胆固醇
    public static final int CHOLESTEROL_TREND = 1101;
    // urineRt趋势子参数
    public static final int URINERT_LEU = 1201;
    public static final int URINERT_NIT = 1202;
    public static final int URINERT_UBG = 1203;
    public static final int URINERT_PRO = 1204;
    public static final int URINERT_PH = 1205;
    public static final int URINERT_BLD = 1206;
    public static final int URINERT_SG = 1207;
    public static final int URINERT_BIL = 1208;
    public static final int URINERT_KET = 1209;
    public static final int URINERT_GLU = 1210;
    public static final int URINERT_VC = 1211;
    public static final int URINERT_ALB = 1212;
    public static final int URINERT_ASC = 1213;
    public static final int URINERT_CRE = 1214;
    public static final int URINERT_CA = 1215;

    //白细胞 bloodWbc趋势子参数
    public static final int BLOOD_WBC = 1401;

    public static final int BLOOD_HGB = 1402;
    public static final int BLOOD_HCT = 1403;
    public static final int BLOOD_LYM = 1404;
    public static final int BLOOD_MON = 1405;
    public static final int BLOOD_NEU = 1406;
    public static final int BLOOD_EOS = 1407;
    public static final int BLOOD_BAS = 1408;

    //血脂四项
    public static final int LIPIDS_TC = 1501;//总胆固醇
    public static final int LIPIDS_TG = 1502;//甘油三酯
    public static final int LIPIDS_HDL = 1503;//高密度脂蛋白
    public static final int LIPIDS_LDL = 1504;//低密度脂蛋白

    //糖化血红蛋白
    public static final int HBA1C_NGSP = 1601;
    public static final int HBA1C_IFCC = 1602;
    public static final int HBA1C_EAG = 1603;

    public static final short START_ECG_DIAGNOSE = (short) 0X15; //心电启动测量
    public static final int ECG_CONNECTION_STATUS = 0x10; //心电连接状态码标示
    public static final int ECG_ABNORMAL = 0x11; //心率失常标示

    //干式免疫分析仪之炎症检测
    public static final int FIA_PCT = 1901;//hs-CRP
    public static final int FIA_HS_CRP = 1902;//hs-CRP
    public static final int FIA_CRP = 1903;//CRP
    public static final int FIA_SAA = 1904;//SAA

    //干式免疫分析仪之心肌三项检测
    public static final int FIA_CTNI = 1911;//
    public static final int FIA_CK_MB = 1912;//
    public static final int FIA_MYO = 1913;//
    public static final int FIA_H_FABP = 1914;//
    public static final int FIA_NT_PROBNP = 1915;//
    public static final int FIA_LP_PLA2 = 1916;//
    public static final int FIA_D_DIMER = 1921;//
    public static final int FIA_D_AMH = 1922;//

    public static final int WEIGHT = 2001;
    public static final int HEIGHT = 2002;

    //胎心1
    public static final int FHR_EAG = 1701;
    //胎心2
    public static final int FHR2 = 1702;
    //宫颈
    public static final int FHR_TOCO = 1703;
}
