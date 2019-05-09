package com.health2world.aio.app.login;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.common.DataServer;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.config.KonsungConfig;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.health2world.aio.util.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.MD5Util;
import aio.health2world.utils.MatchUtil;
import aio.health2world.utils.NetworkUtil;
import aio.health2world.utils.SPUtils;
import aio.health2world.utils.ToastUtil;

/**
 * Created by Administrator on 2018/7/6 0006.
 */

public class LoginPresenter extends LoginContract.Presenter {

    public LoginPresenter(LoginContract.View mView) {
        super(mView);
    }

    //true为账号密码登录  false为扫码登录
    @Override
    void login(final boolean isScan, final String name, final String password) {
        if (!NetworkUtil.isConnected(MyApplication.getInstance())) {
            ToastUtil.showLong(MyApplication.getInstance().getString(R.string.net_error));
            return;
        }
        //登录请求
        ApiRequest.login(name, isScan ? password : MD5Util.getMD5String(password), new HttpSubscriber() {
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
                    try {
                        JSONObject o = new JSONObject(new Gson().toJson(result.data));
                        DoctorBean doctor = DoctorBean.parseBean(o);
                        doctor.setAccount(name);
                        doctor.setTime(System.currentTimeMillis());
                        //由于扫码登录返回的密码是加密之后的 所以不保存密码
                        if (!isScan)
                            doctor.setPassword(password);
                        else
                            doctor.setPassword("");
                        //token
                        SPUtils.put(MyApplication.getInstance(), AppConfig.TOKEN_ID, doctor.getTokenId());
                        //当前登录者
                        SPUtils.put(MyApplication.getInstance(), AppConfig.DOCTOR_ID, doctor.getDoctorId());
                        SPUtils.put(MyApplication.getInstance(), AppConfig.DOCTOR_PHONE, name);
                        SPUtils.put(MyApplication.getInstance(), AppConfig.DOCTOR_PWD, doctor.getPassword());
                        SPUtils.put(MyApplication.getInstance(), AppConfig.ORG_ID, doctor.getOrgId());
                        SPUtils.put(MyApplication.getInstance(), AppConfig.IS_LOGIN, true);
                        //康尚公卫url
                        SPUtils.put(MyApplication.getInstance(), KonsungConfig.PUBLIC_HEALTH_URL, doctor.getKsUrl());
                        //Bugly
                        CrashReport.setUserId(doctor.getDoctorId() + "-" + doctor.getName());
                        try {
                            DBManager.getInstance().getDoctorDao().createOrUpdate(doctor);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (mView != null) {
                            mView.loginSuccess(doctor);
                            DataServer.initData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //获取扫码登录时的二维码
    @Override
    void QRLogin() {
        ApiRequest.QRLogin(new HttpSubscriber() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(HttpResult result) {
                super.onNext(result);
                if (result.code.equals(AppConfig.SUCCESS)) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(result.data));
                        String codeUrl = object.optString("qcodeInfo");
                        if (!codeUrl.startsWith("http://")) {
                            codeUrl = "http://" + codeUrl;
                        }
                        if (mView != null)
                            mView.QRUrlSuccess(codeUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    void securityCode(String phone) {
        ApiRequest.securityCode(phone, new HttpSubscriber() {
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
                    ToastUtil.showShort("短信发送成功");
                    if (mView != null)
                        mView.securityCodeSuccess();
                }
            }
        });
    }

    @Override
    void resetPassword(String phone, String code, String password) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShort("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showShort("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showShort("密码不能为空");
            return;
        }
        ApiRequest.resetPwd(phone, code, password, new HttpSubscriber() {
            @Override
            public void onStart() {
                super.onStart();
                if (mView != null) {
                    mView.showLoading();
                }
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
                        mView.resetPwdSuccess();
                }
            }
        });
    }
}
