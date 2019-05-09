package com.health2world.aio.printer;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.common.MsgEvent;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.util.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.SPUtils;
import aio.health2world.utils.ToastUtil;

import static com.health2world.aio.MyApplication.isBentuUsbPrinterConnect;
//import static com.health2world.aio.printer.PrinterConn.startThreadHeartBeat;

/**
 * @author Runnlin
 * @date 2019/1/11/0011.
 */

public class DeviceManagerConnectActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView wifiRecyclerView;
    private FrameLayout frameLayout;
    private FrameLayout frameLayoutUsb;
    private final List<PrinterDevice> deviceList = new ArrayList<>();
    private PrinterAdapter mPrinterAdapter;
    private int curPosition = -1;
    private int lastPosition = -1;

    private CardView UsbPrintCard;
    private TextView titleBack;
    private ImageView titleHb, ivImage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pointer_connect;
    }

    @Override
    protected void initView() {
        frameLayout = findView(R.id.frameLayout_wifi);
        frameLayoutUsb = findView(R.id.frameLayout_usb);

        titleBack = findView(R.id.title_back);
        ivImage = findView(R.id.ivImage);
        titleHb = findView(R.id.img_hb);

        wifiRecyclerView = findView(R.id.recyclerView);
        wifiRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 7));

        UsbPrintCard = findView(R.id.card_usbPrint);


        if (isBentuUsbPrinterConnect) {
            frameLayoutUsb.setVisibility(View.VISIBLE);
            UsbPrintCard.setVisibility(View.VISIBLE);
        }
        if (AppConfig.isDebug) {
            titleHb.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        mPrinterAdapter = new PrinterAdapter(deviceList);
        wifiRecyclerView.setAdapter(mPrinterAdapter);
        mPrinterAdapter.bindToRecyclerView(wifiRecyclerView);
        ToastUtil.showLong("正在搜索打印机请耐心等待");
    }

    @Override
    protected void initListener() {
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPrinterAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (curPosition == position) {
            lastPosition = position;
            curPosition = -1;
            deviceList.get(position).setConnected(false);
            SPUtils.put(MyApplication.getInstance(), "WifiPrinterAddress", "0.0.0.0");
            MyApplication.setPrinterIp("0.0.0.0");
            MyApplication.isNetPrinterConnnect = false;
            Logger.i("zrl", "Printer Ip Reseted: " + MyApplication.getPrinterIp());
            ToastUtil.showShort("已取消网络客户端连接");
        } else {
            lastPosition = curPosition;
            curPosition = position;
            if (curPosition != -1)
                deviceList.get(curPosition).setConnected(true);
            if (lastPosition != -1)
                deviceList.get(lastPosition).setConnected(false);
            SPUtils.put(MyApplication.getInstance(), "WifiPrinterAddress", deviceList.get(position).getPrinterIP());
            MyApplication.setPrinterIp(deviceList.get(position).getPrinterIP());
            //添加ping并更新连接状态  添加选择某网络客户端后ping并更新连接状态；
//            PrinterConn.pingnscanPrinter(deviceList.get(position).getPrinterIP(), AppConfig.PRINTER_PORT);
            MyApplication.isNetPrinterConnnect = true;
            Logger.i("zrl", "Printer Ip Setting: " + MyApplication.getPrinterIp());
            ToastUtil.showShort("已连接网络客户端: " + MyApplication.getPrinterIp());
            setResult(RESULT_OK);
        }

        if (curPosition != -1)
            wifiRecyclerView
                    .getLayoutManager()
                    .getChildAt(curPosition)
                    .setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        if (lastPosition != -1)
            wifiRecyclerView
                    .getLayoutManager()
                    .getChildAt(lastPosition)
                    .setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(MsgEvent event) {
        //dm打印机状态监听
        if (event.getAction() == AppConfig.MSG_PRINTER_STATUS) {
            String[] paramValue = ((String) event.getT()).split(",");
            //接收服务的打印监听
            switch (Integer.parseInt(paramValue[0])) {
                case 0x00://状态改变
                    switch (Integer.parseInt(paramValue[1])) {
                        case 0:
                            //已连接
                            break;
                        case 1:
                            //断开
                            frameLayoutUsb.setVisibility(View.GONE);
                            UsbPrintCard.setVisibility(View.GONE);
                            break;

                        default:
                            break;
                    }
                    break;
                case 0x01://连接断开
                    switch (Integer.parseInt(paramValue[1])) {
                        case 0:
                            //USB的连接或断开
                            if (isBentuUsbPrinterConnect) {
                                frameLayoutUsb.setVisibility(View.VISIBLE);
                                UsbPrintCard.setVisibility(View.VISIBLE);
                                UsbPrintCard.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            }
                            break;

                        default:
                            break;
                    }
                    break;
            }
        }

        if (event.getAction() == AppConfig.MSG_RECEIVER_PRINTERIP) {
            String hostAddress = (String) event.getT();
            boolean isAdd = true;
            for (int i = 0; i < deviceList.size(); i++) {
                PrinterDevice printerDevice = deviceList.get(i);
                if (printerDevice.getPrinterIP().equals(hostAddress)) {
                    isAdd = false;
                }
            }
            if (isAdd) {
                PrinterDevice device = new PrinterDevice(hostAddress, AppConfig.PRINTER_WIFI, true);
                deviceList.add(device);
                mPrinterAdapter.notifyDataSetChanged();
                frameLayout.setVisibility(View.VISIBLE);
            }
        }

        //dm心跳监听
        if (AppConfig.isDebug && event.getAction() == AppConfig.MSG_DEVICE_MANAGER_HB) {
            Bundle pBundle = (Bundle) event.getT();
            if (pBundle != null) {
//            Logger.d("zrl", "HeartBeat: "+pBundle.getString("heartBeat"));
                wifiRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        titleHb.setImageResource(R.drawable.ico_ecg_sel);
                    }
                }, 500);
            }
            titleHb.setImageResource(R.drawable.ico_ecg);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
