package com.health2world.aio.app.history.service;

import android.support.annotation.Nullable;

import com.health2world.aio.R;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.bean.ServiceLog;
import com.health2world.aio.config.MedicalConstant;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * Created by lishiyou on 2018/8/2 0002.
 */

public class HistoryServiceAdapter extends BaseQuickAdapter<ServiceLog, BaseViewHolder> {

    public HistoryServiceAdapter(@Nullable List<ServiceLog> data) {
        super(R.layout.item_history_service, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceLog item) {

        helper.addOnClickListener(R.id.tvResult);
        helper.addOnClickListener(R.id.tvEcg);

        helper.setText(R.id.tvTitle, item.getServiceName());
        helper.setText(R.id.tvTime, item.getServiceTime());
        helper.setText(R.id.tvServiceItem, item.getServiceItemName());
        helper.setText(R.id.tvSuggest, item.getServiceResult());
        helper.setText(R.id.tvDoctor, item.getServiceDoctorName());

        if (item.getServiceType().equals("1")) {
            helper.setVisible(R.id.llSuggest, false);
            helper.setVisible(R.id.llResult, true);
        } else {
            helper.setVisible(R.id.llSuggest, true);
            helper.setVisible(R.id.llResult, false);
        }

        List<MedicalData> list = item.getCheckDataRecordMap();

        List<MedicalData> listEcg = new ArrayList<>();
        List<MedicalData> listData = new ArrayList<>();

        for (MedicalData data : list) {
            if (data.getCheckKindCode().equals(MedicalConstant.LEAD_ECG))
                listEcg.add(data);
            else
                listData.add(data);
        }

        if (listData.size() == 0) {
            helper.setText(R.id.tvResult, mContext.getString(R.string.no_measure_data));
            helper.setTextColor(R.id.tvResult, mContext.getResources().getColor(R.color.black9));
        } else {
            helper.setText(R.id.tvResult, mContext.getString(R.string.see_measure_data));
            helper.setTextColor(R.id.tvResult, mContext.getResources().getColor(R.color.appThemeColor));
        }

        if (listEcg.size() == 0) {
            helper.setVisible(R.id.tvEcg, false);
        } else {
            helper.setVisible(R.id.tvEcg, true);
        }
    }
}
