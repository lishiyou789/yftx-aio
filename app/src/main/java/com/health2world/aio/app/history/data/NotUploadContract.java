package com.health2world.aio.app.history.data;

import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.MeasureBean;

/**
 * Created by lishiyou on 2019/2/19 0019.
 */

public interface NotUploadContract {

    interface View extends BaseView<Presenter> {

        void uploadSuccess(int position);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        abstract void uploadMeasureData(int position, MeasureBean measure);

    }

}
