package com.health2world.aio.app.adapter;

import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.bean.ServiceItem;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * @author Runnlin
 * @date 2018/7/26/0026.
 */

public class ServicePackageDetailAdapter extends BaseQuickAdapter<ServiceItem, BaseViewHolder> {
    public ServicePackageDetailAdapter(List<ServiceItem> data) {
        super(R.layout.item_package_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceItem item) {
        TextView termUnit = helper.getView(R.id.package_detail_TermUnit);
        int position = helper.getAdapterPosition() + 1;
        int color = mContext.getResources().getColor(R.color.gray_light);
        if ((position + 1) % 2 == 0)
            helper.setBackgroundColor(R.id.item_package_detail, color);
        else
            helper.setBackgroundColor(R.id.item_package_detail,mContext.getResources().getColor(R.color.white));
        helper.setText(R.id.package_detail_pageName, item.getItemName());

        if (item.getTargetNum() != 0) {
            String ServiceTermUnit = "";
            switch (item.getTermUnit()) {
                case "1":
                    ServiceTermUnit = "年";
                    break;
                case "2":
                    ServiceTermUnit = "月";
                    break;
                case "3":
                    ServiceTermUnit = "日";
                    break;
            }
            termUnit.setText(item.getTargetNum() + "次/" + ServiceTermUnit);
        } else {
            termUnit.setText("");
        }
    }
}
