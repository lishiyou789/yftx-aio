package com.health2world.aio.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.health2world.aio.BuildConfig;
import com.health2world.aio.MyApplication;
import com.health2world.aio.bean.AgreementBean;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.bean.SignService;
import com.health2world.aio.bean.UpgradeInfo;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.config.KonsungConfig;
import com.health2world.aio.config.MedicalConstant;
import com.health2world.aio.util.ActivityUtil;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aio.health2world.http.HttpResult;
import aio.health2world.http.tool.RxTransformer;
import aio.health2world.utils.AppUtils;
import aio.health2world.utils.DeviceUtil;
import aio.health2world.utils.MD5Util;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * e10adc3949ba59abbe56e057f20f883e
 * Created by Administrator on 2018/7/3 0003.
 */

public class ApiRequest {
    //登录(true为账号密码登录  false为扫码登录)
    public static Subscription login(String account, String password, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("account", account);
        map.put("password", password);
        //增加公卫版本 和测量库版本的信息
        map.put("deviceVersion", ActivityUtil.getAppInfo(KonsungConfig.APPDEVICE_PACKAGE) == null ? "" :
                ActivityUtil.getAppInfo(KonsungConfig.APPDEVICE_PACKAGE).versionCode + "");
        map.put("healthVersion", ActivityUtil.getAppInfo(KonsungConfig.PUBLIC_HEALTH_PACKAGE) == null ? "" :
                ActivityUtil.getAppInfo(KonsungConfig.PUBLIC_HEALTH_PACKAGE).versionCode + "");
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .login(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    //扫码登录
    public static Subscription QRLogin(Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("alias", DeviceUtil.getAndroidId(MyApplication.getInstance()));
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .QRLogin(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    //注销登录
    public static Subscription logout(String accountId, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("accountId", accountId);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .logout(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    //获取手机验证码
    public static Subscription securityCode(String phone, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "1");
        map.put("phone", phone);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .securityCode(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    //重置密码
    public static Subscription resetPwd(String mobile, String securityCode, String password, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("securityCode", securityCode);
        map.put("password", MD5Util.getMD5String(password));
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .resetPwd(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    //程序版本检验
    public static Subscription validateVersion(int appType, int insId, Subscriber<HttpResult<UpgradeInfo>> subscriber) {
        Map<String, Object> map = new HashMap<>();
        //1:一体机 2:医生 3:患者家属 4公卫App 6-85测量库  7-86测量库
        map.put("appNum", appType + "");
        map.put("insId", insId + "");
        if (appType == AppConfig.APP_SOFTWARE) {
            map.put("currentVersion", BuildConfig.VERSION_CODE + "");
        }
        if (appType == AppConfig.APP_PUBLIC_HEALTH) {
            map.put("currentVersion", ActivityUtil.getAppInfo(KonsungConfig.PUBLIC_HEALTH_PACKAGE) == null ?
                    "1" : ActivityUtil.getAppInfo(KonsungConfig.PUBLIC_HEALTH_PACKAGE).versionCode + "");
        }
        if (appType == AppConfig.APP_DEVICE_85 || appType == AppConfig.APP_DEVICE_86) {
            map.put("currentVersion", ActivityUtil.getAppInfo(KonsungConfig.APPDEVICE_PACKAGE) == null ?
                    "1" : ActivityUtil.getAppInfo(KonsungConfig.APPDEVICE_PACKAGE).versionCode + "");
        }
        //DeviceManager
        if (appType == AppConfig.APP_DEVICE_MANAGER) {
            map.put("currentVersion", ActivityUtil.getAppInfo(KonsungConfig.DEVICE_MANAGER_PACKAGE) == null ?
                    "1" : ActivityUtil.getAppInfo(KonsungConfig.DEVICE_MANAGER_PACKAGE).versionCode + "");
        }
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .validateVersion(map)
                .compose(RxTransformer.<HttpResult<UpgradeInfo>>defaultSchedulers())
                .subscribe(subscriber);
    }

    //任务列表
    public static Subscription getTaskList(String type, int pageIndex, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("instituteId", MyApplication.getInstance().getOrgId());
        map.put("taskType", type);
        map.put("pageNo", pageIndex + "");
        map.put("pageSize", AppConfig.PAGE_SIZE + "");
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .taskList(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    /***
     * 根据任务Id获取任务详情
     */
    public static Subscription getTaskDetail(String taskId, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("instituteId", MyApplication.getInstance().getOrgId());
        map.put("taskId", taskId);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .taskDetail(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    /***
     *获取居民的待处理任务
     * @param patientId
     * @return
     */
    public static Observable<HttpResult> getResidentTask(String patientId, int pageIndex) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        map.put("pageNo", pageIndex + "");
        map.put("pageSize", AppConfig.PAGE_SIZE + "");
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .getResidentTask(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers());
    }

    /**
     * 获取标签信息
     *
     * @param subscriber
     * @return
     */
    public static Subscription getTagList(Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("tagTypes", "1,2,3,4");//1系统定义 2诊所定义 3人群 4病种
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .getTagList(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }


    /***
     * 标记公卫建档任务已经完成
     */
    public static Subscription completePhInfo(String patientId, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .completePhInfo(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    /**
     * 测量数据的上传（包含了所有的测量项目）
     */
    public static Subscription uploadMedicalData(String dataId, String patientId, int sex,
                                                 MeasureBean bean, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("doctorId", MyApplication.getInstance().getDoctorId() + "");
        map.put("patientId", patientId);
        map.put("deviceCode", AppUtils.getDeviceNo());
        map.put("checkDate", String.valueOf(bean.getCheckDay()));
        //0：未知 1：一体机 2：医生端 3：居民端
        map.put("dataSource", "1");
        //0：未知 1：医用设备 2：家用设备 3：手动输入
        map.put("checkMode", "1");
        List<MedicalData> dataList = new ArrayList<>();

        /**血压*/
        if (bean.getSbp() != 0) {
            MedicalData sbp = new MedicalData(MedicalConstant.NIBP, MedicalConstant.SBP, String.valueOf(bean.getSbp()));
            MedicalData dbp = new MedicalData(MedicalConstant.NIBP, MedicalConstant.DBP, String.valueOf(bean.getDbp()));
            MedicalData nibPr = new MedicalData(MedicalConstant.NIBP, MedicalConstant.BLOOD_PR, String.valueOf(bean.getNibPr()));
            dataList.add(sbp);
            dataList.add(dbp);
            dataList.add(nibPr);
        }
        /**血氧*/
        if (bean.getSpo2() != 0) {
            MedicalData spo2 = new MedicalData(MedicalConstant.BLOOD_OXYGEN, MedicalConstant.OXYGEN_SPO2, String.valueOf(bean.getSpo2()));
            MedicalData pr = new MedicalData(MedicalConstant.BLOOD_OXYGEN, MedicalConstant.OXYGEN_PR, String.valueOf(bean.getPr()));
            dataList.add(spo2);
            dataList.add(pr);
        }
        /**体温*/
        if (bean.getTemp() != 0f) {
            MedicalData temp = new MedicalData(MedicalConstant.TEMP, MedicalConstant.TEMPERATURE, String.valueOf(bean.getTemp()));
            dataList.add(temp);
        }

        //一下三项可为0，故初始值改为-1
        /**血糖*/
        if (bean.getGlu() != -1f) {
            MedicalData glu = new MedicalData(MedicalConstant.BLOOD_SUGAR, bean.getGluType() == 0 ? MedicalConstant.SUGAR : MedicalConstant.SUGAR_PBG,
                    String.valueOf(bean.getGlu()));
            dataList.add(glu);
        }

        /**尿酸*/
        if (bean.getUricacid() != -1) {
            //判断男女
            MedicalData ua;
            if (sex == 0) {
                ua = new MedicalData(MedicalConstant.UA, MedicalConstant.MD_URIC_ACID_WOMAN, String.valueOf(bean.getUricacid()));
            } else {
                ua = new MedicalData(MedicalConstant.UA, MedicalConstant.MD_URIC_ACID_MAN, String.valueOf(bean.getUricacid()));
            }
            dataList.add(ua);
        }

        /**总胆固醇*/
        if (bean.getXzzdgc() != -1f) {
            MedicalData zdgc = new MedicalData(MedicalConstant.CHOL, MedicalConstant.CHOLESTEROL, String.valueOf(bean.getXzzdgc()));
            dataList.add(zdgc);
        }

        /**血红蛋白/压积值*/
        if (bean.getAssxhb() != 0f) {
            //判断男女
            MedicalData hb;
            MedicalData hct;
            if (sex == 0) {
                hb = new MedicalData(MedicalConstant.HB, MedicalConstant.ASSXHDB_WOMAN, String.valueOf(bean.getAssxhb()));
                hct = new MedicalData(MedicalConstant.HB, MedicalConstant.ASSXHCT_WOMAN, String.valueOf(bean.getAssxhct()));
            } else {
                hb = new MedicalData(MedicalConstant.HB, MedicalConstant.ASSXHDB_MAN, String.valueOf(bean.getAssxhb()));
                hct = new MedicalData(MedicalConstant.HB, MedicalConstant.ASSXHCT_MAN, String.valueOf(bean.getAssxhct()));
            }
            dataList.add(hb);
            dataList.add(hct);
        }

        /**
         * 血脂 四八项
         */
        //葡萄糖
        if (!TextUtils.isEmpty(bean.getGluTree())) {
            MedicalData gluTree = new MedicalData(MedicalConstant.BLOOD_FAT, MedicalConstant.FAT_FLIPIDSGLU, bean.getGluTree());
            dataList.add(gluTree);
        }
        //高密度脂蛋白
        if (!TextUtils.isEmpty(bean.getBlood_hdl())) {
            MedicalData hdl = new MedicalData(MedicalConstant.BLOOD_FAT, MedicalConstant.FAT_FLIPIDSHDL, bean.getBlood_hdl());
            dataList.add(hdl);
        }
        //低密度脂蛋白
        if (!TextUtils.isEmpty(bean.getBlood_ldl())) {
            MedicalData ldl = new MedicalData(MedicalConstant.BLOOD_FAT, MedicalConstant.FAT_FLIPIDSLDL, bean.getBlood_ldl());
            dataList.add(ldl);
        }
        //极低密度脂蛋白
        if (!TextUtils.isEmpty(bean.getBlood_vldl())) {
            MedicalData vldl = new MedicalData(MedicalConstant.BLOOD_FAT, MedicalConstant.VLDL_C, bean.getBlood_vldl());
            dataList.add(vldl);
        }
        //总胆固醇
        if (!TextUtils.isEmpty(bean.getBlood_tc())) {
            MedicalData tc = new MedicalData(MedicalConstant.BLOOD_FAT, MedicalConstant.FAT_FLIPIDSCHOL, bean.getBlood_tc());
            dataList.add(tc);
        }
        //甘油三酯
        if (!TextUtils.isEmpty(bean.getBlood_tg())) {
            MedicalData tg = new MedicalData(MedicalConstant.BLOOD_FAT, MedicalConstant.FAT_FLIPIDSTRIG, bean.getBlood_tg());
            dataList.add(tg);
        }
        //动脉硬化指数
        if (!TextUtils.isEmpty(bean.getBlood_ai())) {
            MedicalData ai = new MedicalData(MedicalConstant.BLOOD_FAT, MedicalConstant.AI, bean.getBlood_ai());
            dataList.add(ai);
        }
        //冠心病危险指数
        if (!TextUtils.isEmpty(bean.getBlood_r_chd())) {
            MedicalData r_chd = new MedicalData(MedicalConstant.BLOOD_FAT, MedicalConstant.R_CHD, bean.getBlood_r_chd());
            dataList.add(r_chd);
        }

        /**C反应蛋白*/
        if (!TextUtils.isEmpty(bean.getFia_crp())) {
            MedicalData crp = new MedicalData(MedicalConstant.INFLAMMATION, MedicalConstant.INDLAMMATION_CRP, bean.getFia_crp());
            dataList.add(crp);
        }
        /**超敏C反应蛋白*/
        if (!TextUtils.isEmpty(bean.getFia_hscrp())) {
            MedicalData hs_crp = new MedicalData(MedicalConstant.INFLAMMATION, MedicalConstant.INDLAMMATION_HSCRP, bean.getFia_hscrp());
            dataList.add(hs_crp);
        }

        /**血清淡粉样蛋白A*/
        if (!TextUtils.isEmpty(bean.getFia_saa())) {
            MedicalData saa = new MedicalData(MedicalConstant.INFLAMMATION, MedicalConstant.INDLAMMATION_SAA, bean.getFia_saa());
            dataList.add(saa);
        }
        /**降钙素原*/
        if (!TextUtils.isEmpty(bean.getFia_pct())) {
            MedicalData pct = new MedicalData(MedicalConstant.INFLAMMATION, MedicalConstant.INDLAMMATION_PCT, bean.getFia_pct());
            dataList.add(pct);
        }
        /**
         * 心肌三项
         */
        //心肌肌钙蛋白
        if (!TextUtils.isEmpty(bean.getFia_ctnl())) {
            MedicalData ctnl = new MedicalData(MedicalConstant.MYOCARDIUMCARDIAC, MedicalConstant.MYOCARDIUM_CTNI, bean.getFia_ctnl());
            dataList.add(ctnl);
        }
        //肌酸激酶同工酶
        if (!TextUtils.isEmpty(bean.getFia_ckmb())) {
            MedicalData ck_mb = new MedicalData(MedicalConstant.MYOCARDIUMCARDIAC, MedicalConstant.MYOCARDIUM_CKMB, bean.getFia_ckmb());
            dataList.add(ck_mb);
        }
        //肌红蛋白
        if (!TextUtils.isEmpty(bean.getFia_myo())) {
            MedicalData myo = new MedicalData(MedicalConstant.MYOCARDIUMCARDIAC, MedicalConstant.MYOCARDIUM_MYO, bean.getFia_myo());
            dataList.add(myo);
        }
        /**身高*/
        if (!TextUtils.isEmpty(bean.getHeight())) {
            MedicalData height = new MedicalData(MedicalConstant.HEI, MedicalConstant.HEIGHT, bean.getHeight());
            dataList.add(height);
        }
        /**体重*/
        if (!TextUtils.isEmpty(bean.getWeight())) {
            MedicalData weight = new MedicalData(MedicalConstant.WEI, MedicalConstant.WEIGHT, bean.getWeight());
            dataList.add(weight);
        }
        /**
         * 糖化血红蛋白
         */
        if (!TextUtils.isEmpty(bean.getNgsp())) {
            MedicalData ngsp = new MedicalData(MedicalConstant.GHB, MedicalConstant.GHB_NGSP, bean.getNgsp());
            dataList.add(ngsp);
        }
        if (!TextUtils.isEmpty(bean.getIfcc())) {
            MedicalData ifcc = new MedicalData(MedicalConstant.GHB, MedicalConstant.GHB_IFCC, bean.getIfcc());
            dataList.add(ifcc);
        }
        if (!TextUtils.isEmpty(bean.getEag())) {
            MedicalData eag = new MedicalData(MedicalConstant.GHB, MedicalConstant.GHB_EAG, bean.getEag());
            dataList.add(eag);
        }

        /**
         * 白细胞 可以为0，故初始值改为-1
         */
        if (bean.getWbc() != -1.0f) {
            MedicalData wbc = new MedicalData(MedicalConstant.WBC, MedicalConstant.WBC_WBC, String.valueOf(bean.getWbc()));
            dataList.add(wbc);
        }
        if (bean.getWbc_lym() != -1.0f) {
            MedicalData lym = new MedicalData(MedicalConstant.WBC, MedicalConstant.WBC_LYM, String.valueOf(bean.getWbc_lym()));
            dataList.add(lym);
        }
        if (bean.getWbc_mon() != -1.0f) {
            MedicalData mou = new MedicalData(MedicalConstant.WBC, MedicalConstant.WBC_MON, String.valueOf(bean.getWbc_mon()));
            dataList.add(mou);
        }
        if (bean.getWbc_neu() != -1.0f) {
            MedicalData neu = new MedicalData(MedicalConstant.WBC, MedicalConstant.WBC_NEU, String.valueOf(bean.getWbc_neu()));
            dataList.add(neu);
        }
        if (bean.getWbc_eos() != -1.0f) {
            MedicalData eos = new MedicalData(MedicalConstant.WBC, MedicalConstant.WBC_EOS, String.valueOf(bean.getWbc_eos()));
            dataList.add(eos);
        }
        if (bean.getWbc_bas() != -1.0f) {
            MedicalData bas = new MedicalData(MedicalConstant.WBC, MedicalConstant.WBC_BAS, String.valueOf(bean.getWbc_bas()));
            dataList.add(bas);
        }

        /**
         * 尿常规 11  14项
         */
        if (bean.getUrinePh() != 0f) {
            MedicalData ph = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_PH, String.valueOf(bean.getUrinePh()));
            MedicalData ubg = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_UBG, bean.getUrineUbg());
            MedicalData bld = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_BLD, bean.getUrineBld());
            MedicalData pro = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_PRO, bean.getUrinePro());
            MedicalData ket = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_KET, bean.getUrineKet());
            MedicalData nit = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_NIT, bean.getUrineNit());
            MedicalData glu = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_GLU, bean.getUrineGlu());
            MedicalData bil = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_BIL, bean.getUrineBil());
            MedicalData leu = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_LEU, bean.getUrineLeu());
            MedicalData sg = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_SG, String.valueOf(bean.getUrineSg()));
            MedicalData vc = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_VC, bean.getUrineVc());
            dataList.add(ph);
            dataList.add(ubg);
            dataList.add(bld);
            dataList.add(pro);
            dataList.add(ket);
            dataList.add(nit);
            dataList.add(glu);
            dataList.add(bil);
            dataList.add(leu);
            dataList.add(sg);
            dataList.add(vc);

            if (!TextUtils.isEmpty(bean.getUrineCre())) {
                MedicalData cr = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_CR, bean.getUrineCre());
                dataList.add(cr);
            }
            if (!TextUtils.isEmpty(bean.getUrineCa())) {
                MedicalData ca = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_CA, bean.getUrineCa());
                dataList.add(ca);
            }
            if (!TextUtils.isEmpty(bean.getUrineMa())) {
                MedicalData ma = new MedicalData(MedicalConstant.URINE, MedicalConstant.URINE_MA, bean.getUrineMa());
                dataList.add(ma);
            }
        }

        /**
         * 心电数据
         */
        if (!TextUtils.isEmpty(bean.getEcgI())) {
            //心率
            MedicalData hr = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_HR, String.valueOf(bean.getHr()));
            //PR间隔
            MedicalData respRr = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.RESPRR, String.valueOf(bean.getRESP()));
            //波形采样率
            MedicalData sample = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.SAMPLE, "500");
            MedicalData p05 = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.P05, "2150");
            MedicalData n05 = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.N05, "1946");
            //采样时间
            MedicalData duration = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.DURATION, "5");
            //波速
            MedicalData waveSpeed = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.WAVE_SPEED, String.valueOf(bean.getWaveSpeed()));
            //波形增益
            MedicalData waveGain = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.WAVE_GAIN, String.valueOf(bean.getWaveGain()));
            //结论
            MedicalData anal = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ANAL, bean.getAnal());

            MedicalData ecgI = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_I, bean.getEcgI());
            MedicalData ecgIi = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_II, bean.getEcgIi());
            MedicalData ecgIii = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_III, bean.getEcgIii());
            MedicalData ecgV1 = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_V1, bean.getEcgV1());
            MedicalData ecgV2 = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_V2, bean.getEcgV2());
            MedicalData ecgV3 = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_V3, bean.getEcgV3());
            MedicalData ecgV4 = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_V4, bean.getEcgV4());
            MedicalData ecgV5 = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_V5, bean.getEcgV5());
            MedicalData ecgV6 = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_V6, bean.getEcgV6());
            MedicalData ecgAvl = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_AVL, bean.getEcgAvl());
            MedicalData ecgAvf = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_AVF, bean.getEcgAvf());
            MedicalData ecgAvr = new MedicalData(MedicalConstant.LEAD_ECG, MedicalConstant.ECG_AVR, bean.getEcgAvr());

            dataList.add(hr);
            dataList.add(respRr);
            dataList.add(sample);
            dataList.add(p05);
            dataList.add(n05);
            dataList.add(duration);
            dataList.add(waveSpeed);
            dataList.add(waveGain);
            dataList.add(anal);
            dataList.add(ecgI);
            dataList.add(ecgIi);
            dataList.add(ecgIii);
            dataList.add(ecgV1);
            dataList.add(ecgV2);
            dataList.add(ecgV3);
            dataList.add(ecgV4);
            dataList.add(ecgV5);
            dataList.add(ecgV6);
            dataList.add(ecgAvl);
            dataList.add(ecgAvf);
            dataList.add(ecgAvr);
        }
        map.put("checkDataRecordInList", dataList);

        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .uploadMedicalData(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    /***
     * 一体机门诊开方
     * @param reason
     * @param advice
     * @param subscriber
     * @return 1：手机开方；2：保存
     */
    public static Subscription phoneClinic(String patientId, String dataId, int clinicType, String
            reason, String advice, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        map.put("batchNumber", dataId);
        map.put("reason", reason);
        map.put("advice", advice);
        map.put("addType", clinicType + "");
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .phoneClinic(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    /***
     * 添加患者
     */
    public static Subscription addPatientInfo(ResidentBean resident, ResponseSubscriber subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("name", resident.getName());
        map.put("telphone", resident.getTelPhone());
        map.put("memberName", resident.getMemberName());
        map.put("telphoneUrgent", resident.getTelPhoneUrgent());//后台用之前紧急联系人电话
        map.put("relation", resident.getRelation());
        map.put("identityCard", resident.getIdentityCard());
        map.put("localAddr", resident.getLocalAddress());
        map.put("sexy", Integer.toString(resident.getSexy()));
        map.put("birthday", resident.getBirthday());
        map.put("age", resident.getAge() + "");
        map.put("remark", resident.getRemark());
        map.put("medicareCard", resident.getMedicareCard());
        map.put("tagIds", resident.getTagIds());
        map.put("hisDrugAllergy", resident.getAllergy());
        map.put("isBindingAccount", resident.getIsBindingAccount());//是否绑定为账号（0否 1是）
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .addPatientInfo(map)
                .compose(RxTransformer.<ResponseBody>defaultSchedulers())
                .subscribe(subscriber);
    }

    /***
     * 获取家庭信息
     * @param patientId
     * @return
     */
    public static Observable<HttpResult> getFamilyMember(String patientId) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .getFamilyMember(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers());
    }

    /***
     * 获取患者信息
     * @param patientId
     * @return
     */
    public static Observable<HttpResult> getPatientInfo(String patientId) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .getPatientInfo(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers());

    }

    /**
     * 添加家人
     *
     * @param patientId
     * @param resident
     */
    public static Subscription addFamilyMember(String patientId, ResidentBean resident, ResponseSubscriber subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        map.put("instituteId", MyApplication.getInstance().getOrgId());
        map.put("name", resident.getName());
        map.put("telphone", resident.getTelPhone());
        map.put("sexy", resident.getSexy() + "");
        map.put("relation", resident.getRelation());
        map.put("medicareCard", resident.getMedicareCard());
        map.put("identityCard", resident.getIdentityCard());
        map.put("localAddr", resident.getLocalAddress());
        map.put("birthday", resident.getBirthday());
        map.put("hisDrugAllergy", resident.getAllergy());
        map.put("age", resident.getAge() + "");
        map.put("tagIds", resident.getTagIds());
        map.put("remark", resident.getRemark());
        map.put("telphoneUrgent", resident.getTelPhoneUrgent());
        map.put("isBindingAccount", resident.getIsBindingAccount());//是否作为账号（0否 1是）
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .addFamilyMember(map)
                .compose(RxTransformer.<ResponseBody>defaultSchedulers()).subscribe(subscriber);

    }

    /***
     * 解除家人关系
     */
    public static Subscription unBindFamilyMember(String patientId, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .unBindFamilyMember(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }


    /**
     * 关联平台已存在的居民
     *
     * @param patientId      用户id
     * @param familyMemberId 家属id
     * @param relation       关系
     * @return
     */
    public static Subscription relevancyPatientInfo(String patientId, String familyMemberId, String relation,
                                                    Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        map.put("familyMemberId", familyMemberId);
        map.put("relation", relation);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .relevancyPatientInfo(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    /**
     * 修改患者
     *
     * @param firstPatientId
     * @param resident
     */
    public static Subscription updatePatientInfo(String firstPatientId, ResidentBean resident, String addTagIds, String deleteTagIds, ResultSubscriber<String> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", resident.getPatientId());
        map.put("name", resident.getName());
        map.put("telphone", resident.getTelPhone());
        map.put("sexy", resident.getSexy() + "");
        if (resident.getAge() > 0)
            map.put("age", String.valueOf(resident.getAge()));
        map.put("correlationId", firstPatientId);
        map.put("relation", resident.getRelation());
        map.put("medicareCard", resident.getMedicareCard());
        map.put("identityCard", resident.getIdentityCard());
        map.put("localAddr", resident.getLocalAddress());
        map.put("birthday", resident.getBirthday());
        map.put("hisDrugAllergy", resident.getAllergy());
        map.put("telphoneUrgent", resident.getTelPhoneUrgent());
        map.put("deleteTagIds", deleteTagIds);
        map.put("remark", resident.getRemark());
        map.put("addTagIds", addTagIds);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .updatePatientInfo(map)
                .compose(RxTransformer.<HttpResult<String>>defaultSchedulers()).subscribe(subscriber);

    }

    /**
     * 更新患者身份证信息
     */
    public static Subscription updatePatientIdentityCard(String firstPatientId, String patientId,
                                                         String identityCard, ResultSubscriber<String> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        map.put("correlationId", firstPatientId);
        map.put("identityCard", identityCard);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .updatePatientInfo(map)
                .compose(RxTransformer.<HttpResult<String>>defaultSchedulers()).subscribe(subscriber);

    }

    /**
     * 获取患者二维码
     *
     * @param patientId
     */
    public static Subscription getPatientQrCode(String patientId, ResultSubscriber<String> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);

        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .getPatientQrCode(map)
                .compose(RxTransformer.<HttpResult<String>>defaultSchedulers()).subscribe(subscriber);

    }

    //获取高级搜索的筛选条件
    public static Subscription screeningLabel(Subscriber<ResponseBody> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .screeningLabel(map)
                .compose(RxTransformer.<ResponseBody>defaultSchedulers())
                .subscribe(subscriber);
    }

    //患者搜索
    public static Subscription residentQuery(int pageNo, String keyWord, Map<String, Object> filterMap,
                                             Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("keyWord", keyWord);
        map.put("pageNo", pageNo + "");
        map.put("pageSize", String.valueOf(AppConfig.PAGE_SIZE));
        if (filterMap != null) {
            if (filterMap.containsKey("signType")) {
                map.put("signType", filterMap.get("signType"));
            }
            if (filterMap.containsKey("isRegister")) {
                map.put("isRegist", filterMap.get("isRegister"));
            }
            if (filterMap.containsKey("tagId")) {
                map.put("tagId", filterMap.get("tagId"));
            }
            if (filterMap.containsKey("serviceId")) {
                map.put("serviceId", filterMap.get("serviceId"));
            }
        }
        //1.输入框无内容，筛选无内容，搜索当前医生
        //2.输入框有内容，筛选无内容，全平台
        //3.只有筛选条件，当前医生
        //4.既有内容，又有筛选，搜当前医生
//        if (!TextUtils.isEmpty(keyWord) && (filterMap == null || filterMap.size() == 0)) {
//            map.put("searchType", "1");
//        } else {
//            map.put("searchType", "0");
//        }
        map.put("searchType", "0");
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .residentQuery(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    //医生添加居民到自己管辖范围
    public static Subscription doctorAddPatientInfo(String patientId, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .doctorAddPatientInfo(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    //根据患者Id查询患者信息
    public static Subscription getPatientInfoById(String patientId, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .getPatientInfoById(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    /**
     * 获取服务包详情
     *
     * @param serviceId  服务包Id
     * @param subscriber 处理
     * @return Subscription
     */
    public static Subscription getServicePackageDetail(int serviceId, ResultSubscriber<SignService> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("serviceId", serviceId + "");
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .getServicePackage(map)
                .compose(RxTransformer.<HttpResult<SignService>>defaultSchedulers())
                .subscribe(subscriber);
    }


    /***
     *获取医生名下服务包  type=1 家庭医生  isGetItems = 0 获取服务包细项目 否则不获取
     * @return
     */
    public static Observable<HttpResult<List<SignService>>> getServicePackageList(int isGetItems) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
//        map.put("isFamilyPage", type);
        map.put("isGetItems", isGetItems + "");
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .getServicePackageList(map)
                .compose(RxTransformer.<HttpResult<List<SignService>>>defaultSchedulers());
    }

    /**
     * 居民所签约的服务包
     * 0 有效服务包 1：失效服务包 2 未生效服务包
     *
     * @param patientId
     * @param subscriber
     * @return
     */
    public static Subscription getPatientSignDetail(String patientId, int isGetItems, ResultSubscriber<List<SignService>> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        map.put("isGetItems", isGetItems + "");
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .patientSignDetail(map)
                .compose(RxTransformer.<HttpResult<List<SignService>>>defaultSchedulers())
                .subscribe(subscriber);
    }

    /**
     * 获取签约协议
     *
     * @param patientId
     * @param subscriber
     * @return
     */
    public static Subscription getServiceAgreement(String patientId, ResultSubscriber<List<AgreementBean>> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .patientAgreement(map)
                .compose(RxTransformer.<HttpResult<List<AgreementBean>>>defaultSchedulers())
                .subscribe(subscriber);
    }

    /**
     * 解约
     *
     * @param patientId 居民ID
     * @param members
     * @return subscription
     */
    public static Observable<HttpResult> removeSign(String patientId, String members, String doctorAutograph) {
        String tokenId = MyApplication.getInstance().getTokenId();
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", tokenId);
        map.put("members", members);
        map.put("patientId", patientId);
        map.put("doctorAutograph", doctorAutograph);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .removeSign(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers());
    }

    /**
     * 查询居民公卫档案
     */
    public static Subscription queryHealthFile(String idNumber, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("idNumber", idNumber);
        map.put("isGetMessage", "1");
        return ServiceFactory
                .getInstance()
                .createService(ApiService.class)
                .queryHealthFile(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    /**
     * 家庭签约
     */
    public static Observable<HttpResult> familySign(String members, String doctorAutograph) {
        String tokenId = MyApplication.getInstance().getTokenId();
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", tokenId);
        map.put("members", members);
        map.put("doctorAutograph", doctorAutograph);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .familySign(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers());
    }

    /***
     *上传服务记录
     * @return
     */
    public static Observable<HttpResult> addServiceRecord(String patientId, int signId, int serviceItemId,
                                                          int serviceType, String dataId, String advise) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        map.put("signId", signId + "");
        map.put("serviceItemId", serviceItemId + "");
        map.put("serviceType", serviceType + "");
        map.put("batchNumber", dataId);
        map.put("advise", advise);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .addServiceRecord(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers());
    }

    /**
     * 检验身份证是否存在
     */
    public static Subscription validateIdCard(String identityCard, int IsAddRelation, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("identityCard", identityCard);
        map.put("isAddRelation", IsAddRelation);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .validateIdCard(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    /**
     * 检验身份证是否存在（新版）
     */
    public static Subscription validateIdentityCard(int familyId, String identityCard, String patientId, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("instituteId", MyApplication.getInstance().getOrgId());
        if (!TextUtils.isEmpty(patientId)) {
            map.put("patientId", patientId);
        }
        if (familyId != 0) {
            map.put("familyId", familyId + "");
        }
        map.put("identityCard", identityCard);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .validateIdentityCard(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    /**
     * 服务记录列表
     *
     * @param patientId
     * @param pageNo
     * @return
     */
    public static Observable<HttpResult> getPerformanceRecord(String patientId, int pageNo, String startTime, String endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("pageNo", pageNo + "");
        map.put("pageSize", AppConfig.PAGE_SIZE + "");
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .getPerformanceRecord(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers());
    }

    /**
     * 历史记录（新接口）
     */
    public static Observable<HttpResult> historyData(String patientId, String dataType, int pageIndex, int pageSize, String startTime, String endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        if (!TextUtils.isEmpty(dataType))
            map.put("checkKindCode", dataType);
        if (pageIndex != -1) {
            map.put("pageNo", pageIndex + "");
            map.put("pageSize", pageSize + "");
        }
        if (!TextUtils.isEmpty(startTime))
            map.put("startTime", startTime);
        if (!TextUtils.isEmpty(endTime))
            map.put("endTime", endTime);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .historyData(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers());
    }

    /**
     * 健康报告列表数据
     */
    public static Observable<HttpResult> getHealthReport(String patientId, String startTime, String endTime, int pageNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("patientId", patientId);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("pageSize", "5");
        map.put("pageNo", pageNo + "");
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .healthReport(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers());
    }

    /**
     * 验证手机号码是否存在
     */
    public static Subscription isValidDatePhoneAccount(String telphone, HttpSubscriber subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("telphone", telphone);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .isValidDatePhoneAccount(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers()).subscribe(subscriber);
    }

    /**
     * 任务处理
     */
    public static Subscription taskExecute(String taskId, String batchNumber, String patientId, String spFeedback, String advise,
                                           int serviceStatus, Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("taskId", taskId);
        map.put("batchNumber", batchNumber);
        map.put("patientId", patientId);
        map.put("serviceStatus", serviceStatus + "");
        map.put("advise", advise);
        map.put("spFeedback", spFeedback);
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .taskExecute(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

    /***
     * 指标解读
     * //measurementProject  0 血压 1体温 2血氧 3血糖 4心率 5尿常规 6血脂 7心电图 8 心肌 9 C反应蛋白
     * 10血清淀粉样蛋白A  11尿酸 12 总胆固醇 13血红蛋白  14糖化血红蛋白  15身高 16 体重
     */
    public static Subscription interpret(ResidentBean resident, String data, String checkName, String measurementProject, String gluStyle,
                                         Subscriber<HttpResult> subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", MyApplication.getInstance().getTokenId());
        map.put("data", data);
        map.put("name", checkName);
        map.put("measurementProject", measurementProject);
        map.put("gluStyle", gluStyle);
        if (resident.getAge() != 0)
            map.put("age", resident.getAge() + "");
        map.put("sex", resident.getSexy() + "");
        return ServiceFactory.getInstance()
                .createService(ApiService.class)
                .interpret(map)
                .compose(RxTransformer.<HttpResult>defaultSchedulers())
                .subscribe(subscriber);
    }

}
