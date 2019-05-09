package com.health2world.aio.bean;

import com.konsung.bean.MeasureBean;
import com.konsung.listen.Measure;

import java.io.Serializable;

/**
 * 综合医疗页面测量模型数据
 * Created by lishiyou on 2017/7/24 0024.
 */

public class MeasureItem implements Serializable {
    private Measure type;//测量类型
    private MeasureState state = MeasureState.MEASURE_READY;//是否检测（状态） 0 已测量 1 未测量 2 测量中
    private boolean measured;//是否已经测量
    private MeasureBean measureBean;
    private String extraValue;
    private boolean isConnect = false;

    public MeasureItem(Measure type, MeasureState state) {
        this.type = type;
        this.state = state;
    }

    public MeasureItem(Measure type, String extraValue) {
        this.type = type;
        this.extraValue = extraValue;
    }

    public MeasureItem(Measure type, MeasureState state, String extraValue) {
        this.type = type;
        this.state = state;
        this.extraValue = extraValue;
    }

    public MeasureItem(Measure type, boolean measured) {
        this.type = type;
        this.measured = measured;
    }

    public MeasureItem(Measure type, MeasureState state, boolean isConnect) {
        this.type = type;
        this.state = state;
        this.isConnect = isConnect;
    }

    public Measure getType() {
        return type;
    }

    public void setType(Measure type) {
        this.type = type;
    }

    public MeasureState getState() {
        return state;
    }

    public void setState(MeasureState state) {
        this.state = state;
    }

    public boolean isMeasured() {
        return measured;
    }

    public void setMeasured(boolean measured) {
        this.measured = measured;
    }

    public MeasureBean getMeasureBean() {
        return measureBean;
    }

    public void setMeasureBean(MeasureBean measureBean) {
        this.measureBean = measureBean;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }
}
