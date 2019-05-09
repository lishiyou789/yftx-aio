package com.health2world.aio.app.history.chart;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.health2world.aio.R;
import com.health2world.aio.app.clinic.result.EcgResultActivity;
import com.health2world.aio.app.history.data.HistoryDataAdapter;
import com.health2world.aio.bean.HistoryAccount;
import com.health2world.aio.bean.HistoryData;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.common.mvp.MVPBaseFragment;
import com.health2world.aio.config.MedicalConstant;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.recyclerview.DividerItemDecoration;

/**
 * Created by lishiyou on 2018/9/21 0021.
 */

public class EcgChartFragment extends MVPBaseFragment<ChartContract.Presenter> implements ChartContract.View,
        BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;

    private ResidentBean resident;

    private List<HistoryData> measureList = new ArrayList<>();

    private ChartDataAdapter listAdapter;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected ChartContract.Presenter getPresenter() {
        return new ChartPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected void initView() {
        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        refreshLayout = findView(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.appThemeColor);
    }

    @Override
    protected void initData() {
        if (getArguments().containsKey("resident"))
            resident = (ResidentBean) getArguments().getSerializable("resident");

        listAdapter = new ChartDataAdapter(measureList);
        recyclerView.setAdapter(listAdapter);
        listAdapter.bindToRecyclerView(recyclerView);
        listAdapter.setEmptyView(R.layout.layout_empty_view);

        onRefresh();
    }

    @Override
    protected void initListener() {
        listAdapter.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        mPresenter.loadChartData(resident.getPatientId(), MedicalConstant.LEAD_ECG, "", "");
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        HistoryData history = (HistoryData) adapter.getItem(position);
        //心电图
        if (history.getCheckKindCode().equals(MedicalConstant.LEAD_ECG)) {
            List<MedicalData> list = history.getCheckDataOuts();
            String filePath = "";
            String result = "";
            for (MedicalData data : list) {
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
    public void loadChartDataSuccess(List<HistoryData> dataList) {
        refreshLayout.setRefreshing(false);
        measureList.clear();
        measureList.addAll(dataList);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadChartDataError() {
        refreshLayout.setRefreshing(false);
    }
}
