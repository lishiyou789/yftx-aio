package com.health2world.aio.app.resident.info;

import com.health2world.aio.bean.TagInfo;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.ResidentBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18 0018.
 */

public interface FamilyMemberInfoContract {
    interface View extends BaseView<Presenter> {
        void startLoadResidentInfo(ResidentBean residentBean);

        void setResident(ResidentBean resident);

        ResidentBean getResidentBean(ResidentBean residentBean);

        void loadAddTagInfoSuccess(List<TagInfo> data);

        void updateResidentSuccess(String tip);

        void relieveSuccess();
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        abstract void loadResidentInfo(ResidentBean residentBean);

        abstract void updateResidentBean(String patientId, ResidentBean residentBean, String addTagIds, String delTagIds);

        /**
         * 加载标签字段
         */
        abstract void loadTagInfo();

        /**
         * 解除家庭关系
         */
        abstract void relieveFamilyRelation(String patientId);

    }
}
