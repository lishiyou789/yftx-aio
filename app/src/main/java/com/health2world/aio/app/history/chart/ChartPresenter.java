package com.health2world.aio.app.history.chart;

import com.google.gson.Gson;
import com.health2world.aio.bean.HistoryData;
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
 * Created by lishiyou on 2018/9/4 0004.
 */

public class ChartPresenter extends ChartContract.Presenter {


    public ChartPresenter(ChartContract.View mView) {
        super(mView);
    }


    @Override
    void loadChartData(String patientId, final String dataType, String startTime, String endTime) {
        ApiRequest.historyData(patientId, dataType, 1, 100, startTime, endTime)
                .subscribe(new HttpSubscriber() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mView != null)
                            mView.loadChartDataError();
                    }

                    @Override
                    public void onNext(HttpResult result) {
                        super.onNext(result);
                        if (result.code.equals(AppConfig.SUCCESS)) {
                            try {
                                JSONObject object = new JSONObject(new Gson().toJson(result.data));
                                JSONArray array = object.optJSONArray("records");
                                List<HistoryData> list = new ArrayList<>();
                                if (array != null) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.optJSONObject(i);
                                        HistoryData data = HistoryData.parseBean(obj);
                                        list.add(data);
                                    }
                                }
                                if (mView != null)
                                    mView.loadChartDataSuccess(list);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (mView != null)
                                    mView.loadChartDataError();
                            }
                        } else {
                            if (mView != null)
                                mView.loadChartDataError();
                        }
                    }
                });
    }
}
