package com.health2world.aio;

import android.app.Application;
import android.app.PendingIntent;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.caysn.printerlibs.printerlibs_caysnlabel.printerlibs_caysnlabel;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.exception.BleException;
import com.health2world.aio.bean.MeasureItem;
import com.health2world.aio.ble.BleGattCallbackWrap;
import com.health2world.aio.ble.BleNotifyCallbackWrap;
import com.health2world.aio.ble.BleScanCallbackWrap;
import com.health2world.aio.common.MsgEvent;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.config.KonsungConfig;
import com.health2world.aio.config.MeasureConfig;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.printer.PrinterConn;
import com.health2world.aio.provider.Health2WorldResolver;
import com.health2world.aio.service.MeasureService;
import com.health2world.aio.util.ActivityUtil;
import com.health2world.aio.util.Logger;
import com.health2world.aio.util.StringUtils;
import com.health2world.aio.util.UDPUtils;
import com.konsung.bean.ResidentBean;
import com.konsung.util.MeasureUtils;
import com.sun.jna.Pointer;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aio.health2world.SApplication;
import aio.health2world.utils.AppManager;
import aio.health2world.utils.SPUtils;
import aio.health2world.utils.ToastUtil;
import cn.jpush.android.api.JPushInterface;

import static com.health2world.aio.config.AppConfig.ACTION_USB_PERMISSION;
import static com.health2world.aio.config.AppConfig.PRINTER_PORT;


/**
 * Created by lishiyou on 2017/6/27.
 */

public class MyApplication extends Application {

    public static MyApplication INSTANCE;
    private Context mContext;
    //    private ApplicationLike tinkerApplicationLike;
    //存储门诊界面的门诊数据
    private List<MeasureItem> dataList = new ArrayList<>();

    private ResidentBean resident;

    //大树血脂设备是否已连接
    private boolean isFatConnect = false;
    //白细胞设备是否已连接
    private boolean isWbcConnect = false;
    //扫描到的蓝牙设备的集合
    private List<BleDevice> deviceList = new ArrayList<>();
    //接收蓝牙设备传递过来的数据
    private String mData = "";
    //奔图USB打印机设备是否已连接
    public static boolean isBentuUsbPrinterConnect = false;
    //WIFI客户端是否已连接
    private static String PRINTER_IP = "0.0.0.0";
    public static boolean isNetPrinterConnnect = false;
    //现在连接的打印机类型 1：USB奔图  2：USB热敏  3：网络

    private Pointer h;
    //打印页面字体
    private Typeface mTypeface;

    //应用程序是否被覆盖安装
    private boolean isReplace = false;

    public MyApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        mContext = this;

