package com.health2world.aio.app.health.termination;

import android.support.annotation.Nullable;
import android.widget.CheckBox;

import com.health2world.aio.R;
import com.health2world.aio.bean.SignService;
import com.konsung.bean.ResidentBean;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * Created by lishiyou on 2018/8/20 0020.
 */

public class TerminationAdapter extends BaseQuickAdapter<SignService, BaseViewHolder> {

    private ResidentBean resident;

    public TerminationAdapter(@Nullable List<SignService> data, ResidentBean resident) {
        super(R.layout.item_termination, data);
        this.resident = resident;
    }

    @Override
    protected void convert(BaseViewHolder helper, SignService item) {
        helper.setText(R.id.tvResidentName, resident.getName());
        helper.setText(R.id.tvServiceName, item.getServiceName());
        helper.setText(R.id.tvTime, item.getEndTime());
        ((CheckBox) helper.getView(R.id.checkBox)).setChecked(item.isChecked());
    }
}
