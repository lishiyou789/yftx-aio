package com.health2world.aio.common.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.health2world.aio.common.BaseActivity;

import aio.health2world.view.LoadingDialog;

/**
 * Created by Administrator on 2018/7/4 0004.
 */

public abstract class MVPBaseActivity<T extends BasePresenter> extends BaseActivity implements BaseView<T> {

    protected T mPresenter;

    private LoadingDialog mLoadingDialog;


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = getPresenter();
        mLoadingDialog = new LoadingDialog(this);
        super.onCreate(savedInstanceState);
    }

    protected abstract T getPresenter();

    @Override
    public void showLoading() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    @Override
    public void showLoading(String tips) {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.setTips(tips);
            mLoadingDialog.show();
        }
    }

    @Override
    public void setCancelable(boolean isCancel) {
        if (mLoadingDialog != null) {
            mLoadingDialog.setCancelable(isCancel);
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
            mPresenter = null;
        }
    }

}
