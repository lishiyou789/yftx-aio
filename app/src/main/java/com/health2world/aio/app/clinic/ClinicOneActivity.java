package com.health2world.aio.app.clinic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.app.home.RMessageView;
import com.health2world.aio.app.home.TabChangedListener;
import com.health2world.aio.app.search.RSearchActivity;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.util.TitleBarUtil;
import com.health2world.aio.view.TitleBar;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

/**
 * 独立门诊检查
 *
 * @author Runnlin
 * @date 2018/12/5/0005.
 */

public class ClinicOneActivity extends BaseActivity implements TabChangedListener {

    FragmentManager fragmentManager;

    RMessageView residentView;
    TitleBar titleBar;

    ResidentBean resident;

    //去添加居民
    int searchResidentResult = 1;

    //1 正常进入门诊检查  0 履约测量
    int openFrom = 1;
    //    //是否为履约测量
//    int mFlag = 0;
    private ClinicOneFragment _clinicOneFragment;

    //    private Measure measure;
//
    private MeasureBean bean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clinic;
    }

    @Override
    protected void initView() {

        titleBar = findView(R.id.titleBar);
        TitleBarUtil.setAttr(this, "门诊检查", "", titleBar);

        residentView = findView(R.id.rmessage_view);
        residentView.getHistory().setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        setResult(RESULT_CANCELED);
        if (getIntent().hasExtra(MainActivity.KEY_RESIDENT)) {
            resident = (ResidentBean) getIntent().getSerializableExtra(MainActivity.KEY_RESIDENT);
        } else {//如果打开门诊测量时没有带入居民，就从全局中拿
            resident = MyApplication.getInstance().getResident();
        }
        if (resident != null)
            residentView.setResident(resident);

        if (getIntent().hasExtra("bean")) {
            bean = (MeasureBean) getIntent().getSerializableExtra("bean");
        }
        openFrom = getIntent().getIntExtra("openFrom", 1);

        fragmentManager = getSupportFragmentManager();
        _clinicOneFragment = new ClinicOneFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        bundle.putInt("openFrom", openFrom);
        _clinicOneFragment.setArguments(bundle);
        fragmentManager.beginTransaction().add(R.id.container, _clinicOneFragment).commit();
        //扫描蓝牙
        MyApplication.getInstance().scanDevice();
    }

    @Override
    protected void initListener() {
        residentView.setOnTabChangedListener(this);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_clinicOneFragment.hasMeasured()) {
                    showTips();
                } else {
                    if (openFrom == 0) {
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(MainActivity.KEY_RESIDENT, resident);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (_clinicOneFragment.hasMeasured()) {
            showTips();
        } else {
            super.onBackPressed();
        }
    }

    private void showTips() {
        new AlertDialog.Builder(ClinicOneActivity.this)
                .setTitle("当前的测量数据尚未保存")
                .setMessage("不保存数据将舍弃当前测量数据")
                .setPositiveButton("  不保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApplication.getInstance().setDataList(null);
                        finish();
                    }
                })
                .setNegativeButton("按错了  ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    public ResidentBean getResident() {
        return resident;
    }


    @Override
    public void onTabChecked(int position) {
        if (resident == null)
            if (position == RMessageView.TAB_INDEX_FAMILY) {
                Intent searchResident = new Intent(this, RSearchActivity.class);
                searchResident.putExtra("flag", 1);
                startActivityForResult(searchResident, searchResidentResult);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //从搜索居民回来
        if (requestCode == searchResidentResult && resultCode == RESULT_OK) {
            resident = (ResidentBean) data.getSerializableExtra(MainActivity.KEY_RESIDENT);
            residentView.setResident(resident);
            //切换全局居民
            MyApplication.getInstance().setResident(resident);
            if (_clinicOneFragment != null)
                _clinicOneFragment.refreshTable();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().setDataList(null);
    }

    public void resetResident() {
        resident = null;
        residentView.setResident(null);

    }
}
