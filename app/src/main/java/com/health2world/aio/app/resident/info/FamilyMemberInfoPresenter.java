package com.health2world.aio.app.resident.info;

import com.google.gson.Gson;
import com.health2world.aio.bean.TagInfo;
import com.health2world.aio.common.DataServer;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.health2world.aio.http.ResultSubscriber;
import com.konsung.bean.ResidentBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.ToastUtil;

/**
 * Created by Administrator on 2018/7/18 0018.
 */

public class FamilyMemberInfoPresenter extends FamilyMemberInfoContract.Presenter {


    public FamilyMemberInfoPresenter(FamilyMemberInfoContract.View mView) {
        super(mView);
    }

    @Override
    void loadResidentInfo(ResidentBean residentBean) {
        ApiRequest.getPatientInfo(residentBean.getPatientId()).subscribe(new HttpSubscriber() {
            @Override
            public void onStart() {
                if (mView != null)
                    mView.showLoading();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
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
            public void onNext(HttpResult t) {
                super.onNext(t);
                if (t.code.equals(AppConfig.SUCCESS)) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(t.data));
                        ResidentBean data = ResidentBean.parse(object);
                        if (mView != null)
                            mView.setResident(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    void updateResidentBean(String patientId, ResidentBean residentBean, String addTagIds, String delTagIds) {
        ApiRequest.updatePatientInfo(patientId, residentBean, addTagIds, delTagIds, new ResultSubscriber<String>() {
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
            public void onNext(HttpResult<String> t) {
                super.onNext(t);
                String code = t.code;
                if (AppConfig.SUCCESS.equals(code)) {
                    if (mView != null)
                        mView.updateResidentSuccess("修改成功");
                } else {
                    ToastUtil.showShort(t.errorMessage);
                }
            }
        });

    }

    @Override
    public void loadTagInfo() {
        List<TagInfo> tagInfoList = DBManager.getInstance().getTagInfoList();
        List<TagInfo> list = new ArrayList<>();
        //取系统标签
        if (tagInfoList != null && tagInfoList.size() > 0) {
            if (tagInfoList != null && tagInfoList.size() > 0) {
                for (TagInfo info : tagInfoList) {
                    if (info.getType() == 1)
                        list.add(info);
                }
            }
            if (mView != null)
                mView.loadAddTagInfoSuccess(list);
        } else {
            DataServer.initData();
        }

//        ApiRequest.getTagInfo(new ResultSubscriber<List<TagInfoBean>>() {
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//
//            }
//
//            @Override
//            public void onNext(HttpResult<List<TagInfoBean>> t) {
//                super.onNext(t);
//                String code = t.code;
//                if (code.equals(AppConfig.SUCCESS)) {
//                    List<TagInfoBean> data = t.data;
//                    mView.loadAddTagInfoSuccess(data);
//
//                }
//            }
//        });
    }


    @Override
    void relieveFamilyRelation(String patientId) {
        ApiRequest.unBindFamilyMember(patientId, new HttpSubscriber() {
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
                    ToastUtil.showShort("操作成功");
                    if (mView != null)
                        mView.relieveSuccess();
                }
            }
        });
    }
}
