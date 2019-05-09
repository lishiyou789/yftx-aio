package com.health2world.aio.app.health.termination;

import com.health2world.aio.bean.SignService;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;

import java.util.List;

/**
 * Created by lishiyou on 2018/8/20 0020.
 */

public interface TerminationContract {

    interface View extends BaseView<Presenter> {

        void loadServicePackageSuccess(List<SignService> list);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        abstract void loadServicePackage(String patientId);

    }

}
