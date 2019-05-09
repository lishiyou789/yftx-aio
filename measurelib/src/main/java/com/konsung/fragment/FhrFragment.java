package com.konsung.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.konsung.R;
import com.konsung.bean.MeasureBean;
import com.konsung.constant.KParamType;
import com.konsung.listen.AppDeviceListen;
import com.konsung.listen.Measure;
import com.konsung.listen.MeasureCompleteListen;
import com.konsung.net.EchoServerEncoder;
import com.konsung.util.MeasureUtils;
import com.konsung.util.UnitConvertUtil;
import com.konsung.view.FhrView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by YYX on 2017/8/23 0023
 * 台心监的界面
 */

public class FhrFragment extends Fragment {
    private MeasureBean measureBean; //当前测量的bean
    private MeasureCompleteListen l; //监听类
    private View view;
    FhrView fhrView;
    FhrView fhrView1;
    Button btnStart1;
    Button btn_start3;
    Button btn_start4;
    Button btnStart6;
    EditText etVolume;
    private Timer timer;
    private Timer timer1;
    private TimerTask timerTask;
    private TimerTask timerTask1;
    private boolean isChecking = false; //是否正在测量过程标志位
    private boolean isChecking1 = false; //是否正在测量过程标志位
    private List<Integer> fhrAllData;
    private List<Integer> fhrAllData1;
    TextView tvCountDown;
    TextView tvCountDown1;
    private int defaultTime = 1200; //默认倒计时时间
    private int defaultTime1 = 1200; //默认倒计时时间
    TextView tvFhrValueMean;
    TextView tvFhrValueMean1;

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
    public static FhrFragment getInstance(MeasureBean bean) {
//        this.measureBean = bean;
        FhrFragment fragment = new FhrFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fhr, null);
        initView();
        initData();
        return view;
    }

    /**
     * 初始化数据的方法
     */
    private void initData() {
        fhrView.setWaveSpeed();
        fhrView1.setWaveSpeed();
        fhrView1.setScale(new String[]{"0", "20", "40", "60"}, 0);
        MeasureUtils.setAppDeviceListen(new AppDeviceListen() {
            @Override
            public void sendParaStatus(String name, String version) {

            }

            @Override
            public void sendWave(int param, byte[] bytes) {
                switch (param) {
                    case KParamType.FHR2:
                        if (isChecking) {
                            measureBean.setEcgWave(13, UnitConvertUtil.bytesToHexString(bytes));
                            int value;
                            for (byte b : bytes) {
                                value = b & 0xFF;
                                fhrAllData.add(value);
                                fhrView.addEcgData0(value);
                                tvFhrValueMean.setText(String.valueOf(bytes[0] & 0xFF)
                                        + getActivity().getString(R.string.health_unit_bpm));
                            }
                        }
                        break;
                    case KParamType.FHR_TOCO:
                        if (isChecking1) {
                            measureBean.setEcgWave(15, UnitConvertUtil.bytesToHexString(bytes));
                            int value;
                            for (byte b : bytes) {
                                value = b & 0xFF;
                                fhrAllData1.add(value);
                                fhrView1.addEcgData0(value);
                                tvFhrValueMean1.setText(String.valueOf(bytes[0] & 0xFF)
                                        + getActivity().getString(R.string.health_unit_bpm));
                            }
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void sendTrend(int param, int value) {


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
        btnStart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChecking) {
                    fhrAllData = new ArrayList<Integer>();
                    isChecking = true;
                    timer = new Timer();
                    fhrView.setWaveSpeed();
                    btnStart1.setText(getActivity().getString(R.string.cancel_measure));
                    startCountDown();
                } else {
                    fhrAllData.clear();
                    isChecking = false;
                    stopCountDown();
                    btnStart1.setText(getActivity().getString(R.string.nibp_btn_start));
                }
            }
        });
        btnStart6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChecking) {
                    fhrAllData1 = new ArrayList<Integer>();
                    isChecking1 = true;
                    timer1 = new Timer();
                    fhrView1.setWaveSpeed();
                    btnStart6.setText(getActivity().getString(R.string.cancel_measure));
                    startCountDown1();
                } else {
                    fhrAllData1.clear();
                    isChecking1 = false;
                    stopCountDown1();
                    btnStart6.setText(getActivity().getString(R.string.nibp_btn_start));
                }
            }
        });
        btn_start3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EchoServerEncoder.setFrhConfig((short) 0x04, 0);
            }
        });
        btn_start4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etVolume.getText().toString();
                Integer integer = Integer.valueOf(s);
                EchoServerEncoder.setFrhConfig((short) 0x01, integer);
            }
        });
    }


    /**
     * 开始倒计时
     */
    private void startCountDown() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isChecking) {
                    if (defaultTime > 0) {
                        defaultTime--;
                        Message me = handler.obtainMessage();
                        me.arg1 = defaultTime;
                        handler.sendMessage(me);
                    } else {
                        stopCountDown();
                        isChecking = false;
                    }
                } else {
                    Message me = handler.obtainMessage();
                    me.arg1 = 1200;
                    handler.sendMessage(me);
                }
            }
        };
        if (null != timer) {
            timer.schedule(timerTask, 1000);
        }
    }

    /**
     * 开始倒计时
     */
    private void startCountDown1() {
        timerTask1 = new TimerTask() {
            @Override
            public void run() {
                if (isChecking1) {
                    if (defaultTime1 > 0) {
                        defaultTime1--;
                        Message me = handler1.obtainMessage();
                        me.arg1 = defaultTime1;
                        handler1.sendMessage(me);
                    } else {
                        stopCountDown1();
                        isChecking1 = false;
                    }
                } else {
                    Message me = handler1.obtainMessage();
                    me.arg1 = 1200;
                    handler1.sendMessage(me);
                }
            }
        };
        if (null != timer1) {
            timer1.schedule(timerTask1, 1000);
        }
    }

    /**
     * 停止倒计时
     */
    private void stopCountDown() {
        if (fhrAllData != null) {
            fhrAllData.clear();
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != timerTask) {
            timerTask.cancel();
            timerTask = null;
        }
        handler.removeCallbacksAndMessages(timerTask);
        defaultTime = 1200;
        tvCountDown.setText(getActivity().getString(R.string.min20));
    }

    /**
     * 停止倒计时
     */
    private void stopCountDown1() {
        if (fhrAllData1 != null) {
            fhrAllData1.clear();
        }
        if (null != timer1) {
            timer1.cancel();
            timer1 = null;
        }
        if (null != timerTask1) {
            timerTask1.cancel();
            timerTask1 = null;
        }
        handler1.removeCallbacksAndMessages(timerTask1);
        defaultTime1 = 1200;
        tvCountDown1.setText(getActivity().getString(R.string.min20));
    }

    /**
     * 倒计时实时改变界面
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int time = msg.arg1;
            if (time > 60) {
                int min = time / 60;
                int sec = time % 60;
                tvCountDown.setText(min + ":" + sec);
            } else {
                tvCountDown.setText("0:" + time);
            }
            if (isChecking && defaultTime <= 0) {
                int sum = 0;
                for (int value : fhrAllData) {
                    sum += value;
                }
                int mean = sum / fhrAllData.size();
                if (null != l) {
                    l.onComplete(Measure.FHR, measureBean);
                }
            }
            startCountDown();
        }
    };
    /**
     * 倒计时实时改变界面
     */
    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            int time = msg.arg1;
            if (time > 60) {
                int min = time / 60;
                int sec = time % 60;
                tvCountDown1.setText(min + ":" + sec);
            } else {
                tvCountDown1.setText("0:" + time);
            }
            if (isChecking1 && defaultTime1 <= 0) {
                int sum = 0;
                for (int value : fhrAllData1) {
                    sum += value;
                }
                int mean = sum / fhrAllData1.size();
                if (null != l) {
                    l.onComplete(Measure.FHR, measureBean);
                }
            }
            startCountDown();
        }
    };

    /**
     * 初始化界面的方法
     */
    private void initView() {
        fhrView = (FhrView) view.findViewById(R.id.fhr_view);
        fhrView1 = (FhrView) view.findViewById(R.id.fhr_view1);
        btnStart1 = (Button) view.findViewById(R.id.btn_start1);
        btn_start3 = (Button) view.findViewById(R.id.btn_start3);
        tvCountDown = (TextView) view.findViewById(R.id.tv_count_down);
        tvCountDown1 = (TextView) view.findViewById(R.id.tv_count_down1);
        tvFhrValueMean = (TextView) view.findViewById(R.id.tv_fhr_value_mean);
        tvFhrValueMean1 = (TextView) view.findViewById(R.id.tv_fhr_value_mean1);
        btn_start4 = (Button) view.findViewById(R.id.btn_start4);
        etVolume = (EditText) view.findViewById(R.id.et_volume);
        btnStart6 = (Button) view.findViewById(R.id.btn_start6);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MeasureUtils.setAppDeviceListen(null);
    }
}
