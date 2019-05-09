package com.health2world.aio.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


/**
 * 签约服务基本数据模型
 * Created by lishiyou on 2017/9/5 0005.
 */
public class SignService implements Serializable, Cloneable {
    private int serviceId = -1;
    @SerializedName(value = "pageName", alternate = "serviceName")
    private String pageName;//服务包名
    private double price = -1f;//总价格
    private double couponPrice;//现价
    private String serviceDesc;//服务内容
    private int tagId;
    private int serviceTerm;
    private int serviceStatus;
    private String serviceTermUnit;
    private String area;
    private String imgUrl;
    private String tagName;
    private String pkgType;
    @SerializedName("isPatientSign")
    private int limit = 0;//是否达到上限
    private boolean isSigned = false;//是否已经签约
    private String signDoctor;
    private boolean checked = false;
    private String endTime;
    private String delFlag;//是否过期 1：过期 0 ：未过期
    private int signId;
    private String shortName;
    private List<ServiceItem> serviceItems;

    @SerializedName("tagShortName")
    private String tagShortName;

    @Override
    public SignService clone() {
        SignService entity = null;
        try {
            entity = (SignService) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    public String getTagShortName() {
        return tagShortName;
    }

    public void setTagShortName(String tagShortName) {
        this.tagShortName = tagShortName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }

    public int getSignId() {
        return signId;
    }

    public void setSignId(int signId) {
        this.signId = signId;
    }

    public String getSignDoctor() {
        return signDoctor;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setSignDoctor(String signDoctor) {
        this.signDoctor = signDoctor;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getPkgType() {
        return pkgType;
    }

    public void setPkgType(String pkgType) {
        this.pkgType = pkgType;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return pageName;
    }

    public void setServiceName(String serviceName) {
        this.pageName = serviceName;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getServiceTerm() {
        return serviceTerm;
    }

    public void setServiceTerm(int serviceTerm) {
        this.serviceTerm = serviceTerm;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceTermUnit() {
        return serviceTermUnit;
    }

    public void setServiceTermUnit(String serviceTermUnit) {
        this.serviceTermUnit = serviceTermUnit;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<ServiceItem> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(List<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(double couponPrice) {
        this.couponPrice = couponPrice;
    }


}
