package com.health2world.aio.app.health.protocol;

import com.health2world.aio.bean.SignMember;
import com.health2world.aio.common.mvp.BasePresenter;
import com.health2world.aio.common.mvp.BaseView;
import com.konsung.bean.ResidentBean;

import java.io.File;
import java.util.List;

/**
 * Created by lishiyou on 2018/8/17 0017.
 */

public interface ProtocolContract {

    interface View extends BaseView<Presenter> {

        //图片压缩成功
        void compressImageSuccess(File file);

        //图片上传成功
        void uploadImageSuccess(String imageUrl);

        //图片上传失败
        void uploadImageError(String errorMsg);

        //操作成功
        void actionSuccess(String data);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View mView) {
            super(mView);
        }

        //图片压缩
        abstract void compressImage(String imagePath);

        //图片上传
        abstract void uploadImage(File file);

        //签约
        abstract void sign(String imageUrl, List<SignMember> memberList);

        //解约
        abstract void unSign(String imageUrl, ResidentBean resident, List<SignMember> memberList);
    }

}
