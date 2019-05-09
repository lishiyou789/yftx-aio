package com.konsung.util;

/**
 * Created by DengLiXiang on 2017/1/12 0012.
 */

import android.content.Context;

import com.konsung.R;

/**
 * 是否超出范围
 */
public class RangeHelper {
    private float min;
    private float max;

    /**
     * 是否超出范围
     *
     * @return 是否超出
     */
    public boolean isHaveMin() {
        return haveMin;
    }

    /**
     * 设置当前是否超出了范围
     *
     * @param haveMin 是否超出
     */
    public void setHaveMin(boolean haveMin) {
        this.haveMin = haveMin;
    }

    private boolean haveMin = true; //默认包含最小范围
    private String toastMsg;

    /**
     * 构造方法
     *
     * @param min     最小值
     * @param max     最大值
     * @param haveMin 是否
     * @param field   判断的值
     * @param context 上下文
     */
    public RangeHelper(float min, float max, boolean haveMin, String field, Context context) {
        this.min = min;
        this.max = max;
        this.haveMin = haveMin;

        String sMin = min + "";
        if (sMin.endsWith(context.getString(R.string.t_end0))) {
            sMin = sMin.replace(context.getString(R.string.t_end0), "");
        }
        String sMax = max + "";
        if (sMax.endsWith(context.getString(R.string.t_end0))) {
            sMax = sMax.replace(context.getString(R.string.t_end0), "");
        }

        String tMin = "";
        if (haveMin) {
            tMin = context.getString(R.string.t_include) + sMin;
        } else {
            tMin = context.getString(R.string.t_not_include) + sMin;
        }
        toastMsg = field + context.getString(R.string.c_colon) + context.getString(R.string
                .t_valid_range) +
                sMin + context.getString(R.string.c_and) + sMax + tMin + context.getString(R
                .string.c_r_bracket);
    }

    /**
     * 构造方法
     *
     * @param min        最小值
     * @param max        最大值
     * @param haveMin    是否
     * @param fieldResId 判断的值
     * @param context    上下文
     */
    public RangeHelper(float min, float max, boolean haveMin, int fieldResId, Context context) {
        this(min, max, haveMin, context.getString(fieldResId), context);
    }

    /**
     * 构造方法
     *
     * @param min     最小值
     * @param max     最大值
     * @param field   判断的值
     * @param context 上下文
     */
    public RangeHelper(float min, float max, String field, Context context) {
        this.min = min;
        this.max = max;
        String tMin = "";

        String sMin = min + "";
        if (sMin.endsWith(context.getString(R.string.t_end0))) {
            sMin = sMin.replace(context.getString(R.string.t_end0), "");
        }
        String sMax = max + "";
        if (sMax.endsWith(context.getString(R.string.t_end0))) {
            sMax = sMax.replace(context.getString(R.string.t_end0), "");
        }

        if (haveMin) {
            tMin = context.getString(R.string.t_include) + sMin;
        } else {
            tMin = context.getString(R.string.t_not_include) + sMin;
        }

        toastMsg = field + context.getString(R.string.c_colon) + context.getString(R.
                string.t_valid_range) + sMin + context.getString(R.string.c_and) +
                sMax + tMin + context.getString(R.string.c_r_bracket);
    }

    /**
     * 构造方法
     *
     * @param min     最小值
     * @param haveMin 最大值
     * @param field   判断的值
     * @param context 上下文
     */
    public RangeHelper(float min, boolean haveMin, String field, Context context) {
        this.min = min;
        this.max = -1;
        this.haveMin = haveMin;

        String sMin = min + "";
        if (sMin.endsWith(context.getString(R.string.t_end0))) {
            sMin = sMin.replace(context.getString(R.string.t_end0), "");
        }

        if (haveMin) {
            toastMsg = field + context.getString(R.string.c_colon) + context.getString(R.string
                    .t_valid_range_1) +
                    sMin + context.getString(R.string.c_r_bracket);
        } else {
            toastMsg = field + context.getString(R.string.c_colon) + context.getString(R.string
                    .t_valid_range_2) +
                    min + context.getString(R.string.c_r_bracket);
        }
    }

    /**
     * 获取最小值
     *
     * @return 最小值
     */
    public float getMin() {
        return min;
    }

    /**
     * 设置最小值
     *
     * @param min 最小值
     */
    public void setMin(float min) {
        this.min = min;
    }

    /**
     * 获取最大值
     *
     * @return 最大值
     */
    public float getMax() {
        return max;
    }

    /**
     * 设置最大值
     *
     * @param max 最大值
     */
    public void setMax(float max) {
        this.max = max;
    }

    /**
     * 获取提示的语句
     *
     * @return 提示的语句
     */
    public String getToastMsg() {
        return toastMsg;
    }

    /**
     * 设置提示的语句
     *
     * @param toastMsg 提示的语句
     */
    public void setToastMsg(String toastMsg) {
        this.toastMsg = toastMsg;
    }
}
