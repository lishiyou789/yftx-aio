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

/**
 * 图表管理创建的类
 * Created by lishiyou on 2017/8/2 0002.
 */

public class GluChartManager {

    public static void initLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                     ArrayList<Entry> yValue, ArrayList<Integer> listColors) {

        initChartStyle(mLineChart, xValues);

        //设置折线的样式
        LineDataSet dataSet = new LineDataSet(yValue, "血糖趋势图");
        dataSet.setColor(context.getResources().getColor(R.color.appThemeColor));
        dataSet.setCircleColor(context.getResources().getColor(R.color.appThemeColor));
        //线上显示值
        dataSet.setDrawValues(true);
        dataSet.setLineWidth(1f);

        //底部填充颜色
//        if (Utils.getSDKInt() >= 18) {
//            // fill drawable only supported on api level 18 and above
//            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.shape_chart_normal_bg);
//            dataSet.setFillDrawable(drawable);
//        } else {
//            dataSet.setFillColor(Color.GRAY);
//        }
//        dataSet.setDrawFilled(true);
        //虚线
//        dataSet.enableDashedLine(5f, 5f, 0f);

        dataSet.setValueFormatter(new GluValueFormatter());

        //圆圈
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(context.getResources().getColor(R.color.blue));
        dataSet.setDrawCircles(true);
        dataSet.setCircleColors(listColors);

        //贝瑟尔曲线
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

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
        //缩放
        mLineChart.setScaleEnabled(true);
        mLineChart.fitScreen();
        mLineChart.getDescription().setText("");
        mLineChart.setNoDataText("暂无图表数据");
        mLineChart.setScaleMinima((xValues.size() / 12) * 2, 1f);
        mLineChart.setNoDataTextColor(Color.rgb(247, 189, 51));
        //图表的描述
//        mLineChart.getDescription().setText("血氧图表");

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
        xAxis.setValueFormatter(new GluXAxisValueFormatter(xValues));
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
//        yAxisLeft.setAxisMaxValue(33.3f);
//        yAxisLeft.setAxisMinValue(1.1f);
        yAxisLeft.setValueFormatter(new GluYAxisValueFormatter());
        yAxisLeft.setEnabled(true);
        mLineChart.getAxisRight().setEnabled(false);
    }

    private static class GluYAxisValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            String glu = new DecimalFormat("###.#").format(value);
            return glu + "mmol/L";
        }
    }

    private static class GluXAxisValueFormatter implements IAxisValueFormatter {

        ArrayList<String> xValues;

        public GluXAxisValueFormatter(ArrayList<String> xValues) {
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

    private static class GluValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            String glu = new DecimalFormat("###.#").format(value);
            return glu;
        }
    }

}
