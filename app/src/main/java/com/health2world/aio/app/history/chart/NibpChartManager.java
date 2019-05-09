package com.health2world.aio.app.history.chart;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.health2world.aio.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 图表管理创建的类
 * Created by lishiyou on 2017/8/2 0002.
 */

public class NibpChartManager {

    public static void initLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                     ArrayList<Entry> yValue1, ArrayList<Entry> yValue2, List<Integer> listColors1,
                                     List<Integer> listColors2) {
        initChartStyle(mLineChart, xValues);

        //设置折线1的样式
        LineDataSet dataSet1 = new LineDataSet(yValue1, "收缩压");
        dataSet1.setColor(context.getResources().getColor(R.color.blue));
        dataSet1.setCircleColor(context.getResources().getColor(R.color.blue));
        //线上显示值
        dataSet1.setDrawValues(true);
        dataSet1.setLineWidth(1f);

//        //底部填充颜色
//        if (Utils.getSDKInt() >= 18) {
//            // fill drawable only supported on api level 18 and above
//            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.shape_chart_normal_bg);
//            dataSet1.setFillDrawable(drawable);
//        } else {
//            dataSet1.setFillColor(Color.GRAY);
//        }
//        dataSet1.setDrawFilled(true);
        //虚线
//        dataSet1.enableDashedLine(5f, 5f, 0f);
        dataSet1.setValueFormatter(new NibpValueFormatter());
        //圆圈
        dataSet1.setDrawCircleHole(true);
        dataSet1.setCircleRadius(5f);
        dataSet1.setValueTextSize(12f);
        dataSet1.setValueTextColor(context.getResources().getColor(R.color.black3));
        dataSet1.setDrawCircles(true);
        //贝瑟尔曲线
        dataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet1.setCircleColors(listColors1);
        //--------------------------------------------------------------------------------------------------------------------
        //设置折线2的样式
        LineDataSet dataSet2 = new LineDataSet(yValue2, "舒张压");
        dataSet2.setColor(context.getResources().getColor(R.color.appThemeColor));
        dataSet2.setCircleColor(context.getResources().getColor(R.color.appThemeColor));
        //线上显示值
        dataSet2.setDrawValues(true);
        dataSet2.setLineWidth(1f);
//        //底部填充颜色
//        if (Utils.getSDKInt() >= 18) {
//            // fill drawable only supported on api level 18 and above
//            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.shape_chart_white_bg);
//            dataSet2.setFillDrawable(drawable);
//        } else {
//            dataSet2.setFillColor(Color.GRAY);
//        }
//        dataSet2.setDrawFilled(true);
        //虚线
//        dataSet2.enableDashedLine(5f, 5f, 0f);
        dataSet2.setValueFormatter(new NibpValueFormatter());
        //圆圈
        dataSet2.setDrawCircleHole(true);
        dataSet2.setCircleRadius(5f);
        dataSet2.setValueTextSize(12f);
        dataSet2.setValueTextColor(context.getResources().getColor(R.color.black3));
        dataSet2.setDrawCircles(true);
        //贝瑟尔曲线
        dataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet2.setCircleColors(listColors2);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(dataSets);
        //将数据插入
        mLineChart.setData(lineData);

        //设置动画效果
//        mLineChart.animateX(3000);

        //设置折线的描述的样式（默认在图表的左下角）
        Legend l = mLineChart.getLegend();
        l.setForm(Legend.LegendForm.EMPTY);
        l.setTextColor(context.getResources().getColor(R.color.transparent));
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        mLineChart.invalidate();
    }


    private static void initChartStyle(LineChart mLineChart, ArrayList<String> xValues) {
        //设置图表是否支持触控操作
        mLineChart.setTouchEnabled(true);

        mLineChart.setNoDataText("暂无图表数据");
        mLineChart.getDescription().setText("");
        mLineChart.setNoDataTextColor(Color.rgb(247, 189, 51));
        //缩放
        mLineChart.setScaleEnabled(true);
        mLineChart.fitScreen();
        //图表的描述
        mLineChart.getDescription().setText("");
        mLineChart.setScaleMinima((xValues.size() / 12) * 2, 1f);
        //设置点击折线点时，显示其数值
//        MyMakerView mv = new MyMakerView(context, R.layout.item_mark_layout);
//        mLineChart.setMarkerView(mv);

        //设置x轴的样式
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.GRAY);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisLineWidth(1f);
        xAxis.enableAxisLineDashedLine(5f, 5f, 0f);
        xAxis.enableGridDashedLine(5f, 5f, 0f);
        xAxis.setValueFormatter(new NibpXAxisValueFormatter(xValues));
        //设置是否显示x轴
        xAxis.setEnabled(true);

        //设置左边y轴的样式
        YAxis yAxisLeft = mLineChart.getAxisLeft();
        yAxisLeft.setAxisLineColor(Color.GRAY);
        yAxisLeft.setGranularity(1f);
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setAxisLineWidth(1f);
        yAxisLeft.enableAxisLineDashedLine(5f, 5f, 0f);
        yAxisLeft.enableGridDashedLine(5f, 5f, 0f);
        yAxisLeft.setValueFormatter(new YAxisValueFormatter());
        yAxisLeft.setEnabled(true);

//        LimitLine line1 = new LimitLine(60);
//        line1.setLineWidth(0.5f);
//        line1.setLabel("偏低线");
//        line1.enableDashedLine(5f, 5f, 0f);
//        line1.setLineColor(Color.RED);
//        line1.setTextStyle(Paint.Style.FILL);
//        line1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//
//        LimitLine line2 = new LimitLine(140);
//        line2.setLineWidth(0.5f);
//        line2.setLabel("偏高线");
//        line2.setTextStyle(Paint.Style.FILL);
//        line2.enableDashedLine(5f, 5f, 0f);
//        line2.setLineColor(Color.RED);
//        line2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        yAxisLeft.addLimitLine(line1);
//        yAxisLeft.addLimitLine(line2);

        //设置右边y轴的样式
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }

    private static class NibpXAxisValueFormatter implements IAxisValueFormatter {

        ArrayList<String> xValues;

        public NibpXAxisValueFormatter(ArrayList<String> xValues) {
            this.xValues = xValues;
        }

        @Override
        public String getFormattedValue(float v, AxisBase axisBase) {
            int index = (int) v;
            if (index >= xValues.size() || index < 0) {
                return "";
            } else {
                return xValues.get(index);
            }
        }
    }

    private static class NibpValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            String nibp = new DecimalFormat("###.#").format(value);
            return nibp;
        }
    }

    private static class YAxisValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            String yValue = new DecimalFormat("###.#").format(value);
            return yValue + "mmHg";
        }
    }
}
