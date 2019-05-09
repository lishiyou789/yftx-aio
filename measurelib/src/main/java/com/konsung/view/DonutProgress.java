package com.konsung.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.konsung.R;
import com.konsung.util.UnitConvertUtil;


/**
 * Created by bruce on 14-10-30.
 */
public class DonutProgress extends View {
    private Paint finishedPaint;
    private Paint unfinishedPaint;
    private Paint innerCirclePaint;
    protected Paint textPaint;
    protected Paint innerBottomTextPaint;

    private RectF finishedOuterRect = new RectF();
    private RectF unfinishedOuterRect = new RectF();

    private float textSize;
    private int textColor;
    private int innerBottomTextColor;
    private int progress = 0;
    private int max;
    private int finishedStrokeColor;
    private int unfinishedStrokeColor;
    private float finishedStrokeWidth;
    private float unfinishedStrokeWidth;
    private int innerBackgroundColor;
    private String prefixText = "";
    private String suffixText = "s";
    private String text = null;
    private float innerBottomTextSize;
    private String innerBottomText;
    private float innerBottomTextHeight;

    private final float defaultStrokeWidth;

    private final int defaultFinishedColor = Color.parseColor("#4DB133");
    private final int defaultUnfinishedColor = Color.rgb(204, 204, 204);
    private final int defaultTextColor = Color.parseColor("#4DB133");
    private final int defaultInnerBottomTextColor = Color.rgb(66, 145, 241);
    private final int defaultInnerBackgroundColor = Color.TRANSPARENT;
    private final int defaultMax = 100;
    private final float defaultTextSize;
    private final float defaultInnerBottomTextSize;
    private final int minSize;


    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_TEXT = "text";
    private static final String INSTANCE_INNER_BOTTOM_TEXT_SIZE = "inner_bottom_text_size";
    private static final String INSTANCE_INNER_BOTTOM_TEXT = "inner_bottom_text";
    private static final String INSTANCE_INNER_BOTTOM_TEXT_COLOR = "inner_bottom_text_color";
    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_PREFIX = "prefix";
    private static final String INSTANCE_FINISHED_STROKE_WIDTH = "finished_stroke_width";
    private static final String INSTANCE_UNFINISHED_STROKE_WIDTH = "unfinished_stroke_width";
    private static final String INSTANCE_BACKGROUND_COLOR = "inner_background_color";

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public DonutProgress(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param attrs   在布局xml文件中为属性赋值
     */
    public DonutProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context      上下文
     * @param attrs        在布局xml文件中为属性赋值
     * @param defStyleAttr 这是当前Theme中的包含的一个指向style的引用.当我们没有给自定义View
     *                     设置declare-styleable资源集合时,默认从这个集合里面查找布局文件中配置属性值.
     *                     传入0表示不向该defStyleAttr中查找默认值
     */
    public DonutProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        defaultTextSize = UnitConvertUtil.spToPx(getResources(), 18); //值转换
        minSize = (int) UnitConvertUtil.dpToPx(getResources(), 100);
        defaultStrokeWidth = UnitConvertUtil.dpToPx(getResources(), 10);
        defaultInnerBottomTextSize = UnitConvertUtil.spToPx(getResources(), 18);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.DonutProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

    /**
     * 初始化画布的方法
     */
    protected void initPainters() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        innerBottomTextPaint = new TextPaint();
        innerBottomTextPaint.setColor(innerBottomTextColor);
        innerBottomTextPaint.setTextSize(innerBottomTextSize);
        innerBottomTextPaint.setAntiAlias(true);

        finishedPaint = new Paint();
        finishedPaint.setColor(finishedStrokeColor);
        finishedPaint.setStyle(Paint.Style.STROKE);
        finishedPaint.setAntiAlias(true);
        finishedPaint.setStrokeWidth(finishedStrokeWidth);

        unfinishedPaint = new Paint();
        unfinishedPaint.setColor(unfinishedStrokeColor);
        unfinishedPaint.setStyle(Paint.Style.STROKE);
        unfinishedPaint.setAntiAlias(true);
        unfinishedPaint.setStrokeWidth(unfinishedStrokeWidth);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(innerBackgroundColor);
        innerCirclePaint.setAntiAlias(true);
    }

