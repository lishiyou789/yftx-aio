package com.konsung.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.konsung.R;
import com.konsung.bean.MeasureBean;
import com.konsung.constant.Configuration;
import com.konsung.constant.DiagCodeToText;
import com.konsung.constant.EcgDefine;
import com.konsung.constant.KParamType;
import com.konsung.constant.ProtocolDefine;
import com.konsung.constant.StoreConstant;
import com.konsung.constant.TempDefine;
import com.konsung.listen.AppDeviceListen;
import com.konsung.listen.Measure;
import com.konsung.listen.MeasureCompleteListen;
import com.konsung.net.EchoServerEncoder;
import com.konsung.util.MeasureUtils;
import com.konsung.util.UnitConvertUtil;
import com.konsung.view.DonutProgress;
import com.konsung.view.EcgSettingDialog;
import com.konsung.view.EcgViewFor12;


/**
 * 12导心电波形界面
 */
public class Ecg12Fragment extends Fragment {
    TextView tvHrView;
    TextView tvBrView;
    TextView tvEcgAlarm;
    TextView tvStatusView;
    TextView tvMeasure;
    EcgViewFor12 ecgView;
    DonutProgress donutProgress;
    TextView tvEcgSetting;

    public boolean isBind;
    private int currentValue = 0; // 记录AppDevice传人的值
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            final byte[] data = (byte[]) msg.obj;
            saveWave(msg.arg1, data);
        }
    };

    private View view;
    private boolean isChecking = false; // 是否已经开始测量
    private boolean isEcgConnect = false; // ecg是否有连接
    private int max = 17; // 进度条最大值
    private int progress = 17; // 进度条默认值
    private boolean isTimeOut = false;
    private boolean isShowStart = false; //记录启动按钮是否可以按
    private int downLL = 0; //记录LL导联脱落的标记
    public Intent intent;
    private MeasureBean measureBean; //当前测量的bean
    private MeasureCompleteListen l; //监听类
    private EcgSettingDialog dialog;

    /**
     * 设置测量类的方法
     *
     * @param bean 测量类
     */
    public void setMeasureBean(MeasureBean bean) {
        this.measureBean = bean;
    }

    /**
     * 构造方法
     *
     * @param bean 测量bean
     */
    public static Ecg12Fragment getInstance(MeasureBean bean) {
        Ecg12Fragment fragment = new Ecg12Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ecg12, null);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(), R.array.mm_list, R.layout.mm_spinner);
        spAdapter.setDropDownViewResource(R.layout.mm_list_item);
        init();
        tvEcgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = new EcgSettingDialog(getActivity(),
                            getActivity().getString(R.string.ecg_setting), new
                            EcgSettingDialog.UpdataButtonState() {
                                @Override
                                public void getButton(Boolean pressed) {
                                    if (pressed) {
                                        resetView();
                                    }
                                }
                            });
                }
                dialog.show();
            }
        });

        // 设置心电导联类型，旧版本的硬件，拔交流电时会导致参数板断电重启，为规避此问题，在进入心电
        // 界面时再发送一次导联设置命令
        int value = MeasureUtils.getSpInt(getActivity().getApplicationContext(), "sys_config",
                "ecg_lead_system", EcgDefine.ECG_12_LEAD);
        EchoServerEncoder.setEcgConfig(ProtocolDefine.NET_ECG_LEAD_SYSTEM, value);

        // 为了避免硬件上体温对心电的干扰，此处关闭体温测量
        EchoServerEncoder.setTempConfig(ProtocolDefine.NET_TEMP_START_STOP, TempDefine.TEMP_STOP);
        EchoServerEncoder.setTempConfig(ProtocolDefine.NET_TEMP_START_STOP, TempDefine.TEMP_STOP);

        tvMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //&&isEcgConnect
                if (!isChecking && isEcgConnect) {
                    //启动12导诊断
                    EchoServerEncoder.setEcgConfig((short) 0x15, 1);
                    tvMeasure.setText(getActivity().getString(R.string.nibp_btn_stop));
                    tvEcgSetting.setEnabled(false);
                    isChecking = true;
                    if (isChecking) {
                        tvStatusView.setText(getActivity().getString(R.string
                                .ecg_pls_keep_quiet_while_check));
                    }
                } else {
                    tvMeasure.setText(getActivity().getString(R.string.nibp_btn_start));
                    progress = 17;
                    donutProgress.setProgress(max);
                    tvEcgSetting.setEnabled(true);
                    isChecking = false;
                }
            }
        });
        setEcgConnectStatus(StoreConstant.leffoff);
        MeasureUtils.setAppDeviceListen(new AppDeviceListen() {
            @Override
            public void sendParaStatus(String name, String version) {
            }

            @Override
            public void sendWave(int param, byte[] bytes) {

                switch (param) {
                    case KParamType.ECG_I:
                        if (ecgView.isRunning) {
                            for (int i = 0; i < bytes.length / 2; i++) {
                                ecgView.addEcgData0((bytes[i * 2] & 0xFF)
                                        + ((bytes[i * 2 + 1] & 0x0F) << 8));

                            }
                        }

                        if (isChecking) {
                            //绘图
                            Message msg = Message.obtain();
                            msg.arg1 = 1;
                            msg.obj = bytes;
                            //发送数据到Handler保存
                            handler.sendMessage(msg);
                        }
                        break;
                    case KParamType.ECG_II:
                        if (ecgView.isRunning) {
                            for (int i = 0; i < bytes.length / 2; i++) {
                                ecgView.addEcgData1((bytes[i * 2] & 0xFF)
                                        + ((bytes[i * 2 + 1] & 0x0F) << 8));
                                if (downLL == 2) {
                                    ecgView.addEcgData1(2048);
                                }

                            }
                        }

                        if (isChecking) {
                            Message msg = Message.obtain();
                            msg.arg1 = 2;
                            msg.obj = bytes;
                            handler.sendMessage(msg);
                        }

                        break;
                    case KParamType.ECG_III:
                        if (ecgView.isRunning) {
                            for (int i = 0; i < bytes.length / 2; i++) {
                                ecgView.addEcgData2((bytes[i * 2] & 0xFF)
                                        + ((bytes[i * 2 + 1] & 0x0F) << 8));
                                if (downLL == 2) {
                                    ecgView.addEcgData2(2048);
                                }

                            }
                        }

                        if (isChecking) {
                            Message msg = Message.obtain();
                            msg.arg1 = 3;
                            msg.obj = bytes;
                            handler.sendMessage(msg);
                        }
                        break;
                    case KParamType.ECG_AVR:
                        if (ecgView.isRunning) {
                            for (int i = 0; i < bytes.length / 2; i++) {
                                ecgView.addEcgData3((bytes[i * 2] & 0xFF)
                                        + ((bytes[i * 2 + 1] & 0x0F) << 8));
                                if (downLL == 2) {
                                    ecgView.addEcgData3(2048);
                                }
                            }

                        }

                        if (isChecking) {
                            Message msg = Message.obtain();
                            msg.arg1 = 4;
                            msg.obj = bytes;
                            handler.sendMessage(msg);
                            if (isChecking) {
                                progress--;
                                donutProgress.setProgress(progress);
                                if (progress == 0) {
                                    isChecking = false;
                                    isTimeOut = true;
                                    tvMeasure.setText(getActivity().getString(R.string
                                            .nibp_btn_start));
                                    tvEcgSetting.setEnabled(true);
                                    tvStatusView.setText(getActivity().getString(R.string
                                            .ecg_check_timeout));
                                    reinit();
                                }
                            }
                        }
                        break;
                    case KParamType.ECG_AVL:
                        if (ecgView.isRunning) {
                            for (int i = 0; i < bytes.length / 2; i++) {
                                ecgView.addEcgData4((bytes[i * 2] & 0xFF)
                                        + ((bytes[i * 2 + 1] & 0x0F) << 8));
                                if (downLL == 2) {
                                    ecgView.addEcgData4(2048);
                                }
                            }
                        }

                        if (isChecking) {
                            Message msg = Message.obtain();
                            msg.arg1 = 5;
                            msg.obj = bytes;
                            handler.sendMessage(msg);
                        }
                        break;
                    case KParamType.ECG_AVF:
                        if (ecgView.isRunning) {
                            for (int i = 0; i < bytes.length / 2; i++) {
                                ecgView.addEcgData5((bytes[i * 2] & 0xFF)
                                        + ((bytes[i * 2 + 1] & 0x0F) << 8));
                                if (downLL == 2) {
                                    ecgView.addEcgData5(2048);
                                }
                            }
                        }

                        if (isChecking) {
                            Message msg = Message.obtain();
                            msg.arg1 = 6;
                            msg.obj = bytes;
                            handler.sendMessage(msg);
                        }
                        break;

                    case KParamType.ECG_V1:
                        if (ecgView.isRunning) {
                            for (int i = 0; i < bytes.length / 2; i++) {
                                ecgView.addEcgData6((bytes[i * 2] & 0xFF)
                                        + ((bytes[i * 2 + 1] & 0x0F) << 8));
                                if (downLL == 2) {
                                    ecgView.addEcgData6(2048);
                                }
                            }
                        }

                        if (isChecking) {
                            Message msg = Message.obtain();
                            msg.arg1 = 7;
                            msg.obj = bytes;
                            handler.sendMessage(msg);
                        }
                        break;
                    case KParamType.ECG_V2:
                        if (ecgView.isRunning) {
                            for (int i = 0; i < bytes.length / 2; i++) {
                                ecgView.addEcgData7((bytes[i * 2] & 0xFF)
                                        + ((bytes[i * 2 + 1] & 0x0F) << 8));
                                if (downLL == 2) {
                                    ecgView.addEcgData7(2048);
                                }
                            }
                        }

                        if (isChecking) {
                            Message msg = Message.obtain();
                            msg.arg1 = 8;
                            msg.obj = bytes;
                            handler.sendMessage(msg);
                        }
                        break;
                    case KParamType.ECG_V3:
                        if (ecgView.isRunning) {
                            for (int i = 0; i < bytes.length / 2; i++) {
                                ecgView.addEcgData8((bytes[i * 2] & 0xFF)
                                        + ((bytes[i * 2 + 1] & 0x0F) << 8));
                                if (downLL == 2) {
                                    ecgView.addEcgData8(2048);
                                }
                            }
                        }

                        if (isChecking) {
                            Message msg = Message.obtain();
                            msg.arg1 = 9;
                            msg.obj = bytes;
                            handler.sendMessage(msg);
                        }
                        break;
                    case KParamType.ECG_V4:
                        if (ecgView.isRunning) {
                            for (int i = 0; i < bytes.length / 2; i++) {
                                ecgView.addEcgData9((bytes[i * 2] & 0xFF)
                                        + ((bytes[i * 2 + 1] & 0x0F) << 8));
                                if (downLL == 2) {
                                    ecgView.addEcgData9(2048);
                                }
                            }
                        }

                        if (isChecking) {
                            Message msg = Message.obtain();
                            msg.arg1 = 10;
                            msg.obj = bytes;
                            handler.sendMessage(msg);
                        }
                        break;
                    case KParamType.ECG_V5:
                        if (ecgView.isRunning) {
                            for (int i = 0; i < bytes.length / 2; i++) {
                                ecgView.addEcgData10((bytes[i * 2] & 0xFF)
                                        + ((bytes[i * 2 + 1] & 0x0F) << 8));
                                if (downLL == 2) {
                                    ecgView.addEcgData10(2048);
                                }
                            }
                        }

                        if (isChecking) {
                            Message msg = Message.obtain();
                            msg.arg1 = 11;
                            msg.obj = bytes;
                            handler.sendMessage(msg);
                        }
                        break;
                    case KParamType.ECG_V6:
                        if (ecgView.isRunning) {
                            for (int i = 0; i < bytes.length / 2; i++) {
                                ecgView.addEcgData11((bytes[i * 2] & 0xFF)
                                        + ((bytes[i * 2 + 1] & 0x0F) << 8));
                                if (downLL == 2) {
                                    ecgView.addEcgData11(2048);
                                }
                            }
                        }

                        if (isChecking) {
                            Message msg = Message.obtain();
                            msg.arg1 = 12;
                            msg.obj = bytes;
                            handler.sendMessage(msg);
                        }
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void sendTrend(int param, int value) {
                switch (param) {
                    case KParamType.ECG_HR:
                        // HR已改为从12导诊断结果获取，不再从此趋势值获取
                        break;
                    case KParamType.RESP_RR:
                        if (value != Configuration.INVALID_DATA) {
                            measureBean.setRESP(value / Configuration.TREND_FACTOR);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void sendConfig(int param, int value) {
                switch (param) {
                    case 0x10:
                        setEcgConnectStatus(value);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void send12LeadDiaResult(byte[] bytes) {
                String diaResult = " "; // 12导诊断结果
                Log.e("p", "isChecking = " + isChecking);
                // 只有在测量过程中才对12导诊断结果进行处理，用户停止测量、
                // 重新刷新界面等情况下不对12导诊断结果进行处理。
                if (!isChecking) {
                    return;
                }

                //根据AppDevice中的协议，诊断结果最多有26个数据（不包括时间戳）
                if (bytes.length != 52) {
                    Log.v("HealthOne", "12 Lead Dia Result len is not right!");
                }
                int[] result = new int[26];

                for (int i = 0; i < 26; i++) {
                    result[i] = (short) (((bytes[i * 2 + 1] & 0x00FF)
                            << 8) | (0x00FF & bytes[i * 2]));
                }
                int hrValue = result[0]; // HR值
                int prInterval = result[1]; // PR间期
                int qrsDuration = result[2]; // QRS间期, 单位ms
                int qt = result[3]; // QT间期
                int qtc = result[4]; // QTC间期
                int pAxis = result[5]; // P 波轴
                int qrsAxis = result[6]; // QRS波心电轴
                int tAxis = result[7]; // T波心电轴
                int rv5 = result[8]; // RV5, 单位0.01ms
                int sv1 = result[9]; // SV1, 单位0.01ms
                if (prInterval < 0) {
                    prInterval = (short) -prInterval;
                }

                diaResult = String.valueOf(hrValue) + "," + String
                        .valueOf(prInterval) + ","
                        + String.valueOf(qrsDuration) + "," + String
                        .valueOf(qt) + ","
                        + String.valueOf(qtc) + "," + String.valueOf(pAxis) + ","
                        + String.valueOf(qrsAxis) + "," + String.valueOf(tAxis) + ","
                        + String.format("%.2f", (float) rv5 / 100) + ","
                        + String.format("%.2f", (float) sv1 / 100) + ","
                        + String.format("%.2f", ((float) rv5 / 100 +
                        (float) sv1 / 100)) + ",";
                //根据AppDevice协议，诊断码有16个，但不是所有都有效
                DiagCodeToText diagCodeToText = new DiagCodeToText();
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < DiagCodeToText.ecg12LeadDiagText.length; j++) {
                        String[] str = DiagCodeToText.ecg12LeadDiagText[j].split(":");
                        if (result[10 + i] == Integer.parseInt(str[0])) {
                            diaResult += str[1];
                            if ((str[1] != null) && (!"".equals(str[1]))) {
                                diaResult += ";";
                            }
                        }
                    }
                }
                measureBean.setWaveSpeed(ecgView.getWaveSpeed());
                measureBean.setWaveGain(ecgView.getWaveGain());
                // 心电测量完成
                measureFinish(hrValue, diaResult);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                reinit();
                isChecking = false;
                isEcgConnect = false;
                tvStatusView.setText(getActivity().getString(R.string.ecg_pls_checkfordevice));
            }
        });
        return view;
    }


    /**
     * 刷新界面的方法
     */
    private void reinit() {
        init();
    }

    /**
     * 算新心电的方法
     */
    private void resetView() {
        ecgView.setWaveSpeed();
        ecgView.setWaveGain();
    }

    /**
     * 初始化
     */
    private void init() {
        tvHrView = (TextView) view.findViewById(R.id.ecg_hr_tv);
        tvBrView = (TextView) view.findViewById(R.id.ecg_br_tv);
        tvEcgAlarm = (TextView) view.findViewById(R.id.tv_ecg_alarm);
        tvStatusView = (TextView) view.findViewById(R.id.ecg_notify);
        tvMeasure = (TextView) view.findViewById(R.id.measure_btn);
        ecgView = (EcgViewFor12) view.findViewById(R.id.ecg_view);
        donutProgress = (DonutProgress) view.findViewById(R.id.donut_progress);
        tvEcgSetting = (TextView) view.findViewById(R.id.ecg_setting);
        progress = 17;
        donutProgress.setProgress(max);
        donutProgress.setMax(max);
        tvHrView.setText(getActivity().getString(R.string.default_value));
        tvBrView.setText(getActivity().getString(R.string.default_value));
        if (null != measureBean && measureBean.getHr() != 0) {
            tvHrView.setText(measureBean.getHr() + "");
        }

    }

    /**
     * 执行心率报警
     *
     * @param hrValue 心率值
     */
    private void executeHrAlarm(int hrValue) {
        if (hrValue > Configuration.HR_ALARM_HIGH) {
            tvEcgAlarm.setText(getString(R.string.heart_rate_high));
        } else if (hrValue < Configuration.HR_ALARM_LOW) {
            tvEcgAlarm.setText(getString(R.string.heart_rate_low));
        } else {
            tvEcgAlarm.setText("");
        }
    }

    /**
     * 设置心电模拟器连接状态
     *
     * @param value 导联脱落状态值
     */
    public void setEcgConnectStatus(int value) {
        int leadoff = -1;
        if (2 == value && currentValue == 0 || 1 == value && currentValue == 0) {
            currentValue = 1;
            return;
        } else {
            currentValue = 0;
        }
        leadoff = value;
        if (leadoff == 0 || leadoff == 496) {
            if (downLL == 1) {
                downLL = 2;
            }
            if (!isShowStart) {
                tvMeasure.setEnabled(true);
            }
            isEcgConnect = true;
            if (!isChecking && !isTimeOut) {
                tvStatusView.setText(getString(R.string.ecg_check_ready));
            } else {
                tvStatusView.setText(getString(R.string
                        .ecg_pls_keep_quiet_while_check));
            }
        } else if (leadoff == Configuration.INVALID_DATA) {
            tvMeasure.setEnabled(false);
            isEcgConnect = false;
            isChecking = false;
            reinit();
            progress = 17;
            donutProgress.setProgress(max);
            isChecking = false;
            tvMeasure.setText(getString(R.string.nibp_btn_start));
            tvStatusView.setText(getString(R.string.ecg_pls_checkfordevice));
        } else {
            if (isChecking) {
                tvEcgSetting.setEnabled(true);
            }
            progress = 17;
            donutProgress.setProgress(max);
            isChecking = false;
            tvMeasure.setText(getString(R.string.nibp_btn_start));
            tvMeasure.setEnabled(false);
            if (value == 511) {
                isEcgConnect = false;
                isChecking = false;
                reinit();
                tvMeasure.setText(getString(R.string.nibp_btn_start));
                tvStatusView.setText(getString(R.string.ecg_pls_checkforline1));
            } else {
                String string = Integer.toBinaryString(value);
                if (string.length() < 9) {
                    String sr = "";
                    for (int i = 0; i < 9 - string.length(); i++) {
                        sr += "0";
                    }
                    string = sr + string;
                }
                String off = "";
                if ((leadoff & 0x1) == 0x1) {
                    if (downLL == 0) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    if (downLL == 2) {
                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        downLL = 0;
                                        break;
                                    }
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                    }
                    downLL = 1;
                    off += getString(R.string.avf) + ",";
                } else {
                    if (downLL == 1) {
                        downLL = 2;
                    }
                }
                if ((leadoff & 0x2) == 0x2) {
                    off += getString(R.string.avl) + ",";
                }
                if ((leadoff & 0x4) == 0x4) {
                    off += getString(R.string.avr) + ",";
                }
                if ((leadoff & 0x8) == 0x8) {
                    off += getString(R.string.V1) + ",";
                }
//                if ((leadoff & 0x10) == 0x10) {
//                    off += getString(R.string.V2) + ",";
//                }
//                if ((leadoff & 0x20) == 0x20) {
//                    off += getString(R.string.V3) + ",";
//                }
//                if ((leadoff & 0x40) == 0x40) {
//                    off += getString(R.string.V4) + ",";
//                }
//                if ((leadoff & 0x80) == 0x80) {
//                    off += getString(R.string.V5) + ",";
//                }
//                if ((leadoff & 0x100) == 0x100) {
//                    off += getString(R.string.V6) + ",";
//                }
                if (off.length() > 0) {
                    tvStatusView.setText(off.substring(0, off.length() - 1) +
                            getString(R.string.ecg_pls_checkforline));
                }
            }
        }
    }


    /**
     * 保存心电图像的方法
     *
     * @param param 缩引
     * @param bytes 字节
     */
    public void saveWave(int param, byte[] bytes) {
        measureBean.setEcgWave(param, UnitConvertUtil.bytesToHexString(bytes));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MeasureUtils.setAppDeviceListen(null);
    }

    /**
     * 心电测量完成。收到12导诊断结果代表心电测量完成。
     *
     * @param hrValue   心率值
     * @param diaResult 12导诊断结果
     */
    private void measureFinish(int hrValue, String diaResult) {
        Log.e("p", "我测量完成了");
        isChecking = false;
        reinit();
        tvMeasure.setText(getActivity().getString(R.string.nibp_btn_start));
        tvEcgSetting.setEnabled(true);
        tvStatusView.setText(getActivity().getString(R.string.ecg_check_ready));
        measureBean.setHr(hrValue);
        measureBean.setAnal(diaResult);
        // 心率报警
        executeHrAlarm(hrValue);
        // 设置显示值到UI
        tvHrView.setText(String.valueOf(hrValue));
        if (null != l) {
            l.onComplete(Measure.ECG, measureBean);
        }
    }
}
