package com.konsung.bean;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.konsung.constant.Configuration;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 测量数据的类
 */
@DatabaseTable(tableName = "t_measure_data")
public class MeasureBean implements Serializable {

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String idCard = ""; //身份证号码
    @DatabaseField(dataType = DataType.STRING)
    private String name = ""; //昵称
    @DatabaseField(id = true, dataType = DataType.LONG)
    private long checkDate = System.currentTimeMillis(); //检测时间 时间戳
    @DatabaseField(dataType = DataType.INTEGER)
    private int hr = 0; //心率 次/分钟
    @DatabaseField(dataType = DataType.INTEGER)
    private int sbp; //收缩压 mmHg
    @DatabaseField(dataType = DataType.INTEGER)
    private int dbp; //舒张压 mmHg
    @DatabaseField(dataType = DataType.INTEGER)
    private int nibPr; //血压脉率
    @DatabaseField(dataType = DataType.INTEGER)
    private int mbp; //平均压 mmHg
    @DatabaseField(dataType = DataType.INTEGER)
    private int spo2; //血氧饱和度 %
    @DatabaseField(dataType = DataType.INTEGER)
    private int pr = 0; //脉率 次/分钟
    @DatabaseField(dataType = DataType.FLOAT)
    private float glu = -1.0f; //血糖 mmol/L
    @DatabaseField
    private Configuration.BtnFlag gluStyle = Configuration.BtnFlag.lift; //血糖测量方式 0:为空腹血糖，1为随机血糖，2为餐后血糖
    @DatabaseField(dataType = DataType.INTEGER)
    private int uricacid = -1; //尿酸 (umol/L)
    @DatabaseField(dataType = DataType.FLOAT)
    private float xzzdgc = -1.0f; //总胆固醇  (mmol/L)
    @DatabaseField(dataType = DataType.FLOAT)
    private float blood = -1.0f; //血脂  (mmol/L)
    @DatabaseField(dataType = DataType.FLOAT)
    private float temp; //体温 摄氏度
    @DatabaseField(dataType = DataType.STRING)
    private String bmi = ""; //体质指数
    @DatabaseField(dataType = DataType.STRING)
    private String height = ""; //身高
    @DatabaseField(dataType = DataType.STRING)
    private String weight = ""; //体重
    //血脂四八项
    @DatabaseField(dataType = DataType.STRING)
    private String gluTree = "";//葡萄糖
    @DatabaseField(dataType = DataType.STRING)
    private String blood_tc = "";//总胆固醇
    @DatabaseField(dataType = DataType.STRING)
    private String blood_tg = "";//甘油三酯
    @DatabaseField(dataType = DataType.STRING)
    private String blood_hdl = "";//高密度脂蛋白
    @DatabaseField(dataType = DataType.STRING)
    private String blood_ldl = "";//低密度脂蛋白
    private String blood_vldl = "";//极地密度蛋白质
    @DatabaseField(dataType = DataType.STRING)
    private String blood_ai = "";//
    @DatabaseField(dataType = DataType.STRING)
    private String blood_r_chd = "";//
    //干式荧光免疫分析仪 (炎症检测)
    @DatabaseField(dataType = DataType.STRING)
    private String fia_pct = "";//降钙素原
    @DatabaseField(dataType = DataType.STRING)
    private String fia_hscrp = "";//超敏C反应蛋白
    @DatabaseField(dataType = DataType.STRING)
    private String fia_crp = "";//C反应蛋白
    @DatabaseField(dataType = DataType.STRING)
    private String fia_saa = "";//血清淀粉样蛋白A

    //干式荧光免疫分析仪 (心肌三项检测)
    @DatabaseField(dataType = DataType.STRING)
    private String fia_ctnl = "";//心肌肌钙蛋白
    @DatabaseField(dataType = DataType.STRING)
    private String fia_ckmb = "";//肌酸激酶同工酶
    @DatabaseField(dataType = DataType.STRING)
    private String fia_myo = "";//肌红蛋白


    @DatabaseField(dataType = DataType.INTEGER)
    private int assxhb = 0; //血常规--血红蛋白 (g/L)
    @DatabaseField(dataType = DataType.INTEGER)
    private int assxhct; //血常规--红细胞压积值

    @DatabaseField(dataType = DataType.DOUBLE)
    private double rESP = 0.0; //呼吸率

