package com.health2world.aio.bean;

import com.google.gson.annotations.SerializedName;
import com.health2world.aio.MyApplication;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.config.KonsungConfig;
import com.health2world.aio.config.MeasureConfig;
import com.health2world.aio.util.Logger;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

import java.io.Serializable;

import aio.health2world.utils.SPUtils;

import static com.health2world.aio.config.AppConfig.DEFAULT_MEASURE_CONFIG;

/**
 * 医生信息
 * Created by Administrator on 2018/7/3 0003.
 */
@DatabaseTable(tableName = "t_doctor")
public class DoctorBean implements Serializable {

    @DatabaseField(dataType = DataType.STRING)
    private String tokenId;
    @DatabaseField(id = true, dataType = DataType.INTEGER)
    private int doctorId;
    @DatabaseField(dataType = DataType.LONG)
    private long time;//登录时间
    @DatabaseField(dataType = DataType.INTEGER)
    private int insId = -1;
    @DatabaseField(dataType = DataType.STRING)
    private String name;
    @DatabaseField(dataType = DataType.STRING)
    private String doctorCode;//医生码
    @DatabaseField(dataType = DataType.STRING)
    private String hxId;
    @DatabaseField(dataType = DataType.STRING)
    @SerializedName("phone")
    private String account;
    @DatabaseField(dataType = DataType.STRING)
    private String password;
    @DatabaseField(dataType = DataType.STRING)
    private String portrait;
    @DatabaseField(dataType = DataType.STRING)
    private String qcodeInfo;
    @DatabaseField(dataType = DataType.STRING)
    private String doctorType;//0诊所医生 1诊所护士 2平台医生 3平台护士 4诊所管理员 5家庭医生
    @DatabaseField(dataType = DataType.STRING)
    private String instituteId;
    @DatabaseField(dataType = DataType.STRING)
    private String teamName;
    @DatabaseField(dataType = DataType.STRING)
    private String empId;
    @DatabaseField(dataType = DataType.STRING)
    private String orgId;
    @DatabaseField(dataType = DataType.INTEGER)
    private int is_archives = 0; //是否需要强制建档 0：否1：是.
    @DatabaseField(dataType = DataType.INTEGER)
    private int is_edit_archives = 0; //是否允许修改档案信息0：否1：是.
    @DatabaseField(dataType = DataType.INTEGER)
    private int is_login_repeat = 0; // 账号是否允许重复登录0：否1：是.
    @DatabaseField(dataType = DataType.INTEGER)
    private int label_match_pkg = 0; //是否标签匹配服务包才能签约0：否1：是.
    @DatabaseField(dataType = DataType.INTEGER)
    private int signMode = -1;//签约模式（1.一包一协议 2. 单签 3.多签）
    @DatabaseField(dataType = DataType.STRING)
    private String ksUrl;//康尚公卫接口地址（每个地区不一样）


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getInsId() {
        return insId;
    }

