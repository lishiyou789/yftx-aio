package com.health2world.aio.app.resident.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.health2world.aio.R;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.ResultSubscriber;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.ToastUtil;

/**
 * Created by Administrator on 2018/7/23 0023.
 */

public class ResidentQRCodeDialog extends Dialog {
    private String mResidentQrUrl;
    private String mResidentCode;
    private TextView mTvTitle;
    private TextView mTvResidentCode;
    private int mType = 0;//0 添加居民查看 1 居民资料点击查看
    private String patientId = "";
    private ImageView ivImage;

    public ResidentQRCodeDialog(@NonNull Context context) {
        super(context, R.style.residentQrcodeDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_resident_qr_code);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        mTvTitle = findViewById(R.id.tv_add_title);
        ivImage = findViewById(R.id.img_qr_code);
        mTvResidentCode = findViewById(R.id.tv_resident_code);
        mTvResidentCode.setText("居民码：" + mResidentCode);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
        setCancelable(true);
        findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (mType == 0) {
            mTvTitle.setText("添加成功");
            Glide.with(getContext())
                    .load(mResidentQrUrl)
                    .placeholder(R.mipmap.ic_default_image)
                    .error(R.mipmap.ic_default_image)
                    .into(ivImage);

        }
        if (mType == 1) {
            mTvTitle.setText("二维码");
            ApiRequest.getPatientQrCode(patientId, new ResultSubscriber<String>() {
                @Override
                public void onNext(HttpResult<String> t) {
                    super.onNext(t);
                    String code = t.code;
                    if (AppConfig.SUCCESS.equals(code)) {
                        String data = t.data;
                        Glide.with(getContext())
                                .load(data)
                                .placeholder(R.mipmap.ic_default_image)
                                .error(R.mipmap.ic_default_image)
                                .into(ivImage);
                    } else {
                        ToastUtil.showShort(t.errorMessage);
                    }
                }
            });

        }


    }

    public void setResidentQrUrl(String residentQrUrl) {
        this.mResidentQrUrl = residentQrUrl;
    }


    public void setResidentCode(String residentCode) {
        this.mResidentCode = residentCode;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * 对话框类型
     *
     * @param mType
     */
    public void setType(int mType) {
        this.mType = mType;
    }
}
