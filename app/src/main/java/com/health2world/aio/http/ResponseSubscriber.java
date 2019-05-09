package com.health2world.aio.http;

import java.io.IOException;

import aio.health2world.utils.ExceptionHandle;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/7/3 0003.
 */

public abstract class ResponseSubscriber extends Subscriber<ResponseBody> {

    protected abstract void onSuccess(String response);

    protected abstract void onFailed(String errorMessage);

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        ExceptionHandle.ResponseThrowable throwable = ExceptionHandle.handleException(e);
        onFailed(throwable.message);
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            onSuccess(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