    //白细胞
    @DatabaseField(dataType = DataType.FLOAT)
    private float wbc = -1.0f;//血常规--白细胞
    @DatabaseField(dataType = DataType.FLOAT)
    private float wbc_lym = -1.0f;
    @DatabaseField(dataType = DataType.FLOAT)
    private float wbc_mon = -1.0f;
    @DatabaseField(dataType = DataType.FLOAT)
    private float wbc_neu = -1.0f;
    @DatabaseField(dataType = DataType.FLOAT)
    private float wbc_eos = -1.0f;
    @DatabaseField(dataType = DataType.FLOAT)
    private float wbc_bas = -1.0f;

    //糖化血红蛋白
    @DatabaseField(dataType = DataType.STRING)
    private String ngsp = ""; //糖化血红蛋白NGSP值(4.3-6.0%)
    @DatabaseField(dataType = DataType.STRING)
    private String ifcc = ""; //糖化血红蛋白IFCC值(23.5-42.1mmol/mol).
    @DatabaseField(dataType = DataType.STRING)
    private String eag = ""; //糖化血红蛋白eag值(76.7-125.5mg/dl).

    @DatabaseField(dataType = DataType.FLOAT)
    private float urinePh = 0f; //尿常规酸碱度
    @DatabaseField(dataType = DataType.STRING)
    private String urineUbg = ""; //尿常规尿胆原
    @DatabaseField(dataType = DataType.STRING)
    private String urineBld = ""; //尿常规隐血
    @DatabaseField(dataType = DataType.STRING)
    private String urinePro = ""; //尿常规尿蛋白
    @DatabaseField(dataType = DataType.STRING)
    private String urineKet = ""; //尿常规酮体
    @DatabaseField(dataType = DataType.STRING)
    private String urineNit = ""; //尿常规亚硝酸盐
    @DatabaseField(dataType = DataType.STRING)
    private String urineGlu = ""; //尿常规尿糖
    @DatabaseField(dataType = DataType.STRING)
    private String urineBil = ""; //尿常规胆红素
    @DatabaseField(dataType = DataType.STRING)
    private String urineLeu = ""; //尿常规白细胞
    @DatabaseField(dataType = DataType.DOUBLE)
    private double urineSg = 0.0f; //尿常规尿比密
    @DatabaseField(dataType = DataType.STRING)
    private String urineVc = ""; //尿常规维生素c
    @DatabaseField(dataType = DataType.STRING)
    private String urineAsc = ""; //抗坏血酸
    @DatabaseField(dataType = DataType.STRING)
    private String urineCre = ""; //肌酐（mmol/L）
    @DatabaseField(dataType = DataType.STRING)
    private String urineCa = ""; //尿钙（mmol/L）
    @DatabaseField(dataType = DataType.STRING)
    private String urineMa = ""; //微量白蛋白  （mg/dL）

    @DatabaseField(dataType = DataType.STRING)
    private String anal = ""; //心电图自动诊断结果
    @DatabaseField(dataType = DataType.STRING)
    private String sample = ""; //波形采样率
    @DatabaseField(dataType = DataType.STRING)
    private String p05 = "";
    @DatabaseField(dataType = DataType.STRING)
    private String n05 = "";
    @DatabaseField(dataType = DataType.STRING)
    private String duration = ""; //采集时间
    @DatabaseField(dataType = DataType.FLOAT)
    private float waveSpeed = 0.0f;//波速，单位mm/s
    @DatabaseField(dataType = DataType.FLOAT)
    private float waveGain = 0.0f;//波形增益
    //心电波形数据 ,以逗号分割的坐标数。如：1024,1055,985.......
    @DatabaseField(dataType = DataType.STRING)
    private String ecgI = "";
    @DatabaseField(dataType = DataType.STRING)
    private String ecgIi = "";
    @DatabaseField(dataType = DataType.STRING)
    private String ecgIii = "";
    @DatabaseField(dataType = DataType.STRING)
    private String ecgAvr = "";
    @DatabaseField(dataType = DataType.STRING)
    private String ecgAvf = "";
    @DatabaseField(dataType = DataType.STRING)
    private String ecgAvl = "";
    @DatabaseField(dataType = DataType.STRING)
    private String ecgV1 = "";
    @DatabaseField(dataType = DataType.STRING)
    private String ecgV2 = "";
    @DatabaseField(dataType = DataType.STRING)
    private String ecgV3 = "";
    @DatabaseField(dataType = DataType.STRING)
    private String ecgV4 = "";
    @DatabaseField(dataType = DataType.STRING)
    private String ecgV5 = "";
    @DatabaseField(dataType = DataType.STRING)
    private String ecgV6 = "";
    @DatabaseField(dataType = DataType.STRING)
    private String filePath = "";
    @DatabaseField(dataType = DataType.STRING)
    private String respRr = "";

