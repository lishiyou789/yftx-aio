package com.health2world.aio.app.clinic.result;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.history.chart.ChartContract;
import com.health2world.aio.app.task.measureDetail.MeasureDetailActivity;
import com.health2world.aio.common.BaseActivity;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.ToastUtil;

/**
 * 显示血脂测量结果
 * Created by lishiyou on 2018/8/15 0015.
 */

public class BloodFatResultActivity extends BaseActivity {

    public static final int BLOOD_TYPE_4 = 1;
    public static final int BLOOD_TYPE_8 = 2;

    //默认血脂八项
    private int type = 2;

    private MeasureBean measure;

    private RecyclerView recyclerView;

    private List<BloodFatBean> beanList = new ArrayList<>();

    private BloodFatAdapter bloodFatAdapter;

    private String time;

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
        if (getIntent().hasExtra("measureBean"))
            measure = (MeasureBean) getIntent().getSerializableExtra("measureBean");
        if (getIntent().hasExtra("type"))
            type = getIntent().getIntExtra("type", 2);
        if (getIntent().hasExtra("time"))
            time = getIntent().getStringExtra("time");

        bloodFatAdapter = new BloodFatAdapter(beanList);
        recyclerView.setAdapter(bloodFatAdapter);

        if (measure == null)
            return;

        BloodFatBean bean = new BloodFatBean();
        bean.name = "高密度脂蛋白胆固醇（HDL-C）";
//        bean.type = AppConfig.BLOOD;
        bean.min = 1.0f;
        bean.max = 1.9f;
        bean.viewMin = 0.4f;
        bean.viewMax = 3.9f;
        bean.value = measure.getBlood_hdl() == null ? "" : measure.getBlood_hdl();
        if (!TextUtils.isEmpty(bean.value)) {
            if (!bean.value.contains(">") && !bean.value.contains("<")) {
                if (Float.parseFloat(bean.value) > bean.max || Float.parseFloat(bean.value) < bean.min)
                    bean.normal = false;
            }
            beanList.add(bean);
        }
        if (type == BLOOD_TYPE_8) {
            BloodFatBean bean2 = new BloodFatBean();
            bean2.name = "葡萄糖（GLU）";
            bean2.min = 3.9f;
            bean2.max = 6.1f;
            bean2.viewMin = 2.0f;
            bean2.viewMax = 20.0f;
            if (type == BLOOD_TYPE_4)
                bean2.value = measure.getGlu() + "";
            else
                bean2.value = measure.getGluTree() + "";
            if (!TextUtils.isEmpty(bean2.value)) {
                if (!bean2.value.contains(">") && !bean2.value.contains("<")) {
                    if (Float.parseFloat(bean2.value) > bean2.max || Float.parseFloat(bean2.value) < bean2.min)
                        bean2.normal = false;
                }
                beanList.add(bean2);
            }
        }

        BloodFatBean bean3 = new BloodFatBean();
        bean3.name = "总胆固醇（TC）";
        bean3.min = 3.12f;
        bean3.max = 5.18f;
        bean3.viewMin = 2.0f;
        bean3.viewMax = 12.0f;
        bean3.value = measure.getBlood_tc() == null ? "0.00" : measure.getBlood_tc();
        if (!TextUtils.isEmpty(bean3.value)) {
            if (!bean3.value.contains(">") && !bean3.value.contains("<")) {
                if (Float.parseFloat(bean3.value) > bean3.max || Float.parseFloat(bean3.value) < bean3.min)
                    bean3.normal = false;
            }
            beanList.add(bean3);
        }

        BloodFatBean bean4 = new BloodFatBean();
        bean4.name = "甘油三酯（TG）";
        bean4.min = 0.44f;
        bean4.max = 1.7f;
        bean4.viewMin = 0.4f;
        bean4.viewMax = 6.0f;
        bean4.value = measure.getBlood_tg() == null ? "" : measure.getBlood_tg();
        if (!TextUtils.isEmpty(bean4.value)) {
            if (!bean4.value.contains(">") && !bean4.value.contains("<")) {
                if (Float.parseFloat(bean4.value) > bean4.max || Float.parseFloat(bean4.value) < bean4.min)
                    bean4.normal = false;
            }
            beanList.add(bean4);
        }

        if (type == BLOOD_TYPE_8) {
            BloodFatBean bean5 = new BloodFatBean();
            bean5.name = "极低密度脂蛋白胆固醇（VLDL-C）";
            bean5.min = 0.21f;
            bean5.max = 0.78f;
            bean5.value = measure.getBlood_vldl();
            if (!TextUtils.isEmpty(bean5.value)) {
                if (!bean5.value.contains(">") && !bean5.value.contains("<")) {
                    if (Float.parseFloat(bean5.value) > bean5.max || Float.parseFloat(bean5.value) < bean5.min)
                        bean5.normal = false;
                }
                beanList.add(bean5);
            }

        }

        BloodFatBean bean6 = new BloodFatBean();
        bean6.name = "低密度脂蛋白胆固醇（LDL-C）";
        bean6.min = 0f;
        bean6.max = 3.1f;
        bean6.value = measure.getBlood_ldl() == null ? "0.00" : measure.getBlood_ldl();
        if (!TextUtils.isEmpty(bean6.value)) {
            if (!bean6.value.contains(">") && !bean6.value.contains("<")) {
                if (Float.parseFloat(bean6.value) > bean6.max || Float.parseFloat(bean6.value) < bean6.min)
                    bean6.normal = false;
            }

            beanList.add(bean6);
        }

        if (type == BLOOD_TYPE_8) {
            BloodFatBean bean7 = new BloodFatBean();
            bean7.name = "动脉硬化指数（AI）";
            bean7.max = 4.0f;
            bean7.value = measure.getBlood_ai();
            if (!TextUtils.isEmpty(bean7.value)) {
                if (Float.parseFloat(bean7.value) > bean7.max)
                    bean7.normal = false;
                beanList.add(bean7);
            }

            BloodFatBean bean8 = new BloodFatBean();
            bean8.name = "冠心病危险指数（R-CHD）";
            bean8.max = 4.5f;
            bean8.value = measure.getBlood_r_chd();
            if (!TextUtils.isEmpty(bean8.value)) {
                if (Float.parseFloat(bean8.value) > bean8.max)
                    bean8.normal = false;
                beanList.add(bean8);
            }
        }
        bloodFatAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        bloodFatAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tvDetail) {
                    ResidentBean resident = MyApplication.getInstance().getResident();
                    if (resident == null) {
                        ToastUtil.showShort("请设置当前居民");
                        return;
                    }
                    //打开测量数据解读页面
                    BloodFatBean data = beanList.get(position);
                    Intent intent = new Intent(BloodFatResultActivity.this, MeasureDetailActivity.class);
                    intent.putExtra("resident", MyApplication.getInstance().getResident());
                    intent.putExtra("data", data.value);
                    intent.putExtra("name", data.name);
                    intent.putExtra("time", time);
                    intent.putExtra("measurementProject",  "6");
                    startActivity(intent);
                }
            }
        });
    }
}
