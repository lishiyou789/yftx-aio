package com.health2world.aio.app.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.clinic.ClinicOneActivity;
import com.health2world.aio.app.health.HealthManagerActivity;
import com.health2world.aio.app.health.sign.SignActivity;
import com.health2world.aio.app.history.HistoryRecordActivity;
import com.health2world.aio.app.resident.FamilyMemberMainFragment;
import com.health2world.aio.app.resident.add.NewResidentActivity;
import com.health2world.aio.app.resident.view.ResidentQRCodeDialog;
import com.health2world.aio.app.search.RSearchActivity;
import com.health2world.aio.app.setting.SettingActivity;
import com.health2world.aio.app.task.TaskFragment;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.bean.NetStatus;
import com.health2world.aio.bean.UpgradeInfo;
import com.health2world.aio.common.MsgEvent;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.printer.PrintUtils;
import com.health2world.aio.service.MeasureService;
import com.health2world.aio.util.IdCardUtil;
import com.konsung.bean.ResidentBean;
import com.konsung.listen.IdCardListen;
import com.konsung.util.MeasureUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import aio.health2world.http.subscriber.DownloadStatus;
import aio.health2world.utils.AppUtils;
import aio.health2world.utils.ToastUtil;
import aio.health2world.view.MyProgressDialog;