    /**
     * 初始化布局xml文件中为属性赋值
     *
     * @param attributes 布局中的属性
     */
    protected void initByAttributes(TypedArray attributes) {
        finishedStrokeColor = attributes.getColor(R.styleable
                .DonutProgress_donut_finished_color, defaultFinishedColor);
        unfinishedStrokeColor = attributes.getColor(R.styleable
                        .DonutProgress_donut_unfinished_color,
                defaultUnfinishedColor);
        textColor = attributes.getColor(R.styleable
                .DonutProgress_donut_text_color, defaultTextColor);
        textSize = attributes.getDimension(R.styleable
                .DonutProgress_donut_text_size, defaultTextSize);

        setMax(attributes.getInt(R.styleable.DonutProgress_donut_max,
                defaultMax));
        setProgress(attributes.getInt(R.styleable
                .DonutProgress_donut_progress, 0));
        finishedStrokeWidth = attributes.getDimension(R.styleable
                        .DonutProgress_donut_finished_stroke_width,
                defaultStrokeWidth);
        unfinishedStrokeWidth = attributes.getDimension(R.styleable
                        .DonutProgress_donut_unfinished_stroke_width,
                defaultStrokeWidth);
        if (attributes.getString(R.styleable.DonutProgress_donut_prefix_text)
                != null) {
            prefixText = attributes.getString(R.styleable
                    .DonutProgress_donut_prefix_text);
        }
        if (attributes.getString(R.styleable.DonutProgress_donut_suffix_text)
                != null) {
            suffixText = attributes.getString(R.styleable
                    .DonutProgress_donut_suffix_text);
        }
        if (attributes.getString(R.styleable.DonutProgress_donut_text) !=
                null) {
            text = attributes.getString(R.styleable.DonutProgress_donut_text);
        }
        innerBackgroundColor = attributes.getColor(R.styleable
                        .DonutProgress_donut_background_color,
                defaultInnerBackgroundColor);

        innerBottomTextSize = attributes.getDimension(R.styleable
                        .DonutProgress_donut_inner_bottom_text_size,
                defaultInnerBottomTextSize);
        innerBottomTextColor = attributes.getColor(R.styleable
                        .DonutProgress_donut_inner_bottom_text_color,
                defaultInnerBottomTextColor);
        innerBottomText = attributes.getString(R.styleable
                .DonutProgress_donut_inner_bottom_text);
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    /**
     * 获取控件显示完毕的宽度
     *
     * @return 宽度
     */
    public float getFinishedStrokeWidth() {
        return finishedStrokeWidth;
    }

    /**
     * 设置控件显示宽度
     *
     * @param finishedStrokeWidth 宽度
     */
    public void setFinishedStrokeWidth(float finishedStrokeWidth) {
        this.finishedStrokeWidth = finishedStrokeWidth;
        this.invalidate();
    }

    /**
     * 获取控件显示未完毕的宽度
     *
     * @return 宽度
     */
    public float getUnfinishedStrokeWidth() {
        return unfinishedStrokeWidth;
    }

    /**
     * 设置未完成控件显示宽度
     *
     * @param unfinishedStrokeWidth 宽度
     */
    public void setUnfinishedStrokeWidth(float unfinishedStrokeWidth) {
        this.unfinishedStrokeWidth = unfinishedStrokeWidth;
        this.invalidate();
    }

    /**
     * 获取控件显示的百分比
     *
     * @return 显示百分比
     */
    private float getProgressAngle() {
        return getProgress() / (float) max * 360f;
    }

    /**
     * 获取当前的进度
     *
     * @return 进度
     */
    public int getProgress() {
        return progress;
    }

    /**
     * 设置当前的进度
     *
     * @param progress 进度
     */
    public void setProgress(int progress) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
        invalidate();
    }

    /**
     * 获取最大进度值
     *
     * @return 最大值
     */
    public int getMax() {
        return max;
    }

