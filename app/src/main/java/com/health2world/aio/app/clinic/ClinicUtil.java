package com.health2world.aio.app.clinic;

import com.health2world.aio.bean.MeasureItem;
import com.health2world.aio.bean.MeasureState;
import com.health2world.aio.config.NormalRange;
import com.health2world.aio.util.Logger;
import com.health2world.aio.util.RandomUtil;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.StateBean;
import com.konsung.constant.Configuration;
import com.konsung.listen.Measure;

import java.io.BufferedReader;
import java.util.List;

import aio.health2world.utils.MatchUtil;

import static aio.health2world.utils.MatchUtil.roundHalfUp3;

/**
 * Created by lishiyou on 2018/8/21 0021.
 */

public class ClinicUtil {

    public int isEcg = -1;
    public int isSpo2 = -1;
    private int mCuff = 0;
    //动态记录每一个测量项的位置

    public int ITEM_NIBP = -1;//血压
    public int ITEM_SPO2 = -1;//血氧
    public int ITEM_PR = -1;//脉率
    public int ITEM_ECG = -1;//心电
    public int ITEM_TEMP = -1;//体温
    public int ITEM_GLU = -1;//血糖
    public int ITEM_URINE = -1;//尿常规
    public int ITEM_CHOL = -1;//总胆固醇
    public int ITEM_UA = -1;//尿酸
    public int ITEM_BLOOD = -1;//血脂
    public int ITEM_HB = -1;//血红蛋白
    public int ITEM_HEIGHT = -1;//身高
    public int ITEM_WEIGHT = -1;//体重
    public int ITEM_DS100A = -1;//大树血脂
    public int ITEM_CRP = -1;//C反应蛋白
    public int ITEM_HSCRP = -1;//超敏C反应蛋白
    public int ITEM_SAA = -1;//血清淀粉样蛋白A
    public int ITEM_PCT = -1;//降钙素原
    public int ITEM_MYO = -1;//心肌三项
    public int ITEM_GHB = -1;//糖化血红蛋白
    public int ITEM_WBC = -1;//白细胞

