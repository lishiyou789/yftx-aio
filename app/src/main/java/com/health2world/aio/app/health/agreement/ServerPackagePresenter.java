package com.health2world.aio.app.health.agreement;


import com.health2world.aio.bean.SignService;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.ResultSubscriber;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.ToastUtil;

/**
 * 实现方法
 *
 * @author Runnlin
 * @date 2018/7/26/0026.
 */

public class ServerPackagePresenter extends ServerPackageContract.Presenter {

    public ServerPackagePresenter(ServerPackageContract.View mView) {
        super(mView);
    }

    @Override
    void packageDetail(final int serviceId) {
        ApiRequest.getServicePackageDetail(serviceId, new ResultSubscriber<SignService>() {
            @Override
            public void onStart() {
                if (mView != null)
                    mView.showLoading();
            }

            @Override
            public void onCompleted() {
                if (mView != null)
                    mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (mView != null)
                    mView.hideLoading();
            }

            @Override
            public void onNext(HttpResult<SignService> t) {
                super.onNext(t);
                if (t.code.equals(AppConfig.SUCCESS)) {
                    SignService data = t.data;
                    //回调返回请求结果
                    if (mView != null)
                        mView.packageDetailSuccess(data);
                } else {
                    ToastUtil.showLong(t.errorMessage);
                }
            }
        });
    }


}
