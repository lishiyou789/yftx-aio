package com.health2world.aio.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServiceLog implements Serializable {

//            "id" : 18331,
//            "serviceName" : "孕产妇服务包",
//            "serviceTime" : "2019-02-28 09:39:00",
//            "serviceItemName" : "进行血常规",
//            "serviceType" : "1",
//            "serviceDoctorName" : "谢大大",
//            "serviceResult" : "",
//            "checkBatchNumber" : "82637",
//            "checkDataRecordMap"[]

    private int id;
    private String serviceName;
    private String serviceTime;
    private String serviceItemName;
    private String serviceType;
    private String serviceDoctorName;
    private String serviceResult;
    private String checkBatchNumber;
    private List<MedicalData> checkDataRecordMap;

//      "unit" : "mmHg",
//              "status" : 0,
//              "checkKindName" : "血压",
//              "checkTypeName" : "舒张压 ",
//              "checkTypeCode" : "dbp",
//              "checkKindCode" : "0",
//              "checkDate" : "2019-02-28 09:39:00",
//              "value" : "60",
//              "batchNumber" : "82637"

    public static ServiceLog parseBean(JSONObject object) {
        ServiceLog log = new ServiceLog();

        log.setId(object.optInt("id", 0));
        log.setServiceName(object.optString("serviceName"));
        log.setServiceTime(object.optString("serviceTime"));
        log.setServiceItemName(object.optString("serviceItemName"));
        log.setServiceType(object.optString("serviceType"));
        log.setServiceDoctorName(object.optString("serviceDoctorName"));
        log.setServiceResult(object.optString("serviceResult"));
        log.setCheckBatchNumber(object.optString("checkBatchNumber"));

        JSONArray array = object.optJSONArray("checkDataRecordMap");

        List<MedicalData> list = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                MedicalData data = MedicalData.parseBean(obj);
                list.add(data);
            }
        }
        log.setCheckDataRecordMap(list);
        return log;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getServiceItemName() {
        return serviceItemName;
    }

    public void setServiceItemName(String serviceItemName) {
        this.serviceItemName = serviceItemName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceDoctorName() {
        return serviceDoctorName;
    }

    public void setServiceDoctorName(String serviceDoctorName) {
        this.serviceDoctorName = serviceDoctorName;
    }

    public String getServiceResult() {
        return serviceResult;
    }

    public void setServiceResult(String serviceResult) {
        this.serviceResult = serviceResult;
    }

    public String getCheckBatchNumber() {
        return checkBatchNumber;
    }

    public void setCheckBatchNumber(String checkBatchNumber) {
        this.checkBatchNumber = checkBatchNumber;
    }

    public List<MedicalData> getCheckDataRecordMap() {
        return checkDataRecordMap;
    }

    public void setCheckDataRecordMap(List<MedicalData> checkDataRecordMap) {
        this.checkDataRecordMap = checkDataRecordMap;
    }
}
