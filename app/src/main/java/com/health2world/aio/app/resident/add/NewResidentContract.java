package com.health2world.aio.app.resident.add;


import com.health2world.aio.bean.TagInfo;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.ResidentBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/16 0016.
 */

public interface NewResidentContract {
    interface View extends BaseView<Presenter> {


        void toResidentDetails();

        void toResidentHealth();

        void backResidentDetails();

        void backResidentBase();

        void loadTagInfoSuccess(List<TagInfo> data);

        List<TagInfo> getCheckTag();

        /**
         * 获取界面输入的患者信息
         */
        ResidentBean getResidentBean();

        /**
         * 添加患者成功
         */
        void addResidentSuccess(ResidentBean residentBean, String residentCodeUrl);

        /**
         * 添加家属成功
         */
        void addFamilySuccess(ResidentBean residentBean, String residentCodeUrl);

        boolean checkBaseData();

        boolean checkDetailsData();

        /**
         * 验证身份证是否存在
         */
        void validateCallBack(ResidentBean residentBean);

        /**
         * 验证手机号是否存在
         *
         * @param isExit
         */
        void valildateTelphoneCallBack(boolean isExit);

        void validateIdentityCardSuccess(String patientId);

        void relevancyPatientInfoSuccess();
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        /**
         * 提交新居民信息到服务器
         */
        public abstract void postNewResident(ResidentBean residentBean);

        /**
         * 加载标签字段
         */
        public abstract void loadTagInfo();

        /**
         * 提交新居民信息到服务器
         */
        public abstract void postNewFamilyMember(String patientId, ResidentBean newResidentBean);

//        /**
//         * 验证身份证是否存在
//         */
        public abstract void validateIdCard(String idcard);

        /**
         * 验证身份证是否存在
         */
        public abstract void validateIdentityCard(int familyId,String identityCard,String patientId);

        //关联一个已经存在的居民
        abstract void relevancyPatientInfo(String patientId, String relevancyPatientId, String relation);


        /**
         * 验证手机号码是否存在
         */
        public abstract void validateTelphone(String telphone);
    }
}
