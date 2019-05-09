package com.konsung.bean;

/**
 * 对健康测量界面的bean
 */

public class AppBean {
    private String name; //测量名称
    private boolean isClick; //是否点击
    private int picture; //未选中图标
    private int pictureSel; //选中图标

    /**
     * 获取是否点击的方法
     *
     * @return 点击状态
     */
    public boolean isClick() {
        return isClick;
    }

    /**
     * 设置点击的状态
     *
     * @param click 点击状态
     */
    public void setClick(boolean click) {
        isClick = click;
    }

    /**
     * 获取名称的方法
     *
     * @return 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称的方法
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取图片地址的方法
     *
     * @return 图片地址
     */
    public int getPicture() {
        return picture;
    }

    /**
     * 设置图片地址的方法
     *
     * @param picture 图片地址
     */
    public void setPicture(int picture) {
        this.picture = picture;
    }

    /**
     * 获取选中图标的方法
     *
     * @return 选中图标
     */
    public int getPictureSel() {
        return pictureSel;
    }

    /**
     * 设置选中图标的方法
     *
     * @param pictureSel 选中图标
     */
    public void setPictureSel(int pictureSel) {
        this.pictureSel = pictureSel;
    }
}
