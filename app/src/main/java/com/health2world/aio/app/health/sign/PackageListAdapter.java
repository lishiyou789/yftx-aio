package com.health2world.aio.app.health.sign;

import android.view.View;

import com.health2world.aio.R;

import java.util.List;

import aio.health2world.brvah.BaseSectionQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * 推荐服务包和其他服务包进行分组处理
 * Created by lishiyou on 2018/8/8 0008.
 */

public class PackageListAdapter extends BaseSectionQuickAdapter<SignServiceSection, BaseViewHolder> {

    public PackageListAdapter(List<SignServiceSection> data) {
        super(R.layout.item_service_package_content, R.layout.item_service_package_title, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, SignServiceSection item) {
        helper.setText(R.id.tvServiceTitle, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, SignServiceSection item) {

        helper.addOnClickListener(R.id.tvPackageDetail);

        helper.setText(R.id.tvShortName, item.t.getShortName());
        helper.setText(R.id.tvServiceName, item.t.getPageName());
        helper.setText(R.id.ivPrice, item.t.getPrice() + "元/年");
        //选中
        if (item.t.isChecked()) {
            //已经签约背景色
            if (item.t.isSigned()) {
                helper.getView(R.id.ivSign).setVisibility(View.VISIBLE);
                helper.setBackgroundColor(R.id.llServicePackageItem, mContext.getResources().getColor(R.color.eee));
            } else if (item.t.getLimit() == 1) {
                //已经达到上限
                helper.getView(R.id.ivSign).setVisibility(View.GONE);
                helper.setBackgroundColor(R.id.llServicePackageItem, mContext.getResources().getColor(R.color.blackD));
            } else {
                //未签约背景色
                helper.getView(R.id.ivSign).setVisibility(View.GONE);
                helper.setBackgroundColor(R.id.llServicePackageItem, mContext.getResources().getColor(R.color.blue_light));
            }
        } else {
            //普通背景色
            helper.getView(R.id.ivSign).setVisibility(View.GONE);
            helper.setBackgroundColor(R.id.llServicePackageItem, mContext.getResources().getColor(R.color.white));
        }
    }
}
