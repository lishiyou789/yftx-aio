//package aio.health2world.library.utils;
//
//import android.app.Activity;
//
//import com.yancy.gallerypick.config.GalleryConfig;
//import com.yancy.gallerypick.config.GalleryPick;
//import com.yancy.gallerypick.inter.IHandlerCallBack;
//
//import java.util.List;
//
///**
// * 图片单选
// * Created by lishiyou on 2017/7/4.
// */
//
//public class SingleImageSelect {
//
//    public static SingleImageSelect imageSelect;
//
//    private GalleryConfig galleryConfig;
//
//    private IHandlerCallBack callBack;
//
//    private ImageCallBack imageCallBack;
//
//    private SingleImageSelect() {
//        init();
//    }
//
//    public static SingleImageSelect getInstance() {
//        if (imageSelect == null) {
//            imageSelect = new SingleImageSelect();
//        }
//        return imageSelect;
//    }
//
//    public void init() {
//        callBack = new IHandlerCallBack() {
//            @Override
//            public void onStart() {
//            }
//
//            @Override
//            public void onSuccess(List<String> photoList) {
//                if (imageCallBack != null) {
//                    imageCallBack.callback(photoList);
//                }
//            }
//
//            @Override
//            public void onCancel() {
//            }
//
//            @Override
//            public void onFinish() {
//            }
//
//            @Override
//            public void onError() {
//            }
//        };
//
//        galleryConfig = new GalleryConfig.Builder()
//                // ImageLoader 加载框架（必填）
//                .imageLoader(new GlideImageLoader())
//                // 监听接口（必填）
//                .iHandlerCallBack(callBack)
//                // 是否多选   默认：false
//                .multiSelect(false)
//                // 配置多选时 的多选数量。    默认：9
//                .maxSize(1)
//                // 配置裁剪功能的参数，   默认裁剪比例 1:1
//                .crop(false, 1, 1, 500, 500)
//                // 是否现实相机按钮  默认：false
//                .isShowCamera(true)
//                // 图片存放路径
//                .filePath("/yftx/Pictures")
//                .build();
//    }
//
//    public SingleImageSelect open(Activity activity) {
//        GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(activity);
//        return this;
//    }
//
//    public interface ImageCallBack {
//        void callback(List<String> photoList);
//    }
//
//    public void setImageCallBack(ImageCallBack callBack) {
//        this.imageCallBack = callBack;
//    }
//}
