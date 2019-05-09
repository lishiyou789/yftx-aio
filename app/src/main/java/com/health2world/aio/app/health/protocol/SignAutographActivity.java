package com.health2world.aio.app.health.protocol;

import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.health2world.aio.R;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.util.TitleBarUtil;
import com.health2world.aio.view.LinePathView;
import com.health2world.aio.view.TitleBar;

import java.io.File;
import java.io.IOException;

import aio.health2world.utils.ToastUtil;

/**
 * 处理签约/解约操作
 *
 * @author Administrator
 * @date 2018/8/2/0002.
 */

public class SignAutographActivity extends BaseActivity {
    /**
     * UI
     */
    private Button mBtnCancel;
    private Button mBtnConfirm;
    private TitleBar titleBar;
    private LinePathView mLinePathView;
    /**
     * data
     */
    public static String mImgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "signature.png";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_autograph;
    }

    @Override
    protected void initView() {
        titleBar = findView(R.id.titleBar);
        TitleBarUtil.setAttr(this, "确认签名", "", titleBar);
        mBtnCancel = findView(R.id.btn_cancel);
        mLinePathView = findView(R.id.line_path_view);
        mBtnConfirm = findView(R.id.btn_submit);
        mLinePathView.setPaintWidth(8);
        mLinePathView.setPenColor(getResources().getColor(R.color.black));
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinePathView.clear();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processConfirm();
            }
        });
    }

    private void processConfirm() {
        if (mLinePathView.getTouched()) {
            mLinePathView.save(mImgPath, false, 6);
            //检查外置存储是否存在
            boolean sdExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
            if (sdExist) {
                String dbDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                //数据库所在目录
                dbDir += "/health2world";
                //判断目录是否存在，不存在则新建
                File dirFile = new File(dbDir);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                setResult(RESULT_OK);
                finish();
            }
        } else
            ToastUtil.showShort("请手写签名");
    }
}
