package com.health2world.aio.app.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health2world.aio.R;
import com.konsung.bean.ResidentBean;

import aio.health2world.utils.ToastUtil;

/**
 * Created by lishiyou on 2018/7/11 0011.
 */

public class HomeTabView extends FrameLayout implements View.OnClickListener {

    public static final int TAB_INDEX_HEALTH = 0;
    public static final int TAB_INDEX_CLINIC = 1;
    public static final int TAB_INDEX_DOCTOR = 2;
    public static final int TAB_INDEX_TASK = 3;


    private LinearLayout llHealthLayout, llClinicLayout, llDoctorLayout, llTaskLayout;

    private TextView tvHealth, tvClinic, tvDoctor, tvTask;

    private ImageView ivHealth, ivClinic, ivDoctor, ivTask;

    private TabChangedListener listener;

    private ResidentBean resident;

    private Context mContext;


    public HomeTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_home_tab, this);
        initView();
        initData();
        addListener();
    }


    private void initView() {
        tvHealth = findViewById(R.id.tvHealth);
        tvClinic = findViewById(R.id.tvClinic);
        tvDoctor = findViewById(R.id.tvDoctor);
        tvTask = findViewById(R.id.tvTask);
        ivHealth = findViewById(R.id.ivHealth);
        ivClinic = findViewById(R.id.ivClinic);
        ivDoctor = findViewById(R.id.ivDoctor);
        ivTask = findViewById(R.id.ivTask);
        llHealthLayout = findViewById(R.id.llHealthLayout);
        llClinicLayout = findViewById(R.id.llClinicLayout);
        llDoctorLayout = findViewById(R.id.llDoctorLayout);
        llTaskLayout = findViewById(R.id.llTaskLayout);
    }

    private void initData() {
        String[] TITLES = getResources().getStringArray(R.array.home_tabs_array);
        tvHealth.setText(TITLES[0]);
        tvClinic.setText(TITLES[1]);
        tvDoctor.setText(TITLES[2]);
        tvTask.setText(TITLES[3]);
        //默认选中任务中心模块
        updateView(TAB_INDEX_TASK);
    }

    private void addListener() {
        llHealthLayout.setOnClickListener(this);
        llClinicLayout.setOnClickListener(this);
        llDoctorLayout.setOnClickListener(this);
        llTaskLayout.setOnClickListener(this);
    }

    public void setResident(ResidentBean resident) {
        this.resident = resident;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llHealthLayout:
                if (resident == null) {
                    ToastUtil.showLong(mContext.getString(R.string.set_current_resident));
                    return;
                }
//                updateView(TAB_INDEX_HEALTH);
                if (listener != null)
                    listener.onTabChecked(TAB_INDEX_HEALTH);
                break;
            case R.id.llDoctorLayout:
                if (resident == null) {
                    ToastUtil.showLong(mContext.getString(R.string.set_current_resident));
                    return;
                }
//                updateView(TAB_INDEX_DOCTOR);
                if (listener != null)
                    listener.onTabChecked(TAB_INDEX_DOCTOR);
                break;
            case R.id.llClinicLayout:
//                updateView(TAB_INDEX_CLINIC);
                if (listener != null)
                    listener.onTabChecked(TAB_INDEX_CLINIC);
                break;
            case R.id.llTaskLayout:
                updateView(TAB_INDEX_TASK);
                if (listener != null)
                    listener.onTabChecked(TAB_INDEX_TASK);
                break;
        }
    }

    //改变UI选中选效果
    public void updateView(int index) {
        resetAllView();
        switch (index) {
            case TAB_INDEX_HEALTH:
                ivHealth.setImageResource(R.mipmap.tab_health_icon_select);
                tvHealth.setTextColor(getResources().getColor(R.color.white));
                llHealthLayout.setBackgroundResource(R.drawable.shape_home_title_gradient_bg);
                break;
            case TAB_INDEX_CLINIC:
                ivClinic.setImageResource(R.mipmap.tab_clinic_icon_select);
                tvClinic.setTextColor(getResources().getColor(R.color.white));
                llClinicLayout.setBackgroundResource(R.drawable.shape_home_title_gradient_bg);
                break;
            case TAB_INDEX_DOCTOR:
                ivDoctor.setImageResource(R.mipmap.tab_doctor_icon_select);
                tvDoctor.setTextColor(getResources().getColor(R.color.white));
                llDoctorLayout.setBackgroundResource(R.drawable.shape_home_title_gradient_bg);
                break;
            case TAB_INDEX_TASK:
                ivTask.setImageResource(R.mipmap.tab_task_icon_select);
                tvTask.setTextColor(getResources().getColor(R.color.white));
                llTaskLayout.setBackgroundResource(R.drawable.shape_home_title_gradient_bg);
                break;
        }
    }


    public void resetAllView() {
        ivHealth.setImageResource(R.mipmap.tab_health_icon);
        tvHealth.setTextColor(getResources().getColor(R.color.black6));
        llHealthLayout.setBackgroundResource(R.color.white);

        ivClinic.setImageResource(R.mipmap.tab_clinic_icon);
        tvClinic.setTextColor(getResources().getColor(R.color.black6));
        llClinicLayout.setBackgroundResource(R.color.white);

        ivDoctor.setImageResource(R.mipmap.tab_doctor_icon);
        tvDoctor.setTextColor(getResources().getColor(R.color.black6));
        llDoctorLayout.setBackgroundResource(R.color.white);

        ivTask.setImageResource(R.mipmap.tab_task_icon);
        tvTask.setTextColor(getResources().getColor(R.color.black6));
        llTaskLayout.setBackgroundResource(R.color.white);

    }


    public void setOnTabChangedListener(TabChangedListener listener) {
        this.listener = listener;
    }
}
