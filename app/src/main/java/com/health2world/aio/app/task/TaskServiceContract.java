package com.health2world.aio.app.task;

import com.health2world.aio.bean.TaskDetail;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;

/**
 * Created by lishiyou on 2018/7/26 0026.
 */

public interface TaskServiceContract {

    interface View extends BaseView<Presenter> {

        void loadTaskDetailSuccess(TaskDetail detail);

        void executeTaskSuccess();
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        //根据任务ID获取任务的详细数据
        abstract void getTaskDetail(String taskId);

        //处理任务
        abstract void taskExecute(TaskDetail task, int serviceStatus, String feedBack, String advise);

    }

}
