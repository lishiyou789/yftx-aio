package com.health2world.aio.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 健康报告数据模型
 * Created by lishiyou on 2018/9/25 0025.
 */

public class HealthReport implements Serializable {


//   "dataId":24,
//           "patientId":1543,
//           "reportName":"2018-09-06门诊报告",
//           "doctorName":"肖医",
//           "spFeedback":"",
//           "createTime":1536223194000,
//           "medicalResult":"[{"name":"2018-09-05","type":1},{"measurementProject":6,"name":"血脂","value":"血脂","status":0,"type":0,"measureTime":"17:34","measureRecordVoList":[{"numericalValue":"12","name":"总胆固醇"},{"numericalValue":"12","name":"甘油三酯"},{"numericalValue":"12","name":"高密度脂蛋白"},{"numericalValue":"12","name":"低密度脂蛋白"},{"name":"葡萄糖"},{"name":"极低密度脂蛋白胆固醇"},{"name":"动脉硬化指数"},{"name":"冠心病危险指数"}],"statusList":[2,2,2,2,0,0,0,0],"measureDate":"2018-09-05"}]"

    private int dataId;
    private int patientId;
    private String reportName;
    private String doctorName;
    private String spFeedback;
    private String adviceDoctor;
    private long createTime;
    private String medicalResult;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getMedicalResult() {
        return medicalResult;
    }

    public void setMedicalResult(String medicalResult) {
        this.medicalResult = medicalResult;
    }

    public String getAdviceDoctor() {
        return adviceDoctor;
    }

    public void setAdviceDoctor(String adviceDoctor) {
        this.adviceDoctor = adviceDoctor;
    }

    public static HealthReport parseBean(JSONObject obj) {
        HealthReport report = new HealthReport();
        report.setDataId(obj.optInt("dataId"));
        report.setPatientId(obj.optInt("patientId"));
        report.setCreateTime(obj.optLong("createTime"));
        report.setDoctorName(obj.optString("doctorName"));
        report.setMedicalResult(obj.optString("medicalResult"));
        report.setSpFeedback(obj.optString("spFeedback"));
        report.setReportName(obj.optString("reportName"));
        report.setAdviceDoctor(obj.optString("adviceDoctor"));
        return report;
    }
}
