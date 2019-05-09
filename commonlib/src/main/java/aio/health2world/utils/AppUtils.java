package aio.health2world.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aio.health2world.SApplication;

/**
 * Created by lishiyou on 2017/8/3 0003.
 */

public class AppUtils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    //判断某个界面是否在前台
    public static String getRunningActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();

        return runningActivity;

    }

    //屏幕变亮
    public static void setLight(Activity context) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 1.0f;
        lp.dimAmount = 1.0f;
        context.getWindow().setAttributes(lp);
    }

    //屏幕变暗
    public static void setDark(Activity context) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.5f;
        lp.dimAmount = 0.5f;
        context.getWindow().setAttributes(lp);
    }

    public static int getAppVersionCode(Context context) {
        int versioncode = 1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

    public static String getAppVersionName(Context context) {
        String versionName = "1.0";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
//            versionName = pi.versionName;
            versionName = pi.versionName == null ? "" : pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * getRealMetrics - 屏幕的原始尺寸，即包含状态栏。
     * version >= 4.2.2
     */
    public static void getMetrics(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getRealMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        Log.e("lsy", "width=" + width + ",height=" + height + ",density=" + density + ",densityDpi=" + densityDpi);
    }

    /**
     * 不包含状态栏的尺寸
     *
     * @param context
     */
    public static void getMetrics1(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        Log.e("lsy", "width1=" + width + ",height1=" + height + ",density1=" + density + ",densityDpi1=" + densityDpi);
    }

    /**
     * 打开前置摄像头
     *
     * @param activity
     * @param requestCode
     */
    public static void openHeadCamera(Activity activity, int requestCode) {
        Intent intentCamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra("camerasensortype", 2); // 调用前置摄像头
        intentCamera.putExtra("autofocus", true); // 自动对焦
        intentCamera.putExtra("fullScreen", false); // 全屏
        intentCamera.putExtra("showActionIcons", false);
        activity.startActivityForResult(intentCamera, requestCode);
    }

    /**
     * 应用程序安装
     *
     * @param activity
     * @param fileName
     */
    public static void installApk(Activity activity, String fileName,int requestCode) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
        activity.startActivityForResult(intent,requestCode);
    }

    /**
     * 获取设备编号
     *
     * @return
     */
    public static String getDeviceNo() {
        if (TextUtils.equals(android.os.Build.VERSION.RELEASE, "4.4.2"))
            return (String) SPUtils.get(SApplication.mInstance, "device_code", DeviceUtil.getAndroidId(SApplication.mInstance));
        try {
            PackageInfo packageInfo = SApplication.mInstance.getPackageManager().getPackageInfo("com.konsung.factorymaintain", 0);
            if (packageInfo == null) {
                return (String) SPUtils.get(SApplication.mInstance, "device_code", DeviceUtil.getAndroidId(SApplication.mInstance));
            } else {
                Uri uri = Uri.parse("content://com.konsung.factorymaintain/Devices");
                ContentResolver resolver = SApplication.mInstance.getContentResolver();
                Cursor cursor = resolver.query(uri, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String s = cursor.getString(cursor.getColumnIndex("path"));
                        return s;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("lsy", "no public health application...");
        }
        return DeviceUtil.getAndroidId(SApplication.mInstance);
    }

    public static String getDeviceToken() {
        try {
            PackageInfo packageInfo = SApplication.mInstance.getPackageManager().getPackageInfo("com.konsung.factorymaintain", 0);
            if (packageInfo != null) {
                Uri uri = Uri.parse("content://com.konsung.factorymaintain/Devices");
                ContentResolver resolver = SApplication.mInstance.getContentResolver();
                Cursor cursor = resolver.query(uri, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String s = cursor.getString(cursor.getColumnIndex("path"));
                        return s;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("lsy", "no public health application...");
        }
        return "";
    }

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText, int maxLength) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()-+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？1234567890]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    ToastUtil.showShort("请输入正确的姓名");
                    return "";
                } else return null;
            }
        };
        InputFilter spaceFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" "))
                    return "";
                else
                    return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter, spaceFilter, new InputFilter.LengthFilter(maxLength)});
    }
}
