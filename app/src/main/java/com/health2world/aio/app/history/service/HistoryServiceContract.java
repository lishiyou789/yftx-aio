package com.health2world.aio.app.history.service;

import com.health2world.aio.bean.ServiceLog;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;

import java.util.List;

/**
 * Created by lishiyou on 2018/8/11 0011.
 */

interface HistoryServiceContract {


    interface View extends BaseView<Presenter> {

        void loadPerformanceRecordSuccess(List<ServiceLog> list);

        void loadServiceRecordError();
    }


    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }


        abstract void loadPerformanceRecord(int pageIndex, String patientId,String startTime,String endTime);
    }

}
