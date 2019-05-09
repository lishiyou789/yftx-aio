package com.health2world.aio.app.clinic.result;

import android.support.annotation.Nullable;

import com.health2world.aio.R;
import com.health2world.aio.bean.MedicalData;

import java.util.List;

import aio.health2world.DataEntity;
import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * Created by lishiyou on 2018/9/26 0026.
 */

public class CommonResultAdapter extends BaseQuickAdapter<MedicalData, BaseViewHolder> {

    public CommonResultAdapter(@Nullable List<MedicalData> data) {
        super(R.layout.item_task_service, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MedicalData item) {
        helper.setVisible(R.id.detail, false);

        helper.setText(R.id.name, item.getCheckTypeName());
        helper.setText(R.id.value, item.getValue() + item.getUnit());
        if (item.getStatus().equals("0"))
            helper.setTextColor(R.id.value, mContext.getResources().getColor(R.color.black6));
        else
            helper.setTextColor(R.id.value, mContext.getResources().getColor(R.color.red));
    }
}