        PRINTER_IP = (String) SPUtils.get(this, "WifiPrinterAddress", "0.0.0.0");
        Logger.i("zrl", "Printer Ip Setting: " + PRINTER_IP);
//        initTinkerPatch();
        initData();
    }

    public static MyApplication getInstance() {
        return INSTANCE;
    }


    //登录者的tokenId
    public String getTokenId() {
        return (String) SPUtils.get(this, AppConfig.TOKEN_ID, "");
    }

    //登陆者医生Id
    public int getDoctorId() {
        return (int) SPUtils.get(this, AppConfig.DOCTOR_ID, 0);
    }

    //登录者的机构Id
    public String getOrgId() {
        return (String) SPUtils.get(this, AppConfig.ORG_ID, "");
    }

    //医服服务器地址
    public String getServerUrl() {
        if (AppConfig.isDebug)
            return (String) SPUtils.get(this, AppConfig.SERVER_URL, AppConfig.DEFAULT_SERVER_URL);
        else
            return AppConfig.RELEASE_SERVER_URL;
    }

    //康尚家庭医生地址
    public String getPublicHealthUrl() {
        return (String) SPUtils.get(this, KonsungConfig.PUBLIC_HEALTH_URL, KonsungConfig.DEFAULT_PUBLIC_HEALTH_URL);
    }

    //测量模式 默认使用医服的测量模块
    public boolean getMeasureMode() {
        return (boolean) SPUtils.get(this, AppConfig.MEASURE_MODE, true);
    }

    //获取测量项的配置
    public String getMeasureConfig() {
        return (String) SPUtils.get(this, AppConfig.MEASURE_CONFIG, AppConfig.DEFAULT_MEASURE_CONFIG);
    }

    //获取测量驱动的配置
    public int getDeviceConfig() {
        return (int) SPUtils.get(this, AppConfig.DEVICE_CONFIG, MeasureConfig.Device.EA12.getConfig());
    }

    //获取设备编号 4.4.2获取本程序配置的  5.1获取厂家维护软件中配置的
    public String getDeviceCode() {
        return Health2WorldResolver.getInstance().getDeviceNo();
    }

    //设置当前居民的身份证号码
    public void setCurrentIdentityCard(String identityCard) {
        SPUtils.put(this, AppConfig.IDENTITY_CARD, identityCard);
    }

    //获取当前居民的身份证号码
    public String getCurrentIdentityCard() {
        return (String) SPUtils.get(this, AppConfig.IDENTITY_CARD, "");
    }

    public ResidentBean getResident() {
        return resident;
    }

    public void setResident(ResidentBean pResident) {
        resident = pResident;
    }

    public List<BleDevice> getDeviceList() {
        return deviceList;
    }

    public boolean isFatConnect() {
        return isFatConnect;
    }

    public boolean isWbcConnect() {
        return isWbcConnect;
    }

    public Pointer getPrinter() {
        return h;
    }

    public void setPrinter(Pointer pH) {
        h = pH;
    }

    public Typeface getTypeface() {
        return mTypeface;
    }

    public boolean isReplcaed() {
        return isReplace;
    }

    public void setReplace(boolean replace) {
        isReplace = replace;
    }

    //退出当前程序 回到登录界面
    public void logout() {
        resident = null;
        deviceList.clear();
        SPUtils.put(this, AppConfig.IS_LOGIN, false);
        AppManager.getInstance().finishAllActivity();
        Intent intent = new Intent("com.health2world.aio.login");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public List<MeasureItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<MeasureItem> dataList) {
        this.dataList = dataList;
    }

    public static String getPrinterIp() {
        return PRINTER_IP;
    }

    public static void setPrinterIp(String pPrinterIp) {
        PRINTER_IP = pPrinterIp;
        SPUtils.put(MyApplication.getInstance(), "WifiPrinterAddress", PRINTER_IP);
        PrinterConn.pingnscanPrinter(PRINTER_IP, PRINTER_PORT);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
        SPUtils.put(this, AppConfig.IS_LOGIN, false);
        stopService(new Intent(this, MeasureService.class));
        MeasureUtils.unbindService();
        DBManager.getInstance().closeDB();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
        UDPUtils.getInstance().onClose();
    }

    //数据初始化
    private void initData() {
        //启动测量监听等服务
        startService(new Intent(this, MeasureService.class));

        UDPUtils.getInstance().onStart();

        h = Pointer.NULL;
        SApplication.init(mContext, AppConfig.isDebug);
        CrashReport.initCrashReport(getApplicationContext(), AppConfig.ID_BUGLY, false);

        JPushInterface.init(this);
        JPushInterface.setDebugMode(AppConfig.isDebug);

        MeasureConfig.writeXmlConfig();

        try {
            MeasureUtils.startAppDevice(MyApplication.INSTANCE, 1, 0);
            Thread.sleep(100);

            MeasureUtils.startAppDevice(MyApplication.INSTANCE, 2, getDeviceConfig());

            Logger.i("lsy", "DeviceConfig=" + getDeviceConfig());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (android.os.Build.VERSION.RELEASE.startsWith("4")) {
            mTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
        } else {
            mTypeface = Typeface.createFromAsset(getAssets(), "仿宋GB2312.ttf");
        }
        initBle();
        if (AppConfig.isDebug) {
            PackageInfo packageInfo = ActivityUtil.getAppInfo(KonsungConfig.DEVICE_MANAGER_PACKAGE);
            if (packageInfo != null) {
                ToastUtil.showLong("DeviceManager版本号" + packageInfo.versionCode);
            }
            //开启dm调试log
            Intent intent = new Intent();
            intent.setAction("show_log");
            intent.putExtra("isShow", true);
            sendBroadcast(intent);
        } else {
            Intent intent = new Intent();
            intent.setAction("show_log");
            intent.putExtra("isShow", false);
            sendBroadcast(intent);
        }
    }

    //重启dm
    public void reInitDevice() {
        try {
            MeasureUtils.startAppDevice(MyApplication.INSTANCE, 1, 0);
            Logger.i("lsy", "DeviceConfig=" + getDeviceConfig());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 蓝牙初始化
     */
    private void initBle() {
        BleManager.getInstance().init(this);
        BleManager.getInstance()
                .enableLog(AppConfig.isDebug)
                .setReConnectCount(3, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);

        //开机自动扫描蓝牙设备
        if (BleManager.getInstance().isSupportBle()) {
            if (!BleManager.getInstance().isBlueEnable())
                BleManager.getInstance().enableBluetooth();
            MyApplication.getInstance().scanDevice();
        }
    }

    /**
     * 添加设备到集合
     *
     * @param bleDevice
     */
    private void addDevice(BleDevice bleDevice) {
        removeDevice(bleDevice);
        deviceList.add(bleDevice);
    }

    /**
     * 移除设备
     *
     * @param bleDevice
     */
    private void removeDevice(BleDevice bleDevice) {
        for (int i = 0; i < deviceList.size(); i++) {
            BleDevice device = deviceList.get(i);
            if (bleDevice.getKey().equals(device.getKey())) {
                deviceList.remove(i);
            }
        }
    }

    /**
     * 扫描设备
     */
    public void scanDevice() {
        if (BleManager.getInstance().getScanSate() == BleScanState.STATE_SCANNING)
            return;
        clearDevice();
        BleManager.getInstance().enableLog(false).scan(new BleScanCallbackWrap() {
            @Override
            public void onScanFinished(List<BleDevice> list) {
                EventBus.getDefault().post(new MsgEvent<>(AppConfig.MSG_SCAN_FINISH, list));
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                Logger.i("lsy", "name=" + bleDevice.getName() + ",mac=" + bleDevice.getMac());
                //添加随时检查蓝牙是否已打开  --0320
                if (BleManager.getInstance().isBlueEnable() && bleDevice.getName() != null) {
                    addDevice(bleDevice);
                    //自动连接大树血脂
                    if (bleDevice.getName().startsWith(AppConfig.DS100A_BLUETOOTH)) {
                        connectDevice(bleDevice);
                    }
                }
            }
        });
    }

    /**
     * 连接蓝牙设备
     *
     * @param bleDevice
     */
    public void connectDevice(BleDevice bleDevice) {
        //目标设备已经连接则直接返回
        //添加非空检查  0325
        if (bleDevice == null || BleManager.getInstance().isConnected(bleDevice)) {
            return;
        }
        BleManager.getInstance().enableLog(false).connect(bleDevice, new BleGattCallbackWrap() {
            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt bluetoothGatt, int i) {

                String deviceName = bleDevice.getName();

                if (!TextUtils.isEmpty(deviceName) && deviceName.startsWith(AppConfig.WBC_BLUETOOTH)) {
                    isWbcConnect = true;
                    wbcNotify(bleDevice);
                }
                if (!TextUtils.isEmpty(deviceName) && deviceName.startsWith(AppConfig.DS100A_BLUETOOTH)) {
                    isFatConnect = true;
                    fatNotify(bleDevice);
                }
                EventBus.getDefault().post(new MsgEvent(AppConfig.MSG_CONNECTION_CHANGED));
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException e) {
                super.onConnectFail(bleDevice, e);
                EventBus.getDefault().post(new MsgEvent<>(AppConfig.MSG_CONNECTION_FAIL, bleDevice));
            }

            @Override
            public void onDisConnected(boolean b, BleDevice bleDevice, BluetoothGatt bluetoothGatt, int i) {
                //添加随时检查蓝牙是否已打开  --0325
                if (BleManager.getInstance().isBlueEnable() && bleDevice != null && bleDevice.getName() != null) {
                    if (bleDevice.getName().startsWith(AppConfig.WBC_BLUETOOTH)) {
                        isWbcConnect = false;
                    }
                    if (bleDevice.getName().startsWith(AppConfig.DS100A_BLUETOOTH)) {
                        isFatConnect = false;
                    }
                } else if (!BleManager.getInstance().isBlueEnable()) {
                    isWbcConnect = false;
                    isFatConnect = false;
                }
                EventBus.getDefault().post(new MsgEvent(AppConfig.MSG_CONNECTION_CHANGED));
            }
        });
    }

    /**
     * 设置大树蓝牙设备数据接收通知
     *
     * @param bleDevice
     */
    private void fatNotify(BleDevice bleDevice) {
        BleManager.getInstance().notify(bleDevice, AppConfig.DS100A_UUID_SERVICE, AppConfig.DS100A_UUID_NOTIFY,
                new BleNotifyCallbackWrap() {
                    @Override
                    public void onCharacteristicChanged(byte[] bytes) {
                        Logger.i("lsy", "fat data " + Arrays.toString(bytes));
                        String data = StringUtils.byte2HexString(bytes);
                        if (data.startsWith(AppConfig.DS100A_DATA_HEAD)) {
                            mData = data;
                        } else {
                            mData += data;
                        }
                        //将最终结果发送到门诊界面显示
                        if (mData.length() == 72 && mData.startsWith(AppConfig.DS100A_DATA_HEAD)) {
                            EventBus.getDefault().post(new MsgEvent<>(AppConfig.MSG_MEASURE_DATA, mData));
                        }
                    }
                });
    }

    /**
     * 设置白细胞设备数据接收通知
     *
     * @param bleDevice
     */
    private void wbcNotify(BleDevice bleDevice) {
        BleManager.getInstance().notify(bleDevice, AppConfig.WBC_UUID_SERVICE, AppConfig.WBC_UUID_NOTIFY,
                new BleNotifyCallbackWrap() {
                    @Override
                    public void onCharacteristicChanged(byte[] bytes) {
                        Logger.i("lsy", "wbc data " + Arrays.toString(bytes));
                        String data = StringUtils.byte2HexString(bytes);
                        if (data.startsWith(AppConfig.WBC_DATA_HEAD)) {
                            mData = data;
                            if (mData.length() == 40) {
                                EventBus.getDefault().post(new MsgEvent<>(AppConfig.MSG_MEASURE_DATA, mData));
                            }
                        }
                    }
                });
    }


    /**
     * 清除所有设备
     */
    public void clearDevice() {
        if (deviceList != null) {
            deviceList.clear();
        }
    }
//    private void initTinkerPatch() {
//        // 我们可以从这里获得Tinker加载过程的信息
//        tinkerApplicationLike = TinkerPatchApplicationLike
//                .getTinkerPatchApplicationLike();
//        // 初始化TinkerPatch SDK
//        TinkerPatch.init(tinkerApplicationLike)
//                .reflectPatchLibrary()
//                .setPatchRollbackOnScreenOff(true)
//                .setPatchRestartOnSrceenOff(true)
//                .setFetchPatchIntervalByHours(1);
//        // 获取当前的补丁版本
//        Logger.d("lsy", "Current patch version is " + TinkerPatch.with().getPatchVersion());
//        // fetchPatchUpdateAndPollWithInterval 与 fetchPatchUpdate(false)
//        // 不同的是，会通过handler的方式去轮询
//        TinkerPatch.with().fetchPatchUpdateAndPollWithInterval();
//    }

}
