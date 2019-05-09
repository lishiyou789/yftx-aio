package com.health2world.aio.app.health.doctor;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.bean.ServiceItem;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.util.DefaultTextWatcher;

import aio.health2world.utils.ToastUtil;

/**
 * @author Administrator
 * @date 2018/8/14/0014.
 */

public class HealthGuideActivity extends BaseActivity {

    /*UI*/
    private TextView tvMeasureData;
    private EditText etHealthGuide;
    private TextView tvTextNum, tvTitle;
    private Button btnConfirm;

    /*DATA*/
    private String advise = "";

    /*CONS*/
    private ServiceItem serviceItem;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_ta;
    }

    @Override
    protected void initView() {
        tvMeasureData = findView(R.id.tv_measure_data);
        etHealthGuide = findView(R.id.et_health_guide);
        btnConfirm = findView(R.id.btn_confirm);
        tvTextNum = findView(R.id.tv_text_nums);
        tvTitle = findView(R.id.tv_title);
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("serviceItem"))
            serviceItem = (ServiceItem) getIntent().getSerializableExtra("serviceItem");
        if (serviceItem != null)
            tvTitle.setText(serviceItem.getItemName());
        if (serviceItem.getServiceType() == 1)
            etHealthGuide.setHint("请输入本次测量的指导意见，确认后将完成此次服务");

    }

    @Override
    protected void initListener() {
        etHealthGuide.addTextChangedListener(new DefaultTextWatcher() {
            boolean islMaxCount = false;

            @Override
            public void afterTextChanged(Editable editable) {
                int detailLength = editable.length();
                tvTextNum.setText(detailLength + "/255");
                if (detailLength == 254) {
                    islMaxCount = true;
                }
                if (detailLength == 254 && islMaxCount) {
                    ToastUtil.showLong("已达最大输入限制");
                    islMaxCount = false;
                }
                advise = etHealthGuide.getText().toString();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(advise)) {
                    ToastUtil.showShort("请输入指导建议");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("advise", advise);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
