package com.health2world.aio.common.mvp;

/**
 * Created by Administrator on 2018/7/4 0004.
 */

public abstract class BasePresenter<T extends BaseView> {

    protected T mView;

    public BasePresenter(T mView) {
        this.mView = mView;
    }

    public void destroy() {
        mView = null;
    }
}
