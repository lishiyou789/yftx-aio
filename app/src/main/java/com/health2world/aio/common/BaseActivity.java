package com.health2world.aio.common;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.health2world.aio.bean.NetStatus;
import com.health2world.aio.util.Logger;

import java.util.Calendar;

import aio.health2world.utils.AppManager;
import aio.health2world.utils.NetworkUtil;


/**
 * Created by Administrator on 2018/7/3 0003.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "BaseActivity";

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    protected Activity mContext;

    private SparseArray<View> mViews;

    private Health2WorldReceiver receiver;

    private NetStatus netStatus = NetStatus.NONE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //设置横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 隐藏标题
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //保持屏幕不熄灭
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(getLayoutId());
        initPublic();
        initView();
        initData();
        initListener();
    }

    public void initPublic() {

        mViews = new SparseArray<>();

        AppManager.getInstance().addActivity(this);

        receiver = new Health2WorldReceiver();

        IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_BATTERY_CHANGED);//电量

        filter.addAction(Intent.ACTION_TIME_TICK);//时间

        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);//wifi信号变化

        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);//网络连接变化

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙变化

        registerReceiver(receiver, filter);
    }

    /**
     * 通过id找到view
     */
    public <E extends View> E findView(int viewId) {
        E view = (E) mViews.get(viewId);
        if (view == null) {
            view = (E) findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * view设置OnClick事件
     */
    public <E extends View> void setOnClickListener(E view) {
        view.setOnClickListener(this);
    }

    /**
     * 更新当前设备电量
     */
    protected void batteryChanged(int current, int percent, int status) {
    }

    /**
     * 更新当前时间
     */
    protected void timeChanged(int hour, int min) {
        Logger.e(TAG, hour + ":" + min);
    }

    /**
     * wifi信号强度有变化
     */
    protected void wifiLevelChanged(NetStatus netStatus) {
        Logger.i(TAG, "netStatus=" + netStatus);
    }

    /**
     * 蓝牙变化
     */
    protected void blueToothChanged(int blueState) {
        Logger.i(TAG, "blueState=" + blueState);
    }

    /**
     * 网络状态有变化（wifi切换到手机卡网络）
     */
    protected void netWorkChanged(int netType) {
        Logger.i(TAG, "netType=" + netType);
    }

    public class Health2WorldReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //电量变化的广播
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                int current = intent.getExtras().getInt(BatteryManager.EXTRA_LEVEL, 0);// 获得当前电量
                int total = intent.getExtras().getInt(BatteryManager.EXTRA_SCALE, 0);// 获得总电量
                int percent = current * 100 / total;
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                        BatteryManager.BATTERY_STATUS_UNKNOWN);
                //BatteryManager.BATTERY_STATUS_FULL 充电完成
                //BatteryManager.BATTERY_STATUS_NOT_CHARGING 未充电
                //BatteryManager.BATTERY_STATUS_DISCHARGING 放电中
                batteryChanged(current, percent, status);
            }
            //时间变化广播(一分钟广播一次)
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                timeChanged(Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE));
            }

            //网络状态有变化
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                //获取网络类型
                int netType = NetworkUtil.getNetWorkType(BaseActivity.this);
                netWorkChanged(netType);
            }

            //蓝牙状态改变
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                blueToothChanged(blueState);
            }

            //wifi信号强度有变化
            if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
                //获取wifi信号强度
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                //获得信号强度值
                int level = wifiManager.getConnectionInfo().getRssi();
                if (level <= 0 && level >= -50) {
                    //信号最好
                    if (netStatus != NetStatus.PERFECT) {
                        netStatus = NetStatus.PERFECT;
                        wifiLevelChanged(netStatus);
                    }
                } else if (level < -50 && level >= -70) {
                    //信号较好
                    if (netStatus != NetStatus.GOOD) {
                        netStatus = NetStatus.GOOD;
                        wifiLevelChanged(netStatus);
                    }
                } else if (level < -70 && level >= -80) {
                    //信号一般
                    if (netStatus != NetStatus.NORMAL) {
                        netStatus = NetStatus.NORMAL;
                        wifiLevelChanged(netStatus);
                    }
                } else if (level < -80 && level >= -100) {
                    //信号较差
                    if (netStatus != NetStatus.BAD) {
                        netStatus = NetStatus.BAD;
                        wifiLevelChanged(netStatus);
                    }
                } else {
                    //无信号
                    if (netStatus != NetStatus.NONE) {
                        netStatus = NetStatus.NONE;
                        wifiLevelChanged(netStatus);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
