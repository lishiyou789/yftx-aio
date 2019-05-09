package com.health2world.aio.app.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.app.setting.SettingActivity;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.common.MsgEvent;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.jpush.JPushUtil;
import com.health2world.aio.jpush.TagAliasOperatorHelper;
import com.health2world.aio.printer.PrinterConn;
import com.health2world.aio.util.DefaultTextWatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.List;

import aio.health2world.utils.DeviceUtil;
import aio.health2world.utils.SPUtils;
import aio.health2world.utils.ToastUtil;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by lishiyou on 2018/7/6 0006.
 */

public class LoginActivity extends MVPBaseActivity<LoginContract.Presenter> implements LoginContract.View,
        AccountPopupWindow.OnAccountCheckedListener {

    private ImageView ivChangeCode, ivAccountHistory, ivSeePwd, ivLoginCode, ivLogo, ivSetting,
            ivClearAccount, ivClearPwd, ivClearForgetPwd;

    private LinearLayout accountLayout, codeLayout, forgetLayout;

    private TextView tvCodeBack, tvForgetPwd, tvForgetBack;

    private Button btnLogin, btnCode, btnResetPwd;

    private EditText edAccount, edPassword, edForgetAccount, edForgetCode, edNewPassword;

    private AccountPopupWindow popupWindow;

    //密码默认不可见
    private boolean pwdType = true;
    //默认没有获取二维码
    private boolean isCodeUrl = false;
    //账号密码登录
    public static final int LOGIN_ACCOUNT = 1;
    //扫码登录
    public static final int LOGIN_CODE = 2;
    //找回密码
    public static final int LOGIN_FORGET = 3;
    //登录方式(默认账号密码)
    private int loginType = LOGIN_ACCOUNT;

    @Override
    protected LoginContract.Presenter getPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        tvCodeBack = findView(R.id.tvCodeBack);
        tvForgetPwd = findView(R.id.tvForgetPwd);
        tvForgetBack = findView(R.id.tvForgetBack);

        ivLogo = findView(R.id.ivLogo);
        ivSetting = findView(R.id.ivSetting);
        ivChangeCode = findView(R.id.ivChangeCode);
        ivSeePwd = (ImageView) findViewById(R.id.ivSeePwd);
        ivAccountHistory = findView(R.id.ivAccountHistory);
        ivLoginCode = findView(R.id.ivLoginCode);
        ivClearAccount = findView(R.id.ivClearAccount);
        ivClearPwd = findView(R.id.ivClearPwd);
        ivClearForgetPwd = findView(R.id.ivClearForgetPwd);

        btnLogin = findView(R.id.btnLogin);
        btnCode = findView(R.id.btnCode);
        btnResetPwd = findView(R.id.btnResetPwd);

        edAccount = findView(R.id.edAccount);
        edPassword = findView(R.id.edPassword);
        edForgetAccount = findView(R.id.edForgetAccount);
        edForgetCode = findView(R.id.edForgetCode);
        edNewPassword = findView(R.id.edNewPassword);

        accountLayout = findView(R.id.accountLayout);
        codeLayout = findView(R.id.codeLayout);
        forgetLayout = findView(R.id.forgetLayout);
    }

    @Override
    protected void initData() {
        JPushInterface.init(this);
        JPushUtil.setJPushAlias(TagAliasOperatorHelper.ACTION_SET, DeviceUtil.getAndroidId(this));
        EventBus.getDefault().register(this);
        popupWindow = new AccountPopupWindow(this);
        //登录状态初始化
        SPUtils.put(this, AppConfig.IS_LOGIN, false);
        //默认账号栏加载最近登录的一个账号信息  0428更新：不再使用数据库保存账号密码
        {
            //升级软件后找不到就用SP保存的
            //0423 本身就是String，不需要转换
            edAccount.setText((String) SPUtils.get(MyApplication.getInstance(), AppConfig.DOCTOR_PHONE, ""));
            edPassword.setText((String) SPUtils.get(MyApplication.getInstance(), AppConfig.DOCTOR_PWD, ""));
            edPassword.setSelection(edPassword.getText().length());
            ivClearAccount.setVisibility(View.VISIBLE);
            ivClearPwd.setVisibility(View.VISIBLE);
        }

        if (MyApplication.getInstance().isReplcaed()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.x_tip))
                    .setMessage(mContext.getString(R.string.reboot_aio))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            MyApplication.getInstance().setReplace(false);
        }
    }

    @Override
    protected void initListener() {
        ivChangeCode.setOnClickListener(this);
        ivSeePwd.setOnClickListener(this);
        ivLoginCode.setOnClickListener(this);
        ivAccountHistory.setOnClickListener(this);
        ivLogo.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
        ivClearAccount.setOnClickListener(this);
        ivClearPwd.setOnClickListener(this);
        ivClearForgetPwd.setOnClickListener(this);

        tvCodeBack.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
        tvForgetBack.setOnClickListener(this);

        btnLogin.setOnClickListener(this);
        btnCode.setOnClickListener(this);
        btnResetPwd.setOnClickListener(this);

        popupWindow.setOnAccountCheckedListener(this);

        edAccount.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                ivClearAccount.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
            }
        });
        edPassword.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                ivClearPwd.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
            }
        });

        edForgetAccount.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                ivClearForgetPwd.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        countDownTimer.cancel();
        countDownTimer = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLogo:
