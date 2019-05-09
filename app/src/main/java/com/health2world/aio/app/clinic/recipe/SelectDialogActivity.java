package com.health2world.aio.app.clinic.recipe;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.common.BaseActivity;

/**
 * 常见症状选择对话框
 *
 * @author Runnlin
 * @date 2018/8/7/0007.
 */

public class SelectDialogActivity extends BaseActivity implements View.OnClickListener {

    public static final String KEY_ARRAY = "array";
    public static final String KEY_DATA = "data";
    public static final String KEY_TYPE = "type";

    private TextView tvConfirm, tvTitle;
    private ListView lvSelectList;
    private static String[] ary = {"无症状"};
    private static int[] selected;
    private ArrayAdapter<String> listsAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_comm_dialog;
    }

    @Override
    protected void initView() {
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        tvConfirm = findView(R.id.tv_select_dialog_confirm);
        lvSelectList = findView(R.id.lv_select_dialog);
        tvTitle = findView(R.id.tvTitle);
        if (getIntent().getIntExtra(KEY_TYPE, 0) == 1) {
            tvTitle.setText("常用医嘱");
        }
        tvConfirm.setClickable(true);
    }

    @Override
    protected void initData() {
        ary = getIntent().getStringArrayExtra(KEY_ARRAY);
        selected = new int[ary.length];
        for (int i : selected) {
            selected[i] = -1;
        }
        listsAdapter = new ArrayAdapter<>(this, R.layout.item_select_dialog_text, ary);
        lvSelectList.setAdapter(listsAdapter);
    }

    @Override
    protected void initListener() {
        tvConfirm.setOnClickListener(this);
        lvSelectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (selected[i] != 1) {
                    selected[i] = 1;
                    view.setBackgroundColor(getResources().getColor(R.color.blue_light));
                } else {//重复点击，取消选择
                    selected[i] = -1;
                    view.setBackgroundColor(getResources().getColor(R.color.gray_light));
                }
                listsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_select_dialog_confirm:
                String result = "";
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i] == 1)
                        result += ary[i] + "、";
                }
                if (TextUtils.isEmpty(result)) {
                    finish();
                } else {
                    result = result.substring(0, result.length() - 1);
                    Intent intent = new Intent();
                    intent.putExtra(SelectDialogActivity.KEY_DATA, result);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

}
