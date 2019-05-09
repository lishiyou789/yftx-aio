package com.health2world.aio.app.history.chart;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;

import com.health2world.aio.R;
import com.health2world.aio.bean.HistoryData;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.config.MedicalConstant;

import java.util.Date;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;
import aio.health2world.spanbuilder.Spans;
import aio.health2world.utils.DateUtil;

/**
 * Created by lishiyou on 2018/8/2 0002.
 */

public class ChartDataAdapter extends BaseQuickAdapter<HistoryData, BaseViewHolder> {


    public ChartDataAdapter(@Nullable List<HistoryData> data) {
        super(R.layout.item_history_data, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryData item) {

        helper.setVisible(R.id.tvMeasureDate, false);
        helper.setVisible(R.id.llDataLayout, true);

        if (helper.getLayoutPosition() % 2 == 0)
            helper.setBackgroundColor(R.id.item_history, mContext.getResources().getColor(R.color.gray_light));
        else
            helper.setBackgroundColor(R.id.item_history, mContext.getResources().getColor(R.color.white));


        helper.setText(R.id.tvName, item.getCheckKindName());

        helper.setText(R.id.tvMeasureTime, DateUtil.getDate1(new Date(item.getTimestamp())));

        List<MedicalData> list = item.getCheckDataOuts();

        //血脂 尿常规 白细胞 糖化血红蛋白
        if (item.getCheckKindCode().equals(MedicalConstant.BLOOD_FAT)
                || item.getCheckKindCode().equals(MedicalConstant.URINE)
                || item.getCheckKindCode().equals(MedicalConstant.WBC)
                || item.getCheckKindCode().equals(MedicalConstant.GHB)) {
            int unusualCount = 0;
            for (MedicalData data : list) {
                if (!data.getStatus().equals("0"))
                    unusualCount++;
            }
            helper.setText(R.id.tvResult, unusualCount + "项异常>");
            helper.setTextColor(R.id.tvResult, mContext.getResources().getColor(R.color.appThemeColor));
        } else if (item.getCheckKindCode().equals(MedicalConstant.LEAD_ECG)) {
            //心电
            helper.setText(R.id.tvResult,mContext.getString(R.string.see_ecg_data));
            helper.setTextColor(R.id.tvResult, mContext.getResources().getColor(R.color.appThemeColor));
        } else {
            SpannableStringBuilder spans = new SpannableStringBuilder();
            for (MedicalData data : list) {
                StringBuilder builder = new StringBuilder();
                builder.append(data.getValue()).append(data.getUnit()).append("\r  ");
                Spans span;
                if (data.getStatus().equals("1") || data.getStatus().equals("2")) {
                    span = Spans.builder().text(builder, 14, mContext.getResources().getColor(R.color.red)).build();
                } else {
                    span = Spans.builder().text(builder, 14, mContext.getResources().getColor(R.color.black6)).build();
                }
                spans.append(span);
            }
            helper.setText(R.id.tvResult, spans);
        }

    }
}
