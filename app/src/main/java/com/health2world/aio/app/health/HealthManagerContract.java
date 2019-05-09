package com.health2world.aio.app.health;

import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.ResidentBean;

import java.util.List;

/**
 * Created by lishiyou on 2018/8/6 0006.
 */
interface HealthManagerContract {

    interface View extends BaseView<Presenter> {

        void loadFamilyMemberSuccess(List<ResidentBean> residentList);

//        void loadSignServiceSuccess(List<SignService> signServiceList);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        abstract void loadFamilyMember(String patientId);

//        abstract void loadPatientSignDetail(String patientId);

    }

}
