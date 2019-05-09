package com.health2world.aio.app.setting;

import android.text.TextUtils;

import com.health2world.aio.bean.UpgradeInfo;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.ResultSubscriber;
import com.health2world.aio.util.Logger;

import java.io.File;
import java.util.Date;

import aio.health2world.http.HttpResult;
import aio.health2world.http.subscriber.DownloadStatus;
import aio.health2world.http.subscriber.RxDownload;
import aio.health2world.utils.DateUtil;
import aio.health2world.utils.ToastUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lishiyou on 2018/7/19 0019.
 */

public class SettingPresenter extends SettingContract.Presenter {

    public SettingPresenter(SettingContract.View mView) {
        super(mView);
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
                super.onNext(t);
                if (mView != null)
                    mView.hideLoading();
                if (t.code.equals(AppConfig.SUCCESS)) {
                    UpgradeInfo upgradeInfo = t.data;
                    //有新版本
                    if (upgradeInfo.getIsUpgrade() == 1) {
                        if (mView != null)
                            mView.validateSuccess(appType, upgradeInfo);
                    } else {
                        ToastUtil.showShort("暂无新版本");
                    }
                }
            }
        });
    }

    @Override
    void downLoad(int apkType, String url, String versionName) {
        if (TextUtils.isEmpty(url)) {
            ToastUtil.showShort("下载链接异常");
            return;
        }
        boolean isExist = false;
        String apkName = null;
        if (apkType == AppConfig.APP_SOFTWARE) {
            apkName = "Health2World_V" + versionName + ".apk";
        } else if (apkType == AppConfig.APP_PUBLIC_HEALTH) {
            apkName = "HealthOnePublicHealth_V" + versionName + ".apk";
        } else if (apkType == AppConfig.APP_DEVICE_85 || apkType == AppConfig.APP_DEVICE_86) {
            apkName = "AppDevice_V" + versionName + ".apk";
        } else if (apkType == AppConfig.APP_DEVICE_MANAGER) {
            apkName = "DeviceManager_V" + versionName + ".apk";
        }
        String apkDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        apkDir += "/Download";//数据库所在目录
        File file = new File(apkDir);
        if (!file.exists()) {
            file.mkdirs();
        }

        //先清空之前安装包
        File[] files = file.listFiles();
        for (File _file : files) {
            if (_file.exists() && _file.isFile())
                //已下载相同版本安装包
                if (_file.getName().equals(apkName)) {
                    isExist = true;
                    break;
                } else {
                    try {
                        //只删除这个软件的安装包
                        if (apkName != null && _file.getName().startsWith(apkName.split("_")[0]))
                            _file.delete();
                    } catch (Exception ignored) {
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
}
