package com.health2world.aio.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.inputmethod.InputMethodManager;

import com.health2world.aio.MyApplication;
import com.health2world.aio.config.KonsungConfig;

import aio.health2world.utils.ToastUtil;

/**
 * 跳转类
 * Created by Administrator on 2018/7/5 0005.
 */

public class ActivityUtil {


    /**
     * 检查包名是否存在
     */
    public static boolean checkPackInfo(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = MyApplication.getInstance().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("lsy", "PackageName NotFoundException");
        }
        return packageInfo != null;
    }

    /**
     * 检查公卫App是否存在 存在则跳转到公卫App
     */
    public static void enterPublicHealth(Activity activity, int index, int requestCode) {
        if (checkPackInfo(KonsungConfig.PUBLIC_HEALTH_PACKAGE)) {
            PackageManager packageManager = activity.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(KonsungConfig.PUBLIC_HEALTH_PACKAGE);
            intent.putExtra("index", index);
            intent.putExtra("flag", 2);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(intent, requestCode);
        } else {
            ToastUtil.showShort("请安装公共卫生应用程序");
        }
    }


    /**
     * 获取程序版本信息
     */
    public static PackageInfo getAppInfo(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = MyApplication.getInstance().getPackageManager().
                    getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("health2world","Package NotFoundException");
        }
        return packageInfo;
    }

    /**
     * 检查厂家维护软件是否存在，存在则跳转到厂家维护
     */
    public static void enterFactoryMaintain(Activity activity, int requestCode) {
        if (checkPackInfo(KonsungConfig.FACTORY_MAIN_PACKAGE)) {
            PackageManager packageManager = activity.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(KonsungConfig.FACTORY_MAIN_PACKAGE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(intent, requestCode);
        } else {
            ToastUtil.showShort("请安装厂家维护应用程序");
        }
    }

    /**
     * 检查文件管理软件是否存在，存在则跳转到文件管理
     */
    public static void enterFileManager(Activity activity, int requestCode) {
        if (checkPackInfo(KonsungConfig.FILE_MANAGER_PACKAGE)) {
            PackageManager packageManager = activity.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(KonsungConfig.FILE_MANAGER_PACKAGE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(intent, requestCode);
        } else {
            ToastUtil.showShort("找不到文件管理应用程序");
        }
    }

    public static void finishActivity(Activity activity) {
        closeKeyboard(activity);
        activity.finish();
    }


    public static void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }




}
