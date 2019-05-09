package com.health2world.aio.app.clinic.recipe;

import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.MeasureBean;

/**
 * Created by lishiyou on 2018/7/26 0026.
 */

public interface RecipeContract {

    interface View extends BaseView<Presenter> {

        void phoneClinicSuccess();

        void phoneClinicFailed();

    }

    abstract class Presenter extends BasePresenter<View> {
        public Presenter(View mView) {
            super(mView);
        }


        //手机开方  门诊开方
        abstract void phoneClinic(String patientId, String dataId, int clinicType, String
                description, String advice);

    }
}
