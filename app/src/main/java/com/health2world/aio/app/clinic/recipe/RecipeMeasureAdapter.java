package com.health2world.aio.app.clinic.recipe;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.health2world.aio.R;
import com.health2world.aio.bean.MeasureItem;
import com.health2world.aio.config.NormalRange;
import com.konsung.bean.MeasureBean;
import com.konsung.listen.Measure;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;
import aio.health2world.spanbuilder.Spans;

import static com.health2world.aio.config.NormalRange.*;

/**
 * Created by lishiyou on 2018/9/28 0028.
 */

public class RecipeMeasureAdapter extends BaseQuickAdapter<MeasureItem, BaseViewHolder> {

    private int sex = 2;

    public RecipeMeasureAdapter(@Nullable List<MeasureItem> data, int sex) {
        super(R.layout.item_task_service, data);
        this.sex = sex;
    }

    @Override
    protected void convert(BaseViewHolder helper, MeasureItem item) {

        if (item.getType() == Measure.GLU) {
            if (item.getMeasureBean().getGluType() == 0)
                helper.setText(R.id.name, "餐前血糖");
            else
                helper.setText(R.id.name, "餐后血糖");
        } else {
            helper.setText(R.id.name, item.getType().getName());
        }
        helper.setVisible(R.id.detail, false);
        if (item.getType() == Measure.ECG ||
                item.getType() == Measure.URINE ||
                item.getType() == Measure.DS100A ||
                item.getType() == Measure.BLOOD) {
            helper.setText(R.id.value, "点击查看数据");
            helper.setTextColor(R.id.value, mContext.getResources().getColor(R.color.appThemeColor));
        } else {
            MeasureBean bean = item.getMeasureBean();
            //血压
            if (item.getType() == Measure.NIBP)
                helper.setText(R.id.value, Spans.builder()
                        .text("" + bean.getSbp(), 17,
                                (bean.getSbp() > NormalRange.NIBP_SBP_MAX || bean.getSbp() < NormalRange.NIBP_SBP_MIN) ?
                                        mContext.getResources().getColor(R.color.red)
                                        : mContext.getResources().getColor(R.color.appThemeColor))
                        .text("/", 17,
                                mContext.getResources().getColor(R.color.appThemeColor))
                        .text("" + bean.getDbp(), 17,
                                (bean.getDbp() > NormalRange.NIBP_DBP_MAX || bean.getDbp() < NormalRange.NIBP_DBP_MIN) ?
                                        mContext.getResources().getColor(R.color.red)
                                        : mContext.getResources().getColor(R.color.appThemeColor))
                        .text("mmHg")
//                        .text("" + bean.getNibPr(), 17,
//                                (bean.getNibPr() > 100 || bean.getNibPr() < 50) ?
//                                        mContext.getResources().getColor(R.color.red)
//                                        : mContext.getResources().getColor(R.color.appThemeColor))
//                        .text("bpm")
                        .build());
            //血氧
            if (item.getType() == Measure.SPO2)
                helper.setText(R.id.value, Spans.builder()
                        .text(bean.getSpo2() + "%", 17, (bean.getSpo2() < SPO2_MIN || bean.getSpo2() > NormalRange.SPO2_MAX) ?
                                mContext.getResources().getColor(R.color.red)
                                : mContext.getResources().getColor(R.color.appThemeColor))
//                        .text(" / ", 17,
//                                mContext.getResources().getColor(R.color.appThemeColor))
//                        .text(bean.getPr() + "bpm", 17, (bean.getPr() < PR_MIN || bean.getPr() > PR_MAX) ?
//                                mContext.getResources().getColor(R.color.red)
//                                : mContext.getResources().getColor(R.color.appThemeColor))
                        .build());

            //脉率单独显示
            if (item.getType() == Measure.PR) {
//                helper.setText(R.id.value, Spans.builder()
//                        .text(bean.getPr() + "bmp", 17,
//                                (bean.getPr() > PR_MAX || bean.getPr() < PR_MIN) ?
//                                        mContext.getResources().getColor(R.color.red)
//                                        : mContext.getResources().getColor(R.color.appThemeColor))
//                        .build());
                if (bean.getHr() != 0) {
                    helper.setText(R.id.value, Spans.builder()
                            .text(bean.getHr() + "bmp", 17,
                                    (bean.getHr() > ECG_HR_MAX || bean.getHr() < ECG_HR_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                } else if (bean.getPr() != 0) {
                    helper.setText(R.id.value, Spans.builder()
                            .text(bean.getPr() + "bmp", 17,
                                    (bean.getPr() > PR_MAX || bean.getPr() < PR_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                } else {
                    helper.setText(R.id.value, Spans.builder()
                            .text(bean.getNibPr() + "bmp", 17,
                                    (bean.getNibPr() > NIBP_NIBPR_MAX || bean.getNibPr() < NIBP_NIBPR_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                }
            }

            //体温
            if (item.getType() == Measure.TEMP)
                helper.setText(R.id.value, Spans.builder()
                        .text(bean.getTemp() + " ℃", 17,
                                (bean.getTemp() > TEMP_MAX || bean.getTemp() < TEMP_MIN) ?
                                        mContext.getResources().getColor(R.color.red)
                                        : mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            //血糖
            if (item.getType() == Measure.GLU) {
                if (bean.getGluType() == 0) {
                    helper.setText(R.id.value, Spans.builder()
                            .text(bean.getGlu() == -1.0f ? "" : bean.getGlu() + "mmol/L", 17,
                                    (bean.getGlu() > GLU_BEFORE_MAX || bean.getGlu() < GLU_BEFORE_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                } else {
                    helper.setText(R.id.value, Spans.builder()
                            .text(bean.getGlu() == -1.0f ? "" : bean.getGlu() + "mmol/L", 17,
                                    (bean.getGlu() > GLU_AFTER_MAX || bean.getGlu() < GLU_BEFORE_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                }
            }
            //总胆固醇
            if (item.getType() == Measure.CHOL)
                helper.setText(R.id.value, Spans.builder()
                        .text(bean.getXzzdgc() + "mmol/L", 17,
                                (bean.getXzzdgc() > BLOOD_TC_MAX || bean.getXzzdgc() < BLOOD_TC_MIN) ?
                                        mContext.getResources().getColor(R.color.red)
                                        : mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            //尿酸
            if (item.getType() == Measure.UA)
                if (sex == 0) {
                    helper.setText(R.id.value, Spans.builder()
                            .text(bean.getUricacid() + "umol/L", 17,
                                    (bean.getUricacid() > UA_WOMAN_MAX || bean.getUricacid() < UA_WOMAN_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                } else {
                    helper.setText(R.id.value, Spans.builder()
                            .text(bean.getUricacid() + "umol/L", 17,
                                    (bean.getUricacid() > UA_MAN_MAX || bean.getUricacid() < UA_MAN_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                }

            //艾康血红蛋白/压积值
            if (item.getType() == Measure.HB) {
                if (sex == 0) {
                    helper.setText(R.id.value, Spans.builder()
                            .text(bean.getAssxhb() + "g/L", 17,
                                    (bean.getAssxhb() > HB_WOMAN_MAX || bean.getAssxhb() < HB_WOMAN_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .text(" / ", 17,
                                    mContext.getResources().getColor(R.color.appThemeColor))
                            .text(bean.getAssxhct() + "%", 17,
                                    (bean.getAssxhct() > HCT_WOMAN_MAX || bean.getAssxhct() < HCT_WOMAN_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                } else {
                    helper.setText(R.id.value, Spans.builder()
                            .text(bean.getAssxhb() + "g/L", 17,
                                    (bean.getAssxhb() > HB_MAN_MAX || bean.getAssxhb() < HB_MAN_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .text(" / ", 17,
                                    mContext.getResources().getColor(R.color.appThemeColor))
                            .text(bean.getAssxhct() + "%", 17,
                                    (bean.getAssxhct() > HCT_MAN_MAX || bean.getAssxhct() < HCT_MAN_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                }

            }
            //身高
            if (item.getType() == Measure.HEIGHT)
                helper.setText(R.id.value, bean.getHeight() + "cm");
            //体重
            if (item.getType() == Measure.WEIGHT)
                helper.setText(R.id.value, bean.getWeight() + "kg");
            //C反应蛋白
            if (item.getType() == Measure.CRP)
                helper.setText(R.id.value, Spans.builder()
                        .text(bean.getFia_crp() + "mg/L", 17, mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            //超敏C反应蛋白
            if (item.getType() == Measure.Hs_CRP)
                helper.setText(R.id.value, Spans.builder()
                        .text(bean.getFia_hscrp() + "mg/L", 17, mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            //血清淀粉样蛋白A
            if (item.getType() == Measure.SAA)
                helper.setText(R.id.value, Spans.builder()
                        .text(bean.getFia_saa() + "mg/L", 17, mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            //降钙素原PCT
            if (item.getType() == Measure.PCT)
                helper.setText(R.id.value, Spans.builder()
                        .text(bean.getFia_pct() + "ng/L", 17, mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            //心肌三项MYO
            if (item.getType() == Measure.MYO) {
                String ctnl = bean.getFia_ctnl().replace("<", "").replace(">", "");
                String ckmb = bean.getFia_ckmb().replace("<", "").replace(">", "");
                String myo = bean.getFia_myo().replace("<", "").replace(">", "");
                //是否异常
                boolean isCtnl = false;
                boolean isCkmb = false;
                boolean icMyo = false;
                try {
                    if (Float.parseFloat(ctnl) > 0.1f)
                        isCtnl = true;
                } catch (Exception e) {
                }
                try {
                    if (Float.parseFloat(ckmb) > 5f)
                        isCkmb = true;

                } catch (Exception e) {
                }
                try {
                    if (Float.parseFloat(myo) > 0f)
                        icMyo = true;
                } catch (Exception e) {

                }
                helper.setText(R.id.value, Spans.builder()
                        .text(TextUtils.isEmpty(bean.getFia_ctnl()) ? "" : "cTnI=" + bean.getFia_ctnl() + "  ",
                                15, isCtnl ? mContext.getResources().getColor(R.color.red) :
                                        mContext.getResources().getColor(R.color.appThemeColor))
                        .text(TextUtils.isEmpty(bean.getFia_ckmb()) ? "" : "CK-MB=" + bean.getFia_ckmb() + "  ",
                                15, isCkmb ? mContext.getResources().getColor(R.color.red) :
                                        mContext.getResources().getColor(R.color.appThemeColor))
                        .text(TextUtils.isEmpty(bean.getFia_myo()) ? "" : "MYO=" + bean.getFia_myo(),
                                15, icMyo ? mContext.getResources().getColor(R.color.red) :
                                        mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            }

            //糖化血红蛋白
            if (item.getType() == Measure.GHB) {
                String ngsp = bean.getNgsp();
                String ifcc = bean.getIfcc();
                String eag = bean.getEag();
                //是否异常
                boolean isNgsp = false;
                boolean isIfcc = false;
                boolean icEag = false;
                try {
                    if (Float.parseFloat(ngsp) > 6.0f || Float.parseFloat(ngsp) < 4.3f)
                        isNgsp = true;
                } catch (Exception e) {
                }
                try {
                    if (Float.parseFloat(ifcc) > 42.1f || Float.parseFloat(ifcc) < 23.5f)
                        isIfcc = true;
                } catch (Exception e) {
                }
                try {
                    if (Float.parseFloat(eag) > 125.5f || Float.parseFloat(eag) < 76.7f)
                        icEag = true;
                } catch (Exception e) {

                }
                helper.setText(R.id.value, Spans.builder()
                        .text(TextUtils.isEmpty(bean.getNgsp()) ? "" : "NGSP=" + bean.getNgsp() + "  ",
                                15, isNgsp ? mContext.getResources().getColor(R.color.red) :
                                        mContext.getResources().getColor(R.color.appThemeColor))
                        .text(TextUtils.isEmpty(bean.getIfcc()) ? "" : "IFCC=" + bean.getIfcc() + "  ",
                                15, isIfcc ? mContext.getResources().getColor(R.color.red) :
                                        mContext.getResources().getColor(R.color.appThemeColor))
                        .text(TextUtils.isEmpty(bean.getEag()) ? "" : "eAG=" + bean.getEag(),
                                15, icEag ? mContext.getResources().getColor(R.color.red) :
                                        mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            }

            //白细胞
            if (item.getType() == Measure.WBC) {
                helper.setText(R.id.value, Spans.builder()
                        .text(bean.getWbc() + " *10^9/L", 17,
                                (bean.getWbc() > WBC_MAX || bean.getWbc() < WBC_MIN) ?
                                        mContext.getResources().getColor(R.color.red)
                                        : mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            }
        }
    }
}
