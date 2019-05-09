package com.health2world.aio.app.login;

import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;

/**
 * Created by Administrator on 2018/7/9 0009.
 */

interface LoginContract {

    interface View extends BaseView<Presenter> {
        //数据保存
        void loginSuccess(DoctorBean doctor);

        void QRUrlSuccess(String url);

        void securityCodeSuccess();

        void resetPwdSuccess();
    }


    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        //登录 boolean isScan是否为扫码登录
        abstract void login(boolean isScan,String name, String password);

        //获取扫码登录时的二维码

        abstract void QRLogin();

        //获取手机验证码
        abstract void securityCode(String phone);

        //重置密码
        abstract void resetPassword(String phone, String code, String password);

    }


}
