package com.health2world.aio.app.clinic;

import com.google.gson.Gson;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.konsung.bean.MeasureBean;

import org.json.JSONException;
import org.json.JSONObject;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.ToastUtil;

/**
 * Created by lishiyou on 2018/7/16 0016.
 */

public class ClinicPresenter extends ClinicContract.Presenter {


    public ClinicPresenter(ClinicContract.View mView) {
        super(mView);
    }

    //保存测量数据
    @Override
    void uploadMeasureData(String dataId, String patientId,int sex, int gluType, MeasureBean dataBean) {
        dataBean.setGluType(gluType);
        ApiRequest.uploadMedicalData(dataId, patientId,sex, dataBean,
                new HttpSubscriber() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        if (mView != null)
                            mView.showLoading("正在上传数据");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mView != null)
                            mView.hideLoading();
                        ToastUtil.showShort("数据上传失败");
                        mView.uploadFailed();
                    }

                    @Override
                    public void onNext(HttpResult result) {
                        super.onNext(result);
                        if (mView != null)
                            mView.hideLoading();
                        if (result.code.equals(AppConfig.SUCCESS)) {
                            ToastUtil.showShort("数据上传成功");
                            String dataId = result.data.toString();
                            if (mView != null)
                                mView.uploadSuccess(dataId);
                        }
                    }
                });
    }
}
