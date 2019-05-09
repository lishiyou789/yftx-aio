package com.health2world.aio.app.history;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.app.history.data.NotUploadActivity;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.printer.PrintUtils;
import com.health2world.aio.printer.PrinterConn;
import com.health2world.aio.util.DefaultOnPageChangeListener;
import com.health2world.aio.util.TimePickerUtil;
import com.health2world.aio.view.NoScrollViewPager;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import aio.health2world.flycotablayout.SegmentTabLayout;
import aio.health2world.flycotablayout.SlidingTabLayout;
import aio.health2world.flycotablayout.listener.OnTabSelectListener;
import aio.health2world.pickeview.TimePickerView;
import aio.health2world.utils.AppUtils;
import aio.health2world.utils.DateUtil;
import aio.health2world.utils.ToastUtil;

import static com.health2world.aio.app.clinic.recipe.RecipeActivity.REQUEST_NET_PRINTER;
import static com.health2world.aio.app.clinic.recipe.RecipeActivity.REQUEST_USB_PRINTER;

/**
 * 历史记录
 * Created by lishiyou on 2018/7/18 0018.
 */

public class HistoryRecordActivity extends BaseActivity {

    public static String[] TITLES = {"历史数据", "健康报告", "履约记录"};

    public static String[] mTitles = {"血压", "血糖", "血氧", "体温", "心电"};

    private SegmentTabLayout tabLayout;

    private SlidingTabLayout slidingTabLayout;

    public static ProgressDialog mDialog;

    private PrintUtils mPrintUtils;

    //当前居民
    private ResidentBean resident;

    private HistoryPagerAdapter pagerAdapter;

    private HistoryChartAdapter chartAdapter;

    private ViewPager viewPager;

    private ImageView ivChange, ivBack;

    private TextView tvTitle, tvLeftTitle, tvStartTime, tvEndTime, tvClearTime, tvNotUpload;

    private NoScrollViewPager chartViewPager;

    private int viewType = 0;

    //时间选择控件
    private TimePickerView pickerStartTime, pickerEndTime;

    private Calendar calendar;

