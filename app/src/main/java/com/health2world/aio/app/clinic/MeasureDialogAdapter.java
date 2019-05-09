package com.health2world.aio.app.clinic;

import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.bean.MeasureItem;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * @author Administrator
 * @date 2019/2/13/0013.
 */

public class MeasureDialogAdapter extends BaseQuickAdapter<MeasureItem, BaseViewHolder> {


    public MeasureDialogAdapter(List<MeasureItem> data) {
        super(R.layout.item_dialog_measure_data, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeasureItem item) {
        TextView tvName = helper.getView(R.id.tvName);
        TextView tvValue = helper.getView(R.id.tvValue);
        TextView tvTime = helper.getView(R.id.tvTime);

        tvName.setText(item.getType().getName());
        tvValue.setText(item.getExtraValue());
        tvTime.setText(new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()));
    }
}
