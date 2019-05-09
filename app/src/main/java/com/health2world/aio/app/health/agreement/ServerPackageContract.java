package com.health2world.aio.app.health.agreement;

import com.health2world.aio.bean.SignService;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;

/**
 * 定义方法
 * @author Runnlin
 * @date 2018/7/26/0026.
 */

public interface ServerPackageContract {

    //回调
    interface View extends BaseView<Presenter> {
        void packageDetailSuccess(SignService detailBean);
    }

    abstract class Presenter extends BasePresenter<View> {
        public Presenter(View mView) {
            super(mView);
        }

        //获取服务包
        abstract void packageDetail(int serviceId);


    }
}
