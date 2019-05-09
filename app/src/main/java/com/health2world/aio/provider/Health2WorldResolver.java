package com.health2world.aio.provider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.health2world.aio.MyApplication;
import com.health2world.aio.config.KonsungConfig;
import com.health2world.aio.util.ActivityUtil;
import com.health2world.aio.util.Logger;

/**
 * Created by lishiyou on 2018/8/3 0003.
 */
public class Health2WorldResolver {

    public static final String TAG = "Health2WorldResolver";

    public static final String KEY_PATH = "path";

    public static final String KEY_CONFIG = "config";


    public static final Uri SERVICE_URI = Uri.parse(KonsungConfig.URI_SERVER_URL);
    public static final Uri DEVICE_CODE_URI = Uri.parse(KonsungConfig.URI_DEVICE_NO);
    public static final Uri DEVICE_CONFIG_URI = Uri.parse(KonsungConfig.URI_DEVICE_CONFIG);

    private static final Health2WorldResolver ourInstance = new Health2WorldResolver();

    public static Health2WorldResolver getInstance() {
        return ourInstance;
    }

    private Health2WorldResolver() {
    }

    /**
     * 获取设备编号
     *
     * @return
     */
    public String getDeviceNo() {
        //先判断有没有安装厂家维护软件
        if (!ActivityUtil.checkPackInfo(KonsungConfig.FACTORY_MAIN_PACKAGE)) {
            return "";
        }
        ContentResolver resolver = MyApplication.getInstance().getContentResolver();
        Cursor cursor = resolver.query(DEVICE_CODE_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndex("path"));
                return s;
            }
            cursor.close();
        }
        return "";
    }

    /**
     * 康尚测量项项配置
     *
     * @return
     */
    public String getMeasureConfig() {
        if (!ActivityUtil.checkPackInfo(KonsungConfig.FACTORY_MAIN_PACKAGE)) {
            return "";
        }
        ContentResolver resolver = MyApplication.getInstance().getContentResolver();
        Cursor cursor = resolver.query(DEVICE_CONFIG_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndex(KEY_CONFIG));
                Logger.e(TAG, "s=" + s);
                return s;
            }
            cursor.close();
        }
        return "";
    }

    /**
     * 康尚服务器地址
     *
     * @return
     */
    public String getServerUrl() {
        if (!ActivityUtil.checkPackInfo(KonsungConfig.FACTORY_MAIN_PACKAGE)) {
            return "";
        }
        ContentResolver resolver = MyApplication.getInstance().getContentResolver();
        Cursor cursor = resolver.query(SERVICE_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndex(KEY_PATH));
                Logger.e(TAG, "s=" + s);
                return s;
            }
            cursor.close();
        }
        return "";
    }

}
