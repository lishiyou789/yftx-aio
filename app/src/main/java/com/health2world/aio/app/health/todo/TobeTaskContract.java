package com.health2world.aio.app.health.todo;

import com.health2world.aio.bean.TaskInfo;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.ResidentBean;

import java.util.List;

/**
 * Created by lishiyou on 2018/8/6 0006.
 */

interface TobeTaskContract {

    interface View extends BaseView<Presenter> {

        void loadTobeTaskSuccess(List<TaskInfo> list);

        void loadTobeTaskFailure();

        void loadPatientInfoSuccess(ResidentBean resident);

        void donePublicHealthStatus(boolean success);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        abstract void loadTobeTask(ResidentBean resident, int pageIndex);

        //根据居民Id获取居民的详细信息
        abstract void getPatientInfoById(String patientId);

        //标记公卫任务为完成状态
        abstract void donePublicHealthTask(String patientId);

    }
}
