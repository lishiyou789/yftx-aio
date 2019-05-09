package com.health2world.aio.app.health.doctor;

import com.health2world.aio.bean.SignService;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.util.List;

/**
 * Created by lishiyou on 2018/8/6 0006.
 */

public interface FamilyDoctorContract {

    interface View extends BaseView<Presenter> {

        void loadPackageSuccess(List<SignService> list);

        void loadPackageError(Throwable throwable);

        void serviceRecordSuccess();

        void serviceRecordError();

//        void uploadMeasureDataSuccess(String dataId);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        //加载服务包数据以及履约数据
        abstract void loadServicePackage(String patientId);

//        //上传测量数据
//        abstract void uploadMeasureData(String dataId, String patientId, int gluType, MeasureBean dataBean);

        //添加服务记录
        abstract void addServiceRecord(ResidentBean resident, int signId,
                                       int serviceItemId,int serviceType, String dataId, String advise);
    }
}
