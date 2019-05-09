package com.health2world.aio.app.task;

import com.health2world.aio.bean.TaskInfo;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.ResidentBean;

import java.util.List;

/**
 * 任务的契约接口
 * Created by Administrator on 2018/7/9 0009.
 */

public interface TaskContract {

    interface View extends BaseView<Presenter> {

        void loadSuccess(List<TaskInfo> taskList);

        void loadFailed();

        void loadPatientInfoSuccess(ResidentBean resident);

        void donePublicHealthStatus(boolean success);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        //加载待办任务
        abstract void loadTaskData(String taskType, int pageIndex);

        //根据居民Id获取居民的详细信息
        abstract void getPatientInfoById(String patientId);

        //标记公卫任务为完成状态
        abstract void donePublicHealthTask(String patientId);
    }
}
