package com.health2world.aio.app.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.health2world.aio.app.history.chart.EcgChartFragment;
import com.health2world.aio.app.history.chart.GluChartFragment;
import com.health2world.aio.app.history.chart.NibpChartFragment;
import com.health2world.aio.app.history.chart.Spo2ChartFragment;
import com.health2world.aio.app.history.chart.TempChartFragment;
import com.konsung.bean.ResidentBean;

/**
 * Created by lishiyou on 2018/8/2 0002.
 */

public class HistoryChartAdapter extends FragmentPagerAdapter {

    private ResidentBean resident;

    private NibpChartFragment nibpChartFragment;
    private GluChartFragment gluChartFragment;
    private Spo2ChartFragment spo2ChartFragment;
    private EcgChartFragment ecgChartFragment;
    private TempChartFragment tempChartFragment;

    private int index = 0;

    public int getIndex() {
        return index;
    }

    public HistoryChartAdapter(ResidentBean resident, FragmentManager fm) {
        super(fm);
        this.resident = resident;
    }

    @Override
    public int getCount() {
        return HistoryRecordActivity.mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return HistoryRecordActivity.mTitles[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            //懒加载
            case 0:
                index = position;
                if (nibpChartFragment == null) {
                    nibpChartFragment = new NibpChartFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resident", resident);
                    nibpChartFragment.setArguments(bundle);
                } else {
                    if (!nibpChartFragment.isSuccess())
                        nibpChartFragment.loadData();
                }
                return nibpChartFragment;
            case 1:
                index = position;
                if (gluChartFragment == null) {
                    gluChartFragment = new GluChartFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resident", resident);
                    gluChartFragment.setArguments(bundle);
                } else {
                    if (!gluChartFragment.isSuccess())
                        gluChartFragment.loadData();
                }
                return gluChartFragment;
            case 2:
                index = position;
                if (spo2ChartFragment == null) {
                    spo2ChartFragment = new Spo2ChartFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resident", resident);
                    spo2ChartFragment.setArguments(bundle);
                } else {
                    if (!spo2ChartFragment.isSuccess())
                        spo2ChartFragment.loadData();
                }
                return spo2ChartFragment;
            case 3:
                index = position;
                if (tempChartFragment == null) {
                    tempChartFragment = new TempChartFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resident", resident);
                    tempChartFragment.setArguments(bundle);
                } else {
                    if (!tempChartFragment.isSuccess())
                        tempChartFragment.loadData();
                }
                return tempChartFragment;

            case 4:
                if (ecgChartFragment == null) {
                    ecgChartFragment = new EcgChartFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resident", resident);
                    ecgChartFragment.setArguments(bundle);
                }
                return ecgChartFragment;
        }
        return null;
    }

    public void changeView(int viewType) {
        if (nibpChartFragment != null)
            nibpChartFragment.changeView(viewType);
        if (gluChartFragment != null)
            gluChartFragment.changeView(viewType);
        if (spo2ChartFragment != null)
            spo2ChartFragment.changeView(viewType);
        if (tempChartFragment != null)
            tempChartFragment.changeView(viewType);
    }
}
