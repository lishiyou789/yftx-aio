package com.health2world.aio.app.health.sign;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.health2world.aio.R;
import com.health2world.aio.bean.SignMember;
import com.health2world.aio.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * 签约成功之后弹出二维码
 * Created by lishiyou on 2018/10/10 0010.
 */

public class SignSuccessActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    private List<SignMember> list = new ArrayList<>();

    private RecyclerView recyclerView;
    private ImageView ivQRCode;
    private MemberListAdapter listAdapter;

    private SignMember members = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_success;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ivQRCode = (ImageView) findViewById(R.id.ivQRCode);
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("data"))
            list = (List<SignMember>) getIntent().getSerializableExtra("data");

        listAdapter = new MemberListAdapter(list);
        recyclerView.setAdapter(listAdapter);

        if (list != null && list.size() > 0) {
            members = list.get(0);
            members.setChecked(true);
            //取第一个人的ticket生产二维码
            String qrUrl = list.get(0).getMembers();
            Glide.with(this)
                    .load(qrUrl)
                    .placeholder(R.mipmap.ic_default_image)
                    .into(ivQRCode);
        }
    }

    @Override
    protected void initListener() {
        listAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (members != null) {
            members.setChecked(false);
        }
        members = list.get(position);
        members.setChecked(true);
        listAdapter.notifyDataSetChanged();
        String qrUrl = members.getMembers();
        Glide.with(this)
                .load(qrUrl)
                .placeholder(R.mipmap.ic_default_image)
                .centerCrop()
                .into(ivQRCode);
    }

    private class MemberListAdapter extends BaseQuickAdapter<SignMember, BaseViewHolder> {

        public MemberListAdapter(@Nullable List<SignMember> data) {
            super(R.layout.item_member_list, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SignMember item) {
            TextView tvName = helper.getView(R.id.tvName);
            TextView tvPhone = helper.getView(R.id.tvPhone);
            tvName.setText(item.getPatientName());
            tvPhone.setText(item.getTelphone());
            if (item.isChecked()) {
                tvName.setTextColor(getResources().getColor(R.color.appThemeColor));
                tvPhone.setTextColor(getResources().getColor(R.color.appThemeColor));
            } else {
                tvName.setTextColor(getResources().getColor(R.color.black6));
                tvPhone.setTextColor(getResources().getColor(R.color.black6));
            }
        }
    }
}
