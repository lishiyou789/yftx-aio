package com.health2world.aio.app.history.data;

import com.health2world.aio.config.AppConfig;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.sql.SQLException;
import java.util.List;

import aio.health2world.http.HttpResult;

/**
 * Created by lishiyou on 2019/2/19 0019.
 */

public class NotUploadPresenter extends NotUploadContract.Presenter {

    public NotUploadPresenter(NotUploadContract.View mView) {
        super(mView);
    }

    @Override
    void uploadMeasureData(final int position, MeasureBean measure) {
        List<ResidentBean> list = null;
        int sex = 1;
        try {
            list = DBManager.getInstance().getResidentDao().queryBuilder().where().eq("patientId", measure.getPatientId()).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list != null && list.size() > 0) {
            sex = list.get(0).getSexy();
        }
        ApiRequest.uploadMedicalData("", measure.getPatientId(), sex, measure, new HttpSubscriber() {
            @Override
            public void onStart() {
                super.onStart();
                if (mView != null)
                    mView.showLoading();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (mView != null)
                    mView.hideLoading();
            }

            @Override
            public void onNext(HttpResult result) {
                super.onNext(result);
                if (mView != null)
                    mView.hideLoading();
                if (result.code.equals(AppConfig.SUCCESS)) {
                    if (mView != null)
                        mView.uploadSuccess(position);
                }
            }
        });
    }
}
