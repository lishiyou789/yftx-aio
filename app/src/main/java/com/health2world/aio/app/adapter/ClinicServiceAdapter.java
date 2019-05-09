package com.health2world.aio.app.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.bean.SignService;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * @author Administrator
 * @date 2018/8/13/0013.
 */

public class ClinicServiceAdapter extends BaseQuickAdapter<SignService, BaseViewHolder> {
    public ClinicServiceAdapter(@Nullable List<SignService> data) {
        super(R.layout.item_service_package_content, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SignService item) {
        TextView pkgTag = helper.getView(R.id.tvShortName);
        TextView pkgName = helper.getView(R.id.tvServiceName);
        TextView pkgPrice = helper.getView(R.id.ivPrice);//$50/年

        pkgTag.setText(item.getShortName());
        pkgName.setText(item.getPageName());
        pkgPrice.setText("¥" + item.getPrice() + "/" + item.getServiceTermUnit());
    }
}