    /**
     * 设置最大进度值
     *
     * @param max 最大值
     */
    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    /**
     * 获取文本大小
     *
     * @return 文本大小
     */
    public float getTextSize() {
        return textSize;
    }

    /**
     * 设置文本大小
     *
     * @param textSize 文本大小
     */
    public void setTextSize(float textSize) {
        this.textSize = textSize;
        this.invalidate();
    }

    /**
     * 获取文本颜色
     *
     * @return 文本颜色
     */
    public int getTextColor() {
        return textColor;
    }

    /**
     * 设置文本颜色
     *
     * @param textColor 文本颜色
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.invalidate();
    }

    /**
     * 获取边框颜色
     *
     * @return 边框颜色
     */
    public int getFinishedStrokeColor() {
        return finishedStrokeColor;
    }

    /**
     * 设置边框的颜色
     *
     * @param finishedStrokeColor 边框颜色
     */
    public void setFinishedStrokeColor(int finishedStrokeColor) {
        this.finishedStrokeColor = finishedStrokeColor;
        this.invalidate();
    }

    /**
     * 获取未完成边框颜色
     *
     * @return 边框颜色
     */
    public int getUnfinishedStrokeColor() {
        return unfinishedStrokeColor;
    }

    /**
     * 设置未完成边框的颜色
     *
     * @param unfinishedStrokeColor 边框颜色
     */
    public void setUnfinishedStrokeColor(int unfinishedStrokeColor) {
        this.unfinishedStrokeColor = unfinishedStrokeColor;
        this.invalidate();
    }

    /**
     * 获取文本内容
     *
     * @return 文本内容
     */
    public String getText() {
        return text;
    }

    /**
     * 设置文本内容
     *
     * @param text 文本内容
     */
    public void setText(String text) {
        this.text = text;
        this.invalidate();
    }

    /**
     * 获取绘画的逻辑
     *
     * @return 逻辑
     */
    public String getSuffixText() {
        return suffixText;
    }

    /**
     * 设置绘画的逻辑
     *
     * @param suffixText 逻辑
     */
    public void setSuffixText(String suffixText) {
        this.suffixText = suffixText;
        this.invalidate();
    }

    /**
     * 获取文本的字体
     *
     * @return 字体
     */
    public String getPrefixText() {
        return prefixText;
    }

    /**
     * 设置文本字体
     *
     * @param prefixText 字体
     */
    public void setPrefixText(String prefixText) {
        this.prefixText = prefixText;
        this.invalidate();
    }

    /**
     * 获取背景的颜色
     *
     * @return 颜色
     */
    public int getInnerBackgroundColor() {
        return innerBackgroundColor;
    }

    /**
     * 设置背景颜色
     *
     * @param innerBackgroundColor 背景颜色
     */
    public void setInnerBackgroundColor(int innerBackgroundColor) {
        this.innerBackgroundColor = innerBackgroundColor;
        this.invalidate();
    }

    /**
     * 获取控件地板的文字
     *
     * @return 文本内容
     */
    public String getInnerBottomText() {
        return innerBottomText;
    }

    /**
     * 设置文本大小
     *
     * @param innerBottomText 文本
     */
    public void setInnerBottomText(String innerBottomText) {
        this.innerBottomText = innerBottomText;
        this.invalidate();
    }

    /**
     * 获取底部文本大小
     *
     * @return 文本大小
     */
    public float getInnerBottomTextSize() {
        return innerBottomTextSize;
    }

    /**
     * 设置底部文本的大小
     *
     * @param innerBottomTextSize 文本大小
     */
    public void setInnerBottomTextSize(float innerBottomTextSize) {
        this.innerBottomTextSize = innerBottomTextSize;
        this.invalidate();
    }

    /**
     * 返回控件底部文件的颜色
     *
     * @return 颜色
     */
    public int getInnerBottomTextColor() {
        return innerBottomTextColor;
    }

