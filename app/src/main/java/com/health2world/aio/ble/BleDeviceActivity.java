package com.health2world.aio.ble;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.common.MsgEvent;
import com.health2world.aio.config.AppConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.ToastUtil;

/**
 * Created by lishiyou on 2018/11/22 0022.
 */

public class BleDeviceActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    private TextView tvTitle, tvScan, tvDisConnectAll;

    private ImageView ivBack;

    private ProgressBar progressBar;

    private RecyclerView recyclerView;

    private BleDeviceAdapter deviceAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ble_device;
    }

    @Override
    protected void initView() {
        tvScan = findView(R.id.tvScan);
        tvTitle = findView(R.id.tvTitle);
        ivBack = findView(R.id.ivBack);
        tvDisConnectAll = findView(R.id.tvDisConnectAll);
        progressBar = findView(R.id.progressBar);
        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        deviceAdapter = new BleDeviceAdapter(MyApplication.getInstance().getDeviceList());
        recyclerView.setAdapter(deviceAdapter);
        if (MyApplication.getInstance().getDeviceList().size() == 0)
            startScan();
    }

    @Override
    protected void initListener() {
        EventBus.getDefault().register(this);
        deviceAdapter.setOnItemChildClickListener(this);
        ivBack.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        tvScan.setOnClickListener(this);
        tvDisConnectAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ivBack:
            case R.id.tvTitle:
                finish();
                break;
            case R.id.tvScan:
                startScan();
                break;
            case R.id.tvDisConnectAll:
                BleManager.getInstance().disconnectAllDevice();
                break;
        }
    }

    private void startScan() {
        if (BleManager.getInstance().isSupportBle()) {
            if (!BleManager.getInstance().isBlueEnable())
                BleManager.getInstance().enableBluetooth();
            progressBar.setVisibility(View.VISIBLE);
            MyApplication.getInstance().scanDevice();
            ToastUtil.showShort("正在扫描蓝牙设备");
        } else {
            ToastUtil.showShort("该设备不支持蓝牙");
        }
    }

    //蓝牙设备扫描完成之后回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(MsgEvent event) {
        //设备扫描完毕
        if (event.getAction() == AppConfig.MSG_SCAN_FINISH) {
            ToastUtil.showShort("设备扫描完毕");
            progressBar.setVisibility(View.INVISIBLE);
            deviceAdapter.notifyDataSetChanged();
        }
        //设备连接状态改变
        if (event.getAction() == AppConfig.MSG_CONNECTION_CHANGED) {
            deviceAdapter.notifyDataSetChanged();
        }
        //设备连接失败
        if (event.getAction() == AppConfig.MSG_CONNECTION_FAIL) {
            BleDevice bleDevice = (BleDevice) event.getT();
            ToastUtil.showShort("设备" + bleDevice.getName() + "连接失败");
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        BleDevice bleDevice = (BleDevice) adapter.getItem(position);
        //添加空检查  --0326
        if (bleDevice != null)
            switch (view.getId()) {
                case R.id.btnConnect:
                    if (TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_5_1)) {
                        if (!BleManager.getInstance().isConnected(bleDevice)) {
                            ToastUtil.showShort("正在连接 " + bleDevice.getName());
                            MyApplication.getInstance().connectDevice(bleDevice);
                        }
                    }
                    if (TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_4_4_2)) {
                        if (getConnectCount() > 0) {
                            ToastUtil.showShort("请先断开已经连接的设备");
                            return;
                        }
                        if (!BleManager.getInstance().isConnected(bleDevice)) {
                            ToastUtil.showShort("正在连接 " + bleDevice.getName());
                            MyApplication.getInstance().connectDevice(bleDevice);
                        }
                    }
                    break;
                case R.id.btnDisConnect:
                    if (BleManager.getInstance().isConnected(bleDevice)) {
                        BleManager.getInstance().disconnect(bleDevice);
                    }
                    break;
            }
    }

    private int getConnectCount() {
        int count = 0;
        List<BleDevice> list = MyApplication.getInstance().getDeviceList();
        for (BleDevice device : list) {
            if (BleManager.getInstance().isConnected(device))
                count++;
        }
        return count;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
