package com.health2world.aio.bean;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskInfo implements Serializable {
// "taskId" : "41129",
//         "taskType" : "0",
//         "taskStatus" : "1",
//         "checkBatchNumber" : "554632100075012096",
//         "patientId" : "896367",
//         "patientName" : "王一",
//         "labelArray" : [ ],
//         "isRegister" : "0",
//         "isBuy" : "0",
//         "checkDataOuts" : [ {
//        "unit" : "mmol/L",
//                "status" : 0,
//                "checkKindName" : "血脂",
//                "checkTypeName" : "总胆固醇",
//                "checkTypeCode" : "fat_flipidschol",
//                "checkKindCode" : "5",
//                "checkDate" : "2019-03-11 11:49:00",
//                "value" : "3.8",
//                "batchNumber" : "554632100075012096"
//    } ]
//

    private String taskId;
    private String taskType;
    private String taskStatus;
    private String checkBatchNumber;
    private String patientId;
    private String patientName;
    private String portrait;
    private String[] labelArray;
    private String isRegister;
    private String isBuy;
    private long taskTime;
    private List<MedicalData> checkDataOuts;

    public static TaskInfo parseBean(JSONObject object) {
        TaskInfo info = new TaskInfo();
        info.setTaskId(object.optString("taskId"));
        info.setTaskType(object.optString("taskType"));
        info.setTaskStatus(object.optString("taskStatus"));
        info.setCheckBatchNumber(object.optString("checkBatchNumber"));
        info.setPatientId(object.optString("patientId"));
        info.setPatientName(object.optString("patientName"));
        info.setIsRegister(object.optString("isRegister"));
        info.setIsBuy(object.optString("isBuy"));
        info.setPortrait(object.optString("portrait"));
        String taskTime = object.optString("taskTime");
        if (!TextUtils.isEmpty(taskTime))
            try {
                info.setTaskTime(Long.parseLong(taskTime));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        JSONArray array1 = object.optJSONArray("labelArray");
        if (array1 != null) {
            String[] labelArray = new String[array1.length()];
            for (int i = 0; i < array1.length(); i++) {
                labelArray[i] = array1.optString(i);
            }
            info.setLabelArray(labelArray);
        }
        JSONArray array = object.optJSONArray("checkDataOuts");
        List<MedicalData> list = new ArrayList<>();
        if (array != null)
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                MedicalData medicalData = MedicalData.parseBean(obj);
                list.add(medicalData);
            }
        info.setCheckDataOuts(list);
        return info;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCheckBatchNumber() {
        return checkBatchNumber;
    }

    public void setCheckBatchNumber(String checkBatchNumber) {
        this.checkBatchNumber = checkBatchNumber;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String[] getLabelArray() {
        return labelArray;
    }

    public void setLabelArray(String[] labelArray) {
        this.labelArray = labelArray;
    }

    public String getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(String isRegister) {
        this.isRegister = isRegister;
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public long getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(long taskTime) {
        this.taskTime = taskTime;
    }

    public List<MedicalData> getCheckDataOuts() {
        return checkDataOuts;
    }

    public void setCheckDataOuts(List<MedicalData> checkDataOuts) {
        this.checkDataOuts = checkDataOuts;
    }
}
