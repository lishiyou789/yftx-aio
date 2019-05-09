package com.health2world.aio.config;

/**
 * @description: 测量数据常亮类
 * @author: syj
 * @create: 2019-02-18 14:43
 * @version: 1.0
 **/
public class MedicalConstant {

    /*************************response code**************************************/
    public final static String CODE_3001 = "3001";
    public final static String MSG_3001 = "测量数据为空";

    public final static String CODE_3002 = "3002";
    public final static String MSG_3002 = "检测类别code不正确";

    public final static String CODE_3003 = "3003";
    public final static String MSG_3003 = "检测项目code不正确";

    public final static String CODE_3004 = "3004";
    public final static String MSG_3004 = "测量数据上传心电图片异常";


    /***************************检测类别CODE**************************************************/


    /**
     * 检测项目：
     *    旧规则-  0血压 1体温 2血氧 3血糖 4心率 5尿常规 6血脂 7心电十二导 8炎症 9心肌
     *
     *    新规则-  0血压 1体温 2血氧 3血糖 4尿常规 5血脂 6心电十二导 7炎症 8心肌 9尿酸 10白细胞(WBC) 11血红蛋白 12糖化血红蛋白
     */
    /**
     * 血压
     */
    public final static String NIBP = "0";
    /**
     * 体温
     */
    public final static String TEMP = "1";
    /**
     * 血氧
     */
    public final static String BLOOD_OXYGEN = "2";
    /**
     * 血糖
     */
    public final static String BLOOD_SUGAR = "3";
    /**
     * 尿常规
     */
    public final static String URINE = "4";
    /**
     * 血脂
     */
    public final static String BLOOD_FAT = "5";

    /**
     * 十二导心电
     */
    public final static String LEAD_ECG = "6";
    /**
     * 炎症
     */
    public final static String INFLAMMATION = "7";
    /**
     * 心肌
     */
    public final static String MYOCARDIUMCARDIAC = "8";
    /**
     * 尿酸
     */
    public final static String UA = "9";
    /**
     * 白细胞
     */
    public final static String WBC = "10";
    /**
     * 血红蛋白
     */
    public final static String HB = "11";

    /**
     * 糖化血红蛋白
     */
    public final static String GHB = "12";
    /**
     * 身高
     */
    public final static String HEI = "13";
    /**
     * 体重
     */
    public final static String WEI = "14";
    /**
     * 总胆固醇
     */
    public final static String CHOL = "15";

    /****************************检测项目CODE***********************************************************/
    /**
     * 体温
     */
    public final static String TEMPERATURE = "temperature";
    /**
     * 收缩压
     */
    public final static String SBP = "sbp";
    /**
     * 舒张压
     */
    public final static String DBP = "dbp";
    /**
     * 血压脉率
     */
    public final static String BLOOD_PR = "blood_pr";
    /**
     * 血糖空腹
     */
    public final static String SUGAR = "sugar";
    /**
     * 血糖餐后(2小时)
     */
    public final static String SUGAR_PBG = "sugar_pbg";

    /**
     * 血氧
     */
    public final static String OXYGEN_SPO2 = "oxygen_spo2";
    /**
     * 脉率
     */
    public final static String OXYGEN_PR = "oxygen_pr";
    /**
     * 心率
     */
    public final static String ECG_HR = "ecg_hr";
    /**
     * 身高
     */
    public final static String HEIGHT = "height";
    /**
     * 体重
     */
    public final static String WEIGHT = "weight";

    /*******************************血脂begin*********************************************/
    /**
     * 总胆固醇
     */
    public final static String FAT_FLIPIDSCHOL = "fat_flipidschol";
    /**
     * 甘油三酯
     */
    public final static String FAT_FLIPIDSTRIG = "fat_flipidstrig";
    /**
     * 高密度脂蛋白
     */
    public final static String FAT_FLIPIDSHDL = "fat_flipidshdl";
    /**
     * 低密度脂蛋白
     */
    public final static String FAT_FLIPIDSLDL = "fat_flipidsldl";
    /**
     * 葡萄糖
     */
    public final static String FAT_FLIPIDSGLU = "glu_tree";
    /**
     * 极低密度脂蛋白胆固醇
     */
    public final static String VLDL_C = "vldl_c";
    /**
     * 动脉硬化指数
     */
    public final static String AI = "ai";
    /**
     * 冠心病危险指数
     */
    public final static String R_CHD = "r_chd";

    /*******************************血脂end*********************************************/


    /*******************************炎症begin***************************************************/
    /**
     * c反应蛋白-成人
     */
    public final static String INDLAMMATION_CRP = "indlammation_crp";
    /**
     * C反应蛋白-儿童
     */
    public final static String INDLAMMATION_CRP_CHILD = "indlammation_crp_child";
    /**
     * 降钙素原
     */
    public final static String INDLAMMATION_PCT = "indlammation_pct";
    /**
     * 超敏CRP
     */
    public final static String INDLAMMATION_HSCRP = "indlammation_hscrp";

    /**
     * 血淀粉样蛋白A
     */
    public final static String INDLAMMATION_SAA = "indlammation_saa";


    /*******************************炎症end***************************************************/


