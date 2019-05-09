package com.health2world.aio.app.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.health2world.aio.R;
import com.konsung.bean.ResidentBean;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;
import aio.health2world.glide_transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2018/7/18 0018.
 */

public class FamilyMemberAdapter extends BaseQuickAdapter<ResidentBean, BaseViewHolder> {
    public FamilyMemberAdapter(@Nullable List<ResidentBean> data) {
        super(R.layout.item_family_member, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ResidentBean item) {

        ImageView img_member_family_head = helper.getView(R.id.img_member_family_head);
        ImageView ivIndicator = helper.getView(R.id.arrow_family_member);
        helper.setText(R.id.tv_family_member, item.getName());
        if (item.isCheck()) {
            int color = mContext.getResources().getColor(R.color.blue_light);
            helper.setBackgroundColor(R.id.layout_family_member, color);
            helper.getView(R.id.layout_family_member).getBackground();
            ivIndicator.setVisibility(View.VISIBLE);
        } else {
            int color = mContext.getResources().getColor(R.color.white);
            helper.setBackgroundColor(R.id.layout_family_member, color);
            ivIndicator.setVisibility(View.INVISIBLE);
        }
        Glide.with(mContext)
                .load(item.getPortrait())
                .placeholder(R.mipmap.user_portrait_circle)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(img_member_family_head);
    }
}
