package com.health2world.aio.app.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.health2world.aio.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * 标签适配器
 * Created by lishiyou on 2018/7/16 0016.
 */

public class TagListAdapter extends TagAdapter<String> {

    private Context mContext;

    public TagListAdapter(Context context, List<String> data) {
        super(data);
        mContext = context;
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        TextView textView = new TextView(mContext);
        textView.setText(s);
        textView.setTextSize(10f);
        textView.setPadding(4, 1, 4, 1);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(mContext.getResources().getColor(R.color.white));
        textView.setBackgroundResource(R.drawable.shape_tag_text_bg);
        return textView;
    }
}
