package com.health2world.aio.app.history.report;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.health2world.aio.R;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.bean.MedicalReport;
import com.health2world.aio.config.MedicalConstant;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * Created by lishiyou on 2018/8/2 0002.
 */

public class HistoryReportAdapter extends BaseQuickAdapter<MedicalReport, BaseViewHolder> {

    public HistoryReportAdapter(@Nullable List<MedicalReport> data) {
        super(R.layout.item_history_report, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MedicalReport item) {

        helper.addOnClickListener(R.id.tvResult);
        helper.addOnClickListener(R.id.tvEcg);
        helper.addOnClickListener(R.id.btnPrint);

        helper.setText(R.id.tvTitle, item.getReportName());
        helper.setText(R.id.tvDoctorName, item.getDoctorName());

        if (TextUtils.isEmpty(item.getSpFeedback()))
            helper.setText(R.id.tvDescription, "暂无");
        else
            helper.setText(R.id.tvDescription, item.getSpFeedback());

        if (TextUtils.isEmpty(item.getAdviceDoctor()))
            helper.setText(R.id.tvDoctorAdvice, "暂无");
        else
            helper.setText(R.id.tvDoctorAdvice, item.getAdviceDoctor());


        List<MedicalData> list = item.getCheckDataOuts();

        List<MedicalData> listEcg = new ArrayList<>();
        List<MedicalData> listData = new ArrayList<>();

        for (MedicalData data : list) {
            if (data.getCheckKindCode().equals(MedicalConstant.LEAD_ECG))
                listEcg.add(data);
            else
                listData.add(data);
        }

        if (listData.size() == 0 && listEcg.size() == 0) {
            helper.setVisible(R.id.tvResult, true);
            helper.setVisible(R.id.tvEcg, false);
            helper.setText(R.id.tvResult, mContext.getString(R.string.no_measure_data));
            helper.setTextColor(R.id.tvResult, mContext.getResources().getColor(R.color.black9));
        } else if (listData.size() > 0 && listEcg.size() > 0) {
            helper.setVisible(R.id.tvResult, true);
            helper.setVisible(R.id.tvEcg, true);
            helper.setText(R.id.tvResult, mContext.getString(R.string.see_measure_data));
            helper.setTextColor(R.id.tvResult, mContext.getResources().getColor(R.color.appThemeColor));
        } else {
            if (listData.size() > 0) {
                helper.setVisible(R.id.tvResult, true);
                helper.setVisible(R.id.tvEcg, false);
                helper.setText(R.id.tvResult, mContext.getString(R.string.see_measure_data));
                helper.setTextColor(R.id.tvResult, mContext.getResources().getColor(R.color.appThemeColor));
            }
            if (listEcg.size() > 0) {
                helper.setVisible(R.id.tvResult, false);
                helper.setVisible(R.id.tvEcg, true);
            }
        }
    }
}