    /**
     * 控件地板文件的颜色
     *
     * @param innerBottomTextColor 颜色
     */
    public void setInnerBottomTextColor(int innerBottomTextColor) {
        this.innerBottomTextColor = innerBottomTextColor;
        this.invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));

        //TODO calculate aio circle height and then position bottom text at
        // the bottom (3/4)
        innerBottomTextHeight = getHeight() - (getHeight() * 3) / 4;
    }

    /**
     * 输入控件自测的方法
     *
     * @param measureSpec 测试方式
     * @return 控件大小
     */
    private int measure(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = minSize;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float delta = Math.max(finishedStrokeWidth, unfinishedStrokeWidth);
        finishedOuterRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta);
        unfinishedOuterRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta);

        float innerCircleRadius = (getWidth() - Math.min(finishedStrokeWidth,
                unfinishedStrokeWidth) + Math.abs(finishedStrokeWidth -
                unfinishedStrokeWidth)) / 2f;
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f,
                innerCircleRadius, innerCirclePaint);
        canvas.drawArc(finishedOuterRect, 0, getProgressAngle(), false,
                finishedPaint);
        canvas.drawArc(unfinishedOuterRect, getProgressAngle(), 360 -
                getProgressAngle(), false, unfinishedPaint);

       /* String text = this.text != null ? this.text : prefixText + progress
        + suffixText;*/
        String text = this.text != null ? this.text : "";
        if (!TextUtils.isEmpty(text)) {
            float textHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(text, (getWidth() - textPaint.measureText(text))
                    / 2.0f, (getWidth() - textHeight) / 2.0f, textPaint);
        }

        if (!TextUtils.isEmpty(getInnerBottomText())) {
            innerBottomTextPaint.setTextSize(innerBottomTextSize);
            float bottomTextBaseline = getHeight() - innerBottomTextHeight -
                    (textPaint.descent() + textPaint.ascent()) / 2;
            canvas.drawText(getInnerBottomText(), (getWidth() -
                    innerBottomTextPaint.measureText(getInnerBottomText())) /
                    2.0f, bottomTextBaseline, innerBottomTextPaint);
        }

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize());
        bundle.putFloat(INSTANCE_INNER_BOTTOM_TEXT_SIZE,
                getInnerBottomTextSize());
        bundle.putFloat(INSTANCE_INNER_BOTTOM_TEXT_COLOR,
                getInnerBottomTextColor());
        bundle.putString(INSTANCE_INNER_BOTTOM_TEXT, getInnerBottomText());
        bundle.putInt(INSTANCE_INNER_BOTTOM_TEXT_COLOR,
                getInnerBottomTextColor());
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor());
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR,
                getUnfinishedStrokeColor());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_PROGRESS, getProgress());
        bundle.putString(INSTANCE_SUFFIX, getSuffixText());
        bundle.putString(INSTANCE_PREFIX, getPrefixText());
        bundle.putString(INSTANCE_TEXT, getText());
        bundle.putFloat(INSTANCE_FINISHED_STROKE_WIDTH,
                getFinishedStrokeWidth());
        bundle.putFloat(INSTANCE_UNFINISHED_STROKE_WIDTH,
                getUnfinishedStrokeWidth());
        bundle.putInt(INSTANCE_BACKGROUND_COLOR, getInnerBackgroundColor());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            textSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            innerBottomTextSize = bundle.getFloat(INSTANCE_INNER_BOTTOM_TEXT_SIZE);
            innerBottomText = bundle.getString(INSTANCE_INNER_BOTTOM_TEXT);
            innerBottomTextColor = bundle.getInt(INSTANCE_INNER_BOTTOM_TEXT_COLOR);
            finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
            finishedStrokeWidth = bundle.getFloat(INSTANCE_FINISHED_STROKE_WIDTH);
            unfinishedStrokeWidth = bundle.getFloat(INSTANCE_UNFINISHED_STROKE_WIDTH);
            innerBackgroundColor = bundle.getInt(INSTANCE_BACKGROUND_COLOR);
            initPainters();
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            prefixText = bundle.getString(INSTANCE_PREFIX);
            suffixText = bundle.getString(INSTANCE_SUFFIX);
            text = bundle.getString(INSTANCE_TEXT);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
