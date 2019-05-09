package com.health2world.aio.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务详情数据模型
 */
public class TaskDetail implements Serializable {
//   "data" : {
//        "taskId" : "41142",
//                "patientId" : "895860",
//                "patientName" : "谢大傻",
//                "taskName" : "2019-04-16健康报告",
//                "illnessContent" : "头疼头晕",
//                "adviceDoctor":"",
//                "checkDataOuts" : [ {
//            "unit" : "mmHg",
//                    "status" : 2,
//                    "checkKindName" : "血压",
//                    "checkTypeName" : "收缩压",
//                    "checkTypeCode" : "sbp",
//                    "checkKindCode" : "0",
//                    "checkDate" : "2019-04-16 13:32:00",
//                    "value" : "91",
//                    "batchNumber" : "567703859930071040"
//        }, {
//            "unit" : "mmHg",
//                    "status" : 0,
//                    "checkKindName" : "血压",
//                    "checkTypeName" : "舒张压 ",
//                    "checkTypeCode" : "dbp",
//                    "checkKindCode" : "0",
//                    "checkDate" : "2019-04-16 13:32:00",
//                    "value" : "61",
//                    "batchNumber" : "567703859930071040"
//        } ]
//    }

    private String taskId;
    private String patientId;
    private String patientName;
    private String taskName;
    private String illnessContent;
    private String adviceDoctor;
    private List<HistoryData> records;

    public static TaskDetail pareBean(JSONObject object) {
        TaskDetail detail = new TaskDetail();
        detail.setTaskId(object.optString("taskId"));
        detail.setPatientId(object.optString("patientId"));
        detail.setPatientName(object.optString("patientName"));
        detail.setTaskName(object.optString("taskName"));
        detail.setIllnessContent(object.optString("illnessContent"));
        detail.setAdviceDoctor(object.optString("adviceDoctor"));

        JSONArray array = object.optJSONArray("records");
        List<HistoryData> list = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                HistoryData history = HistoryData.parseBean(obj);
                list.add(history);
            }
        }
        detail.setRecords(list);
        return detail;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getIllnessContent() {
        return illnessContent;
    }

    public void setIllnessContent(String illnessContent) {
        this.illnessContent = illnessContent;
    }

    public String getAdviceDoctor() {
        return adviceDoctor;
    }

    public void setAdviceDoctor(String adviceDoctor) {
        this.adviceDoctor = adviceDoctor;
    }

    public List<HistoryData> getRecords() {
        return records;
    }

    public void setRecords(List<HistoryData> records) {
        this.records = records;
    }

    //    public List<MedicalData> getCheckDataOuts() {
//        return checkDataOuts;
//    }
//
//    public void setCheckDataOuts(List<MedicalData> checkDataOuts) {
//        this.checkDataOuts = checkDataOuts;
//    }
}
