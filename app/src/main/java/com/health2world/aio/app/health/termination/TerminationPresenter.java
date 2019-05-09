package com.health2world.aio.app.health.termination;

import com.health2world.aio.bean.SignService;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.ResultSubscriber;

import java.util.List;

import aio.health2world.http.HttpResult;

/**
 * Created by lishiyou on 2018/8/20 0020.
 */

public class TerminationPresenter extends TerminationContract.Presenter {

    public TerminationPresenter(TerminationContract.View mView) {
        super(mView);
    }

    @Override
    void loadServicePackage(String patientId) {
        ApiRequest.getPatientSignDetail(patientId, 1, new ResultSubscriber<List<SignService>>() {

            @Override
            public void onStart() {
                super.onStart();
                if (mView != null)
                    mView.showLoading();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (mView != null)
                    mView.hideLoading();
            }

            @Override
            public void onNext(HttpResult<List<SignService>> t) {
                super.onNext(t);
                if (mView != null)
                    mView.hideLoading();
                if (t.code.equals(AppConfig.SUCCESS)) {
                    if (mView != null)
                        mView.loadServicePackageSuccess(t.data);
                }
            }
        });
    }
}
