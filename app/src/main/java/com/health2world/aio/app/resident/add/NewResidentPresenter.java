package com.health2world.aio.app.resident.add;


import com.google.gson.Gson;
import com.health2world.aio.bean.TagInfo;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.health2world.aio.http.ResponseSubscriber;
import com.konsung.bean.ResidentBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.ToastUtil;

/**
 * Created by Administrator on 2018/7/16 0016.
 */

public class NewResidentPresenter extends NewResidentContract.Presenter {
    private ResidentBean mServerResidentBean;
    private String mResidentCodeUrl;

    public NewResidentPresenter(NewResidentContract.View mView) {
        super(mView);
    }

    @Override
    public void postNewResident(final ResidentBean residentBean) {
        ApiRequest.addPatientInfo(residentBean, new ResponseSubscriber() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            protected void onSuccess(String response) {
                mView.hideLoading();
                handleJson(response);
                if (mServerResidentBean != null) {
                    mServerResidentBean.setTelPhone(residentBean.getTelPhone());
                    mServerResidentBean.setAge(residentBean.getAge());
                    mServerResidentBean.setSexy(residentBean.getSexy());
                    mServerResidentBean.setRemark(residentBean.getRemark());
                    mServerResidentBean.setIdentityCard(residentBean.getIdentityCard());
                    mView.addResidentSuccess(mServerResidentBean, mResidentCodeUrl);
                }
            }

            @Override
            protected void onFailed(String errorMessage) {
                mView.hideLoading();
                ToastUtil.showShort(errorMessage);
            }
        });

    }

    @Override
    public void loadTagInfo() {
        List<TagInfo> tagInfoList = DBManager.getInstance().getTagInfoList();
        //取系统标签
        List<TagInfo> list = new ArrayList<>();
        if (tagInfoList != null && tagInfoList.size() > 0) {
            for (TagInfo info : tagInfoList) {
                if (info.getType() == 1)
                    list.add(info);
            }
        }
        mView.loadTagInfoSuccess(list);
    }


    @Override
    public void postNewFamilyMember(String patientId, final ResidentBean newResidentBean) {
        ApiRequest.addFamilyMember(patientId, newResidentBean, new ResponseSubscriber() {
            @Override
            public void onStart() {
                if (mView != null)
                    mView.showLoading();
            }

            @Override
            protected void onSuccess(String response) {
                if (mView != null)
                    mView.hideLoading();
                handleJson(response);
                if (mServerResidentBean != null) {
                    mServerResidentBean.setRelation(newResidentBean.getRelation());
                    mServerResidentBean.setTelPhone(newResidentBean.getTelPhone());
                    mServerResidentBean.setAge(newResidentBean.getAge());
                    mServerResidentBean.setSexy(newResidentBean.getSexy());
                    mServerResidentBean.setIdentityCard(newResidentBean.getIdentityCard());
                    mServerResidentBean.setRemark(newResidentBean.getRemark());
                    if (mView != null)
                        mView.addFamilySuccess(mServerResidentBean, mResidentCodeUrl);
                }
            }

            @Override
            protected void onFailed(String errorMessage) {
                if (mView != null)
                    mView.hideLoading();
            }
        });

    }

    @Override
    public void validateIdCard(String idCard) {
        ApiRequest.validateIdCard(idCard, 1, new HttpSubscriber() {
            @Override
            public void onStart() {
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
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onNext(HttpResult result) {
//                super.onNext(result);
                //平台存在此居民
                if (result.code.equals("007")) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(result.data));
                        ResidentBean resident = ResidentBean.parse(object);
                        if (mView != null)
                            mView.validateCallBack(resident);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //平台不存在该居民
                    if (mView != null)
                        mView.validateCallBack(null);
                }
            }

        });
    }

    /**
     * 检验身份证是否存在
     *
     * @param identityCard
     */
    @Override
    public void validateIdentityCard(int familyId, String identityCard, String patientId) {
        ApiRequest.validateIdentityCard(familyId, identityCard, patientId, new HttpSubscriber() {
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
                if (result.code.equals("2007")) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(result.data));
                        String patientId = object.optString("patientId");
                        if (mView != null)
                            mView.validateIdentityCardSuccess(patientId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (result.code.equals(AppConfig.SUCCESS)) {
                    //检验通过
                    if (mView != null)
                        mView.validateIdentityCardSuccess("");
                } else {
                    ToastUtil.showShort(result.errorMessage);
                }
            }
        });
    }

    @Override
    public void validateTelphone(String telphone) {
        ApiRequest.isValidDatePhoneAccount(telphone, new HttpSubscriber() {
            @Override
            public void onStart() {
                if (mView != null)
                    mView.showLoading();
            }

            @Override
            public void onCompleted() {
                if (mView != null)
                    mView.hideLoading();
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
                String code = result.code;
                if (code.equals(AppConfig.SUCCESS)) {
                    if (mView != null)
                        mView.valildateTelphoneCallBack(false);
                } else if (code.equals("001")) {
                    if (mView != null)
                        mView.valildateTelphoneCallBack(true);
                } else {
                    ToastUtil.showShort(result.errorMessage);
                }
            }
        });

    }

    private void handleJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
            if (AppConfig.SUCCESS.equals(code)) {
                mServerResidentBean = new ResidentBean();
                JSONObject residentJsonObj = jsonObject.getJSONObject("data");
                String patientId = residentJsonObj.optString("patientId");
                String patientName = residentJsonObj.optString("patientName");
                mResidentCodeUrl = residentJsonObj.optString("qrcodeUrl");
                String residentCode = residentJsonObj.optString("code");
                String labelNames = residentJsonObj.optString("tagNames");
                int isRegister = residentJsonObj.optInt("isRegist");
                int familyId = residentJsonObj.optInt("familyId");
                mServerResidentBean.setName(patientName);
                mServerResidentBean.setPatientId(patientId);
                mServerResidentBean.setResidentCode(residentCode);
                mServerResidentBean.setRegister(isRegister);
                mServerResidentBean.setLabelNames(labelNames);
                mServerResidentBean.setFamilyId(familyId);
            } else {
                String errorMessage = jsonObject.optString("errorMessage", "");
                ToastUtil.showShort(errorMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtil.showShort(e.getMessage());
        }

    }

    @Override
    void relevancyPatientInfo(String patientId, String relevancyPatientId, String relation) {
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
                    if (mView != null)
                        mView.relevancyPatientInfoSuccess();
                }
            }
        });
    }
}
