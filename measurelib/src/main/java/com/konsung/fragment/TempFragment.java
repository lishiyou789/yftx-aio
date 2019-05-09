package com.konsung.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.konsung.R;
import com.konsung.bean.MeasureBean;
import com.konsung.constant.Configuration;
import com.konsung.constant.IrTemp;
import com.konsung.constant.KParamType;
import com.konsung.constant.ProtocolDefine;
import com.konsung.constant.TempDefine;
import com.konsung.listen.AppDeviceListen;
import com.konsung.listen.Measure;
import com.konsung.listen.MeasureCompleteListen;
import com.konsung.net.EchoServerEncoder;
import com.konsung.util.AlarmUtil;
import com.konsung.util.MeasureUtils;
import com.konsung.util.NumInputWatcher;
import com.konsung.util.NumUtil;
import com.konsung.util.PointHelper;
import com.konsung.view.ImageTextButton;

/**
 * 耳温
 * Created by JustRush on 2015/6/17.
 */

public class TempFragment extends Fragment {

    TextView tvIrtempTrend;
    ImageTextButton btnSave;
    RelativeLayout rlContent;
    RelativeLayout rlContentTable;
    ImageView ivIrTemp;
    private Intent mIntent;
    // 是否已经绑定服务
    private boolean isBind;
    // 探头状态
    private boolean isAttach;
    //温度缓存
    //private int temp = -1000;
    //已经得到数据
    private boolean isGetValue;
/*    // 点击开始测量按钮弹出的对话框
    private StartMeasureDialogIrTemp mStartDialog;*/

    // 以下变量使用于模拟测量,不需要时可删除
    private boolean isMeasure;  // 模拟测量时
    /*private int mProgress = 0;*/
    private View view;

    private static final float TEMP_HIGH = 37.3f;
    private static final float TEMP_LOW = 36.2f;
    private MeasureBean measureBean; //当前测量项
    private MeasureCompleteListen l; //监听类

