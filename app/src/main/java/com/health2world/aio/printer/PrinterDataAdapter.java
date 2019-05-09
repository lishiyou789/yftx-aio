package com.health2world.aio.printer;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.config.NormalRange;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * Created by lishiyou on 2019/1/11 0011.
 */

public class PrinterDataAdapter extends BaseQuickAdapter<PrinterData, BaseViewHolder> {

    private Typeface typeface;

    public PrinterDataAdapter(@Nullable List<PrinterData> data,Typeface pTypeface) {
        super(R.layout.item_printer_data, data);
        this.typeface = pTypeface;
    }

    @Override
    protected void convert(BaseViewHolder helper, PrinterData item) {

//        TextView tvNo = helper.getView(R.id.tvNo);
        TextView tvName = helper.getView(R.id.tvName);
        TextView tvResult = helper.getView(R.id.tvResult);
        TextView tvRange = helper.getView(R.id.tvRange);
        TextView tvUnit = helper.getView(R.id.tvUnit);

//        tvNo.setTypeface(typeface);
        tvName.setTypeface(typeface);
        tvResult.setTypeface(typeface);
        tvRange.setTypeface(typeface);
        tvUnit.setTypeface(typeface);

//        tvNo.setVisibility(View.GONE);

//        helper.setText(R.id.tvNo, String.valueOf(helper.getLayoutPosition() + 1));
        helper.setText(R.id.tvName, item.getItemName());
        helper.setText(R.id.tvResult, item.getResult());
        helper.setText(R.id.tvRange, item.getRange());
        helper.setText(R.id.tvUnit, item.getUnit());

        helper.setText(R.id.tvArray, item.getArrow());

    }
}
