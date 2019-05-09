package com.konsung.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.konsung.R;
import com.konsung.util.MeasureUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * 心电波形绘制控件
 */
public class EcgViewFor12 extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    public boolean isRunning; // 波形绘制线程运行标志
    private Canvas canvas;
    // Y轴比例，像素/AD值。1280为屏幕横向像素数，216.96为屏幕宽度，单位mm，2150和1946为标尺上下对应
    // 的AD值。此变量会被频繁调用，因此声明为static形。
    private final static float ECG_Y_RATIO = (1280f / 216.96f * 10) / (2150 - 1946);
    private float lockWidth; // 每次锁屏需要画的宽度，单位像素
    private float waveSpeed; // 波速，单位mm/s
    private int sleepTime = 100; // 每次锁屏的时间间距，单位ms
    private int ecgPerCount = 50; // 每次画心电数据的个数，心电每秒有500个数据包
    private float ecgXOffset = 0; // 每次X坐标偏移的像素
    private int blankLineWidth = 0; // 右侧空白点的宽度，单位像素
    private float waveGain; // 波形增益
    private Context context; //上下文
    private boolean isAutoGain = false; // 是否是自动增益
    private static Queue<Integer> ecg0Data = new LinkedList<>();
    private static Queue<Integer> ecg1Data = new LinkedList<>();
    private static Queue<Integer> ecg2Data = new LinkedList<>();
    private static Queue<Integer> ecg3Data = new LinkedList<>();
    private static Queue<Integer> ecg4Data = new LinkedList<>();
    private static Queue<Integer> ecg5Data = new LinkedList<>();
    private static Queue<Integer> ecg6Data = new LinkedList<>();
    private static Queue<Integer> ecg7Data = new LinkedList<>();
    private static Queue<Integer> ecg8Data = new LinkedList<>();
    private static Queue<Integer> ecg9Data = new LinkedList<>();
    private static Queue<Integer> ecg10Data = new LinkedList<>();
    private static Queue<Integer> ecg11Data = new LinkedList<>();
    private Paint wavePaint; //画波形图的画笔
    private int waveWidgetWidth; //单个波形的控件宽度
    private int waveWidgetHeight; //单个波形的控件高度
    //每次画线的X坐标起点
    private int startX0;
    private int startX1;
    private int startX2;
    private int startX3;
    //每次画线的Y坐标起点
    private int startY0;
    private int startY1;
    private int startY2;
    private int startY3;
    private int startY4;
    private int startY5;
    private int startY6;
    private int startY7;
    private int startY8;
    private int startY9;
    private int startY10;
    private int startY11;
    //Y坐标偏移值
    private int yOffset0;
    private int yOffset1;
    private int yOffset2;
    private int yOffset3;
    private int yOffset4;
    private int yOffset5;
    private int yOffset6;
    private int yOffset7;
    private int yOffset8;
    private int yOffset9;
    private int yOffset10;
    private int yOffset11;
    // 波形绘制矩形区域
    private Rect rect;

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param attrs   属性设置
     */
    public EcgViewFor12(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);

        //将surfaceView背景变为透明
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        initAttributes();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 清除画布
        Canvas canvas = holder.lockCanvas(null);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        holder.unlockCanvasAndPost(canvas);

        initWaveDraw(); // 此时相应的控件尺寸确定下来，可以初始化波形绘制相应的参数了
        resetData(); // 复位波形数据
        setWaveGain(); // 设置增益和更新背景
        startThread(); // 启动波形绘制线程
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopThread(); // 销毁时停止波形绘制线程
    }

    /**
     * 添加波形数据到循环队列
     *
     * @param data 待绘制的心电波形数据
     */
    public void addEcgData0(int data) {
        ecg0Data.add(data);
    }

    /**
     * 添加波形数据到循环队列 导联I
     *
     * @param data 待绘制的心电波形数据
     */
    public void addEcgData1(int data) {
        ecg1Data.add(data);
    }

    /**
     * 添加波形数据到循环队列 导联2
     *
     * @param data 待绘制的心电波形数据
     */
    public void addEcgData2(int data) {
        ecg2Data.add(data);
    }

    /**
     * 添加波形数据到循环队列 导联3
     *
     * @param data 待绘制的心电波形数据
     */
    public void addEcgData3(int data) {
        ecg3Data.add(data);
    }

    /**
     * 添加波形数据到循环队列 导联4
     *
     * @param data 待绘制的心电波形数据
     */
    public void addEcgData4(int data) {
        ecg4Data.add(data);
    }

    /**
     * 添加波形数据到循环队列 导联5
     *
     * @param data 待绘制的心电波形数据
     */
    public void addEcgData5(int data) {
        ecg5Data.add(data);
    }

    /**
     * 添加波形数据到循环队列 导联6
     *
     * @param data 待绘制的心电波形数据
     */
    public void addEcgData6(int data) {
        ecg6Data.add(data);
    }

    /**
     * 添加波形数据到循环队列 导联7
     *
     * @param data 待绘制的心电波形数据
     */
    public void addEcgData7(int data) {
        ecg7Data.add(data);
    }

    /**
     * 添加波形数据到循环队列 导联8
     *
     * @param data 待绘制的心电波形数据
     */
    public void addEcgData8(int data) {
        ecg8Data.add(data);
    }

    /**
     * 添加波形数据到循环队列 导联9
     *
     * @param data 待绘制的心电波形数据
     */
    public void addEcgData9(int data) {
        ecg9Data.add(data);
    }

    /**
     * 添加波形数据到循环队列 导联10
     *
     * @param data 待绘制的心电波形数据
     */
    public void addEcgData10(int data) {
        ecg10Data.add(data);
    }

    /**
     * 添加波形数据到循环队列 导联11
     *
     * @param data 待绘制的心电波形数据
     */
    public void addEcgData11(int data) {
        ecg11Data.add(data);
    }

    /**
     * 设置波形速率
     */
    public void setWaveSpeed() {
        float waveSpeed = getWaveSpeedConfig();
        if (this.waveSpeed != waveSpeed) {
            this.waveSpeed = waveSpeed;
            stopThread();
            // 清除画布
            canvas = surfaceHolder.lockCanvas(null);
            if (canvas == null) {
                return;
            }
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            surfaceHolder.unlockCanvasAndPost(canvas);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MeasureUtils.post(new Runnable() {
                        @Override
                        public void run() {
                            initAttributes();
                            initWaveDraw();
                            resetData();
                            startThread();
                        }
                    });
                }
            }).start();
        }
    }

    /**
     * 设置波形增益
     */
    public void setWaveGain() {
        float gain = getWaveGainConfig();
        if (gain == -1000f) {
            isAutoGain = true;
            waveGain = 2f;
        } else {
            isAutoGain = false;
            waveGain = gain;
        }
        drawBackground();
    }

    /**
     * 初始化属性
     */
    private void initAttributes() {
        // 每毫米有多少像素。1280屏幕横向像素数；216.96为屏幕横向宽度，单位mm
        final float pxPerMm = 1280f / 216.96f;
        float pxPerSecond; //每秒画多少像素

        // 初始化矩形波形绘制区域
        rect = new Rect();
        // 初始化画笔
        wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint.setStrokeWidth(1.5f);
        wavePaint.setColor(Color.parseColor("#6CBA4C"));
        wavePaint.setAntiAlias(true); // 设置抗锯齿
        // 初始化其他波形绘制相关的参数
        waveSpeed = getWaveSpeedConfig();
        pxPerSecond = waveSpeed * pxPerMm; // 计算每秒画多少像素
        lockWidth = pxPerSecond * (sleepTime / 1000f); // 每次锁屏所需画的宽度
        ecgXOffset = lockWidth / ecgPerCount;
        blankLineWidth = 20;
    }

    public float getWaveSpeed() {
        return waveSpeed;
    }

    public float getWaveGain() {
        return waveGain;
    }


    /**
     * 波形绘制初始化
     */
    private void initWaveDraw() {
        final int columnNum = 4; // 波形列数
        final int rowNum = 3; // 波形行数

        waveWidgetWidth = getWidth() / columnNum;
        waveWidgetHeight = getHeight() / rowNum;

        startX0 = 0;
        startX1 = waveWidgetWidth;
        startX2 = waveWidgetWidth * 2;
        startX3 = waveWidgetWidth * 3;

        startY0 = waveWidgetHeight / 2; // 波1初始Y坐标(控件的二分之一处Y坐标)
        startY1 = waveWidgetHeight / 2;
        startY2 = waveWidgetHeight / 2;
        startY3 = waveWidgetHeight / 2;
        startY4 = waveWidgetHeight + waveWidgetHeight / 2;
        startY5 = waveWidgetHeight + waveWidgetHeight / 2;
        startY6 = waveWidgetHeight + waveWidgetHeight / 2;
        startY7 = waveWidgetHeight + waveWidgetHeight / 2;
        startY8 = waveWidgetHeight * 2 + waveWidgetHeight / 2;
        startY9 = waveWidgetHeight * 2 + waveWidgetHeight / 2;
        startY10 = waveWidgetHeight * 2 + waveWidgetHeight / 2;
        startY11 = waveWidgetHeight * 2 + waveWidgetHeight / 2;

        yOffset0 = 0;
        yOffset1 = 0;
        yOffset2 = 0;
        yOffset3 = 0;
        yOffset4 = waveWidgetHeight;
        yOffset5 = waveWidgetHeight;
        yOffset6 = waveWidgetHeight;
        yOffset7 = waveWidgetHeight;
        yOffset8 = waveWidgetHeight * 2;
        yOffset9 = waveWidgetHeight * 2;
        yOffset10 = waveWidgetHeight * 2;
        yOffset11 = waveWidgetHeight * 2;
    }

    /**
     * 获取波形速率配置
     *
     * @return 波形速率
     */
    private float getWaveSpeedConfig() {
        float speed = MeasureUtils.getSpFloat(getContext(), "sys_config", "mm", 1.0f);

        if (speed == 0.2f) {
            return 5f;
        } else if (speed == 0.25f) {
            return 6.25f;
        } else if (speed == 0.4f) {
            return 10f;
        } else if (speed == 0.5f) {
            return 12.5f;
        } else if (speed == 1.0f) {
            return 25f;
        } else if (speed == 2.0f) {
            return 50f;
        } else {
            return 25f;
        }
    }

    /**
     * 获取波形增益配置
     *
     * @return 波形增益
     */
    private float getWaveGainConfig() {
        float gain = MeasureUtils.getSpFloat(getContext(), "sys_config", "xx", 1.0f);

        if (gain == 0.5f) {
            return 0.5f;
        } else if (gain == 1f) {
            return 1f;
        } else if (gain == 2f) {
            return 2f;
        } else if (gain == -1000f) {
            return -1000f;
        } else {
            return 1f;
        }
    }

    /**
     * 启动线程
     */
    private void startThread() {
        isRunning = true;
        new Thread(drawRunnable).start();
    }

    /**
     * 停止线程
     */
    private void stopThread() {
        isRunning = false;
    }

    /**
     * 波形绘制线程
     */
    Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                long startTime = System.currentTimeMillis();

                if (ecg11Data.size() > ecgPerCount) {
                    startDrawWaveGroup0();
                    startDrawWaveGroup1();
                    startDrawWaveGroup2();
                    startDrawWaveGroup3();
                }
                adjustPerCount();

                long endTime = System.currentTimeMillis();
                if (endTime - startTime < sleepTime) {
                    try {
                        Thread.sleep(sleepTime - (endTime - startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 绘制第1列波形
     */
    private void startDrawWaveGroup0() {
        rect.set(startX0, 0, (int) (startX0 + lockWidth + blankLineWidth),
                waveWidgetHeight * 3);
        canvas = surfaceHolder.lockCanvas(rect);
        if (canvas == null) {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Point point0 = drawWave(startX0, startY0, ecg0Data, yOffset0, 0);
        Point point4 = drawWave(startX0, startY4, ecg4Data, yOffset4, 1);
        Point point8 = drawWave(startX0, startY8, ecg8Data, yOffset8, 2);
        startX0 = point0.x;
        startY0 = point0.y;
        startY4 = point4.y;
        startY8 = point8.y;
        surfaceHolder.unlockCanvasAndPost(canvas);
        if (startX0 > waveWidgetWidth) {
            startX0 = 0;
            if (ecg0Data.size() > (ecgPerCount * 17)) {
                List<Integer> ecg0 = new ArrayList<>();
                for (int i = 0; i < ecgPerCount * 17; i++) {
                    try {
                        ecg0.add(ecg0Data.poll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ecg0Data.clear();
                ecg0Data.addAll(ecg0);
            }
            if (ecg4Data.size() > (ecgPerCount * 17)) {
                List<Integer> ecg1 = new ArrayList<>();
                for (int i = 0; i < ecgPerCount * 17; i++) {
                    try {
                        ecg1.add(ecg4Data.poll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ecg4Data.clear();
                ecg4Data.addAll(ecg1);
            }
            if (ecg8Data.size() > (ecgPerCount * 17)) {
                List<Integer> ecg1 = new ArrayList<>();
                for (int i = 0; i < ecgPerCount * 17; i++) {
                    try {
                        ecg1.add(ecg8Data.poll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ecg8Data.clear();
                ecg8Data.addAll(ecg1);
            }
        }
    }

    /**
     * 绘制第2列波形
     */
    private void startDrawWaveGroup1() {
        rect.set(startX1, 0, (int) (startX1 + lockWidth + blankLineWidth),
                waveWidgetHeight * 3);
        canvas = surfaceHolder.lockCanvas(rect);
        if (canvas == null) {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Point point1 = drawWave(startX1, startY1, ecg1Data, yOffset1, 0);
        Point point5 = drawWave(startX1, startY5, ecg5Data, yOffset5, 1);
        Point point9 = drawWave(startX1, startY9, ecg9Data, yOffset9, 2);
        startX1 = point1.x;
        startY1 = point1.y;
        startY5 = point5.y;
        startY9 = point9.y;
        surfaceHolder.unlockCanvasAndPost(canvas);
        if (startX1 > waveWidgetWidth * 2) {
            startX1 = waveWidgetWidth;
            if (ecg1Data.size() > (ecgPerCount * 17)) {
                List<Integer> ecg1 = new ArrayList<>();
                for (int i = 0; i < ecgPerCount * 17; i++) {
                    try {
                        ecg1.add(ecg1Data.poll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ecg1Data.clear();
                ecg1Data.addAll(ecg1);
            }
            if (ecg5Data.size() > (ecgPerCount * 17)) {
                List<Integer> ecg1 = new ArrayList<>();
                for (int i = 0; i < ecgPerCount * 17; i++) {
                    try {
                        ecg1.add(ecg5Data.poll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ecg5Data.clear();
                ecg5Data.addAll(ecg1);
            }
            if (ecg9Data.size() > (ecgPerCount * 17)) {
                List<Integer> ecg1 = new ArrayList<>();
                for (int i = 0; i < ecgPerCount * 17; i++) {
                    try {
                        ecg1.add(ecg9Data.poll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ecg9Data.clear();
                ecg9Data.addAll(ecg1);
                startX1 = waveWidgetWidth + startX0;
            }
        }
    }

    /**
     * 绘制第3列波形
     */
    private void startDrawWaveGroup2() {
        rect.set(startX2, 0, (int) (startX2 + lockWidth + blankLineWidth), waveWidgetHeight * 3);
        canvas = surfaceHolder.lockCanvas(rect);
        if (canvas == null) {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        Point point2 = drawWave(startX2, startY2, ecg2Data, yOffset2, 0);
        Point point6 = drawWave(startX2, startY6, ecg6Data, yOffset6, 1);
        Point point10 = drawWave(startX2, startY10, ecg10Data, yOffset10, 2);
        startX2 = point2.x;
        startY2 = point2.y;
        startY6 = point6.y;
        startY10 = point10.y;
        surfaceHolder.unlockCanvasAndPost(canvas);

        if (startX2 > waveWidgetWidth * 3) {
            startX2 = waveWidgetWidth * 2;
            if (ecg2Data.size() > (ecgPerCount * 17)) {
                List<Integer> ecg1 = new ArrayList<>();
                for (int i = 0; i < ecgPerCount * 17; i++) {
                    try {
                        ecg1.add(ecg2Data.poll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ecg2Data.clear();
                ecg2Data.addAll(ecg1);
            }
            if (ecg6Data.size() > (ecgPerCount * 17)) {
                List<Integer> ecg1 = new ArrayList<>();
                for (int i = 0; i < ecgPerCount * 17; i++) {
                    try {
                        ecg1.add(ecg6Data.poll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ecg6Data.clear();
                ecg6Data.addAll(ecg1);
            }
            if (ecg10Data.size() > (ecgPerCount * 17)) {
                List<Integer> ecg1 = new ArrayList<>();
                for (int i = 0; i < ecgPerCount * 17; i++) {
                    try {
                        ecg1.add(ecg10Data.poll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ecg10Data.clear();
                ecg10Data.addAll(ecg1);
                startX2 = waveWidgetWidth * 2 + startX0;
            }
        }
    }

    /**
     * 绘制第4列波形
     */
    private void startDrawWaveGroup3() {
        rect.set(startX3, 0, (int) (startX3 + lockWidth + blankLineWidth), waveWidgetHeight * 3);
        canvas = surfaceHolder.lockCanvas(rect);
        if (canvas == null) {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        Point point3 = drawWave(startX3, startY3, ecg3Data, yOffset3, 0);
        Point point7 = drawWave(startX3, startY7, ecg7Data, yOffset7, 1);
        Point point11 = drawWave(startX3, startY11, ecg11Data, yOffset11, 2);

        startX3 = point3.x;
        startY3 = point3.y;
        startY7 = point7.y;
        startY11 = point11.y;
        surfaceHolder.unlockCanvasAndPost(canvas);

        if (startX3 > waveWidgetWidth * 4) {
            startX3 = waveWidgetWidth * 3;
            if (ecg3Data.size() > (ecgPerCount * 17)) {
                List<Integer> ecg1 = new ArrayList<>();
                for (int i = 0; i < ecgPerCount * 17; i++) {
                    try {
                        ecg1.add(ecg3Data.poll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ecg3Data.clear();
                ecg3Data.addAll(ecg1);
            }
            if (ecg7Data.size() > (ecgPerCount * 17)) {
                List<Integer> ecg1 = new ArrayList<>();
                for (int i = 0; i < ecgPerCount * 17; i++) {
                    try {
                        ecg1.add(ecg7Data.poll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ecg7Data.clear();
                ecg7Data.addAll(ecg1);
            }
            if (ecg11Data.size() > (ecgPerCount * 17)) {
                List<Integer> ecg1 = new ArrayList<>();
                for (int i = 0; i < ecgPerCount * 17; i++) {
                    try {
                        ecg1.add(ecg11Data.poll());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ecg11Data.clear();
                ecg11Data.addAll(ecg1);
                startX3 = waveWidgetWidth * 3 + startX0;
            }
        }
    }

    /**
     * 画心电数据
     *
     * @param startX           开始X
     * @param startY           开始Y
     * @param data             画的数据
     * @param yOffset          画的速度
     * @param waveWidgetHeight 宽度
     * @return 画笔
     */
    private Point drawWave(int startX, int startY, Queue<Integer> data, int
            yOffset, int waveWidgetHeight) {
        int initX;
        int newX;
        int newY;
        Point point;
        initX = startX;
        point = new Point(startX, startY);
        try {
            for (int i = 0; i < ecgPerCount; i++) {
                if (data.size() != 0) {
                    startX = Math.round(initX + ecgXOffset * i);
                    newX = Math.round(initX + ecgXOffset * (i + 1));
                    newY = ecgConvert(data.poll()) + yOffset;
                    if (newY < this.waveWidgetHeight * waveWidgetHeight) {
                        newY = this.waveWidgetHeight * waveWidgetHeight;
                    } else if (newY > (this.waveWidgetHeight * waveWidgetHeight
                            + this.waveWidgetHeight)) {
                        newY = this.waveWidgetHeight * waveWidgetHeight +
                                this.waveWidgetHeight;
                    }
                    canvas.drawLine(startX, startY, newX, newY, wavePaint);
                    startX = newX;
                    startY = newY;
                }
            }
            point.x = startX;
            point.y = startY;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return point;
    }

    /**
     * 将心电数据转换成用于显示的Y坐标
     *
     * @param data 心电数值
     * @return 转换后的Y坐标值
     */
    private int ecgConvert(int data) {
        int ecgMax = 4096; // 心电的最大值

        // 自动增益
        if (isAutoGain) {
            data = waveWidgetHeight / 2 - (int) ((data - (ecgMax / 2)) * ECG_Y_RATIO * waveGain);
            if ((data < 0) || (data > waveWidgetHeight)) {
                if (waveGain == 2f) {
                    waveGain = 1f;
                    Message message = Message.obtain();
                    handler.sendMessage(message);
                } else if (waveGain == 1f) {
                    waveGain = 0.5f;
                    Message message = Message.obtain();
                    handler.sendMessage(message);
                }
            }
        } else {
            data = waveWidgetHeight / 2 - (int) ((data - (ecgMax / 2)) * ECG_Y_RATIO * waveGain);
        }

        return data;
    }

    /**
     * 动态调节每次画心电数据的个数
     */
    private void adjustPerCount() {
        if (isRunning) {
            // 心电每秒传输500个数据，允许缓存1秒的数据
            if (ecg0Data.size() > 600) {
                // ecgPerCount与waveSpeed成正比。waveSpeed误差不能大于正负10%，因此ecgPerCount的误差
                // 也不能超过正负10%，经计算可知，ecgPerCount不能查过范围45~55。
                if (ecgPerCount < 55) {
                    ecgPerCount++;
                }
            } else if ((ecg0Data.size() < 500) && (ecg0Data.size() != 0)) {
                if (ecgPerCount > 50) {
                    ecgPerCount--;
                }
            }
        }
    }

    /**
     * 复位心电波形数据
     */
    private void resetData() {
        ecg0Data.clear();
        ecg1Data.clear();
        ecg2Data.clear();
        ecg3Data.clear();
        ecg4Data.clear();
        ecg5Data.clear();
        ecg6Data.clear();
        ecg7Data.clear();
        ecg8Data.clear();
        ecg9Data.clear();
        ecg10Data.clear();
        ecg11Data.clear();
    }

    /**
     * 画心电背景图
     * 说明：此函数必须在SurfaceCreated执行之后调用
     */
    private void drawBackground() {
        final int width = getWidth();
        final int height = getHeight();

        final Drawable drawable = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawColor(Color.BLACK);
                drawEcgGrid(canvas, width, height);
                drawEcgWidgetGrid(canvas, width, height);
                drawEcgRuler(canvas, width, height);
                drawEcgTitle(canvas, width, height);
            }

            @Override
            public void setAlpha(int alpha) {
            }

            @Override
            public void setColorFilter(ColorFilter cf) {
            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };
        this.setBackground(drawable);
    }

    /**
     * 画心电测量界面的网格
     *
     * @param canvas 画布
     * @param width  绘画宽度
     * @param height 绘画高度
     */
    private void drawEcgGrid(Canvas canvas, int width, int height) {
        // 设置画笔
        Paint paint = new Paint();
        paint.setColor(0xFF4c4c4c);
        paint.setTextSize(14);
        paint.setAlpha(150);
        paint.setAntiAlias(true);

        // 单位:像素/mm,1280为屏幕横向像素数，216.96为屏幕横向长度（单位mm）
        double pixelPerMm = 1280 / 216.96;
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
            startY = (float) (i * pixelPerMm * 5);
            stopY = (float) (i * pixelPerMm * 5);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        // 画Y轴,每隔5mm画一条线
        paint.setStrokeWidth(2.0f);
        startY = 0;
        stopY = height;
        for (int i = 0; i < widthLen; i++) {
            startX = (float) (i * pixelPerMm * 5);
            stopX = (float) (i * pixelPerMm * 5);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    /**
     * 画心电波形控件网格
     *
     * @param canvas 画布
     * @param width  绘画宽度
     * @param height 绘画高度
     */
    private void drawEcgWidgetGrid(Canvas canvas, int width, int height) {
        float startX;
        float startY;
        float stopX;
        float stopY;
        float waveWidgetWidth = (float) width / 4;
        float waveWidgetHeight = (float) height / 3;

        // 设置画笔
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);

        startX = 0f;
        startY = waveWidgetHeight;
        stopX = width;
        stopY = waveWidgetHeight;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        startX = 0f;
        startY = waveWidgetHeight * 2;
        stopX = width;
        stopY = waveWidgetHeight * 2;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        startX = waveWidgetWidth;
        startY = 0f;
        stopX = waveWidgetWidth;
        stopY = waveWidgetHeight * 3;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        startX = waveWidgetWidth * 2;
        startY = 0f;
        stopX = waveWidgetWidth * 2;
        stopY = waveWidgetHeight * 3;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        startX = waveWidgetWidth * 3;
        startY = 0f;
        stopX = waveWidgetWidth * 3;
        stopY = waveWidgetHeight * 3;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    /**
     * 画心电测量界面的标尺
     *
     * @param canvas 画布
     * @param width  绘画宽度
     * @param height 绘画高度
     */
    private void drawEcgRuler(Canvas canvas, int width, int height) {
        float startX;
        float startY;
        float stopX;
        float stopY;
        float waveWidgetWidth = (float) width / 4;
        float waveWidgetHeight = (float) height / 3;

        // 单位:像素/mm,1280为屏幕横向像素数，216.96为屏幕横向长度（单位mm）
        double pixelPerMm = 1280 / 216.96;

        // 设置画笔
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAlpha(150);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4.0f);

        // 标尺长度为1cm，横向上放在六分之五处，纵向上位于画布中央
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                startX = waveWidgetWidth * i + waveWidgetWidth * 5 / 6;
                startY = waveWidgetHeight * j + waveWidgetHeight / 2 - (float)
                        pixelPerMm * 5 * waveGain;
                stopX = waveWidgetWidth * i + waveWidgetWidth * 5 / 6;
                stopY = waveWidgetHeight * j + waveWidgetHeight / 2 + (float)
                        pixelPerMm * 5 * waveGain;
                canvas.drawLine(startX, startY, stopX, stopY, paint);
            }
        }
    }

    /**
     * 画心电波形标题
     *
     * @param canvas 画布
     * @param width  绘画宽度
     * @param height 绘画高度
     */
    private void drawEcgTitle(Canvas canvas, int width, int height) {
        float startX;
        float startY;
        float waveWidgetWidth = (float) width / 4;
        float waveWidgetHeight = (float) height / 3;
        String title;
        String gainString;

        // 设置画笔
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#6CBA4C"));
        paint.setTextSize(20);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);  //设置画出的线的 粗细程度

        gainString = getGainString();

        // 标题放在该条波形的布局的(10, 30)位置
        startX = waveWidgetWidth * 0 + 10;
        startY = waveWidgetHeight * 0 + 30;
        title = context.getString(R.string.ecg_title_I) + " " + gainString;
        canvas.drawText(title, startX, startY, paint);
        startX = waveWidgetWidth * 1 + 10;
        startY = waveWidgetHeight * 0 + 30;
        title = context.getString(R.string.ecg_title_II) + " " + gainString;
        canvas.drawText(title, startX, startY, paint);
        startX = waveWidgetWidth * 2 + 10;
        startY = waveWidgetHeight * 0 + 30;
        title = context.getString(R.string.ecg_title_III) + " " + gainString;
        canvas.drawText(title, startX, startY, paint);
        startX = waveWidgetWidth * 3 + 10;
        startY = waveWidgetHeight * 0 + 30;
        title = context.getString(R.string.ecg_title_AVR) + " " + gainString;
        canvas.drawText(title, startX, startY, paint);
        startX = waveWidgetWidth * 0 + 10;
        startY = waveWidgetHeight * 1 + 30;
        title = context.getString(R.string.ecg_title_AVL) + " " + gainString;
        canvas.drawText(title, startX, startY, paint);
        startX = waveWidgetWidth * 1 + 10;
        startY = waveWidgetHeight * 1 + 30;
        title = context.getString(R.string.ecg_title_AVF) + " " + gainString;
        canvas.drawText(title, startX, startY, paint);
        startX = waveWidgetWidth * 2 + 10;
        startY = waveWidgetHeight * 1 + 30;
        title = context.getString(R.string.ecg_title_V1) + " " + gainString;
        canvas.drawText(title, startX, startY, paint);
        startX = waveWidgetWidth * 3 + 10;
        startY = waveWidgetHeight * 1 + 30;
        title = context.getString(R.string.ecg_title_V2) + " " + gainString;
        canvas.drawText(title, startX, startY, paint);
        startX = waveWidgetWidth * 0 + 10;
        startY = waveWidgetHeight * 2 + 30;
        title = context.getString(R.string.ecg_title_V3) + " " + gainString;
        canvas.drawText(title, startX, startY, paint);
        startX = waveWidgetWidth * 1 + 10;
        startY = waveWidgetHeight * 2 + 30;
        title = context.getString(R.string.ecg_title_V4) + " " + gainString;
        canvas.drawText(title, startX, startY, paint);
        startX = waveWidgetWidth * 2 + 10;
        startY = waveWidgetHeight * 2 + 30;
        title = context.getString(R.string.ecg_title_V5) + " " + gainString;
        canvas.drawText(title, startX, startY, paint);
        startX = waveWidgetWidth * 3 + 10;
        startY = waveWidgetHeight * 2 + 30;
        title = context.getString(R.string.ecg_title_V6) + " " + gainString;
        canvas.drawText(title, startX, startY, paint);
    }

    /**
     * 获的作为标题的增益字符串
     *
     * @return 当前增益字符串
     */
    private String getGainString() {
        float gain = MeasureUtils.getSpFloat(getContext(), "sys_config", "xx", 1.0f);

        if (gain == 0.5f) {
            return "x0.5";
        } else if (gain == 1.0f) {
            return "x1";
        } else if (gain == 2.0f) {
            return "x2";
        } else if (gain == -1000f) {
            return "x1";
        } else {
            return "x1";
        }
    }

    /**
     * 接收绘图线程的消息，更新背景
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            drawBackground();
        }
    };
}
