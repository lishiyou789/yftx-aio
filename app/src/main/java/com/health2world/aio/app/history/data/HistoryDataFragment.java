package com.health2world.aio.app.history.data;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.health2world.aio.R;
import com.health2world.aio.app.clinic.result.CommonResultActivity;
import com.health2world.aio.app.clinic.result.EcgResultActivity;
import com.health2world.aio.bean.HistoryData;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.common.mvp.MVPBaseFragment;
import com.health2world.aio.config.MedicalConstant;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.recyclerview.DividerItemDecoration;

/**
 * 历史数据
 * Created by lishiyou on 2018/8/2 0002.
 */

public class HistoryDataFragment extends MVPBaseFragment<HistoryDataContract.Presenter> implements HistoryDataContract.View,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ResidentBean resident;

    private List<HistoryData> dataList = new ArrayList<>();

    private HistoryDataAdapter dataAdapter;

    private RecyclerView recyclerView;

    private SwipeRefreshLayout refreshLayout;

    private int pageIndex = 1;
    private int pageSize = 15;
    private String startTime = "", endTime = "";

    @Override
    protected HistoryDataContract.Presenter getPresenter() {
        return new HistoryDataPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected void initView() {
        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        refreshLayout = findView(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.appThemeColor);
    }

    @Override
    protected void initData() {
        if (getArguments().containsKey("resident"))
            resident = (ResidentBean) getArguments().getSerializable("resident");

        dataAdapter = new HistoryDataAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        dataAdapter.bindToRecyclerView(recyclerView);
        dataAdapter.setEmptyView(R.layout.layout_empty_view);
        onRefresh();
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshListener(this);
        dataAdapter.setOnItemClickListener(this);
        dataAdapter.setOnLoadMoreListener(this, recyclerView);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        HistoryData history = (HistoryData) adapter.getItem(position);
        //血脂 尿常规 白细胞 糖化血红蛋白
        if (MedicalConstant.BLOOD_FAT.equals(history.getCheckKindCode())
                || MedicalConstant.URINE.equals(history.getCheckKindCode())
                || MedicalConstant.WBC.equals(history.getCheckKindCode())
                || MedicalConstant.INFLAMMATION.equals(history.getCheckKindCode())
                || MedicalConstant.MYOCARDIUMCARDIAC.equals(history.getCheckKindCode())
                || MedicalConstant.GHB.equals(history.getCheckKindCode())) {
            List<MedicalData> list = history.getCheckDataOuts();
            if (list.size() == 0)
                return;
            Intent intent = new Intent(getActivity(), CommonResultActivity.class);
            intent.putExtra("data", (Serializable) list);
            startActivity(intent);
        }
        //心电图
        if (MedicalConstant.LEAD_ECG.equals(history.getCheckKindCode())) {
            List<MedicalData> list = history.getCheckDataOuts();
            String filePath = "";
            String result = "";
            for (MedicalData data : list) {
                if (MedicalConstant.FILE_PATH.equals(data.getCheckTypeCode()))
                    filePath = data.getValue();
                if (MedicalConstant.CHECK_RESULT.equals(data.getCheckTypeCode()))
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
    public void onRefresh() {
        pageIndex = 1;
        refreshLayout.setRefreshing(true);
        mPresenter.loadHistoryData(resident.getPatientId(), "", pageIndex, pageSize, startTime, endTime);
    }

    public void refresh(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        pageIndex = 1;
        refreshLayout.setRefreshing(true);
        mPresenter.loadHistoryData(resident.getPatientId(), "", pageIndex, pageSize, startTime, endTime);
    }

    public void resetTime() {
        startTime = "";
        endTime = "";
        onRefresh();
    }

    @Override
    public void onLoadMoreRequested() {
        pageIndex++;
        mPresenter.loadHistoryData(resident.getPatientId(), "", pageIndex, pageSize, startTime, endTime);
    }

    @Override
    public void loadHistoryDataSuccess(List<HistoryData> list) {
        refreshLayout.setRefreshing(false);
        if (pageIndex == 1)
            dataList.clear();
        dataList.addAll(list);
        dataAdapter.notifyDataSetChanged();

        if (list.size() >= pageSize) {
            dataAdapter.loadMoreComplete();
        } else {
            dataAdapter.loadMoreEnd();
        }
    }

    @Override
    public void loadHistoryDataError(Throwable e) {
        refreshLayout.setRefreshing(false);
        if (pageIndex > 1) {
            pageIndex--;
            dataAdapter.loadMoreFail();
        }
    }
}
