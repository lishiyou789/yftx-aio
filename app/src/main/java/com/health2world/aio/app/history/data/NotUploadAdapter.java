package com.health2world.aio.app.history.data;

import android.support.annotation.Nullable;

import com.health2world.aio.R;
import com.konsung.bean.MeasureBean;

import java.text.SimpleDateFormat;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * Created by lishiyou on 2019/2/19 0019.
 */

public class NotUploadAdapter extends BaseQuickAdapter<MeasureBean, BaseViewHolder> {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public NotUploadAdapter(@Nullable List<MeasureBean> data) {
        super(R.layout.item_not_upload, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeasureBean item) {
        helper.setText(R.id.tvName, "居民姓名：" + item.getName());
        helper.setText(R.id.tvTime, "测量时间：" + format.format(item.getCheckDay()));
        if (item.isUpload()) {
            helper.setText(R.id.tvUpload, "已上传");
            helper.setTextColor(R.id.tvUpload,mContext.getResources().getColor(R.color.black9));
        } else {
            helper.setText(R.id.tvUpload, "点击上传");
            helper.setTextColor(R.id.tvUpload,mContext.getResources().getColor(R.color.appThemeColor));
        }

        helper.addOnClickListener(R.id.tvUpload);
    }
}
