package com.health2world.aio.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.entity.SectionEntity;

public class HistoryData extends SectionEntity implements Serializable {

//    "checkKindName":"尿酸",
//            "checkKindCode":"9",
//            "checkDataOuts":[
//    {
//        "unit":"μmol/L",
//            "status":0,
//            "checkKindName":"尿酸",
//            "checkTypeName":"尿酸-男",
//            "checkTypeCode":"md_uric_acid_man",
//            "checkKindCode":"9",
//            "checkDate":"2019-04-17 09:04:00",
//            "value":"417",
//            "batchNumber":"567998913156481024",
//            "timestamp":"1555463094000"
//    }
//                ]

    private String checkKindName;
    private String checkKindCode;
    private long timestamp;
    private List<MedicalData> checkDataOuts;

    public HistoryData(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public HistoryData(Object o) {
        super(o);
    }

    public HistoryData() {
    }

    public static HistoryData parseBean(JSONObject object) {
        HistoryData data = new HistoryData();

        data.setCheckKindCode(object.optString("checkKindCode", ""));
        data.setCheckKindName(object.optString("checkKindName", ""));
        data.setTimestamp(object.optLong("checkDate"));

        JSONArray array = object.optJSONArray("checkDataOuts");
        List<MedicalData> list = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                MedicalData medical = MedicalData.parseBean(obj);
                list.add(medical);
            }
        }
        data.setCheckDataOuts(list);

        return data;
    }

    public String getCheckKindName() {
        return checkKindName;
    }

    public void setCheckKindName(String checkKindName) {
        this.checkKindName = checkKindName;
    }

    public String getCheckKindCode() {
        return checkKindCode;
    }

    public void setCheckKindCode(String checkKindCode) {
        this.checkKindCode = checkKindCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<MedicalData> getCheckDataOuts() {
        return checkDataOuts;
    }

    public void setCheckDataOuts(List<MedicalData> checkDataOuts) {
        this.checkDataOuts = checkDataOuts;
    }


}
