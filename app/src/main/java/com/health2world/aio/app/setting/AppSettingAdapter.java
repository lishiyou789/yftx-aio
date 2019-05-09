package com.health2world.aio.app.setting;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.bean.SettingBean;
import com.health2world.aio.config.AppConfig;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;
import aio.health2world.utils.SPUtils;

/**
 * Created by lishiyou on 2018/7/19 0019.
 */

public class AppSettingAdapter extends BaseQuickAdapter<SettingBean, BaseViewHolder> {

    public AppSettingAdapter(List<SettingBean> data) {
        super(R.layout.item_setting, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SettingBean item) {
        ImageView ivSettingIcon = helper.getView(R.id.ivSettingIcon);
        TextView tvSettingName = helper.getView(R.id.tvSettingName);
        TextView tvSettingValue = helper.getView(R.id.tvSettingValue);
        ImageView ivRight = helper.getView(R.id.ivRight);
        Switch switchBtn = helper.getView(R.id.switchBtn);

        ivSettingIcon.setImageResource(item.getResId());
        tvSettingName.setText(item.getName());
        tvSettingValue.setText(item.getValue());

        if (item.getAction() == SettingActivity.ITEM_MEASURE_MODE) {
            switchBtn.setVisibility(View.VISIBLE);
            ivRight.setVisibility(View.GONE);
            switchBtn.setChecked(!MyApplication.getInstance().getMeasureMode());
            switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SPUtils.put(mContext, AppConfig.MEASURE_MODE, !isChecked);
                }
            });
        } else {
            switchBtn.setVisibility(View.GONE);
            ivRight.setVisibility(View.VISIBLE);
            switchBtn.setOnCheckedChangeListener(null);
        }
    }
}
