package com.health2world.aio.app.search;

import com.google.gson.Gson;
import com.health2world.aio.bean.TagInfo;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.health2world.aio.http.ResponseSubscriber;
import com.konsung.bean.ResidentBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.ToastUtil;

/**
 * Created by lishiyou on 2018/7/16 0016.
 */

public class RSearchPresenter extends RSearchContract.Presenter {

    public RSearchPresenter(RSearchContract.View mView) {
        super(mView);
    }

    @Override
    void screeningLabel() {
        ApiRequest.screeningLabel(new ResponseSubscriber() {
            @Override
            protected void onSuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    if (code.equals(AppConfig.SUCCESS)) {
                        JSONObject jsonObject = new JSONObject(object.optString("data"));
                        //标签
                        JSONArray array1 = jsonObject.optJSONArray("tagMessages");
                        //服务包
                        JSONArray array2 = jsonObject.optJSONArray("pageMessage");
                        List<TagInfo> list1 = null;//标签集合
                        List<TagInfo> list2 = null;//家医服务包集合
                        List<TagInfo> list3 = null;//个性服务包集合
                        if (array1 != null && array1.length() > 0) {
                            list1 = new ArrayList<>();
                            for (int i = 0; i < array1.length(); i++) {
                                JSONObject o1 = array1.optJSONObject(i);
                                TagInfo bean = new TagInfo();
                                bean.setTagId(o1.optInt("id"));
                                bean.setName(o1.optString("name"));
                                list1.add(bean);
                            }
                        }
                        if (array2 != null && array2.length() > 0) {
                            list2 = new ArrayList<>();
                            list3 = new ArrayList<>();
                            for (int i = 0; i < array2.length(); i++) {
                                TagInfo bean = new TagInfo();
                                JSONObject o2 = array2.optJSONObject(i);
                                String pkgType = o2.optString("pkgType");
                                bean.setTagId(o2.optInt("id"));
                                bean.setName(o2.optString("serviceName"));
                                if (pkgType.equals("1")) {
                                    list2.add(bean);
                                } else {
                                    list3.add(bean);
                                }
                            }
                        }
                        if (mView != null)
                            mView.screeningLabelSuccess(list1, list2, list3);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onFailed(String errorMessage) {
                ToastUtil.showShort(errorMessage);
            }
        });
    }

    @Override
    void residentQuery(int pageIndex, String keyWord, HashMap<String, Object> map) {
        ApiRequest.residentQuery(pageIndex, keyWord, map, new HttpSubscriber() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (mView != null)
                    mView.loadResidentFailed(e);
            }

            @Override
            public void onNext(HttpResult result) {
                super.onNext(result);
                if (result.code.equals(AppConfig.SUCCESS)) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(result.data));
                        JSONArray array = object.optJSONArray("list");
                        List<ResidentBean> list = new ArrayList<>();
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.optJSONObject(i);
                                ResidentBean bean = ResidentBean.parse(o);
                                list.add(bean);
                            }
                        }
                        if (mView != null)
                            mView.loadResidentSuccess(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (mView != null)
                        mView.loadResidentFailed(null);
                }
            }
        });
    }

    @Override
    void doctorAddPatientInfo(final ResidentBean resident) {
        ApiRequest.doctorAddPatientInfo(resident.getPatientId(), new HttpSubscriber() {
            @Override
            public void onStart() {
                if (mView != null)
                    mView.showLoading();
                super.onStart();
            }

            @Override
            public void onError(Throwable e) {
                if (mView != null)
                    mView.hideLoading();
                super.onError(e);
            }

            @Override
            public void onNext(HttpResult result) {
                if (mView != null)
                    mView.hideLoading();
                super.onNext(result);
                if (result.code.equals(AppConfig.SUCCESS)) {
                    ToastUtil.showShort("添加成功");
                    if (mView != null)
                        mView.addInfoSuccess(resident);
                }
            }
        });
    }
}
