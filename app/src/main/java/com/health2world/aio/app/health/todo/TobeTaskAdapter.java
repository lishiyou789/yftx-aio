package com.health2world.aio.app.health.todo;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;

import com.health2world.aio.R;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.bean.TaskInfo;
import com.health2world.aio.common.DataServer;
import com.health2world.aio.config.MedicalConstant;

import java.util.Date;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;
import aio.health2world.spanbuilder.Spans;
import aio.health2world.utils.DateUtil;

/**
 * Created by lishiyou on 2018/8/6 0006.
 */

public class TobeTaskAdapter extends BaseQuickAdapter<TaskInfo, BaseViewHolder> {

    public TobeTaskAdapter(@Nullable List<TaskInfo> data) {
        super(R.layout.item_wait_task, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskInfo item) {
        //任务状态
        helper.setText(R.id.tv_task_type, DataServer.getTaskType(Integer.valueOf(item.getTaskStatus())));
        try {
            helper.setText(R.id.tv_time, DateUtil.getDate1(new Date(item.getTaskTime())));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        List<MedicalData> medical = item.getCheckDataOuts();
        SpannableStringBuilder spans = new SpannableStringBuilder();
        if (medical != null && medical.size() > 0) {
            for (int i = 0; i < medical.size(); i++) {
                MedicalData data = medical.get(i);
                if (MedicalConstant.FILE_PATH.equals(data.getCheckTypeCode())
                        || MedicalConstant.SAMPLE.equals(data.getCheckTypeCode())
                        || MedicalConstant.P05.equals(data.getCheckTypeCode())
                        || MedicalConstant.N05.equals(data.getCheckTypeCode())
                        || MedicalConstant.DURATION.equals(data.getCheckTypeCode())
                        || MedicalConstant.WAVE_SPEED.equals(data.getCheckTypeCode())
                        || MedicalConstant.WAVE_GAIN.equals(data.getCheckTypeCode())
                        || MedicalConstant.CHECK_RESULT.equals(data.getCheckTypeCode())
                        || MedicalConstant.RESPRR.equals(data.getCheckTypeCode()))
                    continue;
                StringBuilder tempBuilder = new StringBuilder();
                tempBuilder.append(data.getCheckTypeName())
                        .append("：")
                        .append(data.getValue())
                        .append(data.getUnit())
                        .append('\n');
                Spans span;
                if (data.getStatus().equals("1") || data.getStatus().equals("2")) {
                    span = Spans.builder().text(tempBuilder, 14, mContext.getResources().getColor(R.color.red)).build();
                } else {
                    span = Spans.builder().text(tempBuilder, 14, R.color.black6).build();
                }
                spans.append(span);
            }
            helper.setText(R.id.tv_measure_data, spans);
        } else {
            helper.setText(R.id.tv_measure_data, mContext.getString(R.string.no_measure_data));
        }

        helper.addOnClickListener(R.id.tvService);
    }

}