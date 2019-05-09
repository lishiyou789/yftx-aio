package com.konsung.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.konsung.R;
import com.konsung.bean.MeasureBean;
import com.konsung.constant.Configuration;
import com.konsung.constant.KParamType;
import com.konsung.constant.Spo;
import com.konsung.constant.StoreConstant;
import com.konsung.listen.AppDeviceListen;
import com.konsung.listen.Measure;
import com.konsung.listen.MeasureCompleteListen;
import com.konsung.util.MeasureUtils;
import com.konsung.util.OverProofUtil;
import com.konsung.view.DonutProgress;
import com.konsung.view.ImageTextButton;
import com.konsung.view.WaveFormSpo2;

/**
 * 血氧测量
 * Created by JustRush on 2015/6/16.
 */
public class Spo2Fragment extends Fragment {

    private static final String TAG = "Spo2Fragment";

    TextView tvSpo2View;
    TextView tvNotify;
    TextView tvPrView;
    ImageTextButton btnMeasure;
    WaveFormSpo2 waveFb;
    DonutProgress donutProgress;
    private Intent intent;
    private boolean isBind;
    private int spo2Value;
    private int spo2Pr;
    private int measureCount = 0;
    //spo2 attach status
    private boolean attachSpo2 = false;

    private boolean isChecking = false;

    private boolean isFingerInsert = false;
    private int max = 15;
    private int progress = 15;
    private View view;
    private MeasureBean measureBean; //当前测量项
    private MeasureCompleteListen l; //监听类

