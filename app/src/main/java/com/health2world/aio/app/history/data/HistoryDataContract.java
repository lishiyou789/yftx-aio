package com.health2world.aio.app.history.data;

import com.health2world.aio.bean.HistoryData;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;

import java.util.List;

/**
 * Created by lishiyou on 2018/8/3 0003.
 */

interface HistoryDataContract {

    interface View extends BaseView<Presenter> {

        void loadHistoryDataSuccess(List<HistoryData> list);

        void loadHistoryDataError(Throwable e);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }


        abstract void loadHistoryData(String patientId, String dataType, int pageIndex,int pageSize,String startTime, String endTime);
    }

}
