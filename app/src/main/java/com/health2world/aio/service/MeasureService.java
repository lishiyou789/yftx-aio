package com.health2world.aio.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;

import com.caysn.printerlibs.printerlibs_caysnpos.printerlibs_caysnpos;
import com.clj.fastble.BleManager;
import com.health2world.aio.MyApplication;
import com.health2world.aio.app.clinic.ClinicOneActivity;
import com.health2world.aio.app.clinic.ClinicUtil;
import com.health2world.aio.app.clinic.MeasureDataActivity;
import com.health2world.aio.app.clinic.MeasureItemActivity;
import com.health2world.aio.app.clinic.recipe.RecipeActivity;
import com.health2world.aio.app.search.RSearchActivity;
import com.health2world.aio.common.DataServer;
import com.health2world.aio.common.MsgEvent;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.printer.PrinterConn;
import com.health2world.aio.util.Logger;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;
import com.konsung.bean.StateBean;
import com.konsung.listen.DeviceManagerStateListen;
import com.konsung.listen.Measure;
import com.konsung.listen.MeasureListener;
import com.konsung.util.MeasureUtils;
import com.sun.jna.Pointer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import aio.health2world.utils.AppUtils;
import aio.health2world.utils.DateUtil;
import aio.health2world.utils.SPUtils;
import aio.health2world.utils.ToastUtil;

import static com.health2world.aio.MyApplication.isBentuUsbPrinterConnect;
import static com.health2world.aio.MyApplication.isNetPrinterConnnect;
import static com.health2world.aio.config.AppConfig.ACTION_USB_PERMISSION;
import static com.health2world.aio.config.AppConfig.MSG_UPDATE_MEASURE_DATA;
import static com.health2world.aio.config.AppConfig.USB_PRINT_VID;

/**
 * Created by lishiyou on 2018/12/5 0005.
 */

public class MeasureService extends Service implements MeasureListener, DeviceManagerStateListen {

    public static final String TAG = "MeasureService";

    private final int PRINTER_CHANGE = 0x00;
    private final int PRINTER_DISCONNECT = 0x01;
    private final int PRINTER_RESULT = 0x02;

    private MeasureBean dataBean = new MeasureBean();

    private boolean isLogin = false;

    private ResidentBean resident;

//    private String activityName = "";

    private int printerState = -1;

    private int printerConnStyle = -1;

