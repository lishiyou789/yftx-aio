package com.health2world.aio.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.health2world.aio.R;
import com.health2world.aio.bean.TagInfo;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/7/17 0017.
 */

public class ResidentTagListAdapter extends TagAdapter<TagInfo> {
    private Context mContext;
    LayoutInflater mInflater;

    public ResidentTagListAdapter(Context context, List<TagInfo> datas) {
        super(datas);
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(FlowLayout parent, final int position, final TagInfo tagInfoBean) {
        View view = mInflater.inflate(R.layout.item_add_resident_taginfo, parent, false);
        CheckBox checkBox = view.findViewById(R.id.cb_add_resident_taginfo);
        checkBox.setText(tagInfoBean.getName());
        checkBox.setChecked(tagInfoBean.isChecked());
        return view;
    }


}
