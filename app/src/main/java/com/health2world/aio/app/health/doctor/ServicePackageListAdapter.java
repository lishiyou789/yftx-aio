package com.health2world.aio.app.health.doctor;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;

import com.health2world.aio.R;
import com.health2world.aio.bean.SignService;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * Created by lishiyou on 2018/8/7 0007.
 */

public class ServicePackageListAdapter extends BaseQuickAdapter<SignService, BaseViewHolder> {

    public ServicePackageListAdapter(@Nullable List<SignService> data) {
        super(R.layout.item_service_package, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SignService item) {
        helper.setText(R.id.tv_package_name, item.getServiceName());
        int color = mContext.getResources().getColor(R.color.color_94ccfc);
        View view = helper.getView(R.id.layout_service_package);
        if (item.isChecked()) {
            helper.setBackgroundColor(R.id.layout_service_package, color);
            Drawable background = view.getBackground();
            background.setAlpha(20);
            helper.setVisible(R.id.img_arrow, true);
        } else {
            helper.setBackgroundColor(R.id.layout_service_package, Color.WHITE);
            helper.setVisible(R.id.img_arrow, false);
        }

    }
}
