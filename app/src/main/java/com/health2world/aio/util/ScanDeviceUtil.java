package com.health2world.aio.util;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.health2world.aio.MyApplication;
import com.health2world.aio.app.clinic.ClinicOneActivity;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.printer.DeviceManagerConnectActivity;
import com.health2world.aio.printer.PrinterDevice;
import com.stealthcopter.networktools.Ping;
import com.stealthcopter.networktools.PortScan;
import com.stealthcopter.networktools.ping.PingResult;
import com.stealthcopter.networktools.ping.PingStats;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import aio.health2world.utils.AppUtils;
import aio.health2world.utils.SPUtils;
import aio.health2world.utils.ToastUtil;

/**
 * @author Administrator
 * @date 2019/1/11/0011.
 */

public class ScanDeviceUtil {
    private static final String TAG = "zrl";

//    //核心池大小
//    private static final int CORE_POOL_SIZE = 1;
//    //线程池最大线程数
//    private static final int MAX_IMUM_POOL_SIZE = 255;

    private PrinterDevice mPrinterDevice;//每个打印机对象
    //    public static List<PrinterDevice> mIpList;// ping成功的IP地址
//    private ThreadPoolExecutor mExecutor;// 线程池对象
//    private Ping mPing;
    private static Callback callback;
    private static ScanDeviceUtil instance;
//    private static Timer _timer;
    public ScanDeviceUtil() {
    }

    public static ScanDeviceUtil getInstance() {
        if (instance == null) {
            instance = new ScanDeviceUtil();
//            Ping.onAddress()
//            _timer = new Timer();
        }
        return instance;
    }

    /**
     * TODO<扫描局域网内ip，找到对应服务器>
     *
     * @return void
     */
    public void scan() {
        String _devAddress = getLocAddress();
        String _locAddress = getLocAddrIndex(_devAddress);

        if (TextUtils.isEmpty(_locAddress)) {
            Log.e(TAG, "扫描失败，请检查网络");
        }
        Log.d(TAG, "开始扫描设备,本机Ip为：" + _devAddress);
//        mExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_IMUM_POOL_SIZE,
//                2000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(
//                CORE_POOL_SIZE));
        /*
          1.核心池大小 2.线程池最大线程数 3.表示线程没有任务执行时最多保持多久时间会终止
          4.参数keepAliveTime的时间单位，有7种取值,当前为秒
          5.一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响
         */
        //开始扫描任务后，设置倒计时,完成后强制退出
//        if (TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_4_4_2))
//            setTimerTask(30);
//        else
//            setTimerTask(15);

        // 新建一堆线程
        for (int i = 1; i < 255; i++) {// 创建255个线程分别去ping
            final String currentIp = _locAddress + i;
            if (currentIp.equals(_devAddress))
                continue;

//            mExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });

            Ping.onAddress(currentIp)
                    .setTimeOutMillis(300)
                    .setTimes(1)
                    .doPing(new Ping.PingListener() {
                        @Override
                        public void onResult(PingResult pPingResult) {
                            if (pPingResult.isReachable) {

                                Logger.d(TAG, "ping成功：" + currentIp);

                                try {
                                    PortScan.onAddress(currentIp)
                                            .setTimeOutMillis(100)
                                            .setPort(AppConfig.PRINTER_PORT)
                                            .setMethodTCP()
                                            .doScan(new PortScan.PortListener() {
                                                @Override
                                                public void onResult(int portNo, boolean open) {
                                                    if (open) {
                                                        mPrinterDevice = new PrinterDevice(currentIp, AppConfig.PRINTER_WIFI, true);
                                                        callback.scanCallback(mPrinterDevice);
                                                        Logger.d(TAG, "添加设备成功：" + currentIp);
                                                    }
                                                }

                                                @Override
                                                public void onFinished(ArrayList<Integer> pArrayList) {
                                                }
                                            });
                                } catch (UnknownHostException pE) {
                                    Logger.e(TAG, "未知地址");
                                    pE.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFinished(PingStats pPingStats) {

                        }

                        @Override
                        public void onError(Exception pE) {
                            Log.d(TAG, currentIp + " ping失败");
                        }
                    });
        }
    }


//    private void setTimerTask(int second) {
//        if(_timer == null){
//            _timer = new Timer();
//        }
//        if(_timer != null && mTask != null){
//            mTask.cancel();
//        }
//        mTask = new MyTimerTask();
//        _timer.schedule(mTask, 1000 * second);
//    }
//
//    private class MyTimerTask  extends  TimerTask{
//
//        @Override
//        public void run() {
//            //用空表示已经扫描完成
//            callback.scanCallback(null);
//        }
//    }

//    TimerTask task = new TimerTask() {
//        @Override
//        public void run() {
//
//        }
//    };

    /**
     * TODO<销毁正在执行的线程池>
     *
     * @return void
     */
    public void destory() {
//        if (mExecutor != null) {
//            mExecutor.shutdown();
//            mExecutor = null;
//        }
        System.gc();
    }

    public void setCallback(Callback callback) {
        ScanDeviceUtil.callback = callback;
    }

    public interface Callback {
        void scanCallback(PrinterDevice pd);
    }

    /**
     * TODO<获取本地ip地址>
     *
     * @return String
     */
    private String getLocAddress() {
        String ipaddress = "";

        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface networks = en.nextElement();
                // 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> address = networks.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (address.hasMoreElements()) {
                    InetAddress ip = address.nextElement();
                    if (!ip.isLoopbackAddress()) {
                        ipaddress = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(TAG, "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipaddress;
    }

    /**
     * TODO<获取本机IP前缀>
     *
     * @param devAddress // 本机IP地址
     * @return String
     */
    private String getLocAddrIndex(String devAddress) {
        if (!devAddress.equals("")) {
            return devAddress.substring(0, devAddress.lastIndexOf(".") + 1);
        }
        return null;
    }
}
