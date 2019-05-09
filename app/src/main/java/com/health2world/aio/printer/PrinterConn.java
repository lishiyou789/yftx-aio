package com.health2world.aio.printer;

import android.os.Environment;
import android.util.Log;

import com.health2world.aio.MyApplication;
import com.health2world.aio.common.MsgEvent;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.util.Logger;
import com.stealthcopter.networktools.Ping;
import com.stealthcopter.networktools.PortScan;
import com.stealthcopter.networktools.ping.PingResult;
import com.stealthcopter.networktools.ping.PingStats;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import aio.health2world.utils.NetworkUtil;
import aio.health2world.utils.ToastUtil;

import static com.health2world.aio.MyApplication.isNetPrinterConnnect;

/**
 * 向目标IP及端口发送消息
 * @author Administrator
 * @date 2019/1/11/0011.
 */

public class PrinterConn {

    //与服务器建立连接的socket
    private static Socket mSocket;

    public static boolean send() {
        if (("0.0.0.0").equals(MyApplication.getPrinterIp())) {
            ToastUtil.showShort("请先选择打印机");
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(
                            MyApplication.getPrinterIp(),
                            AppConfig.PRINTER_PORT);
                    DataOutputStream out =
                            new DataOutputStream(mSocket.getOutputStream());
                    sendImg(out);

                } catch (IOException pE) {
                    pE.printStackTrace();
                }
            }
        }).start();
        return true;
    }

    private static void sendImg(DataOutputStream out) {
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bmp/print.bmp";
            File file = new File(path);
            if (!file.exists()) {
                return;
            }
            InputStream is = new FileInputStream(file);
            out.write(input2byte(is));
            out.close();
            mSocket.close();
        } catch (IOException pE) {
            pE.printStackTrace();
        }
    }

    private static byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[inStream.available()];
        int rc;
        while ((rc = inStream.read(buff, 0, inStream.available())) > 0) {
            swapStream.write(buff, 0, rc);
        }

        return swapStream.toByteArray();
    }

    /**
     * ping IP并scan端口
     * @param ip 待pingIP
     * @param port 待scan port
     */
    public static void pingnscanPrinter(final String ip, final int port) {
        if ("0.0.0.0".equals(ip)) {
            isNetPrinterConnnect = false;
            return;
        }
        Ping.onAddress(ip)
                .setTimeOutMillis(500)
                .setTimes(1)
                .doPing(new Ping.PingListener() {
                    @Override
                    public void onResult(PingResult pPingResult) {
                        if (pPingResult.isReachable) {

                            Log.d("zrl", "ping成功：" + ip);

                            try {
                                PortScan.onAddress(ip)
                                        .setTimeOutMillis(500)
                                        .setPort(port)
                                        .setMethodTCP()
                                        .doScan(new PortScan.PortListener() {
                                            @Override
                                            public void onResult(int portNo, boolean open) {
                                                isNetPrinterConnnect = open;
                                                EventBus.getDefault().post(new MsgEvent<>(AppConfig.MSG_NET_PRINTER_STATUS));
                                            }

                                            @Override
                                            public void onFinished(ArrayList<Integer> pArrayList) {
                                            }
                                        });
                            } catch (UnknownHostException pE) {
                                Log.e("zrl", "未知地址");
                                pE.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFinished(PingStats pPingStats) {
                    }

                    @Override
                    public void onError(Exception pE) {
                        Log.d("zrl", port + " ping失败");
                        isNetPrinterConnnect = false;
                    }

                });
    }


    /**
     *  接收心跳包
     */
    public static void startThreadHeartBeat() {
        if (!NetworkUtil.isConnected(MyApplication.getInstance()))
            return;
        //循环发送心跳包的线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket();
                    mSocket.connect(
                            new InetSocketAddress(MyApplication.getPrinterIp(),
                                    AppConfig.PRINTER_PORT), 2000);
                    //设置长时间没有连接就断开
                    if (!mSocket.getKeepAlive()) mSocket.setKeepAlive(true);
                    //允许发送紧急数据
                    if (!mSocket.getOOBInline()) mSocket.setOOBInline(true);
                    //获取socket的心跳输出流
//                    OutputStream mOutputStream = mSocket.getOutputStream();
                    OutputStream _outputStream = mSocket.getOutputStream();

                    while (!"0.0.0.0".equals(MyApplication.getPrinterIp()) && mSocket.isConnected()){
                        try {
                            //20s发送一次心跳
                            Thread.sleep(10 * 1000);
                            if (MyApplication.getInstance().getDoctorId() != 0) {
                                String socketContent = "医生ID："+System.currentTimeMillis();
                                Logger.d("zrl", "给网络客户端发送心跳: " + socketContent);
//                                _outputStream.write(socketContent.getBytes(StandardCharsets.UTF_8));
                                _outputStream.write(socketContent.getBytes(StandardCharsets.UTF_8));
                                _outputStream.flush();
                            }
                        } catch (InterruptedException pE) {
                            pE.printStackTrace();
                        } catch (IOException pE) {
                            pE.printStackTrace();
                        }
                    }

                } catch (IOException pE) {
                    pE.printStackTrace();
                }
            }
        }).start();
    }

}
