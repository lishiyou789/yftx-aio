package com.konsung.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.konsung.R;
import com.konsung.bean.MeasureBean;
import com.konsung.constant.Configuration;
import com.konsung.constant.KParamType;
import com.konsung.constant.Nibp;
import com.konsung.listen.AppDeviceListen;
import com.konsung.listen.Measure;
import com.konsung.listen.MeasureCompleteListen;
import com.konsung.net.EchoServerEncoder;
import com.konsung.service.AIDLServer;
import com.konsung.util.AlarmUtil;
import com.konsung.util.MeasureUtils;
import com.konsung.util.OverProofUtil;
import com.konsung.view.ImageTextButton;

/**
 * This file is built by GBK
 * please reload in GBK
 * <p/>
 * 血压
 * Created by JustRush on 2015/6/17.
 */
public class NibpFragment extends Fragment {

    TextView tvDiaView;
    TextView tvSysView;
    TextView tvMapView;
    TextView tvPrView;
    TextView tvCuffView;
    ImageTextButton btnMeasure;
    RelativeLayout rlContentSys;
    RelativeLayout rlContentMap;
    RelativeLayout rlContentDia;
    RelativeLayout rlContentPr;
    RelativeLayout rlContentTable;
    //脉率
    ImageView ivPr;
    //舒张压
    ImageView ivDia;
    //收缩压
    ImageView ivSys;
    private Intent intent = null;              // 用于绑定AIDL
    private boolean isBind = false;            // 是否已经绑定服务
    private int measureState = 0;              // 血压测量状态
    private int cuffStatic;
    private View view;
    public AIDLServer aidlServer;
    //正在测量中
    private boolean isChecking = false;
    private boolean isAttach = false;
    private MeasureBean measureBean; //当前测量项
    private MeasureCompleteListen l; //监听类

    public NibpFragment() {

    }

