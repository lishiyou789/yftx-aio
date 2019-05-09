package com.health2world.aio.app.setting;

import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.common.DataServer;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.config.MeasureConfig;
import com.health2world.aio.util.Logger;

import java.io.File;

import aio.health2world.utils.SPUtils;
import aio.health2world.utils.ToastUtil;

/**
 * Created by lishiyou on 2018/11/13 0013.
 */
//默认百捷三合一
public class DeviceSelectActivity extends BaseActivity {

    private RadioButton rb0, rb1, rb2;

    private TextView tvOk;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_select;
    }

    @Override
    protected void initView() {
        rb0 = findView(R.id.rb0);
        rb1 = findView(R.id.rb1);
        rb2 = findView(R.id.rb2);
        tvOk = findView(R.id.tvOk);
    }

    @Override
    protected void initData() {
        ////记录用户选择的设备 0=BeneCheck 1=EA_12 2=OGM111
        //默认EA-12选中EA-12设备
        int gluDevice = (int) SPUtils.get(this, AppConfig.DEVICE_GLU, 1);
        if (gluDevice == 0) {
            rb0.setChecked(true);
        } else if (gluDevice == 1) {
            rb1.setChecked(true);
        } else {
            rb2.setChecked(true);
        }
    }

    @Override
    protected void initListener() {
        tvOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvOk:
                if (rb0.isChecked()) {
                    SPUtils.put(this, AppConfig.DEVICE_GLU, 0);
                    SPUtils.put(this, AppConfig.DEVICE_CONFIG, MeasureConfig.Device.BENECHECK.getConfig());
                } else if (rb1.isChecked()) {
                    SPUtils.put(this, AppConfig.DEVICE_GLU, 1);
                    SPUtils.put(this, AppConfig.DEVICE_CONFIG, MeasureConfig.Device.EA12.getConfig());
                } else {
                    SPUtils.put(this, AppConfig.DEVICE_GLU, 2);
                    SPUtils.put(this, AppConfig.DEVICE_CONFIG, MeasureConfig.Device.OGM111.getConfig());
                }
                if (AppConfig.isDebug)
                    Logger.i("lsy", "DEVICE_CONFIG=" + MyApplication.getInstance().getDeviceConfig());
                ToastUtil.showLong("设置成功，请重启一体机！");
                Intent intent = new Intent();
                intent.putExtra("data", DataServer.getDeviceName());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    //删除/data/local/AppDevice.xml文件
    private void deleteFile() {
        File file = new File("/data/local/AppDevice.xml");
        if (file.exists())
            file.delete();
    }
}
