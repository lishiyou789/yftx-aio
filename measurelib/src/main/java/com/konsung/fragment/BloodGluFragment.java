package com.konsung.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.konsung.R;
import com.konsung.bean.MeasureBean;
import com.konsung.constant.BeneParamValue;
import com.konsung.constant.Configuration;
import com.konsung.constant.KParamType;
import com.konsung.listen.AppDeviceListen;
import com.konsung.listen.Measure;
import com.konsung.listen.MeasureCompleteListen;
import com.konsung.service.AIDLServer;
import com.konsung.util.AlarmUtil;
import com.konsung.util.MeasureUtils;
import com.konsung.util.OverProofUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JustRush on 2015/7/23.
 */
public class BloodGluFragment extends Fragment implements View
        .OnClickListener {
    // 参数
    TextView tvBloodGluTrend;
    TextView tvNsTrend;
    TextView tvTdTrend;
    RelativeLayout rlContent;
    ScrollView sclViScrollView;
    RelativeLayout rlContentTable;
    Button btnBeforeEat;
    Button btnRandom;
    Button btnLateEat;
    TextView tvBloodGluHigh;
    //血糖正常最低值
    TextView tvBloodGluLow;
    TextView tvUrineMin;
    TextView tvUrineMax;
    ImageView ivBloodGlu;
    ImageView ivBloodNs;
    ImageView ivBloodTd;
    private Intent mIntent;
    // 是否已经绑定服务
    private boolean mIsBind;
    // 开始测量按钮按下状态
    private boolean isStartMeasure;
    //已经得到数据
    private boolean isGetValue;

    // 以下变量使用于模拟测量,不需要时可删除
    private List<Integer> irtempTrendList;
    private View view;

    public AIDLServer aidlServer;

    //数据接收完后的一次刷新
    public int updateFlag = 0;
    //身份证号
    private String idCard;
    //性别
    private String sexType;
    private float xtMax;
    private float xtMin;
    private float nsMax;
    private float nsMin;
    private float cholMax;
    private float cholMin;
    //餐前，餐后值
    private String beforeMeal = "-?-";
    private String afterMeal = "-?-";
    //判断餐前餐后标识
    private boolean isBeforeMeal = true;
    private MeasureBean measureBean; //当前测量项
    private MeasureCompleteListen l; //监听类

    /**
     * 设置测量类的方法
     *
     * @param measureBean 测量类
     */

    public static BloodGluFragment getInstance(MeasureBean measureBean) {
        BloodGluFragment fragment = new BloodGluFragment();
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
        view = inflater.inflate(R.layout.fragment_bloodglu, null);
        initView();
        MeasureUtils.setAppDeviceListen(new AppDeviceListen() {
            @Override
            public void sendParaStatus(String name, String version) {

            }

            @Override
            public void sendWave(int param, byte[] bytes) {

            }


            @Override
            public void sendTrend(int param, int value) {
                float v1 = (float) value / Configuration.TREND_FACTOR;
                switch (param) {
                    case KParamType.BLOODGLU_AFTER_MEAL:
                        float vGlu = (float) value / Configuration.TREND_FACTOR;
                        if (value != Configuration.INVALID_DATA) {
                            isGetValue = true;
                            tvBloodGluTrend.setText(String.valueOf(vGlu));
                            measureBean.setGlu(vGlu);
                            if (null != l) {
                                l.onComplete(Measure.GLU, measureBean);
                            }
                            AlarmUtil.executeOverrunAlarm(vGlu, BeneParamValue
                                            .XT_AFTER_VALUE_MAX, BeneParamValue
                                            .XT_AFTER_VALUE_MIN
                                    , ivBloodGlu);
                        }
                        break;
                    case KParamType.BLOODGLU_BEFORE_MEAL:
                        if (value != Configuration.INVALID_DATA) {
                            isGetValue = true;
                            tvBloodGluTrend.setText(String.valueOf(v1));
                            //保存测量血糖时候病人的测量状态
                            if (isBeforeMeal) {
                                //餐前测量数据
                                afterMeal = "-?-";
                            } else {
                                //餐后测量数据
                                beforeMeal = "-?-";
                            }
                            measureBean.setGlu(v1);
                            if (null != l) {
                                l.onComplete(Measure.GLU, measureBean);
                            }
                            AlarmUtil.executeOverrunAlarm(v1, xtMax, xtMin, ivBloodGlu);
                        }
                        break;
                    //尿酸
                    case KParamType.URICACID_TREND:
                        if (value != Configuration.INVALID_DATA) {
//                            float tempNs = (float) value / Configuration.TREND_FACTOR;
                            tvNsTrend.setText(String.valueOf(value));
                            measureBean.setUricacid(value * 10);
                            if (null != l) {
                                l.onComplete(Measure.UA, measureBean);
                            }
                            AlarmUtil.executeOverrunAlarm(value, nsMax, nsMin
                                    , ivBloodNs);
                        }
                        break;
                    //总胆固醇
                    case KParamType.CHOLESTEROL_TREND:
                        if (value != Configuration.INVALID_DATA) {
                            float temp = (float) value / Configuration.TREND_FACTOR;
                            tvTdTrend.setText(String.valueOf(temp));
                            measureBean.setXzzdgc(temp);
                            if (null != l) {
                                l.onComplete(Measure.CHOL, measureBean);
                            }
                            AlarmUtil.executeOverrunAlarm(temp, cholMax, cholMin
                                    , ivBloodTd);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void sendConfig(int param, int value) {

            }

//            @Override
//            public void sendPersonalDetail(String name, String idcard, int sex, int type,
//                                           String birthday, String picture) {
//
//            }

            @Override
            public void send12LeadDiaResult(byte[] bytes) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isStartMeasure = false;
        MeasureUtils.setAppDeviceListen(null);
    }

    /**
     * 初始化
     */
    private void initView() {
        tvBloodGluTrend = (TextView) view.findViewById(R.id.blood_glu_trend_tv);
        tvNsTrend = (TextView) view.findViewById(R.id.blood_ns_trend_tv);
        tvTdTrend = (TextView) view.findViewById(R.id.blood_td_trend_tv);
        rlContent = (RelativeLayout) view.findViewById(R.id.content);
        rlContentTable = (RelativeLayout) view.findViewById(R.id.content_table);
        btnBeforeEat = (Button) view.findViewById(R.id.btn_before_eat);
        btnRandom = (Button) view.findViewById(R.id.btn_random);
        btnLateEat = (Button) view.findViewById(R.id.btn_late_eat);
        tvBloodGluHigh = (TextView) view.findViewById(R.id.blood_glu_high);
        tvBloodGluLow = (TextView) view.findViewById(R.id.blood_glu_low);
        tvUrineMin = (TextView) view.findViewById(R.id.blood_ns_low);
        tvUrineMax = (TextView) view.findViewById(R.id.blood_ns_high);
        ivBloodGlu = (ImageView) view.findViewById(R.id.iv_blood_glu_trend);
        ivBloodNs = (ImageView) view.findViewById(R.id.iv_blood_ns_trend);
        ivBloodTd = (ImageView) view.findViewById(R.id.iv_blood_td_trend);
        if (null != measureBean && measureBean.getGluStyle() == Configuration.BtnFlag.lift) {
            //餐前
            isBeforeMeal = true;
        } else {
            //餐后
            isBeforeMeal = null != measureBean && measureBean.getGlu() != -1.0f;
        }
        if (null != measureBean && measureBean.getGluStyle() == Configuration.BtnFlag.middle) {
            tvBloodGluTrend.addTextChangedListener(new OverProofUtil(BeneParamValue
                    .XT_AFTER_VALUE_MIN, BeneParamValue.XT_AFTER_VALUE_MAX, tvBloodGluTrend,
                    getActivity()));
            xtMax = BeneParamValue.XT_AFTER_VALUE_MAX;
            xtMin = BeneParamValue.XT_AFTER_VALUE_MIN;
        } else if (null != measureBean && measureBean.getGluStyle() == Configuration.BtnFlag.middle) {
            tvBloodGluTrend.addTextChangedListener(new OverProofUtil(BeneParamValue
                    .XT_VALUE_MIN, BeneParamValue.XT_VALUE_MAX, tvBloodGluTrend, getActivity()));
            xtMax = BeneParamValue.XT_VALUE_MAX;
            xtMin = BeneParamValue.XT_VALUE_MIN;
        } else {
            tvBloodGluTrend.addTextChangedListener(new OverProofUtil(BeneParamValue
                    .XT_VALUE_MIN, BeneParamValue.XT_VALUE_MAX, tvBloodGluTrend, getActivity()));
            xtMax = BeneParamValue.XT_VALUE_MAX;
            xtMin = BeneParamValue.XT_VALUE_MIN;
        }
        idCard = measureBean.getIdCard();
        sexType = MeasureUtils.judgeSexByIdCard(idCard);
        //尿酸
        if (sexType.equals(getString(R.string.boy))) {
            tvUrineMax.setText(BeneParamValue.NS_VALUE_MAX + "");
            tvUrineMin.setText(BeneParamValue.NS_VALUE_MIN + "");
            tvNsTrend.addTextChangedListener(new OverProofUtil(BeneParamValue.NS_VALUE_MIN
                    , BeneParamValue.NS_VALUE_MAX, tvNsTrend, getActivity()));
            nsMax = BeneParamValue.NS_VALUE_MAX;
            nsMin = BeneParamValue.NS_VALUE_MIN;
        } else if (sexType.equals(getString(R.string.girl))) {
            tvUrineMax.setText(BeneParamValue.NS_VALUE_MAXG + "");
            tvUrineMin.setText(BeneParamValue.NS_VALUE_MING + "");
            tvNsTrend.addTextChangedListener(new OverProofUtil(BeneParamValue.NS_VALUE_MING
                    , BeneParamValue.NS_VALUE_MAXG, tvNsTrend, getActivity()));
            nsMax = BeneParamValue.NS_VALUE_MAXG;
            nsMin = BeneParamValue.NS_VALUE_MING;
        }
        tvTdTrend.addTextChangedListener(new OverProofUtil(BeneParamValue.CHOL_VALUE_MIN
                , BeneParamValue.CHOL_VALUE_MAX, tvTdTrend, getActivity()));
        cholMax = BeneParamValue.CHOL_VALUE_MAX;
        cholMin = BeneParamValue.CHOL_VALUE_MIN;
        tvBloodGluHigh.setText(BeneParamValue.XT_VALUE_MAX + "");
        tvBloodGluLow.setText(BeneParamValue.XT_VALUE_MIN + "");
        tvBloodGluTrend.setText(getString(R.string.default_value));
        tvNsTrend.setText(getString(R.string.default_value));
        tvTdTrend.setText(getString(R.string.default_value));
        //有值的情况下进行餐前餐后判断
        if (null != measureBean && measureBean.getGlu() != -1.0f) {
            selectBtn(measureBean.getGluStyle());
            tvBloodGluTrend.setText(String.valueOf(measureBean.getGlu()));
            AlarmUtil.executeOverrunAlarm(measureBean.getGlu(), xtMax, xtMin, ivBloodGlu);
        } else {
            selectBtn(Configuration.BtnFlag.lift);
        }
        if (null != measureBean && measureBean.getXzzdgc() != -1.0f) {
            tvTdTrend.setText(String.valueOf(measureBean.getXzzdgc()));
            AlarmUtil.executeOverrunAlarm(measureBean.getXzzdgc(), cholMax, cholMin, ivBloodTd);
        }

        if (null != measureBean && measureBean.getUricacid() != -1) {
            tvNsTrend.setText(String.valueOf(measureBean.getUricacid()));
            AlarmUtil.executeOverrunAlarm(measureBean.getUricacid(), nsMax, nsMin, ivBloodNs);
        }
        // 模拟测量的数据
        irtempTrendList = new ArrayList<>();
        btnBeforeEat.setOnClickListener(this);
        btnRandom.setOnClickListener(this);
        btnLateEat.setOnClickListener(this);
    }

    /**
     * 测量结束
     *
     * @param server 服务对象
     */
    private void measureOver(AIDLServer server) {
        int irtempTrends = 0;
        for (int i = 0; i < irtempTrendList.size(); i++) {
            irtempTrends += irtempTrendList.get(i);
        }
        //计算平均值
        if (0 != irtempTrendList.size()) {
            if (Configuration.INVALID_DATA == irtempTrends / irtempTrendList
                    .size()) {
                tvBloodGluTrend.setText(getString(R.string
                        .default_value));
            } else {
                tvBloodGluTrend.setText(String.valueOf((float) (irtempTrends /
                        irtempTrendList.size()) / Configuration.TREND_FACTOR));
            }
        }
        irtempTrendList.clear();
        /*  mProgress = 0;*/
    }

    @Override
    public void onClick(View view) {
        //用户餐前按钮
        if (view == btnBeforeEat) {
            if (!isBeforeMeal) {
                isBeforeMeal = true;
                selectBtn(Configuration.BtnFlag.lift);
                if (!tvBloodGluTrend.getText().toString().equals("-?-") &&
                        !TextUtils.isEmpty(tvBloodGluTrend.getText().toString())) {
                    //表示测量了餐后数据
                    //保存餐后数据
                    afterMeal = tvBloodGluTrend.getText().toString();
                    tvBloodGluTrend.setText("-?-");
                    ivBloodGlu.setVisibility(View.INVISIBLE);
                } else {
                    tvBloodGluTrend.setText(beforeMeal);
                    if (!beforeMeal.equals("-?-")) {
                        AlarmUtil.executeOverrunAlarm(Float.valueOf(beforeMeal)
                                , xtMax, xtMin, ivBloodGlu);
                    }
                }
            }
        } else if (view == btnRandom) {
            if (isBeforeMeal) {
                isBeforeMeal = false;
                //用户点击餐后按钮
                selectBtn(Configuration.BtnFlag.middle);
                if (!tvBloodGluTrend.getText().toString().equals("-?-") &&
                        !TextUtils.isEmpty(tvBloodGluTrend.getText().toString())) {
                    //表示测量了餐前数据
                    //保存餐前数据
                    beforeMeal = tvBloodGluTrend.getText().toString();
                    tvBloodGluTrend.setText("-?-");
                    ivBloodGlu.setVisibility(View.INVISIBLE);
                } else {
                    tvBloodGluTrend.setText(afterMeal);
                    if (!afterMeal.equals("-?-")) {
                        AlarmUtil.executeOverrunAlarm(Float.valueOf(afterMeal), xtMax, xtMin
                                , ivBloodGlu);
                    }
                }
            }
        } else if (view == btnLateEat) {
            //废弃按钮
            selectBtn(Configuration.BtnFlag.right);
        }
    }

    /**
     * 选中按钮的方法
     *
     * @param flag 按钮标识
     */
    public void selectBtn(Configuration.BtnFlag flag) {
        switch (flag) {
            case lift://餐前 0
                isBeforeMeal = true;
                clickBtn(btnBeforeEat, R.drawable.select_before_dinner);
                break;
            case middle://餐后 1
                isBeforeMeal = false;
                clickBtn(btnRandom, R.drawable.select_after_dinner);
                break;
//            case right://餐后 2
//                clickBtn(btnLateEat,R.drawable.right_sel);
//                break;
            default:
                break;
        }
    }

    /**
     * 点击按钮切换背景
     *
     * @param btn 点击的按钮
     * @param ba  点击的背景图片
     */
    @SuppressLint("NewApi")
    public void clickBtn(Button btn, int ba) {
        btnBeforeEat.setTextColor(getActivity().getResources()
                .getColor(R.color.grass_konsung_2));
        btnRandom.setTextColor(getActivity().getResources()
                .getColor(R.color.grass_konsung_2));
        btnBeforeEat.setBackground(getActivity().getResources()
                .getDrawable(R.drawable.unselect_before_dinner));
        btnRandom.setBackground(getActivity().getResources()
                .getDrawable(R.drawable.unselect_after_dinner));
        btn.setTextColor(Color.WHITE);
        btn.setBackground(getActivity().getResources().getDrawable(ba));
        int i = btn.getId();
        if (i == R.id.btn_before_eat) {
            tvBloodGluHigh.setText(BeneParamValue.XT_VALUE_MAX + "");
            tvBloodGluLow.setText(BeneParamValue.XT_VALUE_MIN + "");
            xtMin = BeneParamValue.XT_VALUE_MIN;
            xtMax = BeneParamValue.XT_VALUE_MAX;
            measureBean.setGluStyle(Configuration.BtnFlag.lift);
        } else if (i == R.id.btn_random) {
            tvBloodGluHigh.setText(BeneParamValue.XT_AFTER_VALUE_MAX + "");
            tvBloodGluLow.setText(BeneParamValue.XT_AFTER_VALUE_MIN + "");
            xtMin = BeneParamValue.XT_AFTER_VALUE_MIN;
            xtMax = BeneParamValue.XT_AFTER_VALUE_MAX;
            measureBean.setGluStyle(Configuration.BtnFlag.right);
        }
    }
}
