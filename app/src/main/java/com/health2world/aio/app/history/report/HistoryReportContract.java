package com.health2world.aio.app.history.report;

import com.health2world.aio.bean.HealthReport;
import com.health2world.aio.bean.MedicalReport;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;

import java.util.List;

/**
 * Created by lishiyou on 2018/8/11 0011.
 */

interface HistoryReportContract {

    interface View extends BaseView<Presenter> {

         void loadHealthReportSuccess(List<MedicalReport> reportList);

         void loadHealthReportError(Throwable throwable);
    }


    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        abstract void loadHealthReport(String patientId,String startTime,String endTime,int pageIndex);
    }

}