    /*******************************心肌begin***************************************************/
    /**
     * 心肌肌钙蛋白
     */
    public final static String MYOCARDIUM_CTNI = "myocardium_ctnl";
    /**
     * 肌酸激酶同工酶
     */
    public final static String MYOCARDIUM_CKMB = "myocardium_ckmb";
    /**
     * 肌红蛋白
     */
    public final static String MYOCARDIUM_MYO = "myocardium_myo";
    /*******************************心肌end***************************************************/


    /**
     * 尿酸-男
     */
    public final static String MD_URIC_ACID_MAN = "md_uric_acid_man";
    /**
     * 尿酸-女
     */
    public final static String MD_URIC_ACID_WOMAN = "md_uric_acid_woman";


    /**********************************心电*******************************************/
    /**
     * 心电波形采样率
     */
    public final static String SAMPLE = "sample";
    /**
     * +0.5mv对应的数值
     */
    public final static String P05 = "p05";
    /**
     * -0.5mv对应的数值
     */
    public final static String N05 = "n05";

    public final static String RESPRR = "respRr";

    public final static String ANAL = "checkResult";
    /**
     * 波形持续时间
     */
    public final static String DURATION = "duration";
    /**
     * 心电I波形值，还原波形值看文档最后的备注
     */
    public final static String ECG_I = "ecg_i";
    /**
     * 心电II波形值
     */
    public final static String ECG_II = "ecg_ii";
    /**
     * 心电III波形值
     */
    public final static String ECG_III = "ecg_iii";
    /**
     * 心电aVR波形值
     */
    public final static String ECG_AVR = "ecg_avr";
    /**
     * 心电aVF波形值
     */
    public final static String ECG_AVF = "ecg_avf";
    /**
     * 心电aVL波形值
     */
    public final static String ECG_AVL = "ecg_avl";
    /**
     * 心电V1波形
     */
    public final static String ECG_V1 = "ecg_v1";
    /**
     * 心电V2波形
     */
    public final static String ECG_V2 = "ecg_v2";

    public final static String ECG_V3 = "ecg_v3";

    public final static String ECG_V4 = "ecg_v4";

    public final static String ECG_V5 = "ecg_v5";

    public final static String ECG_V6 = "ecg_v6";
    /**
     * 心电图文件路径
     */
    public final static String FILE_PATH = "file_path";
    /**
     * 心电图文件格式
     */
    public final static String FILE_FORMAT = "file_format";
    /**
     * 心电结论
     */
    public final static String CHECK_RESULT = "checkResult";
    /**
     * 波速（mm/s）
     */
    public final static String WAVE_SPEED = "wave_speed";
    /**
     * 波形增益
     */
    public final static String WAVE_GAIN = "wave_gain";

    /**********************************心电END*******************************************/

    /**********************************尿常规START*******************************************/
    public final static String URINE_PH = "urine_ph";
    public final static String URINE_UBG = "urine_ubg";
    public final static String URINE_BLD = "urine_bld";
    public final static String URINE_PRO = "urine_pro";
    public final static String URINE_KET = "urine_ket";
    public final static String URINE_NIT = "urine_nit";
    public final static String URINE_GLU = "urine_glu";
    public final static String URINE_BIL = "urine_bil";
    public final static String URINE_LEU = "urine_leu";
    public final static String URINE_SG = "urine_sg";
    public final static String URINE_VC = "urine_vc";
    public final static String URINE_CR = "urine_cr";
    public final static String URINE_CA = "urine_ca";
    public final static String URINE_MA = "urine_ma";

    /**********************************尿常规END*******************************************/

    /**********************************白细胞START*******************************************/
    public final static String WBC_WBC = "wbc";
    public final static String WBC_LYM = "wbc_lym";
    public final static String WBC_MON = "wbc_mon";
    public final static String WBC_NEU = "wbc_neu";
    public final static String WBC_EOS = "wbc_eos";
    public final static String WBC_BAS = "wbc_bas";
    /**********************************白细胞END*******************************************/
    /**********************************血红蛋白START*******************************************/
    // 血红蛋白（g/L）血红蛋白-男
    public final static String ASSXHDB_MAN = "assxhdb_man";

    // 血红蛋白（g/L）血红蛋白-女
    public final static String ASSXHDB_WOMAN = "assxhdb_woman";
    // 红细胞压积值（%）-男
    public final static String ASSXHCT_MAN = "assxhct_man";
    // 红细胞压积值（%）-女
    public final static String ASSXHCT_WOMAN = "assxhct_woman";
    /**********************************血红蛋白END*******************************************/
    /**********************************糖化血红蛋白START*******************************************/
    public final static String GHB_NGSP = "ghb_ngsp";
    public final static String GHB_IFCC = "ghb_ifcc";
    public final static String GHB_EAG = "ghb_eag";
    /**********************************糖化血红蛋白END*******************************************/

    /**
     * 总胆固醇
     */
    public final static String  CHOLESTEROL ="cholesterol";

    /**********************************检测模式*******************************************/
    /**
     * 检测模式:未知
     */
    public final static Integer CHECK_MODE_UNKOWN = 0;
    /**
     * 检测模式:医用设备
     */
    public final static Integer CHECK_MODE_MEDICAL = 1;
    /**
     * 检测模式:家用设备
     */
    public final static Integer CHECK_MODE_HOUSEHOLD = 2;
    /**
     * 检测模式:手动输入
     */
    public final static Integer CHECK_MODE_HAND = 3;
}
