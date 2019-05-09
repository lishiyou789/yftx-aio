package com.health2world.aio.app.health.personal;

import android.support.v7.widget.RecyclerView;

import com.health2world.aio.R;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.MVPBaseFragment;
import com.konsung.bean.ResidentBean;

/**
 * Created by lishiyou on 2018/8/6 0006.
 */

public class PersonalServiceFragment extends MVPBaseFragment {

    private ResidentBean resident;

    private RecyclerView recyclerView;


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_tobe_developed;
    }


    @Override
    protected void initView() {
       
    }

    @Override
    protected void initData() {
        resident = (ResidentBean) getArguments().getSerializable("resident");
    }

    @Override
    protected void initListener() {

    }


}