    private int printerResult = -1;
    //dm是否有在心跳
    public static boolean isDmHeartBeat = false;
    //门诊测量辅助类
    private ClinicUtil clinicUtil = new ClinicUtil();


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i(TAG, "服务已经启动");
        MeasureUtils.setMeasureBean(dataBean);
        MeasureUtils.setMeasureListener(this);//AD
        MeasureUtils.setPrinterStateListen(this);//DM
        EventBus.getDefault().register(this);
        mCountDownTimer.start();
    }

    //计时器 30秒执行一次数据上传操作 && 扫描血脂 && 检查DM启动状态
    private CountDownTimer mCountDownTimer = new CountDownTimer(1 * 30 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (isDmHeartBeat) {
                isDmHeartBeat = false;
            } else {
                if (AppConfig.isDebug)
                    Logger.i("zrl", "重启DM中。。。。");
                MyApplication.getInstance().reInitDevice();
            }
            DataServer.uploadMeasureData();
            checkDS100A();
            //添加每三十秒进行一次ping检查当前连接
            if (isNetPrinterConnnect) {
                PrinterConn.pingnscanPrinter(MyApplication.getPrinterIp(), AppConfig.PRINTER_PORT);
            }
            mCountDownTimer.start();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onComplete(Measure param, MeasureBean bean) {

        isLogin = (boolean) SPUtils.get(this, AppConfig.IS_LOGIN, false);

        resident = MyApplication.getInstance().getResident();

//        activityName = AppUtils.getRunningActivityName(this);

        if (isLogin && bean != null) {
            showMeasureData(param, bean);
        }
    }

    //重新扫描链接大树血脂
    private void checkDS100A() {
        boolean isConnect = MyApplication.getInstance().isFatConnect();
        if (!isConnect) {
            if (BleManager.getInstance().isSupportBle()) {
                if (!BleManager.getInstance().isBlueEnable()) {
                    BleManager.getInstance().enableBluetooth();
                }
            }
            if (!BleManager.getInstance().isBlueEnable())
                return;
            if (AppConfig.isDebug) {
                Logger.d("zrl", "尝试扫描连接大树血脂：" + DateUtil.getCurrentTime(new Date()));
            }

            MyApplication.getInstance().scanDevice();
        }
    }

    //不弹窗： 心电、血氧测量、门诊测量、门诊开方、居民选择页面
    private void showMeasureData(Measure param, MeasureBean bean) {
        if (AppUtils.getRunningActivityName(this).equals(ClinicOneActivity.class.getName())
                || (AppUtils.getRunningActivityName(this).equals(RecipeActivity.class.getName()))
                || (AppUtils.getRunningActivityName(this).equals(MeasureItemActivity.class.getName()))
                || (AppUtils.getRunningActivityName(this).equals(RSearchActivity.class.getName())))
            return;
        Bundle _bundle = new Bundle();
        _bundle.putSerializable("param", param);
        _bundle.putSerializable("bean", bean);
        if (bean != null) {
            //当现在页面是弹窗时，给他发送数据
            if (!MeasureDataActivity.class.getName().equals(AppUtils.getRunningActivityName(this))) {
                Intent intent = new Intent(this, MeasureDataActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtras(_bundle);
                startActivity(intent);
            } else {
                EventBus.getDefault().post(new MsgEvent<>(MSG_UPDATE_MEASURE_DATA, _bundle));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(MsgEvent<String> event) {
        //蓝牙设备数据
        if (event.getAction() == AppConfig.MSG_MEASURE_DATA) {
            isLogin = (boolean) SPUtils.get(this, AppConfig.IS_LOGIN, false);
            String data = event.getT();
            Logger.i("lsy", "mData=" + data);
            if (isLogin && !"".equals(data)) {
                //血脂数据
                if (data.startsWith(AppConfig.DS100A_DATA_HEAD)) {
                    clinicUtil.analysisData(data, dataBean);
                    showMeasureData(Measure.DS100A, dataBean);
                }
                //白细胞数据
                if (data.startsWith(AppConfig.WBC_DATA_HEAD)) {
                    clinicUtil.analysisWbcData(data, dataBean);
                    showMeasureData(Measure.WBC, dataBean);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPrinterHeartBeat(Bundle pBundle) {
//        Logger.i("zrl", "MeasureService HB: "+pBundle);
        isDmHeartBeat = true;
        EventBus.getDefault().post(new MsgEvent<>(AppConfig.MSG_DEVICE_MANAGER_HB, pBundle));
    }

    @Override
    public void onPrinterState(StateBean bean) {
        if (bean != null) {
            printerState = bean.getPrintDeviceState();
            if (printerState != -1) {
                setPrinterCallback(PRINTER_CHANGE, bean.getPrintDeviceState());
            }
            printerConnStyle = bean.getPrintConnState();
            if (printerConnStyle != -1) {
                setPrinterCallback(PRINTER_DISCONNECT, bean.getPrintConnState());
            }
            printerResult = bean.getPrintResult();
            if (printerResult != -1) {
                setPrinterCallback(PRINTER_RESULT, bean.getPrintResult());
            }
        }
    }

    @Override
    public void onPointState(int param, int value) {
        if (param == 0x00) {
            String msg;
            switch (value) {
                case 0:
                    msg = "连接成功";
                    break;
                case 2:
                    msg = "已断开，请拔插设备";
                    break;
                default:
                    msg = "已断开";
                    break;
            }
            ToastUtil.showShort("点测状态改变--连接状态:" + msg);
        }
        if (param == 0x01) {
            ToastUtil.showShort("点测设备已连接, 方式: " + (value == 0 ? "USB" : "其他"));
        }
    }

    public void setPrinterCallback(int param, int value) {
        Logger.i("lsy", "param=" + param + ",value=" + value);
        switch (param) {
            case PRINTER_CHANGE://状态改变
                switch (value) {
                    case 0:
                        Logger.d("zrl", "打印设备连接成功");
//                        ToastUtil.showShort("USB打印设备连接成功");
                        break;
                    case 1:
                        Logger.d("zrl", "USB已断开");
                        ToastUtil.showShort("USB已断开");
                        isBentuUsbPrinterConnect = false;
                        //USB断开后ping一下网络
                        PrinterConn.pingnscanPrinter(MyApplication.getPrinterIp(), AppConfig.PRINTER_PORT);
                        break;
                    case 2:
                        Logger.d("zrl", "重新拔插USB");
                        ToastUtil.showShort("请重新拔插USB");
                        isBentuUsbPrinterConnect = false;
                        break;
                    default:
                        break;
                }
                break;
            case PRINTER_DISCONNECT://连接断开
                switch (value) {
                    case 0:
                        Logger.d("zrl", "USB连接");
                        ToastUtil.showShort("奔图打印机已连接");
                        isBentuUsbPrinterConnect = true;
                        break;
                    case 1:
                        Logger.d("zrl", "串口连接");
//                        ToastUtil.showShort("串口连接");
                        break;
                    case 2:
                        Logger.d("zrl", "WIFI连接");
//                        ToastUtil.showShort("WIFI连接");
                        break;
                    case 3:
                        Logger.d("zrl", "蓝牙连接");
//                        ToastUtil.showShort("蓝牙连接");
                        break;
                    default:
                        break;
                }
                break;
            case PRINTER_RESULT://打印结果
                switch (value) {
                    case 0:
                        Logger.d("zrl", "Service打印成功");
                        ToastUtil.showShort("打印成功");
                        break;
                    case 1:
                        Logger.d("zrl", "文件不存在");
                        ToastUtil.showShort("文件不存在");
                        break;
                    case 2:
                        Logger.d("zrl", "不支持该命令");
                        ToastUtil.showShort("不支持该命令");
                        break;
                    case 3:
                        Logger.d("zrl", "文件路径过长");
                        ToastUtil.showShort("文件路径过长");
                        break;
                    case 4:
                        Logger.d("zrl", "没有设备");
                        ToastUtil.showShort("没有设备，请检查设备或重新拔插设备");
                    case 255:
                        Logger.d("zrl", "未知错误");
                        ToastUtil.showShort("未知错误");
                        break;
                    default:
                        break;
                }
                break;

            default:
                break;
        }
        int[] paramValueArr = {param, value};
        EventBus.getDefault().post(new MsgEvent<>(AppConfig.MSG_PRINTER_STATUS, paramValueArr));
    }
}
