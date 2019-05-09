package com.health2world.aio.app.history.service;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.health2world.aio.R;
import com.health2world.aio.app.clinic.result.CommonResultActivity;
import com.health2world.aio.app.clinic.result.EcgResultActivity;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.bean.ServiceLog;
import com.health2world.aio.common.mvp.MVPBaseFragment;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.config.MedicalConstant;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;

/**
 * 履约记录
 * Created by lishiyou on 2018/8/2 0002.
 */

public class HistoryServiceFragment extends MVPBaseFragment<HistoryServiceContract.Presenter>
        implements HistoryServiceContract.View, SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

    private ResidentBean resident;
    private int pageIndex = 1;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<ServiceLog> recordList = new ArrayList<>();
    private HistoryServiceAdapter serviceAdapter;

    private String startTime = "", endTime = "";

    @Override
    protected HistoryServiceContract.Presenter getPresenter() {
        return new HistoryServicePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler_view;
    }

    @Override
    protected void initView() {
        refreshLayout = findView(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        recyclerView = findView(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {
        if (getArguments().containsKey("resident"))
            resident = (ResidentBean) getArguments().getSerializable("resident");

        serviceAdapter = new HistoryServiceAdapter(recordList);
        recyclerView.setAdapter(serviceAdapter);

        serviceAdapter.bindToRecyclerView(recyclerView);
        serviceAdapter.setEmptyView(R.layout.layout_empty_view);

        //加载服务记录数据
        onRefresh();

    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshListener(this);
        serviceAdapter.setOnItemChildClickListener(this);
        serviceAdapter.setOnLoadMoreListener(this, recyclerView);
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        refreshLayout.setRefreshing(true);
        mPresenter.loadPerformanceRecord(pageIndex, resident.getPatientId(), startTime, endTime);
    }

    public void refresh(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        pageIndex = 1;
        refreshLayout.setRefreshing(true);
        mPresenter.loadPerformanceRecord(pageIndex, resident.getPatientId(), startTime, endTime);
    }

    public void resetTime() {
        startTime = "";
        endTime = "";
        onRefresh();
    }

    @Override
    public void onLoadMoreRequested() {
        pageIndex++;
        mPresenter.loadPerformanceRecord(pageIndex, resident.getPatientId(), startTime, endTime);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ServiceLog log = (ServiceLog) adapter.getItem(position);

        List<MedicalData> list = log.getCheckDataRecordMap();

        List<MedicalData> listEcg = new ArrayList<>();
        List<MedicalData> listData = new ArrayList<>();

        for (MedicalData data : list) {
            if (data.getCheckKindCode().equals(MedicalConstant.LEAD_ECG))
                listEcg.add(data);
            else
                listData.add(data);
        }

        //查看测量数据
        if (view.getId() == R.id.tvResult) {
            Intent intent = new Intent(getActivity(), CommonResultActivity.class);
            intent.putExtra("data", (Serializable) listData);
            startActivity(intent);
        }
        //查看心电数据
        if (view.getId() == R.id.tvEcg) {
            String filePath = "";
            String result = "";
            for (MedicalData data : listEcg) {
                if (data.getCheckTypeCode().equals(MedicalConstant.FILE_PATH))
                    filePath = data.getValue();
                if (data.getCheckTypeCode().equals(MedicalConstant.CHECK_RESULT))
                    result = data.getValue();
            }
            MeasureBean bean = new MeasureBean();
            bean.setFilePath(filePath);
            bean.setAnal(result);
            Intent intent = new Intent(mContext, EcgResultActivity.class);
            intent.putExtra("measureBean", bean);
            intent.putExtra("resident", resident);
            startActivity(intent);
        }

    }

    @Override
    public void loadPerformanceRecordSuccess(List<ServiceLog> list) {
        refreshLayout.setRefreshing(false);
        if (pageIndex == 1)
            recordList.clear();
        recordList.addAll(list);
        serviceAdapter.notifyDataSetChanged();

        if (list.size() >= AppConfig.PAGE_SIZE) {
            serviceAdapter.loadMoreComplete();
        } else {
            serviceAdapter.loadMoreEnd();
        }
    }

    @Override
    public void loadServiceRecordError() {
        refreshLayout.setRefreshing(false);
        if (pageIndex > 1) {
            pageIndex--;
            serviceAdapter.loadMoreFail();
        }
    }
}
