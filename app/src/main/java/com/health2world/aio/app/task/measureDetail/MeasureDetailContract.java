package com.health2world.aio.app.task.measureDetail;

import com.health2world.aio.bean.IndexData;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

/**
 * @author Administrator
 * @date 2019/1/5/0005.
 */

public interface MeasureDetailContract {
    interface View extends BaseView<Presenter> {

        void loadSuccess(IndexData data);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        abstract void getMeasureDetail(ResidentBean resident, String data, String checkName, String measurementProject, String gluStyle);
    }
}
