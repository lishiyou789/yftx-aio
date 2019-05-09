package com.health2world.aio.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lishiyou on 2017/12/25 0025.
 */
public class ServiceItem implements Serializable {
    public int itemId;
    public String itemName;
    public int feeType;
    public Double itemPrice;
    private int targetNum;
    private int signType;
    private String serviceItemDesc;
    private int executeNum;
    private int serviceType;//  服务类型：0:服务; 1:测量类
    private int signId;
    private String patientId;
    private String packageName;
    private int item_sort;
    @SerializedName("itemType")
    private String termUnit;

    private String serviceTermUnit;

    public String getServiceTermUnit() {
        return serviceTermUnit;
    }

    public void setServiceTermUnit(String serviceTermUnit) {
        this.serviceTermUnit = serviceTermUnit;
    }

    public String getTermUnit() {
        return termUnit;
    }

    public void setTermUnit(String termUnit) {
        this.termUnit = termUnit;
    }

    public int getItem_sort() {
        return item_sort;
    }

    public void setItem_sort(int item_sort) {
        this.item_sort = item_sort;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getSignId() {
        return signId;
    }

    public void setSignId(int signId) {
        this.signId = signId;
    }

    public ServiceItem() {
    }


    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getTargetNum() {
        return targetNum;
    }

    public void setTargetNum(int targetNum) {
        this.targetNum = targetNum;
    }

    public String getServiceItemDesc() {
        return serviceItemDesc;
    }

    public void setServiceItemDesc(String serviceItemDesc) {
        this.serviceItemDesc = serviceItemDesc;
    }

    public int getSignType() {
        return signType;
    }

    public void setSignType(int signType) {
        this.signType = signType;
    }

    public int getExecuteNum() {
        return executeNum;
    }

    public void setExecuteNum(int executeNum) {
        this.executeNum = executeNum;
    }
}
