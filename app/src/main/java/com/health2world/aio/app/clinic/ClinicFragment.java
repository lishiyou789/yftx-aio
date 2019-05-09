//package com.health2world.aio.app.clinic;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.Button;
//
//import com.health2world.aio.MyApplication;
//import com.health2world.aio.R;
//import com.health2world.aio.app.clinic.recipe.RecipeActivity;
//import com.health2world.aio.app.clinic.result.BloodFatResultActivity;
//import com.health2world.aio.app.clinic.result.EcgResultActivity;
//import com.health2world.aio.app.clinic.result.UrineResultActivity;
//import com.health2world.aio.app.health.doctor.PerformanceActivity;
//import com.health2world.aio.app.home.MainActivity;
//import com.health2world.aio.app.setting.InputSettingActivity;
//import com.health2world.aio.bean.MeasureItem;
//import com.health2world.aio.bean.MeasureState;
//import com.health2world.aio.bean.ServiceItem;
//import com.health2world.aio.bean.SettingBean;
//import com.health2world.aio.ble.BleDeviceActivity;
//import com.health2world.aio.common.MsgEvent;
//import com.health2world.aio.common.mvp.MVPBaseFragment;
//import com.health2world.aio.config.AppConfig;
//import com.health2world.aio.config.MeasureConfig;
//import com.health2world.aio.db.DBManager;
//import com.health2world.aio.util.Logger;
//import com.konsung.bean.MeasureBean;
//import com.konsung.bean.ResidentBean;
//import com.konsung.bean.StateBean;
//import com.konsung.constant.Configuration;
//import com.konsung.listen.Measure;
//import com.konsung.listen.MeasureCompleteListen;
//import com.konsung.util.MeasureUtils;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import aio.health2world.brvah.BaseQuickAdapter;
//import aio.health2world.utils.ToastUtil;
//
///**
// * 门诊检查
// * Created by lishiyou on 2018/7/13 0013.
// */
//
//public class ClinicFragment extends MVPBaseFragment<ClinicContract.Presenter> implements ClinicContract.View,
//        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, MeasureCompleteListen {
//
//    public static final int REQUEST_CODE_HEIGHT = 0X21;
//    public static final int REQUEST_CODE_WEIGHT = 0X22;
//    public static final int REQUEST_CODE_SPO2 = 0X23;
//    public static final int REQUEST_CODE_ECG = 0X24;
//    public static final int REQUEST_CODE_RECIPE = 0X25;
//
//    private Button btnClinic;
//    //private Button btnSave
//    private RecyclerView recyclerView;
//    private List<MeasureItem> dataList;
//    private MeasureListAdapter measureAdapter;
//    private MeasureBean dataBean = new MeasureBean();
//    private ResidentBean resident;
//    private ServiceItem serviceItem;
//
//    //记录当前测量项的配置 如果测量项配置发生变化 则刷新当前页面
//    private String measureConfig = MyApplication.getInstance().getMeasureConfig();
//    //门诊测量辅助类
//    private ClinicUtil clinicUtil = new ClinicUtil();
//    //测量Id
//    private String dataId = "";
//    //记录数据是否上传
//    private boolean isUpload = false;
//    //区分点击的是门诊开方还是保存测量  默认保存测量
//    private int clickType = 0;
//
//    @Override
//    protected ClinicContract.Presenter getPresenter() {
//        return new ClinicPresenter(this);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_clinic;
//    }
//
//    @Override
//    protected void initView() {
//        btnClinic = findView(R.id.btnClinic);
////        btnSave = findView(R.id.btnSave);
//        recyclerView = findView(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//    }
//
//    @Override
//    protected void initData() {
//        //履约测量会传入该参数
//        if (getArguments() != null && getArguments().containsKey("serviceItem"))
//            serviceItem = (ServiceItem) getArguments().getSerializable("serviceItem");
//
//        if (serviceItem != null)
//            btnClinic.setVisibility(View.GONE);
//
//        if (serviceItem != null)
//            resident = (ResidentBean) getArguments().getSerializable(MainActivity.KEY_RESIDENT);
//        else
//            resident = ((MainActivity) mContext).getResident();
//
//        dataList = new ArrayList<>();
//        dataList.addAll(MeasureConfig.getMeasureList(mContext));
//
//        measureAdapter = new MeasureListAdapter(dataList);
//        recyclerView.setAdapter(measureAdapter);
//        measureAdapter.bindToRecyclerView(recyclerView);
//        measureAdapter.setEmptyView(R.layout.layout_empty_view);
//
//        //动态记录每一项的测量位置
//        clinicUtil.initPosition(dataList);
//    }
//
//    @Override
//    protected void initListener() {
//        MeasureUtils.setMeasureBean(dataBean);
//        MeasureUtils.setMeasureListen(this, mContext);
//        btnClinic.setOnClickListener(this);
////        btnSave.setOnClickListener(this);
//        measureAdapter.setOnItemClickListener(this);
//        measureAdapter.setOnItemChildClickListener(this);
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        String config = MyApplication.getInstance().getMeasureConfig();
//        //如果当前页面已经测量了 或者配置项没有发生改变 则不执行下面的操作
//        if (hasMeasured() || config.equals(measureConfig))
//            return;
//        measureConfig = config;
//        dataList.clear();
//        dataList.addAll(MeasureConfig.getMeasureList(mContext));
//        measureAdapter.notifyDataSetChanged();
//        //重置所有位置
//        clinicUtil.resetAllPosition();
//        //重新记录每一项的测量位置
//        clinicUtil.initPosition(dataList);
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (serviceItem == null)
//            resident = ((MainActivity) mContext).getResident();
//        switch (v.getId()) {
//            case R.id.btnPointWithSave://测量数据保存
////                clickType = 0;
////                if (resident == null) {
////                    ToastUtil.showShort("请设置当前居民");
////                    return;
////                }
////                if (!hasMeasured()) {
////                    ToastUtil.showShort("暂无测量数据");
////                    return;
////                }
////                if (isUpload) {
////                    ToastUtil.showShort("请勿重复保存数据");
////                    return;
////                }
////                mPresenter.uploadMeasureData(dataId, resident.getPatientId(), measureAdapter.getGluType(), dataBean);
////                break;
//
//            case R.id.btnClinic://门诊开方
//                clickType = 1;
//                if (resident == null) {
//                    ToastUtil.showShort("请设置当前居民");
//                    return;
//                }
//                //门诊开方的时候如果有测量数据则先保存测量数据 然后进行跳转
//                if (hasMeasured() && !isUpload) {
//                    mPresenter.uploadMeasureData(dataId, resident.getPatientId(), measureAdapter.getGluType(), dataBean);
//                } else {
//                    Intent intent = new Intent(mContext, RecipeActivity.class);
//                    intent.putExtra("dataId", dataId);
//                    intent.putExtra(MainActivity.KEY_RESIDENT, resident);
//                    startActivityForResult(intent, REQUEST_CODE_RECIPE);
//                }
//                break;
//        }
//    }
//
//    //蓝牙连接状态 数据传递
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMsgEvent(MsgEvent event) {
//        //蓝牙连接状态
//        if (event.getAction() == AppConfig.MSG_CONNECTION_CHANGED) {
//            if (clinicUtil.ITEM_DS100A != -1) {
//                dataList.get(clinicUtil.ITEM_DS100A).setConnect(MyApplication.getInstance().isFatConnect());
//                measureAdapter.notifyItemChanged(clinicUtil.ITEM_DS100A);
//            }
//            if (clinicUtil.ITEM_WBC != -1) {
//                dataList.get(clinicUtil.ITEM_WBC).setConnect(MyApplication.getInstance().isWbcConnect());
//                measureAdapter.notifyItemChanged(clinicUtil.ITEM_WBC);
//            }
//        }
//
//        //蓝牙设备数据
//        if (event.getAction() == AppConfig.MSG_MEASURE_DATA) {
//            String data = (String) event.getT();
//            Logger.i("lsy", "mData=" + data);
//            //血脂数据
//            if (data.startsWith(AppConfig.DS100A_DATA_HEAD) && clinicUtil.ITEM_DS100A != -1) {
//                clinicUtil.analysisData(data, dataBean);
//                dataList.get(clinicUtil.ITEM_DS100A).setMeasured(true);
//                dataList.get(clinicUtil.ITEM_DS100A).setMeasureBean(dataBean);
//                measureAdapter.notifyItemChanged(clinicUtil.ITEM_DS100A);
//                //状态  数据 重置
//                isUpload = false;
//            }
//            //白细胞数据
//            if (data.startsWith(AppConfig.WBC_DATA_HEAD) && clinicUtil.ITEM_WBC != -1) {
//                clinicUtil.analysisWbcData(data, dataBean);
//                dataList.get(clinicUtil.ITEM_WBC).setMeasured(true);
//                dataList.get(clinicUtil.ITEM_WBC).setMeasureBean(dataBean);
//                measureAdapter.notifyItemChanged(clinicUtil.ITEM_WBC);
//                //状态  数据 重置
//                isUpload = false;
//            }
//        }
//    }
//
//    @Override
//    public void uploadSuccess(String dataId) {
//        //测量数据保存到本地
//        MyApplication.getInstance().setDataList(dataList);
//        saveMeasureDataToLocal();
//        //数据上传成功 重置测量数据 和相关状态
//        this.dataId = dataId;
//        isUpload = true;
//        dataBean = new MeasureBean();
//        //门诊开方
//        if (clickType == 1) {
//            Intent intent = new Intent(mContext, RecipeActivity.class);
//            intent.putExtra(MainActivity.KEY_RESIDENT, resident);
//            intent.putExtra("dataId", dataId);
//            startActivityForResult(intent, REQUEST_CODE_RECIPE);
//        }
//        //本次测量为履约测量 数据上传成功之后进行履约操作
//        if (serviceItem != null) {
//            Intent intent = new Intent();
//            intent.putExtra("dataId", dataId);
//            ((PerformanceActivity) mContext).setResult(Activity.RESULT_OK, intent);
//            ((PerformanceActivity) mContext).finish();
//        }
//    }
//
//    @Override
//    public void uploadFailed() {
//
//    }
//
//
//    /**
//     * 测量数据保存到本地
//     *
//     * @return
//     */
//    private void saveMeasureDataToLocal() {
//        dataBean.setIdCard(resident.getIdentityCard());
//        dataBean.setCheckDay(System.currentTimeMillis());
//        if (measureAdapter.getGluType() == 0)
//            dataBean.setGluStyle(Configuration.BtnFlag.lift);
//        else
//            dataBean.setGluStyle(Configuration.BtnFlag.right);
//        //心率值优先级  心电>血氧>血压  将值设置到心率
//        if (clinicUtil.ITEM_ECG != -1 && dataList.get(clinicUtil.ITEM_ECG).isMeasured()) {
//            dataBean.setHr(dataList.get(clinicUtil.ITEM_ECG).getMeasureBean().getHr());
//        } else {
//            if (clinicUtil.ITEM_SPO2 != -1 && dataList.get(clinicUtil.ITEM_SPO2).isMeasured()) {
//                dataBean.setHr(dataList.get(clinicUtil.ITEM_SPO2).getMeasureBean().getPr());
//            } else {
//                if (clinicUtil.ITEM_NIBP != -1 && dataList.get(clinicUtil.ITEM_NIBP).isMeasured()) {
//                    dataBean.setHr(dataList.get(clinicUtil.ITEM_NIBP).getMeasureBean().getNibPr());
//                }
//            }
//        }
//        try {
//            DBManager.getInstance().getMeasureDao().create(dataBean);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 是否有检测数据
//     *
//     * @return
//     */
//    private boolean hasMeasured() {
//        boolean isSave = false;
//        for (MeasureItem item : dataList) {
//            if (item.isMeasured()) {
//                isSave = true;
//                break;
//            }
//        }
//        return isSave;
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != Activity.RESULT_OK)
//            return;
//        //身高测量成功
//        if (requestCode == REQUEST_CODE_HEIGHT) {
//            isUpload = false;
//            String height = data.getStringExtra("data");
//            dataBean.setHeight(height);
//
//            MeasureItem item = dataList.get(clinicUtil.ITEM_HEIGHT);
//            item.setMeasured(true);
//            item.setMeasureBean(dataBean);
//            measureAdapter.notifyItemChanged(clinicUtil.ITEM_HEIGHT);
//        }
//        //体重测量成功
//        if (requestCode == REQUEST_CODE_WEIGHT) {
//            isUpload = false;
//
//            String weight = data.getStringExtra("data");
//            dataBean.setWeight(weight);
//
//            MeasureItem item = dataList.get(clinicUtil.ITEM_WEIGHT);
//            item.setMeasured(true);
//            item.setMeasureBean(dataBean);
//            measureAdapter.notifyItemChanged(clinicUtil.ITEM_WEIGHT);
//        }
//        //血氧测量成功
//        if (requestCode == REQUEST_CODE_SPO2) {
//            boolean success = data.getBooleanExtra("success", false);
//            if (!success) {
//                dataList.get(clinicUtil.ITEM_SPO2).setState(MeasureState.MEASURE_READY);
//                measureAdapter.notifyItemChanged(clinicUtil.ITEM_SPO2);
//            } else {
//                isUpload = false;
//                MeasureBean measureBean = (MeasureBean) data.getSerializableExtra("measureBean");
//                onComplete(Measure.SPO2, measureBean);
//            }
//        }
//        //心电测量成功
//        if (requestCode == REQUEST_CODE_ECG) {
//            boolean success = data.getBooleanExtra("success", false);
//            if (!success) {
//                dataList.get(clinicUtil.ITEM_ECG).setState(MeasureState.MEASURE_READY);
//                measureAdapter.notifyItemChanged(clinicUtil.ITEM_ECG);
//            } else {
//                isUpload = false;
//                MeasureBean measureBean = (MeasureBean) data.getSerializableExtra("measureBean");
//                onComplete(Measure.ECG, measureBean);
//            }
//
//        }
//        //门诊开方成功
//        if (requestCode == REQUEST_CODE_RECIPE) {
//            MyApplication.getInstance().setDataList(null);
//            dataId = "";
//        }
//    }
//
//    //查看数据
//    @Override
//    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//        MeasureItem item = (MeasureItem) adapter.getItem(position);
//        Measure type = item.getType();
//        //心电 尿常规 血脂四项 血脂八项 数据的查看
//        if (type == Measure.ECG || type == Measure.URINE || type == Measure.BLOOD || type == Measure.DS100A) {
//            if (item.isMeasured()) {
//                //先测量后选居民的情况也存在 所以这里再获取一下当前居民信息
//                if (serviceItem != null)
//                    resident = (ResidentBean) getArguments().getSerializable(MainActivity.KEY_RESIDENT);
//                else
//                    resident = ((MainActivity) mContext).getResident();
//                Intent intent = new Intent();
//                intent.putExtra("resident", resident);
//                if (type == Measure.ECG) {
//                    intent.putExtra("measureBean", dataList.get(clinicUtil.ITEM_ECG).getMeasureBean());
//                    intent.setClass(mContext, EcgResultActivity.class);
//                }
//                if (type == Measure.URINE) {
//                    intent.putExtra("measureBean", dataList.get(clinicUtil.ITEM_URINE).getMeasureBean());
//                    intent.setClass(mContext, UrineResultActivity.class);
//                }
//                if (type == Measure.BLOOD) {
//                    intent.putExtra("type", BloodFatResultActivity.BLOOD_TYPE_4);
//                    intent.putExtra("measureBean", dataList.get(clinicUtil.ITEM_BLOOD).getMeasureBean());
//                    intent.setClass(mContext, BloodFatResultActivity.class);
//                }
//                if (type == Measure.DS100A) {
//                    intent.putExtra("type", BloodFatResultActivity.BLOOD_TYPE_8);
//                    intent.putExtra("measureBean", dataList.get(clinicUtil.ITEM_DS100A).getMeasureBean());
//                    intent.setClass(mContext, BloodFatResultActivity.class);
//                }
//                startActivity(intent);
//            } else {
//                ToastUtil.showShort("该测量项无数据 不支持查看");
//            }
//        }
//        //方便测试 所造的数据
////        if (AppConfig.isDebug) {
////            clinicUtil.debugDataTest(type, dataBean, dataList, measureAdapter);
////            if (item.getType() != Measure.ECG || item.getType() != Measure.URINE
////                    || item.getType() != Measure.DS100A || item.getType() != Measure.MYO)
////                isUpload = false;
////        }
//    }
//
//    //测量
//    @Override
//    public void onItemChildClick(BaseQuickAdapter adapter, View view, int i) {
//        MeasureItem measureItem = (MeasureItem) adapter.getItem(i);
//        MeasureState state = measureItem.getState();
//        switch (measureItem.getType()) {
//            //血压
//            case NIBP:
//                if (state == MeasureState.MEASURE_ING)
//                    measureItem.setState(MeasureState.MEASURE_READY);
//                else
//                    measureItem.setState(MeasureState.MEASURE_ING);
//                measureAdapter.notifyItemChanged(i);
//                MeasureUtils.startMeasure(Measure.NIBP);
//                break;
//            //血氧
//            case SPO2:
//                Intent intentSpo2 = new Intent(mContext, MeasureItemActivity.class);
//                intentSpo2.putExtra("measureItem", measureItem);
//                intentSpo2.putExtra("measureBean", dataBean);
//                startActivityForResult(intentSpo2, REQUEST_CODE_SPO2);
//                break;
//            //心电心率
//            case ECG:
//                measureAdapter.notifyItemChanged(i);
//                Intent intentEcg = new Intent(mContext, MeasureItemActivity.class);
//                intentEcg.putExtra("measureBean", dataBean);
//                intentEcg.putExtra("measureItem", measureItem);
//                startActivityForResult(intentEcg, REQUEST_CODE_ECG);
//                break;
//            //身高
//            case HEIGHT:
//                Intent intentHeight = new Intent(mContext, InputSettingActivity.class);
//                SettingBean heightBean = new SettingBean();
//                heightBean.setName("输入身高值（cm）");
//                heightBean.setValue("");
//                intentHeight.putExtra("settingBean", heightBean);
//                intentHeight.putExtra("isHWeight", true);
//                startActivityForResult(intentHeight, REQUEST_CODE_HEIGHT);
//                break;
//            //体重
//            case WEIGHT:
//                Intent intentWeight = new Intent(mContext, InputSettingActivity.class);
//                SettingBean weightBean = new SettingBean();
//                weightBean.setName("输入体重值（kg）");
//                weightBean.setValue("");
//                intentWeight.putExtra("settingBean", weightBean);
//                intentWeight.putExtra("isHWeight", true);
//                startActivityForResult(intentWeight, REQUEST_CODE_WEIGHT);
//                break;
//            case DS100A:
//                startActivity(new Intent(mContext, BleDeviceActivity.class));
//                break;
//            case WBC:
//                startActivity(new Intent(mContext, BleDeviceActivity.class));
//                break;
//        }
//        //餐前餐后的选择
//        if (view.getId() == R.id.rb0) {
//            measureAdapter.setGluType(0);
//            measureAdapter.notifyItemChanged(clinicUtil.ITEM_GLU);
//        }
//        if (view.getId() == R.id.rb1) {
//            measureAdapter.setGluType(1);
//            measureAdapter.notifyItemChanged(clinicUtil.ITEM_GLU);
//        }
//    }
//
//    @Override
//    public void onComplete(Measure param, MeasureBean bean) {
//        isUpload = false;
//        clinicUtil.onComplete(param, bean, dataBean, dataList, measureAdapter);
//    }
//
//    @Override
//    public void onFail(Measure param, String msg) {
//        ToastUtil.showShort(msg);
//        clinicUtil.onFail(param, dataList, measureAdapter);
//    }
//
//    @Override
//    public void NibpCuff(int va) {
//    }
//
//    @Override
//    public void onState(StateBean bean) {
//        clinicUtil.onState(bean, dataList, measureAdapter);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//}