    public void initPosition(List<MeasureItem> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            Measure measure = dataList.get(i).getType();
            if (measure == Measure.NIBP) {
                ITEM_NIBP = i;
            } else if (measure == Measure.SPO2) {
                ITEM_SPO2 = i;
            } else if (measure == Measure.ECG) {
                ITEM_ECG = i;
            } else if (measure == Measure.TEMP) {
                ITEM_TEMP = i;
            } else if (measure == Measure.URINE) {
                ITEM_URINE = i;
            } else if (measure == Measure.GLU) {
                ITEM_GLU = i;
            } else if (measure == Measure.UA) {
                ITEM_UA = i;
            } else if (measure == Measure.CHOL) {
                ITEM_CHOL = i;
            } else if (measure == Measure.HB) {
                ITEM_HB = i;
            } else if (measure == Measure.BLOOD) {
                ITEM_BLOOD = i;
            } else if (measure == Measure.HEIGHT) {
                ITEM_HEIGHT = i;
            } else if (measure == Measure.WEIGHT) {
                ITEM_WEIGHT = i;
            } else if (measure == Measure.DS100A) {
                ITEM_DS100A = i;
            } else if (measure == Measure.CRP) {
                ITEM_CRP = i;
            } else if (measure == Measure.Hs_CRP) {
                ITEM_HSCRP = i;
            } else if (measure == Measure.SAA) {
                ITEM_SAA = i;
            } else if (measure == Measure.PCT) {
                ITEM_PCT = i;
            } else if (measure == Measure.MYO) {
                ITEM_MYO = i;
            } else if (measure == Measure.GHB) {
                ITEM_GHB = i;
            } else if (measure == Measure.WBC) {
                ITEM_WBC = i;
            } else if (measure == Measure.PR) {
                ITEM_PR = i;
            }
        }
    }

    public void resetAllPosition() {
        isEcg = -1;
        isSpo2 = -1;

        ITEM_NIBP = -1;
        ITEM_SPO2 = -1;
        ITEM_PR = -1;
        ITEM_ECG = -1;
        ITEM_TEMP = -1;
        ITEM_URINE = -1;
        ITEM_GLU = -1;
        ITEM_UA = -1;
        ITEM_CHOL = -1;
        ITEM_HB = -1;
        ITEM_BLOOD = -1;
        ITEM_HEIGHT = -1;
        ITEM_WEIGHT = -1;
        ITEM_DS100A = -1;//蓝牙传输数据
        ITEM_CRP = -1;
        ITEM_SAA = -1;
        ITEM_PCT = -1;
        ITEM_MYO = -1;
        ITEM_WBC = -1;
        ITEM_GHB = -1;
    }

    public void onComplete(Measure param, MeasureBean bean, MeasureBean dataBean,
                           List<MeasureItem> dataList, MeasureListAdapter measureAdapter) {
        //血压NIBP
        if (param.equals(Measure.NIBP) && ITEM_NIBP != -1) {

            dataBean.setSbp(bean.getSbp());
            dataBean.setDbp(bean.getDbp());
            dataBean.setMbp(bean.getMbp());
            dataBean.setNibPr(bean.getNibPr());

            MeasureItem item = dataList.get(ITEM_NIBP);
            item.setState(MeasureState.MEASURE_READY);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_NIBP);

            if (ITEM_PR != -1) {
                MeasureItem itemPr = dataList.get(ITEM_PR);
                dataBean.setNibPr(bean.getNibPr());
                itemPr.setMeasured(true);
                itemPr.setMeasureBean(dataBean);
                measureAdapter.notifyItemChanged(ITEM_PR);
            }

        }

        //血氧SPO2
        if (param.equals(Measure.SPO2) && ITEM_SPO2 != -1) {

            dataBean.setSpo2(bean.getSpo2());
            dataBean.setPr(bean.getPr());
            MeasureItem item = dataList.get(ITEM_SPO2);
            item.setState(MeasureState.MEASURE_READY);
            isSpo2 = 0;
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_SPO2);

            if (ITEM_PR != -1) {
                MeasureItem itemPr = dataList.get(ITEM_PR);
                dataBean.setPr(bean.getPr());
                itemPr.setMeasured(true);
                itemPr.setMeasureBean(dataBean);
                measureAdapter.notifyItemChanged(ITEM_PR);
            }

        }

        //心电ECG
        if (param.equals(Measure.ECG) && ITEM_ECG != -1) {

            dataBean.setDuration(bean.getDuration());
            dataBean.setSample(bean.getSample());
            dataBean.setP05(bean.getP05());
            dataBean.setN05(bean.getN05());
            dataBean.setHr(bean.getHr());
            dataBean.setAnal(bean.getAnal());
            dataBean.setWaveSpeed(bean.getWaveSpeed());
            dataBean.setWaveGain(bean.getWaveGain());
            dataBean.setRespRr(bean.getRespRr());
            dataBean.setRESP(bean.getRESP());
            dataBean.setEcgAvf(bean.getEcgAvf());
            dataBean.setEcgAvl(bean.getEcgAvl());
            dataBean.setEcgAvr(bean.getEcgAvr());
            dataBean.setEcgI(bean.getEcgI());
            dataBean.setEcgIi(bean.getEcgIi());
            dataBean.setEcgIii(bean.getEcgIii());
            dataBean.setEcgV1(bean.getEcgV1());
            dataBean.setEcgV2(bean.getEcgV2());
            dataBean.setEcgV3(bean.getEcgV3());
            dataBean.setEcgV4(bean.getEcgV4());
            dataBean.setEcgV5(bean.getEcgV5());
            dataBean.setEcgV6(bean.getEcgV6());

            MeasureItem item = dataList.get(ITEM_ECG);
            item.setState(MeasureState.MEASURE_READY);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);

            measureAdapter.notifyItemChanged(ITEM_ECG);

            if (ITEM_PR != -1) {
                MeasureItem itemHr = dataList.get(ITEM_PR);
                dataBean.setHr(bean.getHr());
                itemHr.setMeasured(true);
                itemHr.setMeasureBean(dataBean);
                measureAdapter.notifyItemChanged(ITEM_PR);
            }
        }

        //体温TEMP
        if (param.equals(Measure.TEMP) && ITEM_TEMP != -1) {

            dataBean.setTemp(bean.getTemp());

            MeasureItem item = dataList.get(ITEM_TEMP);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_TEMP);

        }
        //尿常规URNIE
        if (param.equals(Measure.URINE) && ITEM_URINE != -1) {

            dataBean.setUrinePh(bean.getUrinePh());
            dataBean.setUrineUbg(bean.getUrineUbg());
            dataBean.setUrineBld(bean.getUrineBld());
            dataBean.setUrinePro(bean.getUrinePro());
            dataBean.setUrineKet(bean.getUrineKet());
            dataBean.setUrineNit(bean.getUrineNit());
            dataBean.setUrineGlu(bean.getUrineGlu());
            dataBean.setUrineBil(bean.getUrineBil());
            dataBean.setUrineLeu(bean.getUrineLeu());
            dataBean.setUrineSg(bean.getUrineSg());
            dataBean.setUrineCre(bean.getUrineCre());
            dataBean.setUrineCa(bean.getUrineCa());
            dataBean.setUrineMa(bean.getUrineMa());
            dataBean.setUrineVc(bean.getUrineVc());
            dataBean.setUrineAsc(bean.getUrineAsc());

            MeasureItem item = dataList.get(ITEM_URINE);
            item.setState(MeasureState.MEASURE_READY);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_URINE);
        }

        //血脂四项
        if (param.equals(Measure.BLOOD) && ITEM_BLOOD != -1) {

            dataBean.setBlood_hdl(bean.getBlood_hdl());
            dataBean.setBlood_ldl(bean.getBlood_ldl());
            dataBean.setBlood_tc(bean.getBlood_tc());
            dataBean.setBlood_tg(bean.getBlood_tg());

            MeasureItem item = dataList.get(ITEM_BLOOD);
            item.setState(MeasureState.MEASURE_READY);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_BLOOD);
        }

        //血脂八项
        if (param.equals(Measure.DS100A) && ITEM_DS100A != -1) {
            dataBean.setGluTree(bean.getGluTree());
            dataBean.setBlood_tc(bean.getBlood_tc());
            dataBean.setBlood_tg(bean.getBlood_tg());
            dataBean.setBlood_hdl(bean.getBlood_hdl());
            dataBean.setBlood_ldl(bean.getBlood_ldl());
            dataBean.setBlood_vldl(bean.getBlood_vldl());
            dataBean.setBlood_ai(bean.getBlood_ai());
            dataBean.setBlood_r_chd(bean.getBlood_r_chd());
            dataList.get(ITEM_DS100A).setMeasured(true);
            dataList.get(ITEM_DS100A).setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_DS100A);
        }

        //血糖GLU
        if (param.equals(Measure.GLU) && ITEM_GLU != -1) {

            dataBean.setGlu(bean.getGlu());
            dataBean.setGluStyle(bean.getGluStyle());
            MeasureItem item = dataList.get(ITEM_GLU);
            item.setState(MeasureState.MEASURE_READY);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_GLU);
        }

        //尿酸UA
        if (param.equals(Measure.UA) && ITEM_UA != -1) {

            dataBean.setUricacid(bean.getUricacid());

            MeasureItem item = dataList.get(ITEM_UA);
            item.setState(MeasureState.MEASURE_READY);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);

            measureAdapter.notifyItemChanged(ITEM_UA);
        }

        //总胆固醇CHOL
        if (param.equals(Measure.CHOL) && ITEM_CHOL != -1) {

            dataBean.setXzzdgc(bean.getXzzdgc());

            MeasureItem item = dataList.get(ITEM_CHOL);
            item.setState(MeasureState.MEASURE_READY);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);

            measureAdapter.notifyItemChanged(ITEM_CHOL);
        }

        //血红蛋白（HB）和压积值
        if ((param.equals(Measure.HB) || param.equals(Measure.HCT)) && ITEM_HB != -1) {

            dataBean.setAssxhb(bean.getAssxhb());
            dataBean.setAssxhct(bean.getAssxhct());

            MeasureItem item = dataList.get(ITEM_HB);
            item.setState(MeasureState.MEASURE_READY);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);

            measureAdapter.notifyItemChanged(ITEM_HB);
        }

        //干式荧光免疫分析之 C反应蛋白
        if (param.equals(Measure.CRP) && ITEM_CRP != -1) {
            if (bean.getFia_crp() != null)
                dataBean.setFia_crp(bean.getFia_crp());

            MeasureItem item = dataList.get(ITEM_CRP);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_CRP);
        }

        //超敏 C反应蛋白
        if (param.equals(Measure.Hs_CRP) && ITEM_HSCRP != -1) {
            if (bean.getFia_hscrp() != null)
                dataBean.setFia_hscrp(bean.getFia_hscrp());

            MeasureItem item = dataList.get(ITEM_HSCRP);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_HSCRP);
        }

        //干式荧光免疫分析之血清淀粉样蛋白A
        if (param.equals(Measure.SAA) && ITEM_SAA != -1) {
            dataBean.setFia_saa(bean.getFia_saa());
            MeasureItem item = dataList.get(ITEM_SAA);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_SAA);
        }

        //降钙素原
        if (param.equals(Measure.PCT) && ITEM_PCT != -1) {
            dataBean.setFia_pct(bean.getFia_pct());
            MeasureItem item = dataList.get(ITEM_PCT);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_PCT);
        }

        //干式荧光免疫分析之心肌三项
        if (param.equals(Measure.MYO) && ITEM_MYO != -1) {
            if (bean.getFia_ctnl() != null)
                dataBean.setFia_ctnl(bean.getFia_ctnl());
            if (bean.getFia_ckmb() != null)
                dataBean.setFia_ckmb(bean.getFia_ckmb());
            if (bean.getFia_myo() != null)
                dataBean.setFia_myo(bean.getFia_myo());

            MeasureItem item = dataList.get(ITEM_MYO);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_MYO);
        }

        //白细胞
        if (param.equals(Measure.WBC) && ITEM_WBC != -1) {
            dataBean.setWbc(bean.getWbc());
            dataBean.setWbc_bas(bean.getWbc_bas());
            dataBean.setWbc_eos(bean.getWbc_eos());
            dataBean.setWbc_lym(bean.getWbc_lym());
            dataBean.setWbc_mon(bean.getWbc_mon());
            dataBean.setWbc_neu(bean.getWbc_neu());
            MeasureItem item = dataList.get(ITEM_WBC);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_WBC);
        }

        //糖化血红蛋白
        if (param.equals(Measure.GHB) && ITEM_GHB != -1) {

        }
    }


    public void onFail(Measure param, List<MeasureItem> dataList, MeasureListAdapter measureAdapter) {
        if (param == Measure.ECG && ITEM_ECG != -1) {
            dataList.get(ITEM_ECG).setState(MeasureState.MEASURE_READY);
            measureAdapter.notifyItemChanged(ITEM_ECG);
        }
        if (param == Measure.NIBP && ITEM_NIBP != -1) {
            dataList.get(ITEM_NIBP).setState(MeasureState.MEASURE_READY);
            measureAdapter.notifyItemChanged(ITEM_NIBP);
        }
        if (param == Measure.SPO2 && ITEM_SPO2 != -1) {
            dataList.get(ITEM_SPO2).setState(MeasureState.MEASURE_READY);
            measureAdapter.notifyItemChanged(ITEM_SPO2);
        }
    }

    public void nibpCuff(int value, List<MeasureItem> dataList, MeasureListAdapter measureAdapter) {
        if (mCuff != value && ITEM_NIBP != -1) {
            mCuff = value;
            dataList.get(ITEM_NIBP).setExtraValue(mCuff + "");
            measureAdapter.notifyItemChanged(ITEM_NIBP);
        }
    }

    public void onState(StateBean bean, List<MeasureItem> dataList, MeasureListAdapter measureAdapter) {
        if (bean.getProbe() && bean.getFinger()) {
            if (isSpo2 != 0) {
                isSpo2 = 0;
                if (ITEM_SPO2 != -1) {
                    dataList.get(ITEM_SPO2).setState(MeasureState.MEASURE_READY);
                    measureAdapter.notifyItemChanged(ITEM_SPO2);
                }
            }
        } else if (!bean.getProbe()) {
            if (isSpo2 != 1) {
                isSpo2 = 1;
                if (ITEM_SPO2 != -1) {
                    dataList.get(ITEM_SPO2).setState(MeasureState.MEASURE_NONE);
                    dataList.get(ITEM_SPO2).setExtraValue("插入探头");
                    measureAdapter.notifyItemChanged(ITEM_SPO2);
                }
            }
        } else {
            if (isSpo2 != 2) {
                isSpo2 = 2;
                if (ITEM_SPO2 != -1) {
                    dataList.get(ITEM_SPO2).setExtraValue("放入手指");
                    dataList.get(ITEM_SPO2).setState(MeasureState.MEASURE_NONE);
                    measureAdapter.notifyItemChanged(ITEM_SPO2);
                }
            }
        }

        if (bean.getLa() && bean.getLl() && bean.getRa()
                && bean.getV1() && bean.getV2() && bean.getV3()
                && bean.getV4() && bean.getV5() && bean.getV6()) {
            if (isEcg != 0) {
                isEcg = 0;
                if (ITEM_ECG != -1) {
                    dataList.get(ITEM_ECG).setState(MeasureState.MEASURE_READY);
                    measureAdapter.notifyItemChanged(ITEM_ECG);
                }

            }
        } else {
            if (isEcg != 1) {
                isEcg = 1;
                if (ITEM_ECG != -1) {
                    dataList.get(ITEM_ECG).setState(MeasureState.MEASURE_NONE);
                    dataList.get(ITEM_ECG).setExtraValue("检查导联线");
                    measureAdapter.notifyItemChanged(ITEM_ECG);
                }
            }
        }
    }

    /**
     * 解析数据  血脂八项
     *
     * @param data 数据源
     */
    public void analysisData(String data, MeasureBean dataBean) {
        //HDL-C
        int v1 = Integer.parseInt(data.substring(32, 36), 16);
        dataBean.setBlood_hdl(String.valueOf(v1 / 100f));
        //GLU
        int v2 = Integer.parseInt(data.substring(38, 42), 16);
        dataBean.setGluTree(String.valueOf(v2 / 10f));
        //TC
        int v3 = Integer.parseInt(data.substring(44, 48), 16);
        dataBean.setBlood_tc(String.valueOf(v3 / 100f));
        //TG
        int v4 = Integer.parseInt(data.substring(50, 54), 16);
        dataBean.setBlood_tg(String.valueOf(v4 / 100f));
        //VLDL-C
        int v5 = Integer.parseInt(data.substring(56, 60), 16);
        dataBean.setBlood_vldl(String.valueOf(v5 / 100f));
        //LDL-C
        int v6 = Integer.parseInt(data.substring(62, 66), 16);
        dataBean.setBlood_ldl(String.valueOf(v6 / 100f));

        if (v1 == 0) {
            //AI
            dataBean.setBlood_ai(String.valueOf(0));
            //R-CHD
            dataBean.setBlood_r_chd(String.valueOf(0));
        } else {
            float AI = Float.parseFloat(MatchUtil.transferValue((v3 / 100f - v1 / 100f) / (v1 / 100f)));
            if (AI <= 0) {
                //AI
                dataBean.setBlood_ai(String.valueOf(0));
            } else {
                //AI
                dataBean.setBlood_ai(String.valueOf(AI));
            }
            float RCHD = Float.parseFloat(MatchUtil.transferValue((v3 / 100f) / (v1 / 100f)));
            //R-CHD
            dataBean.setBlood_r_chd(String.valueOf(RCHD));
        }
    }

    /**
     * 解析数据  白细胞
     * 返回结果分三次，只取第一次开头为A55544的40位16进制数据（全部包含在第一次返回结果中）：
     * A55544002024  0424 0026 000C 0001 0017 0002 0000
     * 每4位转换为10进制并四舍五入保留一位小数
     * 其中0-12位为校验位，详情如上，其余位数如下；
     * 13-16：样本编号，
     * 17-20：WBC，
     * 21-24：LYM,
     * 25-28：MON，
     * 29-32：NEU，
     * 33-36：EOS,
     * 37-40：BAS;
     */
    public void analysisWbcData(String data, MeasureBean dataBean) {
        String res = data.substring(12, 40);
        //编号
        int index = Integer.parseInt(res.substring(0, 4), 16);
        //白细胞
        int wbc = Integer.parseInt(res.substring(5, 8), 16);
//        //尝试修复白细胞大数据变成负数问题 ----0311
//        if(wbc > 65535)
//            wbc -= 65535;
        dataBean.setWbc(MatchUtil.roundHalfUp(wbc));
        int lym = Integer.parseInt(res.substring(9, 12), 16);
        dataBean.setWbc_lym(MatchUtil.roundHalfUp(lym));
        int mon = Integer.parseInt(res.substring(13, 16), 16);
        dataBean.setWbc_mon(MatchUtil.roundHalfUp(mon));
        int neu = Integer.parseInt(res.substring(17, 20), 16);
        dataBean.setWbc_neu(MatchUtil.roundHalfUp(neu));
        int eos = Integer.parseInt(res.substring(21, 24), 16);
        dataBean.setWbc_eos(MatchUtil.roundHalfUp(eos));
        int bas = Integer.parseInt(res.substring(25, 28), 16);
        dataBean.setWbc_bas(MatchUtil.roundHalfUp(bas));

        Logger.d("zrl", "analysisWBC: " + wbc + "mon " + mon + "neu " + neu + "eos " + eos + "bas " + bas);
    }


    //测试版 数据构造
    public void debugDataTest(Measure param, MeasureBean dataBean,
                              List<MeasureItem> dataList, MeasureListAdapter measureAdapter) {
        //血压
        if (param == Measure.NIBP && ITEM_NIBP != -1) {
            switch (RandomUtil.nextInt(1, 2)) {
                case 1:
                    dataBean.setSbp(RandomUtil.nextInt(89, 91));
                    dataBean.setDbp(RandomUtil.nextInt(59, 61));
                    dataBean.setMbp(RandomUtil.nextInt(90, 110));
                    dataBean.setNibPr(RandomUtil.nextInt(58, 62));
                    break;
                case 2:
                    dataBean.setSbp(RandomUtil.nextInt(139, 140));
                    dataBean.setDbp(RandomUtil.nextInt(88, 90));
                    dataBean.setMbp(RandomUtil.nextInt(90, 110));
                    dataBean.setNibPr(RandomUtil.nextInt(99, 101));
                    break;
            }
            MeasureItem item = dataList.get(ITEM_NIBP);
            item.setState(MeasureState.MEASURE_READY);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_NIBP);
            if (ITEM_PR != -1) {
                MeasureItem itemPr = dataList.get(ITEM_PR);
                itemPr.setState(MeasureState.MEASURE_READY);
                itemPr.setMeasured(true);
                itemPr.setMeasureBean(dataBean);
                measureAdapter.notifyItemChanged(ITEM_PR);
            }
        }
        //血氧
        if (param == Measure.SPO2 && ITEM_SPO2 != -1) {
            switch (RandomUtil.nextInt(1, 2)) {
                case 1:
                    dataBean.setSpo2(RandomUtil.nextInt(93, 95));
                    dataBean.setPr(RandomUtil.nextInt(59, 61));
                    break;
                case 2:
                    dataBean.setSpo2(RandomUtil.nextInt(99, 101));
                    dataBean.setPr(RandomUtil.nextInt(59, 61));
                    break;
            }


            MeasureItem item = dataList.get(ITEM_SPO2);
            item.setState(MeasureState.MEASURE_READY);
            isSpo2 = 0;
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_SPO2);
            if (ITEM_PR != -1) {
                MeasureItem itemPr = dataList.get(ITEM_PR);
                itemPr.setState(MeasureState.MEASURE_READY);
                itemPr.setMeasured(true);
                itemPr.setMeasureBean(dataBean);
                measureAdapter.notifyItemChanged(ITEM_PR);
            }
        }

        //血糖
        if (param == Measure.GLU && ITEM_GLU != -1) {

            switch (RandomUtil.nextInt(1, 4)) {
                case 1:
                    dataBean.setGlu(RandomUtil.nextFloat(1, 3.8f, 4.0f));
                    dataBean.setGluStyle(Configuration.BtnFlag.lift);
                    break;
                case 2:
                    dataBean.setGlu(RandomUtil.nextFloat(1, 6.0f, 6.2f));
                    dataBean.setGluStyle(Configuration.BtnFlag.lift);
                    break;
                case 3:
                    dataBean.setGlu(RandomUtil.nextFloat(1, 7.7f, 8.0f));
                    dataBean.setGluStyle(Configuration.BtnFlag.right);
                    break;
                case 4:
                    dataBean.setGlu(RandomUtil.nextFloat(1, 7.7f, 8.0f));
                    dataBean.setGluStyle(Configuration.BtnFlag.right);
                    break;
            }

            MeasureItem item = dataList.get(ITEM_GLU);
            item.setState(MeasureState.MEASURE_READY);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_GLU);
        }


        //尿酸
        if (param == Measure.UA && ITEM_UA != -1) {

            switch (RandomUtil.nextInt(1, 4)) {
                case 1:
                    dataBean.setUricacid(RandomUtil.nextInt(141, 143));
                    break;
                case 2:
                    dataBean.setUricacid(RandomUtil.nextInt(415, 417));
                    break;
                case 3:
                    dataBean.setUricacid(RandomUtil.nextInt(200, 204));
                    break;
                case 4:
                    dataBean.setUricacid(RandomUtil.nextInt(337, 341));
                    break;
            }

            dataList.get(ITEM_UA).setMeasured(true);
            dataList.get(ITEM_UA).setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_UA);
        }

        //总胆固醇
        if (param == Measure.CHOL && ITEM_CHOL != -1) {

            switch (RandomUtil.nextInt(1, 2)) {
                case 1:
                    dataBean.setXzzdgc(RandomUtil.nextFloat(1, 3.1f, 3.3f));
                    break;
                case 2:
                    dataBean.setXzzdgc(RandomUtil.nextFloat(1, 5.0f, 5.2f));
                    break;
            }


            MeasureItem item = dataList.get(ITEM_CHOL);
            item.setState(MeasureState.MEASURE_READY);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);

            measureAdapter.notifyItemChanged(ITEM_CHOL);
        }
        //尿常规
        if (param == Measure.URINE && ITEM_URINE != -1) {

            switch (RandomUtil.nextInt(1, 2)) {
                case 1:
                    dataBean.setUrineAsc("+" + RandomUtil.nextInt(1, 5));
                    dataBean.setUrinePh(RandomUtil.nextFloat(1, 4.3f, 4.7f));
                    dataBean.setUrineUbg("+" + RandomUtil.nextInt(1, 5));
                    dataBean.setUrineBil("-");
                    dataBean.setUrineBld("+" + RandomUtil.nextInt(1, 5));
                    dataBean.setUrineCa("+" + RandomUtil.nextInt(1, 5));
                    dataBean.setUrineCre("-");
                    dataBean.setUrineGlu("+" + RandomUtil.nextInt(1, 5));
                    dataBean.setUrineKet("+" + RandomUtil.nextInt(1, 5));
                    dataBean.setUrineLeu("-");
                    dataBean.setUrineMa("+" + RandomUtil.nextInt(1, 5));
                    dataBean.setUrineNit("-");
                    dataBean.setUrinePro("+" + RandomUtil.nextInt(1, 5));
                    dataBean.setUrineSg(Double.valueOf(String.valueOf(RandomUtil.nextFloat(3, 1.013f, 1.017f))));
                    dataBean.setUrineVc("+" + RandomUtil.nextInt(1, 5));
                    break;

                case 2:
                    dataBean.setUrineAsc("-");
                    dataBean.setUrinePh(RandomUtil.nextFloat(1, 7.6f, 8.1f));
                    dataBean.setUrineUbg("-");
                    dataBean.setUrineBil("+" + RandomUtil.nextInt(1, 5));
                    dataBean.setUrineBld("-");
                    dataBean.setUrineCa("-");
                    dataBean.setUrineCre("+" + RandomUtil.nextInt(1, 5));
                    dataBean.setUrineGlu("-");
                    dataBean.setUrineKet("-");
                    dataBean.setUrineLeu("+" + RandomUtil.nextInt(1, 5));
                    dataBean.setUrineMa("-");
                    dataBean.setUrineNit("+" + RandomUtil.nextInt(1, 5));
                    dataBean.setUrinePro("-");
                    dataBean.setUrineSg(Double.valueOf(String.valueOf(RandomUtil.nextFloat(3, 1.023f, 1.027f))));
                    dataBean.setUrineVc("-");
                    break;
            }


            dataList.get(ITEM_URINE).setMeasured(true);
            dataList.get(ITEM_URINE).setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_URINE);
        }

        //血红蛋白（HB）和压积值
        if ((param.equals(Measure.HB) || param.equals(Measure.HCT)) && ITEM_HB != -1) {

            switch (RandomUtil.nextInt(1, 4)) {
                case 1:
                    dataBean.setAssxhb(RandomUtil.nextInt(119, 111));
                    dataBean.setAssxhct(RandomUtil.nextInt(36, 39));
                    break;
                case 2:
                    dataBean.setAssxhb(RandomUtil.nextInt(158, 162));
                    dataBean.setAssxhct(RandomUtil.nextInt(48, 52));
                    break;
                case 3:
                    dataBean.setAssxhb(RandomUtil.nextInt(108, 112));
                    dataBean.setAssxhct(RandomUtil.nextInt(35, 39));
                    break;
                case 4:
                    dataBean.setAssxhb(RandomUtil.nextInt(148, 152));
                    dataBean.setAssxhct(RandomUtil.nextInt(46, 50));
                    break;
            }


            dataList.get(ITEM_HB).setState(MeasureState.MEASURE_READY);
            dataList.get(ITEM_HB).setMeasured(true);
            dataList.get(ITEM_HB).setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_HB);
        }

        //体温
        if (param == Measure.TEMP && ITEM_TEMP != -1) {

            switch (RandomUtil.nextInt(1, 2)) {
                case 1:
                    dataBean.setTemp(RandomUtil.nextFloat(1, 34.6f, 34.8f));
                    break;
                case 2:
                    dataBean.setTemp(RandomUtil.nextFloat(1, 37.7f, 37.9f));
                    break;
            }

            MeasureItem item = dataList.get(ITEM_TEMP);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_TEMP);
        }

        //身高
        if (param == Measure.HEIGHT && ITEM_HEIGHT != -1) {

            dataBean.setHeight(RandomUtil.nextInt(160, 180) + "");
            dataList.get(ITEM_HEIGHT).setMeasured(true);
            dataList.get(ITEM_HEIGHT).setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_HEIGHT);
        }
        //体重
        if (param == Measure.WEIGHT && ITEM_WEIGHT != -1) {
            dataBean.setWeight(RandomUtil.nextInt(0, 200) + "");
            dataList.get(ITEM_WEIGHT).setMeasured(true);
            dataList.get(ITEM_WEIGHT).setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_WEIGHT);
        }
        //CRP
        if (param.equals(Measure.CRP) && ITEM_CRP != -1) {

            switch (RandomUtil.nextInt(1, 2)) {
                case 1:
                    dataBean.setFia_crp(RandomUtil.nextFloat(1, 0.0f, 2.0f) + "");
                    break;
                case 2:
                    dataBean.setFia_crp(RandomUtil.nextFloat(1, 9.5f, 10.5f) + "");
                    break;
            }

            MeasureItem item = dataList.get(ITEM_CRP);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_CRP);
        }
        //hs-CRP
        if (param.equals(Measure.Hs_CRP) && ITEM_HSCRP != -1) {

            switch (RandomUtil.nextInt(1, 2)) {
                case 1:
                    dataBean.setFia_hscrp(RandomUtil.nextFloat(1, 0.0f, 0.1f) + "");
                    break;
                case 2:
                    dataBean.setFia_hscrp(RandomUtil.nextFloat(1, 2.9f, 3.2f) + "");
                    break;
            }


            MeasureItem item = dataList.get(ITEM_HSCRP);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_HSCRP);
        }
        //SAA
        if (param == Measure.SAA && ITEM_SAA != -1) {

            switch (RandomUtil.nextInt(1, 2)) {
                case 1:
                    dataBean.setFia_saa(RandomUtil.nextFloat(1, 0.0f, 0.2f) + "");
                    break;
                case 2:
                    dataBean.setFia_saa(RandomUtil.nextFloat(1, 9.8f, 10.2f) + "");
                    break;
            }

            MeasureItem item = dataList.get(ITEM_SAA);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_SAA);
        }
        //PCT
        if (param.equals(Measure.PCT) && ITEM_PCT != -1) {

            dataBean.setFia_pct(String.valueOf(RandomUtil.nextFloat(1, 0.0f, 0.1f)));

            MeasureItem item = dataList.get(ITEM_PCT);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_PCT);
        }
        //WBC
        if (param.equals(Measure.WBC) && ITEM_WBC != -1) {
            switch (RandomUtil.nextInt(1, 2)) {
                case 1:
                    dataBean.setWbc(RandomUtil.nextFloat(1, 3.3f, 3.7f));
                    dataBean.setWbc_lym(RandomUtil.nextFloat(1, 0.9f, 1.2f));
                    dataBean.setWbc_mon(RandomUtil.nextFloat(1, 0.0f, 0.2f));
                    dataBean.setWbc_neu(RandomUtil.nextFloat(1, 1.6f, 2.0f));
                    dataBean.setWbc_eos(RandomUtil.nextFloat(2, 0.0f, 0.1f));
                    dataBean.setWbc_bas(RandomUtil.nextFloat(2, 0.0f, 0.1f));
                    break;
                case 2:
                    dataBean.setWbc(RandomUtil.nextFloat(1, 9.3f, 9.7f));
                    dataBean.setWbc_lym(RandomUtil.nextFloat(1, 3.0f, 3.4f));
                    dataBean.setWbc_mon(RandomUtil.nextFloat(1, 0.4f, 0.8f));
                    dataBean.setWbc_neu(RandomUtil.nextFloat(1, 6.1f, 6.5f));
                    dataBean.setWbc_eos(RandomUtil.nextFloat(2, 0.3f, 0.7f));
                    dataBean.setWbc_bas(RandomUtil.nextFloat(2, 0.0f, 0.1f));
                    break;
            }
            MeasureItem item = dataList.get(ITEM_WBC);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_WBC);
        }

        //糖化血红蛋白
        if (param.equals(Measure.GHB) && ITEM_GHB != -1) {

            dataBean.setNgsp(String.valueOf(RandomUtil.nextFloat(1, 3.0f, 7.0f)));
            dataBean.setIfcc(String.valueOf(RandomUtil.nextFloat(1, 30f, 40f)));
            dataBean.setEag(String.valueOf(RandomUtil.nextFloat(1, 4.0f, 7.0f)));

            MeasureItem item = dataList.get(ITEM_GHB);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_GHB);
        }
        //血脂四项
        if (param.equals(Measure.BLOOD) && ITEM_BLOOD != -1) {


            switch (RandomUtil.nextInt(1, 2)) {
                case 1:
                    dataBean.setBlood_hdl(RandomUtil.nextFloat(1, 0.8f, 1.2f) + "");
                    dataBean.setBlood_ldl(RandomUtil.nextFloat(1, 0.0f, 0.2f) + "");
                    dataBean.setBlood_tc(RandomUtil.nextFloat(1, 3.0f, 3.2f) + "");
                    dataBean.setBlood_tg(RandomUtil.nextFloat(1, 0.2f, 0.4f) + "");
                    break;
                case 2:
                    dataBean.setBlood_hdl(RandomUtil.nextFloat(1, 1.6f, 2.2f) + "");
                    dataBean.setBlood_ldl(RandomUtil.nextFloat(1, 2.9f, 3.3f) + "");
                    dataBean.setBlood_tc(RandomUtil.nextFloat(1, 5.0f, 5.3f) + "");
                    dataBean.setBlood_tg(RandomUtil.nextFloat(1, 1.5f, 1.9f) + "");
                    break;
            }


            MeasureItem item = dataList.get(ITEM_BLOOD);
            item.setState(MeasureState.MEASURE_READY);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_BLOOD);
        }

        //大树血脂
        if (param.equals(Measure.DS100A) && ITEM_DS100A != -1) {
//            String data = "AA5AA5011392E301120A12112925060F01830600430E02090B020B1100EE1200933DDDEE";
//            analysisData(data, dataBean);


            switch (RandomUtil.nextInt(1, 2)) {
                case 1:
                    dataBean.setBlood_hdl(RandomUtil.nextFloat(1, 0.8f, 1.2f) + "");
                    dataBean.setBlood_ldl(RandomUtil.nextFloat(1, 0.0f, 0.2f) + "");
                    dataBean.setBlood_tc(RandomUtil.nextFloat(2, 3.0f, 3.2f) + "");
                    dataBean.setBlood_tg(RandomUtil.nextFloat(2, 0.2f, 0.4f) + "");

                    dataBean.setGluTree(RandomUtil.nextFloat(1, 3.7f, 4.1f) + "");
                    dataBean.setBlood_vldl(RandomUtil.nextFloat(2, 0.10f, 0.30f) + "");
                    dataBean.setBlood_ai(RandomUtil.nextFloat(1, 0.0f, 0.1f) + "");
                    dataBean.setBlood_r_chd(RandomUtil.nextFloat(1, 0.0f, 0.1f) + "");
                    break;
                case 2:
                    dataBean.setBlood_hdl(RandomUtil.nextFloat(1, 1.6f, 2.2f) + "");
                    dataBean.setBlood_ldl(RandomUtil.nextFloat(1, 2.9f, 3.3f) + "");
                    dataBean.setBlood_tc(RandomUtil.nextFloat(2, 5.0f, 5.3f) + "");
                    dataBean.setBlood_tg(RandomUtil.nextFloat(2, 1.5f, 1.9f) + "");

                    dataBean.setGluTree(RandomUtil.nextFloat(1, 5.9f, 6.2f) + "");
                    dataBean.setBlood_vldl(RandomUtil.nextFloat(2, 0.70f, 0.90f) + "");
                    dataBean.setBlood_ai(RandomUtil.nextFloat(1, 3.9f, 4.2f) + "");
                    dataBean.setBlood_r_chd(RandomUtil.nextFloat(1, 4.4f, 4.6f) + "");
                    break;
            }

            dataList.get(ITEM_DS100A).setMeasured(true);
            dataList.get(ITEM_DS100A).setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_DS100A);
        }

        //心肌三项
        if (param.equals(Measure.MYO) && ITEM_MYO != -1) {

            switch (RandomUtil.nextInt(1, 2)) {
                case 1:
                    dataBean.setFia_ckmb(RandomUtil.nextFloat(1, 0, 1) + "");
                    dataBean.setFia_ctnl(RandomUtil.nextFloat(1, 0, 1) + "");
                    dataBean.setFia_myo(RandomUtil.nextFloat(1, 0, 1) + "");
                    break;
                case 2:
                    dataBean.setFia_ckmb(RandomUtil.nextFloat(1, 4, 6) + "");
                    dataBean.setFia_ctnl(RandomUtil.nextFloat(1, 0, 1) + "");
                    dataBean.setFia_myo(RandomUtil.nextFloat(1, 0, 1) + "");
                    break;
            }

            MeasureItem item = dataList.get(ITEM_MYO);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(ITEM_MYO);
        }
    }
}
