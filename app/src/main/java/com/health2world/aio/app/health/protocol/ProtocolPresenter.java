package com.health2world.aio.app.health.protocol;

import com.google.gson.Gson;
import com.health2world.aio.MyApplication;
import com.health2world.aio.bean.SignMember;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.ApiUpload;
import com.health2world.aio.http.HttpSubscriber;
import com.health2world.aio.util.Logger;
import com.konsung.bean.ResidentBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.ToastUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by lishiyou on 2018/8/17 0017.
 */

public class ProtocolPresenter extends ProtocolContract.Presenter {

    public ProtocolPresenter(ProtocolContract.View mView) {
        super(mView);
    }

    @Override
    void compressImage(String imagePath) {
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        if (sdExist) {
            String dbDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            dbDir += "/health2world";//数据库所在目录
            //判断目录是否存在，不存在则创建该目录
            File dirFile = new File(dbDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            Luban.with(MyApplication.getInstance())
                    .load(imagePath)
                    .ignoreBy(100)
                    .setTargetDir(dbDir)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            if (mView != null)
                                mView.showLoading();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mView != null)
                                mView.hideLoading();
                            ToastUtil.showShort(e.getMessage());
                        }

                        @Override
                        public void onSuccess(File file) {
                            if (mView != null)
                                mView.hideLoading();
                            if (mView != null)
                                mView.compressImageSuccess(file);
                        }
                    })
                    .launch();
        }
    }

    @Override
    void uploadImage(File file) {
        if (mView != null)
            mView.showLoading();
        ApiUpload.getInstance().uploadImage("0", file.getAbsolutePath(), new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (mView != null)
                    mView.hideLoading();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String string = response.body().string();
                    Logger.i("imageUpload", string);
                    JSONObject object = new JSONObject(string);
                    String code = object.optString("code");
                    String errorMessage = object.optString("errorMessage");
                    if (code.equals(AppConfig.SUCCESS)) {
                        String imageUrl = object.optString("data");
                        if (mView != null)
                            mView.uploadImageSuccess(imageUrl);
                    } else {
                        if (mView != null)
                            mView.uploadImageError(errorMessage);
                    }
                } catch (JSONException e) {
                    if (mView != null)
                        mView.uploadImageError(e.getMessage());
                }
            }
        });
    }

    @Override
    void sign(String imageUrl, List<SignMember> memberList) {
        SignMember member = new SignMember();
        String jsonData = new Gson().toJson(memberList);
        member.setMembers(jsonData);
        String submitJsonData = new Gson().toJson(member);
        ApiRequest
                .familySign(submitJsonData, imageUrl)
                .subscribe(new HttpSubscriber() {

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
                                mView.actionSuccess(new Gson().toJson(result.data));
                        } else {
                            ToastUtil.showShort(result.errorMessage);
                        }
                    }
                });
    }

    @Override
    void unSign(String imageUrl, final ResidentBean resident, List<SignMember> memberList) {
        SignMember member = new SignMember();
        String jsonData = new Gson().toJson(memberList);
        member.setMembers(jsonData);
        String submitJsonData = new Gson().toJson(member);
        ApiRequest
                .removeSign(resident.getPatientId(), submitJsonData, imageUrl)
                .subscribe(new HttpSubscriber() {
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
                                mView.actionSuccess(new Gson().toJson(result.data));
                        } else {
                            ToastUtil.showShort(result.errorMessage);
                        }
                    }
                });
    }
}
