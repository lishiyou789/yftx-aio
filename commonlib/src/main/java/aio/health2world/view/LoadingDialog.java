package aio.health2world.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aio.health2world.library.R;

/**
 * Created by Administrator on 2017/8/9 0009.
 */

public class LoadingDialog extends Dialog {

    private TextView tv_load_dialog;
    private Activity activity;
    private LayoutInflater inflater;

    public LoadingDialog(Activity activity) {
        super(activity, R.style.LoadingDialog);
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    private void initView() {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        View view = inflater.inflate(R.layout.layout_loading_dialog, null);
//        this.setContentView(R.layout.layout_loading_dialog);
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        getWindow().setAttributes(params);
        this.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setCancelable(false);
        tv_load_dialog = (TextView) view.findViewById(R.id.tv_load_dialog);
//        tv_load_dialog.setSingleLine(true);
    }

    public void showDialog(boolean b, String title) {
        setCancelable(b);
        setCanceledOnTouchOutside(b);
        if (!TextUtils.isEmpty(title)) {
            tv_load_dialog.setText(title);
        }
        if (!activity.isDestroyed()) {
            show();
        }
    }

    public void setTips(String tips) {
        if (!TextUtils.isEmpty(tips)) {
            tv_load_dialog.setText(tips);
        }
    }

}
