package com.health2world.aio.app.clinic.result;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.health2world.aio.R;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.common.DataServer;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.utils.ToastUtil;
import uk.co.senab.photoview.PhotoView;


/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class EcgResultActivity extends BaseActivity {
    private TextView tvName;
    private TextView tvSex;
    private TextView tvAge;
    private TextView tvQrs;
    private TextView tvPr;
    private TextView tvQtQtc;
    private TextView tvPQrsT;
    private TextView tvRv5Sv1;
    private TextView tvRv5PlusSv1;
    private TextView tvHr;
    private TextView tvEcgDiagnoseResult;
    private ResidentBean bean;
    private MeasureBean measureBean;

    private PhotoView pvEcg;
    private static List<String> ecgList = new ArrayList<>();
    private EcgReportDrawable drawable;
    private Bitmap bitmap;
    private Canvas canvas;
    private ImageView ivBack;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ecg_result;
    }

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvQrs = (TextView) findViewById(R.id.tv_qrs);
        tvPr = (TextView) findViewById(R.id.tv_pr);
        tvQtQtc = (TextView) findViewById(R.id.tv_qt_qtc);
        tvPQrsT = (TextView) findViewById(R.id.tv_p_qrs_t);
        tvRv5Sv1 = (TextView) findViewById(R.id.tv_rv5_sv1);
        tvRv5PlusSv1 = (TextView) findViewById(R.id.tv_rv5_plus_sv1);
        tvHr = (TextView) findViewById(R.id.tv_hr);
        pvEcg = findView(R.id.iv_heart_image);
        tvEcgDiagnoseResult = (TextView) findViewById(R.id.tv_ecg_diagnose_result);
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("measureBean")) {
            measureBean = (MeasureBean) getIntent().getSerializableExtra("measureBean");
        }
        if (getIntent().hasExtra("resident")) {
            bean = (ResidentBean) getIntent().getSerializableExtra("resident");
        }

        if (bean != null) {
            tvName.setText(bean.getName());
            tvAge.setText(bean.getAge() <= 0 ? "--" : bean.getAge() + "岁");
            tvSex.setText(DataServer.getSexNick(bean.getSexy()));
        }
        String ecgDiagnoseResult = measureBean.getAnal();
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
        if (!TextUtils.isEmpty(measureBean.getFilePath())) {
            Glide.with(this)
                    .load(measureBean.getFilePath())
                    .asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .into(pvEcg);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initReportEcgWave();
                }
            });
        }
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化心电波形
     */
    private void initReportEcgWave() {
        ecgList.clear();
        if (!TextUtils.isEmpty(measureBean.getEcgI()))
            ecgList.add(measureBean.getEcgI());
        if (!TextUtils.isEmpty(measureBean.getEcgIi()))
            ecgList.add(measureBean.getEcgIi());
        if (!TextUtils.isEmpty(measureBean.getEcgIii()))
            ecgList.add(measureBean.getEcgIii());
        if (!TextUtils.isEmpty(measureBean.getEcgAvr()))
            ecgList.add(measureBean.getEcgAvr());
        if (!TextUtils.isEmpty(measureBean.getEcgAvl()))
            ecgList.add(measureBean.getEcgAvl());
        if (!TextUtils.isEmpty(measureBean.getEcgAvf()))
            ecgList.add(measureBean.getEcgAvf());

        if (!TextUtils.isEmpty(measureBean.getEcgV1()))
            ecgList.add(measureBean.getEcgV1());
        if (!TextUtils.isEmpty(measureBean.getEcgV2()))
            ecgList.add(measureBean.getEcgV2());
        if (!TextUtils.isEmpty(measureBean.getEcgV3()))
            ecgList.add(measureBean.getEcgV3());
        if (!TextUtils.isEmpty(measureBean.getEcgV4()))
            ecgList.add(measureBean.getEcgV4());
        if (!TextUtils.isEmpty(measureBean.getEcgV5()))
            ecgList.add(measureBean.getEcgV5());
        if (!TextUtils.isEmpty(measureBean.getEcgV6()))
            ecgList.add(measureBean.getEcgV6());

        if (ecgList.size() < 12) {
            ToastUtil.showShort("心电图波形数据缺失");
            finish();
        } else {
            int widgetWidth = 1024; // 波形控件宽度
            int widgetHeight = 706; // 波形控件高度
            drawable = new EcgReportDrawable(ecgList, widgetHeight, widgetWidth);
            bitmap = Bitmap.createBitmap(widgetWidth, widgetHeight, drawable.getOpacity() !=
                    PixelFormat.OPAQUE ? Bitmap.Config.ARGB_4444 : Bitmap.Config.RGB_565);
            canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, widgetWidth, widgetHeight);
            drawable.draw(canvas);
            pvEcg.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (drawable != null) {
            drawable.drawDone();
            drawable = null;
        }
        if (canvas != null) {
            canvas = null;
        }
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
