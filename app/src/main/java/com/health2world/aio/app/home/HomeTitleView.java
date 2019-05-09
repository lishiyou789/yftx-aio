package com.health2world.aio.app.home;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.BatteryManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.health2world.aio.R;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.bean.NetStatus;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.view.BatteryView;

import java.util.Calendar;

import aio.health2world.glide_transformations.CropCircleTransformation;
import aio.health2world.utils.NetworkUtil;

import static android.content.Context.BATTERY_SERVICE;

/**
 * 组合控件 首页头部搜索框 医生信息 电量 时间
 * Created by lishiyou on 2018/7/11 0011.
 */

public class HomeTitleView extends FrameLayout implements View.OnClickListener {

    public static final int TAB_INDEX_ADD_RESIDENT = 0x50;
    public static final int TAB_INDEX_SEARCH = 0x51;
    private Context mContext;

    private LinearLayout llDoctorMsg, llSearchLayout;

    private TextView tvTime, tvDoctorName, tvDoctorCode;

    private ImageView ivDoctorImage, ivWifi, ivBlueTooth, ivNetwork;
    private TabChangedListener listener;
    private BatteryView batteryView;
    private TextView mTvAddResident;
    private SettingPopupWindow popupWindow;
    private DoctorBean doctor;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HomeTitleView(@NonNull Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HomeTitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View.inflate(context, R.layout.layout_home_title, this);
        initView();
        initData();
        initListener();
    }

    public TextView getTvTime() {
        return tvTime;
    }

    public BatteryView getBatteryView() {
        return batteryView;
    }

    private void initView() {
        tvTime = findViewById(R.id.tvTime);
        batteryView = findViewById(R.id.batteryView);
        llDoctorMsg = findViewById(R.id.llDoctorMsg);
        llSearchLayout = findViewById(R.id.llSearchLayout);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvDoctorCode = findViewById(R.id.tvDoctorCode);
        mTvAddResident = findViewById(R.id.tv_add_resident);
        ivDoctorImage = findViewById(R.id.ivDoctorImage);
        ivWifi = findViewById(R.id.ivWifi);
        ivBlueTooth = findViewById(R.id.ivBlueTooth);
        ivNetwork = findViewById(R.id.ivNetwork);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        //医生信息
        doctor = DBManager.getInstance().getCurrentDoctor();
        if (doctor != null) {
            tvDoctorName.setText(doctor.getName());
            tvDoctorCode.setText("医服码:" + doctor.getDoctorCode());
            Glide.with(mContext)
                    .load(doctor.getPortrait())
                    .placeholder(R.mipmap.user_portrait_circle)
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(ivDoctorImage);
        }
        //显示当前的时间
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        tvTime.setText(hours + ":" + (min < 10 ? "0" + min : min));
        //显示当前的电量
        BatteryManager batteryManager = (BatteryManager) mContext.getSystemService(BATTERY_SERVICE);
        if (batteryManager != null) {
            int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            batteryView.setPower(battery);
        }
        //获取当前的网络状态
        int netType = NetworkUtil.getNetWorkType(mContext);
        if (netType == NetworkUtil.NETWORK_WIFI) {
            ivWifi.setImageResource(R.mipmap.wifi_icon);
            ivNetwork.setImageResource(R.mipmap.signal_icon_gray);
        } else if (netType == NetworkUtil.NETWORK_UNKNOWN) {
            ivWifi.setImageResource(R.mipmap.wifi_icon_gray);
            ivNetwork.setImageResource(R.mipmap.signal_icon_gray);
        } else {
            ivWifi.setImageResource(R.mipmap.wifi_icon_gray);
            ivNetwork.setImageResource(R.mipmap.signal_icon);
        }
        //蓝牙状态
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter == null || !blueAdapter.isEnabled())
            //无蓝牙模块   //蓝牙已经关闭
            ivBlueTooth.setImageResource(R.mipmap.bluetooth_icon_gray);
        else if (blueAdapter.isEnabled())
            //已经开启蓝牙
            ivBlueTooth.setImageResource(R.mipmap.bluetooth_icon);
    }

    private void initListener() {
        llDoctorMsg.setOnClickListener(this);
        llSearchLayout.setOnClickListener(this);
        mTvAddResident.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.llDoctorMsg:
//                if (listener != null) {
//                    listener.onTabChecked(TAB_INDEX_DOCTOR_INFO);
//                }
                showSettingDialog();
                break;
            case R.id.llSearchLayout:
                if (listener != null) {
                    listener.onTabChecked(TAB_INDEX_SEARCH);
                }
                break;
            case R.id.tv_add_resident:
                if (listener != null) {
                    listener.onTabChecked(TAB_INDEX_ADD_RESIDENT);
                }
                break;
        }
    }

    private void showSettingDialog() {
        //防止崩溃
        if (mContext!=null) {
            if (popupWindow == null)
                popupWindow = new SettingPopupWindow((MainActivity) mContext, doctor);
            popupWindow.showAsDropDown(llDoctorMsg);
        }
    }

    /**
     * 设置蓝牙状态
     *
     * @param blueStatus
     */
    public void setBlueToothStatus(int blueStatus) {
        if (blueStatus == BluetoothAdapter.STATE_ON)
            ivBlueTooth.setImageResource(R.mipmap.bluetooth_full);
        else
            ivBlueTooth.setImageResource(R.mipmap.bluetooth_null);
    }

    /**
     * 设置wifi状态
     */
    public void setWifiStatus(NetStatus netStatus) {
        if (netStatus == NetStatus.BAD)
            ivWifi.setImageResource(R.mipmap.wifi_null);
        else
            ivWifi.setImageResource(R.mipmap.wifi_full);
    }

    /**
     * 手机信号强度
     */
    public void setNetworkStatus(int netType) {
        if (netType == NetworkUtil.NETWORK_WIFI) {
            ivNetwork.setImageResource(R.mipmap.signal_null);
        } else if (netType == NetworkUtil.NETWORK_UNKNOWN) {
            ivNetwork.setImageResource(R.mipmap.signal_null);
        } else {
            ivNetwork.setImageResource(R.mipmap.signal_full);
        }

    }

    public void setOnTabChangedListener(TabChangedListener listener) {
        this.listener = listener;
    }
}
