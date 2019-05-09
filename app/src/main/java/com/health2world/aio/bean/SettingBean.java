package com.health2world.aio.bean;

import java.io.Serializable;

/**
 * Created by lishiyou on 2018/7/19 0019.
 */

public class SettingBean implements Serializable {

    private int action = -1;

    private int resId;

    private String name;

    private String value;

    private boolean checked;

    public SettingBean() {
    }

    public SettingBean(int action, String name, int resId, String value) {
        this.action = action;
        this.name = name;
        this.resId = resId;
        this.value = value;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
