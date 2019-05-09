package com.health2world.aio.app.health.sign;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.bean.SignService;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.health2world.aio.http.ResultSubscriber;
import com.health2world.aio.provider.PublicHealthDictionary;
import com.konsung.bean.ResidentBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.DateUtil;
import aio.health2world.utils.ToastUtil;

/**
 * Created by lishiyou on 2018/8/8 0008.
 */

public class SignPresenter extends SignContract.Presenter {

    public SignPresenter(SignContract.View mView) {
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
                        if (mView != null)
                            mView.loadFamilyMemberError(e);
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
                                if (mView != null)
                                    mView.loadFamilyMemberSuccess(list);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (mView != null)
                                mView.loadFamilyMemberError(new Throwable(result.errorMessage));
                        }
                    }
                });
    }

    @Override
    void loadServicePackage() {
        ApiRequest
                .getServicePackageList(1)
                .subscribe(new ResultSubscriber<List<SignService>>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mView != null)
                            mView.loadServicePackageError(e);
                    }

                    @Override
                    public void onNext(HttpResult<List<SignService>> t) {
                        super.onNext(t);
                        if (mView != null)
                            mView.loadServicePackageSuccess(t.data);
                    }
                });
    }

    @Override
    void queryHealthFile(String identityCard, final SignServiceSection section, final int position) {
        ApiRequest.queryHealthFile(identityCard, new HttpSubscriber() {

            @Override
            public void onStart() {
                super.onStart();
                if (mView != null)
                    mView.showLoading("正在查询公卫档案");
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

                ResidentBean resident = new ResidentBean();
                String data = new Gson().toJson(result.data);
                if (result.code.equals(AppConfig.SUCCESS) && !TextUtils.isEmpty(data)) {
                    try {
                        JSONObject obj = new JSONObject(data);
                        resident.setHealthFileNo(obj.optString("healthfileNo"));
                        resident.setTelPhone(obj.optString("telephoneNumber"));
                        resident.setLocalAddress(obj.optString("villageOfResidence"));
                        resident.setSexy(PublicHealthDictionary.matchSexy(obj.optString("gender")));
                        String birthDate = obj.optString("birthDate");
                        if (!TextUtils.isEmpty(birthDate)) {
                            resident.setBirthday(DateUtil.getBirthDate(birthDate));
                        }
                        String age = obj.optString("age");
                        if (!TextUtils.isEmpty(age)) {
                            resident.setAge(Integer.valueOf(age));
                        }
                        String allergicHistoryName = obj.optString("allergicHistoryName");
                        if (!TextUtils.isEmpty(allergicHistoryName)) {
                            String allergy = "";
                            String[] strings = allergicHistoryName.split(",");
                            for (String s : strings) {
                                allergy += PublicHealthDictionary.matchAllergic(s) + " ";
                            }
                            resident.setAllergy(allergy);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    resident.setHealthFileNo("");
                if (mView != null)
                    mView.queryHealthFileSuccess(resident, section, position);
            }
        });
    }

    @Override
    void updatePatientIdentityCard(String firstPatientId, String patientId, final String identityCard) {
        ApiRequest.updatePatientIdentityCard(firstPatientId, patientId, identityCard, new ResultSubscriber<String>() {

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
            public void onNext(HttpResult<String> t) {
                super.onNext(t);
                if (mView != null)
                    mView.hideLoading();
                if (t.code.equals(AppConfig.SUCCESS)) {
                    ToastUtil.showShort(MyApplication.getInstance().getString(R.string.update_success));
                    if (mView != null)
                        mView.updateIdentityCardSuccess(identityCard);
                }
            }
        });
    }
}
