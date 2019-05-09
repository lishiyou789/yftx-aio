package com.health2world.aio.app.setting;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.bean.MeasureItem;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.config.MeasureConfig;
import com.health2world.aio.util.Logger;
import com.konsung.listen.Measure;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.SPUtils;
import aio.health2world.utils.ToastUtil;

/**
 * 测量项配置(只配置可以测量的项目) 不能测量的项目暂时不支持配置
 * Created by lishiyou on 2018/7/20 0020.
 */

public class MeasureSettingActivity extends BaseActivity implements View.OnClickListener,
        BaseQuickAdapter.OnItemClickListener {

    private TextView tvCancel, tvSure;

    private RecyclerView recyclerView;

    private List<MeasureItem> itemList;

    private MeasureSettingAdapter settingAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_measure_setting;
    }

    @Override
    public void initView() {
        tvCancel = findView(R.id.tvCancel);
        tvSure = findView(R.id.tvSure);
        recyclerView = findView(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
//        if (AppConfig.isDebug)
//            findView(R.id.llBottom).setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        //获取用户已经配置好的测量项
        //11111100000000111
        String mConfig = MyApplication.getInstance().getMeasureConfig();
        Logger.e("zrl", "测量项： "+mConfig);
        itemList = new ArrayList<>();
        for (int i = 0; i < mConfig.length(); i++) {
            boolean isOpen = Integer.valueOf(String.valueOf(mConfig.charAt(i))) == 1;
            if (i == MeasureConfig.NIBP)
                itemList.add(new MeasureItem(Measure.NIBP, isOpen));
            if (i == MeasureConfig.SPO2)
                itemList.add(new MeasureItem(Measure.SPO2, isOpen));
            if (i == MeasureConfig.ECG)
                itemList.add(new MeasureItem(Measure.ECG, isOpen));
            if (i == MeasureConfig.TEMP)
                itemList.add(new MeasureItem(Measure.TEMP, isOpen));
            if (i == MeasureConfig.GLU)
                itemList.add(new MeasureItem(Measure.GLU, isOpen));
            if (i == MeasureConfig.URINE)
                itemList.add(new MeasureItem(Measure.URINE, isOpen));
            if (i == MeasureConfig.CHOL)
                itemList.add(new MeasureItem(Measure.CHOL, isOpen));
            if (i == MeasureConfig.UA)
                itemList.add(new MeasureItem(Measure.UA, isOpen));
            if (i == MeasureConfig.BLOOD)
                itemList.add(new MeasureItem(Measure.BLOOD, isOpen));
            if (i == MeasureConfig.HB)
                itemList.add(new MeasureItem(Measure.HB, isOpen));
            if (i == MeasureConfig.HEIGHT)
                itemList.add(new MeasureItem(Measure.HEIGHT, isOpen));
            if (i == MeasureConfig.WEIGHT)
                itemList.add(new MeasureItem(Measure.WEIGHT, isOpen));
            if (i == MeasureConfig.DS100A)
                itemList.add(new MeasureItem(Measure.DS100A, isOpen));
            if (i == MeasureConfig.CRP)
                itemList.add(new MeasureItem(Measure.CRP, isOpen));
            if (i == MeasureConfig.SAA)
                itemList.add(new MeasureItem(Measure.SAA, isOpen));
            if (i == MeasureConfig.PCT)
                itemList.add(new MeasureItem(Measure.PCT, isOpen));
            if (i == MeasureConfig.MYO)
                itemList.add(new MeasureItem(Measure.MYO, isOpen));
            if (i == MeasureConfig.GHB)
                itemList.add(new MeasureItem(Measure.GHB, isOpen));
            if (i == MeasureConfig.WBC)
                itemList.add(new MeasureItem(Measure.WBC, isOpen));
        }
        settingAdapter = new MeasureSettingAdapter(itemList);
        recyclerView.setAdapter(settingAdapter);
    }

    @Override
    protected void initListener() {
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        settingAdapter.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//        MeasureItem item = (MeasureItem) adapter.getItem(position);
//        if (AppConfig.isDebug) {
//            item.setMeasured(!item.isMeasured());
//            settingAdapter.notifyItemChanged(position);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                finish();
                break;
            case R.id.tvSure:
                //根据选中的状态修改本地测量项的配置
                String config = "";
                boolean isBlood4 = false;
                boolean isBlood8 = false;
                for (MeasureItem item : itemList) {
                    //如果选中
                    if (item.isMeasured()) {
                        config += 1;
                        if (item.getType() == Measure.BLOOD)
                            isBlood4 = true;
                        if (item.getType() == Measure.DS100A)
                            isBlood8 = true;
                    } else {
                        config += 0;
                    }
                }
                if (isBlood4 && isBlood8) {
                    ToastUtil.showShort("血脂四项和血脂检测不可同时选中");
                    return;
                }
                //血脂四项和血脂八项不可同时选中
                SPUtils.put(this, AppConfig.MEASURE_CONFIG, config);
                finish();
                break;
        }
    }
}
