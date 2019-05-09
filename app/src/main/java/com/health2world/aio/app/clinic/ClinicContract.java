package com.health2world.aio.app.clinic;

import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.MeasureBean;

/**
 * Created by lishiyou on 2018/7/16 0016.
 */

public interface ClinicContract {

    interface View extends BaseView<Presenter> {
        //测量数据保存成功
        void uploadSuccess(String dataId);
        //测量数据保存失败
        void uploadFailed();
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        //保存测量数据
        abstract void uploadMeasureData(String dataId, String patientId,int sex, int gluType, MeasureBean dataBean);
    }

}
