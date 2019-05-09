package com.health2world.aio.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.health2world.aio.MyApplication;
import com.health2world.aio.app.home.MyQRDialogActivity;
import com.health2world.aio.common.DataServer;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.printer.PrintUtils;
import com.health2world.aio.util.Logger;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aio.health2world.utils.DateUtil;
import aio.health2world.utils.ToastUtil;

import static android.content.Context.USB_SERVICE;

/**
 * Created by lishiyou on 2018/9/4 0004.
 */

public class Health2WorldReceiver extends BroadcastReceiver {

    public static final String ACTION_SHOW_QR_DIALOG = "com.health2world.aio.qr.dialog";
    public static final String ACTION_SCAN_DS100 = "scanDS100";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //二维码调用广播
        if (action.equals(ACTION_SHOW_QR_DIALOG)) {
            Intent intent1 = new Intent(context, MyQRDialogActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getInstance().startActivity(intent1);
        }

        //重新扫描连接大树血脂定时任务
        if (action.equals(ACTION_SCAN_DS100)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (BleManager.getInstance().isSupportBle()) {
                        if (!BleManager.getInstance().isBlueEnable()) {
                            BleManager.getInstance().enableBluetooth();
                        }
                    }
                    if (!BleManager.getInstance().isBlueEnable())
                        return;
                    if (AppConfig.isDebug)
                        Logger.d("zrl", "尝试扫描连接大树血脂：" + DateUtil.getCurrentTime(new Date()));

                    List<BleDevice> list = MyApplication.getInstance().getDeviceList();
                    BleDevice _bleDevice = null;
                    if (list.size() > 0)
                        for (BleDevice _bleDevice1 : list) {
                            if (_bleDevice1 != null
                                    && _bleDevice1.getName() != null
                                    && _bleDevice1.getName().startsWith(AppConfig.DS100A_BLUETOOTH))
                                _bleDevice = _bleDevice1;
                        }
                    if (_bleDevice != null)
                        MyApplication.getInstance().connectDevice(_bleDevice);
                    else
                        MyApplication.getInstance().scanDevice();
                }
            }).start();
        }

        //USB插入,若为热敏打印机，将此设备对象保存
        if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            Logger.d("zrl", "USB插入: VID: " + device.getVendorId());
            if (device.getVendorId() == AppConfig.USB_PRINT_VID) {
                PrintUtils.registerPrinter(device);
            }
        }

        //USB拔出,若为热敏打印机，将此设备对象销毁
        if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            Logger.d("zrl", "USB拔出: VID: " + device.getVendorId());
            if (device.getVendorId() == AppConfig.USB_PRINT_VID) {
                PrintUtils.unRegisterPrinter();
            }
        }
    }
}

