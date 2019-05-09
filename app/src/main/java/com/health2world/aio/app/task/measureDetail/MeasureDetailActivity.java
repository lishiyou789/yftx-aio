package com.health2world.aio.app.task.measureDetail;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.bean.IndexData;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.konsung.bean.ResidentBean;

import java.util.Date;

import aio.health2world.utils.DateUtil;

/**
 * @author Administrator
 * @date 2019/1/5/0005.
 */

public class MeasureDetailActivity extends MVPBaseActivity<MeasureDetailContract.Presenter> implements MeasureDetailContract.View {

    private ResidentBean resident;

    private String data;
    private String name;
    private String time;
    private String gluStyle;
    private ImageView imgClose;

    private String measurementProject;

    private TextView tvClinic, tvCheck, tvMean, tvRange, tvDecide, tvMeasureData, tvName, tvAge, tvTime;

    private View layoutMean, layoutClinic;

    @Override
    protected MeasureDetailContract.Presenter getPresenter() {
        return new MeasureDetailPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_measure_detail;
    }

    @Override
    protected void initView() {
        tvClinic = findView(R.id.tvClinic);
        layoutClinic = findView(R.id.layoutClinic);
        tvCheck = findView(R.id.tvCheck);
        tvMean = findView(R.id.tvMean);
        layoutMean = findView(R.id.layoutMean);
        tvRange = findView(R.id.tvRange);
        tvDecide = findView(R.id.tvDecide);
        tvMeasureData = findView(R.id.tvMeasureData);
        tvName = findView(R.id.tvName);
        tvAge = findView(R.id.tvAge);
        tvTime = findView(R.id.tvTime);
        imgClose = findView(R.id.imgClose);
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("resident"))
            resident = (ResidentBean) getIntent().getSerializableExtra("resident");
        if (getIntent().hasExtra("data"))
            data = getIntent().getStringExtra("data");
        if (getIntent().hasExtra("measurementProject"))
            measurementProject = getIntent().getStringExtra("measurementProject");
        if (getIntent().hasExtra("gluStyle"))
            gluStyle = getIntent().getStringExtra("gluStyle");
        if (getIntent().hasExtra("name"))
            name = getIntent().getStringExtra("name");
        if (getIntent().hasExtra("time"))
            time = getIntent().getStringExtra("time");

        mPresenter.getMeasureDetail(resident, data, name, measurementProject, gluStyle);

        if (resident != null) {
            tvName.setText("姓名：" + resident.getName());
            tvAge.setText("年龄：" + (resident.getAge() == -1 ? "0" : resident.getAge()) + "岁");
        }
        tvCheck.setText("检测项目：" + name);
        tvTime.setText("检查日期：" + (TextUtils.isEmpty(time) ? DateUtil.getCurrentTime(new Date(System.currentTimeMillis())) : time));
    }

    @Override
    public void loadSuccess(IndexData data) {
        tvMeasureData.setText(data.getData());
        tvRange.setText(data.getNormalData());
        //指标意义
        if (TextUtils.isEmpty(data.getItemMean())) {
            layoutMean.setVisibility(View.GONE);
        } else {
            layoutMean.setVisibility(View.VISIBLE);
            tvMean.setText(data.getItemMean());
        }
        //临床意义
        if (TextUtils.isEmpty(data.getClinicalTob())) {
            layoutClinic.setVisibility(View.GONE);
        } else {
            layoutClinic.setVisibility(View.VISIBLE);
            tvClinic.setText(data.getClinicalTob());
        }
        //指标判断
        tvDecide.setText(data.getQuotaJudge());
        if (data.getQuotaJudge().startsWith("正常")) {
            tvDecide.setTextColor(mContext.getResources().getColor(R.color.black6));
            tvMeasureData.setTextColor(mContext.getResources().getColor(R.color.black6));
        } else {
            tvDecide.setTextColor(mContext.getResources().getColor(R.color.red));
            tvMeasureData.setTextColor(mContext.getResources().getColor(R.color.red));
        }

    }

    @Override
    protected void initListener() {
        imgClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.imgClose) {
            this.finish();
        }
    }
}
