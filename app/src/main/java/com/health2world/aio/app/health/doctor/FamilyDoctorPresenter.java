package com.health2world.aio.app.health.doctor;

import com.google.gson.Gson;
import com.health2world.aio.bean.SignService;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.health2world.aio.http.ResultSubscriber;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.ToastUtil;

/**
 * Created by lishiyou on 2018/8/6 0006.
 */

public class FamilyDoctorPresenter extends FamilyDoctorContract.Presenter {

    public FamilyDoctorPresenter(FamilyDoctorContract.View mView) {
        super(mView);
    }

    @Override
    void loadServicePackage(String patientId) {
        ApiRequest.getPatientSignDetail(patientId, 0, new ResultSubscriber<List<SignService>>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (mView != null)
                    mView.loadPackageError(e);
            }

            @Override
            public void onNext(HttpResult<List<SignService>> t) {
                super.onNext(t);
                if (t.code.equals(AppConfig.SUCCESS)) {
                    if (mView != null)
                        mView.loadPackageSuccess(t.data);
                } else {
                    if (mView != null)
                        mView.loadPackageError(null);
                }
            }
        });
    }

//    @Override
//    void uploadMeasureData(String dataId, String patientId, int gluType, MeasureBean dataBean) {
//        dataBean.setGluType(gluType);
//        ApiRequest.uploadMedicalData(dataId, patientId, dataBean, new HttpSubscriber() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                if (mView != null)
//                    mView.showLoading("正在上传数据");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                if (mView != null)
//                    mView.hideLoading();
//            }
//
//            @Override
//            public void onNext(HttpResult result) {
//                super.onNext(result);
//                if (mView != null)
//                    mView.hideLoading();
//                if (result.code.equals(AppConfig.SUCCESS)) {
//                    ToastUtil.showShort("数据上传成功");
//                    try {
//                        JSONObject o = new JSONObject(new Gson().toJson(result.data));
//                        int dataId = o.optInt("dataId");
//                        if (mView != null)
//                            mView.uploadMeasureDataSuccess(dataId + "");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }

    @Override
    void addServiceRecord(final ResidentBean resident, int signId, int serviceItemId, int serviceType, String dataId, String advise) {
        ApiRequest
                .addServiceRecord(resident.getPatientId(), signId, serviceItemId, serviceType, dataId, advise)
                .subscribe(new HttpSubscriber() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mView != null)
                            mView.serviceRecordError();
                    }

                    @Override
                    public void onNext(HttpResult result) {
                        super.onNext(result);
                        if (result.code.equals(AppConfig.SUCCESS)) {
                            if (mView != null)
                                mView.serviceRecordSuccess();
                        } else {
                            if (mView != null)
                                mView.serviceRecordError();
                        }
                    }
                });
    }
}
