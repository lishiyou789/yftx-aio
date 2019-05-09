package com.konsung.util;

import android.view.View;
import android.widget.ImageView;

import com.konsung.R;

/**
 * 描述 报警工具类
 */
public class AlarmUtil {
    /**
     * 执行超限报警
     *
     * @param value     测量值
     * @param high      报警高限
     * @param low       报警低限
     * @param imageView 超限报警标志控件
     */
    public static void executeOverrunAlarm(float value, float high, float low,
                                           ImageView imageView) {
        if (imageView == null) {
            return;
        }
        //超过最高值
        if (value == -100f) {
            imageView.setImageResource(R.drawable.alarm_high);
            imageView.setVisibility(View.VISIBLE);
        } else if (value == -10f) {
            //超过最低值
            imageView.setImageResource(R.drawable.alarm_low);
            imageView.setVisibility(View.VISIBLE);
        } else if (value > high) {
            imageView.setImageResource(R.drawable.alarm_high);
            imageView.setVisibility(View.VISIBLE);
        } else if (value < low) {
            imageView.setImageResource(R.drawable.alarm_low);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 执行超限报警
     *
     * @param value     测量值
     * @param high      报警高限
     * @param low       报警低限
     * @param imageView 超限报警标志控件
     */
    public static void executeOverrunAlarm(int value, int high, int low,
                                           ImageView imageView) {
        if (imageView == null) {
            return;
        }
        if (value > high) {
            imageView.setImageResource(R.drawable.alarm_high);
            imageView.setVisibility(View.VISIBLE);
        } else if (value < low) {
            imageView.setImageResource(R.drawable.alarm_low);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}
