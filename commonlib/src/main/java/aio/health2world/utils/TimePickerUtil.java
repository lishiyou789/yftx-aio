package aio.health2world.utils;

import android.content.Context;


import java.util.Calendar;

import aio.health2world.library.R;
import aio.health2world.pickeview.OptionsPickerView;
import aio.health2world.pickeview.TimePickerView;
import aio.health2world.pickeview.lib.WheelView;

/**
 * Created by lishiyou on 2017/7/24 0024.
 */

public class TimePickerUtil {

    public static TimePickerView init(Context context, TimePickerView.OnTimeSelectListener listener) {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(startDate.get(Calendar.YEAR) - 2, 0, 31);
        Calendar endDate = Calendar.getInstance();
        endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
        //时间选择器
        TimePickerView pvTime = new TimePickerView
                .Builder(context, listener)
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
                .isCenterLabel(false)
                .isDialog(true)
                .setTitleSize(22)
                .setContentSize(22)
                .setCancelText("取消")
                .setSubmitText("确定")
                .setTitleColor(context.getResources().getColor(R.color.appThemeColor))
                .setSubmitColor(context.getResources().getColor(R.color.appThemeColor))
                .setDividerColor(context.getResources().getColor(R.color.appThemeColor))
                .setTextColorCenter(context.getResources().getColor(R.color.appThemeColor))
                .setDividerType(WheelView.DividerType.WRAP)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .build();
        return pvTime;
    }

    public static OptionsPickerView initOptions(Context context, OptionsPickerView.OnOptionsSelectListener listener) {
        OptionsPickerView pickerView = new OptionsPickerView.Builder(context, listener)
                .isDialog(true)
                .setTitleSize(20)
                .setContentTextSize(20)
                .setCancelText("取消")
                .setSubmitText("确定")
                .setOutSideCancelable(false)
                .setTitleColor(context.getResources().getColor(R.color.appThemeColor))
                .setSubmitColor(context.getResources().getColor(R.color.appThemeColor))
                .setDividerColor(context.getResources().getColor(R.color.appThemeColor))
                .setTextColorCenter(context.getResources().getColor(R.color.appThemeColor))
                .setDividerType(WheelView.DividerType.FILL)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .build();
        return pickerView;

    }


}
