package com.health2world.aio.app.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.health2world.aio.BuildConfig;
import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.bean.SettingBean;
import com.health2world.aio.bean.UpgradeInfo;
import com.health2world.aio.ble.BleDeviceActivity;
import com.health2world.aio.common.DataServer;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.config.KonsungConfig;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.printer.DeviceManagerConnectActivity;
import com.health2world.aio.util.ActivityUtil;
import com.health2world.aio.util.Logger;
import com.health2world.aio.util.TitleBarUtil;
import com.health2world.aio.view.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.http.subscriber.DownloadStatus;
import aio.health2world.utils.AppUtils;
import aio.health2world.utils.SPUtils;
import aio.health2world.utils.ToastUtil;
import aio.health2world.view.MyProgressDialog;

import static com.health2world.aio.config.AppConfig.FACTORY_MAINTENANCE;

/**
 * Created by lishiyou on 2018/7/19 0019.
 */

public class SettingActivity extends MVPBaseActivity<SettingContract.Presenter> implements SettingContract.View,
        BaseQuickAdapter.OnItemClickListener {

    //软件更新
    public static final int ITEM_SOFTWARE_UPDATE = 0;
    //公卫更新
    public static final int ITEM_PUBLIC_UPDATE = 1;
    //测量库更新
    public static final int ITEM_DEVICE_UPDATE = 2;
    //设备编码
    public static final int ITEM_DEVICE_NO = 3;
    //测量模式
    public static final int ITEM_MEASURE_MODE = 4;
    //应用程序服务器地址
    public static final int ITEM_APP_SERVER_URL = 5;
    //公卫服务器地址
    public static final int ITEM_PUBLIC_SERVER_URL = 6;
    //测量项配置
    public static final int ITEM_MEASURE_CONFIG = 7;
    //设备驱动配置
    public static final int ITEM_DEVICE_CONFIG = 8;
    //文件管理
    public static final int ITEM_FILE_MANAGER = 9;
    //厂家维护
    public static final int ITEM_FACTORY_MAIN = 10;
    //系统设置
    public static final int ITEM_SYSTEM_SETTING = 11;

    public static final int ITEM_SYSTEM_BLUETHOOTH = 12;
    //打印机连接
    public static final int ITEM_PRINTER = 13;
    //DeviceManager版本升级
    public static final int ITEM_DEVICEMANAGER_UPDATE = 14;

    public static final int INSTALL_APK_REQUEST = 0x86;

    private int mAction;

    private TitleBar titleBar;

    private DoctorBean doctor;

    private MyProgressDialog progressDialog;

    private RecyclerView recyclerView;

    private AppSettingAdapter settingAdapter;

    private List<SettingBean> settingList = new ArrayList<>();

    private String deviceNo;

    //数据自动上传 软件自动更新
    @Override
    protected SettingContract.Presenter getPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        titleBar = findView(R.id.titleBar);
        TitleBarUtil.setAttr(this, getString(R.string.setting_more), "", titleBar);
        recyclerView = findView(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {
        deviceNo = MyApplication.getInstance().getDeviceCode();
        doctor = DBManager.getInstance().getCurrentDoctor();
        addSettingsItem();
        settingAdapter = new AppSettingAdapter(settingList);
        recyclerView.setAdapter(settingAdapter);
    }

    private void addSettingsItem() {
        settingList.clear();
        settingList.add(new SettingBean(ITEM_SOFTWARE_UPDATE, "应用更新", R.mipmap.setting_app_update,
                "V" + BuildConfig.VERSION_NAME + "_" + BuildConfig.GIT_VERSION));

        settingList.add(new SettingBean(ITEM_PUBLIC_UPDATE, "公卫更新", R.mipmap.setting_health_update,
                ActivityUtil.getAppInfo(KonsungConfig.PUBLIC_HEALTH_PACKAGE) == null ? "" :
                        "V" + ActivityUtil.getAppInfo(KonsungConfig.PUBLIC_HEALTH_PACKAGE).versionName));

        settingList.add(new SettingBean(ITEM_DEVICE_UPDATE, "测量库更新", R.mipmap.setting_app_update,
                ActivityUtil.getAppInfo(KonsungConfig.APPDEVICE_PACKAGE) == null ? "" :
                        "V" + ActivityUtil.getAppInfo(KonsungConfig.APPDEVICE_PACKAGE).versionName));

        settingList.add(new SettingBean(ITEM_DEVICEMANAGER_UPDATE, "设备管理更新", R.mipmap.setting_app_update,
                ActivityUtil.getAppInfo(KonsungConfig.DEVICE_MANAGER_PACKAGE) == null ? "" :
                        "V" + ActivityUtil.getAppInfo(KonsungConfig.DEVICE_MANAGER_PACKAGE).versionCode));

        settingList.add(new SettingBean(ITEM_APP_SERVER_URL, "应用服务器地址", R.mipmap.setting_server_address,
                MyApplication.getInstance().getServerUrl()));

        settingList.add(new SettingBean(ITEM_PUBLIC_SERVER_URL, "公卫服务器地址", R.mipmap.setting_server_health,
                MyApplication.getInstance().getPublicHealthUrl()));

        settingList.add(new SettingBean(ITEM_DEVICE_NO, "设备编号", R.mipmap.setting_device_code,
                MyApplication.getInstance().getDeviceCode()));
//
//        settingList.add(new SettingBean(ITEM_MEASURE_CONFIG, "测量项配置", R.mipmap.setting_measure_config, ""));

        settingList.add(new SettingBean(ITEM_DEVICE_CONFIG, "血糖设备选择", R.mipmap.setting_switch_measure, DataServer.getDeviceName()));

//        settingList.add(new SettingBean(ITEM_MEASURE_MODE, "开启家医测量", R.mipmap.setting_switch_measure, ""));

        //5.1以上版本才支持厂家维护软件
        if (TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_5_1))
            settingList.add(new SettingBean(ITEM_FACTORY_MAIN, "厂家维护", R.mipmap.setting_maitain_icon, ""));

        if (AppConfig.isDebug)
            settingList.add(new SettingBean(ITEM_FILE_MANAGER, "文件管理", R.mipmap.setting_file_manager, ""));

        settingList.add(new SettingBean(ITEM_SYSTEM_SETTING, "系统设置", R.mipmap.setting_system_icon, ""));

//        settingList.add(new SettingBean(ITEM_SYSTEM_BLUETHOOTH, "蓝牙设备管理", R.mipmap.setting_system_bluethooth, ""));

        settingList.add(new SettingBean(ITEM_PRINTER, "打印机连接", R.mipmap.icon_printer, ""));
    }

    @Override
    protected void initListener() {
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        settingAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSTALL_APK_REQUEST) {
            ToastUtil.showLong("安装完成请重启一体机");
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        String value = data.getStringExtra("data");
        settingList.get(requestCode).setValue(value);
        settingAdapter.notifyItemChanged(requestCode);
        //设备编号
        if (mAction == ITEM_DEVICE_NO) {
            SPUtils.put(this, AppConfig.DEVICE_CODE, value);
        }
        //应用服务器地址
        if (mAction == ITEM_APP_SERVER_URL) {
            SPUtils.put(this, AppConfig.SERVER_URL, value);
        }
        //公卫服务器地址
        if (mAction == ITEM_PUBLIC_SERVER_URL) {
            SPUtils.put(this, KonsungConfig.PUBLIC_HEALTH_URL, value);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_4_4_2))
            return;
        if (!deviceNo.equals(MyApplication.getInstance().getDeviceCode())) {
            settingList.get(ITEM_DEVICE_NO).setValue(MyApplication.getInstance().getDeviceCode());
            settingAdapter.notifyItemChanged(ITEM_DEVICE_NO);
            deviceNo = MyApplication.getInstance().getDeviceCode();
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        SettingBean settingBean = (SettingBean) adapter.getItem(position);
        mAction = settingBean.getAction();
        //软件更新1:一体机 2:医生 3:患者家属 4公卫App 6-85测量库 7-86测量库 8-DeviceManager
        if (mAction == ITEM_SOFTWARE_UPDATE || mAction == ITEM_DEVICE_UPDATE || mAction == ITEM_PUBLIC_UPDATE
                || mAction == ITEM_DEVICEMANAGER_UPDATE) {

            int appType = -1;

            if (mAction == ITEM_SOFTWARE_UPDATE) {
                appType = AppConfig.APP_SOFTWARE;
            } else if (mAction == ITEM_PUBLIC_UPDATE) {
                appType = AppConfig.APP_PUBLIC_HEALTH;
            } else if (mAction == ITEM_DEVICE_UPDATE) {
                if (TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_4_4_2))
                    appType = AppConfig.APP_DEVICE_85;
                else
                    appType = AppConfig.APP_DEVICE_86;
            } else if (mAction == ITEM_DEVICEMANAGER_UPDATE) {
                appType = AppConfig.APP_DEVICE_MANAGER;
            }
            //公卫程序升级需要在登录之后进行 各地区版本不一样 所以不登录程序无法识别地区
            if (mAction == ITEM_PUBLIC_UPDATE && !
                    (boolean) SPUtils.get(MyApplication.getInstance(), AppConfig.IS_LOGIN, false)) {
                ToastUtil.showShort("请登录程序之后再进行更新操作");
                return;
            }
            mPresenter.validateVersion(appType, doctor == null ? -1 : doctor.getInsId());
        }

        //设备编号 因为4.4.2不支持安装厂家维护 所以让用户手动填写设备编码
        if (mAction == ITEM_DEVICE_NO && TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_4_4_2)) {
            Intent intent = new Intent(this, InputSettingActivity.class);
            intent.putExtra("settingBean", settingBean);
            intent.putExtra("isUrl", false);
            startActivityForResult(intent, position);
        }

        //服务器地址
        if ((mAction == ITEM_APP_SERVER_URL || mAction == ITEM_PUBLIC_SERVER_URL) && AppConfig.isDebug) {
            Intent intent = new Intent(this, InputSettingActivity.class);
            intent.putExtra("settingBean", settingBean);
            intent.putExtra("isUrl", true);
            startActivityForResult(intent, position);
        }
        //测量项配置
        if (mAction == ITEM_MEASURE_CONFIG)
            startActivity(new Intent(this, MeasureSettingActivity.class));

        //血糖设备选择
        if (mAction == ITEM_DEVICE_CONFIG) {
            Intent intent = new Intent(this, DeviceSelectActivity.class);
            startActivityForResult(intent, position);
        }


        //厂家维护
        if (mAction == ITEM_FACTORY_MAIN)
            ActivityUtil.enterFactoryMaintain(this, 0x01);

        //文件管理
        if (mAction == ITEM_FILE_MANAGER)
            ActivityUtil.enterFileManager(this, 0x01);

        //程序设置
        if (mAction == ITEM_SYSTEM_SETTING) {
            //以厂家维护的方式启动系统设置
            Intent _intent = new Intent(Settings.ACTION_SETTINGS);
            //true原生态系统设置，false阉割版
            _intent.putExtra(FACTORY_MAINTENANCE, AppConfig.isDebug);
            startActivity(_intent);
        }

        //蓝牙管理界面
        if (mAction == ITEM_SYSTEM_BLUETHOOTH)
            startActivity(new Intent(this, BleDeviceActivity.class));

        //打印机连接
        if (mAction == ITEM_PRINTER) {
            startActivity(new Intent(this, DeviceManagerConnectActivity.class));
        }


    }


    @Override
    public void validateSuccess(final int appType, final UpgradeInfo info) {
        //强制升级
        if (info.getEnforceFlag() == 1) {
            if (progressDialog == null) {
                progressDialog = new MyProgressDialog(SettingActivity.this);
                progressDialog.init(100);
                progressDialog.show();
            }
            mPresenter.downLoad(appType, info.getUrl(),
                    info.getCurrentVersion() == 0 ? info.getVersionName() : info.getCurrentVersion() + "");
        } else {
            String tips = info.getUpdateContent();
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.update_tips))
                    .setMessage(TextUtils.isEmpty(tips) ? getString(R.string.new_version) : tips.replace("&", "\r\n"))
                    .setNegativeButton(getString(R.string.update_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(getString(R.string.update_now), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (progressDialog == null) {
                                progressDialog = new MyProgressDialog(SettingActivity.this);
                                progressDialog.init(100);
                                progressDialog.show();
                            }
                            mPresenter.downLoad(appType, info.getUrl(),
                                    info.getCurrentVersion() == 0 ? info.getVersionName() : info.getCurrentVersion() + "");
                        }
                    })
                    .create()
                    .show();
        }
    }

    @Override
    public void loadProgress(DownloadStatus status) {
        int percent = (int) (status.getDownloadSize() * 100 / status.getTotalSize());
        progressDialog.update(percent, status.getPercent());
    }

    @Override
    public void loadCompleted(String installApkPath) {
        progressDialog.dismiss();
        progressDialog = null;
        AppUtils.installApk(SettingActivity.this, installApkPath, INSTALL_APK_REQUEST);
    }

    @Override
    public void loadError(String apkPath) {
        progressDialog.dismiss();
        progressDialog = null;
        File file = new File(apkPath);
        if (file.exists())
            file.delete();
        ToastUtil.showLong("下载失败，请重新下载！");
    }


    private void resetAllSetting() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.action_tips))
                .setMessage(getString(R.string.reset_setting_tips))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SPUtils.put(SettingActivity.this, AppConfig.DEVICE_CODE, AppConfig.DEFAULT_DEVICE_CODE);
                        SPUtils.put(SettingActivity.this, AppConfig.MEASURE_MODE, true);
                        SPUtils.put(SettingActivity.this, AppConfig.SERVER_URL, AppConfig.DEFAULT_SERVER_URL);
                        SPUtils.put(SettingActivity.this, KonsungConfig.PUBLIC_HEALTH_URL, KonsungConfig.DEFAULT_PUBLIC_HEALTH_URL);
                        SPUtils.put(SettingActivity.this, AppConfig.MEASURE_CONFIG, AppConfig.DEFAULT_MEASURE_CONFIG);
                        addSettingsItem();
                        settingAdapter.notifyDataSetChanged();
                    }
                })
                .create()
                .show();
    }

}
