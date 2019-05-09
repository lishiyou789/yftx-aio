package com.health2world.aio.common;

import com.konsung.bean.MeasureBean;
import com.konsung.listen.Measure;

/**
 * Created by lishiyou on 2017/8/10 0010.
 */

public class MeasureEvent {

    private Measure measure;

    private MeasureBean measureBean;

    private boolean success;


    public MeasureEvent(Measure measure, MeasureBean measureBean, boolean success) {
        this.measure = measure;
        this.measureBean = measureBean;
        this.success = success;
    }

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    public MeasureBean getMeasureBean() {
        return measureBean;
    }

    public void setMeasureBean(MeasureBean measureBean) {
        this.measureBean = measureBean;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
