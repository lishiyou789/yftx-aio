package com.health2world.aio.app.history.service;

import com.google.gson.Gson;
import com.health2world.aio.bean.ServiceLog;
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

public class HistoryServicePresenter extends HistoryServiceContract.Presenter {

    public HistoryServicePresenter(HistoryServiceContract.View mView) {
        super(mView);
    }

    @Override
    void loadPerformanceRecord(int pageNo, String patientId,String startTime,String endTime) {
        ApiRequest
                .getPerformanceRecord(patientId, pageNo,startTime,endTime)
                .subscribe(new HttpSubscriber() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mView != null)
                            mView.loadServiceRecordError();
                    }
                    @Override
                    public void onNext(HttpResult result) {
                        super.onNext(result);
                        if (result.code.equals(AppConfig.SUCCESS)) {
                            String data = new Gson().toJson(result.data);
                            try {
                                JSONObject obj = new JSONObject(data);
                                JSONArray array = obj.optJSONArray("records");
                                List<ServiceLog> list = new ArrayList<>();
                                if (array != null) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.optJSONObject(i);
                                        ServiceLog record = ServiceLog.parseBean(object);
                                        list.add(record);
                                    }
                                }
                                if (mView != null)
                                    mView.loadPerformanceRecordSuccess(list);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (mView != null)
                                mView.loadServiceRecordError();
                        }
                    }
                });
    }
}
