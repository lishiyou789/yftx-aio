package com.konsung.presenter.impl;

import android.app.FragmentManager;
import android.content.Context;
import android.widget.BaseAdapter;

import com.konsung.R;
import com.konsung.adapter.HealthCheckSelectedAdapter;
import com.konsung.bean.AppBean;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.StateBean;
import com.konsung.fragment.BloodGluFragment;
import com.konsung.fragment.Ecg12Fragment;
import com.konsung.fragment.FhrFragment;
import com.konsung.fragment.NibpFragment;
import com.konsung.fragment.Spo2Fragment;
import com.konsung.fragment.TempFragment;
import com.konsung.fragment.UrineFourttenFragment;
import com.konsung.listen.Measure;
import com.konsung.listen.MeasureCompleteListen;
import com.konsung.presenter.AppPresenter;
import com.konsung.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 健康测量界面逻辑处理健康测量界面
 */
public class AppPresenterImpl extends BasePresenter implements AppPresenter.Presenter {

    private HealthCheckSelectedAdapter adapter; //当前adapter显示的内容
    private List<AppBean> list; //记录显示的数据
    private MeasureBean measureBean; //记录当前操作的测量类
    private MeasureCompleteListen l; //记录测量完成的监听类

    @Override
    public List<AppBean> getItemBean(Context context) {
        list = new ArrayList<>();
        String[] measureNames = context.getResources().getStringArray(R.array.appMeasureNames);
        //心电
        if (measureNames.length > 0) {
            AppBean bean = new AppBean();
            bean.setName(measureNames[0]);
            bean.setPicture(R.drawable.ico_ecg);
            bean.setPictureSel(R.drawable.ico_ecg_sel);
            list.add(bean);
        }
        //血氧
        if (measureNames.length > 1) {
            AppBean bean = new AppBean();
            bean.setName(measureNames[1]);
            bean.setPicture(R.drawable.ico_spo2);
            bean.setPictureSel(R.drawable.ico_spo2_sel);
            list.add(bean);
        }
        //血压
        if (measureNames.length > 2) {
            AppBean bean = new AppBean();
            bean.setName(measureNames[2]);
            bean.setPicture(R.drawable.ico_nibp);
            bean.setPictureSel(R.drawable.ico_nibp_sel);
            list.add(bean);
        }
        //体温
        if (measureNames.length > 3) {
            AppBean bean = new AppBean();
            bean.setName(measureNames[3]);
            bean.setPicture(R.drawable.ico_temp);
            bean.setPictureSel(R.drawable.ico_temp_sel);
            list.add(bean);
        }
        //血液三项
        if (measureNames.length > 4) {
            AppBean bean = new AppBean();
            bean.setName(measureNames[4]);
            bean.setPicture(R.drawable.ico_glu);
            bean.setPictureSel(R.drawable.ico_glu_sel);
            list.add(bean);
        }
        //尿常规
        if (measureNames.length > 5) {
            AppBean bean = new AppBean();
            bean.setName(measureNames[5]);
            bean.setPicture(R.drawable.ico_urien);
            bean.setPictureSel(R.drawable.ico_urien_sel);
            list.add(bean);
        }
        //尿常规
        if (measureNames.length > 6) {
            AppBean bean = new AppBean();
            bean.setName(measureNames[6]);
            bean.setPicture(R.drawable.ico_urien);
            bean.setPictureSel(R.drawable.ico_urien_sel);
            list.add(bean);
        }
        return list;
    }

    @Override
    public BaseAdapter getItemAdapter(Context context) {
        adapter = new HealthCheckSelectedAdapter(context,
                getItemBean(context));
        return adapter;
    }

    @Override
    public void clickItem(int position, FragmentManager fragmentTransaction) {
        if (null != list) {
            for (int i = 0; i < list.size(); i++) {
                AppBean appBean = list.get(i);
                if (i == position) {
                    appBean.setClick(true);
                } else {
                    appBean.setClick(false);
                }
                list.set(i, appBean);
            }
        }
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }
        if (position == 0) {
//            Ecg12Fragment fragment = new Ecg12Fragment(measureBean);
            Ecg12Fragment fragment = Ecg12Fragment.getInstance(measureBean);
            fragmentTransaction.beginTransaction().replace(R.id.app_content, fragment).commit();
            fragment.setMeasureListen(listen);
        } else if (position == 1) {
            Spo2Fragment fragment = Spo2Fragment.getInstance(measureBean);
            fragmentTransaction.beginTransaction().replace(R.id.app_content, fragment).commit();
            fragment.setMeasureListen(listen);
        } else if (position == 2) {
            NibpFragment fragment = NibpFragment.getInstance(measureBean);
            fragmentTransaction.beginTransaction().replace(R.id.app_content, fragment).commit();
            fragment.setMeasureListen(listen);
        } else if (position == 3) {
            TempFragment fragment = TempFragment.getInstance(measureBean);
            fragmentTransaction.beginTransaction().replace(R.id.app_content, fragment).commit();
            fragment.setMeasureListen(listen);
        } else if (position == 4) {
            BloodGluFragment fragment = BloodGluFragment.getInstance(measureBean);
            fragmentTransaction.beginTransaction().replace(R.id.app_content, fragment).commit();
            fragment.setMeasureListen(listen);
        } else if (position == 5) {
            UrineFourttenFragment fragment = UrineFourttenFragment.getInstance(measureBean);
            fragmentTransaction.beginTransaction().replace(R.id.app_content, fragment).commit();
            fragment.setMeasureListen(listen);
        } else if (position == 6) {
            FhrFragment fragment = FhrFragment.getInstance(measureBean);
            fragmentTransaction.beginTransaction().replace(R.id.app_content, fragment).commit();
            fragment.setMeasureListen(listen);
        }
    }

    @Override
    public void setMeasure(MeasureBean bean) {
        this.measureBean = bean;
    }

    @Override
    public void setMeasureListen(MeasureCompleteListen l) {
        this.l = l;
    }

    /**
     * 监听的回调类
     */
    private MeasureCompleteListen listen = new MeasureCompleteListen() {
        @Override
        public void onComplete(Measure param, MeasureBean bean) {
            AppPresenterImpl.this.measureBean = bean;
            if (null != l) {
                l.onComplete(param, bean);
            }
        }

        @Override
        public void onFail(Measure param, String mag) {

        }

        @Override
        public void NibpCuff(int va) {

        }


        @Override
        public void onState(StateBean bean) {

        }
    };
}
