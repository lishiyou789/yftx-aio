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
///**图片多选
// * Created by lishiyou on 2017/7/4.
// */
//
//public class MultiImageSelect {
//
//    public static MultiImageSelect imageSelect;
//
//    private GalleryConfig galleryConfig;
//
//    private IHandlerCallBack callBack;
//
//    private ImageCallBack imageCallBack;
//
//    private MultiImageSelect() {
//        init();
//    }
//
//    public static MultiImageSelect getInstance() {
//        if (imageSelect == null) {
//            imageSelect = new MultiImageSelect();
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
//                // 配置是否多选的同时 配置多选数量   默认：false ， 9
//                .multiSelect(true, 6)
//                // 配置裁剪功能的参数，   默认裁剪比例 1:1
//                .crop(false, 1, 1, 500, 500)
//                // 是否现实相机按钮  默认：false
//                .isShowCamera(true)
//                // 图片存放路径
//                .filePath("/yftx/Pictures")
//                .build();
//    }
//
//    public MultiImageSelect open(Activity activity){
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
