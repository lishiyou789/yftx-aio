package com.health2world.aio.app.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.login.LoginActivity;
import com.health2world.aio.app.setting.SettingActivity;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.health2world.aio.receiver.Health2WorldReceiver;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.AppManager;
import aio.health2world.utils.SPUtils;

/**
 * Created by lishiyou on 2018/8/1 0001.
 */

public class SettingPopupWindow extends PopupWindow implements View.OnClickListener {

    private Activity activity;

    private DoctorBean doctor;

    private View contentView;

    private LinearLayout llCodeLayout, llSettingLayout, llLoginOutLayout;


    public SettingPopupWindow(Activity activity, DoctorBean doctor) {
        this.activity = activity;
        this.doctor = doctor;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.layout_setting_popupwindow, null);
        this.setContentView(contentView);
        int h = activity.getWindowManager().getDefaultDisplay().getHeight();
        int w = activity.getWindowManager().getDefaultDisplay().getWidth();
        this.setWidth(w / 4);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(77666666);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        initView();
    }

    private void initView() {
        llCodeLayout = contentView.findViewById(R.id.llCodeLayout);
        llSettingLayout = contentView.findViewById(R.id.llSettingLayout);
        llLoginOutLayout = contentView.findViewById(R.id.llLoginOutLayout);

        llCodeLayout.setOnClickListener(this);
        llSettingLayout.setOnClickListener(this);
        llLoginOutLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCodeLayout:
                Intent intent = new Intent(Health2WorldReceiver.ACTION_SHOW_QR_DIALOG);
                activity.sendBroadcast(intent);
                dismiss();
                break;
            case R.id.llSettingLayout:
                dismiss();
                activity.startActivity(new Intent(activity, SettingActivity.class));
                break;
            case R.id.llLoginOutLayout:
                dismiss();
                MyApplication.getInstance().logout();
                ApiRequest.logout(doctor.getAccount(), new HttpSubscriber() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(HttpResult result) {
                        super.onNext(result);
                    }
                });
                break;
        }
    }
}
