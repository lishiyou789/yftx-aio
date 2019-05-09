package com.health2world.aio.util;

import com.health2world.aio.common.MsgEvent;
import com.health2world.aio.config.AppConfig;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by lishiyou on 2019/3/20 0020.
 */

public class UDPUtils extends Thread {
    private static UDPUtils ourInstance;

    private DatagramSocket socket;

    private DatagramPacket packet;

    private boolean flag = true;

    public static synchronized UDPUtils getInstance() {
        if (ourInstance == null)
            ourInstance = new UDPUtils();
        return ourInstance;
    }

    private UDPUtils() {
        try {
            socket = new DatagramSocket(AppConfig.PRINTER_PORT);
            socket.setSoTimeout(5 * 1000);
            socket.setBroadcast(true);
            byte[] _data = new byte[64];
            packet = new DatagramPacket(_data, 0, _data.length);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (flag) {
            try {
                socket.receive(packet);

                if (AppConfig.isDebug) {
                    String result = new String(packet.getData(), packet.getOffset(), packet.getLength());
                    Logger.i("zrl", "receive ip is " + packet.getAddress().getHostAddress()
                            + " result is " + result);
                }

                EventBus.getDefault().post(new MsgEvent<>(AppConfig.MSG_RECEIVER_PRINTERIP, packet.getAddress().getHostAddress()));
            } catch (SocketTimeoutException e) {
                if (flag) {
                    Logger.e("zrl", "接收UDP广播超时");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onStart() {
        this.start();
    }

    public void onClose() {
        flag = false;
        socket.close();
    }
}
