package com.health2world.aio.app.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gcssloop.widget.RCRelativeLayout;
import com.health2world.aio.R;
import com.health2world.aio.common.DataServer;
import com.konsung.bean.ResidentBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;
import aio.health2world.glide_transformations.GlideRoundTransform;

/**
 * Created by lishiyou on 2018/7/12 0012.
 */

public class RMessageView extends FrameLayout implements View.OnClickListener {

    public static final int TAB_INDEX_FAMILY = 5;
    public static final int TAB_INDEX_HISTORY = 6;

    private Context mContext;

    private ImageView ivImage;

    private RCRelativeLayout ivHead;

    private TextView tvSignFlag, tvName, tvPhone, tvAge, tvHistory, tvFName;

    private LinearLayout llPhone;

    private RecyclerView tagRecyclerView;

    private ResidentBean resident;

    private TabChangedListener listener;

    private List<String> list = new ArrayList<>();//标签简称集合

    private TagListAdapter listAdapter;

    public RMessageView(@NonNull Context context) {
        this(context, null);
    }

    public RMessageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_resident_message, this);
        initView();
        initListener();
    }

    public RCRelativeLayout getIvHead() {
        return ivHead;
    }

    public TextView getHistory() {
        return tvHistory;
    }

    private void initView() {
        llPhone = findViewById(R.id.llPhone);
        ivImage = findViewById(R.id.ivImage);
        tvSignFlag = findViewById(R.id.tvSignFlag);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAge = findViewById(R.id.tvAge);
        tvHistory = findViewById(R.id.tvHistory);
        tvFName = findViewById(R.id.tvFirstName);
        ivHead = findViewById(R.id.iv_resident_head);

        tagRecyclerView = findViewById(R.id.tagRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tagRecyclerView.setLayoutManager(linearLayoutManager);
        listAdapter = new TagListAdapter(list);
        tagRecyclerView.setAdapter(listAdapter);
    }


    private void initListener() {
        tvHistory.setOnClickListener(this);
        ivImage.setOnClickListener(this);
    }

    public void setOnTabChangedListener(TabChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
//        if (resident == null) {
//            ToastUtil.showLong(mContext.getString(R.string.set_current_resident));
//            return;
//        }
        if (v.getId() == R.id.tvHistory) {
            if (listener != null) {
                listener.onTabChecked(TAB_INDEX_HISTORY);
            }
        }
        if (v.getId() == R.id.ivImage) {
            if (listener != null) {
                listener.onTabChecked(TAB_INDEX_FAMILY);
            }
            setChecked(true);
        }
    }

    /**
     * 设置头像选择效果
     */
    public void setChecked(boolean checked) {
        if (checked) {
            ivImage.setBackgroundResource(R.drawable.shape_home_head_img);
            ivHead.setStrokeColor(getResources().getColor(R.color.task_select_color));
            ivHead.setStrokeWidth(2);
        } else {
            ivImage.setBackgroundResource(R.color.white);
            ivHead.setStrokeColor(getResources().getColor(R.color.blackD));
            ivHead.setStrokeWidth(1);
        }
    }

    public void setResident(ResidentBean resident) {
        if (resident != null) {
            this.resident = resident;
            //姓名
            tvName.setText(resident.getName());
            //年龄
            if (resident.getAge() < 0) {
                tvAge.setVisibility(View.GONE);
            } else {
                tvAge.setVisibility(View.VISIBLE);
                tvAge.setText(String.format(mContext.getString(R.string.age), resident.getAge()));
            }
            //电话
            if (TextUtils.isEmpty(resident.getTelPhone())) {
                llPhone.setVisibility(View.GONE);
            } else {
                llPhone.setVisibility(View.VISIBLE);
                tvPhone.setText(resident.getTelPhone());
            }
            //是否签约
            if (resident.getSign() == 1) {
                tvSignFlag.setVisibility(VISIBLE);
            } else {
                tvSignFlag.setVisibility(GONE);
            }
            //标签
            list.clear();
            if (!TextUtils.isEmpty(resident.getLabelNames())) {
                Collections.addAll(list, resident.getLabelNames().split(","));
            }
            listAdapter.notifyDataSetChanged();
//            if (!TextUtils.isEmpty(resident.getTagIds())) {
//                String tagNames = DataServer.getTagShortName(resident.getTagIds());
//                if (!TextUtils.isEmpty(tagNames)) {
//                    Collections.addAll(list, tagNames.split(","));
//                    listAdapter.notifyDataSetChanged();
//                }
//            } else {
//                if (!TextUtils.isEmpty(resident.getLabelNames())) {
//                    Collections.addAll(list, resident.getLabelNames().split(","));
//                }
//                listAdapter.notifyDataSetChanged();
//            }

            //头像 是否注册了微信居民端
            if (resident.getRegister() == 0) {
                ivImage.setImageResource(R.color.tab_resident_background);
                //未注册就用姓来代替头像
                tvFName.setText(resident.getName().substring(0, 1));
            } else {
                tvFName.setText("");
                Glide.with(mContext)
                        .load(resident.getPortrait())
                        .placeholder(R.color.tab_resident_background)
                        .centerCrop()
                        .transform(new GlideRoundTransform(mContext, 2))
                        .into(ivImage);
            }
        } else {//置空居民
            tvName.setText("设置当前居民");
            //年龄
            tvAge.setVisibility(View.GONE);

            //电话
            llPhone.setVisibility(View.GONE);

            //是否签约
            tvSignFlag.setVisibility(GONE);

            //标签
            list.clear();
            listAdapter.notifyDataSetChanged();

            //头像
            ivImage.setImageResource(R.color.tab_resident_background);
            tvFName.setText("无");
        }

    }

    public static class TagListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public TagListAdapter(@Nullable List<String> data) {
            super(R.layout.item_tag_view, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ((TextView) helper.getView(R.id.tvTagName)).setText(item);
        }
    }
}
