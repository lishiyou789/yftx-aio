package com.health2world.aio.app.task;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.health2world.aio.R;
import com.health2world.aio.app.adapter.TagListAdapter;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.bean.TaskInfo;
import com.health2world.aio.common.DataServer;
import com.health2world.aio.config.MedicalConstant;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;
import aio.health2world.glide_transformations.CropCircleTransformation;
import aio.health2world.spanbuilder.Spans;

/**
 * Created by lishiyou on 2018/7/12 0012.
 */

public class TaskListAdapter extends BaseQuickAdapter<TaskInfo, BaseViewHolder> {

    public TaskListAdapter(@Nullable List<TaskInfo> data) {
        super(R.layout.item_task_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskInfo item) {

        ImageView ivResidentImage = helper.getView(R.id.ivResidentImage);
        TextView tvSign = helper.getView(R.id.tvSign);
        helper.addOnClickListener(R.id.tvService);
        TagFlowLayout tagLayout = helper.getView(R.id.tagLayout);
        //姓名
        helper.setText(R.id.tvResidentName, item.getPatientName());
        //未注册
        if (item.getIsRegister().equals("0")) {
            ivResidentImage.setImageResource(R.mipmap.user_portrait_circle_no_register);
        } else {
            //头像
            Glide.with(mContext)
                    .load(item.getPortrait())
                    .placeholder(R.mipmap.user_portrait_circle)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(ivResidentImage);
        }
        //是否签约
        if (item.getIsBuy().equals("1")) {
            tvSign.setVisibility(View.VISIBLE);
        } else {
            tvSign.setVisibility(View.INVISIBLE);
        }
        //任务状态
        if (item.getTaskType().equals("7")) {
            helper.setText(R.id.tvTaskStatus, "（待建档）");
        } else {
            helper.setText(R.id.tvTaskStatus, "（" + DataServer.getTaskType(Integer.valueOf(item.getTaskStatus())) + "）");
        }
        //标签 最多显示6个
        tagLayout.removeAllViews();
        if (item.getLabelArray() != null && item.getLabelArray().length > 0) {
            List<String> list = new ArrayList<>();
            for (String s : item.getLabelArray()) {
                list.add(s);
                if (list.size() == 6)
                    break;
            }
            tagLayout.setAdapter(new TagListAdapter(mContext, list));
        }
        //测量数据
        if (item.getTaskType().equals("7")) {
            helper.setText(R.id.tvMeasureData, "预签约，需要完善公卫档案");
        } else {
            List<MedicalData> list = item.getCheckDataOuts();
            SpannableStringBuilder spans = new SpannableStringBuilder();
            for (MedicalData bean : list) {
                if (MedicalConstant.FILE_PATH.equals(bean.getCheckTypeCode())
                        || MedicalConstant.SAMPLE.equals(bean.getCheckTypeCode())
                        || MedicalConstant.P05.equals(bean.getCheckTypeCode())
                        || MedicalConstant.N05.equals(bean.getCheckTypeCode())
                        || MedicalConstant.DURATION.equals(bean.getCheckTypeCode())
                        || MedicalConstant.WAVE_SPEED.equals(bean.getCheckTypeCode())
                        || MedicalConstant.WAVE_GAIN.equals(bean.getCheckTypeCode())
                        || MedicalConstant.CHECK_RESULT.equals(bean.getCheckTypeCode())
                        || MedicalConstant.RESPRR.equals(bean.getCheckTypeCode()))
                    continue;
                StringBuilder builder = new StringBuilder();
                builder.append(bean.getCheckTypeName()).append("：").append(bean.getValue()).append(bean.getUnit()).append("  \r  ");
                Spans span;
                if (bean.getStatus().equals("1") || bean.getStatus().equals("2")) {
                    span = Spans.builder().text(builder, 14, mContext.getResources().getColor(R.color.red)).build();
                } else {
                    span = Spans.builder().text(builder, 14, mContext.getResources().getColor(R.color.black6)).build();
                }
                spans.append(span);
            }
            if (list.size() == 0) {
                helper.setText(R.id.tvMeasureData, "暂无测量数据");
            } else {
                helper.setText(R.id.tvMeasureData, spans);
            }
        }
        //点击事件
//        helper.addOnClickListener(R.id.tvService);
    }
}