    /**
     * 设置测量类的方法
     *
     * @param measureBean 测量类
     */
    public static NibpFragment getInstance(MeasureBean measureBean) {

//        this.measureBean = measureBean;
        NibpFragment fragment = new NibpFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", measureBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        measureBean = (MeasureBean) getArguments().getSerializable("bean");
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nibp, null);
        isAttach = true;
        cuffStatic = 0;
        initView();
        initEvent();
        // 初始化底部按钮
        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (measureState == 0) {
                    initView();
                    /* handler.post(updateThread);*/            // 更新数据
                        /*_aidlInterface.sendNibpConfig(0x05, 0);         //
                        发送启动测量指令*/
//                    EchoServerEncoder.setEcgConfig((short)0x05,3);
                    // 暂时通过发送两条命令的方式，解决连续多次快速点击启动测量
                    // 按钮时，偶发下发命令不成功的问题
                    EchoServerEncoder.setNibpConfig((short) 0x05, 0);
                    EchoServerEncoder.setNibpConfig((short) 0x05, 0);
                    cuffStatic = 0;
                    //EchoServerEncoder.setNibpConfig((short) 0x07,1);
                    btnMeasure.setText(getString(R.string.nibp_btn_stop));
                    measureState = 1;
                } else {
                    initView();
                    // _aidlInterface.sendNibpConfig(0x06, 0);         //
                    // 发送停止测量指令
                    // 暂时通过发送两条命令的方式，解决连续多次快速点击启动测量
                    // 按钮时，偶发下发命令不成功的问题
                    EchoServerEncoder.setNibpConfig((short) 0x06, 0);
                    EchoServerEncoder.setNibpConfig((short) 0x06, 0);
                    btnMeasure.setText(getString(R.string.nibp_btn_start));
                    /*handler.removeCallbacks(updateThread);*/
                    measureState = 0;
                }
            }
        });
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
                    case KParamType.NIBP_SYS:
                        if (value > 0) {
                            tvSysView.setText(String.valueOf(value /
                                    Configuration.TREND_FACTOR));
                            AlarmUtil.executeOverrunAlarm(Float.valueOf(tvSysView.getText()
                                    .toString()), Nibp.SYS_HIGH, Nibp.SYS_LOW, ivSys);
                            measureBean.setSbp(value / Configuration.TREND_FACTOR);
                        }
                        break;
                    case KParamType.NIBP_DIA:
                        if (measureState == 2) {
                            if (value > 0) {
                                tvDiaView.setText(String.valueOf(value /
                                        Configuration.TREND_FACTOR));
                                AlarmUtil.executeOverrunAlarm(Float.valueOf(tvDiaView.getText()
                                        .toString()), Nibp.DIA_HIGH, Nibp.DIA_LOW, ivDia);
                                btnMeasure.setText(getString(R.string.nibp_btn_start));
                                showMeasureResult(0);
                                measureState = 0;
                                measureBean.setDbp(value / Configuration.TREND_FACTOR);
                            }
                        }
                        break;
                    case KParamType.NIBP_MAP:
                        if (value > 0) {
                            tvMapView.setText(String.valueOf(value / Configuration
                                    .TREND_FACTOR));
                            measureBean.setMbp(value / Configuration.TREND_FACTOR);
                        }
                        break;
                    case KParamType.NIBP_PR:
                        if (value > 0) {
                            tvPrView.setText(String.valueOf(value /
                                    Configuration.TREND_FACTOR));
                            AlarmUtil.executeOverrunAlarm(Float.valueOf(tvPrView
                                    .getText().toString()), Nibp.PR_HIGH, Nibp.PR_LOW, ivPr);
                            measureBean.setNibPr(value / Configuration.TREND_FACTOR);
                            if (null != l) {
                                l.onComplete(Measure.NIBP, measureBean);
                            }
                            redrawChart();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void sendConfig(int param, int value) {
                switch (param) {
                    case 0x07:
                        if (measureState == 1) {
                            if (value == 1) {
                                measureState = 2;
                            }
                        }
                        break;
                    case 0x02:
                        if (value > 0) {
                            measureState = 0;
                            btnMeasure.setText(getString(R.string
                                    .nibp_btn_start));
                            /*Log.d("Test","0x02 = "+value);*/
                            showMeasureResult(value);
                        }
                        break;
                    case 0x04:
                        if (measureState == 2) {
                            tvCuffView.setText(getString(R.string
                                    .nibp_cuff) + ":" + (value ==
                                    Configuration.INVALID_DATA ?
                                    cuffStatic : String.valueOf(value)));
                        }
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

            }
        });
        return view;
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tvSysView.addTextChangedListener(new OverProofUtil(Nibp.SYS_LOW, Nibp.SYS_HIGH,
                tvSysView, getActivity()));
        tvDiaView.addTextChangedListener(new OverProofUtil(Nibp.DIA_LOW, Nibp.DIA_HIGH,
                tvDiaView, getActivity()));
        tvMapView.addTextChangedListener(new OverProofUtil(Nibp.MAP_LOW, Nibp.MAP_HIGH,
                tvMapView, getActivity()));
        tvPrView.addTextChangedListener(new OverProofUtil(Nibp.PR_LOW, Nibp.PR_HIGH, tvPrView,
                getActivity()));
    }

    /**
     * 重绘折现图
     */
    private void redrawChart() {
        rlContentMap.removeAllViews();
        rlContentDia.removeAllViews();
        rlContentPr.removeAllViews();
        rlContentTable.removeAllViews();
        rlContentSys.removeAllViews();
    }

    /**
     * 初始化
     */
    private void initView() {
        tvDiaView = (TextView) view.findViewById(R.id.nibp_dia_tv);
        tvSysView = (TextView) view.findViewById(R.id.nibp_sys_tv);
        tvMapView = (TextView) view.findViewById(R.id.nibp_map_tv);
        tvPrView = (TextView) view.findViewById(R.id.nibp_pr_tv);
        tvCuffView = (TextView) view.findViewById(R.id.nibp_cuff);
        btnMeasure = (ImageTextButton) view.findViewById(R.id.measure_btn);
        rlContentSys = (RelativeLayout) view.findViewById(R.id.content_sys);
        rlContentMap = (RelativeLayout) view.findViewById(R.id.content_map);
        rlContentDia = (RelativeLayout) view.findViewById(R.id.content_dia);
        rlContentPr = (RelativeLayout) view.findViewById(R.id.content_pr);
        rlContentTable = (RelativeLayout) view.findViewById(R.id.content_table);
        ivPr = (ImageView) view.findViewById(R.id.iv_nibp_pr);
        ivDia = (ImageView) view.findViewById(R.id.iv_nibp_dia);
        ivSys = (ImageView) view.findViewById(R.id.iv_nibp_sys);
        tvSysView.setText(getString(R.string.default_value));
        tvDiaView.setText(getString(R.string.default_value));
        tvMapView.setText(getString(R.string.default_value));
        tvPrView.setText(getString(R.string.default_value));
        tvCuffView.setText("");
        //收缩压
        if (null != measureBean && measureBean.getSbp() != 0) {
            tvSysView.setText(String.valueOf(measureBean.getSbp()));
            AlarmUtil.executeOverrunAlarm(measureBean.getSbp()
                    , Nibp.SYS_HIGH, Nibp.SYS_LOW, ivSys);
        }
        //舒张压
        if (null != measureBean && measureBean.getDbp() != 0) {
            tvDiaView.setText(String.valueOf(measureBean.getDbp()));
            AlarmUtil.executeOverrunAlarm(measureBean.getDbp()
                    , Nibp.DIA_HIGH, Nibp.DIA_LOW, ivDia);
        }

        //脉率
        if (null != measureBean && measureBean.getNibPr() != 0) {
            tvPrView.setText(String.valueOf(measureBean.getNibPr()));
            AlarmUtil.executeOverrunAlarm(measureBean.getNibPr(), Nibp.PR_HIGH, Nibp.PR_LOW, ivPr);
        }
    }

    /**
     * 显示测量结果
     *
     * @param code 结果值
     */
    private void showMeasureResult(int code) {
        String result = new String();
        switch (code) {
            case 0:
                result = getString(R.string.nibbp_result_0);
                break;
            case 1:
                result = getString(R.string.nibbp_result_1);
                break;
            case 2:
                result = getString(R.string.nibbp_result_2);
                break;
            case 3:
                result = getString(R.string.nibbp_result_3);
                break;
            case 4:
                result = getString(R.string.nibbp_result_4);
                break;
            case 5:
                result = getString(R.string.nibbp_result_5);
                break;
            case 6:
                result = getString(R.string.nibbp_result_6);
                break;
            case 7:
            case 8:
                result = getString(R.string.nibbp_result_7);
                break;
            case 9:
                result = getString(R.string.nibbp_result_8);
                break;
            case 10:
                result = getString(R.string.nibbp_result_9);
                break;
            case 11:
                result = getString(R.string.nibbp_result_10);
                break;
            case 12:
                result = getString(R.string.nibbp_result_11);
                break;
            case 13:
                result = getString(R.string.nibbp_result_12);
                break;
            default:
                break;
        }
        tvCuffView.setText(result);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EchoServerEncoder.setNibpConfig((short) 0x06, 0);
        MeasureUtils.setAppDeviceListen(null);
    }
}
