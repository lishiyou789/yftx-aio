package com.health2world.aio.app.resident;

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
 * Created by Administrator on 2018/7/18 0018.
 */

public class FamilyMemberMainPresenter extends FamilyMemberMainContract.Presenter {
    public FamilyMemberMainPresenter(FamilyMemberMainContract.View mView) {
        super(mView);
    }

    @Override
    public void loadFamilyMember(final ResidentBean residentBean) {
        ApiRequest.getFamilyMember(residentBean.getPatientId())
                .subscribe(new HttpSubscriber() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mView != null)
                            mView.loadFamilyMemberError();
                    }

                    @Override
                    public void onNext(HttpResult result) {
                        super.onNext(result);
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
                        } else {
                            if (mView != null)
                                mView.loadFamilyMemberError();
                        }
                    }
                });
    }


    @Override
    void relevancyPatientInfo(String patientId, String relevancyPatientId, final String relation) {
        ApiRequest.relevancyPatientInfo(patientId, relevancyPatientId, relation, new HttpSubscriber() {
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
                if (result.code.equals(AppConfig.SUCCESS) || result.code.equals(AppConfig.SUCCESS_AIO)) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(result.data));
                        int familyId = object.optInt("familyId");
                        if (mView != null)
                            mView.relevancyPatientInfoSuccess(familyId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
