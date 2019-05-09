package com.health2world.aio.app.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.bean.HistoryAccount;
import com.health2world.aio.db.DBManager;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 历史搜索记录
 * Created by lishiyou on 2018/7/24 0024.
 */

public class SearchHistoryView extends FrameLayout {

    private Context mContext;

    private ImageView ivClearHistory;

    private TextView tvClear;

    private TagFlowLayout flowLayout;

    private LinearLayout historyLayout;

    private TagAdapter<HistoryAccount> tagAdapter;

    private List<HistoryAccount> historyAccountList = new ArrayList<>();

    private OnSelectListener listener;

    public SearchHistoryView(@NonNull Context context) {
        this(context, null);
    }

    public SearchHistoryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this);
        initView();
        initListener();
    }


    private void initView() {
        tvClear = findViewById(R.id.tvClear);
        flowLayout = findViewById(R.id.flowLayout);
        historyLayout = findViewById(R.id.historyLayout);
        ivClearHistory = findViewById(R.id.ivClearHistory);
        flowLayout.setVisibility(View.VISIBLE);
        historyLayout.setVisibility(View.VISIBLE);
        List<HistoryAccount> list = null;
        try {
            list = DBManager.getInstance().getHistoryDao().queryBuilder().limit(10).orderBy("time", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list != null) {
            historyAccountList.clear();
            historyAccountList.addAll(list);
        }
        tagAdapter = new TagAdapter<HistoryAccount>(historyAccountList) {
            @Override
            public View getView(FlowLayout parent, int position, HistoryAccount dataBean) {
                TextView textView = new TextView(mContext);
                textView.setText(dataBean.getData());
                textView.setTextSize(14f);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(18, 8, 18, 8);
                textView.setBackgroundResource(R.drawable.selector_history_button_bg);
                return textView;
            }
        };
        flowLayout.setAdapter(tagAdapter);
    }

    private void initListener() {
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (listener != null) {
                    listener.onSelect(historyAccountList.get(position).getData());
                }
                return false;
            }
        });
        ivClearHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DBManager.getInstance().getHistoryDao().queryRaw("delete from t_history");
                    refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        tvClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DBManager.getInstance().getHistoryDao().queryRaw("delete from t_history");
                    refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void refresh() {
        List<HistoryAccount> list = null;
        try {
            list = DBManager.getInstance().getHistoryDao().queryBuilder().limit(10).orderBy("time", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list != null) {
            historyAccountList.clear();
            historyAccountList.addAll(list);
            tagAdapter.notifyDataChanged();
        }
    }

    interface OnSelectListener {
        void onSelect(String keyWord);
    }

    public void setOnSelectListener(OnSelectListener listener) {
        this.listener = listener;
    }
}
