package com.health2world.aio.app.health.agreement;

import com.health2world.aio.bean.AgreementBean;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2018/7/31/0031.
 */

public interface ServiceAgreementContract {

    interface View extends BaseView<Presenter> {
        void serviceAgreementSuccess(List<AgreementBean> agreementBean);
    }

    abstract class Presenter extends BasePresenter<View> {
        public Presenter(View mView) {
            super(mView);
        }

        //获取服务协议
        abstract void serviceAgreement(String serviceId);
    }
}
