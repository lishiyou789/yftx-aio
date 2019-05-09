package com.health2world.aio.app.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.bean.SettingBean;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.health2world.aio.printer.PrinterConn;
import com.health2world.aio.util.ActivityUtil;
import com.health2world.aio.util.DefaultTextWatcher;
import com.health2world.aio.util.IdCardUtil;
import com.konsung.util.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.NetworkUtil;
import aio.health2world.utils.ToastUtil;

/**
 * 输入类型的设置
 * Created by lishiyou on 2018/7/19 0019.
 */

public class InputSettingActivity extends Activity implements View.OnClickListener {

    public static final String SETTING_BEAN = "settingBean";

    private TextView tvTitle, tvCancel, tvSure, tvTest;

    private EditText edUrl;

    private SettingBean settingBean;

    private float max = 999.9f;
    private float min = 0.0f;

    //是否是网址
    private boolean isUrl = false;
    //是否为身份证
    private boolean isIdCard = false;
    //身高体重
    private boolean isHWeight = false;
    //IP
    private boolean isIP = false;
    private boolean isAvaliableIP = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_input_setting);
        initView();
        initData();
        addListener();
    }


    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        tvCancel = findViewById(R.id.tvCancel);
        tvSure = findViewById(R.id.tvSure);
        tvTest = findViewById(R.id.tvTest);
        edUrl = findViewById(R.id.edUrl);
    }

    private void initData() {
        if (getIntent().hasExtra(SETTING_BEAN)) {
            settingBean = (SettingBean) getIntent().getSerializableExtra(SETTING_BEAN);
        }
        if (getIntent().hasExtra("isUrl")) {
            isUrl = getIntent().getBooleanExtra("isUrl", false);
        }
        if (getIntent().hasExtra("isIdCard")) {
            isIdCard = getIntent().getBooleanExtra("isIdCard", false);
        }
        if (getIntent().hasExtra("isIP")) {
            isIP = getIntent().getBooleanExtra("isIP", false);
        }

        if (getIntent().hasExtra("isHWeight"))
            isHWeight = getIntent().getBooleanExtra("isHWeight", false);

        if (TextUtils.isEmpty(settingBean.getValue()) && isUrl)
            edUrl.setText("http://");
        else
            edUrl.setText(settingBean.getValue());

        if (isUrl)
            tvTest.setVisibility(View.VISIBLE);
        if (isIP)
            edUrl.setText("192.168.");

        if (isUrl || isIP)
            tvTest.setVisibility(View.VISIBLE);
        else
            tvTest.setVisibility(View.GONE);

        if (isIdCard) {
            edUrl.setHint("输入身份证号码");
            edUrl.setHintTextColor(getResources().getColor(R.color.black9));
            edUrl.setFilters(new InputFilter[]{typeFilter, new InputFilter.LengthFilter(18)});
        }

        if (isHWeight) {
            //0430修改为可以输入小数
            edUrl.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
            edUrl.setFilters(new InputFilter[]{typeFilter1, new InputFilter.LengthFilter(5)});
            edUrl.addTextChangedListener(new DefaultTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (start >= 0) {//从一输入就开始判断，
                        if (min != -1 && max != -1) {
                            try {
                                float num = Float.parseFloat(s.toString());
                                //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                                if (num > max) {
                                    //如果大于max，则内容为max
                                    edUrl.setText(String.valueOf(max));
                                } else if (num < min) {
                                    //如果小于min,则内容为min
                                    edUrl.setText(String.valueOf(min));
                                }
                            } catch (NumberFormatException e) {
                                Logger.e("lsy", "NumberFormatException");
                            }
                            //editText中的数字在max和min之间，则不做处理，正常显示即可。
                            return;
                        }
                    }
                }
            });
        }
        tvTitle.setText(settingBean.getName());
        edUrl.setSelection(edUrl.getText().length());
    }


    private void addListener() {
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        tvTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tvCancel:
                intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                ActivityUtil.finishActivity(this);
                break;
            case R.id.tvTest:
                checkUrl(edUrl.getText().toString().trim());
                break;
            case R.id.tvSure:
                String url = edUrl.getText().toString().trim();
                if (TextUtils.isEmpty(url))
                    return;
                //----------------------------------
                if (isIdCard) {
                    IdCardUtil util = new IdCardUtil(url);
                    if (util.isCorrect() != 0) {
                        ToastUtil.showShort("身份证格式有误");
                        return;
                    }
                    validateIdCard(url);
                } else if (isUrl) {
                    //--------------------------------------------
                    intent = new Intent();
                    intent.putExtra("data", url);
                    //--------------------------------------------
                    ToastUtil.showShort("设置成功 请重新登录程序");
                    setResult(RESULT_OK, intent);
                    ActivityUtil.finishActivity(this);
                    //----------IP-----------------
                } else if (isIP) {
                    intent = new Intent();
                    intent.putExtra("data", url);
                    checkUrl(url);
                    if (isAvaliableIP) {
                        setResult(RESULT_OK, intent);
                        ActivityUtil.finishActivity(this);
                    } else {
                        ToastUtil.showShort("链接访问异常");
                        setResult(RESULT_CANCELED, intent);
                        ActivityUtil.finishActivity(this);
                    }
                } else {
                    intent = new Intent();
                    intent.putExtra("data", url);
                    setResult(RESULT_OK, intent);
                    ActivityUtil.finishActivity(this);
                }

                break;
        }
    }

    /**
     * 检查链接是否可以访问
     */
    private void checkUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            ToastUtil.showShort("无效的链接地址");
            return;
        }
        if (!NetworkUtil.isConnected(this)) {
            ToastUtil.showShort("无网络连接");
            return;
        }

        //检查局域网的客户端
        if (isIP) {
            try {
                PrinterConn.pingnscanPrinter(url, AppConfig.PRINTER_PORT);
                Thread.sleep(500);
                if (MyApplication.isNetPrinterConnnect) {
                    ToastUtil.showShort("IP访问正常");
                    isAvaliableIP = true;
                } else {
                    ToastUtil.showShort("IP访问异常");
                }
            } catch (InterruptedException pE) {
                pE.printStackTrace();
            }
        } else if (isUrl) {
            if (url.startsWith("http://")) {
                url = url.replace("http://", "");
                if (url.contains(":")) {
                    url = url.split(":")[0];
                }
                NetworkUtil.isUrlAvailable(url, new Comparable<Boolean>() {
                    @Override
                    public int compareTo(@NonNull Boolean o) {
                        if (o)
                            ToastUtil.showShort("链接访问正常");
                        else
                            ToastUtil.showShort("链接访问异常");
                        return 0;
                    }
                });
            } else {
                ToastUtil.showShort("无效的链接地址");
            }
        }
    }

    private void validateIdCard(final String cardNo) {
        ApiRequest.validateIdCard(cardNo, 0, new HttpSubscriber() {
            @Override
            public void onNext(HttpResult result) {
                super.onNext(result);
                //身份证号码已存在
                if (result.code.equals("007")) {
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("data", cardNo);
                    setResult(RESULT_OK, intent);
                    ActivityUtil.finishActivity(InputSettingActivity.this);
                }
            }
        });
    }

    private InputFilter typeFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern p = Pattern.compile("[xX0-9]");
            Matcher m = p.matcher(source.toString());
            if (!m.matches()) return "";
            return null;
        }
    };

    private InputFilter typeFilter1 = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern p = Pattern.compile("[.0-9]");
            Matcher m = p.matcher(source.toString());
            if (!m.matches()) return "";
            return null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
