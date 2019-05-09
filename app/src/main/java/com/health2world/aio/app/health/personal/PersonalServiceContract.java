package com.health2world.aio.app.health.personal;

import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;

/**
 * Created by lishiyou on 2018/8/6 0006.
 */

interface PersonalServiceContract {

    interface View extends BaseView<Presenter> {

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }
    }

}
