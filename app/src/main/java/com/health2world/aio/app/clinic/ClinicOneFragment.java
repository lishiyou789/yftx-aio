package com.health2world.aio.app.clinic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.clinic.recipe.RecipeActivity;
import com.health2world.aio.app.clinic.result.BloodFatResultActivity;
import com.health2world.aio.app.clinic.result.EcgResultActivity;
import com.health2world.aio.app.clinic.result.UrineResultActivity;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.app.setting.InputSettingActivity;
import com.health2world.aio.bean.MeasureItem;
import com.health2world.aio.bean.MeasureState;
import com.health2world.aio.bean.SettingBean;
import com.health2world.aio.ble.BleDeviceActivity;
import com.health2world.aio.common.MsgEvent;
import com.health2world.aio.common.mvp.MVPBaseFragment;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.config.MeasureConfig;
import com.health2world.aio.config.NormalRange;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.printer.PrinterConn;
import com.health2world.aio.util.Logger;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;
import com.konsung.bean.StateBean;
import com.konsung.constant.Configuration;
import com.konsung.listen.DeviceManagerStateListen;
import com.konsung.listen.Measure;
import com.konsung.listen.MeasureCompleteListen;
import com.konsung.net.EchoServerEncoder;
import com.konsung.util.MeasureUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.ToastUtil;

import static android.app.Activity.RESULT_OK;

/**
 * 门诊检查
 * Created by lishiyou on 2018/7/13 0013.
 */

public class ClinicOneFragment extends MVPBaseFragment<ClinicContract.Presenter> implements ClinicContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, MeasureCompleteListen {

    public static final int REQUEST_CODE_HEIGHT = 0x21;
    public static final int REQUEST_CODE_WEIGHT = 0x22;
    public static final int REQUEST_CODE_SPO2 = 0x23;
    public static final int REQUEST_CODE_ECG = 0x24;
    public static final int REQUEST_CODE_RECIPE = 0x20;

    private Button btnClinic;
    //private Button btnSave
    private RecyclerView recyclerView;
    private static List<MeasureItem> dataList;
    MeasureBean bean;
    private static MeasureListAdapter measureAdapter;
    //1 正常进入门诊检查  0 履约测量
    private static int openFrom = 1;
    private static MeasureBean dataBean;

    private static ResidentBean resident;

    //记录当前测量项的配置 如果测量项配置发生变化 则刷新当前页面
    private String measureConfig;
    //门诊测量辅助类
    private static ClinicUtil clinicUtil;
    //测量Id
    private static String dataId = "";
    //记录数据是否上传
    private boolean isUpload = false;

    @Override
    protected ClinicContract.Presenter getPresenter() {
        return new ClinicPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_clinic;
    }

