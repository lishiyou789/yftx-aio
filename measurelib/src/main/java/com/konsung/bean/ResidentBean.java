package com.konsung.bean;

import android.text.TextUtils;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 居民信息的类
 */
@DatabaseTable(tableName = "t_resident")
public class ResidentBean implements Serializable, Cloneable {
    @DatabaseField(dataType = DataType.STRING)
    private String patientId = "";//患者Id
    @DatabaseField(id = true, dataType = DataType.STRING)//身份证号码
    private String identityCard = "";
    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String name = ""; //姓名
    @DatabaseField(dataType = DataType.INTEGER)
    private int sexy = 2; //性别 2-未知 1-男 0-女
    @DatabaseField(dataType = DataType.INTEGER)
    private int age = 0;//年龄
    @DatabaseField(dataType = DataType.STRING)
    private String localAddress = "";//现住址
    @DatabaseField(dataType = DataType.STRING)
    private String address;//户籍地址
    @DatabaseField(dataType = DataType.STRING)
    private String birthday = "";//生日
    @DatabaseField(dataType = DataType.STRING)
    private String idCardPic = "";//加密图片
    @DatabaseField(dataType = DataType.STRING)
    private String openId = "";//患者微信Id
    @DatabaseField(dataType = DataType.STRING)
    private String telPhone = "";//患者电话
    @DatabaseField(dataType = DataType.STRING)
    private String height = "";//身高
    @DatabaseField(dataType = DataType.STRING)
    private String weight = "";//体重
    @DatabaseField(dataType = DataType.STRING)
    private String portrait = "";//患者头像（网络头像）
    @DatabaseField(dataType = DataType.INTEGER)
    private int sign;//患者签约信息（0否1是）
    @DatabaseField(dataType = DataType.INTEGER)
    private int doctor;//是否和此医生签约（0否1是）
    @DatabaseField(dataType = DataType.INTEGER)
    private int familyId;//家庭组id
    @DatabaseField(dataType = DataType.STRING)
    private String serviceIds;//签约服务项
    @DatabaseField(dataType = DataType.STRING)
    private String tagIds;//签约服务项
    @DatabaseField(dataType = DataType.STRING)
    private String labelNames;//标签简称
    @DatabaseField(dataType = DataType.STRING)
    private String relation;//关系
    @DatabaseField(dataType = DataType.STRING)
    private String allergy = "";//过敏史
    @DatabaseField(dataType = DataType.STRING)
    private String medicareCard;//社保卡号
    @DatabaseField(dataType = DataType.INTEGER)
    private int doctorId;//签约的医生Id
    @DatabaseField(dataType = DataType.STRING)
    private String memberName;//紧急联系人姓名
    @DatabaseField(dataType = DataType.STRING)
    private String telPhoneUrgent;//紧急联系人电话
    @DatabaseField(dataType = DataType.STRING)
    private String remark;//备注
    private String memberId;//成员Id
    private int register;//判断是否注册在居民端 1 注册 0未注册
    private String isBindingAccount = "0";//居民手机号是否绑定为账号  0 否 1 是
    private String residentCode;//居民码
    private String healthFileNo;
    private boolean check;

    public ResidentBean() {
    }

    @Override
    public ResidentBean clone() {
        ResidentBean entity = null;
        try {
            entity = (ResidentBean) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    public String getHealthFileNo() {
        return healthFileNo;
    }

    public void setHealthFileNo(String healthFileNo) {
        this.healthFileNo = healthFileNo;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public String getResidentCode() {
        return residentCode;
    }

    public void setResidentCode(String residentCode) {
        this.residentCode = residentCode;
    }

    public String getIsBindingAccount() {
        return isBindingAccount;
    }

    public void setIsBindingAccount(String isBindingAccount) {
        this.isBindingAccount = isBindingAccount;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getLabelNames() {
        return labelNames;
    }

    public void setLabelNames(String labelNames) {
        this.labelNames = labelNames;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSexy() {
        return sexy;
    }

    public void setSexy(int sexy) {
        this.sexy = sexy;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdCardPic() {
        return idCardPic;
    }

    public void setIdCardPic(String idCardPic) {
        this.idCardPic = idCardPic;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getTelPhoneUrgent() {
        return telPhoneUrgent;
    }

    public void setTelPhoneUrgent(String telPhoneUrgent) {
        this.telPhoneUrgent = telPhoneUrgent;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public int getDoctor() {
        return doctor;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    public String getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(String serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
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

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String pRemark) {
        remark = pRemark;
    }

    public String getMedicareCard() {
        return medicareCard;
    }

    public void setMedicareCard(String medicareCard) {
        this.medicareCard = medicareCard;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public static ResidentBean parse(JSONObject obj) {
        int patientId = obj.optInt("patientId", -1);
        if (patientId == -1) {
            patientId = obj.optInt("id");
        }
        String code = obj.optString("code");
        String name = obj.optString("name");
        String identityCard = obj.optString("identityCard");
        int sexy = obj.optInt("sexy");
        int age = obj.optInt("age", -1);
        String telPhone = obj.optString("telphone");
        String telPhoneUrgent = obj.optString("telphoneUrgent");
        String portrait = obj.optString("portrait");
        int doctorId = obj.optInt("doctorId");
        int hasSign = obj.optInt("hasSign");
        int register = obj.optInt("isRegist", -1);
        int isDoctor = obj.optInt("isDoctor");
        int familyId = obj.optInt("familyId");
        String allergy = obj.optString("hisDrugAllergy");
        String localAddress = obj.optString("localAddr");
        String address = obj.optString("address");
        String urgentName = obj.optString("urgentName");
        String serviceIds = obj.optString("serviceIds");
        String tagIds = obj.optString("tagIds");
        String birthday = obj.optString("birthday");
        String medicareCard = obj.optString("medicareCard");
        String relation = obj.optString("familyRelation");
        String remark = obj.optString("remark");
        String labelNames = obj.optString("labelNames", "");
        JSONArray array = obj.optJSONArray("labelArray");
        ResidentBean resident = new ResidentBean();
        resident.setPatientId(patientId + "");
        resident.setDoctorId(doctorId);
        resident.setName(name);
        resident.setAge(age);
        resident.setSexy(sexy);
        resident.setSign(hasSign);
        resident.setBirthday(birthday);
        resident.setMedicareCard(medicareCard);
        resident.setRelation(relation);
        resident.setFamilyId(familyId);
        resident.setRemark(remark);
        //避免刷身份证的时候服务器的数据覆盖了身份证上的号码
        if (!TextUtils.isEmpty(identityCard)) {
            resident.setIdentityCard(identityCard);
        }
        String label = "";
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    label += array.getString(i) + ",";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!TextUtils.isEmpty(label)) {
            resident.setLabelNames(label);
        } else {
            resident.setLabelNames(labelNames);
        }
        resident.setTelPhone(telPhone);
        resident.setPortrait(portrait);
        resident.setAllergy(allergy);
        resident.setTelPhoneUrgent(telPhoneUrgent);
        resident.setLocalAddress(localAddress);
        resident.setAddress(address);
        resident.setMemberName(urgentName);
        resident.setTagIds(tagIds);
        resident.setServiceIds(serviceIds);
        resident.setResidentCode(code);
        resident.setDoctor(isDoctor);
        resident.setRegister(register);
        resident.setSign(hasSign);
        return resident;
    }
}
