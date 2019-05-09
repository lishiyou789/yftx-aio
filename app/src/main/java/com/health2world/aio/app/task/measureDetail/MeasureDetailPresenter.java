package com.health2world.aio.app.task.measureDetail;

import com.google.gson.Gson;
import com.health2world.aio.bean.IndexData;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.j256.ormlite.stmt.query.In;
import com.konsung.bean.ResidentBean;

import org.json.JSONException;
import org.json.JSONObject;

import aio.health2world.http.HttpResult;

/**
 * @author Administrator
 * @date 2019/1/5/0005.
 */

public class MeasureDetailPresenter extends MeasureDetailContract.Presenter {

    public MeasureDetailPresenter(MeasureDetailContract.View mView) {
        super(mView);
    }

    @Override
    void getMeasureDetail(final ResidentBean resident, String data,String checkName, String measurementProject, String gluStyle) {
        ApiRequest.interpret(resident, data, checkName,measurementProject, gluStyle, new HttpSubscriber() {

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
                        IndexData data1 = IndexData.parseData(object);
                        if (mView != null) {
                            mView.loadSuccess(data1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
