package com.konsung.bean;

/**
 * Created by YYX on 2017/9/6 0006.
 * 设备的状态bean
 */

public class StateBean {
    //心电
    private boolean ll = false;
    private boolean la = false;
    private boolean ra = false;
    private boolean v1 = false;
    private boolean v2 = false;
    private boolean v3 = false;
    private boolean v4 = false;
    private boolean v5 = false;
    private boolean v6 = false;
    //血氧探头
    private boolean probe = false;
    //血氧手指是否插入
    private boolean finger = false;
//    //胎心1
//    private boolean fetalHeart1 = false;
//    //胎心2
//    private boolean fetalHeart2 = false;
//    //宫缩
//    private boolean uterine = false;

    //打印设备状态  0:连接成功  1：USB被拔出  2：USB Interface不存在（提示重新拔插）
    private int printDeviceState = -1;
    //打印设备连接方式   0：USB连接  1：串口连接  2：WIfi连接  3：蓝牙连接
    private int printConnState = -1;
    //打印结果   0：打印成功  1：文件不存在  2：不支持该命令  3：文件路径过长  255：未知错误
    private int printResult = -1;

    public int getPrintDeviceState() {
        return printDeviceState;
    }

    public void setPrintDeviceState(int pPrintDeviceState) {
        printDeviceState = pPrintDeviceState;
    }

    public int getPrintConnState() {
        return printConnState;
    }

    public void setPrintConnState(int pPrintConnState) {
        printConnState = pPrintConnState;
    }

    public int getPrintResult() {
        return printResult;
    }

    public void setPrintResult(int pPrintResult) {
        printResult = pPrintResult;
    }

    /**
     * 获取ll的值
     *
     * @return ll ll值
     */
    public boolean getLl() {
        return ll;
    }

    /**
     * 设置ll的值
     *
     * @param ll ll值
     */
    public void setLl(boolean ll) {
        this.ll = ll;
    }

    /**
     * 获取la的值
     *
     * @return la la值
     */
    public boolean getLa() {
        return la;
    }

    /**
     * 设置la的值
     *
     * @param la la值
     */
    public void setLa(boolean la) {
        this.la = la;
    }

    /**
     * 获取ra的值
     *
     * @return ra ra值
     */
    public boolean getRa() {
        return ra;
    }

    /**
     * 设置ra的值
     *
     * @param ra ra值
     */
    public void setRa(boolean ra) {
        this.ra = ra;
    }

    /**
     * 获取v1的值
     *
     * @return v1 v1值
     */
    public boolean getV1() {
        return v1;
    }

    /**
     * 设置v1的值
     *
     * @param v1 v1值
     */
    public void setV1(boolean v1) {
        this.v1 = v1;
    }

    /**
     * 获取v2的值
     *
     * @return v2 v2值
     */
    public boolean getV2() {
        return v2;
    }

    /**
     * 设置v2的值
     *
     * @param v2 v2值
     */
    public void setV2(boolean v2) {
        this.v2 = v2;
    }

    /**
     * 获取v3的值
     *
     * @return v3 v3值
     */
    public boolean getV3() {
        return v3;
    }

    /**
     * 设置v3的值
     *
     * @param v3 v3值
     */
    public void setV3(boolean v3) {
        this.v3 = v3;
    }

    /**
     * 获取v4的值
     *
     * @return v4 v4值
     */
    public boolean getV4() {
        return v4;
    }

    /**
     * 设置v4的值
     *
     * @param v4 v4值
     */
    public void setV4(boolean v4) {
        this.v4 = v4;
    }

    /**
     * 获取v5的值
     *
     * @return v5 v5值
     */
    public boolean getV5() {
        return v5;
    }

    /**
     * 设置v5的值
     *
     * @param v5 v5值
     */
    public void setV5(boolean v5) {
        this.v5 = v5;
    }

    /**
     * 获取v6的值
     *
     * @return v6 v6值
     */
    public boolean getV6() {
        return v6;
    }

    /**
     * 设置v6的值
     *
     * @param v6 v6值
     */
    public void setV6(boolean v6) {
        this.v6 = v6;
    }

    /**
     * 获取probe的值
     *
     * @return probe probe值
     */
    public boolean getProbe() {
        return probe;
    }

    /**
     * 设置probe的值
     *
     * @param probe probe值
     */
    public void setProbe(boolean probe) {
        this.probe = probe;
    }

    /**
     * 获取finger的值
     *
     * @return finger finger值
     */
    public boolean getFinger() {
        return finger;
    }

    /**
     * 设置finger的值
     *
     * @param finger finger值
     */
    public void setFinger(boolean finger) {
        this.finger = finger;
    }

    /**
     * 获取fetalHeart的值
     *
     * @return fetalHeart1 fetalHeart值
     */
//    public boolean getFetalHeart1() {
//        return fetalHeart1;
//    }

    /**
     * 设置fetalHeart的值
     *
     * @param fetalHeart1 fetalHeart值
     */
//    public void setFetalHeart1(boolean fetalHeart1) {
//        this.fetalHeart1 = fetalHeart1;
//    }

    /**
     * 获取fetalHeart的值
     *
     * @return fetalHeart fetalHeart值
     */
//    public boolean getFetalHeart2() {
//        return fetalHeart2;
//    }

    /**
     * 设置fetalHeart的值
     *
     * @param fetalHeart fetalHeart值
     */
//    public void setFetalHeart2(boolean fetalHeart) {
//        this.fetalHeart2 = fetalHeart;
//    }

    /**
     * 获取uterine的值
     *
     * @return uterine uterine值
     */
//    public boolean getUterine() {
//        return uterine;
//    }

    /**
     * 设置uterine的值
     */
//    public void setUterine(boolean uterine) {
//        this.uterine = uterine;
//    }
    @Override
    public String toString() {
        return "StateBean{" +
                "ll=" + ll +
                ", la=" + la +
                ", ra=" + ra +
                ", v1=" + v1 +
                ", v2=" + v2 +
                ", v3=" + v3 +
                ", v4=" + v4 +
                ", v5=" + v5 +
                ", v6=" + v6 +
                ", probe=" + probe +
                ", finger=" + finger +
                '}';
    }

}