//                if (AppConfig.isDebug)
//                    DBManager.getInstance().exportDb();

                if (AppConfig.isDebug)
                    PrinterConn.send();
                break;
            case R.id.ivSetting:
                Intent settingIntent = new Intent(this, SettingActivity.class);
                settingIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(settingIntent);
                break;
            case R.id.ivChangeCode://扫码登录
                loginType = LOGIN_CODE;
                changeLoginView();
                if (!isCodeUrl)
                    mPresenter.QRLogin();
                break;
            case R.id.ivLoginCode://二维码刷新
                JPushUtil.setJPushAlias(TagAliasOperatorHelper.ACTION_SET, DeviceUtil.getAndroidId(this));
                mPresenter.QRLogin();
                break;
            case R.id.tvCodeBack://返回账号登录
            case R.id.tvForgetBack:
                loginType = LOGIN_ACCOUNT;
                changeLoginView();
                break;
            case R.id.tvForgetPwd://忘记密码
                loginType = LOGIN_FORGET;
                changeLoginView();
                break;
            case R.id.btnLogin://登录
                if (TextUtils.isEmpty(edAccount.getText().toString().trim())) {
                    ToastUtil.showLong(MyApplication.getInstance().getString(R.string.account_not_null));
                    return;
                }
                if (TextUtils.isEmpty(edPassword.getText().toString().trim())) {
                    ToastUtil.showLong(MyApplication.getInstance().getString(R.string.pwd_not_null));
                    return;
                }
                mPresenter.login(false, edAccount.getText().toString().trim(),
                        edPassword.getText().toString().trim());
                break;
            case R.id.btnCode://获取手机验证码
                String phone = edForgetAccount.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || !phone.startsWith("1") || phone.length() != 11) {
                    ToastUtil.showShort("手机号格式有误");
                    return;
                }
                mPresenter.securityCode(edForgetAccount.getText().toString().trim());
                break;
            case R.id.btnResetPwd://重置密码
                if (edNewPassword.getText().length() < 6 || edNewPassword.getText().length() > 16) {
                    ToastUtil.showShort("请输入6-16位字符");
                    return;
                }
                mPresenter.resetPassword(edForgetAccount.getText().toString().trim(),
                        edForgetCode.getText().toString().trim(),
                        edNewPassword.getText().toString().trim());
                break;
            case R.id.ivAccountHistory://显示历史登录的账号
                popupWindow.showPopupWindow(edAccount);
                break;
            case R.id.ivSeePwd://显示密码
                changePwdEditTextView();
                break;
            case R.id.ivClearAccount:
                edAccount.getText().clear();
                edPassword.getText().clear();
                break;
            case R.id.ivClearPwd:
                edPassword.getText().clear();
                break;
            case R.id.ivClearForgetPwd:
                edForgetAccount.getText().clear();
                break;
        }
    }

    @Override
    public void accountChecked(DoctorBean doctor) {
        edAccount.setText(doctor.getAccount());
        edPassword.setText(doctor.getPassword());
        edAccount.setSelection(edAccount.getText().length());
    }

    @Override
    public void accountDelete(DoctorBean doctor) {
        if (edAccount.getText().toString().trim().equals(doctor.getAccount())) {
            edAccount.getText().clear();
            edPassword.getText().clear();
        }
    }

    @Override
    public void loginSuccess(DoctorBean doctor) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void securityCodeSuccess() {
        btnCode.setEnabled(false);
        countDownTimer.start();
    }

    @Override
    public void QRUrlSuccess(String url) {
        isCodeUrl = true;
        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_default_image)
                .error(R.mipmap.ic_default_image)
                .into(ivLoginCode);
    }

    @Override
    public void resetPwdSuccess() {
        ToastUtil.showShort(getString(R.string.action_success));
        edForgetCode.getText().clear();
        edNewPassword.getText().clear();
        tvForgetBack.performClick();
    }

    //扫码之后回调方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(MsgEvent event) {
        if (event.getAction() == AppConfig.MSG_ACTION_SCAN) {
            DoctorBean info = (DoctorBean) event.getT();
            mPresenter.login(true, info.getAccount(), info.getPassword());
        }
    }

    //计时器
    private CountDownTimer countDownTimer = new CountDownTimer(59000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btnCode.setText(millisUntilFinished / 1000 + "秒后获取");
        }

        @Override
        public void onFinish() {
            btnCode.setEnabled(true);
            btnCode.setText(getString(R.string.login_code_get));
        }
    };

    //切换登录界面
    private void changeLoginView() {
        if (loginType == LOGIN_ACCOUNT) {
            ivChangeCode.setVisibility(View.VISIBLE);
            accountLayout.setVisibility(View.VISIBLE);
            codeLayout.setVisibility(View.GONE);
            forgetLayout.setVisibility(View.GONE);
        }
        if (loginType == LOGIN_CODE) {
            ivChangeCode.setVisibility(View.INVISIBLE);
            accountLayout.setVisibility(View.GONE);
            codeLayout.setVisibility(View.VISIBLE);
            forgetLayout.setVisibility(View.GONE);
        }
        if (loginType == LOGIN_FORGET) {
            ivChangeCode.setVisibility(View.INVISIBLE);
            accountLayout.setVisibility(View.GONE);
            codeLayout.setVisibility(View.GONE);
            forgetLayout.setVisibility(View.VISIBLE);
        }
    }

    //密码输入框 明文和密码显示的切换
    private void changePwdEditTextView() {
        if (edPassword.length() == 0) {
            return;
        }
        pwdType = !pwdType;
        if (pwdType) {
            edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        edPassword.setSelection(edPassword.length());
    }

}
