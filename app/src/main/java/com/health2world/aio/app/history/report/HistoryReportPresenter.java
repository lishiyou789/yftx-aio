package com.health2world.aio.app.history.report;

import com.google.gson.Gson;
import com.health2world.aio.bean.HealthReport;
import com.health2world.aio.bean.MedicalReport;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.http.HttpResult;

/**
 * Created by lishiyou on 2018/8/11 0011.
 */

public class HistoryReportPresenter extends HistoryReportContract.Presenter {

    public HistoryReportPresenter(HistoryReportContract.View mView) {
        super(mView);
    }

    @Override
    void loadHealthReport(String patientId, String startTime, String endTime, int pageIndex) {
        ApiRequest.getHealthReport(patientId, startTime, endTime, pageIndex)
                .subscribe(new HttpSubscriber() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mView != null)
                            mView.loadHealthReportError(e);
                    }

                    @Override
                    public void onNext(HttpResult result) {
                        super.onNext(result);
                        if (result.code.equals(AppConfig.SUCCESS)) {
                            try {
                                JSONObject object = new JSONObject(new Gson().toJson(result.data));
                                JSONArray array = object.optJSONArray("records");
                                List<MedicalReport> reportList = new ArrayList<>();
                                if (array != null && array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.optJSONObject(i);
                                        MedicalReport report = MedicalReport.parseBean(obj);
                                        reportList.add(report);
                                    }
                                }
                                if (mView != null)
                                    mView.loadHealthReportSuccess(reportList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (mView != null)
                                mView.loadHealthReportError(new JSONException("JSONException"));
                        }
                    }
                });
    }
}
