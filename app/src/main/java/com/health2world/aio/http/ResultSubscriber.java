package com.health2world.aio.http;//package aio.health2world.http.subscriber;

import com.health2world.aio.MyApplication;
import com.health2world.aio.config.AppConfig;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.ExceptionHandle;
import aio.health2world.utils.ToastUtil;
import rx.Subscriber;

/**
 * Created by _SOLID2016/7/27
 * Date:
 * Time:21:27
 */
public class ResultSubscriber<T> extends Subscriber<HttpResult<T>> {

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        if (e != null) {
            ExceptionHandle.ResponseThrowable e1 = ExceptionHandle.handleException(e);
            ToastUtil.showLong(e1.message);
            e.printStackTrace();
        }
    }

    @Override
    public void onNext(HttpResult<T> t) {
        //登录失效004  账号被移除009
        if (!t.code.equals(AppConfig.SUCCESS) && !t.code.equals(AppConfig.SUCCESS_AIO)) {
            ToastUtil.showShort(t.errorMessage);
        }
        if (t.code.equals("004") || t.code.equals("109")) {
            MyApplication.getInstance().logout();
        }
    }

}
