package com.health2world.aio.app.task;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.home.MainActivity;
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
 * Created by Administrator on 2018/7/6 0006.
 */

public class TaskFragment extends MVPBaseFragment<TaskContract.Presenter> implements TaskContract.View
        , RadioGroup.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    private TaskListAdapter listAdapter;
    private List<TaskInfo> beanList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private RadioGroup rbGroup;
    private int pageIndex = 1;//默认加载第一页
    private String taskType = "";//默认显示全部任务
    private String patientId = "";
    private TaskInfo taskBean;

    @Override
    protected TaskContract.Presenter getPresenter() {
        return new TaskPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    public void initView() {
        recyclerView = findView(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshLayout = findView(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        rbGroup = findView(R.id.rbGroup);
    }

    @Override
    protected void initData() {
        ((RadioButton) rbGroup.getChildAt(0)).setChecked(true);
        beanList = new ArrayList<>();
        listAdapter = new TaskListAdapter(beanList);
        recyclerView.setAdapter(listAdapter);

        listAdapter.bindToRecyclerView(recyclerView);
        listAdapter.setEmptyView(R.layout.layout_empty_view);
        //获取待办
        refreshLayout.setRefreshing(true);
        mPresenter.loadTaskData(taskType, pageIndex);
    }

    @Override
    protected void initListener() {
        rbGroup.setOnCheckedChangeListener(this);
        listAdapter.setOnLoadMoreListener(this, recyclerView);
        listAdapter.setOnItemChildClickListener(this);
        listAdapter.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        refreshLayout.setRefreshing(true);
        mPresenter.loadTaskData(taskType, pageIndex);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.rbAll) {
            if (!taskType.equals("")) {
                taskType = "";
                onRefresh();
            }
        } else if (checkedId == R.id.rbExtract) {
            if (!taskType.equals("1,2")) {
                taskType = "1,2";
                onRefresh();
            }
        } else if (checkedId == R.id.rbFile) {
            if (!taskType.equals("7")) {
                taskType = "7";
                onRefresh();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        //任务处理操作成功 刷新界面
        if (requestCode == 0x07)
            onRefresh();
    }

    @Override
    public void onLoadMoreRequested() {
        pageIndex++;
        mPresenter.loadTaskData(taskType, pageIndex);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        taskBean = (TaskInfo) adapter.getItem(position);
        mPresenter.getPatientInfoById(taskBean.getPatientId());
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        onItemChildClick(adapter, view, position);
    }

    @Override
    public void loadPatientInfoSuccess(ResidentBean resident) {
        ((MainActivity) getActivity()).setResident(true,resident);
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
            Intent intent = new Intent(mContext, TaskServiceActivity.class);
            intent.putExtra("taskBean", taskBean);
            intent.putExtra("resident", resident);
            startActivityForResult(intent, 0x07);
        }
    }

    @Override
    public void loadSuccess(List<TaskInfo> list) {
        refreshLayout.setRefreshing(false);

        if (pageIndex == 1)
            beanList.clear();
        beanList.addAll(list);
        listAdapter.notifyDataSetChanged();

        if (list.size() >= AppConfig.PAGE_SIZE)
            listAdapter.loadMoreComplete();
        else
            listAdapter.loadMoreEnd();
    }

    @Override
    public void loadFailed() {
        refreshLayout.setRefreshing(false);
        if (pageIndex > 1)
            listAdapter.loadMoreFail();
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
}