public class MainActivity extends MVPBaseActivity<MainContract.Presenter>
        implements MainContract.View, TabChangedListener, IdCardListen {

    public static final int REQUEST_CODE_ADD_RESIDENT = 0x02;
    public static final int REQUEST_CODE_SEARCH_RESIDENT = 0x03;
    public static final int REQUEST_CODE_CLINIC = 0x04;
    public static final String KEY_RESIDENT = "resident";
    public static final String KEY_CODE_URL = "resident_url";
    //头部布局View
    private HomeTitleView titleView;
    //左侧TabView
    private HomeTabView tabView;
    private RMessageView messageView;

    private FragmentManager fragmentManager;
    //任务中心
    private TaskFragment taskFragment;
    //门诊检查
//    private ClinicFragment clinicFragment;
    //家庭成员界面
    private FamilyMemberMainFragment familyMemberFragment;
    //二维码弹出框
    private ResidentQRCodeDialog qrCodeDialog;
    //主页当前患者信息
    private ResidentBean resident;

    private DoctorBean doctor;
    private MyProgressDialog progressDialog;

    public ResidentBean getResident() {
        return resident;
    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        titleView = findView(R.id.titleView);
        tabView = findView(R.id.tabView);
        messageView = findView(R.id.messageView);
    }

    @Override
    protected void initData() {
        fragmentManager = getSupportFragmentManager();
        onTabChecked(HomeTabView.TAB_INDEX_TASK);
        doctor = DBManager.getInstance().getCurrentDoctor();
        //应用程序检测更新
        mPresenter.validateVersion(AppConfig.APP_SOFTWARE, doctor == null ? -1 : doctor.getInsId());


        //登录后查找注册热敏
        new Thread(new Runnable() {
            @Override
            public void run() {
                UsbDevice _usbDevice = null;
                UsbManager mUsbManager = (UsbManager) MyApplication.getInstance().getSystemService(USB_SERVICE);
                HashMap<String, UsbDevice> _map = null;
                //添加判空
                if (mUsbManager != null) {
                    _map = mUsbManager.getDeviceList();
                }
                if (_map != null && !_map.isEmpty()) {
                    for (Map.Entry<String, UsbDevice> _entry : _map.entrySet()) {
                        if ((short) _entry.getValue().getVendorId() == AppConfig.USB_PRINT_VID) {
                            _usbDevice = _entry.getValue();
                            break;
                        }
                    }
                }
                if (_usbDevice != null)
                    PrintUtils.registerPrinter(_usbDevice);
            }
        }).start();
    }

    @Override
    protected void initListener() {
        tabView.setOnTabChangedListener(this);
        messageView.setOnTabChangedListener(this);
        titleView.setOnTabChangedListener(this);
        MeasureUtils.setIdCardListen(this);
    }

    //首页跳转页面回调事件
    @Override
    public void onTabChecked(int position) {
        //需要切换fragment的页面
        boolean isChange =
                position == HomeTabView.TAB_INDEX_TASK ||
                        position == RMessageView.TAB_INDEX_FAMILY;
        FragmentTransaction transaction = null;
        if (isChange) {
            if (fragmentManager == null)
                fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            hideAllFragments(transaction);
        }
        switch (position) {
            case HomeTabView.TAB_INDEX_CLINIC://门诊检查
//                messageView.setChecked(false);
//                if (clinicFragment == null) {
//                    clinicFragment = new ClinicFragment();
//                    transaction.add(R.id.container, clinicFragment);
//                } else {
//                    transaction.show(clinicFragment);
//                }
                Intent clinicIntent = new Intent(this, ClinicOneActivity.class);
                //手动打开门诊检查的场景会有复用activity的情况
                clinicIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                clinicIntent.putExtra(KEY_RESIDENT, resident);
                startActivityForResult(clinicIntent, REQUEST_CODE_CLINIC);
                break;

            case HomeTabView.TAB_INDEX_TASK://任务中心
                messageView.setChecked(false);
                if (taskFragment == null) {
                    taskFragment = new TaskFragment();
                    transaction.add(R.id.container, taskFragment);
                } else {
                    transaction.show(taskFragment);
                }
                break;

            case RMessageView.TAB_INDEX_FAMILY://家庭成员资料界面
                if (resident == null) {
                    ToastUtil.showShort(mContext.getString(R.string.set_current_resident));
                    Intent selectResident = new Intent(this, RSearchActivity.class);
                    selectResident.putExtra("flag", 1);
                    startActivityForResult(selectResident, REQUEST_CODE_SEARCH_RESIDENT);
                } else {
                    messageView.setChecked(true);
                    tabView.resetAllView();
                    if (familyMemberFragment == null) {
                        familyMemberFragment = new FamilyMemberMainFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(KEY_RESIDENT, resident);
                        familyMemberFragment.setArguments(bundle);
                        transaction.add(R.id.container, familyMemberFragment);
                    } else {
                        transaction.show(familyMemberFragment);
                    }
                }
                break;
            case HomeTabView.TAB_INDEX_HEALTH://健康管理
                Intent intent = new Intent(this, HealthManagerActivity.class);
                intent.putExtra(MainActivity.KEY_RESIDENT, resident);
                startActivity(intent);
                break;

            case RMessageView.TAB_INDEX_HISTORY://历史记录
                if (resident == null) {
                    ToastUtil.showShort(mContext.getString(R.string.set_current_resident));
                    return;
                }
                Intent historyIntent = new Intent(this, HistoryRecordActivity.class);
                historyIntent.putExtra(KEY_RESIDENT, resident);
                startActivity(historyIntent);
                break;

            case HomeTabView.TAB_INDEX_DOCTOR://家庭公卫
                Intent intentSign = new Intent(this, SignActivity.class);
                intentSign.putExtra(KEY_RESIDENT, resident);
                startActivity(intentSign);
                break;

            case HomeTitleView.TAB_INDEX_ADD_RESIDENT://添加居民
                startActivityForResult(new Intent(this, NewResidentActivity.class), REQUEST_CODE_ADD_RESIDENT);
                break;

            case HomeTitleView.TAB_INDEX_SEARCH://居民搜索
                Intent searchIntent = new Intent(this, RSearchActivity.class);
                searchIntent.putExtra("flag", 1);
                startActivityForResult(searchIntent, REQUEST_CODE_SEARCH_RESIDENT);
                break;

            default:
                break;
        }
        if (isChange)
            transaction.commitAllowingStateLoss();
    }

    //更新设备的当前电量
    @Override
    protected void batteryChanged(int current, int percent, int status) {
        super.batteryChanged(current, percent, status);
        titleView.getBatteryView().setPower(current);
    }

    //更新当前时间
    @Override
    protected void timeChanged(int hour, int min) {
        super.timeChanged(hour, min);
        titleView.getTvTime().setText(hour + ":" + (min < 10 ? "0" + min : min));
    }

    //蓝牙变化
    @Override
    protected void blueToothChanged(int blueState) {
        super.blueToothChanged(blueState);
        titleView.setBlueToothStatus(blueState);
    }

    //wifi状态有变化
    @Override
    protected void wifiLevelChanged(NetStatus netStatus) {
        super.wifiLevelChanged(netStatus);
        titleView.setWifiStatus(netStatus);
    }

    //网络状态有变化
    @Override
    protected void netWorkChanged(int netType) {
        super.blueToothChanged(netType);
        titleView.setNetworkStatus(netType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null && requestCode == REQUEST_CODE_SEARCH_RESIDENT) {
            onTabChecked(HomeTabView.TAB_INDEX_TASK);
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        //添加居民成功
        if (requestCode == REQUEST_CODE_ADD_RESIDENT) {
            ResidentBean bean = (ResidentBean) data.getSerializableExtra(KEY_RESIDENT);
            setResident(true, bean);
            String codeUrl = data.getStringExtra(KEY_CODE_URL);
            if (!TextUtils.isEmpty(codeUrl)) {
                showQRCodeDialog(bean, codeUrl);
            }
        }
        //居民选择
        if (requestCode == REQUEST_CODE_SEARCH_RESIDENT) {
            ResidentBean residentBean = (ResidentBean) data.getSerializableExtra(KEY_RESIDENT);
            setResident(true, residentBean);
            onTabChecked(RMessageView.TAB_INDEX_FAMILY);
        }

        //门诊检查 后回到任务中心
        if (requestCode == REQUEST_CODE_CLINIC) {
            onTabChecked(HomeTabView.TAB_INDEX_TASK);
        }

    }

    public void setResident(boolean chaneView, ResidentBean resident) {
        boolean isFlag = (this.resident == null || this.resident.getPatientId().equals(resident.getPatientId()));
        this.resident = resident;
        MyApplication.getInstance().setResident(resident);
        messageView.setResident(resident);
        tabView.setResident(resident);

        resetAllFragments(isFlag, chaneView);
        //当前没有居民或者居民没改变
        if (!isFlag && chaneView) {
            onTabChecked(HomeTabView.TAB_INDEX_TASK);
            tabView.updateView(HomeTabView.TAB_INDEX_TASK);
        }
    }

    private void hideAllFragments(FragmentTransaction transaction) {
        if (taskFragment != null)
            transaction.hide(taskFragment);
//        if (clinicFragment != null)
//            transaction.hide(clinicFragment);
        if (familyMemberFragment != null)
            transaction.hide(familyMemberFragment);
    }

    /***置空与患者相关的Fragment*/
    public void resetAllFragments(boolean isFlag, boolean changeView) {
        if (MainActivity.this.isDestroyed()) {
            return;
        }
        if (isFlag) {
            return;
        }
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (familyMemberFragment != null && changeView) {
            transaction.remove(familyMemberFragment);
            familyMemberFragment = null;
        }

//        if (taskFragment != null) {
//            transaction.remove(taskFragment);
//            taskFragment = null;
//        }
        transaction.commitAllowingStateLoss();
    }

    //居民添加成功弹出二维码
    private void showQRCodeDialog(ResidentBean pResident, String url) {
        qrCodeDialog = new ResidentQRCodeDialog(this);
        qrCodeDialog.setResidentQrUrl(url);
        qrCodeDialog.setResidentCode(pResident.getResidentCode());
        qrCodeDialog.setType(0);
        qrCodeDialog.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MeasureUtils.setIdCardListen(this);
        //如果当前页面处于待办任务 并且进入公卫完善健康档案
        if (taskFragment != null && taskFragment.isVisible()) {
            taskFragment.donePublicHealthTask();
        }
        ResidentBean bean = MyApplication.getInstance().getResident();
        if (resident == null || (bean != null && !resident.getPatientId().equals(bean.getPatientId()))) {
            setResident(true, bean);
            messageView.setResident(bean);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        messageView.setResident(resident);
        tabView.setResident(resident);
    }

    //身份证读取的回调
    @Override
    public void onListen(String name, String idCard, int sex, int type, String birthday, String picture, String address) {
        if (!AppUtils.getRunningActivityName(this).equals(MainActivity.class.getName()))
            return;
        ResidentBean resident = IdCardUtil.createResident(name, idCard, address);
        mPresenter.validateIdentityCard(idCard, resident);
    }

    @Override
    public void validateCallBack(boolean success, ResidentBean resident) {
        if (success) {
            setResident(true, resident);
            onTabChecked(RMessageView.TAB_INDEX_FAMILY);
        } else {
            if (resident == null)
                return;
            ToastUtil.showLong(getString(R.string.resident_not_exist));
            Intent intent = new Intent(this, NewResidentActivity.class);
            intent.putExtra(MainActivity.KEY_RESIDENT, resident);
            startActivityForResult(intent, REQUEST_CODE_ADD_RESIDENT);
        }
    }

    //检查更新
    @Override
    public void validateSuccess(final int appType, final UpgradeInfo info) {
        //强制升级
        if (info.getEnforceFlag() == 1) {
            if (progressDialog == null) {
                progressDialog = new MyProgressDialog(MainActivity.this);
                progressDialog.init(100);
                progressDialog.show();
            }
            mPresenter.downLoad(appType, info.getUrl(), info.getCurrentVersion() == 0 ?
                    info.getVersionName() : info.getCurrentVersion() + "");
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
                                progressDialog = new MyProgressDialog(MainActivity.this);
                                progressDialog.init(100);
                                progressDialog.show();
                            }
                            mPresenter.downLoad(appType, info.getUrl(), info.getCurrentVersion() == 0
                                    ? info.getVersionName() : info.getCurrentVersion() + "");
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
        AppUtils.installApk(MainActivity.this, installApkPath, SettingActivity.INSTALL_APK_REQUEST);
    }

    @Override
    public void loadError(String installApkPath) {
        progressDialog.dismiss();
        progressDialog = null;
        ToastUtil.showLong("下载失败，请重新下载！");
        //在主线程删除未下载完成的文件
        File file = new File(installApkPath);
        if (file.exists())
            file.delete();
    }

    //扫码之后回调方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(MsgEvent event) {
        if (event.getAction() == AppConfig.MSG_ACTION_TASK) {
            if (taskFragment != null && taskFragment.isVisible())
                taskFragment.onRefresh();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeasureUtils.setIdCardListen(null);
        resident = null;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("确定退出程序吗?")
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MyApplication.getInstance().logout();
                    }
                })
                .create()
                .show();
    }
}
