package com.health2world.aio.printer;

import com.konsung.listen.Measure;

import java.io.Serializable;

/**
 * Created by lishiyou on 2019/1/11 0011.
 */

public class PrinterData implements Serializable {

    //测量大类
    private Measure param;
    //测量项
    private String itemName;
    //结果
    private String result;
    //上下箭头
    private String arrow;
    //参考范围
    private String range;
    //单位
    private String unit;

    public PrinterData() {
    }

    public PrinterData(Measure param, String itemName, String result, String arrow, String range, String unit) {
        this.param = param;
        this.itemName = itemName;
        this.result = result;
        this.arrow = arrow;
        this.range = range;
        this.unit = unit;
    }

    public Measure getParam() {
        return param;
    }

    public void setParam(Measure param) {
        this.param = param;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getArrow() {
        return arrow;
    }

    public void setArrow(String pArrow) {
        arrow = pArrow;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
