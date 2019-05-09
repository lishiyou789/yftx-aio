package com.health2world.aio.app.history.report;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.clinic.result.CommonResultActivity;
import com.health2world.aio.app.clinic.result.EcgResultActivity;
import com.health2world.aio.app.history.HistoryRecordActivity;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.bean.MedicalReport;
import com.health2world.aio.common.mvp.MVPBaseFragment;
import com.health2world.aio.config.MedicalConstant;
import com.health2world.aio.config.NormalRange;
import com.health2world.aio.printer.PrintUtils;
import com.health2world.aio.printer.PrinterA5PreviewActivity;
import com.health2world.aio.printer.PrinterConn;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;
import com.konsung.constant.Configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.ToastUtil;

import static android.app.Activity.RESULT_OK;
import static com.health2world.aio.app.clinic.recipe.RecipeActivity.REQUEST_NET_PRINTER;
import static com.health2world.aio.app.clinic.recipe.RecipeActivity.REQUEST_USB_PRINTER;

/**
 * 健康报告
 * Created by lishiyou on 2018/8/2 0002.
 */

public class HistoryReportFragment extends MVPBaseFragment<HistoryReportContract.Presenter> implements
        HistoryReportContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemChildClickListener {

    private ResidentBean resident;

    private List<MedicalReport> beanList = new ArrayList<>();

    private HistoryReportAdapter reportAdapter;

    private RecyclerView recyclerView;

    private SwipeRefreshLayout refreshLayout;

    private int pageIndex = 1;

    private String startTime = "", endTime = "";

    private ProgressDialog mDialog;

    private PrintUtils mPrintUtils;

    @Override
    protected HistoryReportContract.Presenter getPresenter() {
        return new HistoryReportPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler_view;
    }

    @Override
    protected void initView() {
        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        refreshLayout = findView(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mDialog = HistoryRecordActivity.mDialog;
    }

    @Override
    protected void initData() {
        if (getArguments().containsKey("resident"))
            resident = (ResidentBean) getArguments().getSerializable("resident");

        reportAdapter = new HistoryReportAdapter(beanList);
        recyclerView.setAdapter(reportAdapter);
        reportAdapter.bindToRecyclerView(recyclerView);
        reportAdapter.setEmptyView(R.layout.layout_empty_view);

        mPrintUtils = new PrintUtils();

        //加载健康报告数据
        onRefresh();
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshListener(this);
        reportAdapter.setOnItemChildClickListener(this);
        reportAdapter.setOnLoadMoreListener(this, recyclerView);
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        refreshLayout.setRefreshing(true);
        mPresenter.loadHealthReport(resident.getPatientId(), startTime, endTime, pageIndex);
    }

    public void refresh(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        pageIndex = 1;
        refreshLayout.setRefreshing(true);
        mPresenter.loadHealthReport(resident.getPatientId(), startTime, endTime, pageIndex);
    }

    public void resetTime() {
        startTime = "";
        endTime = "";
        onRefresh();
    }

    @Override
    public void onLoadMoreRequested() {
        pageIndex++;
        mPresenter.loadHealthReport(resident.getPatientId(), startTime, endTime, pageIndex);
    }

    @Override
    public void loadHealthReportSuccess(List<MedicalReport> reportList) {
        refreshLayout.setRefreshing(false);
        if (pageIndex == 1)
            beanList.clear();
        beanList.addAll(reportList);
        reportAdapter.notifyDataSetChanged();

        if (reportList.size() >= 5) {
            reportAdapter.loadMoreComplete();
        } else {
            reportAdapter.loadMoreEnd();
        }
    }

    @Override
    public void loadHealthReportError(Throwable throwable) {
        refreshLayout.setRefreshing(false);
        if (pageIndex > 1) {
            pageIndex--;
            reportAdapter.loadMoreFail();
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        MedicalReport report = (MedicalReport) adapter.getItem(position);

        List<MedicalData> list = report.getCheckDataOuts();
        List<MedicalData> listData = new ArrayList<>();
        List<MedicalData> listEcg = new ArrayList<>();

        for (MedicalData data : list) {
            if (data.getCheckKindCode().equals(MedicalConstant.LEAD_ECG))
                listEcg.add(data);
            else
                listData.add(data);
        }
        //查看测量数据
        if (view.getId() == R.id.tvResult) {
            Intent intent = new Intent(getActivity(), CommonResultActivity.class);
            intent.putExtra("data", (Serializable) listData);
            startActivity(intent);
        }
        //查看心电数据
        if (view.getId() == R.id.tvEcg) {
            String filePath = "";
            String result = "";
            for (MedicalData data : listEcg) {
                if (data.getCheckTypeCode().equals(MedicalConstant.FILE_PATH))
                    filePath = data.getValue();
                if (data.getCheckTypeCode().equals(MedicalConstant.CHECK_RESULT))
                    result = data.getValue();
            }
            MeasureBean bean = new MeasureBean();
            bean.setFilePath(filePath);
            bean.setAnal(result);
            Intent intent = new Intent(mContext, EcgResultActivity.class);
            intent.putExtra("measureBean", bean);
            intent.putExtra("resident", resident);
            startActivity(intent);
        }

        //报告打印
        if (view.getId() == R.id.btnPrint) {
            if (listData.size() == 0) {
                ToastUtil.showShort("暂无测量数据");
                return;
            }
            mDialog.show();
            mCountDownTimer.start();
            MeasureBean bean = new MeasureBean();
            for (MedicalData data : listData) {
                switch (data.getCheckTypeCode()) {
                    case MedicalConstant.SBP:
                        bean.setSbp(Integer.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.DBP:
                        bean.setDbp(Integer.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.ECG_HR:
                        bean.setHr(Integer.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.OXYGEN_PR:
                        bean.setPr(Integer.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.BLOOD_PR:
                        bean.setNibPr(Integer.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.SUGAR:
                        //餐前血糖
                        bean.setGluType(0);
                        bean.setGluStyle(Configuration.BtnFlag.lift);
                        bean.setGlu(Float.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.SUGAR_PBG:
                        //餐后血糖
                        bean.setGluType(1);
                        bean.setGluStyle(Configuration.BtnFlag.right);
                        bean.setGlu(Float.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.OXYGEN_SPO2:
                        bean.setSpo2(Integer.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.TEMPERATURE:
                        bean.setTemp(Float.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.URINE_BIL:
                        bean.setUrineBil(data.getValue());
                        break;
                    case MedicalConstant.URINE_BLD:
                        bean.setUrineBld(data.getValue());
                        break;
                    case MedicalConstant.URINE_CA:
                        bean.setUrineCa(data.getValue());
                        break;
                    case MedicalConstant.URINE_CR:
                        bean.setUrineCre(data.getValue());
                        break;
                    case MedicalConstant.URINE_GLU:
                        bean.setUrineGlu(data.getValue());
                        break;
                    case MedicalConstant.URINE_KET:
                        bean.setUrineKet(data.getValue());
                        break;
                    case MedicalConstant.URINE_LEU:
                        bean.setUrineLeu(data.getValue());
                        break;
                    case MedicalConstant.URINE_MA:
                        bean.setUrineMa(data.getValue());
                        break;
                    case MedicalConstant.URINE_NIT:
                        bean.setUrineNit(data.getValue());
                        break;
                    case MedicalConstant.URINE_PH:
                        bean.setUrinePh(Float.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.URINE_PRO:
                        bean.setUrinePro(data.getValue());
                        break;
                    case MedicalConstant.URINE_SG:
                        bean.setUrineSg(Double.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.URINE_UBG:
                        bean.setUrineUbg(data.getValue());
                        break;
                    case MedicalConstant.URINE_VC:
                        bean.setUrineVc(data.getValue());
                        break;
                    case MedicalConstant.FAT_FLIPIDSCHOL:
                        bean.setBlood_tc(data.getValue());
                        break;
                    case MedicalConstant.FAT_FLIPIDSGLU:
                        bean.setGluTree(data.getValue());
                        break;
                    case MedicalConstant.FAT_FLIPIDSHDL:
                        bean.setBlood_hdl(data.getValue());
                        break;
                    case MedicalConstant.FAT_FLIPIDSLDL:
                        bean.setBlood_ldl(data.getValue());
                        break;
                    case MedicalConstant.FAT_FLIPIDSTRIG:
                        bean.setBlood_tg(data.getValue());
                        break;
                    case MedicalConstant.VLDL_C:
                        bean.setBlood_vldl(data.getValue());
                        break;
                    case MedicalConstant.AI:
                        bean.setBlood_ai(data.getValue());
                        break;
                    case MedicalConstant.R_CHD:
                        bean.setBlood_r_chd(data.getValue());
                        break;
                    case MedicalConstant.HEIGHT:
                        bean.setHeight(data.getValue());
                        break;
                    case MedicalConstant.WEIGHT:
                        bean.setWeight(data.getValue());
                        break;
                    case MedicalConstant.INDLAMMATION_CRP_CHILD: //儿童CRP
                        bean.setFia_crp(data.getValue());
                        break;
                    case MedicalConstant.INDLAMMATION_CRP:
                        bean.setFia_crp(data.getValue());
                        break;
                    case MedicalConstant.INDLAMMATION_HSCRP:
                        bean.setFia_hscrp(data.getValue());
                        break;
                    case MedicalConstant.INDLAMMATION_PCT:
                        bean.setFia_pct(data.getValue());
                        break;
                    case MedicalConstant.INDLAMMATION_SAA:
                        bean.setFia_saa(data.getValue());
                        break;
                    case MedicalConstant.MYOCARDIUM_CKMB:
                        bean.setFia_ckmb(data.getValue());
                        break;
                    case MedicalConstant.MYOCARDIUM_CTNI:
                        bean.setFia_ctnl(data.getValue());
                        break;
                    case MedicalConstant.MYOCARDIUM_MYO:
                        bean.setFia_myo(data.getValue());
                        break;
                    case MedicalConstant.MD_URIC_ACID_WOMAN:
                        bean.setUricacid(Math.round(Float.parseFloat(data.getValue())));
                        resident.setSexy(0);
                        break;
                    case MedicalConstant.MD_URIC_ACID_MAN:
                        bean.setUricacid(Math.round(Float.parseFloat(data.getValue())));
                        resident.setSexy(1);
                        break;
                    case MedicalConstant.WBC_WBC:
                        bean.setWbc(Float.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.WBC_BAS:
                        bean.setWbc_bas(Float.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.WBC_EOS:
                        bean.setWbc_eos(Float.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.WBC_LYM:
                        bean.setWbc_lym(Float.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.WBC_MON:
                        bean.setWbc_mon(Float.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.WBC_NEU:
                        bean.setWbc_neu(Float.valueOf(data.getValue()));
                        break;
                    case MedicalConstant.ASSXHCT_MAN:
                        bean.setAssxhct(Integer.valueOf(data.getValue()));
                        resident.setSexy(1);
                        break;
                    case MedicalConstant.ASSXHCT_WOMAN:
                        bean.setAssxhct(Integer.valueOf(data.getValue()));
                        resident.setSexy(0);
                        break;
                    case MedicalConstant.ASSXHDB_MAN:
                        bean.setAssxhb(Integer.valueOf(data.getValue()));
                        resident.setSexy(1);
                        break;
                    case MedicalConstant.ASSXHDB_WOMAN:
                        bean.setAssxhb(Integer.valueOf(data.getValue()));
                        resident.setSexy(0);
                        break;
                    case MedicalConstant.GHB_EAG:
                        bean.setEag(data.getValue());
                        break;
                    case MedicalConstant.GHB_IFCC:
                        bean.setIfcc(data.getValue());
                        break;
                    case MedicalConstant.GHB_NGSP:
                        bean.setNgsp(data.getValue());
                        break;

                    default:
                        break;

                }

            }
            PrintData(bean);
        }
    }

    public void PrintData(MeasureBean dataBean) {
        // || 判断到第一个为真时，直接执行下方代码
        if (MyApplication.isBentuUsbPrinterConnect || MyApplication.isNetPrinterConnnect) {
            /*奔图USB打印机*/
            //先生成预览图 再回调打印
            Intent intent = new Intent(mContext, PrinterA5PreviewActivity.class);
            intent.putExtra("resident", resident);
            intent.putExtra("measure", dataBean);
            startActivityForResult(intent,
                    MyApplication.isBentuUsbPrinterConnect ? REQUEST_USB_PRINTER : REQUEST_NET_PRINTER);

        } else {
            /*热敏打印机*/
            mPrintUtils.printByRM(resident, dataBean);
            mDialog.dismiss();
        }
    }

    //86机型（5.1系统）回调到这里
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //使用USB打印机时的生成打印图片
                case REQUEST_USB_PRINTER:
                    mPrintUtils.printByBenTu();
                    if (mDialog.isShowing())
                        mDialog.dismiss();
                    break;
                //使用网络打印机时的生成打印图片
                case REQUEST_NET_PRINTER:
                    if (PrinterConn.send()) {
                        ToastUtil.showLong("网络客户端发送成功，请稍后");
                        if (mDialog.isShowing())
                            mDialog.dismiss();
                    }
                    break;
            }
        }
    }

    //计时器 30秒钟倒计时 检查打印状态
    private CountDownTimer mCountDownTimer = new CountDownTimer(30 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
                ToastUtil.showLong("打印超时，请拔插打印机");
            }
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
    }
}
