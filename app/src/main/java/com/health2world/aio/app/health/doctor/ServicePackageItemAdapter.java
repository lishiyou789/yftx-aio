package com.health2world.aio.app.health.doctor;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.health2world.aio.R;
import com.health2world.aio.bean.ServiceItem;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * Created by lishiyou on 2018/8/7 0007.
 */

public class ServicePackageItemAdapter extends BaseQuickAdapter<ServiceItem, BaseViewHolder> {

    public ServicePackageItemAdapter(@Nullable List<ServiceItem> data) {
        super(R.layout.item_service_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceItem item) {
        int position = helper.getAdapterPosition() + 1;
        helper.setText(R.id.tv_sort, String.valueOf(position) + "：");
        helper.setText(R.id.tv_item, item.getItemName());
        if (position % 2 == 0)
            helper.setBackgroundColor(R.id.layout_service_item, Color.WHITE);
        else
            helper.setBackgroundColor(R.id.layout_service_item, mContext.getResources().getColor(R.color.gray_light));

        int executeNum = item.getExecuteNum();
        int targetNum = item.getTargetNum();
        String strNum = targetNum == 0 ? String.valueOf(executeNum) + "次" : executeNum + "/" + targetNum + "次";
        helper.setText(R.id.tv_num, strNum);
        helper.addOnClickListener(R.id.tv_execute);
    }
}
