package com.health2world.aio.common.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.health2world.aio.common.BaseFragment;

import aio.health2world.view.LoadingDialog;

/**
 * Created by Administrator on 2018/7/6 0006.
 */

public abstract class MVPBaseFragment<T extends BasePresenter> extends BaseFragment implements BaseView<T> {

    public T mPresenter;

    private LoadingDialog mLoadingDialog;

    public MVPBaseFragment() {
        mPresenter = getPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingDialog = new LoadingDialog(mActivity);
    }

    protected abstract T getPresenter();

    @Override
    public void showLoading() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
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
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.destroy();
            mPresenter = null;
        }
    }
}
