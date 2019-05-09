package com.health2world.aio.printer;

import android.text.TextUtils;

import com.caysn.printerlibs.printerlibs_caysnlabel.printerlibs_caysnlabel;
import com.caysn.printerlibs.printerlibs_caysnpos.printerlibs_caysnpos;
import com.health2world.aio.config.NormalRange;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;
import com.konsung.constant.Configuration;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

import java.util.Date;

import aio.health2world.utils.DateUtil;
import aio.health2world.utils.ToastUtil;

import static com.health2world.aio.config.NormalRange.*;

/**
 * 热敏打印
 * Created by lishiyou on 2018/12/4 0004.
 * <p>
 * Updated 2018/12/13
 */

public class PrinterRM {

    //打印对象
    private static Pointer h = Pointer.NULL;
    //打印宽度
    private static int paperWidth = 384;

    /**
     * 打印居中长线
     */
    private static void printLongLine() {
        printFeedLine(1);
        printerlibs_caysnpos.INSTANCE.CaysnPos_SetAlignment(h, printerlibs_caysnpos.PosAlignment_HCenter);
        printerlibs_caysnpos.INSTANCE.CaysnPos_PrintTextInUTF8W(h, new WString("-----------------------------"));
    }

    private static void printLongLine(String string) {
        printFeedLine(1);
        printerlibs_caysnpos.INSTANCE.CaysnPos_SetAlignment(h, printerlibs_caysnpos.PosAlignment_HCenter);
        printerlibs_caysnpos.INSTANCE.CaysnPos_PrintTextInUTF8W(h, new WString("————" + string + "————"));
    }

    /**
     * 打印空行
     *
     * @param numLines 空行数
     */
    private static void printFeedLine(int numLines) {
        printerlibs_caysnpos.INSTANCE.CaysnPos_FeedLine(h, numLines);
    }

    /**
     * 打印UTF8字符
     *
     * @param str 待打印字符
     */
    private static void printUTF8(String str) {
        printerlibs_caysnpos.INSTANCE.CaysnPos_PrintTextInUTF8W(h, new WString(str));
    }

    /**
     * 设置下行文字格式
     *
     * @param alignment 格式
     */
    private static void setAlignment(int alignment) {
        printerlibs_caysnpos.INSTANCE.CaysnPos_SetAlignment(h, alignment);
    }

    /**
     * 设置下行文字起始的水平位置
     *
     * @param position 水平位置
     */
    private static void setHorizontalAbsolutePrintPosition(int position) {
        printerlibs_caysnpos.INSTANCE.CaysnPos_SetHorizontalAbsolutePrintPosition(h, position);
    }

    /**
     * 打印单行的测量项目和数据
     *
     * @param name 测量项
     * @param data 测量值
     */
    private static void printSingleLineMeasure(String name, String data) {
        printFeedLine(1);
        setAlignment(printerlibs_caysnpos.PosAlignment_Left);
        printUTF8(name);
        setHorizontalAbsolutePrintPosition(paperWidth - 12 * 22);
        printUTF8(data);
    }

    /**
     * 打印换行的测量项目和数据
     *
     * @param name 测量项
     * @param data 测量值
     */
    private static void printFeedLineMeasure(String name, String data) {
        printFeedLine(1);
        setAlignment(printerlibs_caysnpos.PosAlignment_Left);
        printUTF8(name);
        printFeedLine(1);
        setAlignment(printerlibs_caysnpos.PosAlignment_Left);
        printUTF8(data);
    }

