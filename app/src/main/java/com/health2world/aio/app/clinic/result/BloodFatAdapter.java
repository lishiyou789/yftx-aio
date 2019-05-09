package com.health2world.aio.app.clinic.result;

import android.support.annotation.Nullable;

import com.health2world.aio.R;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * Created by lishiyou on 2018/8/22 0022.
 */

public class BloodFatAdapter extends BaseQuickAdapter<BloodFatBean, BaseViewHolder> {

    public BloodFatAdapter(@Nullable List<BloodFatBean> data) {
        super(R.layout.item_blood_fat_result, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BloodFatBean item) {
        helper.setText(R.id.tvItemName, item.name);
        if (item.name.equals("动脉硬化指数（AI）") || item.name.equals("冠心病危险指数（R-CHD）")) {
            helper.setText(R.id.tvNormalRange, "[ <=" + item.max + " ]");
            helper.setText(R.id.tvResult, item.value);
        } else {
            helper.setText(R.id.tvResult, item.value + " mmol/l");
            helper.setText(R.id.tvNormalRange, "[ " + item.min + "-" + item.max + "mmol/l ]");
        }

        if (item.normal)
            helper.setTextColor(R.id.tvResult, mContext.getResources().getColor(R.color.black6));
        else
            helper.setTextColor(R.id.tvResult, mContext.getResources().getColor(R.color.red));

        helper.addOnClickListener(R.id.tvDetail);
    }
}
