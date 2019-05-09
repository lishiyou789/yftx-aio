package com.health2world.aio.printer;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.common.DataServer;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.config.NormalRange;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.util.ScreenShotUtil;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;
import com.konsung.listen.Measure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aio.health2world.utils.DateUtil;

import static com.health2world.aio.config.NormalRange.BLOOD_HDLC_MAX;
import static com.health2world.aio.config.NormalRange.BLOOD_HDLC_MIN;
import static com.health2world.aio.config.NormalRange.BLOOD_LDLC_MAX;
import static com.health2world.aio.config.NormalRange.BLOOD_LDLC_MIN;
import static com.health2world.aio.config.NormalRange.BLOOD_TC_MAX;
import static com.health2world.aio.config.NormalRange.BLOOD_TC_MIN;
import static com.health2world.aio.config.NormalRange.BLOOD_TG_MAX;
import static com.health2world.aio.config.NormalRange.BLOOD_TG_MIN;
import static com.health2world.aio.config.NormalRange.BLOOD_VLDL_MAX;
import static com.health2world.aio.config.NormalRange.BLOOD_VLDL_MIN;
import static com.health2world.aio.config.NormalRange.CK_MB_MAX;
import static com.health2world.aio.config.NormalRange.CRP_MAX;
import static com.health2world.aio.config.NormalRange.CRP_MIN;
import static com.health2world.aio.config.NormalRange.CTNI_MAX;
import static com.health2world.aio.config.NormalRange.GLU_AFTER_MAX;
import static com.health2world.aio.config.NormalRange.GLU_AFTER_MIN;
import static com.health2world.aio.config.NormalRange.GLU_BEFORE_MAX;
import static com.health2world.aio.config.NormalRange.GLU_BEFORE_MIN;
import static com.health2world.aio.config.NormalRange.HB_MAN_MAX;
import static com.health2world.aio.config.NormalRange.HB_MAN_MIN;
import static com.health2world.aio.config.NormalRange.HB_WOMAN_MAX;
import static com.health2world.aio.config.NormalRange.HB_WOMAN_MIN;
import static com.health2world.aio.config.NormalRange.HCT_MAN_MAX;
import static com.health2world.aio.config.NormalRange.HCT_MAN_MIN;
import static com.health2world.aio.config.NormalRange.HCT_WOMAN_MAX;
import static com.health2world.aio.config.NormalRange.HCT_WOMAN_MIN;
import static com.health2world.aio.config.NormalRange.HSCRP_MAX;
import static com.health2world.aio.config.NormalRange.HSCRP_MIN;
import static com.health2world.aio.config.NormalRange.MYO_MAX;
import static com.health2world.aio.config.NormalRange.NIBP_DBP_MAX;
import static com.health2world.aio.config.NormalRange.NIBP_DBP_MIN;
import static com.health2world.aio.config.NormalRange.NIBP_SBP_MAX;
import static com.health2world.aio.config.NormalRange.NIBP_SBP_MIN;
import static com.health2world.aio.config.NormalRange.PCT_MAX;
import static com.health2world.aio.config.NormalRange.PCT_MIN;
import static com.health2world.aio.config.NormalRange.PR_MAX;
import static com.health2world.aio.config.NormalRange.PR_MIN;
import static com.health2world.aio.config.NormalRange.SAA_MAX;
import static com.health2world.aio.config.NormalRange.SAA_MIN;
import static com.health2world.aio.config.NormalRange.SPO2_MAX;
import static com.health2world.aio.config.NormalRange.SPO2_MIN;
import static com.health2world.aio.config.NormalRange.TEMP_MAX;
import static com.health2world.aio.config.NormalRange.TEMP_MIN;
import static com.health2world.aio.config.NormalRange.UA_MAN_MAX;
import static com.health2world.aio.config.NormalRange.UA_MAN_MIN;
import static com.health2world.aio.config.NormalRange.UA_WOMAN_MAX;
import static com.health2world.aio.config.NormalRange.UA_WOMAN_MIN;
import static com.health2world.aio.config.NormalRange.URINE_PH_MAX;
import static com.health2world.aio.config.NormalRange.URINE_PH_MIN;
import static com.health2world.aio.config.NormalRange.URINE_SG_MAX;
import static com.health2world.aio.config.NormalRange.URINE_SG_MIN;
import static com.health2world.aio.config.NormalRange.WBC_BAS_MAX;
import static com.health2world.aio.config.NormalRange.WBC_BAS_MIN;
import static com.health2world.aio.config.NormalRange.WBC_EOS_MAX;
import static com.health2world.aio.config.NormalRange.WBC_EOS_MIN;
import static com.health2world.aio.config.NormalRange.WBC_LYM_MAX;
import static com.health2world.aio.config.NormalRange.WBC_LYM_MIN;
import static com.health2world.aio.config.NormalRange.WBC_MAX;
import static com.health2world.aio.config.NormalRange.WBC_MIN;
import static com.health2world.aio.config.NormalRange.WBC_MON_MAX;
import static com.health2world.aio.config.NormalRange.WBC_MON_MIN;
import static com.health2world.aio.config.NormalRange.WBC_NEU_MAX;
import static com.health2world.aio.config.NormalRange.WBC_NEU_MIN;
import static com.health2world.aio.config.NormalRange.checkValue;

