package com.health2world.aio.app.home;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.health2world.aio.R;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.db.DBManager;

import aio.health2world.glide_transformations.CropCircleTransformation;
import aio.health2world.glide_transformations.CropSquareTransformation;
import aio.health2world.utils.ToastUtil;

/**
 * @author Runnlin
 * @date 2018/8/9/0009.
 */

public class MyQRDialogActivity extends BaseActivity {

    private TextView myName;
    private TextView myCode;
    private ImageView myHead;
    private ImageView myQrCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_my_qrcode;
    }

    protected void initView() {
        getWindow().setBackgroundDrawable(new ColorDrawable());
        myName = findView(R.id.my_name);
        myHead = findView(R.id.my_head);
        myCode = findView(R.id.my_code);
        myQrCode = findView(R.id.my_qr);
    }

    protected void initData() {
        DoctorBean doctor = DBManager.getInstance().getCurrentDoctor();
        if (doctor != null) {
            myName.setText(doctor.getName());
            myCode.setText(String.format("医服号：%s", doctor.getDoctorCode()));
            Glide.with(this)
                    .load(doctor.getPortrait())
                    .placeholder(R.mipmap.user_portrait_circle)
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(myHead);

            Glide.with(this)
                    .load(doctor.getQcodeInfo())
                    .centerCrop()
                    .placeholder(R.mipmap.icon_default_qr_code)
                    .bitmapTransform(new CropSquareTransformation(this))
                    .into(myQrCode);
        } else {
            ToastUtil.showLong(getString(R.string.cant_gat_doctor_info));
        }

    }

    @Override
    protected void initListener() {

    }

}
