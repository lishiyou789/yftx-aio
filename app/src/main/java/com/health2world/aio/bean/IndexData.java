package com.health2world.aio.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 指标数据
 * Created by lishiyou on 2019/1/16 0016.
 */

public class IndexData implements Serializable {

    private String itemName;
    private String data;
    private String quotaJudge;
    private String normalData;
    private String clinicalTob;
    private String clinicalToc;
    private String itemMean;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getQuotaJudge() {
        return quotaJudge;
    }

    public void setQuotaJudge(String quotaJudge) {
        this.quotaJudge = quotaJudge;
    }

    public String getNormalData() {
        return normalData;
    }

    public void setNormalData(String normalData) {
        this.normalData = normalData;
    }

    public String getClinicalTob() {
        return clinicalTob;
    }

    public void setClinicalTob(String clinicalTob) {
        this.clinicalTob = clinicalTob;
    }

    public String getClinicalToc() {
        return clinicalToc;
    }

    public void setClinicalToc(String clinicalToc) {
        this.clinicalToc = clinicalToc;
    }

    public String getItemMean() {
        return itemMean;
    }

    public void setItemMean(String itemMean) {
        this.itemMean = itemMean;
    }

    public static IndexData parseData(JSONObject object) {
        IndexData data = new IndexData();

        data.setItemName(object.optString("itemName", ""));
        data.setData(object.optString("data", ""));
        data.setQuotaJudge(object.optString("quotaJudge", ""));
        data.setNormalData(object.optString("normalData", ""));
        data.setClinicalTob(object.optString("clinicalTob", ""));
        data.setClinicalToc(object.optString("clinicalToc", ""));
        data.setItemMean(object.optString("itemMean", ""));

        return data;
    }
}
