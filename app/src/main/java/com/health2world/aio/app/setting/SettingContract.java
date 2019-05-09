package com.health2world.aio.app.setting;

import com.health2world.aio.bean.UpgradeInfo;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;

import aio.health2world.http.subscriber.DownloadStatus;

/**
 * Created by lishiyou on 2018/7/19 0019.
 */
interface SettingContract {

    interface View extends BaseView<Presenter> {

        void validateSuccess(int appType, UpgradeInfo info);

        void loadProgress(DownloadStatus status);

        void loadCompleted(String apkPath);

        void loadError(String apkPath);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        //版本校验
        abstract void validateVersion(int appType, int insId);

        //应用下载 apkType 1一体机应用软件  4 公卫App
        abstract void downLoad(int apkType, String url,String versionName);
    }
}
