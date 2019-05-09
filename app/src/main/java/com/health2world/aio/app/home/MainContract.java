package com.health2world.aio.app.home;

import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.bean.UpgradeInfo;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.ResidentBean;

import aio.health2world.http.subscriber.DownloadStatus;

/**
 * 首页契约接口
 * Created by lishiyou on 2018/7/9 0009.
 */

public interface MainContract {

    interface View extends BaseView<Presenter> {


        void validateSuccess(int appType, UpgradeInfo info);

        void loadProgress(DownloadStatus status);

        void loadCompleted(String apkPath);

        void loadError(String path);

        void validateCallBack(boolean success, ResidentBean resident);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        //校验该身份证是否存在平台
        abstract void validateIdentityCard(String identityCard, ResidentBean resident);

        //版本校验
        abstract void validateVersion(int appType, int insId);

        //应用下载 apkType 1一体机应用软件  4 公卫App
        abstract void downLoad(int apkType, String url, String versionName);

    }
}
