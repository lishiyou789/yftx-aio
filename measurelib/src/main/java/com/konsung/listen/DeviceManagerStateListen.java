package com.konsung.listen;

import android.os.Bundle;

import com.konsung.bean.StateBean;

/**
 * @author Administrator
 * @date 2019/1/21/0021.
 */

public interface DeviceManagerStateListen {

    /**
     * 打印机心跳监听
     *
     * @param pBundle
     */
    void onPrinterHeartBeat(Bundle pBundle);

    /**
     * 打印机状态监听
     */
    void onPrinterState(StateBean bean);

    /**
     * 量点数据接收
     *
     * @param param
     * @param value
     */
    void onPointState(int param, int value);
}
