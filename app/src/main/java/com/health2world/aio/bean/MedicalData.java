package com.health2world.aio.bean;

import android.text.TextUtils;
import android.widget.TextView;

import com.health2world.aio.util.Logger;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 测量数据基本模型
 * Created by lishiyou on 2019/2/21 0021.
 */

public class MedicalData implements Serializable {

//       "unit" : "次/分钟",
//               "checkKindName" : "血氧",
//               "checkTypeName" : "脉率",
//               "checkTypeCode" : "oxygen_pr",
//               "checkKindCode" : "2",
//               "checkDate" : "2019-02-28 11:09:00",
//               "value" : "61"

    private String checkKindCode;
    private String checkKindName;

    private String checkTypeName;
    private String checkTypeCode;

    private String value;
    private String unit;
    private String checkDate;
    private String batchNumber;
    private String status;
    private long timestamp;


    public MedicalData() {
    }

    public MedicalData(String checkKindCode, String checkTypeCode, String value) {
        this.checkKindCode = checkKindCode;
        this.checkTypeCode = checkTypeCode;
        this.value = value;
    }

    public String getCheckKindCode() {
        return checkKindCode;
    }

    public void setCheckKindCode(String checkKindCode) {
        this.checkKindCode = checkKindCode;
    }

    public String getCheckTypeCode() {
        return checkTypeCode;
    }

    public void setCheckTypeCode(String checkTypeCode) {
        this.checkTypeCode = checkTypeCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCheckKindName() {
        return checkKindName;
    }

    public void setCheckKindName(String checkKindName) {
        this.checkKindName = checkKindName;
    }

    public String getCheckTypeName() {
        return checkTypeName;
    }

    public void setCheckTypeName(String checkTypeName) {
        this.checkTypeName = checkTypeName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static MedicalData parseBean(JSONObject object) {
        MedicalData data = new MedicalData();
        data.setCheckDate(object.optString("checkDate"));
        data.setCheckKindCode(object.optString("checkKindCode"));
        data.setCheckKindName(object.optString("checkKindName"));
        data.setCheckTypeCode(object.optString("checkTypeCode"));
        data.setCheckTypeName(object.optString("checkTypeName"));
        data.setValue(object.optString("value"));
        data.setUnit(object.optString("unit"));
        data.setStatus(String.valueOf(object.optInt("status", 0)));
        data.setBatchNumber(object.optString("batchNumber"));
        String timestamp = object.optString("timestamp");
        if (!TextUtils.isEmpty(timestamp))
            try {
                long time = Long.parseLong(timestamp);
                data.setTimestamp(time * 1000);
            } catch (NumberFormatException e) {
                Logger.e("lsy", e.getMessage() == null ? "NumberFormatException" : e.getMessage());
            }
        return data;
    }
}