    /**
     * 设置测量类的方法
     *
     * @param measureBean 测量类
     */
    public static Spo2Fragment getInstance(MeasureBean measureBean) {
//        this.measureBean = measureBean;
        Spo2Fragment fragment = new Spo2Fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_spo2, null);
        initView();
        initEvent();
        donutProgress.setMax(max);
        waveFb.setSampleRate(125);
        tvSpo2View.setText(getString(R.string.default_value));
        tvPrView.setText(getString(R.string.default_value));
//        if (null != measureBean && measureBean.getSpo2() != 0) {
//            tvSpo2View.setText(String.valueOf(measureBean.getSpo2()));
//        }
//        if (null != measureBean && measureBean.getPr() != 0) {
//            tvPrView.setText(String.valueOf(measureBean.getPr()));
//        }
        spo2Value = 0;
        measureCount = 0;
        /*        handler.post(ecgStatus);*/
        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attachSpo2 && !isChecking && isFingerInsert) {
                    reinit();
                    isChecking = true;
                    restartMeasure();
//                    Log.d("Test", "start");
                    btnMeasure.setText(getString(R.string.nibp_btn_stop));
                    tvNotify.setText(getString(R.string.spo2_isChecking));
                } else {
                    btnMeasure.setText(getString(R.string.nibp_btn_start));
//                    waveFb.reset();
                    reinit();
                    isChecking = false;
                    if (isFingerInsert) {
                        tvNotify.setText(getString(R.string.spo2_waiting));
                    } else if (attachSpo2 && !isFingerInsert) {
                        tvNotify.setText(getString(R.string
                                .spo2_pls_put_finger));
                    } else if (!attachSpo2) {
                        tvNotify.setText(getString(R.string
                                .spo2_pls_put_probe));
                    }
                }
            }
        });
        setSpo2LeffStatus(StoreConstant.leffoff);
        waveFb.reset();
        MeasureUtils.setAppDeviceListen(new AppDeviceListen() {
            @Override
            public void sendParaStatus(String name, String version) {

            }

            @Override
            public void sendWave(int param, byte[] bytes) {
                switch (param) {
                    case KParamType.SPO2_WAVE:
                        waveFb.setData(bytes);
                        if (isChecking) {
                            progress--;
                            if (progress == 0) {
                                isChecking = false;
                                reinit();
                                tvNotify.setText(getString(R.string
                                        .ecg_check_timeout));
                                btnMeasure.setText(getString(R.string
                                        .nibp_btn_start));
                            }
                            donutProgress.setProgress(progress);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void sendTrend(int param, int value) {
                switch (param) {
                    case KParamType.SPO2_TREND:
                        if (isChecking) {
                            if ((Math.abs(spo2Value - value /
                                    Configuration.TREND_FACTOR) < 4)
                                    && value != Configuration.INVALID_DATA) {
                                if ((measureCount++) == 6) {
                                    tvSpo2View.setText(String.valueOf(spo2Value));
                                    tvPrView.setText(String.valueOf(spo2Pr));
                                    measureCount = 0;
//                                        waveFb.reset();
                                    reinit();
                                    //当血氧值小于94时，进行低血氧报警
                                    //添加防止已启动就显示测量完成  --0320
                                    if (spo2Value < Spo.SPO2_LOW && spo2Value > 0) {
                                        tvNotify.setText(getString(R.string.spo2_low_alarm));
                                    } else {
                                        tvNotify.setText(getString(R.string
                                                .spo2_check_complited));
                                    }
                                    measureBean.setSpo2(spo2Value);
                                    measureBean.setPr(spo2Pr);
                                    if (null != l) {
                                        l.onComplete(Measure.SPO2, measureBean);
                                    }
                                    setSpo2LeffStatus(StoreConstant.leffoff);
                                    isChecking = false;
                                    btnMeasure.setText(getString(R.string.nibp_btn_start));
                                    return;
                                }
                            } else {
                                spo2Value = value / Configuration.TREND_FACTOR;
                                measureCount = 0;
                            }
                        }
                        break;
                    case KParamType.SPO2_PR:
                        spo2Pr = value / Configuration.TREND_FACTOR;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void sendConfig(int param, int value) {
//                Log.e("lsy", "param=" + param + ",value=" + value);
                switch (param) {
                    case 0x05:
                        setSpo2LeffStatus(value);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void send12LeadDiaResult(byte[] bytes) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                waveFb.reset();
                waveFb.stop();
                waveFb.invalidate();
                isChecking = false;
                attachSpo2 = false;
                tvNotify.setText(getString(R.string.ecg_pls_checkfordevice));
            }
        });
        return view;
    }

    /**
     * 初始化页面
     */
    private void initView() {
        tvSpo2View = (TextView) view.findViewById(R.id.spo2_trend);
        tvNotify = (TextView) view.findViewById(R.id.spo2_notify);
        tvPrView = (TextView) view.findViewById(R.id.spo2_pr_tv);
        btnMeasure = (ImageTextButton) view.findViewById(R.id.measure_btn);
        waveFb = (WaveFormSpo2) view.findViewById(R.id.wave_form);
        donutProgress = (DonutProgress) view.findViewById(R.id.donut_progress);
//        rlContentSpo2 = (RelativeLayout) view.findViewById(R.id.content_spo2);
//        rlContentHr = (RelativeLayout) view.findViewById(R.id.content_hr);
//        rlContentTable = (RelativeLayout) view.findViewById(R.id.content_table);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tvSpo2View.addTextChangedListener(new OverProofUtil(Spo.SPO2_LOW, Spo.SPO2_HIGH,
                tvSpo2View, getActivity()));
        tvPrView.addTextChangedListener(new OverProofUtil(Spo.PR_LOW, Spo.PR_HIGH, tvPrView,
                getActivity()));
    }

    /**
     * 重置进度条值
     */
    private void reinit() {
        progress = 15;
        donutProgress.setProgress(progress);
//        waveFb.stop();
        waveFb.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        waveFb.stop();
        MeasureUtils.setAppDeviceListen(null);
    }

    /**
     * 重置显示值
     */
    private void restartMeasure() {
        spo2Value = 0;
        measureCount = 0;
    }

    /**
     * 根据设备连接状态处理方法
     *
     * @param value 连接状态
     */
    public void setSpo2LeffStatus(int value) {
        if (value == 0) {
            attachSpo2 = true;
            isFingerInsert = true;
            if (!isChecking) {
                btnMeasure.setEnabled(true);
                tvNotify.setText(getString(R.string.spo2_waiting));
            }
        } else if (value == 1) {
            attachSpo2 = false;
            isChecking = false;
//            waveFb.reset();
            reinit();
            btnMeasure.setEnabled(false);
            tvNotify.setText(getString(R.string.spo2_pls_put_probe));
            btnMeasure.setText(getString(R.string.nibp_btn_start));
        } else if (value == 2) {
            attachSpo2 = true;
            isChecking = false;
            isFingerInsert = false;
//            waveFb.reset();
            reinit();
            btnMeasure.setEnabled(false);
            tvNotify.setText(getString(R.string.spo2_pls_put_finger));
            btnMeasure.setText(getString(R.string.nibp_btn_start));
        }
    }


}
