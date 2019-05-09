package com.health2world.aio.ble;

import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.exception.BleException;
import com.health2world.aio.util.Logger;

/**
 * Created by lishiyou on 2018/11/22 0022.
 */

public class BleNotifyCallbackWrap extends BleNotifyCallback {

    @Override
    public void onNotifySuccess() {
        Logger.i("lsy", "notify success");
    }

    @Override
    public void onNotifyFailure(BleException e) {
        Logger.i("lsy", "notify exception");
    }

    @Override
    public void onCharacteristicChanged(byte[] bytes) {

    }
}
