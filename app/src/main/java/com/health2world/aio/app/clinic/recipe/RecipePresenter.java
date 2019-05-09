package com.health2world.aio.app.clinic.recipe;

import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;

import aio.health2world.http.HttpResult;

/**
 * Created by lishiyou on 2018/7/26 0026.
 */

public class RecipePresenter extends RecipeContract.Presenter {

    public RecipePresenter(RecipeContract.View mView) {
        super(mView);
    }

    //手机开方(生成任务或报告)
    @Override
    void phoneClinic(String patientId, String dataId, int clinicType, String description, String advice) {
        ApiRequest.phoneClinic(patientId, dataId, clinicType, description, advice, new HttpSubscriber() {
            @Override
            public void onStart() {
                super.onStart();
                if (mView != null)
                    mView.showLoading();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (mView != null) {
                    mView.hideLoading();
                    mView.phoneClinicFailed();
                }
            }

            @Override
            public void onNext(HttpResult result) {
                super.onNext(result);
                if (mView != null)
                    mView.hideLoading();
                if (result.code.equals(AppConfig.SUCCESS)) {
                    if (mView != null)
                        mView.phoneClinicSuccess();
                }
            }
        });
    }
}
