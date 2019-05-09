package aio.health2world.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import aio.health2world.library.R;

/**
 * Created by Administrator on 2017/8/9 0009.
 */

public class MyProgressDialog extends Dialog {

    public static final int DIALOG_LOADING = 0;
    public static final int DIALOG_PROGRESS = 1;

    private ProgressBar progress, progress1;
    private Context context;
    private TextView tvTitle;

    private LayoutInflater inflater;

    public MyProgressDialog(Context context) {
        this(context, R.style.dialogStyle_Horizontal);
    }

    public MyProgressDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
    }

    private void initView() {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.layout_my_dialog, null);
        this.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        progress = (ProgressBar) view.findViewById(R.id.progress);
        progress1 = (ProgressBar) view.findViewById(R.id.progress1);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
    }

    public void init(int max) {
        progress1.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        progress.setProgress(0);
        progress.setMax(max);
    }

    public void update(int mProgress, int max) {
        progress.setProgress(mProgress);
        tvTitle.setText("数据同步中(" + mProgress + "/" + max + ")");
    }

    public void update(int mProgress, String percent) {
        progress.setProgress(mProgress);
        tvTitle.setText(" [ " + percent + " ] ");
    }

    public void initLoading(String text) {
        progress.setVisibility(View.GONE);
        progress1.setVisibility(View.VISIBLE);
        tvTitle.setText(text);
    }

}
