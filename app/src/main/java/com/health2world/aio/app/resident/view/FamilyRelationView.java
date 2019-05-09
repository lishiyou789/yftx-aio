package com.health2world.aio.app.resident.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.util.DisplayUtil;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

public class FamilyRelationView extends PopupWindow {
    private Activity mContext;
    private View conentView;
    private ListView mListView;
    private String[] relationArray;

    public FamilyRelationView(Activity context) {
        this.mContext = context;
        initView();
    }

    private RelationListener mSelectRelationListener;

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popup_select_relation, null);
        int w = mContext.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(conentView);
        this.setWidth(w / 8);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());

        this.update();
        mListView = conentView.findViewById(R.id.lv_select_relation_ship);
        relationArray = mContext.getResources().getStringArray(R.array.family_relation);
        mListView.setAdapter(new SelectRelationShipAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                if (mSelectRelationListener != null) {
                    mSelectRelationListener.onClick(relationArray[position]);
                }
            }
        });

    }

    public void setSelectRelationListener(RelationListener selectRelationListener) {
        this.mSelectRelationListener = selectRelationListener;
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 10);
        } else {
            this.dismiss();
        }
    }

    private class SelectRelationShipAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return relationArray.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(mContext);
            textView.setText(relationArray[position]);
            textView.setTextSize(DisplayUtil.px2sp(mContext, 20));
            textView.setPadding(20, 20, 20, 20);
            return textView;
        }
    }

    public interface RelationListener {
        void onClick(String relationShip);
    }

}
