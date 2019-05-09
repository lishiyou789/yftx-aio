package com.health2world.aio.app.history.chart;

import com.health2world.aio.bean.HistoryData;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;

import java.util.List;

/**
 * Created by lishiyou on 2018/9/4 0004.
 */

public interface ChartContract {

    interface View extends BaseView<Presenter> {

        void loadChartDataError();

        void loadChartDataSuccess(List<HistoryData> dataList);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        //加载图表数据
        abstract void loadChartData(String patientId, String dataType, String startTime, String endTime);
    }
}
