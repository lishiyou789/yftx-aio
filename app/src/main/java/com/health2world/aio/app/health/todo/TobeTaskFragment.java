package com.health2world.aio.app.health.todo;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.health.HealthManagerActivity;
import com.health2world.aio.app.task.TaskServiceActivity;
import com.health2world.aio.bean.TaskInfo;
import com.health2world.aio.common.mvp.MVPBaseFragment;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.util.ActivityUtil;
import com.konsung.bean.ResidentBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.ToastUtil;

/**
 * Created by lishiyou on 2018/8/6 0006.
 */

public class TobeTaskFragment extends MVPBaseFragment<TobeTaskContract.Presenter>
        implements TobeTaskContract.View, BaseQuickAdapter.OnItemChildClickListener,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    private ResidentBean resident;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private List<TaskInfo> taskBeanList = new ArrayList<>();

    private TobeTaskAdapter listAdapter;

    private int pageIndex = 1;

    private TaskInfo taskBean;

    private String patientId = "";

    @Override
    protected TobeTaskContract.Presenter getPresenter() {
        return new TobeTaskPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler_view;
    }

    @Override
    protected void initView() {
        refreshLayout = findView(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setBackgroundColor(mContext.getResources().getColor(R.color.gray_light));
        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    protected void initData() {
        resident = (ResidentBean) getArguments().getSerializable("resident");

        listAdapter = new TobeTaskAdapter(taskBeanList);
        recyclerView.setAdapter(listAdapter);
        listAdapter.bindToRecyclerView(recyclerView);
        listAdapter.setEmptyView(R.layout.layout_empty_view);

        //加载该居民的待办任务
        onRefresh();
    }

    @Override
    protected void initListener() {
        listAdapter.setOnItemChildClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        listAdapter.setOnLoadMoreListener(this, recyclerView);
    }

    @Override
    public void onRefresh() {
        ((HealthManagerActivity) getActivity()).setLoading(true);
        refreshLayout.setRefreshing(true);
        pageIndex = 1;
        mPresenter.loadTobeTask(resident, pageIndex);
    }

    @Override
    public void onLoadMoreRequested() {
        ((HealthManagerActivity) getActivity()).setLoading(true);
        pageIndex++;
        mPresenter.loadTobeTask(resident, pageIndex);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        //任务处理操作成功 刷新界面
        if (requestCode == 0x60)
            onRefresh();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        taskBean = (TaskInfo) adapter.getItem(position);
        mPresenter.getPatientInfoById(String.valueOf(resident.getPatientId()));
    }

    @Override
    public void loadTobeTaskFailure() {
        ((HealthManagerActivity) getActivity()).setLoading(false);
        refreshLayout.setRefreshing(false);
        if (pageIndex > 1) {
            pageIndex--;
            listAdapter.loadMoreFail();
        }

    }

    @Override
    public void loadTobeTaskSuccess(List<TaskInfo> list) {
        ((HealthManagerActivity) getActivity()).setLoading(false);
        refreshLayout.setRefreshing(false);
        if (pageIndex == 1)
            taskBeanList.clear();
        taskBeanList.addAll(list);
        listAdapter.notifyDataSetChanged();

        if (list.size() >= AppConfig.PAGE_SIZE)
            listAdapter.loadMoreComplete();
        else
            listAdapter.loadMoreEnd();
    }

    @Override
    public void loadPatientInfoSuccess(ResidentBean resident) {
        //公卫待建档任务
        if (taskBean.getTaskType().equals("7")) {
            if (TextUtils.isEmpty(resident.getIdentityCard())) {
                ToastUtil.showShort(getString(R.string.identity_card_error));
                return;
            }
            patientId = resident.getPatientId();
            MyApplication.getInstance().setCurrentIdentityCard(resident.getIdentityCard());
            try {
                DBManager.getInstance().getResidentDao().createOrUpdate(resident);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ActivityUtil.enterPublicHealth(mActivity, 0, 0x08);
        } else {
            //其他任务
            Intent intent = new Intent(getActivity(), TaskServiceActivity.class);
            taskBean.setPatientId(resident.getPatientId());
            intent.putExtra("taskBean", taskBean);
            intent.putExtra("resident", resident);
            startActivityForResult(intent, 0x60);
        }
    }

    public void donePublicHealthTask() {
        if (TextUtils.isEmpty(patientId))
            return;
        mPresenter.donePublicHealthTask(patientId);
    }

    @Override
    public void donePublicHealthStatus(boolean success) {
        patientId = "";
        if (success)
            onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }
}