    @DatabaseField(dataType = DataType.STRING)
    private String fhr1 = ""; //胎心1
    @DatabaseField(dataType = DataType.STRING)
    private String fhr2 = ""; //胎心2
    @DatabaseField(dataType = DataType.STRING)
    private String toco = ""; //宫颈
    @DatabaseField(dataType = DataType.STRING)
    private String dataId = ""; //测量id
    @DatabaseField(dataType = DataType.STRING)
    private String patientId = ""; //患者id
    @DatabaseField(dataType = DataType.INTEGER)
    private int gluType = 0; //餐前餐后血糖
    //是否已经上传
    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean upload = false;
    //心电图字节缓存
    private ArrayList<String> wave1 = new ArrayList<>();
    private ArrayList<String> wave2 = new ArrayList<>();
    private ArrayList<String> wave3 = new ArrayList<>();
    private ArrayList<String> wave4 = new ArrayList<>();
    private ArrayList<String> wave5 = new ArrayList<>();
    private ArrayList<String> wave6 = new ArrayList<>();
    private ArrayList<String> wave7 = new ArrayList<>();
    private ArrayList<String> wave8 = new ArrayList<>();
    private ArrayList<String> wave9 = new ArrayList<>();
    private ArrayList<String> wave10 = new ArrayList<>();
    private ArrayList<String> wave11 = new ArrayList<>();
    private ArrayList<String> wave12 = new ArrayList<>();
    private ArrayList<String> wave13 = new ArrayList<>();
    private ArrayList<String> wave14 = new ArrayList<>();
    private ArrayList<String> wave15 = new ArrayList<>();

    public MeasureBean() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getGluType() {
        return gluType;
    }

