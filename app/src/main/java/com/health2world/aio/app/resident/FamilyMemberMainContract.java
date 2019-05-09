package com.health2world.aio.app.resident;

import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.ResidentBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18 0018.
 */

public interface FamilyMemberMainContract {

    interface View extends BaseView<Presenter> {

        void loadFamilyMemberSuccess(List<ResidentBean> data);

        void loadFamilyMemberError();

        void relevancyPatientInfoSuccess(int familyId);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        abstract void loadFamilyMember(ResidentBean residentBean);

        //关联一个已经存在的居民
        abstract void relevancyPatientInfo(String patientId, String relevancyPatientId, String relation);

    }

}