/**
 * Created by lishiyou on 2019/1/11 0011.
 */

public class PrinterA5PreviewActivity extends BaseActivity {

    private RecyclerView recyclerViewLeft, recyclerViewRight;

    private PrinterDataAdapter dataAdapterLeft, dataAdapterRight;

    private List<PrinterData> dataList;
    private List<PrinterData> dataListLeft = new ArrayList<>();
    private List<PrinterData> dataListRight = new ArrayList<>();

    private TextView tvTitle, tvName, tvCode, tvNo, tvSex, tvAge, tvDoctor, tvType,
            tvItem, tvResult, tvRange, tvUnit, tvItem1, tvResult1, tvRange1, tvUnit1,
            tvTime, tvCheck, tvBottom;

    private Typeface typeface;

    private DoctorBean doctor;
    /**
     * 测量数据
     */
    private MeasureBean measure;
    /**
     * 测量者信息
     */
    private ResidentBean resident;

    private boolean checkNormal;
    private boolean checkWbc;
    private boolean checkHB;
    private boolean checkCRP;
    private boolean checkUrine;
    private boolean checkBiochemistry;

    private Handler handler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_urine_print_preview;
    }

    @Override
    protected void initView() {
        typeface = MyApplication.getInstance().getTypeface();
        recyclerViewLeft = findView(R.id.recyclerViewLeft);
        recyclerViewLeft.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRight = findView(R.id.recyclerViewRight);
        recyclerViewRight.setLayoutManager(new LinearLayoutManager(this));
        tvTitle = findView(R.id.tvTitle);
        tvName = findView(R.id.tvName);
        tvCode = findView(R.id.tvCode);
        tvNo = findView(R.id.tvNo);
        tvSex = findView(R.id.tvSex);
        tvAge = findView(R.id.tvAge);
        tvDoctor = findView(R.id.tvDoctor);
        tvType = findView(R.id.tvType);

        tvItem = findView(R.id.tvItem);
        tvResult = findView(R.id.tvResult);
        tvRange = findView(R.id.tvRange);
        tvUnit = findView(R.id.tvUnit);
        tvItem1 = findView(R.id.tvItem1);
        tvResult1 = findView(R.id.tvResult1);
        tvRange1 = findView(R.id.tvRange1);
        tvUnit1 = findView(R.id.tvUnit1);

        tvTime = findView(R.id.tvTime);
        tvCheck = findView(R.id.tvCheck);
        tvBottom = findView(R.id.tvBottom);


        tvTitle.setTypeface(typeface);
        tvName.setTypeface(typeface);
        tvCode.setTypeface(typeface);
        tvNo.setTypeface(typeface);
        tvSex.setTypeface(typeface);
        tvAge.setTypeface(typeface);
        tvDoctor.setTypeface(typeface);
        tvType.setTypeface(typeface);
        tvItem.setTypeface(typeface);
        tvResult.setTypeface(typeface);
        tvRange.setTypeface(typeface);
        tvUnit.setTypeface(typeface);
        tvItem1.setTypeface(typeface);
        tvResult1.setTypeface(typeface);
        tvRange1.setTypeface(typeface);
        tvUnit1.setTypeface(typeface);

        tvTime.setTypeface(typeface);
        tvCheck.setTypeface(typeface);
        tvBottom.setTypeface(typeface);
    }

    @Override
    protected void initData() {

        doctor = DBManager.getInstance().getCurrentDoctor();
        handler = new Handler();

        if (getIntent().hasExtra("resident"))
            resident = (ResidentBean) getIntent().getSerializableExtra("resident");
        if (getIntent().hasExtra("measure"))
            measure = (MeasureBean) getIntent().getSerializableExtra("measure");

        if (resident == null || measure == null)
            finish();

        fillData();

        dataAdapterLeft = new PrinterDataAdapter(dataListLeft, typeface);
        recyclerViewLeft.setAdapter(dataAdapterLeft);
        dataAdapterRight = new PrinterDataAdapter(dataListRight, typeface);
        recyclerViewRight.setAdapter(dataAdapterRight);

        //对半分，最多显示25行，共50项
        for (int i = 0; i < dataList.size() && i <= 50; i++) {
            if (i <= dataList.size() / 2)
                dataListLeft.add(dataList.get(i));
            else
                dataListRight.add(dataList.get(i));
        }
        dataAdapterLeft.notifyDataSetChanged();
        dataAdapterRight.notifyDataSetChanged();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap;
                if (TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_5_1)) {
                    bitmap = ScreenShotUtil.screenShot1(PrinterA5PreviewActivity.this);
                } else {
                    bitmap = ScreenShotUtil.screenShot(PrinterA5PreviewActivity.this);
                }
                ScreenShotUtil.savePicture(bitmap, "print.bmp");
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
                setResult(RESULT_OK);
                finish();
            }
        }, 500);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler = null;
        recyclerViewLeft = null;
        recyclerViewRight = null;
        dataList.clear();
        dataList = null;
        dataListLeft.clear();
        dataListLeft = null;
        dataListRight.clear();
        dataListRight = null;
        typeface = null;
        //彻底关闭
