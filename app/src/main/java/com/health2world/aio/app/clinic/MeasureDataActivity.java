package com.health2world.aio.app.clinic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.app.search.RSearchActivity;
import com.health2world.aio.bean.MeasureItem;
import com.health2world.aio.common.BaseActivity;
import com.health2world.aio.common.MsgEvent;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;
import com.konsung.listen.Measure;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.utils.ToastUtil;

import static com.health2world.aio.config.AppConfig.MSG_UPDATE_MEASURE_DATA;

/**
 * Created by lishiyou on 2018/12/5 0005.
 */

public class MeasureDataActivity extends BaseActivity {

    /**
     * 列出和记录的list
     */
    private List<MeasureItem> dataList;

    /**
     * 保存数据的bean
     */
    private MeasureBean _measureBean;
    private Measure _param;

    /**
     * UI
     */
    private TextView tvMessage;
    private Button btnIgnore, btnSelect;
    private RecyclerView mRecyclerView;
    private MeasureDialogAdapter measureAdapter;

    //如果测量项是有同时复数数据上传的，应接收后设置pass为真，避免重复出现
    boolean _pass = false;

    boolean hasResident = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_measure_data;
    }

    @Override
    protected void initView() {
        setFinishOnTouchOutside(false);
        tvMessage = findView(R.id.tv_message);
        btnIgnore = findView(R.id.btnIgnore);
        btnSelect = findView(R.id.btnSelect);
        mRecyclerView = findView(R.id.rv_measureData);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        hasResident = MyApplication.getInstance().getResident() != null;

        if (hasResident) {
            btnSelect.setText("门诊测量");
            tvMessage.setText("请选择打开门诊测量以保存测量数据");
        }

        if (getIntent().hasExtra("bean"))
            _measureBean = (MeasureBean) getIntent().getSerializableExtra("bean");
        if (getIntent().hasExtra("param"))
            _param = (Measure) getIntent().getSerializableExtra("param");


        dataList = new ArrayList<>();
//        _measureBean = new MeasureBean();
        measureAdapter = new MeasureDialogAdapter(dataList);
        mRecyclerView.setAdapter(measureAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        measureAdapter.bindToRecyclerView(mRecyclerView);

        parseData(_param, _measureBean);
    }

    @Override
    protected void initListener() {
        setOnClickListener(btnSelect);
        setOnClickListener(btnIgnore);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(MsgEvent<Bundle> event) {
        if (event.getAction() == MSG_UPDATE_MEASURE_DATA) {
            _measureBean = (MeasureBean) event.getT().getSerializable("bean");
            _param = (Measure) event.getT().getSerializable("param");
            parseData(_param, _measureBean);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnSelect:
                //有居民时直接去门诊检测
                if (hasResident) {
                    Intent _intent = new Intent(this, ClinicOneActivity.class);
                    _intent.putExtra("openFrom", 1);
                    Bundle _bundle = new Bundle();
                    MyApplication.getInstance().setDataList(dataList);
//                    _bundle.putSerializable("dataList", (Serializable) dataList);
                    if (_measureBean != null)
                        _bundle.putSerializable("bean", _measureBean);
                    _intent.putExtras(_bundle);
                    _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(_intent);
                    finish();
                }
                //没居民先选居民
                else {
                    Intent intent = new Intent(this, RSearchActivity.class);
                    intent.putExtra("flag", 1);
                    startActivityForResult(intent, 0x09);
                }
                break;
            case R.id.btnIgnore:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            //选择居民后回来
            if (requestCode == 0x09) {
                ResidentBean resident = (ResidentBean) data.getSerializableExtra(MainActivity.KEY_RESIDENT);
                Intent intent = new Intent(this, ClinicOneActivity.class);
                intent.putExtra(MainActivity.KEY_RESIDENT, resident);
                intent.putExtra("flag", 1);
                if (_measureBean != null)
                    intent.putExtra("bean", _measureBean);
                Bundle _bundle = new Bundle();
//                _bundle.putSerializable("dataList", (Serializable) dataList);
                MyApplication.getInstance().setDataList(dataList);
                intent.putExtras(_bundle);
                startActivity(intent);
            }
        finish();
    }

    private void parseData(Measure type, MeasureBean bean) {
        MeasureItem _measureItem = null;

        if (_pass)
            ToastUtil.showShort("请于门诊测量中进行测量, 以保证数据正常接收");

        //体温
        if (type == Measure.TEMP) {
            _measureItem = new MeasureItem(type, bean.getTemp() + "");
        }

        //血糖
        if (type == Measure.GLU) {
            _measureItem = new MeasureItem(type, bean.getGlu() + "");
        }

        //尿常规
        if (type == Measure.URINE) {
            _measureItem = new MeasureItem(type, "门诊检查中查看");

        }

        //总胆固醇
        if (type == Measure.CHOL) {
            _measureItem = new MeasureItem(type, bean.getXzzdgc() + "");
        }

        //尿酸
        if (type == Measure.UA) {
            _measureItem = new MeasureItem(type, bean.getUricacid() + "");

        }

        //血脂四项
        if (type == Measure.BLOOD) {
            _measureItem = new MeasureItem(type, "门诊检查中查看");
        }

        //艾康血红蛋白/压积值
        if (!_pass && (type == Measure.HB || type == Measure.HCT)) {
            _measureItem = new MeasureItem(Measure.HB, "" + bean.getAssxhb() + "/" + bean.getAssxhct());
            _pass = true;
        }

        //血脂八项（大树血脂）
        if (type == Measure.DS100A) {
            _measureItem = new MeasureItem(type, "门诊检查中查看");
        }

        /**
         * 因为量点设备上传数据时一个bean内同时带有几个数据
         * 故这样区别分发
         */
        //C反应蛋白 + 超敏C反应蛋白 + 血清淀粉样蛋白A
        if ((!_pass) && (type == Measure.CRP || type == Measure.Hs_CRP || type == Measure.SAA)) {
            if (!"".equals(bean.getFia_crp()) &&
                    !"".equals(bean.getFia_hscrp()) &&
                    !"".equals(bean.getFia_saa())) {
                _measureItem = new MeasureItem(Measure.CRP, bean.getFia_crp());
                dataList.add(_measureItem);

                _measureItem = new MeasureItem(Measure.Hs_CRP, bean.getFia_hscrp());
                dataList.add(_measureItem);

                _measureItem = new MeasureItem(Measure.SAA, bean.getFia_saa());

                _pass = true;
            }
            //C反应蛋白 + 超敏C反应蛋白
            else if (!"".equals(bean.getFia_crp()) &&
                    !"".equals(bean.getFia_hscrp())) {
                _measureItem = new MeasureItem(Measure.CRP, bean.getFia_crp());
                dataList.add(_measureItem);

                _measureItem = new MeasureItem(Measure.Hs_CRP, bean.getFia_hscrp());
                _pass = true;
            }
            //C反应蛋白 + 血清淀粉样蛋白A
            else if (!"".equals(bean.getFia_crp()) &&
                    !"".equals(bean.getFia_saa())) {
                _measureItem = new MeasureItem(Measure.CRP, bean.getFia_crp());
                dataList.add(_measureItem);

                _measureItem = new MeasureItem(Measure.SAA, bean.getFia_saa());
                _pass = true;
            }
            //C反应蛋白
            else if (type == Measure.CRP) {
                _measureItem = new MeasureItem(type, "" + bean.getFia_crp());
            }
            //超敏C反应蛋白
            else if (type == Measure.Hs_CRP) {
                _measureItem = new MeasureItem(type, "" + bean.getFia_hscrp());
            }
            //血清淀粉样蛋白A
            else if (type == Measure.SAA) {
                _measureItem = new MeasureItem(type, "" + bean.getFia_saa());
            }
        }

        //降钙素原
        if (type == Measure.PCT) {
            _measureItem = new MeasureItem(type, "" + bean.getFia_pct());

        }

        //心肌三项
        if (type == Measure.MYO) {
            _measureItem = new MeasureItem(type, "" + bean.getFia_ctnl() + "/" + bean.getFia_ckmb() + "/" + bean.getFia_myo());
        }

        //白细胞
        if ((!_pass) && (type == Measure.WBC)) {
            _measureItem = new MeasureItem(type, "" + bean.getWbc());
            _pass = true;
        }

        if (_measureItem != null) {
            dataList.add(_measureItem);
        }

        measureAdapter.notifyItemChanged(dataList.size());
        mRecyclerView.smoothScrollToPosition(dataList.size());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
