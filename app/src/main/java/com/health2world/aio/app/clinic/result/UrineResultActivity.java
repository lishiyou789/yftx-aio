package com.health2world.aio.app.clinic.result;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.health2world.aio.R;
import com.konsung.bean.MeasureBean;
import com.konsung.constant.Urine;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class UrineResultActivity extends Activity {

    private TextView urinert_leu_tv, urinert_nit_tv, urinert_ubg_tv, urinert_pro_tv, urinert_ph_tv,
            urinert_sg_tv, urinert_bld_tv, urinert_ket_tv, urinert_bil_tv, urinert_glu_tv,
            urinert_vc_tv, urinert_alb_tv, urinert_cre_tv, urinert_ca_tv;


    private ImageView leu_icon, nit_icon, ubg_icon, pro_icon, ph_icon, sg_icon, bld_icon, ket_icon,
            bil_icon, glu_icon, vc_icon, alb_icon, cre_icon, ca_icon;

    private MeasureBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(true);
        setContentView(R.layout.activity_urine_result);
        initView();
    }


    private void initView() {
        urinert_leu_tv = findViewById(R.id.urinert_leu_tv);
        urinert_nit_tv = findViewById(R.id.urinert_nit_tv);
        urinert_ubg_tv = findViewById(R.id.urinert_ubg_tv);
        urinert_pro_tv = findViewById(R.id.urinert_pro_tv);
        urinert_ph_tv = findViewById(R.id.urinert_ph_tv);
        urinert_sg_tv = findViewById(R.id.urinert_sg_tv);
        urinert_bld_tv = findViewById(R.id.urinert_bld_tv);
        urinert_ket_tv = findViewById(R.id.urinert_ket_tv);
        urinert_bil_tv = findViewById(R.id.urinert_bil_tv);
        urinert_glu_tv = findViewById(R.id.urinert_glu_tv);
        urinert_vc_tv = findViewById(R.id.urinert_vc_tv);
        urinert_alb_tv = findViewById(R.id.urinert_alb_tv);
        urinert_cre_tv = findViewById(R.id.urinert_cre_tv);
        urinert_ca_tv = findViewById(R.id.urinert_ca_tv);

        leu_icon = findViewById(R.id.leu_icon);
        nit_icon = findViewById(R.id.nit_icon);
        ubg_icon = findViewById(R.id.ubg_icon);
        pro_icon = findViewById(R.id.pro_icon);
        ph_icon = findViewById(R.id.ph_icon);
        sg_icon = findViewById(R.id.sg_icon);
        bld_icon = findViewById(R.id.bld_icon);
        ket_icon = findViewById(R.id.ket_icon);
        bil_icon = findViewById(R.id.bil_icon);
        glu_icon = findViewById(R.id.glu_icon);
        vc_icon = findViewById(R.id.vc_icon);
        alb_icon = findViewById(R.id.alb_icon);
        cre_icon = findViewById(R.id.cre_icon);
        cre_icon = findViewById(R.id.cre_icon);
        ca_icon = findViewById(R.id.ca_icon);

        initData();
    }


    private void initData() {
        if (getIntent().hasExtra("measureBean")) {
            bean = (MeasureBean) getIntent().getSerializableExtra("measureBean");
        }
        if (bean != null) {
            fillView();
        }
    }


    private void fillView() {
        //1
        if (!TextUtils.isEmpty(bean.getUrineLeu())) {
            urinert_leu_tv.setText(parseString(bean.getUrineLeu()));
            if (!bean.getUrineLeu().equals("-")) {
                leu_icon.setVisibility(View.VISIBLE);
                urinert_leu_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            }
        }
        //2
        if (!TextUtils.isEmpty(bean.getUrineNit())) {
            urinert_nit_tv.setText(parseString(bean.getUrineNit()));
            if (!bean.getUrineNit().equals("-")) {
                nit_icon.setVisibility(View.VISIBLE);
                urinert_nit_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            }
        }
        //3
        if (!TextUtils.isEmpty(bean.getUrineUbg())) {
            urinert_ubg_tv.setText(parseString(bean.getUrineUbg()));
            if (!bean.getUrineUbg().equals("-")) {
                ubg_icon.setVisibility(View.VISIBLE);
                urinert_ubg_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            }
        }
        //4
        if (!TextUtils.isEmpty(bean.getUrinePro())) {
            urinert_pro_tv.setText(parseString(bean.getUrinePro()));
            if (!bean.getUrinePro().equals("-")) {
                pro_icon.setVisibility(View.VISIBLE);
                urinert_pro_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            }
        }
        //5
        if (bean.getUrinePh() != 0.0f) {
            urinert_ph_tv.setText(String.valueOf(bean.getUrinePh()));
            if (bean.getUrinePh() > Urine.PH_HIGH) {
                ph_icon.setVisibility(View.VISIBLE);
                ph_icon.setImageResource(com.konsung.R.drawable.alarm_high);
            } else if (bean.getUrinePh() < Urine.PH_LOW) {
                ph_icon.setImageResource(com.konsung.R.drawable.alarm_low);
                ph_icon.setVisibility(View.VISIBLE);
            } else {
                ph_icon.setVisibility(View.INVISIBLE);
            }
        }
        //6
        if (bean.getUrineSg() != 0.0f) {
            urinert_sg_tv.setText(String.format("%.3f", bean.getUrineSg()));
            if ((float)bean.getUrineSg() > Urine.SG_HIGH) {
                sg_icon.setImageResource(com.konsung.R.drawable.alarm_high);
                sg_icon.setVisibility(View.VISIBLE);
                urinert_sg_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            } else if ((float)bean.getUrineSg() < Urine.SG_LOW) {
                sg_icon.setImageResource(com.konsung.R.drawable.alarm_low);
                urinert_sg_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
                sg_icon.setVisibility(View.VISIBLE);
            } else {
                sg_icon.setVisibility(View.INVISIBLE);
            }
        }
        //7
        if (!TextUtils.isEmpty(bean.getUrineBld())) {
            urinert_bld_tv.setText(parseString(bean.getUrineBld()));
            if (!"-".equals(bean.getUrineBld())) {
                bld_icon.setVisibility(View.VISIBLE);
                urinert_bld_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            } else {
                bld_icon.setVisibility(View.INVISIBLE);
            }
        }
        //8
        if (!TextUtils.isEmpty(bean.getUrineKet())) {
            urinert_ket_tv.setText(parseString(bean.getUrineKet()));
            if (!"-".equals(bean.getUrineKet())) {
                ket_icon.setVisibility(View.VISIBLE);
                urinert_ket_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            } else {
                ket_icon.setVisibility(View.INVISIBLE);
            }
        }
        //9
        if (!TextUtils.isEmpty(bean.getUrineBil())) {
            urinert_bil_tv.setText(parseString(bean.getUrineBil()));
            if (!"-".equals(bean.getUrineBil())) {
                bil_icon.setVisibility(View.VISIBLE);
                urinert_bil_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            } else {
                bil_icon.setVisibility(View.INVISIBLE);
            }
        }
        //10
        if (!TextUtils.isEmpty(bean.getUrineGlu())) {
            urinert_glu_tv.setText(parseString(bean.getUrineGlu()));
            if (!"-".equals(bean.getUrineGlu())) {
                glu_icon.setVisibility(View.VISIBLE);
                urinert_glu_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            } else {
                glu_icon.setVisibility(View.INVISIBLE);
            }
        }
        //11
        if (!TextUtils.isEmpty(bean.getUrineVc())) {
            urinert_vc_tv.setText(parseString(bean.getUrineVc()));
            if (!"-".equals(bean.getUrineVc())) {
                vc_icon.setVisibility(View.VISIBLE);
                urinert_vc_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            } else {
                vc_icon.setVisibility(View.INVISIBLE);
            }
        }
        //12
        if (!TextUtils.isEmpty(bean.getUrineMa())) {
            urinert_alb_tv.setText(parseString(bean.getUrineMa()));
            if (!"-".equals(bean.getUrineMa())) {
                alb_icon.setVisibility(View.VISIBLE);
                urinert_alb_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            }
        }

        if (!TextUtils.isEmpty(bean.getUrineCa())) {
            urinert_ca_tv.setText(parseString(bean.getUrineCa()));
            if (!"-".equals(bean.getUrineCa())) {
                ca_icon.setVisibility(View.VISIBLE);
                urinert_ca_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            } else {
                ca_icon.setVisibility(View.INVISIBLE);
            }
        }
        if (!TextUtils.isEmpty(bean.getUrineCre())) {
            urinert_cre_tv.setText(parseString(bean.getUrineCre()));
            if (!"-".equals(bean.getUrineCre())) {
                cre_icon.setVisibility(View.VISIBLE);
                urinert_cre_tv.setTextColor(getResources().getColor(com.konsung.R.color.red));
            } else {
                cre_icon.setVisibility(View.INVISIBLE);
            }
        }
    }

    //减号转换为阴性
    private String parseString(String data) {
        if (data.equals("-"))
            return "阴性";
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
