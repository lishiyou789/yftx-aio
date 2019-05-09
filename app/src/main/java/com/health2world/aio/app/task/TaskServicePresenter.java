package com.health2world.aio.app.task;

import com.google.gson.Gson;
import com.health2world.aio.bean.HistoryData;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.bean.TaskDetail;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import aio.health2world.http.HttpResult;

/**
 * Created by lishiyou on 2018/7/26 0026.
 */

public class TaskServicePresenter extends TaskServiceContract.Presenter {

    public TaskServicePresenter(TaskServiceContract.View mView) {
        super(mView);
    }

    @Override
    void getTaskDetail(String taskId) {
        ApiRequest.getTaskDetail(taskId, new HttpSubscriber() {
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
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(result.data));
                        TaskDetail detail = TaskDetail.pareBean(object);
                        if (mView != null)
                            mView.loadTaskDetailSuccess(detail);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    void taskExecute(TaskDetail task, int serviceStatus, String feedBack, String advise) {
        String batchNumber = "";
        List<HistoryData> list = task.getRecords();
        if (list.size() > 0) {
            HistoryData history = list.get(0);
            if (history.getCheckDataOuts().size() > 0) {
                batchNumber = history.getCheckDataOuts().get(0).getBatchNumber();
            }
        }
        ApiRequest.taskExecute(task.getTaskId(), batchNumber, task.getPatientId(), feedBack,
                advise, serviceStatus, new HttpSubscriber() {
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
                                mView.executeTaskSuccess();
                        }
                    }
                });
    }
}
