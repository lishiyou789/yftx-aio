package com.health2world.aio.app.setting;

import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.bean.MeasureItem;
import com.konsung.listen.Measure;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * Created by lishiyou on 2018/7/20 0020.
 */

public class MeasureSettingAdapter extends BaseQuickAdapter<MeasureItem, BaseViewHolder> {

    public MeasureSettingAdapter(@Nullable List<MeasureItem> data) {
        super(R.layout.item_measure_setting, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeasureItem item) {
        TextView tvItemName = helper.getView(R.id.tvItemName);
        CheckBox checkBox = helper.getView(R.id.checkBox);
        tvItemName.setText(item.getType().getName());
        if (item.getType() == Measure.SAA)
            tvItemName.setTextSize(11f);
        else
            tvItemName.setTextSize(14f);

        checkBox.setEnabled(false);
        if (item.isMeasured())
            checkBox.setButtonDrawable(R.mipmap.check_box_select_enable);
        else
            checkBox.setButtonDrawable(R.mipmap.check_box_unselect);

//        if (AppConfig.isDebug) {
//            if (item.isMeasured())
//                checkBox.setChecked(true);
//            else
//                checkBox.setChecked(false);
//        } else {
//            checkBox.setEnabled(false);
//            if (item.isMeasured())
//                checkBox.setButtonDrawable(R.mipmap.check_box_select_enable);
//            else
//                checkBox.setButtonDrawable(R.mipmap.check_box_unselect);
//        }

    }
}
