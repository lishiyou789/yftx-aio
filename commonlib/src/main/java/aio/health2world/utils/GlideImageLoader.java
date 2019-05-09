//package aio.health2world.library.utils;
//
//import android.app.Activity;
//import android.content.Context;
//
//import com.bumptech.glide.Glide;
//import com.yancy.gallerypick.inter.ImageLoader;
//import com.yancy.gallerypick.widget.GalleryImageView;
//
//import aio.health2world.library.R;
//
///**
// * GlideImageLoader
// */
//public class GlideImageLoader implements ImageLoader {
//
//    @Override
//    public void displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height) {
//        Glide.with(context)
//                .load(path)
//                .placeholder(R.mipmap.anim_rotate_loading)
//                .centerCrop()
//                .into(galleryImageView);
//    }
//
//    @Override
//    public void clearMemoryCache() {
//
//    }
//}
///*
// *   ┏┓　　　┏┓
// * ┏┛┻━━━┛┻┓
// * ┃　　　　　　　┃
// * ┃　　　━　　　┃
// * ┃　┳┛　┗┳　┃
// * ┃　　　　　　　┃
// * ┃　　　┻　　　┃
// * ┃　　　　　　　┃
// * ┗━┓　　　┏━┛
// *     ┃　　　┃
// *     ┃　　　┃
// *     ┃　　　┗━━━┓
// *     ┃　　　　　　　┣┓
// *     ┃　　　　　　　┏┛
// *     ┗┓┓┏━┳┓┏┛
// *       ┃┫┫　┃┫┫
// *       ┗┻┛　┗┻┛
// *        神兽保佑
// *        代码无BUG!
// */