//        System.exit(0);
    }

    /***
     * 数据填充分割
     */
    private void fillData() {

        dataList = new ArrayList<>();
        if (resident != null) {
            tvName.setText("姓名：" + resident.getName());
            tvSex.setText("性别：" + DataServer.getSexNick(resident.getSexy()));
            tvAge.setText("年龄：" + (resident.getAge() < 0 ? "--" : resident.getAge()));
            tvCode.setText("居民码：" + resident.getResidentCode());
        }
        //样本编号
        tvNo.setText("样本编号：" + System.currentTimeMillis());
        //送检医生
        tvDoctor.setText("送检医生：" + doctor.getName());
        //检测项目
        tvCheck.setText("审核：");
        //时间
        tvTime.setText("打印时间：" + DateUtil.getCurrentTime(new Date(System.currentTimeMillis())));

        /**获取测量项*/

        //血压
        if (measure.getSbp() != 0) {
            checkNormal = true;
            dataList.add(new PrinterData(Measure.NIBP, "收缩压(SBP)", measure.getSbp() + "",
                    checkValue(measure.getSbp(), NIBP_SBP_MIN, NIBP_SBP_MAX),
                    NormalRange.NIBP_SBP_MIN + "-" + NIBP_SBP_MAX, "mmHg"));
            dataList.add(new PrinterData(Measure.NIBP, "舒张压(DBP)", measure.getDbp() + "",
                    checkValue(measure.getDbp(), NIBP_DBP_MIN, NIBP_DBP_MAX),
                    NIBP_DBP_MIN + "-" + NIBP_DBP_MAX, "mmHg"));
        }
        //血氧
        if (measure.getSpo2() != 0) {
            checkNormal = true;
            dataList.add(new PrinterData(Measure.SPO2, "血氧(SpO2)", measure.getSpo2() + "",
                    checkValue(measure.getSpo2(), SPO2_MIN, SPO2_MAX),
                    SPO2_MIN + "-" + SPO2_MAX, "%"));
        }
        //脉率
        if (measure.getPr() != 0 || measure.getHr() != 0 || measure.getNibPr() != 0) {
            //0505 用优先级取脉率值
            int pr = (measure.getHr() != 0 ? measure.getHr() : (measure.getPr() != 0 ? measure.getPr() : measure.getNibPr()));
            dataList.add(new PrinterData(Measure.PR, "脉率/心率(HR)", pr + "",
                    checkValue(pr, PR_MIN, PR_MAX),
                    PR_MIN + "-" + PR_MAX, "bpm"));
        }
        //体温
        if (measure.getTemp() != 0f) {
            checkNormal = true;
            dataList.add(new PrinterData(Measure.TEMP, "体温(Temp)", measure.getTemp() + "",
                    checkValue(measure.getTemp(), TEMP_MIN, TEMP_MAX),
                    TEMP_MIN + "-" + TEMP_MAX, "℃"));
        }
        //血糖
        if (measure.getGlu() != -1.0f) {
            checkBiochemistry = true;
            String itemName = measure.getGluType() == 0 ? "餐前血糖(GLU)" : "餐后血糖(GLU)";
            float min = measure.getGluType() == 0 ? GLU_BEFORE_MIN : GLU_AFTER_MIN;
            float max = measure.getGluType() == 0 ? GLU_BEFORE_MAX : GLU_AFTER_MAX;

            dataList.add(new PrinterData(Measure.GLU, itemName, measure.getGlu() + "",
                    checkValue(measure.getGlu(), min, max),
                    min + "-" + max, "mmol/L"));
        }
        //尿酸
        if (measure.getUricacid() != -1) {
            checkBiochemistry = true;
            //性别 2-未知 1-男 0-女
            //如果不是女的，都看作男
            int min = resident.getSexy() == 0 ? UA_WOMAN_MIN : UA_MAN_MIN;
            int max = resident.getSexy() == 0 ? UA_WOMAN_MAX : UA_MAN_MAX;

            dataList.add(new PrinterData(Measure.UA, "尿酸(UA)", measure.getUricacid() + "",
                    checkValue(measure.getUricacid(), min, max),
                    min + "-" + max, "umol/L"));
        }
        //总胆固醇
        if (measure.getXzzdgc() != -1.0f) {
            checkBiochemistry = true;
            dataList.add(new PrinterData(Measure.CHOL, "总胆固醇(CHOL)", measure.getXzzdgc() + "",
                    checkValue(measure.getXzzdgc(), BLOOD_TC_MIN, BLOOD_TC_MAX),
                    BLOOD_TC_MIN + "-" + BLOOD_TC_MAX, "mmol/L"));
        }

        //心电
//        if (!TextUtils.isEmpty(measure.getAnal())) {
//            String[] result = measure.getAnal().split(",");
//            if (result.length >= 12 && !TextUtils.isEmpty(result[11]))
//                dataList.add(new PrinterData(Measure.ECG, "心电报告", result[11],
//                        "", "", ""));
//        }

        //血红蛋白
        if (measure.getAssxhb() != 0f) {
            checkHB = true;
            //性别 2-未知 1-男 0-女
            //如果不是女的，都看作男
            int hbmin = resident.getSexy() == 0 ? HB_WOMAN_MIN : HB_MAN_MIN;
            int hbmax = resident.getSexy() == 0 ? HB_WOMAN_MAX : HB_MAN_MAX;
            int hctmin = resident.getSexy() == 0 ? HCT_WOMAN_MIN : HCT_MAN_MIN;
            int hctmax = resident.getSexy() == 0 ? HCT_WOMAN_MAX : HCT_MAN_MAX;

            dataList.add(new PrinterData(Measure.HB, "血红蛋白(Hb)", measure.getAssxhb() + "",
                    checkValue(measure.getAssxhb(), hbmin, hbmax),
                    hbmin + "-" + hbmax, "g/L"));
            dataList.add(new PrinterData(Measure.HCT, "红细胞比容(HCT)", measure.getAssxhct() + "",
                    checkValue(measure.getAssxhct(), hctmin, hctmax),
                    hctmin + "-" + hctmax, "%"));
        }
        //身高
        if (!TextUtils.isEmpty(measure.getHeight())) {
            checkNormal = true;
            dataList.add(new PrinterData(Measure.HEIGHT, "身高(Height)", measure.getHeight() + "",
                    " ", "--", "cm"));
        }
        //体重
        if (!TextUtils.isEmpty(measure.getWeight())) {
            checkNormal = true;
            dataList.add(new PrinterData(Measure.WEIGHT, "体重(Weight)", measure.getWeight() + "",
                    " ", "--", "kg"));
        }
        //白细胞
        if (measure.getWbc() != -1.0f) {
            checkWbc = true;
            dataList.add(new PrinterData(Measure.WBC, "白细胞(WBC)", measure.getWbc() + "",
                    checkValue(measure.getWbc(), WBC_MIN, WBC_MAX), WBC_MIN + "-" + WBC_MAX, "10^9/L"));

            dataList.add(new PrinterData(Measure.WBC, "*淋巴细胞(LYM)", measure.getWbc_lym() + "",
                    checkValue(measure.getWbc_lym(), WBC_LYM_MIN, WBC_LYM_MAX), WBC_LYM_MIN + "-" + WBC_LYM_MAX, "10^9/L"));

            dataList.add(new PrinterData(Measure.WBC, "*单核细胞(MON)", measure.getWbc_mon() + "",
                    checkValue(measure.getWbc_mon(), WBC_MON_MIN, WBC_MON_MAX), WBC_MON_MIN + "-" + WBC_MON_MAX, "10^9/L"));

            dataList.add(new PrinterData(Measure.WBC, "*中性粒细胞(NEU)", measure.getWbc_neu() + "",
                    checkValue(measure.getWbc_neu(), WBC_NEU_MIN, WBC_NEU_MAX), WBC_NEU_MIN + "-" + WBC_NEU_MAX, "10^9/L"));

            dataList.add(new PrinterData(Measure.WBC, "*嗜酸性粒细胞(EOS)", measure.getWbc_eos() + "",
                    checkValue(measure.getWbc_eos(), WBC_EOS_MIN, WBC_EOS_MAX), WBC_EOS_MIN + "-" + WBC_EOS_MAX, "10^9/L"));

            dataList.add(new PrinterData(Measure.WBC, "*嗜碱性粒细胞(BAS)", measure.getWbc_bas() + "",
                    checkValue(measure.getWbc_bas(), WBC_BAS_MIN, WBC_BAS_MAX), WBC_BAS_MIN + "-" + WBC_BAS_MAX, "10^9/L"));
        }
        //添加替换大于小于符号后再进行范围判断  --0411
        //C反应蛋白
        if (!TextUtils.isEmpty(measure.getFia_crp())) {
            checkCRP = true;
            dataList.add(new PrinterData(Measure.CRP, "C反应蛋白(CRP)", measure.getFia_crp() + "",
                    checkValue(Float.parseFloat(measure.getFia_crp()
                                    .replace("<", "")
                                    .replace(">", "")
                                    .replace("≥", "")
                                    .replace("≤", "")),
                            CRP_MIN, CRP_MAX),
                    CRP_MIN + "-" + CRP_MAX, "mg/L"));
        }
        //超敏C反应蛋白
        if (!TextUtils.isEmpty(measure.getFia_hscrp())) {
            checkCRP = true;
            dataList.add(new PrinterData(Measure.CRP, "超敏C反应蛋白(hs-CRP)", measure.getFia_hscrp() + "",
                    checkValue(Float.parseFloat(measure.getFia_hscrp()
                                    .replace("<", "")
                                    .replace(">", "")
                                    .replace("≥", "")
                                    .replace("≤", "")),
                            HSCRP_MIN, HSCRP_MAX),
                    HSCRP_MIN + "-" + HSCRP_MAX, "mg/L"));
        }
        //血清淀粉样蛋白
        if (!TextUtils.isEmpty(measure.getFia_saa())) {
            checkCRP = true;
            dataList.add(new PrinterData(Measure.SAA, "血清淀粉样蛋白A(SAA)", measure.getFia_saa() + "",
                    checkValue(Float.parseFloat(measure.getFia_saa()
                                    .replace("<", "")
                                    .replace(">", "")
                                    .replace("≥", "")
                                    .replace("≤", "")),
                            SAA_MIN, SAA_MAX),
                    SAA_MIN + "-" + SAA_MAX, "mg/L"));
        }
        //降钙素原
        if (!TextUtils.isEmpty(measure.getFia_pct())) {
            checkCRP = true;
            dataList.add(new PrinterData(Measure.PCT, "降钙素原(PCT)", measure.getFia_pct() + "",
                    checkValue(Float.valueOf(measure.getFia_pct()), PCT_MIN, PCT_MAX),
                    PCT_MIN + "-" + PCT_MAX, "ng/mL"));
        }
        //心肌三项
        if (!TextUtils.isEmpty(measure.getFia_myo())) {
            checkBiochemistry = true;
            dataList.add(new PrinterData(Measure.MYO, "肌红蛋白(MYO)", measure.getFia_myo(),
                    checkValue(Float.valueOf(measure.getFia_myo()), 0, MYO_MAX),
                    "", "ng/ml"));
        }
        if (!TextUtils.isEmpty(measure.getFia_ckmb())) {
            checkBiochemistry = true;
            dataList.add(new PrinterData(Measure.MYO, "肌酸激酶同工酶(CK-MB)", measure.getFia_ckmb(),
                    checkValue(Float.valueOf(measure.getFia_ckmb()), 0, CK_MB_MAX),
                    "<=5", "ng/ml"));
        }
        if (!TextUtils.isEmpty(measure.getFia_ctnl())) {
            checkBiochemistry = true;
            dataList.add(new PrinterData(Measure.MYO, "心肌肌钙蛋白(cTnI)", measure.getFia_ctnl(),
                    checkValue(Float.valueOf(measure.getFia_ctnl()), 0, CTNI_MAX),
                    "", "ng/ml"));
        }
        //血脂四项
        if (!TextUtils.isEmpty(measure.getBlood_hdl()) && TextUtils.isEmpty(measure.getBlood_ai())) {
            checkBiochemistry = true;
            dataList.add(new PrinterData(Measure.BLOOD, "高密度脂蛋白(DHL-C)", measure.getBlood_hdl(),
                    checkValue(Float.valueOf(measure.getBlood_hdl()), BLOOD_HDLC_MIN, BLOOD_HDLC_MAX),
                    BLOOD_HDLC_MIN + "-" + BLOOD_HDLC_MAX, "mmol/L"));
            dataList.add(new PrinterData(Measure.BLOOD, "低密度脂蛋白(LDL-C)", measure.getBlood_ldl(),
                    checkValue(Float.valueOf(measure.getBlood_ldl()), BLOOD_LDLC_MIN, BLOOD_LDLC_MAX),
                    BLOOD_LDLC_MIN + "-" + BLOOD_LDLC_MAX, "mmol/L"));
            dataList.add(new PrinterData(Measure.BLOOD, "总胆固醇(TC)", measure.getBlood_tc(),
                    checkValue(Float.valueOf(measure.getBlood_tc()), BLOOD_TC_MIN, BLOOD_TC_MAX),
                    BLOOD_TC_MIN + "-" + BLOOD_TC_MAX, "mmol/L"));
            dataList.add(new PrinterData(Measure.BLOOD, "低密度脂蛋白(TG)", measure.getBlood_tg(),
                    checkValue(Float.valueOf(measure.getBlood_tg()), BLOOD_TG_MIN, BLOOD_TG_MAX),
                    BLOOD_TG_MIN + "-" + BLOOD_TG_MAX, "mmol/L"));
        }
        //血脂八项
        if (!TextUtils.isEmpty(measure.getBlood_hdl())
                && !TextUtils.isEmpty(measure.getBlood_ai())
                && !TextUtils.isEmpty(measure.getBlood_tc())) {
            checkBiochemistry = true;
            dataList.add(new PrinterData(Measure.DS100A, "高密度脂蛋白(DHL-C)", measure.getBlood_hdl(),
                    checkValue(Float.valueOf(measure.getBlood_hdl()), BLOOD_HDLC_MIN, BLOOD_HDLC_MAX),
                    BLOOD_HDLC_MIN + "-" + BLOOD_HDLC_MAX, "mmol/L"));
            dataList.add(new PrinterData(Measure.DS100A, "低密度脂蛋白(LDL-C)", measure.getBlood_ldl(),
                    checkValue(Float.valueOf(measure.getBlood_ldl()), BLOOD_LDLC_MIN, BLOOD_LDLC_MAX),
                    BLOOD_LDLC_MIN + "-" + BLOOD_LDLC_MAX, "mmol/L"));
            dataList.add(new PrinterData(Measure.DS100A, "总胆固醇(TC)", measure.getBlood_tc(),
                    checkValue(Float.valueOf(measure.getBlood_tc()), BLOOD_TC_MIN, BLOOD_TC_MAX),
                    BLOOD_TC_MIN + "-" + BLOOD_TC_MAX, "mmol/L"));
            dataList.add(new PrinterData(Measure.DS100A, "甘油三酯(TG)", measure.getBlood_tg(),
                    checkValue(Float.valueOf(measure.getBlood_tg()), BLOOD_TG_MIN, BLOOD_TG_MAX),
                    BLOOD_TG_MIN + "-" + BLOOD_TG_MAX, "mmol/L"));

            dataList.add(new PrinterData(Measure.DS100A, "葡萄糖(GLU)", measure.getGluTree(),
                    checkValue(Float.valueOf(measure.getGluTree()), GLU_BEFORE_MIN, GLU_BEFORE_MAX),
                    GLU_BEFORE_MIN + "-" + GLU_BEFORE_MAX, "mmol/L"));
            dataList.add(new PrinterData(Measure.DS100A, "极低密度脂蛋白(VLDL-C)", measure.getBlood_vldl(),
                    checkValue(Float.valueOf(measure.getBlood_vldl()), BLOOD_VLDL_MIN, BLOOD_VLDL_MAX),
                    BLOOD_VLDL_MIN + "-" + BLOOD_VLDL_MAX, "mmol/L"));
//            dataList.add(new PrinterData(Measure.DS100A, "动脉硬化指数(ai)", measure.getBlood_ai(), 0 + "-" + BLOOD_AI_MAX, ""));
//            dataList.add(new PrinterData(Measure.DS100A, "冠心病危险指数(r-chd)", measure.getBlood_r_chd(), 0 + "-" + BLOOD_RCHD_MAX, ""));
        }

        //尿常规 分为11项和14项
        if (measure.getUrinePh() != 0f) {
            checkUrine = true;
            dataList.add(new PrinterData(Measure.URINE, "尿白细胞(LEU)", measure.getUrineLeu(), " ", "-", ""));
            dataList.add(new PrinterData(Measure.URINE, "亚硝酸盐(NIT)", measure.getUrineNit(), " ", "-", ""));
            dataList.add(new PrinterData(Measure.URINE, "尿胆原(URO)", measure.getUrineUbg(), " ", "-", ""));
            dataList.add(new PrinterData(Measure.URINE, "尿蛋白(PRO)", measure.getUrinePro(), " ", "-", ""));
            dataList.add(new PrinterData(Measure.URINE, "酸碱值(pH)", measure.getUrinePh() + "",
                    checkValue(measure.getUrinePh(), URINE_PH_MIN, URINE_PH_MAX),
                    URINE_PH_MIN + "-" + URINE_PH_MAX, ""));
            //下版本代码0505,添加精确到三位小数
            //(Math.round(measure.getUrineSg()*1000)/1000)
            dataList.add(new PrinterData(Measure.URINE, "比重(SG)", String.valueOf(measure.getUrineSg()),
                    checkValue((Float.parseFloat(String.valueOf(measure.getUrineSg()))), URINE_SG_MIN, URINE_SG_MAX),
                    URINE_SG_MIN + "-" + URINE_SG_MAX, ""));
            dataList.add(new PrinterData(Measure.URINE, "潜血(BLD)", measure.getUrineBld(), " ", "-", ""));
            dataList.add(new PrinterData(Measure.URINE, "酮体(KET)", measure.getUrineKet(), " ", "-", ""));
            dataList.add(new PrinterData(Measure.URINE, "胆红素(BIL)", measure.getUrineBil(), " ", "-", ""));
            dataList.add(new PrinterData(Measure.URINE, "葡萄糖(GLU)", measure.getUrineGlu(), " ", "-", ""));
            dataList.add(new PrinterData(Measure.URINE, "维生素C(Vc)", measure.getUrineVc(), " ", "-", ""));

            if (!TextUtils.isEmpty(measure.getUrineCa()))
                dataList.add(new PrinterData(Measure.URINE, "尿钙(Ca)", measure.getUrineCa(), " ", "-", ""));
            if (!TextUtils.isEmpty(measure.getUrineMa()))
                dataList.add(new PrinterData(Measure.URINE, "微量白蛋白(MA)", measure.getUrineMa(), " ", "-", ""));
            if (!TextUtils.isEmpty(measure.getUrineCre()))
                dataList.add(new PrinterData(Measure.URINE, "肌酐(Cr)", measure.getUrineCre(), " ", "-", ""));
        }

        boolean _checkXXB = (checkWbc || checkHB);
        tvType.setText("检查项目：" + (checkNormal ? "常规检查" : "") + (_checkXXB ? " 血细胞分析" : "") + (checkUrine ? " 尿常规" : "") + (checkCRP ? " 炎症检测" : "")
                + (checkBiochemistry ? " 血生化" : "") + (checkHB ? " 血红蛋白" : ""));

        tvBottom.setText("※本结果仅对此标本负责，请结合临床！" + ((measure.getWbc() != -1.0f) ? " *白细胞细项为研究参数" : ""));
    }
}
