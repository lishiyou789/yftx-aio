package com.health2world.aio.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.health2world.aio.MyApplication;

/**
 * Created by lishiyou on 2019/3/25 0025.
 */

public class PackageReplaceReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // 安装
        if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
//            ToastUtil.showShort("应用程序安装");
//            Logger.i("lsy", "应用程序安装");
        }
        // 覆盖安装
        if (action.equals(Intent.ACTION_PACKAGE_REPLACED)
                || action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
            MyApplication.getInstance().setReplace(true);
        }
        // 移除
        if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
//            EventBus.getDefault().post(new MsgEvent<>(AppConfig.MSG_PACKAGE_REPLACED));
//            ToastUtil.showShort("应用程序卸载");
//            Logger.i("lsy", "应用程序卸载");
        }
    }
}
