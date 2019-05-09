package com.health2world.aio.app.home;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.bean.UpgradeInfo;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.health2world.aio.http.ResultSubscriber;
import com.health2world.aio.util.Logger;
import com.konsung.bean.ResidentBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import aio.health2world.http.HttpResult;
import aio.health2world.http.subscriber.DownloadStatus;
import aio.health2world.http.subscriber.RxDownload;
import aio.health2world.utils.DateUtil;
import retrofit2.http.Url;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页契约接口
 * Created by lishiyou on 2018/7/6 0006.
 */

public class MainPresenter extends MainContract.Presenter {

    public MainPresenter(MainContract.View mView) {
        super(mView);
    }

    @Override
    void validateIdentityCard(String identityCard, final ResidentBean resident) {
        ApiRequest.validateIdCard(identityCard, 0, new HttpSubscriber() {
            @Override
            public void onStart() {
                super.onStart();
                if (mView != null)
                    mView.showLoading("正在查询...");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (mView != null)
                    mView.hideLoading();
            }

            @Override
            public void onNext(HttpResult result) {
//                super.onNext(result);
                if (mView != null)
                    mView.hideLoading();
                //平台存在此居民
                if (result.code.equals("007")) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(result.data));
                        ResidentBean resident = ResidentBean.parse(object);
                        if (mView != null)
                            mView.validateCallBack(true, resident);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //平台不存在该居民
                    if (mView != null)
                        mView.validateCallBack(false, resident);
                }
            }
        });
    }

    @Override
    void validateVersion(final int appType, int insId) {
        ApiRequest.validateVersion(appType, insId, new ResultSubscriber<UpgradeInfo>() {
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
            public void onNext(HttpResult<UpgradeInfo> t) {
                if (mView != null)
                    mView.hideLoading();
                if (t.code.equals(AppConfig.SUCCESS)) {
                    UpgradeInfo upgradeInfo = t.data;
                    //有新版本
                    if (upgradeInfo.getIsUpgrade() == 1) {
                        if (mView != null)
                            mView.validateSuccess(appType, upgradeInfo);
                    }
                }
            }
        });
    }

    @Override
    void downLoad(int apkType, String url, String versionName) {
        if (TextUtils.isEmpty(url))
            return;
        boolean isExist = false;
        String apkName = "Health2World_V" + versionName + ".apk";
        String apkDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        apkDir += "/Download";//数据库所在目录
        File file = new File(apkDir);
        //判断是否存在此文件夹,若无则创建
        if (!file.exists()) {
            file.mkdir();
        }
        //先清空之前
        File[] files = file.listFiles();
        for (File _file : files) {
            if (_file.exists() && _file.isFile())
                //已下载相同版本安装包
                if (_file.getName().equals(apkName)) {
                    isExist = true;
                    break;
                } else {
                    try {
                        if (_file.getName().startsWith("Health2World_V"))
                            _file.delete();
                    } catch (Exception pE) {
                        Logger.e("zrl", "文件删除失败: " + pE.getMessage());
                    }
                }
        }

        //再重新下载
        if (!isExist) {
            final String installApkPath = apkDir + "/" + apkName;
            RxDownload
                    .getInstance()
                    .download(url, apkName, apkDir)//开始下载
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<DownloadStatus>() {
                        @Override
                        public void onCompleted() {
                            if (mView != null)
                                mView.loadCompleted(installApkPath);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mView != null)
                                mView.loadError(installApkPath);
                        }

                        @Override
                        public void onNext(DownloadStatus status) {
                            if (mView != null)
                                mView.loadProgress(status);
                        }
                    });
        } else {
            //已存在安装包
            if (mView != null)
                mView.loadCompleted(apkDir + "/" + apkName);
        }
    }

    //获取文件MD5
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