    private String startTime = "", endTime = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history_record;
    }

    @Override
    protected void initView() {

        tvLeftTitle = findView(R.id.tvLeftTitle);
        tvTitle = findView(R.id.tvTitle);
        ivChange = findView(R.id.ivChange);
        ivBack = findView(R.id.ivBack);

        tvStartTime = findView(R.id.tvStartTime);
        tvEndTime = findView(R.id.tvEndTime);
        tvClearTime = findView(R.id.tvClearTime);
        tvNotUpload = findView(R.id.tvNotUpload);

        tabLayout = findView(R.id.tabLayout);
        slidingTabLayout = findView(R.id.slidingTabLayout);

        viewPager = findView(R.id.viewPager);
        chartViewPager = findView(R.id.chartViewPager);
        //避免切换页签和图表滑动的冲突 所以禁止viewPager左右滑动
        chartViewPager.setScroll(false);
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在打印...");
        mDialog.setCancelable(false);
    }

    @Override
    protected void initData() {
        resident = (ResidentBean) getIntent().getSerializableExtra(MainActivity.KEY_RESIDENT);

        tvTitle.setText(resident.getName());
        tabLayout.setTabData(TITLES);

        calendar = Calendar.getInstance();

        pagerAdapter = new HistoryPagerAdapter(resident, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        chartAdapter = new HistoryChartAdapter(resident, getSupportFragmentManager());
        chartViewPager.setAdapter(chartAdapter);
        slidingTabLayout.setViewPager(chartViewPager);

        if (getIntent().hasExtra("index")) {
            int index = getIntent().getIntExtra("index", 0);
            viewPager.setCurrentItem(index);
            tabLayout.setCurrentTab(index);
        }

        //默认一个月的历史记录
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (month - 1 == 0) {
            startTime = (year - 1) + "-12-" + (day < 10 ? "0" + day : day);
        } else {
            startTime = year + "-" + (month - 1 < 10 ? "0" + (month - 1) : month - 1) + "-" + (day < 10 ? "0" + day : day);
        }
        endTime = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);

        List<MeasureBean> data = null;
        try {
            data = DBManager.getInstance().getMeasureDao().queryBuilder().where().eq("upload", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (data != null && data.size() > 0)
            tvNotUpload.setVisibility(View.VISIBLE);

        mPrintUtils = new PrintUtils();
    }

    @Override
    protected void initListener() {
        ivChange.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvLeftTitle.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvClearTime.setOnClickListener(this);
        tvNotUpload.setOnClickListener(this);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.addOnPageChangeListener(new DefaultOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

        });

        slidingTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                chartViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        chartViewPager.addOnPageChangeListener(new DefaultOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                slidingTabLayout.setCurrentTab(position);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tvLeftTitle:
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivChange:
                viewType = (viewType == 0 ? 1 : 0);
                chartAdapter.changeView(viewType);
                break;
            case R.id.tvStartTime:
                if (pickerStartTime == null) {
                    pickerStartTime = TimePickerUtil.init(this, onTimeSelectListener1);
                    String[] sArray = startTime.split("-");
                    int year = Integer.parseInt(sArray[0]);
                    int month = Integer.parseInt(sArray[1]) - 1;
                    int day = Integer.parseInt(sArray[2]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    pickerStartTime.setDate(calendar);
                }
                pickerStartTime.show();
                break;
            case R.id.tvEndTime:
                if (pickerEndTime == null)
                    pickerEndTime = TimePickerUtil.init(this, onTimeSelectListener2);
                pickerEndTime.show();
                break;
            case R.id.tvClearTime:
                tvStartTime.setText("开始时间");
                tvEndTime.setText("结束时间");
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                if (month - 1 == 0) {
                    startTime = (year - 1) + "-12-" + (day < 10 ? "0" + day : day);
                } else {
                    startTime = year + "-" + (month - 1 < 10 ? "0" + (month - 1) : month - 1) + "-" + (day < 10 ? "0" + day : day);
                }
                endTime = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
                pagerAdapter.resetTime();
                break;
            case R.id.tvNotUpload:
                startActivity(new Intent(this, NotUploadActivity.class));
                break;
        }
    }

    //时间选择器的回调 （开始时间）
    private TimePickerView.OnTimeSelectListener onTimeSelectListener1 = new TimePickerView.OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, View v) {
            String time = DateUtil.getTime(date);
            long a = Long.parseLong(time.replace("-", ""));
            long b = Long.parseLong(endTime.replace("-", ""));
            if (a > b) {
                ToastUtil.showLong("起止时间选择有误");
            } else {
                startTime = time;
                tvStartTime.setText(startTime);
                pagerAdapter.refreshFragment(startTime, endTime);
            }
        }
    };

    //时间选择器的回调 （结束时间）
    private TimePickerView.OnTimeSelectListener onTimeSelectListener2 = new TimePickerView.OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, View v) {
            String time = DateUtil.getTime(date);
            long a = Long.parseLong(startTime.replace("-", ""));
            long b = Long.parseLong(time.replace("-", ""));
            if (a > b) {
                ToastUtil.showLong("起止时间选择有误");
            } else {
                endTime = time;
                tvEndTime.setText(time);
                pagerAdapter.refreshFragment(startTime, endTime);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_4_4_2)){
            switch (requestCode) {
                case REQUEST_USB_PRINTER:
                    mPrintUtils.printByBenTu();
                    mDialog.dismiss();
                    break;
                case REQUEST_NET_PRINTER:
                    if (PrinterConn.send()) {
                        ToastUtil.showLong("网络客户端发送成功，请稍后");
                        mDialog.dismiss();
                    }
                    break;
            }
        }
    }


//    //计时器 30秒钟倒计时 检查打印状态
//    private CountDownTimer mCountDownTimer = new CountDownTimer(30 * 1000, 1000) {
//        @Override
//        public void onTick(long millisUntilFinished) {
//        }
//
//        @Override
//        public void onFinish() {
//            if (mDialog.isShowing()) {
//                mDialog.dismiss();
//                ToastUtil.showLong("打印超时，请拔插打印机");
//            }
//        }
//
//    };
}
