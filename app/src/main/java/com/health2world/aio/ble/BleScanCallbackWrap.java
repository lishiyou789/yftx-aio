package com.health2world.aio.ble;

import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;

import java.util.List;

/**
 * Created by lishiyou on 2018/11/22 0022.
 */

public class BleScanCallbackWrap extends BleScanCallback {

    @Override
    public void onScanStarted(boolean b) {

    }

    @Override
    public void onScanning(BleDevice bleDevice) {

    }

    @Override
    public void onLeScan(BleDevice bleDevice) {
        super.onLeScan(bleDevice);
    }

    @Override
    public void onScanFinished(List<BleDevice> list) {

    }
}
