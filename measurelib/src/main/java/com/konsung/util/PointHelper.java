package com.konsung.util;

import android.content.Context;

import com.konsung.R;

/**
 * Created by DengLiXiang on 2017/1/12 0012.
 */

public class PointHelper {
    public int pointBefore;
    public int pointAfter;
    public String toastMsg;

    /**
     * 构造方法
     *
     * @param pointBefore 截取之前的内容
     * @param pointAfter  截取之后的内容
     * @param context     上下文
     */
    public PointHelper(int pointBefore, int pointAfter, Context context) {
        this.pointBefore = pointBefore;
        this.pointAfter = pointAfter;
        toastMsg = context.getString(R.string.t_point_len1) + pointBefore
                + context.getString(R.string.t_point_len3) + context.getString(R.string
                .t_point_len2)
                + pointAfter + context.getString(R.string.t_point_len3);
    }
}
