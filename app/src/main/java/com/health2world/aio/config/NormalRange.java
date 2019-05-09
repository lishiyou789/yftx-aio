package com.health2world.aio.config;

/**
 * Created by lishiyou on 2018/10/25 0025.
 */

public class NormalRange {
    //血压
    public static final int NIBP_SBP_MIN = 90;
    public static final int NIBP_SBP_MAX = 139;
    public static final int NIBP_DBP_MIN = 60;
    public static final int NIBP_DBP_MAX = 89;
    public static final int NIBP_NIBPR_MIN = 60;
    public static final int NIBP_NIBPR_MAX = 100;

    //血氧
    public static final int SPO2_MIN = 94;
    public static final int SPO2_MAX = 100;
    public static final int SPO2_PR_MIN = 60;
    public static final int SPO2_PR_MAX = 100;

    //心电 心率
    public static final int ECG_HR_MIN = 60;
    public static final int ECG_HR_MAX = 100;

    //脉率
    public static final int PR_MIN = 60;
    public static final int PR_MAX = 100;

    //体温
    public static final float TEMP_MIN = 34.7F;
    public static final float TEMP_MAX = 37.8F;

    //血糖 餐前餐后(mmol/L)
    public static final float GLU_BEFORE_MIN = 3.9F;
    public static final float GLU_BEFORE_MAX = 6.1F;
    public static final float GLU_AFTER_MIN = 3.9F;
    public static final float GLU_AFTER_MAX = 7.8F;

    //尿酸(mmol/L)
    public static final int UA_MAN_MIN = 202;
    public static final int UA_MAN_MAX = 416;
    public static final int UA_WOMAN_MIN = 142;
    public static final int UA_WOMAN_MAX = 339;

    //总胆固醇(mmol/L)
    public static final float CHOL_MIN = 3.12F;
    public static final float CHOL_MAX = 5.18F;

    //尿常规
    public static final float URINE_PH_MIN = 4.5F;
    public static final float URINE_PH_MAX = 7.9F;
    public static final float URINE_SG_MIN = 1.015F;
    public static final float URINE_SG_MAX = 1.025F;

    //白细胞 *10^9/L
    public static final float WBC_MIN = 3.5F;
    public static final float WBC_MAX = 9.5F;
    //淋巴细胞
    public static final float WBC_LYM_MIN = 1.1F;
    public static final float WBC_LYM_MAX = 3.2F;
    //单核细胞
    public static final float WBC_MON_MIN = 0.1F;
    public static final float WBC_MON_MAX = 0.6F;
    //中性粒细胞
    public static final float WBC_NEU_MIN = 1.8F;
    public static final float WBC_NEU_MAX = 6.3F;
    //嗜酸粒细胞
    public static final float WBC_EOS_MIN = 0.02F;
    public static final float WBC_EOS_MAX = 0.52F;
    //嗜碱粒细胞
    public static final float WBC_BAS_MIN = 0F;
    public static final float WBC_BAS_MAX = 0.06F;


    //CRP
    public static final float CRP_MIN = 0F;
    public static final float CRP_MAX = 10.0F;
    //Hs-CRP
    public static final float HSCRP_MIN = 0F;
    public static final float HSCRP_MAX = 3.0F;
    //SAA
    public static final float SAA_MIN = 0F;
    public static final float SAA_MAX = 10.0F;
    //降钙素原 ng/mL
    public static final float PCT_MIN = 0F;
    public static final float PCT_MAX = 0.05F;

    //血红蛋白(g/L)
    public static final int HB_MAN_MIN = 120;
    public static final int HB_MAN_MAX = 160;
    public static final int HB_WOMAN_MIN = 110;
    public static final int HB_WOMAN_MAX = 150;

    //压积值(%)
    public static final int HCT_MAN_MIN = 40;
    public static final int HCT_MAN_MAX = 50;
    public static final int HCT_WOMAN_MIN = 37;
    public static final int HCT_WOMAN_MAX = 48;

    //心肌三项
    public static final float CTNI_MAX = 1.0f;
    public static final float CK_MB_MAX = 5.0f;
    public static final float MYO_MAX = 0.0f;