    /**
     * 设置测量类的方法
     *
     * @param measureBean 测量类
     */
    public static TempFragment getInstance(MeasureBean measureBean) {
//        this.measureBean = measureBean;
        TempFragment fragment = new TempFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", measureBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 设置测量完成的监听类
     *
     * @param l 监听的类
     */
    public void setMeasureListen(MeasureCompleteListen l) {
        this.l = l;
    }

    /**
     * 设置测量类的方法
     *
     * @param bean 测量类
     */
    public void setMeasureBean(MeasureBean bean) {
        this.measureBean = bean;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        measureBean = (MeasureBean) getArguments().getSerializable("bean");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ir_temp, null);
        initView();
        tvIrtempTrend.setText(getString(R.string.default_value));
        initEvent();
        // 设置体温类型,此处设置体温类型是为了规避参数板复位的问题
        int value = MeasureUtils.getSpInt(getActivity().getApplicationContext()
                , "sys_config", "temp_type", TempDefine.TEMP_INFRARED);
        EchoServerEncoder.setTempConfig(ProtocolDefine.NET_TEMP_TYPE, value);

        // 为了避免干扰，在心电测量界面已经关闭了体温测量，此处重新打开体温测量
        EchoServerEncoder.setTempConfig(ProtocolDefine.NET_TEMP_START_STOP, TempDefine.TEMP_START);
        EchoServerEncoder.setTempConfig(ProtocolDefine.NET_TEMP_START_STOP, TempDefine.TEMP_START);
        MeasureUtils.setAppDeviceListen(new AppDeviceListen() {
            @Override
            public void sendParaStatus(String name, String version) {

            }

            @Override
            public void sendWave(int param, byte[] bytes) {

            }

            @Override
            public void sendTrend(int param, int value) {

                switch (param) {
                    case KParamType.IRTEMP_TREND:
                        saveTrend(value);
                        /*tvIrtempTrend.setText(String.valueOf(v1));*/
                        /*measureOver(aidlServer);*/
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void sendConfig(int param, int value) {

            }

            @Override
            public void send12LeadDiaResult(byte[] bytes) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        });
        return view;
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        NumInputWatcher watcher = new NumInputWatcher(new PointHelper(2, 1, getActivity()), null
                , new NumInputWatcher.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                String s = text;
                s = s.replaceAll(" ", "");
                if (!s.equals(getString(R.string.default_value)) && s.length() > 0) {
                    try {
                        Float aFloat = Float.valueOf(s);
                        if (aFloat < TEMP_LOW || aFloat > TEMP_HIGH) {
                            tvIrtempTrend.setTextColor(getActivity().getResources()
                                    .getColor(R.color.red));
                        } else {
                            tvIrtempTrend.setTextColor(getActivity().getResources()
                                    .getColor(R.color.mesu_text));
                        }
                    } catch (Exception e) {
                        tvIrtempTrend.setText("");
                        tvIrtempTrend.setTextColor(getActivity().getResources()
                                .getColor(R.color.mesu_text));
                    }
                } else {
                    tvIrtempTrend.setTextColor(getActivity().getResources()
                            .getColor(R.color.mesu_text));
                }
            }

            @Override
            public void onTextEmpty() {

            }
        });
        tvIrtempTrend.addTextChangedListener(watcher);

        tvIrtempTrend.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                EditText textView = (EditText) view;

                String text = textView.getText().toString();
                if (b) {
                    if (text.equals(getString(R.string.default_value))) {
                        textView.setText("");
                    }
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempStr = tvIrtempTrend.getText().toString();
                if (tempStr.length() > 0 && !tempStr.equals(getString(R.string.default_value))) {
                    try {
                        float tempFloat = Float.valueOf(tempStr);
                        int temp = NumUtil.trans2Int(tempFloat, Configuration.TREND_FACTOR);
                        saveTrend(temp);
                        InputMethodManager inputmanger = (InputMethodManager) getActivity()
                                .getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        View view = getActivity().getWindow().peekDecorView();
        if (view != null) {
            //隐藏虚拟键盘
            InputMethodManager inputmanger = (InputMethodManager) getActivity()
                    .getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        MeasureUtils.setAppDeviceListen(null);
    }

    /**
     * 初始化
     */
    private void initView() {
        tvIrtempTrend = (TextView) view.findViewById(R.id.tv_irtemp_trend);
        btnSave = (ImageTextButton) view.findViewById(R.id.btn_save);
        rlContent = (RelativeLayout) view.findViewById(R.id.content);
        rlContentTable = (RelativeLayout) view.findViewById(R.id.content_table);
        ivIrTemp = (ImageView) view.findViewById(R.id.iv_irtemp_trend);
        if (null != measureBean && measureBean.getTemp() != 0.0f) {
            tvIrtempTrend.setText(String.valueOf(measureBean.getTemp()));
            AlarmUtil.executeOverrunAlarm(measureBean.getTemp(), IrTemp.HIGH
                    , IrTemp.LOW, ivIrTemp);
        } else {
            tvIrtempTrend.setText(getString(R.string.default_value));
            tvIrtempTrend.setTextColor(getResources().getColor(R.color.mesu_text));
        }
    }

    /**
     * 保存趋势值
     *
     * @param value 趋势值
     */
    private void saveTrend(int value) {
        if (value != Configuration.INVALID_DATA) {
            isGetValue = true;
            tvIrtempTrend.setText(String.valueOf((float) value / Configuration.TREND_FACTOR));
            float temp = NumUtil.trans2FloatValue((float) value / Configuration.TREND_FACTOR, 1);
            AlarmUtil.executeOverrunAlarm(Float.valueOf(tvIrtempTrend.getText().toString())
                    , IrTemp.HIGH, IrTemp.LOW, ivIrTemp);
            measureBean.setTemp(temp);
            if (null != l) {
                l.onComplete(Measure.TEMP, measureBean);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            EchoServerEncoder.setNibpConfig((short) 0x06, 0);
            isBind = false;
            if (tvIrtempTrend.getText().toString().length() == 0) {
                tvIrtempTrend.setText(getString(R.string.default_value));
            }
            View view = getActivity().getWindow().peekDecorView();
            if (view != null) {
                //隐藏虚拟键盘
                InputMethodManager inputmanger = (InputMethodManager) getActivity()
                        .getSystemService(getActivity().INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else {
            initView();

        }
    }
}