    public void setGluType(int gluType) {
        this.gluType = gluType;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    /**
     * 获取checkDay的值
     *
     * @return checkDay checkDay值
     */
    public long getCheckDay() {
        return checkDate;
    }

    /**
     * 设置checkDay的值
     *
     * @paramcheckDay checkDay值
     */
    public void setCheckDay(long checkDate) {
        this.checkDate = checkDate;
    }

    /**
     * 获取hr的值
     *
     * @return hr hr值
     */
    public int getHr() {
        return hr;
    }

    /**
     * 设置hr的值
     *
     * @param hr hr值
     */
    public void setHr(int hr) {
        this.hr = hr;
    }

    /**
     * 获取sbp的值
     *
     * @return sbp sbp值
     */
    public int getSbp() {
        return sbp;
    }

    /**
     * 设置sbp的值
     *
     * @param sbp sbp值
     */
    public void setSbp(int sbp) {
        this.sbp = sbp;
    }

    /**
     * 获取dbp的值
     *
     * @return dbp dbp值
     */
    public int getDbp() {
        return dbp;
    }

    /**
     * 设置dbp的值
     *
     * @param dbp dbp值
     */
    public void setDbp(int dbp) {
        this.dbp = dbp;
    }

    /**
     * 获取mbp的值
     *
     * @return mbp mbp值
     */
    public int getMbp() {
        return mbp;
    }

    /**
     * 设置mbp的值
     *
     * @param mbp mbp值
     */
    public void setMbp(int mbp) {
        this.mbp = mbp;
    }

    /**
     * 获取spo2的值
     *
     * @return spo2 spo2值
     */
    public int getSpo2() {
        return spo2;
    }

    /**
     * 设置spo2的值
     *
     * @param spo2 spo2值
     */
    public void setSpo2(int spo2) {
        this.spo2 = spo2;
    }

    /**
     * 获取pr的值
     *
     * @return pr pr值
     */
    public int getPr() {
        return pr;
    }

    /**
     * 设置pr的值
     *
     * @param pr pr值
     */
    public void setPr(int pr) {
        this.pr = pr;
    }

    /**
     * 获取glu的值
     *
     * @return glu glu值
     */
    public float getGlu() {
        return glu;
    }

    /**
     * 设置glu的值
     *
     * @param glu glu值
     */
    public void setGlu(float glu) {
        this.glu = glu;
    }

    /**
     * 获取temp的值
     *
     * @return temp temp值
     */
    public float getTemp() {
        return temp;
    }

    /**
     * 设置temp的值
     *
     * @param temp temp值
     */
    public void setTemp(float temp) {
        this.temp = temp;
    }

    /**
     * 获取bmi的值
     *
     * @return bmi bmi值
     */
    public String getBmi() {
        return bmi;
    }

    /**
     * 设置bmi的值
     *
     * @param bmi bmi值
     */
    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public int getAssxhb() {
        return assxhb;
    }

    public void setAssxhb(int assxhb) {
        this.assxhb = assxhb;
    }

    public int getAssxhct() {
        return assxhct;
    }

    public void setAssxhct(int assxhct) {
        this.assxhct = assxhct;
    }

    public float getWbc() {
        return wbc;
    }

    public void setWbc(float wbc) {
        this.wbc = wbc;
    }

    public float getWbc_lym() {
        return wbc_lym;
    }

    public void setWbc_lym(float wbc_lym) {
        this.wbc_lym = wbc_lym;
    }

    public float getWbc_mon() {
        return wbc_mon;
    }

    public void setWbc_mon(float wbc_mon) {
        this.wbc_mon = wbc_mon;
    }

    public float getWbc_neu() {
        return wbc_neu;
    }

    public void setWbc_neu(float wbc_neu) {
        this.wbc_neu = wbc_neu;
    }

    public float getWbc_eos() {
        return wbc_eos;
    }

    public void setWbc_eos(float wbc_eos) {
        this.wbc_eos = wbc_eos;
    }

    public float getWbc_bas() {
        return wbc_bas;
    }

    public void setWbc_bas(float wbc_bas) {
        this.wbc_bas = wbc_bas;
    }

    /**
     * 获取urinePh的值
     *
     * @return urinePh urinePh值
     */
    public float getUrinePh() {
        return urinePh;
    }

    /**
     * 设置urinePh的值
     *
     * @param urinePh urinePh值
     */
    public void setUrinePh(float urinePh) {
        this.urinePh = urinePh;
    }

    /**
     * 获取urineUbg的值
     *
     * @return urineUbg urineUbg值
     */
    public String getUrineUbg() {
        return urineUbg;
    }

    /**
     * 设置urineUbg的值
     *
     * @param urineUbg urineUbg值
     */
    public void setUrineUbg(String urineUbg) {
        this.urineUbg = urineUbg;
    }

    /**
     * 获取urineBld的值
     *
     * @return urineBld urineBld值
     */
    public String getUrineBld() {
        return urineBld;
    }

    /**
     * 设置urineBld的值
     *
     * @param urineBld urineBld值
     */
    public void setUrineBld(String urineBld) {
        this.urineBld = urineBld;
    }

    /**
     * 获取urinePro的值
     *
     * @return urinePro urinePro值
     */
    public String getUrinePro() {
        return urinePro;
    }

    /**
     * 设置urinePro的值
     *
     * @param urinePro urinePro值
     */
    public void setUrinePro(String urinePro) {
        this.urinePro = urinePro;
    }

    /**
     * 获取urineKet的值
     *
     * @return urineKet urineKet值
     */
    public String getUrineKet() {
        return urineKet;
    }

    /**
     * 设置urineKet的值
     *
     * @param urineKet urineKet值
     */
    public void setUrineKet(String urineKet) {
        this.urineKet = urineKet;
    }

    /**
     * 获取urineNit的值
     *
     * @return urineNit urineNit值
     */
    public String getUrineNit() {
        return urineNit;
    }

    /**
     * 设置urineNit的值
     *
     * @param urineNit urineNit值
     */
    public void setUrineNit(String urineNit) {
        this.urineNit = urineNit;
    }

    /**
     * 获取urineGlu的值
     *
     * @return urineGlu urineGlu值
     */
    public String getUrineGlu() {
        return urineGlu;
    }

    /**
     * 设置urineGlu的值
     *
     * @param urineGlu urineGlu值
     */
    public void setUrineGlu(String urineGlu) {
        this.urineGlu = urineGlu;
    }

    /**
     * 获取urineBil的值
     *
     * @return urineBil urineBil值
     */
    public String getUrineBil() {
        return urineBil;
    }

    /**
     * 设置urineBil的值
     *
     * @param urineBil urineBil值
     */
    public void setUrineBil(String urineBil) {
        this.urineBil = urineBil;
    }

    /**
     * 获取urineLeu的值
     *
     * @return urineLeu urineLeu值
     */
    public String getUrineLeu() {
        return urineLeu;
    }

    /**
     * 设置urineLeu的值
     *
     * @param urineLeu urineLeu值
     */
    public void setUrineLeu(String urineLeu) {
        this.urineLeu = urineLeu;
    }

    /**
     * 获取urineSg的值
     *
     * @return urineSg urineSg值
     */
    public double getUrineSg() {
        return urineSg;
    }

    /**
     * 设置urineSg的值
     *
     * @param urineSg urineSg值
     */
    public void setUrineSg(double urineSg) {
        this.urineSg = urineSg;
    }

    public String getUrineAsc() {
        return urineAsc;
    }

    public void setUrineAsc(String urineAsc) {
        this.urineAsc = urineAsc;
    }

    public String getUrineVc() {
        return urineVc;
    }

    public void setUrineVc(String urineVc) {
        this.urineVc = urineVc;
    }

    /**
     * 获取urineCre的值
     *
     * @return urineCre urineCre值
     */
    public String getUrineCre() {
        return urineCre;
    }

    /**
     * 设置urineCre的值
     *
     * @param urineCre urineCre值
     */
    public void setUrineCre(String urineCre) {
        this.urineCre = urineCre;
    }

    /**
     * 获取urineCa的值
     *
     * @return urineCa urineCa值
     */
    public String getUrineCa() {
        return urineCa;
    }

    /**
     * 设置urineCa的值
     *
     * @param urineCa urineCa值
     */
    public void setUrineCa(String urineCa) {
        this.urineCa = urineCa;
    }

    public String getUrineMa() {
        return urineMa;
    }

    public void setUrineMa(String urineMa) {
        this.urineMa = urineMa;
    }

    /**
     * 获取uricacid的值
     *
     * @return uricacid uricacid值
     */
    public int getUricacid() {
        return uricacid;
    }

    /**
     * 设置uricacid的值
     *
     * @param uricacid uricacid值
     */
    public void setUricacid(int uricacid) {
        this.uricacid = uricacid;
    }


    /**
     * 获取xzzdgc的值
     *
     * @return xzzdgc xzzdgc值
     */
    public float getXzzdgc() {
        return xzzdgc;
    }

    /**
     * 设置xzzdgc的值
     *
     * @param xzzdgc xzzdgc值
     */
    public void setXzzdgc(float xzzdgc) {
        this.xzzdgc = xzzdgc;
    }

    /**
     * 获取anal的值
     *
     * @return anal anal值
     */
    public String getAnal() {
        return anal;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 设置anal的值
     *
     * @param anal anal值
     */
    public void setAnal(String anal) {
        this.anal = anal;
    }

    /**
     * 获取sample的值
     *
     * @return sample sample值
     */
    public String getSample() {
        return sample;
    }

    /**
     * 设置sample的值
     *
     * @param sample sample值
     */
    public void setSample(String sample) {
        this.sample = sample;
    }

    /**
     * 获取p05的值
     *
     * @return p05 p05值
     */
    public String getP05() {
        return p05;
    }

    public float getWaveSpeed() {
        return waveSpeed;
    }

    public void setWaveSpeed(float waveSpeed) {
        this.waveSpeed = waveSpeed;
    }

    public float getWaveGain() {
        return waveGain;
    }

    public void setWaveGain(float waveGain) {
        this.waveGain = waveGain;
    }

    /**
     * 设置p05的值
     *
     * @param p05 p05值
     */
    public void setP05(String p05) {
        this.p05 = p05;
    }

    /**
     * 获取n05的值
     *
     * @return n05 n05值
     */
    public String getN05() {
        return n05;
    }

    /**
     * 设置n05的值
     *
     * @param n05 n05值
     */
    public void setN05(String n05) {
        this.n05 = n05;
    }

    /**
     * 获取duration的值
     *
     * @return duration duration值
     */
    public String getDuration() {
        return duration;
    }

    /**
     * 设置duration的值
     *
     * @param duration duration值
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * 获取ecgI的值
     *
     * @return ecgI ecgI值
     */
    public String getEcgI() {
        return ecgI;
    }

    /**
     * 设置ecgI的值
     *
     * @param ecgI ecgI值
     */
    public void setEcgI(String ecgI) {
        this.ecgI = ecgI;
    }

    /**
     * 获取ecgIi的值
     *
     * @return ecgIi ecgIi值
     */
    public String getEcgIi() {
        return ecgIi;
    }

    /**
     * 设置ecgIi的值
     *
     * @param ecgIi ecgIi值
     */
    public void setEcgIi(String ecgIi) {
        this.ecgIi = ecgIi;
    }

    /**
     * 获取ecgIii的值
     *
     * @return ecgIii ecgIii值
     */
    public String getEcgIii() {
        return ecgIii;
    }

    /**
     * 设置ecgIii的值
     *
     * @param ecgIii ecgIii值
     */
    public void setEcgIii(String ecgIii) {
        this.ecgIii = ecgIii;
    }

    /**
     * 获取ecgAvr的值
     *
     * @return ecgAvr ecgAvr值
     */
    public String getEcgAvr() {
        return ecgAvr;
    }

    /**
     * 设置ecgAvr的值
     *
     * @param ecgAvr ecgAvr值
     */
    public void setEcgAvr(String ecgAvr) {
        this.ecgAvr = ecgAvr;
    }

    /**
     * 获取ecgAvf的值
     *
     * @return ecgAvf ecgAvf值
     */
    public String getEcgAvf() {
        return ecgAvf;
    }

    /**
     * 设置ecgAvf的值
     *
     * @param ecgAvf ecgAvf值
     */
    public void setEcgAvf(String ecgAvf) {
        this.ecgAvf = ecgAvf;
    }

    /**
     * 获取ecgAvl的值
     *
     * @return ecgAvl ecgAvl值
     */
    public String getEcgAvl() {
        return ecgAvl;
    }

    /**
     * 设置ecgAvl的值
     *
     * @param ecgAvl ecgAvl值
     */
    public void setEcgAvl(String ecgAvl) {
        this.ecgAvl = ecgAvl;
    }

    /**
     * 获取ecgV1的值
     *
     * @return ecgV1 ecgV1值
     */
    public String getEcgV1() {
        return ecgV1;
    }

    /**
     * 设置ecgV1的值
     *
     * @param ecgV1 ecgV1值
     */
    public void setEcgV1(String ecgV1) {
        this.ecgV1 = ecgV1;
    }

    /**
     * 获取ecgV2的值
     *
     * @return ecgV2 ecgV2值
     */
    public String getEcgV2() {
        return ecgV2;
    }

    /**
     * 设置ecgV2的值
     *
     * @param ecgV2 ecgV2值
     */
    public void setEcgV2(String ecgV2) {
        this.ecgV2 = ecgV2;
    }

    /**
     * 获取ecgV3的值
     *
     * @return ecgV3 ecgV3值
     */
    public String getEcgV3() {
        return ecgV3;
    }

    /**
     * 设置ecgV3的值
     *
     * @param ecgV3 ecgV3值
     */
    public void setEcgV3(String ecgV3) {
        this.ecgV3 = ecgV3;
    }

    /**
     * 获取ecgV4的值
     *
     * @return ecgV4 ecgV4值
     */
    public String getEcgV4() {
        return ecgV4;
    }

    /**
     * 设置ecgV4的值
     *
     * @param ecgV4 ecgV4值
     */
    public void setEcgV4(String ecgV4) {
        this.ecgV4 = ecgV4;
    }

    /**
     * 获取ecgV5的值
     *
     * @return ecgV5 ecgV5值
     */
    public String getEcgV5() {
        return ecgV5;
    }

    /**
     * 设置ecgV5的值
     *
     * @param ecgV5 ecgV5值
     */
    public void setEcgV5(String ecgV5) {
        this.ecgV5 = ecgV5;
    }

    /**
     * 获取ecgV6的值
     *
     * @return ecgV6 ecgV6值
     */
    public String getEcgV6() {
        return ecgV6;
    }

    /**
     * 设置ecgV6的值
     *
     * @param ecgV6 ecgV6值
     */
    public void setEcgV6(String ecgV6) {
        this.ecgV6 = ecgV6;
    }

    /**
     * 获取respRr的值
     *
     * @return respRr respRr值
     */
    public String getRespRr() {
        return respRr;
    }

    /**
     * 设置respRr的值
     *
     * @param respRr respRr值
     */
    public void setRespRr(String respRr) {
        this.respRr = respRr;
    }

    /**
     * 这里需要修改，ecg数据会一直发送，只需保存最后10分钟的数据
     *
     * @param param 标记
     * @param wave  存储的字段
     */
    public void setEcgWave(int param, String wave) {
        int ecgsize = 10; //记录时长
        switch (param) {
            case 1:
                wave1.add(wave);
                if (wave1.size() > ecgsize) {
                    wave1.remove(0);
                }
                ecgI = "";
                for (int i = 0; i < wave1.size(); i++) {
                    ecgI += wave1.get(i);
                }
                break;
            case 2:
                wave2.add(wave);
                if (wave2.size() > ecgsize) {
                    wave2.remove(0);
                }
                ecgIi = "";
                for (int i = 0; i < wave2.size(); i++) {
                    ecgIi += wave2.get(i);
                }
                break;
            case 3:
                wave3.add(wave);
                if (wave3.size() > ecgsize) {
                    wave3.remove(0);
                }
                ecgIii = "";
                for (int i = 0; i < wave3.size(); i++) {
                    ecgIii += wave3.get(i);
                }
                break;
            case 4:
                wave4.add(wave);
                if (wave4.size() > ecgsize) {
                    wave4.remove(0);
                }
                ecgAvr = "";
                for (int i = 0; i < wave4.size(); i++) {
                    ecgAvr += wave4.get(i);
                }
                break;
            case 5:
                wave5.add(wave);
                if (wave5.size() > ecgsize) {
                    wave5.remove(0);
                }
                ecgAvl = "";
                for (int i = 0; i < wave5.size(); i++) {
                    ecgAvl += wave5.get(i);
                }
                break;
            case 6:
                wave6.add(wave);
                if (wave6.size() > ecgsize) {
                    wave6.remove(0);
                }
                ecgAvf = "";
                for (int i = 0; i < wave6.size(); i++) {
                    ecgAvf += wave6.get(i);
                }
                break;
            case 7:
                wave7.add(wave);
                if (wave7.size() > ecgsize) {
                    wave7.remove(0);
                }
                ecgV1 = "";
                for (int i = 0; i < wave7.size(); i++) {
                    ecgV1 += wave7.get(i);
                }
                break;
            case 8:
                wave8.add(wave);
                if (wave8.size() > ecgsize) {
                    wave8.remove(0);
                }
                ecgV2 = "";
                for (int i = 0; i < wave8.size(); i++) {
                    ecgV2 += wave8.get(i);
                }
                break;
            case 9:
                wave9.add(wave);
                if (wave9.size() > ecgsize) {
                    wave9.remove(0);
                }
                ecgV3 = "";
                for (int i = 0; i < wave9.size(); i++) {
                    ecgV3 += wave9.get(i);
                }
                break;
            case 10:
                wave10.add(wave);
                if (wave10.size() > ecgsize) {
                    wave10.remove(0);
                }
                ecgV4 = "";
                for (int i = 0; i < wave10.size(); i++) {
                    ecgV4 += wave10.get(i);
                }
                break;
            case 11:
                wave11.add(wave);
                if (wave11.size() > ecgsize) {
                    wave11.remove(0);
                }
                ecgV5 = "";
                for (int i = 0; i < wave11.size(); i++) {
                    ecgV5 += wave11.get(i);
                }
                break;
            case 12:
                wave12.add(wave);
                if (wave12.size() > ecgsize) {
                    wave12.remove(0);
                }
                ecgV6 = "";
                for (int i = 0; i < wave12.size(); i++) {
                    ecgV6 += wave12.get(i);
                }
                break;
            case 13:
                wave13.add(wave);
                if (wave13.size() > ecgsize) {
                    wave13.remove(0);
                }
                fhr1 = "";
                for (int i = 0; i < wave13.size(); i++) {
                    fhr1 += wave13.get(i);
                }
                break;
            case 14:
                wave14.add(wave);
                if (wave14.size() > ecgsize) {
                    wave14.remove(0);
                }
                fhr2 = "";
                for (int i = 0; i < wave14.size(); i++) {
                    fhr2 += wave14.get(i);
                }
                break;
            case 15:
                wave15.add(wave);
                if (wave15.size() > ecgsize) {
                    wave15.remove(0);
                }
                toco = "";
                for (int i = 0; i < wave15.size(); i++) {
                    toco += wave15.get(i);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取nibPr的值
     *
     * @return nibPr nibPr值
     */
    public int getNibPr() {
        return nibPr;
    }

    /**
     * 设置nibPr的值
     *
     * @param nibPr nibPr值
     */
    public void setNibPr(int nibPr) {
        this.nibPr = nibPr;
    }

    /**
     * 获取idCard的值
     *
     * @return idCard idCard值
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * 设置idCard的值
     *
     * @param idCard idCard值
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    /**
     * 获取rESP的值
     *
     * @return rESP rESP值
     */
    public double getRESP() {
        return rESP;
    }

    /**
     * 设置rESP的值
     *
     * @param rESP rESP值
     */
    public void setRESP(double rESP) {
        this.rESP = rESP;
    }

    /**
     * 获取fhr1的值
     *
     * @return fhr1 fhr1值
     */
    public String getFhr1() {
        return fhr1;
    }

    /**
     * 设置fhr1的值
     *
     * @param fhr1 fhr1值
     */
    public void setFhr1(String fhr1) {
        this.fhr1 = fhr1;
    }

    /**
     * 获取fhr2的值
     *
     * @return fhr2 fhr2值
     */
    public String getFhr2() {
        return fhr2;
    }

    /**
     * 设置fhr2的值
     *
     * @param fhr2 fhr2值
     */
    public void setFhr2(String fhr2) {
        this.fhr2 = fhr2;
    }

    /**
     * 获取toco的值
     *
     * @return toco toco值
     */
    public String getToco() {
        return toco;
    }

    /**
     * 设置toco的值
     *
     * @param toco toco值
     */
    public void setToco(String toco) {
        this.toco = toco;
    }

    public Configuration.BtnFlag getGluStyle() {
        return gluStyle;
    }

    public void setGluStyle(Configuration.BtnFlag gluStyle) {
        this.gluStyle = gluStyle;
    }

    public float getBlood() {
        return blood;
    }

    public void setBlood(float blood) {
        this.blood = blood;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBlood_tc() {
        return blood_tc;
    }

    public void setBlood_tc(String blood_tc) {
        this.blood_tc = blood_tc;
    }

    public String getBlood_tg() {
        return blood_tg;
    }

    public void setBlood_tg(String blood_tg) {
        this.blood_tg = blood_tg;
    }

    public String getBlood_hdl() {
        return blood_hdl;
    }

    public void setBlood_hdl(String blood_hdl) {
        this.blood_hdl = blood_hdl;
    }

    public String getBlood_ldl() {
        return blood_ldl;
    }

    public void setBlood_ldl(String blood_ldl) {
        this.blood_ldl = blood_ldl;
    }

    public String getBlood_vldl() {
        return blood_vldl;
    }

    public void setBlood_vldl(String blood_vldl) {
        this.blood_vldl = blood_vldl;
    }

    public String getBlood_ai() {
        return blood_ai;
    }

    public void setBlood_ai(String blood_ai) {
        this.blood_ai = blood_ai;
    }

    public String getBlood_r_chd() {
        return blood_r_chd;
    }

    public void setBlood_r_chd(String blood_r_chd) {
        this.blood_r_chd = blood_r_chd;
    }

    public String getFia_pct() {
        return fia_pct;
    }

    public void setFia_pct(String fia_pct) {
        this.fia_pct = fia_pct;
    }

    public String getFia_hscrp() {
        return fia_hscrp;
    }

    public void setFia_hscrp(String fia_hscrp) {
        this.fia_hscrp = fia_hscrp;
    }

    public String getFia_crp() {
        return fia_crp;
    }

    public void setFia_crp(String fia_crp) {
        this.fia_crp = fia_crp;
    }

    public String getFia_saa() {
        return fia_saa;
    }

    public void setFia_saa(String fia_saa) {
        this.fia_saa = fia_saa;
    }

    public String getFia_ctnl() {
        return fia_ctnl;
    }

    public void setFia_ctnl(String fia_ctnl) {
        this.fia_ctnl = fia_ctnl;
    }

    public String getFia_ckmb() {
        return fia_ckmb;
    }

    public void setFia_ckmb(String fia_ckmb) {
        this.fia_ckmb = fia_ckmb;
    }

    public String getFia_myo() {
        return fia_myo;
    }

    public void setFia_myo(String fia_myo) {
        this.fia_myo = fia_myo;
    }

    public String getGluTree() {
        return gluTree;
    }

    public void setGluTree(String gluTree) {
        this.gluTree = gluTree;
    }

    public String getNgsp() {
        return ngsp;
    }

    public void setNgsp(String ngsp) {
        this.ngsp = ngsp;
    }

    public String getIfcc() {
        return ifcc;
    }

    public void setIfcc(String ifcc) {
        this.ifcc = ifcc;
    }

    public String getEag() {
        return eag;
    }

    public void setEag(String eag) {
        this.eag = eag;
    }

}
