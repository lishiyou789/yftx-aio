package com.health2world.aio.app.clinic.result;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.health2world.aio.R;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.DataEntity;

/**
 * 通用展示界面 （血脂，尿常规）
 * Created by lishiyou on 2018/9/26 0026.
 */

public class CommonResultActivity extends BaseActivity {

    private RecyclerView recyclerView;

    private List<MedicalData> dataList = new ArrayList<>();

    private CommonResultAdapter resultAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_blood_fat_result;
    }

    @Override
    protected void initView() {
        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    protected void initData() {
        resultAdapter = new CommonResultAdapter(dataList);
        recyclerView.setAdapter(resultAdapter);
        List<MedicalData> list = null;
        if (getIntent().hasExtra("data"))
            list = (List<MedicalData>) getIntent().getSerializableExtra("data");

        if (list != null && list.size() > 0) {
            dataList.addAll(list);
            resultAdapter.notifyDataSetChanged();
        } else {
            finish();
        }
    }

    @Override
    protected void initListener() {

    }
}
