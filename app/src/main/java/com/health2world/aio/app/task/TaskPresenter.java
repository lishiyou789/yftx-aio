package com.health2world.aio.app.task;

import com.google.gson.Gson;
import com.health2world.aio.bean.TaskInfo;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.konsung.bean.ResidentBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.http.HttpResult;

/**
 * Created by Administrator on 2018/7/6 0006.
 */

public class TaskPresenter extends TaskContract.Presenter {

    public TaskPresenter(TaskContract.View mView) {
        super(mView);
    }

    @Override
    void loadTaskData(String taskType, int pageIndex) {
        ApiRequest.getTaskList(taskType, pageIndex, new HttpSubscriber() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (mView != null)
                    mView.loadFailed();
            }

            @Override
            public void onNext(HttpResult result) {
                super.onNext(result);
                if (result.code.equals(AppConfig.SUCCESS)) {
                    try {
                        JSONArray array = new JSONArray(new Gson().toJson(result.data));
                        List<TaskInfo> list = new ArrayList();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.optJSONObject(i);
                            TaskInfo bean = TaskInfo.parseBean(o);
                            list.add(bean);
                        }
                        if (mView != null)
                            mView.loadSuccess(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (mView != null)
                            mView.loadSuccess(new ArrayList<TaskInfo>());
                    }
                }else {
                    if (mView != null)
                        mView.loadFailed();
                }
            }
        });
    }


    @Override
    void getPatientInfoById(String patientId) {
        ApiRequest.getPatientInfoById(patientId, new HttpSubscriber() {
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
                if (mView != null)
                    mView.hideLoading();
                super.onNext(result);
                if (result.code.equals(AppConfig.SUCCESS)) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(result.data));
                        ResidentBean resident = ResidentBean.parse(object);
                        if (mView != null)
                            mView.loadPatientInfoSuccess(resident);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    void donePublicHealthTask(String patientId) {
        ApiRequest.completePhInfo(patientId, new HttpSubscriber() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (mView != null)
                    mView.donePublicHealthStatus(false);
            }

            @Override
            public void onNext(HttpResult result) {
                super.onNext(result);
                if (result.code.equals(AppConfig.SUCCESS)) {
                    if (mView != null)
                        mView.donePublicHealthStatus(true);
                } else {
                    if (mView != null)
                        mView.donePublicHealthStatus(false);
                }

            }
        });
    }
}
