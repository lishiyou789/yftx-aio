package com.health2world.aio.app.history.chart;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.health2world.aio.R;
import com.health2world.aio.app.history.data.HistoryDataAdapter;
import com.health2world.aio.bean.HistoryData;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.common.mvp.MVPBaseFragment;
import com.health2world.aio.config.MedicalConstant;
import com.health2world.aio.config.NormalRange;
import com.konsung.bean.ResidentBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aio.health2world.utils.DateUtil;

/**
 * Created by lishiyou on 2018/8/2 0002.
 */

public class NibpChartFragment extends MVPBaseFragment<ChartContract.Presenter> implements ChartContract.View {

    private ResidentBean resident;

    private String startTime = "", endTime = "";

    private LineChart lineChart;

    private RecyclerView recyclerView;

    private ChartDataAdapter dataAdapter;

    private List<HistoryData> dataList = new ArrayList<>();

    private boolean success = false;

    public boolean isSuccess() {
        return success;
    }

    @Override
    protected ChartContract.Presenter getPresenter() {
        return new ChartPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_linechart;
    }

    @Override
    protected void initView() {
        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        lineChart = findView(R.id.lineChart);
        lineChart.setNoDataText("暂无图表数据");
        lineChart.getDescription().setText("血压趋势图");
        //设置是否可以缩放 x和y，默认true
        lineChart.setScaleXEnabled(true);
        lineChart.setScaleYEnabled(false);
    }

    @Override
    protected void initData() {
        if (getArguments().containsKey("resident"))
            resident = (ResidentBean) getArguments().getSerializable("resident");
        dataAdapter = new ChartDataAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);
        dataAdapter.bindToRecyclerView(recyclerView);
        dataAdapter.setEmptyView(R.layout.layout_empty_view);
        loadData();
    }

    public void loadData() {
        mPresenter.loadChartData(resident.getPatientId(), MedicalConstant.NIBP, startTime, endTime);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void loadChartDataSuccess(List<HistoryData> dataList) {
        success = true;
        parseData(dataList);
    }

    @Override
    public void loadChartDataError() {
        success = false;
    }

    //列表视图和图表视图的切换 0 图表  1 列表
    public void changeView(int type) {
        if (type == 0) {
            lineChart.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            lineChart.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void parseData(List<HistoryData> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        dataAdapter.notifyDataSetChanged();
        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Entry> yValues1 = new ArrayList<>();//sbp
        ArrayList<Entry> yValues2 = new ArrayList<>();//dbp
        ArrayList<Integer> listColors1 = new ArrayList<>();
        ArrayList<Integer> listColors2 = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            HistoryData history = dataList.get(i);
            String time = DateUtil.getDate1(new Date(history.getTimestamp()));
            xValues.add(time);
            List<MedicalData> list = history.getCheckDataOuts();
            for (MedicalData data : list) {
                int value = Integer.valueOf(data.getValue());
                if (data.getCheckTypeCode().equals(MedicalConstant.SBP)) {
                    yValues1.add(new Entry(i, value));
                    if (value > NormalRange.NIBP_SBP_MAX) {
                        listColors1.add(Color.parseColor("#FF0000"));
                    } else if (value < NormalRange.NIBP_SBP_MIN) {
                        listColors1.add(Color.parseColor("#FF982D"));
                    } else {
                        listColors1.add(Color.parseColor("#739FF8"));
                    }
                }
                if (data.getCheckTypeCode().equals(MedicalConstant.DBP)) {
                    yValues2.add(new Entry(i, value));
                    if (value > NormalRange.NIBP_DBP_MAX) {
                        listColors2.add(Color.parseColor("#FF0000"));
                    } else if (value < NormalRange.NIBP_DBP_MIN) {
                        listColors2.add(Color.parseColor("#FF982D"));
                    } else {
                        listColors2.add(Color.parseColor("#6CBA4C"));
                    }
                }
            }
        }
        if (xValues.size() > 0)
            NibpChartManager.initLineChart(getActivity(), lineChart, xValues, yValues1, yValues2, listColors1, listColors2);
    }
}
