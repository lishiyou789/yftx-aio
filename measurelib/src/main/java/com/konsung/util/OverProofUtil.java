package com.konsung.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.konsung.R;

/**
 * Created by lipengjie on 2016/11/30 0030.
 */
public class OverProofUtil implements TextWatcher {
    private float min;
    private float max;
    private TextView view;
    private Context context;

    /**
     * 构造方法
     *
     * @param min     参考值最小值
     * @param max     参考值最大值
     * @param view    TextView控件
     * @param context 上下文
     */
    public OverProofUtil(float min, float max, TextView view, Context context) {
        this.min = min;
        this.max = max;
        this.view = view;
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String s = editable.toString();
        if (s.contains(">") || s.contains("<")) {
            view.setTextColor(context.getResources().getColor(R.color.red));
        } else if (!s.equals(context.getString(R.string.default_value)) && s.length() > 0) {
            try {
                Float aFloat;
                aFloat = Float.valueOf(s);
                if (aFloat < min || aFloat > max) {
                    view.setTextColor(context.getResources().getColor(R.color.red));
                } else {

                    view.setTextColor(context.getResources().getColor(R.color
                            .mesu_text));
                }
            } catch (Exception e) {
                view.setTextColor(context.getResources().getColor(R.color
                        .mesu_text));
            }
        } else {

            view.setTextColor(context.getResources().getColor(R.color
                    .mesu_text));
        }
    }
}
