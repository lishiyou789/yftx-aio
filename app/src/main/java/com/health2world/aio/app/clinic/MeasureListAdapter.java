package com.health2world.aio.app.clinic;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.bean.MeasureItem;
import com.health2world.aio.bean.MeasureState;
import com.konsung.bean.MeasureBean;
import com.konsung.listen.Measure;

import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;
import aio.health2world.spanbuilder.Spans;

import static com.health2world.aio.config.NormalRange.*;

/**
 * Created by lishiyou on 2017/7/24 0024.
 */

public class MeasureListAdapter extends BaseQuickAdapter<MeasureItem, BaseViewHolder> {
    private int gluType = 0;//0餐前 1餐后
    private int sex = 1;//1男  0 女  2 未知

    public int getGluType() {
        return gluType;
    }

    public void setGluType(int gluType) {
        this.gluType = gluType;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSex() {
        return sex;
    }

    public MeasureListAdapter(List<MeasureItem> data) {
        super(R.layout.item_measure_layout, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MeasureItem item) {
        TextView tvResult = helper.getView(R.id.tvResult);
        TextView tvInput = helper.getView(R.id.tvInput);
        Button btnStartMeasure = helper.getView(R.id.btnStartMeasure);
        LinearLayout radioGroup = helper.getView(R.id.radioGroup);
        MeasureBean bean = item.getMeasureBean();
        Measure type = item.getType();
        MeasureState state = item.getState();

        if (type == Measure.NIBP || type == Measure.SPO2 || type == Measure.ECG)
            helper.addOnClickListener(R.id.btnStartMeasure);

        if (type == Measure.HEIGHT || type == Measure.WEIGHT)
            helper.addOnClickListener(R.id.tvInput);

        //控制View的显示和隐藏
        if (type == Measure.HEIGHT || type == Measure.WEIGHT || type == Measure.DS100A || type == Measure.WBC)
            tvInput.setVisibility(View.VISIBLE);
        else
            tvInput.setVisibility(View.GONE);

        if (type == Measure.NIBP || type == Measure.SPO2 || type == Measure.ECG)
            btnStartMeasure.setVisibility(View.VISIBLE);
        else
            btnStartMeasure.setVisibility(View.GONE);

        if (type == Measure.GLU)
            radioGroup.setVisibility(View.VISIBLE);
        else
            radioGroup.setVisibility(View.GONE);

//        if (type == Measure.NIBP)
//            helper.setVisible(R.id.tvOther,true);
//        else
//            helper.setVisible(R.id.tvOther,false);

        helper.setText(R.id.tvMeasureName, item.getType().getName());
        helper.setText(R.id.tvNormalRange, item.getType().getNormalValue());

        //血压
        if (type == Measure.NIBP) {
            btnStartMeasure.setEnabled(true);
            if (state == MeasureState.MEASURE_ING) {
                btnStartMeasure.setText(mContext.getString(R.string.stop_measure));
                //取消袖带压的显示
//                helper.setText(R.id.tvOther,item.getExtraValue() == null ? "" : item.getExtraValue());
                tvResult.setText(mContext.getString(R.string.measureing));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.blue));
            } else {
//                helper.setText(R.id.tvOther,"");
                if (!item.isMeasured() || bean == null) {
                    btnStartMeasure.setText(mContext.getString(R.string.start_measure));
                    tvResult.setText(mContext.getString(R.string.not_measure));
                    tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
                } else {
                    btnStartMeasure.setText(mContext.getString(R.string.remeasure));
                    tvResult.setText(Spans.builder()
                            .text("" + bean.getSbp(), 17,
                                    (bean.getSbp() > NIBP_SBP_MAX || bean.getSbp() < NIBP_SBP_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .text("/", 17,
                                    mContext.getResources().getColor(R.color.appThemeColor))
                            .text("" + bean.getDbp(), 17,
                                    (bean.getDbp() > NIBP_DBP_MAX || bean.getDbp() < NIBP_DBP_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .text("mmHg", 17, mContext.getResources().getColor(R.color.appThemeColor))
//                            .text(bean.getNibPr() + "bpm", 17,
//                                    (bean.getNibPr() > 100 || bean.getNibPr() < 50) ?
//                                            mContext.getResources().getColor(R.color.red)
//                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                }
            }
        }

        //血氧
        if (type == Measure.SPO2) {
            if (state == MeasureState.MEASURE_READY) {
                btnStartMeasure.setEnabled(true);
                btnStartMeasure.setBackgroundResource(R.drawable.selector_button_blue_hollow_bg);
                btnStartMeasure.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                if (item.isMeasured()) {
                    btnStartMeasure.setText(mContext.getString(R.string.remeasure));
                } else {
                    btnStartMeasure.setText(mContext.getString(R.string.start_measure));
                }
            } else if (state == MeasureState.MEASURE_ING) {
                btnStartMeasure.setEnabled(false);
                btnStartMeasure.setText(mContext.getString(R.string.measureing));
                btnStartMeasure.setBackgroundResource(R.drawable.shape_measure_button_enable);
                btnStartMeasure.setTextColor(mContext.getResources().getColor(R.color.white));
            } else if (state == MeasureState.MEASURE_NONE) {
                btnStartMeasure.setEnabled(false);
                btnStartMeasure.setText(item.getExtraValue());
                btnStartMeasure.setBackgroundResource(R.drawable.shape_measure_button_enable);
                btnStartMeasure.setTextColor(mContext.getResources().getColor(R.color.white));
            }
            if (state == MeasureState.MEASURE_ING) {
                tvResult.setText(mContext.getString(R.string.wait_result));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.blue));
            } else {
                if (item.isMeasured() && bean != null) {
                    tvResult.setText(Spans.builder()
                            .text(bean.getSpo2() + "%", 17, (bean.getSpo2() < SPO2_MIN || bean.getSpo2() > SPO2_MAX) ?
                                    mContext.getResources().getColor(R.color.red)
                                    : mContext.getResources().getColor(R.color.appThemeColor))
//                            .text("%", 17, mContext.getResources().getColor(R.color.appThemeColor))
//                            .text(" / ", 17,
//                                    mContext.getResources().getColor(R.color.appThemeColor))
//                            .text(bean.getPr() + "", 17, (bean.getPr() < 50 || bean.getPr() > 100) ?
//                                    mContext.getResources().getColor(R.color.red)
//                                    : mContext.getResources().getColor(R.color.appThemeColor))
//                            .text(" bpm", 17, mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                } else {
                    tvResult.setText(mContext.getString(R.string.not_measure));
                    tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
                }
            }
        }

        //脉率单独显示
        if (type == Measure.PR) {
            if (bean == null || (bean.getHr() == 0 && bean.getPr() == 0 && bean.getNibPr() == 0)) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                if (bean.getHr() != 0) {
                    tvResult.setText(Spans.builder()
                            .text(bean.getHr() + "bmp", 17,
                                    (bean.getHr() > ECG_HR_MAX || bean.getHr() < ECG_HR_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                } else if (bean.getPr() != 0) {
                    tvResult.setText(Spans.builder()
                            .text(bean.getPr() + "bmp", 17,
                                    (bean.getPr() > PR_MAX || bean.getPr() < PR_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                } else {
                    tvResult.setText(Spans.builder()
                            .text(bean.getNibPr() + "bmp", 17,
                                    (bean.getNibPr() > NIBP_NIBPR_MAX || bean.getNibPr() < NIBP_NIBPR_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                }
            }
        }

        //心电
        if (type == Measure.ECG) {
            if (state == MeasureState.MEASURE_READY) {
                btnStartMeasure.setEnabled(true);
                btnStartMeasure.setBackgroundResource(R.drawable.selector_button_blue_hollow_bg);
                btnStartMeasure.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                if (item.isMeasured()) {
                    btnStartMeasure.setText(mContext.getString(R.string.remeasure));
                } else {
                    btnStartMeasure.setText(mContext.getString(R.string.start_measure));
                }
            } else if (state == MeasureState.MEASURE_ING) {
                btnStartMeasure.setEnabled(false);
                btnStartMeasure.setText(mContext.getString(R.string.measureing));
                btnStartMeasure.setBackgroundResource(R.drawable.shape_measure_button_enable);
                btnStartMeasure.setTextColor(mContext.getResources().getColor(R.color.white));
            } else if (state == MeasureState.MEASURE_NONE) {
                btnStartMeasure.setEnabled(false);
                btnStartMeasure.setText(item.getExtraValue());
                btnStartMeasure.setBackgroundResource(R.drawable.shape_measure_button_enable);
                btnStartMeasure.setTextColor(mContext.getResources().getColor(R.color.white));
            }

            if (state == MeasureState.MEASURE_ING) {
                tvResult.setText(mContext.getString(R.string.wait_result));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.blue));
            } else {
                if (item.isMeasured()) {
                    tvResult.setTextColor(mContext.getResources().getColor(R.color.appThemeColor));
                    tvResult.setText(R.string.click_see);
                } else {
                    tvResult.setText(mContext.getString(R.string.not_measure));
                    tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
                }
            }
        }

        //体温
        if (type == Measure.TEMP) {
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                tvResult.setText(Spans.builder()
                        .text(bean.getTemp() + " ℃", 17,
                                (bean.getTemp() > TEMP_MAX || bean.getTemp() < TEMP_MIN) ?
                                        mContext.getResources().getColor(R.color.red)
                                        : mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            }
        }

        //血糖
        if (type == Measure.GLU) {
            if (gluType == 0) {
                ((RadioButton) helper.getView(R.id.rb0)).setChecked(true);
                ((RadioButton) helper.getView(R.id.rb1)).setChecked(false);
                helper.setText(R.id.tvNormalRange, item.getType().getNormalValue());
            } else {
                ((RadioButton) helper.getView(R.id.rb0)).setChecked(false);
                ((RadioButton) helper.getView(R.id.rb1)).setChecked(true);
                helper.setText(R.id.tvNormalRange, "<" + GLU_AFTER_MAX + "mmol/L");
            }
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                if (gluType == 0) {
                    tvResult.setText(Spans.builder()
                            .text(bean.getGlu() == -1.0f ? "" : bean.getGlu() + "mmol/L", 17,
                                    (bean.getGlu() > GLU_BEFORE_MAX || bean.getGlu() < GLU_BEFORE_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                } else {
                    tvResult.setText(Spans.builder()
                            .text(bean.getGlu() == -1.0f ? "" : bean.getGlu() + "mmol/L", 17,
                                    (bean.getGlu() > GLU_AFTER_MAX || bean.getGlu() < GLU_AFTER_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                }
            }
            helper.addOnClickListener(R.id.rb0);
            helper.addOnClickListener(R.id.rb1);
        }

        //尿常规
        if (type == Measure.URINE) {
            if (!item.isMeasured()) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                tvResult.setText(R.string.click_see);
                tvResult.setTextColor(mContext.getResources().getColor(R.color.appThemeColor));
            }
        }

        //总胆固醇
        if (type == Measure.CHOL) {
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                tvResult.setText(Spans.builder()
                        .text(bean.getXzzdgc() + "mmol/L", 17,
                                (bean.getXzzdgc() > CHOL_MAX || bean.getXzzdgc() < CHOL_MIN) ?
                                        mContext.getResources().getColor(R.color.red)
                                        : mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            }
        }

        //尿酸
        if (type == Measure.UA) {
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                //尿酸根据男女判断
                if (sex == 0) {
                    tvResult.setText(Spans.builder()
                            .text(bean.getUricacid() + "μmmol/L", 17,
                                    (bean.getUricacid() > UA_WOMAN_MAX || bean.getUricacid() < UA_WOMAN_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                } else {
                    tvResult.setText(Spans.builder()
                            .text(bean.getUricacid() + "μmmol/L", 17,
                                    (bean.getUricacid() > UA_MAN_MAX || bean.getUricacid() < UA_MAN_MIN) ?
                                            mContext.getResources().getColor(R.color.red)
                                            : mContext.getResources().getColor(R.color.appThemeColor))
                            .build());
                }
            }
        }

        //血脂四项
        if (type == Measure.BLOOD) {
            if (!item.isMeasured()) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                tvResult.setText(R.string.click_see);
                tvResult.setTextColor(mContext.getResources().getColor(R.color.appThemeColor));
            }
        }

        //艾康血红蛋白/压积值
        if (type == Measure.HB) {
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                //血红蛋白根据男女判断
                if (sex == 0) {
                    tvResult.setText(Spans.builder()
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
                    tvResult.setText(Spans.builder()
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
        }

        //身高体重
        if (type == Measure.HEIGHT || type == Measure.WEIGHT) {
            tvInput.setText(item.getExtraValue());
            tvInput.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                if (type == Measure.HEIGHT) {
                    tvResult.setText(bean.getHeight() + "cm");
                }
                if (type == Measure.WEIGHT) {
                    tvResult.setText(bean.getWeight() + "kg");
                }
                tvResult.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
        }

        //血脂八项（大树血脂）
        if (type == Measure.DS100A) {
            if (item.isConnect()) {
                tvInput.setText(mContext.getResources().getString(R.string.device_connect));
                tvInput.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                tvInput.setText(mContext.getResources().getString(R.string.device_no_connect));
                tvInput.setTextColor(mContext.getResources().getColor(R.color.black9));
            }
            if (!item.isMeasured()) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                tvResult.setText(R.string.click_see);
                tvResult.setTextColor(mContext.getResources().getColor(R.color.appThemeColor));
            }
            helper.addOnClickListener(R.id.tvInput);
        }

        //C反应蛋白
        if (type == Measure.CRP) {
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                tvResult.setText(Spans.builder()
                        .text(bean.getFia_crp() + "mg/L", 17,
                                (Float.parseFloat(bean.getFia_crp().replace("<", "")) > CRP_MAX
                                        || Float.parseFloat(bean.getFia_crp().replace("<", "")) < CRP_MIN)
                                        ? mContext.getResources().getColor(R.color.red) :
                                        mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            }
        }

        //超敏C反应蛋白
        if (type == Measure.Hs_CRP) {
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                tvResult.setText(Spans.builder()
                        .text(bean.getFia_hscrp() + "mg/L", 17,
                                (Float.parseFloat(bean.getFia_hscrp().replace("<", "").replace(">", "")) > HSCRP_MAX ||
                                        Float.parseFloat(bean.getFia_hscrp().replace("<", "").replace(">", "")) < HSCRP_MIN)
                                        ? mContext.getResources().getColor(R.color.red) :
                                        mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            }
        }

        //血清淀粉样蛋白A
        if (type == Measure.SAA) {
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                tvResult.setText(Spans.builder()
                        .text(bean.getFia_saa() + "mg/L", 17,
                                (Float.parseFloat(bean.getFia_saa()) > SAA_MAX || Float.parseFloat(bean.getFia_saa()) < SAA_MIN)
                                        ? mContext.getResources().getColor(R.color.red) :
                                        mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            }
        }

        //降钙素原
        if (type == Measure.PCT) {
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                tvResult.setText(Spans.builder()
                        .text(bean.getFia_pct() + "ng/mL", 17,
                                (Float.parseFloat(bean.getFia_pct()) > PCT_MAX || Float.parseFloat(bean.getFia_pct()) < PCT_MIN)
                                        ? mContext.getResources().getColor(R.color.red) :
                                        mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            }
        }

        //心肌三项
        if (type == Measure.MYO) {
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
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
                tvResult.setText(Spans.builder()
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
        }

        //糖化血红蛋白
        if (type == Measure.GHB) {
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
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
                tvResult.setText(Spans.builder()
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
        }
        //白细胞
        if (type == Measure.WBC) {
//            if (item.isConnect()) {
//                tvInput.setText(mContext.getResources().getString(R.string.device_connect));
//                tvInput.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
//            } else {
//                tvInput.setText(mContext.getResources().getString(R.string.device_no_connect));
//                tvInput.setTextColor(mContext.getResources().getColor(R.color.black9));
//            }
            tvInput.setText("");
            if (!item.isMeasured() || bean == null) {
                tvResult.setText(mContext.getString(R.string.not_measure));
                tvResult.setTextColor(mContext.getResources().getColor(R.color.black9));
            } else {
                tvResult.setText(Spans.builder()
                        .text(bean.getWbc() + " *10^9/L", 17,
                                (bean.getWbc() > WBC_MAX || bean.getWbc() < WBC_MIN) ?
                                        mContext.getResources().getColor(R.color.red)
                                        : mContext.getResources().getColor(R.color.appThemeColor))
                        .build());
            }
            helper.addOnClickListener(R.id.tvInput);
        }
    }
}