    //血脂八项（mmol/L）
    /**
     * 高密度脂蛋白胆固醇（HDL-C）```
     * 总胆固醇（TC）```
     * 甘油三酯（TG）```
     * 低密度脂蛋白胆固醇（LDL-C）```
     * 极低密度脂蛋白胆固醇（VLDL-C）```
     * 动脉硬化指数（AI）
     * 冠心病危险指数（R-CHD）
     */
    //总胆固醇
    public static final float BLOOD_TC_MIN = 3.12F;
    public static final float BLOOD_TC_MAX = 5.18F;
    //甘油三酯
    public static final float BLOOD_TG_MIN = 0.44F;
    public static final float BLOOD_TG_MAX = 1.70F;
    //高密度脂蛋白
    public static final float BLOOD_HDLC_MIN = 1.00F;
    public static final float BLOOD_HDLC_MAX = 1.90F;
    //低密度脂蛋白
    public static final float BLOOD_LDLC_MIN = 0.00F;
    public static final float BLOOD_LDLC_MAX = 3.10F;
    //极低密度脂蛋白
    public static final float BLOOD_VLDL_MIN = 0.21F;
    public static final float BLOOD_VLDL_MAX = 0.78F;
    //动脉硬化指数
    public static final float BLOOD_AI_MAX = 4.0F;
    //冠心病危险指数
    public static final float BLOOD_RCHD_MAX = 4.5F;

    //糖化血红蛋白
    public static final float GHB_NGSP_MIN = 4.3F;


    //血红蛋白 男性：120-160g/L  女性：110-150g/L
    //红细胞压积值 男：40～%50 女：37%～%45
    public static int checkHBResult(int sex, String value) {
        //女性
        if (sex == 0) {
            try {
                if (Integer.valueOf(value) > HB_WOMAN_MAX)
                    return 2;
                if (Integer.valueOf(value) < HB_WOMAN_MIN)
                    return 1;
            } catch (NumberFormatException e) {

            }
        } else if (sex == 1) {
            try {
                if (Integer.valueOf(value) > HB_MAN_MAX)
                    return 2;
                if (Integer.valueOf(value) < HB_MAN_MIN)
                    return 1;
            } catch (NumberFormatException e) {

            }
        } else {
            try {
                if (Integer.valueOf(value) > HB_MAN_MAX)
                    return 2;
                if (Integer.valueOf(value) < HB_WOMAN_MIN)
                    return 1;
            } catch (NumberFormatException e) {

            }
        }
        return 0;
    }

    public static int checkHCTResult(int sex, String value) {
        //女性
        if (sex == 0) {
            try {
                if (Integer.valueOf(value) > HCT_WOMAN_MAX)
                    return 2;
                if (Integer.valueOf(value) < HCT_WOMAN_MIN)
                    return 1;
            } catch (NumberFormatException e) {

            }
        } else if (sex == 1) {
            try {
                if (Integer.valueOf(value) > HCT_MAN_MAX)
                    return 2;
                if (Integer.valueOf(value) < HCT_MAN_MIN)
                    return 1;
            } catch (NumberFormatException e) {

            }
        } else {
            try {
                if (Integer.valueOf(value) > HCT_MAN_MAX)
                    return 2;
                if (Integer.valueOf(value) < HCT_WOMAN_MIN)
                    return 1;
            } catch (NumberFormatException e) {

            }
        }
        return 0;
    }

    public static int checkGHBResult(int sex, String value) {
        //女性
        if (sex == 0) {
            try {
                if (Integer.valueOf(value) > HB_WOMAN_MAX)
                    return 2;
                if (Integer.valueOf(value) < HB_WOMAN_MIN)
                    return 1;
            } catch (NumberFormatException e) {

            }
        } else if (sex == 1) {
            try {
                if (Integer.valueOf(value) > HB_MAN_MAX)
                    return 2;
                if (Integer.valueOf(value) < HB_MAN_MIN)
                    return 1;
            } catch (NumberFormatException e) {

            }
        } else {
            try {
                if (Integer.valueOf(value) > HB_MAN_MAX)
                    return 2;
                if (Integer.valueOf(value) < HB_WOMAN_MIN)
                    return 1;
            } catch (NumberFormatException e) {

            }
        }
        return 0;
    }

    /**
     * 检查测量值，大于的话返回↑
     *
     * @param value 测量值
     * @param min   最小
     * @param max   最大
     * @return 大小字符
     */
    public static String checkValue(int value, int min, int max) {
        if (min == 0 && max == 0)
            return " ";
        if (value > max)
            return "↑ ";
        if (value < min)
            return "↓ ";
        return " ";
    }

    /**
     * 检查测量值，大于的话返回↑
     *
     * @param value 测量值
     * @param min   最小
     * @param max   最大
     * @return 大小字符
     */
    public static String checkValue(float value, float min, float max) {
        if (value > max)
            return "↑ ";
        if (value < min)
            return "↓ ";
        return " ";
    }
}
