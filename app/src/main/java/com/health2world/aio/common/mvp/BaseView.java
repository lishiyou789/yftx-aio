package com.health2world.aio.common.mvp;

/**
 * Created by Administrator on 2018/7/4 0004.
 */

public interface BaseView<T extends BasePresenter> {


    void showLoading();

    void showLoading(String tips);

    void hideLoading();

    void setCancelable(boolean isCancel);
}

