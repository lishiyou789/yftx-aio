package com.health2world.aio.printer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Environment;

import com.caysn.printerlibs.printerlibs_caysnlabel.printerlibs_caysnlabel;
import com.caysn.printerlibs.printerlibs_caysnpos.printerlibs_caysnpos;
import com.health2world.aio.MyApplication;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.config.KonsungConfig;
import com.health2world.aio.service.MeasureService;
import com.health2world.aio.util.ActivityUtil;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;
import com.konsung.net.EchoDeviceManagerServerEncoder;
import com.sun.jna.Pointer;

import java.util.HashMap;
import java.util.Map;

import aio.health2world.utils.ToastUtil;

import static android.content.Context.USB_SERVICE;
import static com.health2world.aio.config.AppConfig.ACTION_USB_PERMISSION;
import static com.health2world.aio.config.AppConfig.USB_PRINT_VID;

/**
 * @author Administrator
 * @date 2019/4/19/0019.
 */
public class PrintUtils {

    private UsbManager mUsbManager;
    private Context mContext;
    private static ResidentBean resident;
    private static MeasureBean dataBean;
    //这个回调是 插入热敏打印机时申请权限回调时用的
    private static printCallback mCallback;
    //是否是手动开始的热敏打印（可能是插入自动连接时）
    private static boolean isHandlePrint;

    public interface printCallback {
        void printStatus(boolean isSuccess);

        void isPrinterConnected(boolean isConnected);
    }

    public void setPrintUtils(Context pContext, printCallback pCallback) {
        mUsbManager = (UsbManager) pContext.getSystemService(USB_SERVICE);
        mContext = pContext;
        mCallback = pCallback;
    }

    public void printByBenTu() {
        if (!ActivityUtil.checkPackInfo(KonsungConfig.DEVICE_MANAGER_PACKAGE)) {
            ToastUtil.showShort("请先安装打印驱动");
            return;
        }
        if (!MeasureService.isDmHeartBeat) {
            ToastUtil.showShort("打印驱动未启动，请稍后尝试");
            return;
        }
        //value:  1:A5  0:A4
        EchoDeviceManagerServerEncoder.setPrintConfig(0x04, 1);
        //value:  1:横向 0:纵向
        EchoDeviceManagerServerEncoder.setPrintConfig(0x06, 1);
        //路径
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bmp/print.bmp";
        EchoDeviceManagerServerEncoder.setPrintData(path);
        ToastUtil.showLong("正在打印，请稍后");
        System.gc();
    }

