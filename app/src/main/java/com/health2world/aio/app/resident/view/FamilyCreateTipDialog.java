package com.health2world.aio.app.resident.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.health2world.aio.R;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class FamilyCreateTipDialog extends Dialog {
    private TextView mTvOK, mTvNo;
    private View.OnClickListener mListener;

    public FamilyCreateTipDialog(@NonNull Context context) {
        super(context, R.style.familyCreateDialogStyle);
    }

    public void setListener(View.OnClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_family_create_tip);
        mTvOK = findViewById(R.id.tv_ok);
        mTvNo = findViewById(R.id.tv_no);
        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    dismiss();
                    mListener.onClick(v);
                }


            }
        });
        mTvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    dismiss();
                    mListener.onClick(v);
                }
            }
        });
    }

}
