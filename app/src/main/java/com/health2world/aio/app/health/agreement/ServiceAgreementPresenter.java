package com.health2world.aio.app.health.agreement;

import com.health2world.aio.bean.AgreementBean;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.ResultSubscriber;

import java.util.List;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.ToastUtil;

/**
 * Created by Administrator on 2018/7/31/0031.
 */

public class ServiceAgreementPresenter extends ServiceAgreementContract.Presenter {


    public ServiceAgreementPresenter(ServiceAgreementContract.View mView) {
        super(mView);
    }

    @Override
    void serviceAgreement(String patientId) {
        ApiRequest.getServiceAgreement(patientId, new ResultSubscriber<List<AgreementBean>>() {

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (mView != null)
                    mView.hideLoading();
            }

            @Override
            public void onStart() {
                super.onStart();
                if (mView != null)
                    mView.showLoading();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (mView != null)
                    mView.hideLoading();
            }

            @Override
            public void onNext(HttpResult<List<AgreementBean>> t) {
                super.onNext(t);
                if (t.code.equals(AppConfig.SUCCESS)) {
                    List<AgreementBean> data = t.data;
                    if (mView != null)
                        mView.serviceAgreementSuccess(data);
                } else {
                    ToastUtil.showLong(t.errorMessage);
                }
            }
        });
    }


}
