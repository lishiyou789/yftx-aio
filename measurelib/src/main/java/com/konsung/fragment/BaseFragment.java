package com.konsung.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konsung.presenter.BasePresenter;

/**
 * Fragment的基类
 *
 * @param <P> 泛型
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment {
    protected P presenter; //业务逻辑处理类

    /**
     * 构造方法
     */
    public BaseFragment() {
        presenter = initPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 实例逻辑处理
     *
     * @return 逻辑处理
     */
    public abstract P initPresenter();
}
