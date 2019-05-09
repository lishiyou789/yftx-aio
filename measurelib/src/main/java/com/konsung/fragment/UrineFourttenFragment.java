package com.konsung.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.konsung.R;
import com.konsung.bean.MeasureBean;
import com.konsung.constant.Configuration;
import com.konsung.constant.KParamType;
import com.konsung.constant.Urine;
import com.konsung.listen.AppDeviceListen;
import com.konsung.listen.Measure;
import com.konsung.listen.MeasureCompleteListen;
import com.konsung.service.AIDLServer;
import com.konsung.util.MeasureUtils;
import com.konsung.util.OverProofUtil;
import com.konsung.util.UrineType;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 尿常规页面
 */
public class UrineFourttenFragment extends Fragment {

    // 参数
    TextView tvUrineRtLeu;
    TextView tvUrineRtUbg;
    TextView tvUrineRtAlb;
    TextView tvUrineRtPro;
    TextView tvUrineRtBil;
    TextView tvUrineRtGlu;
    TextView tvUrineRtAsc;
    TextView tvUrineRtSg;
    TextView tvUrineRtKet;
    TextView tvUrineRtNit;
    TextView tvUrineRtCre;
    TextView tvUrineRtPh;
    TextView tvUrineRtBld;
    TextView tvUrineRtCa;
    ImageView ivLeuIcon;
    ImageView ivUbgIcon;
    ImageView ivAlbIcon;
    ImageView ivProIcon;
    ImageView ivBilIcon;
    ImageView ivGluIcon;
    ImageView ivAscIcon;
    ImageView ivSgIcon;
    ImageView ivKetIcon;
    ImageView ivNitIcon;
    ImageView ivCreIcon;
    ImageView ivPhIcon;
    ImageView ivBldIcon;
    ImageView ivCaIcon;
    LinearLayout caLayout;
    LinearLayout maLayout;
    LinearLayout crLayout;

    private HashMap<Integer, TextView> views;

    public AIDLServer aidlServer;
    private Intent intent;
    // 是否已经绑定服务
    private boolean isBind;
    private Handler handler = new Handler();
    private View view;
    private MeasureBean measureBean; //当前测量项
    private MeasureCompleteListen l; //监听类