    /**
     * 打印测量数据，包括居民信息
     * 性别 2-未知 1-男 0-女
     *
     * @param point    Pointer
     * @param resident resident
     * @param measure  measure
     */
    public static boolean printerMeasureData(Pointer point, ResidentBean resident, MeasureBean measure) {
        h = point;
        if (measure == null)
            return false;

        int status = printerlibs_caysnpos.INSTANCE.CaysnPos_QueryPrinterStatus(h, 3000);
        if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_QUERYFAILED(status)) {
            ToastUtil.showLong("打印机连接失败");
            return false;
        } else {
            if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_NOPAPER(status)) {
                ToastUtil.showLong("打印机无纸");
                return false;
            }
            if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_ERROR_OCCURED(status)) {
                ToastUtil.showLong("未知错误");
                return false;
            }
            if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_OFFLINE(status)) {
                ToastUtil.showLong("打印机离线");
                return false;
            }
        }

        {//初始化设置打印机
            printerlibs_caysnpos.INSTANCE.CaysnPos_ResetPrinter(h);
            printerlibs_caysnpos.INSTANCE.CaysnPos_SetMultiByteMode(h);
            printerlibs_caysnpos.INSTANCE.CaysnPos_SetMultiByteEncoding(h, printerlibs_caysnpos.MultiByteModeEncoding_UTF8);
            printFeedLine(2);

        }

        {//头部
//            DoctorBean doctorBean = DBManager.getInstance().getCurrentDoctor();
//            //卫生室信息
//            setAlignment(printerlibs_caysnpos.PosAlignment_HCenter);
//            printUTF8(doctorBean.getTeamName());
//            printFeedLine(1);
            setAlignment(printerlibs_caysnpos.PosAlignment_HCenter);
            printUTF8("检查/检验报告单");
            printLongLine();
            printFeedLine(1);
        }

        {//居民信息
            setAlignment(printerlibs_caysnpos.PosAlignment_Left);
            printUTF8("姓名：");
            printUTF8(resident.getName());

//            printFeedLine(1);
//            setAlignment(printerlibs_caysnpos.PosAlignment_Left);
//            printUTF8("身份证：");
//            String id = resident.getIdentityCard().replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1*****$2");
//            printUTF8(id);

            printFeedLine(1);
            setAlignment(printerlibs_caysnpos.PosAlignment_Left);
            printUTF8("年龄：");
            printUTF8(String.valueOf(resident.getAge() == -1 ? "--" : resident.getAge()));

            printFeedLine(1);
            setAlignment(printerlibs_caysnpos.PosAlignment_Left);
            printUTF8("性别：");
            String sex = "--";//性别 2-未知 1-男 0-女
            if (resident.getSexy() == 0)
                sex = "女";
            else if (resident.getSexy() == 1)
                sex = "男";
            printUTF8(sex);
        }

        {//检查结果_标题
            printLongLine();
            printFeedLine(1);
            setAlignment(printerlibs_caysnpos.PosAlignment_Left);
            printUTF8("【检查/检验结果】");
            printFeedLine(1);
            setAlignment(printerlibs_caysnpos.PosAlignment_Left);
            printUTF8("测量项");
            setHorizontalAbsolutePrintPosition(paperWidth - 12 * 23);
            printUTF8("结果(参考范围)单位");
        }

        {//检查结果
            if (!TextUtils.isEmpty(measure.getHeight()) || !TextUtils.isEmpty(measure.getWeight()) ||
                    measure.getSbp() != 0 || measure.getSpo2() != 0 ||
                    measure.getHr() != 0 || measure.getPr() != 0 ||
                    measure.getNibPr() != 0 || measure.getTemp() != 0f) {
                printLongLine("常规检查");
                //身高
                if (!TextUtils.isEmpty(measure.getHeight())) {
                    printSingleLineMeasure("身高", measure.getHeight() + "cm");
                }
                //体重
                if (!TextUtils.isEmpty(measure.getWeight())) {
                    printSingleLineMeasure("体重", measure.getWeight() + "kg");
                }
                //血压
                if (measure.getSbp() != 0) {

                    String sbp = measure.getSbp() + checkValue(measure.getSbp(),
                            NIBP_SBP_MIN, NIBP_SBP_MAX) + "(" + NIBP_SBP_MIN + "-" + NIBP_SBP_MAX + ")mmHg";
                    String dbp = measure.getDbp() + checkValue(measure.getDbp(),
                            NIBP_DBP_MIN, NIBP_DBP_MAX) + "(" + NIBP_DBP_MIN + "-" + NIBP_DBP_MAX + ")mmHg";

                    printSingleLineMeasure("收缩压", sbp);
                    printSingleLineMeasure("舒张压", dbp);
                }

                //血氧
                if (measure.getSpo2() != 0) {

                    String spo2 = measure.getSpo2() + checkValue(measure.getSpo2(),
                            SPO2_MIN, SPO2_MAX) + "(" + SPO2_MIN + "-" + SPO2_MAX + ")%";

                    printSingleLineMeasure("血氧", spo2);
                }
                //脉率，优先级：心电>血氧>血压
                if (measure.getHr() != 0 || measure.getPr() != 0 || measure.getNibPr() != 0) {
                    //0505 用优先级取脉率值
                    int pr = (measure.getHr() != 0 ? measure.getHr() : (measure.getPr() != 0 ? measure.getPr() : measure.getNibPr()));
                    String prResult = pr + checkValue(pr, PR_MIN, PR_MAX) + "(" + PR_MIN + "-" + PR_MAX + ")次/分钟";
                    printSingleLineMeasure("脉率/心率", prResult);
                }

                //体温
                if (measure.getTemp() != 0f) {
                    String temp = measure.getTemp() + checkValue(measure.getTemp(),
                            TEMP_MIN, TEMP_MAX) + "(" + TEMP_MIN + "-" + TEMP_MAX + ")℃";
                    printSingleLineMeasure("体温", temp);
                }
            }

            //血细胞分析  （血红蛋白 + 白细胞）
            if (measure.getAssxhb() != 0 || measure.getWbc() != -1.0f) {
                printLongLine("血细胞分析");

                //血红蛋白
                if (measure.getAssxhb() != 0) {
                    String hb;
                    String hct;
                    //如果性别不是女，都当作男的看
                    if (resident.getSexy() == 0) {
                        //女血红蛋白
                        hb = measure.getAssxhb() + checkValue(measure.getAssxhb(),
                                HB_WOMAN_MIN, HB_WOMAN_MAX) + "(" + HB_WOMAN_MIN + "-" + HB_WOMAN_MAX + ")g/L";
                        //女压积值
                        hct = measure.getAssxhct() + checkValue(measure.getAssxhct(),
                                HCT_WOMAN_MIN, HCT_WOMAN_MAX) + "(" + HCT_WOMAN_MIN + "-" + HCT_WOMAN_MAX + ")%";
                    } else {
                        hb = measure.getAssxhb() + checkValue(measure.getAssxhb(),
                                HB_MAN_MIN, HB_MAN_MAX) + "(" + HB_MAN_MIN + "-" + HB_MAN_MAX + ")g/L";
                        hct = measure.getAssxhct() + checkValue(measure.getAssxhct(),
                                HCT_MAN_MIN, HCT_MAN_MAX) + "(" + HCT_MAN_MIN + "-" + HCT_MAN_MAX + ")%";
                    }
                    printSingleLineMeasure("血红蛋白", hb);
                    printSingleLineMeasure("红细胞比容", hct);
                }

                //白细胞
                if (measure.getWbc() != -1.0f) {
                    //TODO 白细胞各项绝对值
                    String wbc = measure.getWbc() + checkValue(measure.getWbc(),
                            WBC_MIN, WBC_MAX) + "(" + WBC_MIN + "-" + WBC_MAX + ")*10^9/L";
                    String wbc_lym = measure.getWbc_lym() + checkValue(measure.getWbc_lym(),
                            WBC_LYM_MIN, WBC_LYM_MAX) + "(" + WBC_LYM_MIN + "-" + WBC_LYM_MAX + ")*10^9/L";
                    String wbc_mon = measure.getWbc_mon() + checkValue(measure.getWbc_mon(),
                            WBC_MON_MIN, WBC_MON_MAX) + "(" + WBC_MON_MIN + "-" + WBC_MON_MAX + ")*10^9/L";
                    String wbc_neu = measure.getWbc_neu() + checkValue(measure.getWbc_neu(),
                            WBC_NEU_MIN, WBC_NEU_MAX) + "(" + WBC_NEU_MIN + "-" + WBC_NEU_MAX + ")*10^9/L";
                    String wbc_eos = measure.getWbc_eos() + checkValue(measure.getWbc_eos(),
                            WBC_EOS_MIN, WBC_EOS_MAX) + "(" + WBC_EOS_MIN + "-" + WBC_EOS_MAX + ")*10^9/L";
                    String wbc_bas = measure.getWbc_bas() + checkValue(measure.getWbc_bas(),
                            WBC_BAS_MIN, WBC_BAS_MAX) + "(" + WBC_BAS_MIN + "-" + WBC_BAS_MAX + ")*10^9/L";

                    printFeedLineMeasure("白细胞计数", wbc);
                    printFeedLine(1);
                    setAlignment(printerlibs_caysnpos.PosAlignment_HCenter);
                    printUTF8("***以下为研究参数***");
                    printFeedLineMeasure("淋巴细胞", wbc_lym);
                    printFeedLineMeasure("单核细胞", wbc_mon);
                    printFeedLineMeasure("中性粒细胞", wbc_neu);
                    printFeedLineMeasure("嗜酸粒细胞", wbc_eos);
                    printFeedLineMeasure("嗜碱粒细胞", wbc_bas);
                    printFeedLine(1);
                }
            }

            //心电结论
//            if (!TextUtils.isEmpty(measure.getAnal())) {
//                printLongLine("心电");
//                String[] resultEgc = measure.getAnal().split(",");
//                if (resultEgc.length >= 12) {
//                    String ecgResult = resultEgc[11];
//
//                    printSingleLineMeasure("心电结论", ecgResult);
//                }
//            }

            //糖化血红蛋白
            if (!TextUtils.isEmpty(measure.getNgsp())) {
                printLongLine("糖化血红蛋白");
                String ngsp = measure.getNgsp() + "(4.3-6.0)g/L";
                String ifcc = measure.getIfcc() + "(23.5-42.1)%";
                String eag = measure.getEag() + "(76.7-125.5)ng/mL";

                printFeedLineMeasure("糖化血红蛋白", ngsp);
                printFeedLineMeasure("糖化血红蛋白", ifcc);
                printFeedLineMeasure("糖化血红蛋白", eag);
            }

            //尿常规
            if (measure.getUrinePh() > 0f) {
                printLongLine("尿常规");
                //白细胞
                String leu = measure.getUrineLeu() + "(-)";
                //亚硝酸盐
                String nit = measure.getUrineNit() + "(-)";
                //尿胆原
                String ubg = measure.getUrineUbg() + "(-)";
                //蛋白质
                String pro = measure.getUrinePro() + "(-)";
                //pH值
                String ph = measure.getUrinePh() + checkValue(
                        measure.getUrinePh(), URINE_PH_MIN, URINE_PH_MAX)
                        + "(" + URINE_PH_MIN + "-" + URINE_PH_MAX + ")";
                //比重
                //下版本代码0505,添加精确到三位小数
                //(Math.round(measure.getUrineSg()*1000)/1000)
                String sg = measure.getUrineSg() + checkValue(
                        (float) measure.getUrineSg(), URINE_SG_MIN, URINE_SG_MAX)
                        + "(" + URINE_SG_MIN + "-" + URINE_SG_MAX + ")";
                //潜血
                String bld = measure.getUrineBld() + "(-)";
                //酮体
                String ket = measure.getUrineKet() + "(-)";
                //胆红素
                String bil = measure.getUrineBil() + "(-)";
                //葡萄糖
                String glu = measure.getUrineGlu() + "(-)";
                //维生素
                String vc = measure.getUrineVc() + "(-)";
                //尿钙
                String ca = measure.getUrineCa() + "(-)";
                //微量白蛋白
                String ma = measure.getUrineMa() + "(-)";
                //肌酐
                String cr = measure.getUrineCre() + "(-)";

//                printFeedLineMeasure("白细胞", leu);
                printSingleLineMeasure("尿白细胞", leu);
//                printFeedLineMeasure("亚硝酸盐", nit);
                printSingleLineMeasure("亚硝酸盐", nit);
//                printFeedLineMeasure("尿胆原", ubg);
                printSingleLineMeasure("尿胆原", ubg);
//                printFeedLineMeasure("蛋白质", pro);
                printSingleLineMeasure("尿蛋白", pro);
//                printFeedLineMeasure("pH值", ph);
                printSingleLineMeasure("酸碱值", ph);
//                printFeedLineMeasure("比重", sg);
                printSingleLineMeasure("比重", sg);
//                printFeedLineMeasure("潜血", bld);
                printSingleLineMeasure("潜血", bld);
//                printFeedLineMeasure("酮体", ket);
                printSingleLineMeasure("酮体", ket);
//                printFeedLineMeasure("胆红素", bil);
                printSingleLineMeasure("胆红素", bil);
//                printFeedLineMeasure("葡萄糖", glu);
                printSingleLineMeasure("葡萄糖", glu);
//                printFeedLineMeasure("维生素", vc);
                printSingleLineMeasure("维生素", vc);
//                printFeedLineMeasure("尿钙", ca);
                //尿常规十一项没有以下三项
                if (!TextUtils.isEmpty(measure.getUrineCa()))
                    printSingleLineMeasure("尿钙", ca);
//                printFeedLineMeasure("微量白蛋白", ma);
                if (!TextUtils.isEmpty(measure.getUrineMa()))
                    printSingleLineMeasure("微量白蛋白", ma);
//                printFeedLineMeasure("肌酐", cr);
                if (!TextUtils.isEmpty(measure.getUrineCre()))
                    printSingleLineMeasure("肌酐", cr);
            }

            //血脂四项
            if (TextUtils.isEmpty(measure.getGluTree()) && !TextUtils.isEmpty(measure.getBlood_hdl())) {
                printLongLine("血脂四项");
                //高密度脂蛋白(HDL-C)
                String hdl_c = measure.getBlood_hdl() + "(" + BLOOD_HDLC_MIN + "-" + BLOOD_HDLC_MAX + ")mmol/L";
                //总胆固醇(TC)
                String tc = measure.getBlood_tc() + "(" + BLOOD_TC_MIN + "-" + BLOOD_TC_MAX + ")mmol/L";
                //甘油三酯(TG)
                String tg = measure.getBlood_tg() + "(" + BLOOD_TG_MIN + "-" + BLOOD_TG_MAX + ")mmol/L";
                //低密度脂蛋白胆固醇(LDL-C)
                String ldl = measure.getBlood_ldl() + "(" + BLOOD_LDLC_MIN + "-" + BLOOD_LDLC_MAX + ")mmol/L";

                printFeedLineMeasure("总胆固醇", tc);
                printFeedLineMeasure("高密度脂蛋白胆固醇", hdl_c);
                printFeedLineMeasure("低密度脂蛋白胆固醇", ldl);
                printFeedLineMeasure("甘油三酯", tg);
            }
            //血脂八项、血糖、尿常规、尿酸
            if (!TextUtils.isEmpty(measure.getGluTree())
                    || measure.getGlu() != -1.0f
                    || !TextUtils.isEmpty(measure.getGluTree())
                    || measure.getUricacid() != -1
                    || measure.getXzzdgc() != -1.0f) {
                printLongLine("血生化");
                //血脂八项
                if (!TextUtils.isEmpty(measure.getGluTree())) {
                    //高密度脂蛋白胆固醇(HDL-C)
                    String hdl_c = measure.getBlood_hdl() + checkValue(Float.parseFloat(measure.getBlood_hdl()),
                            BLOOD_HDLC_MIN, BLOOD_HDLC_MAX) + "(" + BLOOD_HDLC_MIN + "-" + BLOOD_HDLC_MAX + ")mmol/L";
                    //总胆固醇(TC)
                    String tc = measure.getBlood_tc() + checkValue(Float.parseFloat(measure.getBlood_tc()),
                            BLOOD_TC_MIN, BLOOD_TC_MAX) + "(" + BLOOD_TC_MIN + "-" + BLOOD_TC_MAX + ")mmol/L";
                    //甘油三酯(TG)
                    String tg = measure.getBlood_tg() + checkValue(Float.parseFloat(measure.getBlood_tg()),
                            BLOOD_TG_MIN, BLOOD_TG_MAX) + "(" + BLOOD_TG_MIN + "-" + BLOOD_TG_MAX + ")mmol/L";
                    //极低密度脂蛋白胆固醇(VLDL-C)
                    String vldl = measure.getBlood_vldl() + checkValue(Float.parseFloat(measure.getBlood_vldl()),
                            BLOOD_VLDL_MIN, BLOOD_VLDL_MAX) + "(" + BLOOD_VLDL_MIN + "-" + BLOOD_VLDL_MAX + ")mmol/L";
                    //低密度脂蛋白胆固醇(LDL-C)
                    String ldl = measure.getBlood_ldl() + checkValue(Float.parseFloat(measure.getBlood_ldl()),
                            BLOOD_LDLC_MIN, BLOOD_LDLC_MAX) + "(" + BLOOD_LDLC_MIN + "-" + BLOOD_LDLC_MAX + ")mmol/L";
//                    //动脉硬化指数(AI)
//                    String ai = measure.getBlood_ai() + "(<=4)";
//                    //冠心病危险指数(RCH-D)
//                    String rchd = measure.getBlood_r_chd() + "(<=4.5)";

                    printFeedLineMeasure("总胆固醇", tc);
                    printFeedLineMeasure("高密度脂蛋白胆固醇", hdl_c);
                    printFeedLineMeasure("低密度脂蛋白胆固醇", ldl);
                    printFeedLineMeasure("极低密度脂蛋白胆固醇", vldl);
                    printFeedLineMeasure("甘油三酯", tg);
//                    printFeedLineMeasure("动脉硬化指数(AI)", ai);
//                    printFeedLineMeasure("冠心病危险指数(RCH-D)", rchd);
                }

                //总胆固醇
                if (measure.getXzzdgc() != -1.0f) {
                    String zdgc = measure.getXzzdgc() + checkValue(measure.getXzzdgc(),
                            BLOOD_TC_MIN, BLOOD_TC_MAX) + "(" + BLOOD_TC_MIN + "-" + BLOOD_TC_MAX + ")mmol/L";
                    printFeedLineMeasure("总胆固醇", zdgc);
                }

                //血糖 优先级大于 血脂葡萄糖
                if (measure.getGlu() != -1.0f || !TextUtils.isEmpty(measure.getGluTree())) {
                    String glu = "";
                    String gluStr = "";
                    if (measure.getGlu() != -1.0f) {
                        //血糖
                        Configuration.BtnFlag gluStyle = measure.getGluStyle();
                        String gluUnit = (gluStyle == Configuration.BtnFlag.lift ?
                                "(" + GLU_BEFORE_MIN + "-" + GLU_BEFORE_MAX + ")mmol/L" : "(" + GLU_AFTER_MIN + "-" + GLU_AFTER_MAX + ")mmol/L");
                        glu = measure.getGlu() + (gluStyle == Configuration.BtnFlag.lift ?
                                checkValue(measure.getGlu(), GLU_BEFORE_MIN, GLU_BEFORE_MAX) :
                                checkValue(measure.getGlu(), GLU_AFTER_MIN, GLU_AFTER_MAX)) + gluUnit;
                        gluStr = (gluStyle == Configuration.BtnFlag.lift ? "(餐前)" : "(餐后)");
                    } else if (!TextUtils.isEmpty(measure.getGluTree())) {
                        //血脂葡萄糖
                        glu = measure.getGluTree() + checkValue(Float.parseFloat(measure.getGluTree()),
                                GLU_BEFORE_MIN, GLU_BEFORE_MAX) + "(" + GLU_BEFORE_MIN + "-" + GLU_BEFORE_MAX + ")mmol/L";
                        gluStr = "";
                    }
                    printFeedLineMeasure("血糖" + gluStr, glu);
                }

                //尿酸
                if (measure.getUricacid() != -1) {
                    //如果不是女的，都看作男
                    int min = resident.getSexy() == 0 ? UA_WOMAN_MIN : UA_MAN_MIN;
                    int max = resident.getSexy() == 0 ? UA_WOMAN_MAX : UA_MAN_MAX;

                    String uricacid = measure.getUricacid() + checkValue(measure.getUricacid(),
                            min, max) + "(" + min + "-" + max + ")umol/L";
                    printFeedLineMeasure("尿酸", uricacid);
                }

            }

            //炎症检测
            if (!TextUtils.isEmpty(measure.getFia_crp()) || !TextUtils.isEmpty(measure.getFia_hscrp())
                    || !TextUtils.isEmpty(measure.getFia_saa()) || !TextUtils.isEmpty(measure.getFia_pct())
                    || !TextUtils.isEmpty(measure.getFia_ctnl()) || !TextUtils.isEmpty(measure.getFia_ckmb())
                    || !TextUtils.isEmpty(measure.getFia_myo())) {

                printLongLine("炎症检测");

                //CRP C反应蛋白
                if (!TextUtils.isEmpty(measure.getFia_crp())) {
                    String crp = measure.getFia_crp() + checkValue(Float.valueOf(measure.getFia_crp()
                                    .replace("<", "")
                                    .replace(">", "")
                                    .replace("≥", "")
                                    .replace("≤", "")),
                            CRP_MIN, CRP_MAX)
                            + "(" + CRP_MIN + "-" + CRP_MAX + ")mg/L";

                    printFeedLineMeasure("C反应蛋白", crp);
                }

                //HsCRP
                if (!TextUtils.isEmpty(measure.getFia_hscrp())) {
                    String hscrp = measure.getFia_hscrp() + checkValue(Float.valueOf(measure.getFia_hscrp()
                                    .replace("<", "")
                                    .replace(">", "")
                                    .replace("≥", "")
                                    .replace("≤", "")),
                            HSCRP_MIN, HSCRP_MAX)
                            + "(" + HSCRP_MIN + "-" + HSCRP_MAX + ")mg/L";
                    printFeedLineMeasure("超敏C反应蛋白", hscrp);
                }

                //SAA  血清淀粉样蛋白A
                if (!TextUtils.isEmpty(measure.getFia_saa())) {
                    String saa = measure.getFia_saa() + checkValue(Float.valueOf(measure.getFia_saa()
                                    .replace("<", "")
                                    .replace(">", "")
                                    .replace("≥", "")
                                    .replace("≤", "")),
                            SAA_MIN, SAA_MAX)
                            + "(" + SAA_MIN + "-" + SAA_MAX + ")mg/L";

                    printFeedLineMeasure("血清淀粉样蛋白A", saa);
                }

                //PCT  降钙素原
                if (!TextUtils.isEmpty(measure.getFia_pct())) {
                    String pct = measure.getFia_pct() + checkValue(Float.valueOf(measure.getFia_pct()), PCT_MIN, PCT_MAX)
                            + " (0-0.05)ng/mL";

                    printFeedLineMeasure("降钙素原", pct);
                }

                //心肌肌钙蛋白 cTnI
                if (!TextUtils.isEmpty(measure.getFia_ctnl())) {
                    String ctnl = measure.getFia_ctnl() + checkValue(Float.valueOf(measure.getFia_ctnl()), 0, CTNI_MAX)
                            + " (<=1.0)ng/mL";

                    printFeedLineMeasure("心肌肌钙蛋白", ctnl);
                }

                //肌酸激酶同工酶 CK-MB
                if (!TextUtils.isEmpty(measure.getFia_ckmb())) {
                    String ckmb = measure.getFia_ckmb() + checkValue(Float.valueOf(measure.getFia_ckmb()), 0, CK_MB_MAX)
                            + " (<=5)ng/mL";

                    printFeedLineMeasure("肌酸激酶同工酶", ckmb);
                }

                //肌红蛋白 MYO
                if (!TextUtils.isEmpty(measure.getFia_myo())) {
                    String myo = measure.getFia_myo() + checkValue(Float.valueOf(measure.getFia_myo()), 0, MYO_MAX)
                            + " (<=0)ng/mL";

                    printFeedLineMeasure("肌红蛋白", myo);
                }
            }
        }

        //底部信息
        {
            printLongLine();
            printFeedLine(1);
            //打印测量时间  医生签名
            //打印【检查结果字样】
            String checkTime = "打印时间：" + DateUtil.getCurrentTime(new Date()).substring(0, 19);
            printUTF8(checkTime);
            printFeedLine(2);
            setAlignment(printerlibs_caysnpos.PosAlignment_HCenter);
            printUTF8("-----本结果只对本标本负责-----");
            printFeedLine(1);
            setAlignment(printerlibs_caysnpos.PosAlignment_Left);
            printUTF8("审核：");
            printFeedLine(3);
            //打印横线
            printLongLine();
            //打印空行
            printFeedLine(2);
        }
        return true;
    }

}
