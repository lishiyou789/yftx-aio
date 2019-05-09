package com.health2world.aio.app.health;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.health2world.aio.R;
import com.health2world.aio.app.adapter.HealthFamilyMemberAdapter;
import com.health2world.aio.app.health.agreement.ServiceAgreementActivity;
import com.health2world.aio.app.health.doctor.FamilyDoctorFragment;
import com.health2world.aio.app.health.doctor.HealthGuideActivity;
import com.health2world.aio.app.health.personal.PersonalServiceFragment;
import com.health2world.aio.app.health.termination.TerminationActivity;
import com.health2world.aio.app.health.todo.TobeTaskFragment;
import com.health2world.aio.app.history.HistoryRecordActivity;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.util.Logger;
import com.health2world.aio.util.TitleBarUtil;
import com.health2world.aio.view.TitleBar;
import com.konsung.bean.ResidentBean;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.flycotablayout.SegmentTabLayout;
import aio.health2world.flycotablayout.listener.OnTabSelectListener;
import aio.health2world.utils.ToastUtil;

import static com.health2world.aio.app.health.doctor.FamilyDoctorFragment.serviceItem;

/**
 * 健康管理页面
 * Created by lishiyou on 2018/8/6 0006.
 */

public class HealthManagerActivity extends MVPBaseActivity<HealthManagerContract.Presenter>
        implements HealthManagerContract.View, BaseQuickAdapter.OnItemClickListener, PopupMenu.OnMenuItemClickListener {

    public static final int REQUEST_CODE_EXECUTE = 0x01;
    public static final int REQUEST_CODE_MEASURE = 0x02;
    public static final int REQUEST_CODE_SERVICE = 0x03;
    public static final int REQUEST_CODE_TERMINATION = 0x04;

    public static String[] mTitles = {"待办任务", "家庭医生", "个性服务"};

    private TitleBar titleBar;
    private SegmentTabLayout tabLayout;
    private RecyclerView recyclerView;

    private ResidentBean resident;
    private List<ResidentBean> residentList;
    private HealthFamilyMemberAdapter memberAdapter;
    private int mPosition = 0;

    private FragmentManager fragmentManager;
    private TobeTaskFragment taskFragment;//待办任务
    private FamilyDoctorFragment doctorFragment;//家庭医生
    private PersonalServiceFragment serviceFragment;//个性化服务

    //数据加载中 不支持切换操作
    private boolean isLoading = false;

    public List<ResidentBean> getResidentList() {
        return residentList;
    }

    public synchronized void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    protected HealthManagerContract.Presenter getPresenter() {
        return new HealthManagerPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_health_manager;
    }

    @Override
    protected void initView() {
        titleBar = findView(R.id.titleBar);
        tabLayout = findView(R.id.tabLayout);
        recyclerView = findView(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {
        resident = (ResidentBean) getIntent().getSerializableExtra(MainActivity.KEY_RESIDENT);
        TitleBarUtil.setAttr(this, getString(R.string.health_manager), "", titleBar);
        fragmentManager = getSupportFragmentManager();

        residentList = new ArrayList<>();
        memberAdapter = new HealthFamilyMemberAdapter(residentList);
        recyclerView.setAdapter(memberAdapter);

        //加载家庭成员列表数据
        mPresenter.loadFamilyMember(resident.getPatientId());
    }

    private void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.health_management_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.setGravity(Gravity.START);
        popup.show();
    }

    @Override
    protected void initListener() {
        memberAdapter.setOnItemClickListener(this);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBar.addAction(new TitleBar.TextAction("更多") {
            @Override
            public void performAction(View view) {
                showPopup(view);
            }
        });

        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                changeFragment(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void changeFragment(int position) {
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();
        FragmentTransaction fr = fragmentManager.beginTransaction();
        hideAllFragments(fr);
        switch (position) {
            case 0:
                if (taskFragment == null) {
                    taskFragment = new TobeTaskFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resident", resident);
                    taskFragment.setArguments(bundle);
                    fr.add(R.id.container, taskFragment, resident.getPatientId());
                } else {
                    fr.show(taskFragment);
                }
                break;
            case 1:
                if (doctorFragment == null) {
                    doctorFragment = new FamilyDoctorFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resident", resident);
                    doctorFragment.setArguments(bundle);
                    fr.add(R.id.container, doctorFragment, resident.getPatientId());
                } else {
                    fr.show(doctorFragment);
                }
                break;
            case 2:
                if (serviceFragment == null) {
                    serviceFragment = new PersonalServiceFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resident", resident);
                    serviceFragment.setArguments(bundle);
                    fr.add(R.id.container, serviceFragment, resident.getPatientId());
                } else {
                    fr.show(serviceFragment);
                }
                break;
        }
        fr.commitAllowingStateLoss();
    }

    private void hideAllFragments(FragmentTransaction fr) {
        if (taskFragment != null)
            fr.hide(taskFragment);
        if (serviceFragment != null)
            fr.hide(serviceFragment);
        if (doctorFragment != null)
            fr.hide(doctorFragment);
    }


    private void resetAllFragment() {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (taskFragment != null) {
            transaction.remove(taskFragment);
            taskFragment = null;
        }
        if (serviceFragment != null) {
            transaction.remove(serviceFragment);
            serviceFragment = null;
        }
        if (doctorFragment != null) {
            transaction.remove(doctorFragment);
            doctorFragment = null;
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void loadFamilyMemberSuccess(List<ResidentBean> list) {
        if (list.size() > 0) {
            residentList.addAll(list);
            residentList.get(0).setCheck(true);
            memberAdapter.notifyDataSetChanged();
            initTab();
        }
    }

    private void initTab() {
        tabLayout.setTabData(mTitles);
        changeFragment(0);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (isLoading) {
            ToastUtil.showShort("数据加载中 不支持操作");
            return;
        }
        if (mPosition != position) {
            residentList.get(mPosition).setCheck(false);
            residentList.get(position).setCheck(true);
            memberAdapter.notifyItemChanged(mPosition);
            memberAdapter.notifyItemChanged(position);
            mPosition = position;
            resident = (ResidentBean) adapter.getItem(position);
            resetAllFragment();
            changeFragment(0);
            tabLayout.setCurrentTab(0);
        }
    }

    /* 更多菜单 */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_service_agreement:
                Intent agreeIntent = new Intent(this, ServiceAgreementActivity.class);
                agreeIntent.putExtra(MainActivity.KEY_RESIDENT, resident);
                startActivity(agreeIntent);
                break;
            case R.id.btn_service_record:
                Intent intentRecord = new Intent(this, HistoryRecordActivity.class);
                intentRecord.putExtra(MainActivity.KEY_RESIDENT, resident);
                intentRecord.putExtra("index", 2);
                startActivity(intentRecord);
                break;
            case R.id.btn_unsign:
                Intent unSignIntent = new Intent(this, TerminationActivity.class);
                unSignIntent.putExtra(MainActivity.KEY_RESIDENT, resident);
                startActivityForResult(unSignIntent, REQUEST_CODE_TERMINATION);
                break;
        }
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //如果当前页面处于待办任务 并且进入公卫完善健康档案
        if (taskFragment != null && taskFragment.isVisible()) {
            taskFragment.donePublicHealthTask();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == REQUEST_CODE_TERMINATION) {
            if (doctorFragment != null)
                doctorFragment.onRefresh();
        }

        //85机型限定：从履约测量页面回来
        if (requestCode == REQUEST_CODE_EXECUTE) {
            FamilyDoctorFragment.dataId = data.getStringExtra("dataId");
            Intent intent = new Intent(mContext, HealthGuideActivity.class);
            intent.putExtra("serviceItem", serviceItem);
            startActivityForResult(intent, REQUEST_CODE_MEASURE);
        }

        //85机型限定：从履约测量页面回来
        //履约测量后填写医嘱回来
        if (requestCode == REQUEST_CODE_MEASURE || requestCode == REQUEST_CODE_SERVICE) {
            FamilyDoctorFragment.advise = data.getStringExtra("advise");
            doctorFragment.addServiceRecord();
        }


//        if (requestCode == PerformanceManager.REQUEST_CODE_SP02 ||
//                requestCode == PerformanceManager.REQUEST_CODE_ECG) {
//            if (doctorFragment != null)
//                doctorFragment.onActivityResult(requestCode, resultCode, data);
//        }

    }
}
