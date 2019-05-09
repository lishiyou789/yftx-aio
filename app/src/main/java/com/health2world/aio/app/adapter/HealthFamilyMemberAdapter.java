package com.health2world.aio.app.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.health2world.aio.R;
import com.health2world.aio.common.DataServer;
import com.konsung.bean.ResidentBean;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;
import aio.health2world.glide_transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2018/7/25 0025.
 */

public class HealthFamilyMemberAdapter extends BaseQuickAdapter<ResidentBean, BaseViewHolder> {

    private TagFlowLayout tagRecyclerView;
    private ImageView ivImage, ivIndicator;
    //标签简称集合
    private List<String> list = new ArrayList<>();

    public HealthFamilyMemberAdapter(@Nullable List<ResidentBean> data) {
        super(R.layout.item_health_family_member, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ResidentBean item) {
        tagRecyclerView = helper.getView(R.id.recyclerView_tag);
        ivImage = helper.getView(R.id.ivImage);
        ivIndicator = helper.getView(R.id.ivIndicator);
        TextView tv_name = helper.getView(R.id.tv_name);
        tv_name.setMaxEms(4);
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_sex, DataServer.getSexNick(item.getSexy()));
        helper.setText(R.id.tv_age, item.getAge()<0 ?"": item.getAge()+"岁");
        if (item.isCheck()) {
            int color = mContext.getResources().getColor(R.color.blue_light);
            helper.setBackgroundColor(R.id.layout_health_family_member, color);
            ivIndicator.setVisibility(View.VISIBLE);
        } else {
            int color = mContext.getResources().getColor(R.color.white);
            helper.setBackgroundColor(R.id.layout_health_family_member, color);
            ivIndicator.setVisibility(View.GONE);
        }
        tagRecyclerView.removeAllViews();
        if (!TextUtils.isEmpty(item.getTagIds())) {
            list.clear();
            String tagNames = DataServer.getTagShortName(item.getTagIds());
            for (String s : tagNames.split(",")) {
                if (!TextUtils.isEmpty(s))
                    list.add(s);
                if (list.size() >= 4)
                    break;
            }
            tagRecyclerView.setAdapter(new TagListAdapter(mContext, list));
        }

        if (TextUtils.isEmpty(item.getServiceIds()))
            helper.setVisible(R.id.tvSign, false);
        else
            helper.setVisible(R.id.tvSign, true);

        Glide.with(mContext)
                .load(item.getPortrait())
                .placeholder(R.mipmap.user_portrait_circle)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(ivImage);
    }
}
