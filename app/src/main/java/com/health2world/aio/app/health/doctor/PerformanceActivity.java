package com.health2world.aio.app.health.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.health2world.aio.R;
import com.health2world.aio.app.clinic.ClinicOneActivity;
import com.health2world.aio.app.clinic.ClinicOneFragment;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.bean.ServiceItem;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.util.TitleBarUtil;
import com.health2world.aio.view.TitleBar;
import com.konsung.bean.ResidentBean;

import static com.health2world.aio.app.home.MainActivity.KEY_RESIDENT;

/**
 * 履约测量类
 * Created by lishiyou on 2018/9/29 0029.
 */

public class PerformanceActivity extends BaseActivity {

    private TitleBar titleBar;

    private ServiceItem serviceItem;

    private ResidentBean resident;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_performance;
    }

    @Override
    protected void initView() {
        titleBar = findView(R.id.titleBar);
        TitleBarUtil.setAttr(this, "履约测量", "", titleBar);
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("serviceItem"))
            serviceItem = (ServiceItem) getIntent().getSerializableExtra("serviceItem");
        if (getIntent().hasExtra("resident"))
            resident = (ResidentBean) getIntent().getSerializableExtra("resident");

        if (serviceItem == null || resident == null)
            return;

        ClinicOneFragment mClinicOneFragment = new ClinicOneFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.KEY_RESIDENT, resident);
        mClinicOneFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, mClinicOneFragment).commit();
    }

    @Override
    protected void initListener() {
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
