package com.health2world.aio.app.clinic.result;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Base64;
import java.util.List;

/**
 * 心电报告绘制
 */

public class EcgReportDrawable extends Drawable {
    private final float zoom = 0.8f; //波形缩放倍数
    private final float pixelPerMm = (float) (1280 / 216.576 * zoom); //每毫米像素数

    private int height; // 高
    private int width; //宽
    private List<String> ecgList;
    private Handler handler = new Handler();
    private Paint paint = new Paint();
    private Canvas canvas;
    private final float waveWidgetHeight = 94.6f; //为了适应心电报告页面，单条波形高度默认设置为4 * 5 * pixelPerMm = 94.6px

    /**
     * 构造器
     * @param list 数据
     * @param height 画布高度
     * @param width 画布宽度
     */
    public EcgReportDrawable(List<String> list, int height, int width) {
        this.height = height;
        this.width = width;
        this.ecgList = list;
    }

    @Override
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        if (canvas == null)
            this.canvas = new Canvas();

//        new Thread(drawLineRunnable).start();
        handler.post(drawLineRunnable);
        handler.post(drawTitleRunnable);
        handler.post(drawWaveRunnable);
    }

    public void drawDone() {
        this.handler = null;
        this.canvas = null;
        this.ecgList = null;
    }

    @Override
    public void setAlpha(int alpha) { }

    @Override
    public void setColorFilter(ColorFilter cf) { }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    private final Runnable drawLineRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                drawLine(canvas, width, height);
                drawPoint(canvas, width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private final Runnable drawTitleRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                drawDottedCurve(canvas, width);
                drawRuler(canvas, width);
                drawTitle(canvas, width);
                drawWaveData(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private final Runnable drawWaveRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                drawWave(canvas, width);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 绘制网格线
     * @param canvas 画布
     * @param width 宽
     * @param height 高
     */
    private void drawLine(Canvas canvas, int width, int height) {
        // 设置画笔
        paint.reset();
        paint.setColor(0xedb2b2);
        paint.setTextSize(14);
        paint.setAlpha(150);
        paint.setAntiAlias(true);

        // 单位:像素/mm,1280为屏幕横向像素数，216.576为屏幕横向长度（单位mm）
        int widthLen = (int) (width / pixelPerMm); //转换为mm长度
        int heightLen = (int) (height / pixelPerMm); //转换为mm长度

        float startX;
        float startY;
        float stopX;
        float stopY;
        // 画X轴,每隔5mm画一条线
        paint.setStrokeWidth(2.0f);
        startX = 0;
        stopX = width;
        for (int i = 0; i < heightLen; i++) {
            startY = i * pixelPerMm * 5;
            stopY = i * pixelPerMm * 5;
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        // 画Y轴,每隔5mm画一条线
        paint.setStrokeWidth(2.0f);
        startY = 0;
        stopY = height;
        for (int i = 0; i < widthLen; i++) {
            startX = i * pixelPerMm * 5;
            stopX = i * pixelPerMm * 5;
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
        paint.reset();
    }

    /**
     * 绘制格子里的点
     * @param canvas 画布
     * @param width 宽
     * @param height 高
     */
    private void drawPoint(Canvas canvas, int width, int height) {
        // 设置画笔
        paint.reset();
        paint.setColor(0xedb2b2);
        paint.setTextSize(14);
        paint.setAlpha(150);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        // 单位:像素/mm,1280为屏幕横向像素数，216.576为屏幕横向长度（单位mm）
        int widthLen = (int) (width / pixelPerMm); //转换为mm长度
        int heightLen = (int) (height / pixelPerMm); //转换为mm长度
        float x;
        float y;
        float gap = pixelPerMm;
        float radius = (float)0.25 * pixelPerMm;

        for (int a = 0; a < heightLen; a++) {
            if (a%5 == 0) {
                continue;
            }
            y = a * gap;
            for (int b = 0; b < widthLen; b++) {
                if (b%5 == 0) {
                    continue;
                }
                x = b * gap;
                canvas.drawCircle(x, y, radius, paint);
            }
        }

    }

    /**
     * 绘制分割线 虚线
     * @param canvas 画布
     * @param width 宽
     */
    private void drawDottedCurve(Canvas canvas, int width) {
        float waveWidgetWidth = (float) width / 2 - 2;
        // 设置画笔
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.moveTo(waveWidgetWidth, 0);
        path.lineTo(waveWidgetWidth, waveWidgetHeight * 6);
        PathEffect effects = new DashPathEffect(new float[]{2, 5, 2, 5}, 1); //分割线
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }

    /**
     * 绘制“几”字形标尺
     * @param canvas 画布
     * @param width 宽
     */
    private void drawRuler(Canvas canvas, int width) {
        float startX;
        float startY;
        float stopX;
        float stopY;
        float waveWidgetWidth = (float) width / 2; //屏幕一半

        // 设置画笔
        paint.reset();
        paint.setColor(Color.parseColor("#323333"));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);

        // i列数，j行数
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 7; j++) {
                startX = i * waveWidgetWidth;
                startY = waveWidgetHeight / 2 + waveWidgetHeight * j;
                stopX = i * waveWidgetWidth + pixelPerMm;
                stopY = waveWidgetHeight / 2 + waveWidgetHeight * j;
                canvas.drawLine(startX, startY + (float) (pixelPerMm * 2.5), stopX, stopY +
                        (float) (pixelPerMm * 2.5), paint); //画第一横

                startX = i * waveWidgetWidth + pixelPerMm;
                startY = waveWidgetHeight / 2 + waveWidgetHeight * j - pixelPerMm * 10;
                stopX = i * waveWidgetWidth + pixelPerMm * 3;
                stopY = waveWidgetHeight / 2 + waveWidgetHeight * j - pixelPerMm * 10;
                canvas.drawLine(startX, startY + (float) (pixelPerMm * 2.5), stopX, stopY +
                        (float) (pixelPerMm * 2.5), paint); //画第二横

                startX = i * waveWidgetWidth + pixelPerMm * 3;
                startY = waveWidgetHeight / 2 + waveWidgetHeight * j;
                stopX = i * waveWidgetWidth + pixelPerMm * 4;
                stopY = waveWidgetHeight / 2 + waveWidgetHeight * j;
                canvas.drawLine(startX, startY + (float) (pixelPerMm * 2.5), stopX, stopY +
                        (float) (pixelPerMm * 2.5), paint); //画第三横

                startX = i * waveWidgetWidth + pixelPerMm;
                startY = waveWidgetHeight / 2 + waveWidgetHeight * j;
                stopX = i * waveWidgetWidth + pixelPerMm;
                stopY = waveWidgetHeight / 2 + waveWidgetHeight * j - pixelPerMm * 10;
                canvas.drawLine(startX, startY + (float) (pixelPerMm * 2.5), stopX, stopY +
                        (float) (pixelPerMm * 2.5), paint); //画第一竖

                startX = i * waveWidgetWidth + pixelPerMm * 3;
                startY = waveWidgetHeight / 2 + waveWidgetHeight * j;
                stopX = i * waveWidgetWidth + pixelPerMm * 3;
                stopY = waveWidgetHeight / 2 + waveWidgetHeight * j - pixelPerMm * 10;
                canvas.drawLine(startX, startY + (float) (pixelPerMm * 2.5), stopX, stopY +
                        (float) (pixelPerMm * 2.5), paint); //画第二竖
            }
        }
    }

    /**
     * 绘制波形标题
     * @param canvas 画布
     * @param width 宽startY
     */
    private void drawTitle(Canvas canvas, int width) {
        float startX;
        float startY;
        float waveWidgetWidth = (float) width / 2;

        // 设置画笔
        paint.reset();
        paint.setColor(Color.parseColor("#323333"));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setTextSize(13);
        Typeface font = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        paint.setTypeface(font);
        String[] titleArr1 = new String[]{"I", "II", "III", "aVR", "aVL", "aVF", "II"};
        // j行数
        for (int j = 0; j < 7; j++) {
            startX = pixelPerMm * 5;
            startY = waveWidgetHeight / 2 + waveWidgetHeight * j - pixelPerMm * 5;
            canvas.drawText(titleArr1[j], startX, startY, paint);
        }

        String[] titleArr2 = new String[]{"V1", "V2", "V3", "V4", "V5", "V6"};
        // j行数
        for (int j = 0; j < 6; j++) {
            startX = pixelPerMm * 5 + waveWidgetWidth;
            startY = waveWidgetHeight / 2 + waveWidgetHeight * j - pixelPerMm * 5;
            canvas.drawText(titleArr2[j], startX, startY, paint);
        }
    }

    /**
     * 绘制波形数据
     * @param canvas 画布
     */
    private void drawWaveData(Canvas canvas) {
        float startX;
        float startY;

        // 设置画笔
        paint.reset();
        paint.setColor(Color.parseColor("#323333"));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setTextSize(18);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        paint.setTypeface(font);
        startX = pixelPerMm * 4;
        startY = waveWidgetHeight * 7 + pixelPerMm * 6;
        canvas.drawText("25mm/s    10mm/mV", startX, startY, paint);
        startX = 200;
        canvas.drawText("0.67-150Hz  AC 50Hz", startX, startY, paint);
    }

    /**
     * 绘制心电波形
     * @param canvas 画布
     * @param width 宽
     * @throws Exception 波形异常
     */
    private void drawWave(Canvas canvas, int width) {
        float startX;
        float startY;

        // 设置画笔
        paint.reset();
        paint.setColor(Color.parseColor("#323333"));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        startX = pixelPerMm * 4; //几 标尺的宽度
        //先绘制II导联
        //再绘制6*2波形
        for (int i = 0; i < 6; i++) { // 1-6:I,II,III,AVR,AVL,AVF波形
            startY = waveWidgetHeight * i - pixelPerMm; //为了调节波形与几标尺同水平
            String waveStr = Base64.encodeToString(UnitConvertUtil.
                    getByteformHexString(ecgList.get(i)), Base64.NO_WRAP);
            int[] ints = UnitConvertUtil.intValue(waveStr);
            // width / 2， 只绘制宽度的一半
            drawEcgWave(ints, canvas, paint, width / 2 - (int)pixelPerMm * 2, startX, startY);
        }
        startX = width / 2 + pixelPerMm; // 屏幕的一半开始绘制
        for (int i = 6; i < 12; i++) { // 7-12 v1-v6波形
            startY = waveWidgetHeight * (i - 6) - pixelPerMm;
            String waveStr = Base64.encodeToString(UnitConvertUtil.
                    getByteformHexString(ecgList.get(i)), Base64.NO_WRAP);
            int[] ints = UnitConvertUtil.intValue(waveStr);
            drawEcgWave(ints, canvas, paint, width, startX, startY);
        }
        startX = pixelPerMm * 4;
        startY = waveWidgetHeight * 6 - pixelPerMm; //为了调节波形与几标尺同水平
        String waveII = Base64.encodeToString(UnitConvertUtil.
                getByteformHexString(ecgList.get(1)), Base64.NO_WRAP);
        int[] intII = UnitConvertUtil.intValue(waveII);
        drawEcgWave(intII, canvas, paint, width, startX, startY);
    }

    /**
     * 根据心电波形数据绘制波形
     * @param value 波形数据
     * @param canvas 画布
     * @param paint 画笔
     * @param width 宽
     * @param xStart 初始X坐标
     * @param yStart 初始Y坐标
     */
    @SuppressLint("Range")
    private void drawEcgWave(int[] value, Canvas canvas, Paint paint, int width,
                             float xStart, float yStart) {
        float[] points1 = new float[20000];
        float[] points2 = new float[20000];
        int j = 0;
        float pointPerPix = (1280 / 216.576f) / (500 / 25); //单位：像素/点
        int pointNum = value.length;
        if (pointNum >= 4) { //超过4个点才绘制
            if (pointNum % 2 != 0) {
                pointNum--;
            }
            for (int i = 0; i < pointNum; i++) {
                //当点的X轴坐标达到width时跳出
                if (((i * pointPerPix * zoom) + xStart) >= width) {
                    break;
                }
                // 第i个点的X轴坐标
                points1[j * 2] = (i * pointPerPix * zoom) + xStart;

                // 第i个点的Y轴坐标
                // 2048为基线的AD值，1280为屏幕横向分辨率，216.576mm为屏幕宽度，
                // 2150为标志高值，1946为标尺低值，zoom波幅缩放倍数
                points1[j * 2 + 1] = yStart + (160f / 2f - ((float) value[i] -
                        2048) * (1280f / 216.576f) / ((2150f - 1946f) / 10)) * zoom;

                if (j >= 1) {
                    points2[(j - 1) * 2] = points1[j * 2];
                    points2[(j - 1) * 2 + 1] = points1[j * 2 + 1];
                }

                j++;
            }
            points2[(j - 1 - 1) * 2] = 0;
            points2[(j - 1 - 1) * 2 + 1] = 0;
        }
        canvas.drawLines(points1, paint);
        canvas.drawLines(points2, paint);
    }
}
