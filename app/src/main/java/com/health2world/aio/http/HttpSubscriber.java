package com.health2world.aio.http;

import com.health2world.aio.MyApplication;
import com.health2world.aio.config.AppConfig;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.ExceptionHandle;
import aio.health2world.utils.ToastUtil;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public abstract class HttpSubscriber extends Subscriber<HttpResult> {

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        if (e != null) {
            ExceptionHandle.ResponseThrowable e1 = ExceptionHandle.handleException(e);
            ToastUtil.showLong(e1.message);
        }
    }

    @Override
    public void onNext(HttpResult result) {
        //登录失效004  账号被移除009
        if (!result.code.equals(AppConfig.SUCCESS) &&
                !result.code.equals(AppConfig.SUCCESS_AIO)) {
            ToastUtil.showShort(result.errorMessage);
        }
        if (result.code.equals("004") || result.code.equals("109")) {
            MyApplication.getInstance().logout();
        }
    }
}