    public void printByRM(ResidentBean resident, MeasureBean dataBean) {
        PrintUtils.resident = resident;
        PrintUtils.dataBean = dataBean;
        UsbDevice usbDevice = getPrinterDevice();
        if (usbDevice == null) {
            ToastUtil.showShort("请连接打印机设备");
            if (mCallback != null)
                mCallback.printStatus(false);
        } else if (MyApplication.getInstance().getPrinter() == Pointer.NULL) {
            //有连接打印机却没有保存
            registerPrinter(usbDevice);
        }
        if (mUsbManager == null) {
            mUsbManager = (UsbManager) MyApplication.getInstance().getSystemService(USB_SERVICE);
        }
        //先确保有保存打印机
        if (MyApplication.getInstance().getPrinter() != Pointer.NULL)
            if (mUsbManager.hasPermission(usbDevice)) {
                boolean isPrinted =
                        PrinterRM.printerMeasureData(MyApplication.getInstance().getPrinter(), resident, dataBean);
                if (mCallback != null)
                    mCallback.printStatus(isPrinted);
            } else {
                //没有权限，有保存打印机
                //获取USB权限
                PendingIntent mPermissionIntent = PendingIntent
                        .getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_UPDATE_CURRENT);
                //经过申请权限成功后直接跳转广播接收方法
                isHandlePrint = true;
                mUsbManager.requestPermission(usbDevice, mPermissionIntent);
            }
    }


    //注册保存热敏打印机
    public static void registerPrinter(UsbDevice pUsbDevice) {
        UsbManager mUsbManager = (UsbManager) MyApplication.getInstance().getSystemService(USB_SERVICE);
        if (mUsbManager == null)
            return;
        UsbReceiver mUsbReceiver = new UsbReceiver();
        IntentFilter mFilter = new IntentFilter(ACTION_USB_PERMISSION);
        MyApplication.getInstance().registerReceiver(mUsbReceiver, mFilter);

        //一开始没有权限，去申请权限后在接收权限的地方保存打印机
        if (!mUsbManager.hasPermission(pUsbDevice)) {
            //热敏打印机连接时就应该申请权限，但是会在权限申请后回调到PrintUtils
            //将是否为手动点击连接热敏设为否，防止插入自动打印
            isHandlePrint = false;
            PendingIntent mPermissionIntent =
                    PendingIntent.getBroadcast(MyApplication.getInstance(), 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_UPDATE_CURRENT);
            mUsbManager.requestPermission(pUsbDevice, mPermissionIntent);
        } else {
            //一开始就有权限的情况下，直接保存此打印机
            Pointer _pointer = printerlibs_caysnpos.INSTANCE.
                    CaysnPos_OpenUsbVidPid(USB_PRINT_VID, (short) pUsbDevice.getProductId());
            MyApplication.getInstance().setPrinter(_pointer);
        }
        if (mCallback != null)
            mCallback.isPrinterConnected(true);
    }

    //断开usb设备(热敏)端口
    public static void unRegisterPrinter() {
        if (MyApplication.getInstance().getPrinter() != Pointer.NULL) {
            printerlibs_caysnlabel.INSTANCE.CaysnLabel_Close(MyApplication.getInstance().getPrinter());
            MyApplication.getInstance().setPrinter(Pointer.NULL);
            if (mCallback != null)
                mCallback.isPrinterConnected(false);
        }
    }

    //寻找热敏打印机
    public UsbDevice getPrinterDevice() {
        //如果打印机一直处于连接的状态 重启一体机则找到UsbDevice 并赋值
        UsbDevice _usbDevice = null;
        UsbManager mUsbManager = (UsbManager) MyApplication.getInstance().getSystemService(USB_SERVICE);
        HashMap<String, UsbDevice> _map = null;
        //添加判空
        if (mUsbManager != null) {
            _map = mUsbManager.getDeviceList();
        }
        if (_map != null && !_map.isEmpty()) {
            for (Map.Entry<String, UsbDevice> _entry : _map.entrySet()) {
                if ((short) _entry.getValue().getVendorId() == AppConfig.USB_PRINT_VID) {
                    _usbDevice = _entry.getValue();
                    break;
                }
            }
        }
        return _usbDevice;
    }

    //申请USB权限结果广播结果回调
    static class UsbReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_USB_PERMISSION.equals(intent.getAction())) {
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    //申请权限后返回的device保存为局部对象，这个对象和全局对象的区别只在于这个有权限
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null && device.getVendorId() == AppConfig.USB_PRINT_VID) {
                        //获取打印机对象，保存在全局
                        if (MyApplication.getInstance().getPrinter() == Pointer.NULL) {
                            //申请到了权限后再保存此打印机
//                            ToastUtil.showLong("正在连接 若无响应请重新拔插打印机");
                            Pointer _pointer = printerlibs_caysnpos.INSTANCE.
                                    CaysnPos_OpenUsbVidPid(USB_PRINT_VID, (short) device.getProductId());
                            MyApplication.getInstance().setPrinter(_pointer);
//                            ToastUtil.showShort("热敏打印机已连接");
                        }
                        //获取到权限后，继续进行打印
                        if (isHandlePrint &&
                                PrinterRM.printerMeasureData(MyApplication.getInstance().getPrinter(), resident, dataBean)) {
                            if (mCallback != null)
                                mCallback.printStatus(true);
                        }
                    }
                } else {
                    ToastUtil.showShort("打印机无权限，请同意访问请求");
                    if (mCallback != null)
                        mCallback.printStatus(false);
                }
            }
            isHandlePrint = false;
        }
    }
}
