package com.konsung.listen;

import android.content.ComponentName;

/**
 * 监听app传入数据的方法接口
 * Created by --- on 2017/8/24 0024.
 */

public interface AppDeviceListen {

    void sendParaStatus(String name, String version);

    void sendWave(int param, byte[] bytes);

    void sendTrend(int param, int value);

    void sendConfig(int param, int value);

    void send12LeadDiaResult(byte[] bytes);

    void onServiceDisconnected(ComponentName name);

}
