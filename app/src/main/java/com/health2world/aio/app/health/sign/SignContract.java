package com.health2world.aio.app.health.sign;

import com.health2world.aio.bean.SignService;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.ResidentBean;

import java.util.List;

/**
 * Created by lishiyou on 2018/8/8 0008.
 */

interface SignContract {

    interface View extends BaseView<Presenter> {

        void loadFamilyMemberSuccess(List<ResidentBean> residentList);

        void loadFamilyMemberError(Throwable e);

        void loadServicePackageSuccess(List<SignService> serviceList);

        void loadServicePackageError(Throwable e);

        void queryHealthFileSuccess(ResidentBean resident, SignServiceSection section, int position);

        void updateIdentityCardSuccess(String identityCard);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        //加载家庭成员
        abstract void loadFamilyMember(String patientId);

        //加载服务包数据
        abstract void loadServicePackage();

        //查询公卫档案
        abstract void queryHealthFile(String identityCard, SignServiceSection section, int position);

        abstract void updatePatientIdentityCard(String firstPatientId, String patientId,
                                                String identityCard);

    }
}