    /**
     * 设置测量类的方法
     *
     * @param measureBean 测量类
     */
    public static UrineFourttenFragment getInstance(MeasureBean measureBean) {
//        this.measureBean = measureBean;
        UrineFourttenFragment fragment = new UrineFourttenFragment();
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
        view = inflater.inflate(R.layout.fragment_urine_fourteen, null);
        views = new HashMap<>();
        initView();
        views.put(R.id.urinert_leu_tv, tvUrineRtLeu);
        views.put(R.id.urinert_ubg_tv, tvUrineRtUbg);
        views.put(R.id.urinert_alb_tv, tvUrineRtAlb);
        views.put(R.id.urinert_pro_tv, tvUrineRtPro);
        views.put(R.id.urinert_bil_tv, tvUrineRtBil);
        views.put(R.id.urinert_glu_tv, tvUrineRtGlu);
        views.put(R.id.urinert_asc_tv, tvUrineRtAsc);
        views.put(R.id.urinert_sg_tv, tvUrineRtSg);
        views.put(R.id.urinert_ket_tv, tvUrineRtKet);
        views.put(R.id.urinert_nit_tv, tvUrineRtNit);
        views.put(R.id.urinert_cre_tv, tvUrineRtCre);
        views.put(R.id.urinert_ph_tv, tvUrineRtPh);
        views.put(R.id.urinert_bld_tv, tvUrineRtBld);
        views.put(R.id.urinert_ca_tv, tvUrineRtCa);
        MeasureUtils.setAppDeviceListen(new AppDeviceListen() {
            @Override
            public void sendParaStatus(String name, String version) {

            }

            @Override
            public void sendWave(int param, byte[] bytes) {

            }

            @Override
            public void sendTrend(int param, int value) {
                int i = value / Configuration.TREND_FACTOR;
                if (value == Configuration.INVALID_DATA) {
                    return;
                }
                String str = valueToString(i);
                switch (param) {
                    case KParamType.URINERT_LEU:
                        tvUrineRtLeu.setText(str);
                        measureBean.setUrineLeu(str);
                        if (!"-".equals(str)) {
                            ivLeuIcon.setVisibility(View.VISIBLE);
                            tvUrineRtLeu.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivLeuIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtLeu.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
                        sendData();
                        break;
                    case KParamType.URINERT_UBG:
                        tvUrineRtUbg.setText(str);
                        measureBean.setUrineUbg(str);
                        if (!"-".equals(str)) {
                            ivUbgIcon.setVisibility(View.VISIBLE);
                            tvUrineRtUbg.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivUbgIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtUbg.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
                        break;
                    case KParamType.URINERT_ALB:
                        tvUrineRtAlb.setText(str);
                        measureBean.setUrineMa(str);
                        if (!"-".equals(str)) {
                            ivAlbIcon.setVisibility(View.VISIBLE);
                            tvUrineRtAlb.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivAlbIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtAlb.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
                        break;
                    case KParamType.URINERT_PRO:
                        tvUrineRtPro.setText(str);
                        measureBean.setUrinePro(str);
                        if (!"-".equals(str)) {
                            ivProIcon.setVisibility(View.VISIBLE);
                            tvUrineRtPro.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivProIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtPro.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
                        break;
                    case KParamType.URINERT_BIL:
                        tvUrineRtBil.setText(str);
                        measureBean.setUrineBil(str);
                        if (!"-".equals(str)) {
                            ivBilIcon.setVisibility(View.VISIBLE);
                            tvUrineRtBil.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivBilIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtBil.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
                        break;
                    case KParamType.URINERT_GLU:
                        tvUrineRtGlu.setText(str);
                        measureBean.setUrineGlu(str);
                        if (!"-".equals(str)) {
                            ivGluIcon.setVisibility(View.VISIBLE);
                            tvUrineRtGlu.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivGluIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtGlu.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
                        break;
                    //兼容恩普尿机的VC
                    case KParamType.URINERT_VC:
                        tvUrineRtAsc.setText(str);
                        measureBean.setUrineAsc(str);
                        if (!"-".equals(str)) {
                            ivAscIcon.setVisibility(View.VISIBLE);
                            tvUrineRtAsc.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivAscIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtAsc.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
                        break;
                    case KParamType.URINERT_ASC:
                        tvUrineRtAsc.setText(str);
                        measureBean.setUrineAsc(str);
                        if (!"-".equals(str)) {
                            ivAscIcon.setVisibility(View.VISIBLE);
                            tvUrineRtAsc.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivAscIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtAsc.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
                        break;
                    case KParamType.URINERT_SG:
                        double sg = (double) value / 1000.0f;
                        tvUrineRtSg.setText(String.format("%.3f", sg));
                        tvUrineRtSg.setTextColor(getActivity()
                                .getResources().getColor(R.color.black));
                        measureBean.setUrineSg(sg);
                        if ((float) sg > Urine.SG_HIGH) {
                            ivSgIcon.setImageResource(R.drawable
                                    .alarm_high);
                            ivSgIcon.setVisibility(View.VISIBLE);
                        } else if ((float) sg < Urine.SG_LOW) {
                            ivSgIcon.setImageResource(R.drawable
                                    .alarm_low);
                            ivSgIcon.setVisibility(View.VISIBLE);
                        } else {
                            ivSgIcon.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case KParamType.URINERT_KET:
                        tvUrineRtKet.setText(str);
                        measureBean.setUrineKet(str);
                        if (!"-".equals(str)) {
                            ivKetIcon.setVisibility(View.VISIBLE);
                            tvUrineRtKet.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivKetIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtKet.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
                        break;
                    case KParamType.URINERT_NIT:
                        tvUrineRtNit.setText(str);
                        measureBean.setUrineNit(str);
                        if (!"-".equals(str)) {
                            ivNitIcon.setVisibility(View.VISIBLE);
                            tvUrineRtNit.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivNitIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtNit.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
                        break;
                    case KParamType.URINERT_CRE:
                        tvUrineRtCre.setText(str);
                        measureBean.setUrineCre(str);
                        if (!"-".equals(str)) {
                            ivCreIcon.setVisibility(View.VISIBLE);
                            tvUrineRtCre.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivCreIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtCre.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
                        break;
                    case KParamType.URINERT_PH:
                        float ph = value / 100.0f;
                        tvUrineRtPh.setText(String.valueOf(value / 100.0f));
                        measureBean.setUrinePh(ph);
                        if (ph > Urine.PH_HIGH) {
                            ivPhIcon.setImageResource(R.drawable
                                    .alarm_high);
                            ivPhIcon.setVisibility(View.VISIBLE);
                        } else if (ph < Urine.PH_LOW) {
                            ivPhIcon.setImageResource(R.drawable
                                    .alarm_low);
                            ivPhIcon.setVisibility(View.VISIBLE);
                        } else {
                            ivPhIcon.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case KParamType.URINERT_BLD:
                        tvUrineRtBld.setText(str);
                        measureBean.setUrineBld(str);
                        if (!"-".equals(str)) {
                            ivBldIcon.setVisibility(View.VISIBLE);
                            tvUrineRtBld.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivBldIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtBld.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
                        break;
                    case KParamType.URINERT_CA:
                        tvUrineRtCa.setText(str);
                        measureBean.setUrineCa(str);
                        if (!"-".equals(str)) {
                            ivCaIcon.setVisibility(View.VISIBLE);
                            tvUrineRtCa.setTextColor(getActivity()
                                    .getResources().getColor(R.color.red));
                        } else {
                            ivCaIcon.setVisibility(View.INVISIBLE);
                            tvUrineRtCa.setTextColor(getActivity()
                                    .getResources().getColor(R.color.black));
                        }
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
     * 初始化界面的方法
     */
    private void initView() {
        tvUrineRtLeu = (TextView) view.findViewById(R.id.urinert_leu_tv);
        tvUrineRtUbg = (TextView) view.findViewById(R.id.urinert_ubg_tv);
        tvUrineRtAlb = (TextView) view.findViewById(R.id.urinert_alb_tv);
        tvUrineRtPro = (TextView) view.findViewById(R.id.urinert_pro_tv);
        tvUrineRtBil = (TextView) view.findViewById(R.id.urinert_bil_tv);
        tvUrineRtGlu = (TextView) view.findViewById(R.id.urinert_glu_tv);
        tvUrineRtAsc = (TextView) view.findViewById(R.id.urinert_asc_tv);
        tvUrineRtSg = (TextView) view.findViewById(R.id.urinert_sg_tv);
        tvUrineRtKet = (TextView) view.findViewById(R.id.urinert_ket_tv);
        tvUrineRtNit = (TextView) view.findViewById(R.id.urinert_nit_tv);
        tvUrineRtCre = (TextView) view.findViewById(R.id.urinert_cre_tv);
        tvUrineRtPh = (TextView) view.findViewById(R.id.urinert_ph_tv);
        tvUrineRtBld = (TextView) view.findViewById(R.id.urinert_bld_tv);
        tvUrineRtCa = (TextView) view.findViewById(R.id.urinert_ca_tv);
        ivLeuIcon = (ImageView) view.findViewById(R.id.leu_icon);
        ivUbgIcon = (ImageView) view.findViewById(R.id.ubg_icon);
        ivAlbIcon = (ImageView) view.findViewById(R.id.alb_icon);
        ivProIcon = (ImageView) view.findViewById(R.id.pro_icon);
        ivBilIcon = (ImageView) view.findViewById(R.id.bil_icon);
        ivGluIcon = (ImageView) view.findViewById(R.id.glu_icon);
        ivAscIcon = (ImageView) view.findViewById(R.id.asc_icon);
        ivSgIcon = (ImageView) view.findViewById(R.id.sg_icon);
        caLayout = (LinearLayout) view.findViewById(R.id.urine_ca_layout);
        maLayout = (LinearLayout) view.findViewById(R.id.ma_layout);
        crLayout = (LinearLayout) view.findViewById(R.id.cr_layout);
        int urineType = MeasureUtils.getSpInt(getActivity(), Configuration.APP_CONFIG
                , Configuration.URINETYPE, UrineType.ELEVEN);
        //11项
        if (urineType == UrineType.ELEVEN) {
            caLayout.setVisibility(View.GONE);
            maLayout.setVisibility(View.GONE);
            crLayout.setVisibility(View.GONE);
        } else {
            //14项
            caLayout.setVisibility(View.VISIBLE);
            maLayout.setVisibility(View.VISIBLE);
            crLayout.setVisibility(View.VISIBLE);
        }
        //如果超出范围 就设置字体颜色为红色
        tvUrineRtPh.addTextChangedListener(new OverProofUtil(Urine.PH_LOW, Urine.PH_HIGH
                , tvUrineRtPh, getActivity()));
        tvUrineRtSg.addTextChangedListener(new OverProofUtil(Urine.SG_LOW, Urine.SG_HIGH
                , tvUrineRtSg, getActivity()));

        Iterator iterator = views.keySet().iterator();
        while (iterator.hasNext()) {
            views.get(iterator.next()).setText(getString(R.string.default_value));
        }
        String str;
        if (null != measureBean && !TextUtils.isEmpty(measureBean.getUrineLeu())) {
            str = measureBean.getUrineLeu();
            tvUrineRtLeu.setText(str);
            if (!"-".equals(str)) {
                ivLeuIcon.setVisibility(View.VISIBLE);
                tvUrineRtLeu.setTextColor(getActivity().getResources().getColor(R.color.red));
            }
        }
        if (null != measureBean && !TextUtils.isEmpty(measureBean.getUrineUbg())) {
            str = measureBean.getUrineUbg();
            tvUrineRtUbg.setText(str);
            if (!"-".equals(str)) {
                ivUbgIcon.setVisibility(View.VISIBLE);
                tvUrineRtUbg.setTextColor(getActivity().getResources().getColor(R.color.red));
            }
        }
        if (null != measureBean && !TextUtils.isEmpty(measureBean.getUrineMa())) {
            str = measureBean.getUrineMa();
            tvUrineRtAlb.setText(str);
            if (!"-".equals(str)) {
                ivAlbIcon.setVisibility(View.VISIBLE);
                tvUrineRtAlb.setTextColor(getActivity().getResources().getColor(R.color.red));
            }
        }
        if (null != measureBean && !TextUtils.isEmpty(measureBean.getUrinePro())) {
            str = measureBean.getUrinePro();
            tvUrineRtPro.setText(str);
            if (!"-".equals(str)) {
                ivProIcon.setVisibility(View.VISIBLE);
                tvUrineRtPro.setTextColor(getActivity().getResources().getColor(R.color.red));
            } else {
                ivProIcon.setVisibility(View.INVISIBLE);
            }
        }
        if (null != measureBean && !TextUtils.isEmpty(measureBean.getUrineBil())) {
            str = measureBean.getUrineBil();
            tvUrineRtBil.setText(str);
            if (!"-".equals(str)) {
                ivBilIcon.setVisibility(View.VISIBLE);
                tvUrineRtBil.setTextColor(getActivity().getResources().getColor(R.color.red));
            } else {
                ivBilIcon.setVisibility(View.INVISIBLE);
            }
        }
        if (null != measureBean && !TextUtils.isEmpty(measureBean.getUrineGlu())) {
            str = measureBean.getUrineGlu();
            tvUrineRtGlu.setText(str);
            if (!"-".equals(str)) {
                ivGluIcon.setVisibility(View.VISIBLE);
                tvUrineRtGlu.setTextColor(getActivity().getResources().getColor(R.color.red));
            } else {
                ivGluIcon.setVisibility(View.INVISIBLE);
            }
        }
        if (null != measureBean && !TextUtils.isEmpty(measureBean.getUrineAsc())) {
            str = measureBean.getUrineAsc();
            tvUrineRtAsc.setText(str);
            if (!"-".equals(str)) {
                ivAscIcon.setVisibility(View.VISIBLE);
                tvUrineRtAsc.setTextColor(getActivity().getResources().getColor(R.color.red));
            } else {
                ivAscIcon.setVisibility(View.INVISIBLE);
            }
        }
        if (null != measureBean && measureBean.getUrineSg() != 0.0f) {
            tvUrineRtSg.setText(String.format("%.3f", measureBean.getUrineSg()));
            if (measureBean.getUrineSg() > Urine.SG_HIGH) {
                ivSgIcon.setImageResource(R.drawable.alarm_high);
                ivSgIcon.setVisibility(View.VISIBLE);
            } else if (measureBean.getUrineSg() < Urine.SG_LOW) {
                ivSgIcon.setImageResource(R.drawable.alarm_low);
                ivSgIcon.setVisibility(View.VISIBLE);
            } else {
                ivSgIcon.setVisibility(View.INVISIBLE);
            }
        }
        if (null != measureBean && !TextUtils.isEmpty(measureBean.getUrineKet())) {
            str = measureBean.getUrineKet();
            tvUrineRtKet.setText(str);
            if (!"-".equals(str)) {
                ivKetIcon.setVisibility(View.VISIBLE);
                tvUrineRtKet.setTextColor(getActivity().getResources().getColor(R.color.red));
            } else {
                ivKetIcon.setVisibility(View.INVISIBLE);
            }
        }
        if (null != measureBean && !TextUtils.isEmpty(measureBean.getUrineNit())) {
            str = measureBean.getUrineNit();
            tvUrineRtNit.setText(str);
            if (!"-".equals(str)) {
                ivNitIcon.setVisibility(View.VISIBLE);
                tvUrineRtNit.setTextColor(getActivity().getResources().getColor(R.color.red));
            } else {
                ivNitIcon.setVisibility(View.INVISIBLE);
            }
        }
        if (null != measureBean && !TextUtils.isEmpty(measureBean.getUrineCre())) {
            str = measureBean.getUrineCre();
            tvUrineRtCre.setText(str);
            if (!"-".equals(str)) {
                ivCreIcon.setVisibility(View.VISIBLE);
                tvUrineRtCre.setTextColor(getActivity().getResources().getColor(R.color.red));
            }
        }
        if (null != measureBean && measureBean.getUrinePh() != 0.0f) {
            tvUrineRtPh.setText(String.valueOf(measureBean.getUrinePh()));
            if (measureBean.getUrinePh() > Urine.PH_HIGH) {
                ivPhIcon.setImageResource(R.drawable.alarm_high);
                ivPhIcon.setVisibility(View.VISIBLE);
            } else if (measureBean.getUrinePh() < Urine.PH_LOW) {
                ivPhIcon.setImageResource(R.drawable.alarm_low);
                ivPhIcon.setVisibility(View.VISIBLE);
            } else {
                ivPhIcon.setVisibility(View.INVISIBLE);
            }
        }
        if (null != measureBean && !TextUtils.isEmpty(measureBean.getUrineBld())) {
            str = measureBean.getUrineBld();
            tvUrineRtBld.setText(str);
            if (!"-".equals(str)) {
                ivBldIcon.setVisibility(View.VISIBLE);
                tvUrineRtBld.setTextColor(getActivity().getResources().getColor(R.color.red));
            } else {
                ivBldIcon.setVisibility(View.INVISIBLE);
            }
        }
        if (null != measureBean && !TextUtils.isEmpty(measureBean.getUrineBld())) {
            str = measureBean.getUrineBld();
            tvUrineRtCa.setText(str);
            if (!"-".equals(str)) {
                ivCaIcon.setVisibility(View.VISIBLE);
                tvUrineRtCa.setTextColor(getActivity().getResources().getColor(R.color.red));
            } else {
                ivCaIcon.setVisibility(View.INVISIBLE);
            }
        }
    }


    /**
     * 发生测量成功的监听数据
     */
    private void sendData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MeasureUtils.post(new Runnable() {
                    @Override
                    public void run() {
                        if (null != l) {
                            l.onComplete(Measure.URINE, measureBean);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 尿常规值转换
     *
     * @param value 模块传递过来的值
     * @return 显示测量值
     */
    private String valueToString(int value) {

        switch (value) {
            case -1:
                return "-";
            case 0:
                return "+-";
            case 1:
                return "+1";
            case 2:
                return "+2";
            case 3:
                return "+3";
            case 4:
                return "+4";
            case 5:
                return "+";
            case 6:
                return "Normal";
            default:
                return String.valueOf(value);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MeasureUtils.setAppDeviceListen(null);
    }
}
