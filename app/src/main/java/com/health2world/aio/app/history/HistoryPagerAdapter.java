package com.health2world.aio.app.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.health2world.aio.app.history.data.HistoryDataFragment;
import com.health2world.aio.app.history.report.HistoryReportFragment;
import com.health2world.aio.app.history.service.HistoryServiceFragment;
import com.konsung.bean.ResidentBean;

/**
 * Created by lishiyou on 2018/8/2 0002.
 */

public class HistoryPagerAdapter extends FragmentPagerAdapter {

    //历史数据
    private HistoryDataFragment dataFragment;
    //健康报告
    private HistoryReportFragment reportFragment;
    //服务记录
    private HistoryServiceFragment serviceFragment;

    private ResidentBean resident;

    public HistoryPagerAdapter(ResidentBean resident, FragmentManager fm) {
        super(fm);
        this.resident = resident;
    }

    @Override
    public int getCount() {
        return HistoryRecordActivity.TITLES.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (dataFragment == null) {
                    dataFragment = new HistoryDataFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resident", resident);
                    dataFragment.setArguments(bundle);
                }
                return dataFragment;
            case 1:
                if (reportFragment == null) {
                    reportFragment = new HistoryReportFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resident", resident);
                    reportFragment.setArguments(bundle);
                }
                return reportFragment;
            case 2:
                if (serviceFragment == null) {
                    serviceFragment = new HistoryServiceFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resident", resident);
                    serviceFragment.setArguments(bundle);
                }
                return serviceFragment;
        }
        return null;
    }

    public void refreshFragment(String startTime, String endTime) {
        if (dataFragment != null)
            dataFragment.refresh(startTime, endTime);
        if (reportFragment != null)
            reportFragment.refresh(startTime, endTime);
        if (serviceFragment != null)
            serviceFragment.refresh(startTime, endTime);
    }

    public void resetTime(){
        if (dataFragment != null)
            dataFragment.resetTime();
        if (reportFragment != null)
            reportFragment.resetTime();
        if (serviceFragment != null)
            serviceFragment.resetTime();
    }
}