    public void setInsId(int insId) {
        this.insId = insId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHxId() {
        return hxId;
    }

    public void setHxId(String hxId) {
        this.hxId = hxId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(String doctorType) {
        this.doctorType = doctorType;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public int getIsArchives() {
        return is_archives;
    }

    public void setIsArchives(int isArchives) {
        this.is_archives = isArchives;
    }

    public int getIsEditArchives() {
        return is_edit_archives;
    }

    public void setIsEditArchives(int isEditArchives) {
        this.is_edit_archives = isEditArchives;
    }

    public int getIsLoginRepeat() {
        return is_login_repeat;
    }

    public void setIsLoginRepeat(int isLoginRepeat) {
        this.is_login_repeat = isLoginRepeat;
    }

    public int getLabelMatchPkg() {
        return label_match_pkg;
    }

    public void setLabelMatchPkg(int labelMatchPkg) {
        this.label_match_pkg = labelMatchPkg;
    }

    public int getSignMode() {
        return signMode;
    }

    public void setSignMode(int signMode) {
        this.signMode = signMode;
    }

    public String getKsUrl() {
        return ksUrl;
    }

    public void setKsUrl(String ksUrl) {
        this.ksUrl = ksUrl;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getQcodeInfo() {
        return qcodeInfo;
    }

    public void setQcodeInfo(String qcodeInfo) {
        this.qcodeInfo = qcodeInfo;
    }

    public static DoctorBean parseBean(JSONObject object) {
        String tokenId = object.optString("tokenId");
        String name = object.optString("name");
        String doctorCode = object.optString("doctorCode");
        String hxId = object.optString("hxUserId");
        String portrait = object.optString("portrait");
        String doctorType = object.optString("doctorType");
        String empId = object.optString("empId");
        String teamName = object.optString("instituteName");
        String orgId = object.optString("orgId");
        String codeInfo = object.optString("qcodeInfo");
        int doctorId = object.optInt("doctorId");
        int insId = object.optInt("insId");
        int instituteId = object.optInt("instituteId");
        int isEditArchives = object.optInt("isEditArchives");
        int isLoginRepeat = object.optInt("isLoginRepeat");
        int labelMatchPkg = object.optInt("labelMatchPkg");

        DoctorBean info = new DoctorBean();

        //获取配置信息
        JSONObject configMap = object.optJSONObject("configMap");
        if (configMap != null) {
            //公卫url地址 默认丹阳的公卫地址
            String ksUrl = configMap.optString("pub_health_url", KonsungConfig.DEFAULT_PUBLIC_HEALTH_URL);
            //是否强制建档  默认不强制
            String isArchives = configMap.optString("is_archive", "0");
            //单签 多签  默认单签
            String signMode = configMap.optString("is_sign_mode", "2");
            info.setKsUrl(ksUrl);
            info.setSignMode(Integer.valueOf(signMode));
            info.setIsArchives(Integer.valueOf(isArchives));
            compareMap(configMap);
        } else {
            info.setKsUrl(object.optString("ksUrl", KonsungConfig.DEFAULT_PUBLIC_HEALTH_URL));
            info.setIsArchives(object.optInt("isArchives", 0));
            info.setSignMode(object.optInt("signMode", 2));
            //设置默认的测量项配置
            SPUtils.put(MyApplication.getInstance(), AppConfig.MEASURE_CONFIG, DEFAULT_MEASURE_CONFIG);
        }
        info.setName(name);
        info.setDoctorCode(doctorCode);
        info.setPortrait(portrait);
        info.setDoctorType(doctorType);
        info.setTokenId(tokenId);
        info.setDoctorId(doctorId);
        info.setTeamName(teamName);
        info.setEmpId(empId);
        info.setHxId(hxId);
        info.setIsEditArchives(isEditArchives);
        info.setIsLoginRepeat(isLoginRepeat);
        info.setLabelMatchPkg(labelMatchPkg);
        info.setQcodeInfo(codeInfo);

        info.setInsId(insId);
        info.setOrgId(String.valueOf(instituteId));
        info.setInstituteId(orgId);

        return info;
    }

    //获取后台配置项
    private static void compareMap(JSONObject object) {
        //MyApplication.getInstance().getMeasureConfig().length()   --0312
        //DEFAULT_MEASURE_CONFIG.length()
        String[] data = new String[AppConfig.DEFAULT_MEASURE_CONFIG.length()];
        for (int i = 0; i < data.length; i++) {
            data[i] = "0";
        }
        data[MeasureConfig.NIBP] = object.optString("NIBP", "0");
        data[MeasureConfig.SPO2] = object.optString("SPO2", "0");
        data[MeasureConfig.PR] = object.optString("PR", "1");
        data[MeasureConfig.ECG] = object.optString("ECG", "0");
        data[MeasureConfig.TEMP] = object.optString("TEMP", "0");
        data[MeasureConfig.URINE] = object.optString("URINE", "0");
        data[MeasureConfig.GLU] = object.optString("GLU", "0");
        data[MeasureConfig.CHOL] = object.optString("CHOL", "0");
        data[MeasureConfig.UA] = object.optString("UA", "0");
        data[MeasureConfig.BLOOD] = object.optString("BLOOD", "0");
        data[MeasureConfig.HB] = object.optString("HB", "0");
        data[MeasureConfig.HEIGHT] = object.optString("HEIGHT", "0");
        data[MeasureConfig.WEIGHT] = object.optString("WEIGHT", "0");
        data[MeasureConfig.DS100A] = object.optString("DS100A", "0");
        data[MeasureConfig.CRP] = object.optString("CRP", "0");
        data[MeasureConfig.SAA] = object.optString("SAA", "0");
        data[MeasureConfig.PCT] = object.optString("PCT", "0");
        data[MeasureConfig.MYO] = object.optString("MYO", "0");
        data[MeasureConfig.GHB] = object.optString("GHB", "0");
        data[MeasureConfig.WBC] = object.optString("WBC", "0");
        data[MeasureConfig.HSCRP] = object.optString("HSCRP", "0");

        String dataConfig = "";
        for (int i = 0; i < data.length; i++) {
            dataConfig += data[i];
        }
        Logger.i("lsy", dataConfig);
        SPUtils.put(MyApplication.getInstance(), AppConfig.MEASURE_CONFIG, dataConfig);
    }
}
