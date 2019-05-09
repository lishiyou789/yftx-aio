package com.konsung.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.konsung.R;
import com.konsung.constant.Configuration;
import com.konsung.util.MeasureUtils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 胎心率波形绘制控件
 */
public class FhrView extends BaseEcgView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    public boolean isRunning; // 波形绘制线程运行标志
    private Canvas canvas;
    // Y轴比例，像素/AD值。1280为屏幕横向像素数，216.96为屏幕宽度，单位mm，2150和1946为标尺上下对应
    // 的AD值。此变量会被频繁调用，因此声明为static形。
    private final static float ecgYRatio = (1280f / 216.96f * 10) / (2150 - 1946);
    private float lockWidth; // 每次锁屏需要画的宽度，单位像素
    private float waveSpeed; // 波速，单位cm/m
    private int sleepTime = 250; // 每次锁屏的时间间距，单位ms
    private final static int SLEEP_TIME = 100; // 每次锁屏的时间间距，单位ms
    private int ecgPerCount = 4; // 每次画心电数据的个数，心电每秒有4个数据包
    private float ecgXOffset = 0; // 每次X坐标偏移的像素
    private int blankLineWidth = 0; // 右侧空白点的宽度，单位像素
    private float waveGain; // 波形增益
    private boolean isAutoGain = false; // 是否是自动增益
    private static Queue<Integer> fhrData = new LinkedList<>();
    private Paint wavePaint;//画波形图的画笔
    private int waveWidgetWidth;//单个波形的控件宽度
    private int waveWidgetHeight;//单个波形的控件高度
    //每次画线的X坐标起点
    private int startX;
    private float startFX;
    //每次画线的Y坐标起点
    private int startY;
    //Y坐标偏移值
    private int yOffset;
    // 波形绘制矩形区域
    private Rect rect;
    String[] scale = {"100", "120", "140", "160"};//刻度尺
    private int scaleValue = 90;//刻度起点值

    public void setScale(String[] scale, int scaleValue) {
        this.scale = scale;
        this.scaleValue = scaleValue;
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param attrs   属性设置
     */
    public FhrView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
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
        fhrData.add(data);
    }

    /**
     * 设置波形速率
     * 重新初始化波形数据绘制。
     */
    public void setWaveSpeed() {
        this.waveSpeed = getWaveSpeedConfig();
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
        wavePaint = new Paint();
        wavePaint.setStrokeWidth(1);
        wavePaint.setColor(getResources().getColor(R.color.waveLine));
        wavePaint.setAntiAlias(true); // 设置抗锯齿
        // 初始化其他波形绘制相关的参数
        blankLineWidth = 20;
        waveSpeed = getWaveSpeedConfig();
        pxPerSecond = waveSpeed * pxPerMm; // 计算每秒画多少像素
        lockWidth = pxPerSecond * (SLEEP_TIME / 1000); // 每次锁屏所需画的宽度
        ecgXOffset = pxPerSecond / ecgPerCount;
    }

    /**
     * 波形绘制初始化
     */
    private void initWaveDraw() {
        waveWidgetWidth = getWidth();
        waveWidgetHeight = getHeight();
        startX = 0;
        startFX = 0;
        startY = waveWidgetHeight / 2; // 波1初始Y坐标(控件的二分之一处Y坐标)
        yOffset = 0;
    }

    /**
     * 获取波形速率配置
     *
     * @return 波形速率
     */
    private float getWaveSpeedConfig() {
        int speed = MeasureUtils.getSpInt(getContext(), Configuration.APP_CONFIG
                , Configuration.FHR_SELECTED, 0);
        if (speed == 0) {
            return 1 / 6f;
        } else if (speed == 1) {
            return 2 / 6f;
        } else if (speed == 2) {
            return 3 / 6f;
        } else {
            return 1 / 6f;
        }
    }

    /**
     * 获取波形增益配置
     *
     * @return 波形增益倍数
     */
    private float getWaveGainConfig() {
        float gain = MeasureUtils.getSpFloat(getContext(), Configuration.SYS_CONFIG, "xx", 1.0f);

        if (gain == 0.5f) {
            return 0.5f;
        } else if (gain == 0.25f) {
            return 0.25f;
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
    public void startThread() {
        isRunning = true;
        new Thread(drawRunnable).start();
    }

    /**
     * 停止线程
     */
    public void stopThread() {
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
                if (fhrData.size() > ecgPerCount) {
                    startDrawWaveGroup();
                }

                long endTime = System.currentTimeMillis();
                if (endTime - startTime < SLEEP_TIME) {
                    try {
                        Thread.sleep(SLEEP_TIME - (endTime - startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 绘制波形
     */
    private void startDrawWaveGroup() {
        rect.set(startX, 0, (int) (startX + lockWidth + blankLineWidth),
                waveWidgetHeight);
        canvas = surfaceHolder.lockCanvas(rect);
        if (canvas == null) {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        PointF point1 = drawWave(startFX, startY, fhrData, yOffset, waveWidgetHeight); // 0高度倍数
        startX = Math.round(point1.x);
        startFX = point1.x;
        startY = Math.round(point1.y);
        surfaceHolder.unlockCanvasAndPost(canvas);
        if (startX > waveWidgetWidth) {
            startX = 0;
            startFX = 0;
        }

    }

    /**
     * 绘制心电波形
     *
     * @return 绘制波形的最后一个点的坐标
     */
    private PointF drawWave(float initX, int startY, Queue<Integer> data, int
            yOffset, int waveWidgetHeight) {
        float startX;
        float newX;
        int newY;
        PointF point;

        startX = initX;
        point = new PointF(startX, startY);

        try {
            for (int i = 0; i < ecgPerCount; i++) { //每秒画一次
                if (data.size() != 0) {
                    startX = initX + ecgXOffset * i;
                    newX = initX + ecgXOffset * (i + 1);
//                    startX = Math.round(initX);
//                    newX = Math.round(initX + ecgXOffset );
                    newY = ecgConvert(data.poll()) + yOffset;
                    if (newY < 0) {
                        newY = 0;
                    } else if (newY > (this.waveWidgetHeight)) {
                        newY = this.waveWidgetHeight;
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
        waveWidgetHeight = getHeight(); // 同步高度
        //波形图起点是90， 波形从90-170，占据了80个刻度。（初始化波形数据对刻度的占有量）
        data = ((data - scaleValue) * waveWidgetHeight / 80);

        //界面ui尺寸大小是从上到下依次递增。而波形是由下到上递增。
        //波形数据对高度一半的相对值。
        if (data > waveWidgetHeight / 2) {
            data = data - ((data - (waveWidgetHeight / 2)) * 2);
        } else if (data < waveWidgetHeight / 2) {
            data = data + ((waveWidgetHeight / 2 - data) * 2);
        }

        return data;
    }

    /**
     * 复位心电波形数据
     */
    public void resetData() {
        fhrData.clear();
    }

    /**
     * 画心电背景图
     * 画心电背景图
     * 说明：此函数必须在SurfaceCreated执行之后调用
     */
    private void drawBackground() {
        final int width = getWidth();
        final int height = getHeight();

        final Drawable drawable = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawColor(Color.WHITE);
                drawFhrGrid(canvas, width, height);
                drawEcgRuler(canvas, width, height);

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
     * 画胎心率测量界面的网格
     *
     * @param canvas 画布
     * @param width  绘画宽度
     * @param height 绘画高度
     */
    private void drawFhrGrid(Canvas canvas, int width, int height) {
        // 设置画笔
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.thickLine));
        paint.setTextSize(1);
        paint.setAntiAlias(true);

        // 单位:像素/mm,1280为屏幕横向像素数，216.96为屏幕横向长度（单位mm）
        double pixelPerMm = 1280 / 216.96;
        int widthLen = (int) (width / pixelPerMm);//转换为mm长度
        int heightLen = (int) (height / pixelPerMm);//转换为mm长度

        float startX;
        float startY;
        float stopX;
        float stopY;

        //绘制蓝色背景
        paint.setColor(getResources().getColor(R.color.fhrBackground));
        paint.setStrokeWidth((float) (pixelPerMm * 25));
        startY = (float) (pixelPerMm * 17.5);
        stopY = (float) (pixelPerMm * 17.5);
        canvas.drawLine(0, startY, getWidth(), stopY, paint);

        // 画X轴,每隔5mm画一条细线
        paint.setColor(getResources().getColor(R.color.thickLine));
        paint.setStrokeWidth(1.0f);
        startX = 0;
        stopX = width;
        for (int i = 0; i < heightLen; i++) {
            startY = (float) (i * pixelPerMm * 5);
            stopY = (float) (i * pixelPerMm * 5);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        // 画Y轴,每隔10mm画一条细线
        paint.setStrokeWidth(1.0f);
        startY = 0;
        stopY = height;
        for (int i = 0; i < widthLen; i++) {
            startX = (float) (i * pixelPerMm * 10);
            stopX = (float) (i * pixelPerMm * 10);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
        paint.setColor(getResources().getColor(R.color.thickLine));
        // 画X轴,每隔10mm画一条粗线
        paint.setStrokeWidth(2.0f);
        startX = 0;
        stopX = width;
        for (int i = 0; i < 4; i++) {
            startY = (float) (pixelPerMm * (i * 10 + 5));
            stopY = (float) (pixelPerMm * (i * 10 + 5));
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        // 画Y轴,每隔30mm画一条粗线线
        paint.setStrokeWidth(2.0f);
        startY = 0;
        stopY = height;
        for (int i = 0; i < widthLen; i++) {
            startX = (float) (i * pixelPerMm * 30);
            stopX = (float) (i * pixelPerMm * 30);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    /**
     * 画胎心率的刻度
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

        // 单位:像素/mm,1280为屏幕横向像素数，216.96为屏幕横向长度（单位mm）
        double pixelPerMm = 1280 / 216.96;
        // 设置画笔
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.scale));
        paint.setAlpha(150);
        paint.setAntiAlias(true);
        paint.setTextSize(14);
        paint.setStrokeWidth(1);
        stopX = (int) (pixelPerMm * 33);
        stopY = (int) (pixelPerMm * 6.25);
        //绘制刻度的空白处
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth((float) (pixelPerMm * 3));
        startX = (float) (pixelPerMm * 27);
        startY = (float) (pixelPerMm * 4);
        startY = (startY + stopY) / 2;
        canvas.drawLine(startX, startY, stopX, startY, paint);
        startY = startY + (float) (pixelPerMm * 10);
        canvas.drawLine(startX, startY, stopX, startY, paint);
        startY = startY + (float) (pixelPerMm * 10);
        canvas.drawLine(startX, startY, stopX, startY, paint);
        startY = startY + (float) (pixelPerMm * 10);
        canvas.drawLine(startX, startY, stopX, startY, paint);
        // 开始绘制刻度
        paint.setColor(getResources().getColor(R.color.scale));
        startX = (float) (pixelPerMm * 28);
        startY = (float) (pixelPerMm * 6);
        canvas.drawText(scale[3], startX, startY, paint);
        startX = (float) (pixelPerMm * 28);
        startY = (float) (pixelPerMm * 16);
        canvas.drawText(scale[2], startX, startY, paint);
        startX = (float) (pixelPerMm * 28);
        startY = (float) (pixelPerMm * 26);
        canvas.drawText(scale[1], startX, startY, paint);
        startX = (float) (pixelPerMm * 28);
        startY = (float) (pixelPerMm * 36);
        canvas.drawText(scale[0], startX, startY, paint);


    }

    /**
     * 获的作为标题的增益字符串
     *
     * @return 当前增益倍数字符串
     */
    private String getGainString() {
        float gain = MeasureUtils.getSpFloat(getContext(), Configuration.SYS_CONFIG, "xx", 1.0f);

        if (gain == 0.5f) {
            return "x0.5";
        } else if (gain == 0.25f) {
            return "x0.25";
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
