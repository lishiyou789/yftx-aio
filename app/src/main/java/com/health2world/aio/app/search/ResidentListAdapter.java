package com.health2world.aio.app.search;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.common.DataServer;
import com.konsung.bean.ResidentBean;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * 居民列表
 * Created by lishiyou on 2018/7/23 0023.
 */

public class ResidentListAdapter extends BaseQuickAdapter<ResidentBean, BaseViewHolder> {

    public ResidentListAdapter(@Nullable List<ResidentBean> data) {
        super(R.layout.item_resident_list, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ResidentBean item) {
        TextView tvResidentName = helper.getView(R.id.tvResidentName);
        TextView tvResidentSex = helper.getView(R.id.tvResidentSex);
        TextView tvResidentAge = helper.getView(R.id.tvResidentAge);
        TextView tvResidentIdCard = helper.getView(R.id.tvResidentIdCard);
        TextView tvResidentPhone = helper.getView(R.id.tvResidentPhone);
        TextView tvResidentCode = helper.getView(R.id.tvResidentCode);
        TextView tvAction = helper.getView(R.id.tvAction);

//        if (helper.getLayoutPosition() % 2 == 0) {
//            helper.getView(R.id.itemLayout).setBackgroundResource(R.color.white);
//        } else {
//            helper.getView(R.id.itemLayout).setBackgroundResource(R.color.eee);
//        }

        tvResidentName.setText(item.getName());
        tvResidentSex.setText(DataServer.getSexNick(item.getSexy()));
        tvResidentAge.setText(item.getAge() <= 0 ? "--" : item.getAge() + "岁");
        if (!TextUtils.isEmpty(item.getTelPhone())) {
            tvResidentPhone.setText(item.getTelPhone().replaceAll("(.{3})(.{4})(.{4})", "$1 $**** $3"));
        } else {
            tvResidentPhone.setText("---");
        }
        tvResidentCode.setText(item.getResidentCode());
        CharSequence str = "---";
        if (!TextUtils.isEmpty(item.getIdentityCard())) {
            StringBuilder builder = new StringBuilder(item.getIdentityCard());
            str = builder.replace(3, 14, "*********");
        }
        tvResidentIdCard.setText(str);

        if (item.getDoctor() == 1) {//医生管辖区域
            tvAction.setText(mContext.getString(R.string.select));
//            tvAction.setTextColor(mContext.getResources().getColor(R.color.black3));
        } else {//全平台
            tvAction.setText(mContext.getString(R.string.add));
//            tvAction.setTextColor(mContext.getResources().getColor(R.color.title_center_color));
        }
        helper.addOnClickListener(R.id.tvAction);
    }
}