    @Override
    protected void initView() {
        btnClinic = findView(R.id.btnClinic);
        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    protected void initData() {
        bean = new MeasureBean();
        dataBean = new MeasureBean();
        clinicUtil = new ClinicUtil();
        measureConfig = MyApplication.getInstance().getMeasureConfig();
        resident = ((ClinicOneActivity) mContext).getResident();

        dataList = new ArrayList<>();
        dataList.addAll(MeasureConfig.getMeasureList(mContext));

        measureAdapter = new MeasureListAdapter(dataList);
        recyclerView.setAdapter(measureAdapter);
        measureAdapter.bindToRecyclerView(recyclerView);
        measureAdapter.setEmptyView(R.layout.layout_empty_view);

        //动态记录每一项的测量位置
        clinicUtil.initPosition(dataList);

        //跳转时接收测量数据
        if (getArguments() != null && getArguments().containsKey("bean")) {
            bean = (MeasureBean) getArguments().getSerializable("bean");
        }
        openFrom = getArguments().getInt("openFrom");
        if (openFrom == 0)
            btnClinic.setText("履约完成");

        List<MeasureItem> jumpList = MyApplication.getInstance().getDataList();
        if (jumpList != null) {
            for (MeasureItem mi : jumpList) {
                if (!"".equals(mi.getExtraValue())) {
                    clinicUtil.onComplete(mi.getType(), bean, dataBean, dataList, measureAdapter);
                    stickMeasureItem(mi.getType());
                }
            }
        }
        refreshTable();
        //在门诊检查的时候就先ping一下ip，确保进入开方后拿到结果是真实情况
        PrinterConn.pingnscanPrinter(MyApplication.getPrinterIp(), AppConfig.PRINTER_PORT);
    }

    public void refreshTable() {
        resident = MyApplication.getInstance().getResident();
        int sex = resident == null ? 1 : resident.getSexy();
        //会导致错误地将无性别居民赋予男
//        if (sex == 2)
//            sex = 1;
        measureAdapter.setSex(sex);
        //尿酸根据男女判断
        if (clinicUtil.ITEM_UA != -1) {
            if (sex == 0) {
                dataList.get(clinicUtil.ITEM_UA).getType().setNormalValue(NormalRange.UA_WOMAN_MIN + "-"
                        + NormalRange.UA_WOMAN_MAX + "μmmol/L");
            } else {
                dataList.get(clinicUtil.ITEM_UA).getType().setNormalValue(NormalRange.UA_MAN_MIN + "-"
                        + NormalRange.UA_MAN_MAX + "μmmol/L");
            }
            measureAdapter.notifyItemChanged(clinicUtil.ITEM_UA);
        }
        //血红蛋白
        if (clinicUtil.ITEM_HB != -1) {
            if (sex == 0) {
                dataList.get(clinicUtil.ITEM_HB).getType().setNormalValue(NormalRange.HB_WOMAN_MIN + "-"
                        + NormalRange.HB_WOMAN_MAX + "g/L" + " " + NormalRange.HCT_WOMAN_MIN + "-"
                        + NormalRange.HCT_WOMAN_MAX + "%");
            } else {
                dataList.get(clinicUtil.ITEM_HB).getType().setNormalValue(NormalRange.HB_MAN_MIN + "-"
                        + NormalRange.HB_MAN_MAX + "g/L" + " " + NormalRange.HCT_MAN_MIN + "-"
                        + NormalRange.HCT_MAN_MAX + "%");
            }
            measureAdapter.notifyItemChanged(clinicUtil.ITEM_HB);
        }
    }

    @Override
    protected void initListener() {
        MeasureUtils.setMeasureBean(dataBean);
        MeasureUtils.setMeasureListen(this, mContext);
        btnClinic.setOnClickListener(this);
//        btnSave.setOnClickListener(this);
        measureAdapter.setOnItemClickListener(this);
        measureAdapter.setOnItemChildClickListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //重新获取context
        mContext = getActivity();
        String config = MyApplication.getInstance().getMeasureConfig();
        //如果当前页面已经测量了 或者配置项没有发生改变 则不执行下面的操作
        if (hasMeasured() || config.equals(measureConfig))
            return;
        measureConfig = config;
        dataList.clear();
        dataList.addAll(MeasureConfig.getMeasureList(mContext));
        measureAdapter.notifyDataSetChanged();
        //重置所有位置
        clinicUtil.resetAllPosition();
        //重新记录每一项的测量位置
        clinicUtil.initPosition(dataList);
    }

    @Override
    public void onClick(View v) {
        //Context 改成 getActivity  0506
        resident = ((ClinicOneActivity) getActivity()).getResident();
        switch (v.getId()) {
            case R.id.btnClinic://门诊开方
//                clickType = 1;
                if (resident == null) {
                    ToastUtil.showShort("请设置当前居民");
                    return;
                }
                //门诊开方的时候如果有测量数据则先保存测量数据 然后进行跳转
                //先上传数据后再跳转到开方
                if (hasMeasured() && !isUpload) {
                    mPresenter.uploadMeasureData(dataId, resident.getPatientId(), resident.getSexy(), measureAdapter.getGluType(), dataBean);
                } else {
                    enterRecipe();
                }
                break;
        }
    }

    //蓝牙连接状态 数据传递
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(MsgEvent event) {
        //蓝牙连接状态
        if (event.getAction() == AppConfig.MSG_CONNECTION_CHANGED) {
            if (clinicUtil.ITEM_DS100A != -1) {
                dataList.get(clinicUtil.ITEM_DS100A).setConnect(MyApplication.getInstance().isFatConnect());
                measureAdapter.notifyItemChanged(clinicUtil.ITEM_DS100A);
            }
            if (clinicUtil.ITEM_WBC != -1) {
                dataList.get(clinicUtil.ITEM_WBC).setConnect(MyApplication.getInstance().isWbcConnect());
                measureAdapter.notifyItemChanged(clinicUtil.ITEM_WBC);
            }
        }
        //蓝牙设备数据
        if (event.getAction() == AppConfig.MSG_MEASURE_DATA) {
            dataBean.setCheckDay(System.currentTimeMillis());
            String data = (String) event.getT();
            Logger.i("lsy", "mData=" + data);
            //血脂数据
            if (data.startsWith(AppConfig.DS100A_DATA_HEAD) && clinicUtil.ITEM_DS100A != -1) {
                clinicUtil.analysisData(data, dataBean);
                dataList.get(clinicUtil.ITEM_DS100A).setMeasured(true);
                dataList.get(clinicUtil.ITEM_DS100A).setMeasureBean(dataBean);
                measureAdapter.notifyItemChanged(clinicUtil.ITEM_DS100A);
                //状态  数据 重置
                isUpload = false;
                stickMeasureItem(Measure.DS100A);
            }
            //白细胞数据
            if (data.startsWith(AppConfig.WBC_DATA_HEAD) && clinicUtil.ITEM_WBC != -1) {
                clinicUtil.analysisWbcData(data, dataBean);
                dataList.get(clinicUtil.ITEM_WBC).setMeasured(true);
                dataList.get(clinicUtil.ITEM_WBC).setMeasureBean(dataBean);
                measureAdapter.notifyItemChanged(clinicUtil.ITEM_WBC);
                //状态  数据 重置
                isUpload = false;
                stickMeasureItem(Measure.WBC);
            }
        }
    }

    /**
     * 测量数据上传成功
     *
     * @param dataId
     */
    @Override
    public void uploadSuccess(String dataId) {
        //测量数据保存到本地
        MyApplication.getInstance().setDataList(dataList);
        saveMeasureDataToLocal(dataId);
        //数据上传成功 重置测量数据 和相关状态
        ClinicOneFragment.dataId = dataId;
        isUpload = true;
        //门诊开方
        //如果不是履约，才进入门诊开方 0424:取消上传成功就回调，与saveDone冲突
        if (openFrom == 0) {
            saveDone();
        } else {
            enterRecipe();
        }
    }

    /**
     * 测量数据上传失败
     */
    @Override
    public void uploadFailed() {
        MyApplication.getInstance().setDataList(dataList);
        saveMeasureDataToLocal("");
        //上传数据失败直接进入门诊开方
        dataId = "";
        isUpload = false;
        enterRecipe();
    }

    private void enterRecipe() {
        if (hasMeasured()) {
            Intent intent = new Intent(mContext, RecipeActivity.class);
            intent.putExtra(MainActivity.KEY_RESIDENT, resident);
            intent.putExtra("dataId", dataId);
            intent.putExtra("bean", dataBean);
            intent.putExtra("openFrom", openFrom);
            startActivityForResult(intent, REQUEST_CODE_RECIPE);
        } else {
            ToastUtil.showShort("尚未进行测量");
        }
    }

    /**
     * 测量数据保存到本地 无dataId的则表示上传失败的数据
     *
     * @return
     */
    private void saveMeasureDataToLocal(String dataId) {
        dataBean.setIdCard(resident.getIdentityCard());
        dataBean.setName(resident.getName());
        dataBean.setPatientId(resident.getPatientId());
        if (measureAdapter.getGluType() == 0) {
            dataBean.setGluStyle(Configuration.BtnFlag.lift);
            dataBean.setGluType(0);
        } else {
            dataBean.setGluStyle(Configuration.BtnFlag.right);
            dataBean.setGluType(1);
        }
        if (TextUtils.isEmpty(dataId)) {
            dataBean.setUpload(false);
        } else {
            dataBean.setUpload(true);
        }
        if (clinicUtil.ITEM_ECG != -1 && dataList.get(clinicUtil.ITEM_ECG).isMeasured())
            dataBean.setHr(dataList.get(clinicUtil.ITEM_ECG).getMeasureBean().getHr());

        if (clinicUtil.ITEM_SPO2 != -1 && dataList.get(clinicUtil.ITEM_SPO2).isMeasured())
            dataBean.setPr(dataList.get(clinicUtil.ITEM_SPO2).getMeasureBean().getPr());

        if (clinicUtil.ITEM_NIBP != -1 && dataList.get(clinicUtil.ITEM_NIBP).isMeasured())
            dataBean.setNibPr(dataList.get(clinicUtil.ITEM_NIBP).getMeasureBean().getNibPr());

        try {
            DBManager.getInstance().getMeasureDao().createOrUpdate(dataBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否有检测数据
     *
     * @return
     */
    public boolean hasMeasured() {
        boolean isSave = false;
        for (MeasureItem item : dataList) {
            if (item.isMeasured()) {
                isSave = true;
                break;
            }
        }
        return isSave;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        //身高测量成功
        if (requestCode == REQUEST_CODE_HEIGHT) {
            isUpload = false;
            String height = data.getStringExtra("data");
            dataBean.setHeight(height);
            dataBean.setCheckDay(System.currentTimeMillis());

            MeasureItem item = dataList.get(clinicUtil.ITEM_HEIGHT);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(clinicUtil.ITEM_HEIGHT);
        }
        //体重测量成功
        if (requestCode == REQUEST_CODE_WEIGHT) {
            isUpload = false;

            String weight = data.getStringExtra("data");
            dataBean.setWeight(weight);
            dataBean.setCheckDay(System.currentTimeMillis());

            MeasureItem item = dataList.get(clinicUtil.ITEM_WEIGHT);
            item.setMeasured(true);
            item.setMeasureBean(dataBean);
            measureAdapter.notifyItemChanged(clinicUtil.ITEM_WEIGHT);
        }
        //血氧测量成功
        if (requestCode == REQUEST_CODE_SPO2) {
            boolean success = data.getBooleanExtra("success", false);
            if (!success) {
                dataList.get(clinicUtil.ITEM_SPO2).setState(MeasureState.MEASURE_READY);
                measureAdapter.notifyItemChanged(clinicUtil.ITEM_SPO2);
            } else {
                isUpload = false;
                MeasureBean measureBean = (MeasureBean) data.getSerializableExtra("measureBean");
                onComplete(Measure.SPO2, measureBean);
            }
        }
        //心电测量成功
        if (requestCode == REQUEST_CODE_ECG) {
            boolean success = data.getBooleanExtra("success", false);
            if (!success) {
                dataList.get(clinicUtil.ITEM_ECG).setState(MeasureState.MEASURE_READY);
                measureAdapter.notifyItemChanged(clinicUtil.ITEM_ECG);
            } else {
                isUpload = false;
                MeasureBean measureBean = (MeasureBean) data.getSerializableExtra("measureBean");
                onComplete(Measure.ECG, measureBean);
            }
        }
        //门诊开方成功
        if (requestCode == REQUEST_CODE_RECIPE) {
            mContext = getActivity();
            saveDone();
            Logger.i("zrl", "ClinicOneFragment result");
        }
    }

    //清空居民及测量数据
    public static void saveDone() {
        //如果是履约测量，则在开方完成后关闭页面，而非重置
        if (openFrom == 0) {
            Intent _intent = new Intent();
            _intent.putExtra("dataId", dataId);
            ((ClinicOneActivity) mContext).setResult(Activity.RESULT_OK, _intent);
            ((ClinicOneActivity) mContext).finish();
        } else {
            clinicUtil.resetAllPosition();
            dataList.clear();
            dataList.addAll(MeasureConfig.getMeasureList(mContext));
            measureAdapter.notifyDataSetChanged();
            clinicUtil.initPosition(dataList);
            MyApplication.getInstance().setDataList(null);
            dataBean = new MeasureBean();
            resident = null;
            dataId = "";
            ((ClinicOneActivity) mContext).resetResident();
        }
    }

    //查看数据
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        resident = ((ClinicOneActivity) mContext).getResident();
        MeasureItem item = (MeasureItem) adapter.getItem(position);
        Measure type = item.getType();
        //心电 尿常规 血脂四项 血脂八项 数据的查看
        if (type == Measure.ECG || type == Measure.URINE || type == Measure.BLOOD || type == Measure.DS100A) {
            if (item.isMeasured()) {
                //先测量后选居民的情况也存在 所以这里再获取一下当前居民信息
                Intent intent = new Intent();
                intent.putExtra("resident", resident);
                if (type == Measure.ECG) {
                    intent.putExtra("measureBean", dataList.get(clinicUtil.ITEM_ECG).getMeasureBean());
                    intent.setClass(mContext, EcgResultActivity.class);
                }
                if (type == Measure.URINE) {
                    intent.putExtra("measureBean", dataList.get(clinicUtil.ITEM_URINE).getMeasureBean());
                    intent.setClass(mContext, UrineResultActivity.class);
                }
                if (type == Measure.BLOOD) {
                    intent.putExtra("type", BloodFatResultActivity.BLOOD_TYPE_4);
                    intent.putExtra("measureBean", dataList.get(clinicUtil.ITEM_BLOOD).getMeasureBean());
                    intent.setClass(mContext, BloodFatResultActivity.class);
                }
                if (type == Measure.DS100A) {
                    intent.putExtra("type", BloodFatResultActivity.BLOOD_TYPE_8);
                    intent.putExtra("measureBean", dataList.get(clinicUtil.ITEM_DS100A).getMeasureBean());
                    intent.setClass(mContext, BloodFatResultActivity.class);
                }
                startActivity(intent);
            } else {
                ToastUtil.showShort("该测量项无数据 不支持查看");
            }
        }
        //方便测试 所造的数据
        if (AppConfig.isDebug) {
            clinicUtil.debugDataTest(type, dataBean, dataList, measureAdapter);
//            if (item.getType() != Measure.ECG || item.getType() != Measure.URINE
//                    || item.getType() != Measure.DS100A || item.getType() != Measure.MYO)
            isUpload = false;
        }
    }

    //测量
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int i) {
        MeasureItem measureItem = (MeasureItem) adapter.getItem(i);
        MeasureState state = measureItem.getState();
        switch (measureItem.getType()) {
            //血压
            case NIBP:
                if (state == MeasureState.MEASURE_ING)
                    measureItem.setState(MeasureState.MEASURE_READY);
                else
                    measureItem.setState(MeasureState.MEASURE_ING);
                measureAdapter.notifyItemChanged(i);
                MeasureUtils.startMeasure(Measure.NIBP);
                break;
            //血氧
            case SPO2:
                Intent intentSpo2 = new Intent(mContext, MeasureItemActivity.class);
                intentSpo2.putExtra("measureItem", measureItem);
                intentSpo2.putExtra("measureBean", dataBean);
                startActivityForResult(intentSpo2, REQUEST_CODE_SPO2);
                break;
            //心电心率
            case ECG:
                measureAdapter.notifyItemChanged(i);
                Intent intentEcg = new Intent(mContext, MeasureItemActivity.class);
                intentEcg.putExtra("measureBean", dataBean);
                intentEcg.putExtra("measureItem", measureItem);
                startActivityForResult(intentEcg, REQUEST_CODE_ECG);
                break;
            //身高
            case HEIGHT:
                Intent intentHeight = new Intent(mContext, InputSettingActivity.class);
                SettingBean heightBean = new SettingBean();
                heightBean.setName("输入身高值（cm）");
                heightBean.setValue("");
                intentHeight.putExtra("settingBean", heightBean);
                intentHeight.putExtra("isHWeight", true);
                startActivityForResult(intentHeight, REQUEST_CODE_HEIGHT);
                break;
            //体重
            case WEIGHT:
                Intent intentWeight = new Intent(mContext, InputSettingActivity.class);
                SettingBean weightBean = new SettingBean();
                weightBean.setName("输入体重值（kg）");
                weightBean.setValue("");
                intentWeight.putExtra("settingBean", weightBean);
                intentWeight.putExtra("isHWeight", true);
                startActivityForResult(intentWeight, REQUEST_CODE_WEIGHT);
                break;
            case DS100A:
                startActivity(new Intent(mContext, BleDeviceActivity.class));
                break;
            //去掉无用的白细胞蓝牙入口，根据需求只保留大树血脂的入口
//            case WBC:
//                startActivity(new Intent(mContext, BleDeviceActivity.class));
//                break;
        }
        //餐前餐后的选择
        if (view.getId() == R.id.rb0) {
            dataBean.setGluStyle(Configuration.BtnFlag.lift);
            measureAdapter.setGluType(0);
            measureAdapter.notifyItemChanged(clinicUtil.ITEM_GLU);
        }
        if (view.getId() == R.id.rb1) {
            dataBean.setGluStyle(Configuration.BtnFlag.right);
            measureAdapter.setGluType(1);
            measureAdapter.notifyItemChanged(clinicUtil.ITEM_GLU);
        }
    }


    @Override
    public void onComplete(Measure param, MeasureBean bean) {
        isUpload = false;
        dataBean.setCheckDay(System.currentTimeMillis());
        clinicUtil.onComplete(param, bean, dataBean, dataList, measureAdapter);
        stickMeasureItem(param);
    }

    @Override
    public void onFail(Measure param, String msg) {
        ToastUtil.showShort(msg);
        clinicUtil.onFail(param, dataList, measureAdapter);
    }

    @Override
    public void NibpCuff(int va) {
    }

    @Override
    public void onState(StateBean bean) {
        clinicUtil.onState(bean, dataList, measureAdapter);
    }


    //收到测量数据后置顶该测量项
    private void stickMeasureItem(Measure param) {
        //血压
        if (param == Measure.NIBP && clinicUtil.ITEM_NIBP != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_NIBP);
            dataList.remove(clinicUtil.ITEM_NIBP);
            dataList.add(0, item);
//            measureAdapter.notifyDataSetChanged();
            if (clinicUtil.ITEM_PR != -1) {
                MeasureItem itemPr = dataList.get(clinicUtil.ITEM_PR);
                dataList.remove(clinicUtil.ITEM_PR);
                dataList.add(1, itemPr);
            }
            measureAdapter.notifyDataSetChanged();
        }
        //血氧
        if (param == Measure.SPO2 && clinicUtil.ITEM_SPO2 != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_SPO2);
            dataList.remove(clinicUtil.ITEM_SPO2);
            dataList.add(0, item);
//            measureAdapter.notifyDataSetChanged();

            if (clinicUtil.ITEM_PR != -1) {
                MeasureItem itemPr = dataList.get(clinicUtil.ITEM_PR);
                dataList.remove(clinicUtil.ITEM_PR);
                dataList.add(1, itemPr);
            }
            measureAdapter.notifyDataSetChanged();

        }
        //心电
        if (param == Measure.ECG && clinicUtil.ITEM_ECG != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_ECG);
            dataList.remove(clinicUtil.ITEM_ECG);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //体温
        if (param == Measure.TEMP && clinicUtil.ITEM_TEMP != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_TEMP);
            dataList.remove(clinicUtil.ITEM_TEMP);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //尿常规
        if (param == Measure.URINE && clinicUtil.ITEM_URINE != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_URINE);
            dataList.remove(clinicUtil.ITEM_URINE);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //血脂四项
        if (param == Measure.BLOOD && clinicUtil.ITEM_BLOOD != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_BLOOD);
            dataList.remove(clinicUtil.ITEM_BLOOD);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //大树血脂
        if (param == Measure.DS100A && clinicUtil.ITEM_DS100A != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_DS100A);
            dataList.remove(clinicUtil.ITEM_DS100A);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //血糖
        if (param == Measure.GLU && clinicUtil.ITEM_GLU != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_GLU);
            dataList.remove(clinicUtil.ITEM_GLU);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //尿酸
        if (param == Measure.UA && clinicUtil.ITEM_UA != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_UA);
            dataList.remove(clinicUtil.ITEM_UA);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //总胆固醇
        if (param == Measure.CHOL && clinicUtil.ITEM_CHOL != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_CHOL);
            dataList.remove(clinicUtil.ITEM_CHOL);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //血红蛋白
        if (param == Measure.HB && clinicUtil.ITEM_HB != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_HB);
            dataList.remove(clinicUtil.ITEM_HB);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //糖化血红蛋白
        if (param == Measure.GHB && clinicUtil.ITEM_GHB != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_GHB);
            dataList.remove(clinicUtil.ITEM_GHB);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //身高
        if (param == Measure.HEIGHT && clinicUtil.ITEM_HEIGHT != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_HEIGHT);
            dataList.remove(clinicUtil.ITEM_HEIGHT);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //体重
        if (param == Measure.WEIGHT && clinicUtil.ITEM_WEIGHT != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_WEIGHT);
            dataList.remove(clinicUtil.ITEM_WEIGHT);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //白细胞
        if (param == Measure.WBC && clinicUtil.ITEM_WBC != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_WBC);
            dataList.remove(clinicUtil.ITEM_WBC);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //C反应蛋白
        if (param == Measure.CRP && clinicUtil.ITEM_CRP != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_CRP);
            dataList.remove(clinicUtil.ITEM_CRP);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //超敏C反应蛋白
        if (param == Measure.Hs_CRP && clinicUtil.ITEM_HSCRP != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_HSCRP);
            dataList.remove(clinicUtil.ITEM_HSCRP);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //血清淀粉样蛋白A
        if (param == Measure.SAA && clinicUtil.ITEM_SAA != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_SAA);
            dataList.remove(clinicUtil.ITEM_SAA);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //降钙素原
        if (param == Measure.PCT && clinicUtil.ITEM_PCT != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_PCT);
            dataList.remove(clinicUtil.ITEM_PCT);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        //心肌三项
        if (param == Measure.MYO && clinicUtil.ITEM_MYO != -1) {
            MeasureItem item = dataList.get(clinicUtil.ITEM_MYO);
            dataList.remove(clinicUtil.ITEM_MYO);
            dataList.add(0, item);
            measureAdapter.notifyDataSetChanged();
        }
        clinicUtil.initPosition(dataList);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //移除测量回调
        MyApplication.getInstance().setDataList(null);
        MeasureUtils.setMeasureListen(null, mContext);
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        //息屏、退出时确保血压停止测量
        if (clinicUtil.ITEM_NIBP != -1) {
            EchoServerEncoder.setNibpConfig((short) 0x06, 0);
            EchoServerEncoder.setNibpConfig((short) 0x06, 0);
            MeasureUtils.measureState = 0;
            dataList.get(clinicUtil.ITEM_NIBP).setState(MeasureState.MEASURE_READY);
            measureAdapter.notifyDataSetChanged();
        }
    }
}
