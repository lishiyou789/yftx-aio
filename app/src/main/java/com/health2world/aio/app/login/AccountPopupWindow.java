package com.health2world.aio.app.login;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.health2world.aio.R;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.db.DBManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * 历史登录的账号
 * Created by lishiyou on 2018/7/18 0018.
 */

public class AccountPopupWindow extends PopupWindow implements BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener {

    private Activity activity;

    private View contentView;

    private RecyclerView recyclerView;

    private AccountListAdapter listAdapter;

    private List<DoctorBean> doctorBeanList;

    private OnAccountCheckedListener listener;

    public AccountPopupWindow(Activity activity) {
        this.activity = activity;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.layout_account_popupwindow, null);
        this.setContentView(contentView);
        int h = activity.getWindowManager().getDefaultDisplay().getHeight();
        int w = activity.getWindowManager().getDefaultDisplay().getWidth();
        this.setWidth(w / 4);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(77666666);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        initView();
    }

    private void initView() {
        recyclerView = contentView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        doctorBeanList = new ArrayList<>();
        listAdapter = new AccountListAdapter(doctorBeanList);
        recyclerView.setAdapter(listAdapter);

        List<DoctorBean> dataList = getAccountInfo();
        if (dataList != null) {
            doctorBeanList.addAll(dataList);
        }
        listAdapter.notifyDataSetChanged();
        listAdapter.setOnItemClickListener(this);
        listAdapter.setOnItemChildClickListener(this);
    }


    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupWindow
            this.showAsDropDown(parent, -10, 10);
            refresh();
//            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }

    private void refresh() {
        List<DoctorBean> dataList = getAccountInfo();
        if (dataList != null) {
            doctorBeanList.clear();
            doctorBeanList.addAll(dataList);
            listAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取登录成功的历史账号 前五条
     *
     * @return
     */
    private List<DoctorBean> getAccountInfo() {
        List<DoctorBean> doctorList = null;
        try {
            doctorList = DBManager.getInstance().getDoctorDao().queryBuilder()
                    .orderBy("time", false).limit(5).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctorList;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (listener != null) {
            DoctorBean doctor = (DoctorBean) adapter.getItem(position);
            listener.accountChecked(doctor);
        }
        this.dismiss();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        DoctorBean doctor = (DoctorBean) adapter.getItem(position);
        try {
            DBManager.getInstance().getDoctorDao().delete(doctor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refresh();
        if (listener != null) {
            listener.accountDelete(doctor);
        }
        this.dismiss();
    }

    private class AccountListAdapter extends BaseQuickAdapter<DoctorBean, BaseViewHolder> {
        public AccountListAdapter(@Nullable List<DoctorBean> data) {
            super(R.layout.item_account_textview, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DoctorBean item) {
            helper.setText(R.id.tvAccount, item.getAccount());
            helper.addOnClickListener(R.id.ivClear);
        }
    }

    public interface OnAccountCheckedListener {
        void accountChecked(DoctorBean doctor);

        void accountDelete(DoctorBean doctor);
    }

    public void setOnAccountCheckedListener(OnAccountCheckedListener listener) {
        this.listener = listener;
    }
}
