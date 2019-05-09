package com.health2world.aio.app.history.data;

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

public class HistoryDataAdapter extends BaseQuickAdapter<HistoryData, BaseViewHolder> {

    public HistoryDataAdapter(@Nullable List<HistoryData> data) {
        super(R.layout.item_history_data, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryData item) {
        helper.setVisible(R.id.tvMeasureDate, false);
        helper.setVisible(R.id.llDataLayout, true);

        List<MedicalData> list = item.getCheckDataOuts();

        if (MedicalConstant.BLOOD_SUGAR.equals(item.getCheckKindCode())) {
            String title = "";
            if (list != null && list.size() > 0) {
                title = list.get(0).getCheckTypeName();
            }
            helper.setText(R.id.tvName, title);
        } else {
            helper.setText(R.id.tvName, item.getCheckKindName());
        }

        helper.setText(R.id.tvMeasureTime, DateUtil.getDate(new Date(item.getTimestamp())));

        //血脂 尿常规 白细胞 糖化血红蛋白
        if (MedicalConstant.BLOOD_FAT.equals(item.getCheckKindCode())
                || MedicalConstant.URINE.equals(item.getCheckKindCode())
                || MedicalConstant.WBC.equals(item.getCheckKindCode())
                || MedicalConstant.INFLAMMATION.equals(item.getCheckKindCode())
                || MedicalConstant.MYOCARDIUMCARDIAC.equals(item.getCheckKindCode())
                || MedicalConstant.GHB.equals(item.getCheckKindCode())) {
            int unusualCount = 0;
            for (MedicalData data : list) {
                if (!data.getStatus().equals("0"))
                    unusualCount++;
            }
            if (unusualCount == 0) {
                helper.setText(R.id.tvResult, mContext.getString(R.string.see_measure_data));
                helper.setTextColor(R.id.tvResult, mContext.getResources().getColor(R.color.appThemeColor));
            } else {
                helper.setText(R.id.tvResult, unusualCount + "项数据异常>");
                helper.setTextColor(R.id.tvResult, mContext.getResources().getColor(R.color.red));
            }
        } else if (MedicalConstant.LEAD_ECG.equals(item.getCheckKindCode())) {
            //心电
            helper.setText(R.id.tvResult, mContext.getString(R.string.see_ecg_data));
            helper.setTextColor(R.id.tvResult, mContext.getResources().getColor(R.color.appThemeColor));
        } else {
            SpannableStringBuilder spans = new SpannableStringBuilder();
            for (MedicalData data : list) {
                StringBuilder builder = new StringBuilder();
                builder.append(data.getValue()).append(data.getUnit()).append("\r  ");
                Spans span;
                //status: 1 == "abnormal"   2 == "normal"
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
