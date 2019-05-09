package com.health2world.aio.app.health.protocol;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.health2world.aio.R;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.bean.SignMember;
import com.health2world.aio.bean.SignProtocolInfo;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.util.Logger;
import com.health2world.aio.util.TitleBarUtil;
import com.health2world.aio.view.TitleBar;
import com.konsung.bean.ResidentBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.utils.ToastUtil;

/**
 * 定义签约/解约，发到SignAutographActivity处理
 *
 * @author Administrator
 * @date 2018/8/1/0001.
 */

public class ProtocolActivity extends MVPBaseActivity<ProtocolContract.Presenter> implements ProtocolContract.View {

    /*
    const
     */
    public static final int REQUEST_SIGNATURE = 0x12;
    public static final int CODE_SIGN = 10;
    public static final int CODE_UN_SIGN = 11;
    public static final String KEY_TYPE = "signType";
    public static final String KEY_SIGN_MODE = "signMode";
    public static final String KEY_RESIDENT = "resident";
    public static final String KEY_DATA = "data";

    /*
    UI
     */
    private WebView webView;
    private FloatingActionButton btnConfirm;
    private TitleBar titleBar;

    /*
    data
     */
    private ResidentBean residentBean;
    private List<SignMember> mSignDataList;
    private SignProtocolInfo mSignInfo = new SignProtocolInfo();
    private String mWebViewUrl = "";
    private DoctorBean doctor;
    private int mSignType;//标识是 签约或解约

    @Override
    protected ProtocolContract.Presenter getPresenter() {
        return new ProtocolPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_protocol;
    }

    @Override
    protected void initView() {
        mContext = this;
        titleBar = findView(R.id.titleBar);
        webView = findView(R.id.webView);
        btnConfirm = findView(R.id.btnConfirm);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
    }

    @Override
    protected void initData() {
        doctor = DBManager.getInstance().getCurrentDoctor();
        mSignDataList = (List<SignMember>) getIntent().getSerializableExtra(KEY_DATA);
        residentBean = (ResidentBean) getIntent().getSerializableExtra(KEY_RESIDENT);
        mSignType = getIntent().getIntExtra(KEY_TYPE, -1);
        //获取协议
        if (mSignType == CODE_UN_SIGN) {
            TitleBarUtil.setAttr(this, getString(R.string.confirm_unsign_service), "", titleBar);
            mWebViewUrl = "file:///android_asset/protocol_un_sign.html";
        }
        if (mSignType == CODE_SIGN) {
            TitleBarUtil.setAttr(this, getString(R.string.confirm_sign_service), "", titleBar);
            mWebViewUrl = "file:///android_asset/protocol_sign.html";
        }
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(mWebViewUrl);
        webView.addJavascriptInterface(new jsHook(), "jsObj");
    }

    /**
     * 定义协议内容
     */
    class jsHook {
        @JavascriptInterface
        public String getSignData() {
            initSignPerson();
            List<SignProtocolInfo.SignMemberBean> signServicePerson = initSignServiceInfo(mSignDataList);
            mSignInfo.setSignList(signServicePerson);
            return new Gson().toJson(mSignInfo);
        }
    }


    @Override
    protected void initListener() {
        btnConfirm.setOnClickListener(this);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent(ProtocolActivity.this, SignAutographActivity.class);
        startActivityForResult(intent, REQUEST_SIGNATURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == REQUEST_SIGNATURE) {
            mPresenter.compressImage(SignAutographActivity.mImgPath);
        }
    }

    @Override
    public void compressImageSuccess(File file) {
        mPresenter.uploadImage(file);
    }

    @Override
    public void uploadImageSuccess(String imageUrl) {
        if (mSignType == CODE_SIGN) {
            mPresenter.sign(imageUrl, mSignDataList);
        } else {
            mPresenter.unSign(imageUrl, residentBean, mSignDataList);
        }
    }

    @Override
    public void uploadImageError(final String errorMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShort(errorMsg);
            }
        });
    }

    @Override
    public void actionSuccess(String data) {
        if (mSignType == CODE_SIGN)
            ToastUtil.showShort("签约成功");
        else
            ToastUtil.showShort("解约成功");
        if (mSignType == CODE_SIGN) {
            Intent intent = new Intent();
            intent.putExtra("data", data);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_OK);
        }
        finish();
    }

    private void initSignPerson() {
        mSignInfo.setDoctorName(doctor.getName());
        mSignInfo.setInstitution(doctor.getTeamName());
        mSignInfo.setDoctortelphone(doctor.getAccount());
        mSignInfo.setSignPersonName(residentBean.getName());
        mSignInfo.setSignPersonPhone(residentBean.getTelPhone());
    }

    private List<SignProtocolInfo.SignMemberBean> initSignServiceInfo(List<SignMember> data) {
        List<SignProtocolInfo.SignMemberBean> tempList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            SignMember signMember = data.get(i);
            if (TextUtils.isEmpty(signMember.getServiceIds()))
                continue;
            SignProtocolInfo.SignMemberBean temp = new SignProtocolInfo.SignMemberBean();
            if (signMember.getPatientId().equals(residentBean.getPatientId())) {
                temp.setRelation(residentBean.getRelation());
            } else {
                temp.setRelation(TextUtils.isEmpty(signMember.getRelation()) ? "" : signMember.getRelation());
            }
            temp.setIdcard(signMember.getIdentityCard());
            temp.setTotalPrice(signMember.getTotalPrice());
            temp.setFamilyName(signMember.getPatientName());
            temp.setServiceNames(signMember.getServiceNames());
            tempList.add(temp);
        }
        return tempList;
    }

}
