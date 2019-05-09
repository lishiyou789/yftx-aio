package com.health2world.aio.app.health;

import com.google.gson.Gson;
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
 * Created by lishiyou on 2018/8/6 0006.
 */

public class HealthManagerPresenter extends HealthManagerContract.Presenter {

    public HealthManagerPresenter(HealthManagerContract.View mView) {
        super(mView);
    }

    @Override
    void loadFamilyMember(String patientId) {
        ApiRequest.getFamilyMember(patientId)
                .subscribe(new HttpSubscriber() {
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
                                String data = new Gson().toJson(result.data);
                                List<ResidentBean> list = new ArrayList<>();
                                JSONArray array = new JSONArray(data);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject o = array.getJSONObject(i);
                                    ResidentBean bean = ResidentBean.parse(o);
                                    list.add(bean);
                                }
                                if (mView != null) {
                                    mView.loadFamilyMemberSuccess(list);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

//    @Override
//    public void loadPatientSignDetail(String patientId) {
//        ApiRequest.getPatientSignDetail(patientId, new ResultSubscriber<List<SignService>>() {
//            @Override
//            public void onStart() {
//                mView.showLoading();
//            }
//
//            @Override
//            public void onCompleted() {
//                mView.hideLoading();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                mView.hideLoading();
//            }
//
//            @Override
//            public void onNext(HttpResult<List<SignService>> t) {
//                super.onNext(t);
//                if (t.code.equals(AppConfig.SUCCESS)) {
//                    mView.loadSignServiceSuccess(t.data);
//                } else {
//                    ToastUtil.showShort(t.errorMessage);
//                }
//            }
//        });
//    }
}
