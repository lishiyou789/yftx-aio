package com.konsung.fragment;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.konsung.R;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;
import com.konsung.bean.TaioHeacheckData;
import com.konsung.util.EcgImage;
import com.konsung.util.UnitConvertUtil;


public class GetEcgWaveRecordFor12 extends Fragment {
    TextView tvName;
    TextView tvSex;
    TextView tvAge;
    TextView tvQrs;
    TextView tvPr;
    TextView tvQtQtc;
    TextView tvPQrsT;
    TextView tvRv5Sv1;
    TextView tvRv5PlusSv1;
    TextView tvDistrict;
    TextView tvHr;
    ImageView ivHeartImage;
    TextView tvEcgDiagnoseResult;
    private ResidentBean bean;
    private MeasureBean measureBean;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bean = (ResidentBean) getArguments().getSerializable("bean");
        measureBean = (MeasureBean) getArguments().getSerializable("measureBean");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_get_ecg_wave_recordfor12, container, false);
        initView();
        return view;
    }

    public static GetEcgWaveRecordFor12 getInstance(ResidentBean bean, MeasureBean measureBean) {
//        this.bean = bean;
//        this.measureBean = measureBean;
        GetEcgWaveRecordFor12 getEcgWaveRecordFor12 = new GetEcgWaveRecordFor12();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        bundle.putSerializable("measureBean", measureBean);
        getEcgWaveRecordFor12.setArguments(bundle);
        return getEcgWaveRecordFor12;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 加载心电图
     */
    private void loadHeartImage() {
        // 心电图宽度，单位为像素(1103恰好为17cm,刚好满足布局)
        final int ImageWidth = 1003;
        final int ImageHeight = 1920;// 心电图高度，单位为像素

        if (measureBean == null) {
            return;
        }
        TaioHeacheckData thd = new TaioHeacheckData();
        thd.setECG_I(Base64.encodeToString(UnitConvertUtil
                .getByteformHexString(measureBean.getEcgI()), Base64.NO_WRAP));
        thd.setECG_II(Base64.encodeToString(UnitConvertUtil
                .getByteformHexString(measureBean.getEcgIi()), Base64.NO_WRAP));
        thd.setECG_III(Base64.encodeToString(UnitConvertUtil
                .getByteformHexString(measureBean.getEcgIii()), Base64.NO_WRAP));
        thd.setECG_aVR(Base64.encodeToString(UnitConvertUtil
                .getByteformHexString(measureBean.getEcgAvr()), Base64.NO_WRAP));
        thd.setECG_aVF(Base64.encodeToString(UnitConvertUtil
                .getByteformHexString(measureBean.getEcgAvf()), Base64.NO_WRAP));
        thd.setECG_aVL(Base64.encodeToString(UnitConvertUtil
                .getByteformHexString(measureBean.getEcgAvl()), Base64.NO_WRAP));
        thd.setECG_V1(Base64.encodeToString(UnitConvertUtil
                .getByteformHexString(measureBean.getEcgV1()), Base64.NO_WRAP));
        thd.setECG_V2(Base64.encodeToString(UnitConvertUtil
                .getByteformHexString(measureBean.getEcgV2()), Base64.NO_WRAP));
        thd.setECG_V3(Base64.encodeToString(UnitConvertUtil
                .getByteformHexString(measureBean.getEcgV3()), Base64.NO_WRAP));
        thd.setECG_V4(Base64.encodeToString(UnitConvertUtil
                .getByteformHexString(measureBean.getEcgV4()), Base64.NO_WRAP));
        thd.setECG_V5(Base64.encodeToString(UnitConvertUtil
                .getByteformHexString(measureBean.getEcgV5()), Base64.NO_WRAP));
        thd.setECG_V6(Base64.encodeToString(UnitConvertUtil
                .getByteformHexString(measureBean.getEcgV6()), Base64.NO_WRAP));

        // 设置创建位图时初始化的颜色
        int[] colors = new int[ImageWidth * ImageHeight];
        for (int y = 0; y < ImageHeight; y++) {
            for (int x = 0; x < ImageWidth; x++) {
                colors[y * ImageWidth + x] = Color.WHITE;
            }
        }

        // 如果不用copy会报Immutable bitmap passed to Canvas constructor错误
        Bitmap bitmapEcg = Bitmap.createBitmap(colors, ImageWidth, ImageHeight,
                Bitmap.Config.RGB_565).copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(bitmapEcg);

        try {
            EcgImage.drawImage(canvas, thd, getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ivHeartImage.setImageBitmap(bitmapEcg);
    }


    /**
     * 初始化病人的体检显示 各类测量数据显示
     */
    private void initView() {
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvSex = (TextView) view.findViewById(R.id.tv_sex);
        tvAge = (TextView) view.findViewById(R.id.tv_age);
        tvQrs = (TextView) view.findViewById(R.id.tv_qrs);
        tvPr = (TextView) view.findViewById(R.id.tv_pr);
        tvQtQtc = (TextView) view.findViewById(R.id.tv_qt_qtc);
        tvPQrsT = (TextView) view.findViewById(R.id.tv_p_qrs_t);
        tvRv5Sv1 = (TextView) view.findViewById(R.id.tv_rv5_sv1);
        tvRv5PlusSv1 = (TextView) view.findViewById(R.id.tv_rv5_plus_sv1);
        tvDistrict = (TextView) view.findViewById(R.id.tv_district);
        tvHr = (TextView) view.findViewById(R.id.tv_hr);
        ivHeartImage = (ImageView) view.findViewById(R.id.iv_heart_image);
        tvEcgDiagnoseResult = (TextView) view.findViewById(R.id.tv_ecg_diagnose_result);
        if (bean != null) {
            tvName.setText(bean.getName());
            tvAge.setText(bean.getAge() == -1 ? "" : (bean.getAge()
                    + getActivity().getString(R.string.unit_age)));
            int sex = bean.getSexy();
            String sexStr;
            switch (sex) {
                case 0:
                    sexStr = getString(R.string.sex_woman);
                    break;
                case 1:
                    sexStr = getString(R.string.sex_man);
                    break;
                default:
                    sexStr = "未知";
                    break;
            }
            tvSex.setText(sexStr);
        }
        String ecgDiagnoseResult = measureBean.getAnal();
        Log.e("lsy", ecgDiagnoseResult + "");
        if (!"-?-".equals(ecgDiagnoseResult) && !TextUtils.isEmpty(ecgDiagnoseResult)) {
            String[] result = ecgDiagnoseResult.split(",");
            if (result.length >= 11) {
                String hrValue = result[0];// HR值
                String prInterval = result[1];// PR间期
                String qrsDuration = result[2];// QRS间期, 单位ms
                String qt = result[3];// QT间期
                String qtc = result[4];// QTC间期
                String pAxis = result[5];// P 波轴
                String qrsAxis = result[6];// QRS波心电轴
                String tAxis = result[7];// T波心电轴
                String rv5 = result[8];// RV5, 单位0.01ms
                String sv1 = result[9];// SV1, 单位0.01ms
                String rv5_Sv1 = result[10];// SV1+RV5, 单位0.01ms

                tvQrs.setText(qrsDuration + " " + "ms");
                tvPr.setText(prInterval + " " + "ms");
                tvQtQtc.setText(qt + "/" + qtc + " " + "ms");
                tvPQrsT.setText(pAxis + "/" + qrsAxis + "/" + tAxis + " " + "°");
                tvRv5Sv1.setText(rv5 + "/" + sv1 + " " + "mV");
                tvRv5PlusSv1.setText(rv5_Sv1 + " " + "mV");
                tvHr.setText(hrValue + " " + "bpm");
                if (result.length >= 12) {
                    tvEcgDiagnoseResult.setText(result[11]);
                }
            }
        }

        loadHeartImage();
    }
}
