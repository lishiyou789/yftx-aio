package com.health2world.aio.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicalReport implements Serializable {

//"patientId" : 895860,
//        "reportName" : "2019-02-28门诊报告",
//        "doctorName" : "谢大大",
//        "spFeedback" : "",
//        "createTime" : "2019-02-28 09:39:00",
//        "adviceDoctor" : "Th",
//        "batchNumber" : "82637"


    private String patientId;
    private String reportName;
    private String doctorName;
    private String spFeedback;
    private String createTime;
    private String adviceDoctor;
    private String batchNumber;
    private List<MedicalData> checkDataOuts;

    public static MedicalReport parseBean(JSONObject object) {
        MedicalReport report = new MedicalReport();
        report.setPatientId(object.optString("patientId"));
        report.setReportName(object.optString("reportName"));
        report.setDoctorName(object.optString("doctorName"));
        report.setSpFeedback(object.optString("spFeedback"));
        report.setCreateTime(object.optString("createTime"));
        report.setAdviceDoctor(object.optString("adviceDoctor"));
        report.setBatchNumber(object.optString("batchNumber"));

        JSONArray array = object.optJSONArray("checkDataOuts");
        List<MedicalData> list = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                MedicalData data = MedicalData.parseBean(obj);
                list.add(data);
            }
        }
        report.setCheckDataOuts(list);
        return report;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpFeedback() {
        return spFeedback;
    }

    public void setSpFeedback(String spFeedback) {
        this.spFeedback = spFeedback;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAdviceDoctor() {
        return adviceDoctor;
    }

    public void setAdviceDoctor(String adviceDoctor) {
        this.adviceDoctor = adviceDoctor;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public List<MedicalData> getCheckDataOuts() {
        return checkDataOuts;
    }

    public void setCheckDataOuts(List<MedicalData> checkDataOuts) {
        this.checkDataOuts = checkDataOuts;
    }
}
