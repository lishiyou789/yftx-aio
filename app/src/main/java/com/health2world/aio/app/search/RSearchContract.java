package com.health2world.aio.app.search;

import com.health2world.aio.bean.TagInfo;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.ResidentBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lishiyou on 2018/7/16 0016.
 */

public interface RSearchContract {

    interface View extends BaseView<Presenter> {

        void screeningLabelSuccess(List<TagInfo> listTag, List<TagInfo> listP1, List<TagInfo> listP2);

        void loadResidentSuccess(List<ResidentBean> list);

        void loadResidentFailed(Throwable throwable);

        void addInfoSuccess(ResidentBean resident);
    }


    abstract class Presenter extends BasePresenter<View> {
        public Presenter(View mView) {
            super(mView);
        }

        //获取标签和服务包
        abstract void screeningLabel();

        //居民检索
        abstract void residentQuery(int pageIndex, String keyWord, HashMap<String, Object> map);

        //医生添加居民
        abstract void doctorAddPatientInfo(ResidentBean resident);

    }

}
