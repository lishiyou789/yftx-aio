package com.konsung.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.konsung.R;
import com.konsung.constant.StoreConstant;
import com.konsung.util.MeasureUtils;

/**
 * Created by JustRush on 2015/8/25.
 */
public class EcgSettingDialog extends Dialog {
    private Context context;
    private String title = "";

    private View contentView;
    private View rootView;

    ImageTextButton btnCommit;
    ImageTextButton btnCancel;
    TextView tvDialogTitle;
    Spinner spEcgMm;
    Spinner spEcgXx;

    private UpdataButtonState mLinster;

    /**
     * 监听回调的接口
     */
    public interface UpdataButtonState {
        /**
         * 点击按钮的回调
         *
         * @param pressed 回调标志
         */
        void getButton(Boolean pressed);
    }

    /**
     * 构造方法
     *
     * @param context  上下文
     * @param title    显示标题内容
     * @param mLinster 监听事件
     */
    public EcgSettingDialog(Context context, String title, UpdataButtonState
            mLinster) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;
        this.title = title;
        this.mLinster = mLinster;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ecg_setting);
        contentView = findViewById(R.id.dg_content);
        rootView = findViewById(R.id.dg_rootview);
        btnCommit = (ImageTextButton) findViewById(R.id.btn_commit);
        btnCancel = (ImageTextButton) findViewById(R.id.btn_cancel);
        tvDialogTitle = (TextView) findViewById(R.id.tv_dialog_title);
        spEcgMm = (Spinner) findViewById(R.id.sp_ecg_mm);
        spEcgXx = (Spinner) findViewById(R.id.sp_ecg_xx);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setECGmm(spEcgMm.getSelectedItemPosition());
                setECGxx(spEcgXx.getSelectedItemPosition());
                mLinster.getButton(true);
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinster.getButton(false);
                dismiss();
            }
        });
        tvDialogTitle.setText(title);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                context, R.array.mm_list, R.layout.spinner_button);
        spAdapter.setDropDownViewResource(R.layout.flat_list_item);
        spEcgMm.setAdapter(spAdapter);

        ArrayAdapter<CharSequence> xxAdapter = ArrayAdapter.createFromResource(
                context, R.array.xx_list, R.layout.spinner_button);
        // Specify the layout to use when the list of choices appears
        xxAdapter.setDropDownViewResource(R.layout.flat_list_item);
        spEcgXx.setAdapter(xxAdapter);

        init();
    }

    @Override
    public void show() {
        super.show();
        // set dialog enter animations
        contentView.startAnimation(AnimationUtils.loadAnimation(context, R
                .anim.dialog_main_show_amination));
        rootView.startAnimation(AnimationUtils.loadAnimation(context, R.anim
                .dialog_root_show_amin));
    }

    @Override
    public void dismiss() {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim
                .dialog_main_hide_amination);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                contentView.post(new Runnable() {
                    @Override
                    public void run() {
                        EcgSettingDialog.super.dismiss();
                    }
                });
            }
        });

        Animation backAnim = AnimationUtils.loadAnimation(context, R.anim
                .dialog_root_hide_amin);
        contentView.startAnimation(anim);
        rootView.startAnimation(backAnim);
    }

    /**
     * 设置标题内容的类
     *
     * @param title 标题内容
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 设置ECG出纸速度
     *
     * @param position 索引
     * @return 当前保存的值
     */
    private float setECGmm(int position) {
        switch (position) {
            case 0:
                StoreConstant.ecgRate = 0.2f;

                MeasureUtils.saveToSp(context, "sys_config", "mm", 0.2f);

                return 0.2f;
            case 1:
                StoreConstant.ecgRate = 0.25f;
                MeasureUtils.saveToSp(context, "sys_config", "mm", 0.25f);
                return 0.25f;

            case 2:
                StoreConstant.ecgRate = 0.4f;
                MeasureUtils.saveToSp(context, "sys_config", "mm", 0.4f);
                return 0.4f;

            case 3:
                StoreConstant.ecgRate = 0.5f;
                MeasureUtils.saveToSp(context, "sys_config", "mm", 0.5f);
                return 0.5f;

            case 4:
                StoreConstant.ecgRate = 1.0f;
                MeasureUtils.saveToSp(context, "sys_config", "mm", 1.0f);
                return 1.0f;

            case 5:
                StoreConstant.ecgRate = 2.0f;
                MeasureUtils.saveToSp(context, "sys_config", "mm", 2.0f);
                return 2.0f;

            default:
                StoreConstant.ecgRate = 1.0f;
                MeasureUtils.saveToSp(context, "sys_config", "mm", 1.0f);
                return 1.0f;
        }
    }

    /**
     * 获取当前出纸速度
     *
     * @param mm 出纸速度
     * @return 走纸速度
     */
    private int getECGmm(float mm) {
        if (mm == 0.2f) {
            return 1;
        } else if (mm == 0.25f) {
            return 2;
        } else if (mm == 0.4f) {
            return 3;
        } else if (mm == 0.5f) {
            return 4;
        } else if (mm == 1.0f) {
            return 5;
        } else if (mm == 2.0f) {
            return 6;
        } else {
            return 5;
        }
    }

    /**
     * 设置ECG出纸增幅
     *
     * @param position 索引
     * @return 当前保存的值
     */
    private float setECGxx(int position) {
        switch (position) {
            case 0:
                StoreConstant.ecgGrowth = 0.5f;
                MeasureUtils.saveToSp(context, "sys_config", "xx", 0.5f);
                return 0.5f;

            case 1:
                StoreConstant.ecgGrowth = 1.0f;
                MeasureUtils.saveToSp(context, "sys_config", "xx", 1.0f);

                return 1.0f;
            case 2:
                StoreConstant.ecgGrowth = 2.0f;
                MeasureUtils.saveToSp(context, "sys_config", "xx", 2.0f);

                return 2.0f;
            case 3:
                StoreConstant.ecgGrowth = -1000f;
                MeasureUtils.saveToSp(context, "sys_config", "xx", -1000f);

                return -1000f;
            default:
                StoreConstant.ecgGrowth = 1.0f;
                MeasureUtils.saveToSp(context, "sys_config", "xx", 1.0f);
                return 1.0f;
        }
    }

    /**
     * 获取当前出纸速度
     *
     * @param xx 出纸速度
     * @return 速度
     */
    private int getECGxx(float xx) {
        if (xx == 0.5f) {
            return 1;
        } else if (xx == 1.0f) {
            return 2;
        } else if (xx == 2.0f) {
            return 3;
        } else if (xx == -1000f) {
            return 4;
        } else {
            return 1;
        }
    }

    /**
     * 初始化
     */
    public void init() {
        if (spEcgMm == null || spEcgXx == null) {
            return;
        }
        spEcgMm.setSelection(getECGmm(MeasureUtils.getSpFloat(context.getApplicationContext(),
                "sys_config", "mm", 1.0f)) - 1);
        spEcgXx.setSelection(getECGxx(MeasureUtils.getSpFloat(context.getApplicationContext(),
                "sys_config", "xx", 1.0f)) - 1);
    }
}
