//package com.health2world.aio.receiver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbManager;
//import com.caysn.printerlibs.printerlibs_caysnpos.printerlibs_caysnpos;
//import com.health2world.aio.MyApplication;
//import com.health2world.aio.config.AppConfig;
//import com.health2world.aio.printer.PrintUtils;
//import com.health2world.aio.util.Logger;
//import com.sun.jna.Pointer;
//import static com.health2world.aio.config.AppConfig.ACTION_USB_PERMISSION;
//import static com.health2world.aio.config.AppConfig.USB_PRINT_VID;
//
///**
// * @author Administrator
// * @date 2018/12/27/0027.
// */
//
//public class UsbReceiver extends BroadcastReceiver {
//
//    UsbManager um;
//
//    public UsbReceiver(UsbManager pUsbManager) {
//        um = pUsbManager;
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (ACTION_USB_PERMISSION.equals(intent.getAction())) {
//            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                //申请权限后返回的device保存为局部对象，这个对象和全局对象的区别只在于这个有权限
//                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                if (device != null && device.getVendorId() == AppConfig.USB_PRINT_VID) {
//                    if (um.hasPermission(device))
//                        um.openDevice(device);
//                    //获取打印机对象，保存在全局
//                    if (MyApplication.getInstance().getPrinter() == Pointer.NULL) {
//                        Logger.i("zrl", "热敏打印机接入，正在打开端口");
//                        Pointer _pointer= printerlibs_caysnpos.INSTANCE.
//                                CaysnPos_OpenUsbVidPid(USB_PRINT_VID, (short) device.getProductId());
//                        MyApplication.getInstance().setPrinter(_pointer);
//                    }
//                }
//            } else {
//                Logger.i("zrl", "热敏打印机接入，无法获取权限");
//            }
//        }
//    }
//}