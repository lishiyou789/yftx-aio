package com.health2world.aio.app.clinic.recipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.adapter.ClinicServiceAdapter;
import com.health2world.aio.app.clinic.ClinicOneFragment;
import com.health2world.aio.app.clinic.result.BloodFatResultActivity;
import com.health2world.aio.app.clinic.result.EcgResultActivity;
import com.health2world.aio.app.clinic.result.UrineResultActivity;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.bean.MeasureItem;
import com.health2world.aio.bean.SignService;
import com.health2world.aio.common.DataServer;
import com.health2world.aio.common.MsgEvent;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.printer.DeviceManagerConnectActivity;
import com.health2world.aio.printer.PrintUtils;
import com.health2world.aio.printer.PrinterA5PreviewActivity;
import com.health2world.aio.printer.PrinterConn;
import com.health2world.aio.util.DefaultTextWatcher;
import com.health2world.aio.util.Logger;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;
import com.konsung.listen.Measure;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.glide_transformations.CropCircleTransformation;
import aio.health2world.utils.ToastUtil;

import static com.health2world.aio.MyApplication.isBentuUsbPrinterConnect;

/**
 * 门诊开方
 * Created by lishiyou on 2018/7/26 0026.
 */

public class RecipeActivity extends MVPBaseActivity<RecipeContract.Presenter> implements
        RecipeContract.View, BaseQuickAdapter.OnItemClickListener, PrintUtils.printCallback {
    private static final int REQUEST_SYMP = 0x11;
    private static final int REQUEST_TEMP = 0x22;
    private static final int REQUEST_SREC = 0x33;
    private static final int REQUEST_SCAN = 0x34;
    public static final int REQUEST_USB_PRINTER = 0x12;
    public static final int REQUEST_NET_PRINTER = 0x13;

//    private TitleBar titleBar;

    private EditText edDescription, edSuggest;

    private TextView tvSymptom;
    private TextView tvDescriptionCount;
    private TextView tvTemplate;
    private TextView tvSuggestCount;
    private TextView tvResidentName;
    private TextView tvResidentAge;
    private TextView tvResidentSex;
    private TextView tvResidentPhone;
    private TextView tvResidentCode;
    private TextView tvIsPrintConn;

    private ImageView ivResidentHead;

    private Button btnSave, btnPointWithSave;

//    private int openFrom = 1;//1 正常进入门诊检查  0 履约测量

    private RecyclerView recyclerView;
    private List<SignService> serviceList;
    private ClinicServiceAdapter serviceAdapter;
    private RecipeMeasureAdapter measureAdapter;

    //居民数据
    private ResidentBean resident;
    //测量数据
    private List<MeasureItem> dataList = new ArrayList<>();
    private MeasureBean dataBean;
    //数据上传之后的dataId
    private String dataId;
    //记录点击的是手机开方还是保存 1：手机开方；2：保存
    private int clinicType = 1;
    //    private UsbManager mUsbManager;
    //用于判断是否正在打印,正在打印中不可返回
    private boolean isPrinting;
    private ProgressDialog mDialog;
    private PrintUtils mPrintUtils;
    private boolean isStart;

    @Override
    protected RecipeContract.Presenter getPresenter() {
        return new RecipePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        //每当重回这个门诊测量页面，先ping一下
        PrinterConn.pingnscanPrinter(MyApplication.getPrinterIp(), AppConfig.PRINTER_PORT);
        //PrintUtils监听
        mPrintUtils = new PrintUtils();
        mPrintUtils.setPrintUtils(this, this);
        return R.layout.activity_prescribing;
    }

    @Override
    protected void initView() {
        edDescription = findView(R.id.edDescription);
        edSuggest = findView(R.id.edSuggest);
        tvSymptom = findView(R.id.tvSymptom);
        tvDescriptionCount = findView(R.id.tvDescriptionCount);
        tvTemplate = findView(R.id.tvTemplate);
        tvSuggestCount = findView(R.id.tvSuggestCount);
        tvResidentName = findView(R.id.tv_resident_name);
        tvResidentAge = findView(R.id.tv_resident_age);
        tvResidentSex = findView(R.id.tv_resident_sex);
        tvResidentPhone = findView(R.id.tv_resident_phone);
        tvResidentCode = findView(R.id.tv_resident_code);
        ivResidentHead = findView(R.id.iv_resident_head);
        btnSave = findView(R.id.btnSave);
        btnPointWithSave = findView(R.id.btnPointWithSave);
        recyclerView = findView(R.id.recyclerView);
        tvIsPrintConn = findView(R.id.tv_isPrinterConn);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在打印...");
        mDialog.setCancelable(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        isPrinting = false;

        if (getIntent().hasExtra(MainActivity.KEY_RESIDENT))
            resident = (ResidentBean) getIntent().getSerializableExtra(MainActivity.KEY_RESIDENT);

        if (getIntent().hasExtra("dataId"))
            dataId = getIntent().getStringExtra("dataId");

        if (getIntent().hasExtra("bean"))
            dataBean = (MeasureBean) getIntent().getSerializableExtra("bean");

        //获取已测量项
        List<MeasureItem> list = MyApplication.getInstance().getDataList();
        //过滤没有数据的测量项
        if (list != null) {
            for (MeasureItem item : list) {
                if (item.isMeasured())
                    dataList.add(item);
            }
        }
        serviceList = new ArrayList<>();
        serviceAdapter = new ClinicServiceAdapter(serviceList);

        measureAdapter = new RecipeMeasureAdapter(dataList, resident.getSexy());
        recyclerView.setAdapter(measureAdapter);
        measureAdapter.bindToRecyclerView(recyclerView);
        measureAdapter.setEmptyView(R.layout.layout_empty_view);

        tvResidentName.setText(resident.getName());
        tvResidentAge.setText("年龄：" + (resident.getAge() <= 0 ? "--" : resident.getAge()));
        tvResidentSex.setText("性别：" + DataServer.getSexNick(resident.getSexy()));
        tvResidentPhone.setText("手机号：" + resident.getTelPhone());
        tvResidentCode.setText("居民码：" + resident.getResidentCode());
        Glide.with(mContext)
                .load(resident.getPortrait())
                .placeholder(R.mipmap.user_portrait_circle)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(ivResidentHead);


        //优先级：奔图、PC客户端、热敏
        if (MyApplication.isBentuUsbPrinterConnect) {
            tvIsPrintConn.setText("奔图打印机\n已连接");
            tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
        } else if (MyApplication.isNetPrinterConnnect) {
            tvIsPrintConn.setText("电脑客户端\n已连接");
            tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
        } else if (mPrintUtils.getPrinterDevice() != null) {
            tvIsPrintConn.setText("热敏打印机\n已连接");
            tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
        } else {
            tvIsPrintConn.setText("无打印机\n点击连接");
            tvIsPrintConn.setTextColor(getResources().getColor(R.color.red));
        }

    }

    @Override
    protected void initListener() {
        //DeviceManager监听
        EventBus.getDefault().register(this);

        setOnClickListener(tvSymptom);
        setOnClickListener(tvTemplate);
        setOnClickListener(btnSave);
        setOnClickListener(btnPointWithSave);
        measureAdapter.setOnItemClickListener(this);

        edDescription.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                tvDescriptionCount.setText(s.length() + "/100");
            }
        });

        edSuggest.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                tvSuggestCount.setText(s.length() + "/255");
            }
        });

        tvIsPrintConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, DeviceManagerConnectActivity.class);
                startActivityForResult(intent, REQUEST_SCAN);
            }
        });
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MeasureItem item = (MeasureItem) adapter.getItem(position);
        Measure type = item.getType();
        //心电 尿常规 血脂四项 血脂八项 数据的查看
        if (type == Measure.ECG || type == Measure.URINE || type == Measure.BLOOD || type == Measure.DS100A) {
            Intent intent = new Intent();
            intent.putExtra("measureBean", item.getMeasureBean());
            intent.putExtra("resident", resident);
            if (type == Measure.ECG)
                intent.setClass(mContext, EcgResultActivity.class);
            if (type == Measure.URINE)
                intent.setClass(mContext, UrineResultActivity.class);
            if (type == Measure.BLOOD || type == Measure.DS100A) {
                intent.setClass(mContext, BloodFatResultActivity.class);
                intent.putExtra("type", type == Measure.BLOOD ?
                        BloodFatResultActivity.BLOOD_TYPE_4 : BloodFatResultActivity.BLOOD_TYPE_8);
            }
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSymptom://常见症状
                String[] symptoms = getResources().getStringArray(R.array.array_symptoms);
                Intent intent0 = new Intent(this, SelectDialogActivity.class);
                intent0.putExtra(SelectDialogActivity.KEY_TYPE, 0);
                intent0.putExtra(SelectDialogActivity.KEY_ARRAY, symptoms);
                startActivityForResult(intent0, REQUEST_SYMP);
                break;
            case R.id.tvTemplate://选择模板
                String[] templates = getResources().getStringArray(R.array.array_templates);
                Intent intent1 = new Intent(this, SelectDialogActivity.class);
                intent1.setAction(Intent.ACTION_VIEW);
                intent1.putExtra(SelectDialogActivity.KEY_TYPE, 1);
                intent1.putExtra(SelectDialogActivity.KEY_ARRAY, templates);
                startActivityForResult(intent1, REQUEST_TEMP);
                break;

            //保存和打印相同逻辑，用是否填写医嘱建议来判断是否为开方还是普通保存
            //若已填写医嘱建议则为原保存逻辑，若未填写则为原手机开方逻辑
            //保存完成后需要回到门诊检查页面!
            case R.id.btnSave://保存
                phoneClinic();
                break;
            case R.id.btnPointWithSave://打印并保存
                if (isPrinting || (tvIsPrintConn.getCurrentTextColor() != getResources().getColor(R.color.bright_blue))) {
                    return;
                }
                printMeasureData();
                break;
        }
    }

    //屏蔽物理返回按钮
    @Override
    public void onBackPressed() {
//        if (!isPrinting) {
//            super.onBackPressed();
//        }
    }

    /**
     * 按照途牛、网络打印机、热敏打印机的优先级进行打印
     */
    private void printMeasureData() {
        isPrinting = true;
        mDialog.show();
        mCountDownTimer.start();

        if (MyApplication.isBentuUsbPrinterConnect) {
            /*奔图USB打印机*/
            //先生成预览图 再回调打印
            Intent intent = new Intent(RecipeActivity.this, PrinterA5PreviewActivity.class);
            intent.putExtra("resident", resident);
            intent.putExtra("measure", dataBean);
            startActivityForResult(intent, REQUEST_USB_PRINTER);

        } else if (MyApplication.isNetPrinterConnnect) {
            /*网络打印机*/
            //先生成预览图 再回调打印
            Intent intent = new Intent(RecipeActivity.this, PrinterA5PreviewActivity.class);
            intent.putExtra("resident", resident);
            intent.putExtra("measure", dataBean);
            startActivityForResult(intent, REQUEST_NET_PRINTER);

        } else {
            /*热敏打印机*/
            mPrintUtils.printByRM(resident, dataBean);
        }
    }

    private void printByBenTu() {
        if (!MyApplication.isBentuUsbPrinterConnect) {
            isPrinting = false;
            if (mDialog.isShowing())
                mDialog.dismiss();
            return;
        }
        mPrintUtils.printByBenTu();
    }

    private void printByNet() {
        if (PrinterConn.send()) {
            ToastUtil.showLong("网络客户端发送成功，请稍后");
            if (mDialog.isShowing())
                mDialog.dismiss();
            phoneClinic();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(MsgEvent event) {
        if (event.getAction() == AppConfig.MSG_PRINTER_STATUS) {
            //接收到设备连接状态改变或打印结果时，关掉弹窗
            if (mDialog.isShowing())
                mDialog.dismiss();
            int[] paramValueArr = (int[]) event.getT();
            Logger.d("zrl", "paramValueArr: " + Arrays.toString(paramValueArr));
            //接收服务的打印监听
            switch (paramValueArr[0]) {
                case 0x00://状态改变
                    switch (paramValueArr[1]) {
                        case 0://USB已连接
                            //只检查USB端口
                            //在0x01中才能说明真正已连接，一般连接奔图时会同时先后发送 0x00:0和0x01:0
//                            tvIsPrintConn.setText("奔图打印机\n已连接");
//                            tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
                            break;
                        case 1://USB已断开
                            isPrinting = false;
                            if (MyApplication.isNetPrinterConnnect) {
                                tvIsPrintConn.setText("电脑客户端\n已连接");
                                tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
                            } else if (mPrintUtils.getPrinterDevice() != null) {
                                tvIsPrintConn.setText("热敏打印机\n已连接");
                                tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
                            } else {
                                tvIsPrintConn.setText("无打印机\n点击连接");
                                tvIsPrintConn.setTextColor(getResources().getColor(R.color.red));
                            }
                            break;
                        case 2:
                            tvIsPrintConn.setText("重新拔插\nUSB");
                            tvIsPrintConn.setTextColor(getResources().getColor(R.color.red_focused));
                            break;
                        default:
                            break;
                    }
                    break;
                case 0x01://连接断开
                    switch (paramValueArr[1]) {
                        case 0:
                            //先确认下MeasureService收到了这个信息
                            if (isBentuUsbPrinterConnect) {
                                tvIsPrintConn.setText("奔图打印机\n已连接");
                                tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
                            } else {
                                tvIsPrintConn.setText("重新拔插\nUSB");
                                tvIsPrintConn.setTextColor(getResources().getColor(R.color.red_focused));
                            }
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        default:
                            break;
                    }
                    break;
                case 0x02://打印结果
                    switch (paramValueArr[1]) {
                        case 0:
//                            Logger.d("zrl", "打印成功");
                            ToastUtil.showShort("打印成功");
                            phoneClinic();
                            break;
                        //打印失败情况放开返回限制
                        case 1:
//                            Logger.d("zrl", "文件不存在");
//                            break;
                        case 2:
//                            Logger.d("zrl", "不支持该命令");
//                            break;
                        case 3:
//                            Logger.d("zrl", "文件路径过长");
//                            break;
                        case 4:
                            tvIsPrintConn.setText("重新拔插\nUSB");
                            tvIsPrintConn.setTextColor(getResources().getColor(R.color.red_focused));
                            isPrinting = false;
//                            break;
                        case 255:
//                            Logger.d("zrl", "未知错误");

                        default:
                            ToastUtil.showShort("未知打印错误");
                            isPrinting = false;
                            break;
                    }
                    break;

                default:
                    break;

            }
        }
        //异步接收网络打印机连接状态
        else if (event.getAction() == AppConfig.MSG_NET_PRINTER_STATUS) {

            if (MyApplication.isBentuUsbPrinterConnect) {
                tvIsPrintConn.setText("奔图打印机\n已连接");
                tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
            } else if (MyApplication.isNetPrinterConnnect) {
                tvIsPrintConn.setText("电脑客户端\n已连接");
                tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
            } else if (mPrintUtils.getPrinterDevice() != null) {
                tvIsPrintConn.setText("热敏打印机\n已连接");
                tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
            } else {
                tvIsPrintConn.setText("无打印机\n点击连接");
                tvIsPrintConn.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }

    //PrintUtils 返回热敏打印结果
    @Override
    public void printStatus(boolean isSuccess) {
        if (isSuccess) {
//            ToastUtil.showShort("打印成功");
            if (mDialog.isShowing() && isStart)
                mDialog.dismiss();
            phoneClinic();
        } else {
            ToastUtil.showShort("打印失败");
            if (mDialog.isShowing() && isStart)
                mDialog.dismiss();
        }
        isPrinting = false;
    }

    //PrintUtils 返回打印机连接结果
    @Override
    public void isPrinterConnected(boolean isConnected) {
        if (isConnected) {
            tvIsPrintConn.setText("热敏打印机\n已连接");
            tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
        } else {
            tvIsPrintConn.setText("无打印机\n点击连接");
            tvIsPrintConn.setTextColor(getResources().getColor(R.color.red));
        }
    }

    //手机开方
    public void phoneClinic() {
        //打印完成后才进入此开方方法，所以放开返回限制
        isPrinting = false;
        //无网络状态保存直接关闭
        if (dataId.equals("")) {
            setResult(RESULT_OK);
            if (TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_4_4_2))
                ClinicOneFragment.saveDone();
            finish();
        } else {
            //用是否输入医嘱来判断： 1创建任务  2创建报告
            clinicType = edSuggest.getText().length() == 0 ? 1 : 2;
            if (mPresenter != null) {
                mPresenter.phoneClinic(resident.getPatientId(), dataId, clinicType,
                        edDescription.getText().toString().trim(), edSuggest.getText().toString().trim());
            }
        }
    }

    //手机开方成功
    @Override
    public void phoneClinicSuccess() {
        MyApplication.getInstance().setDataList(null);
        ToastUtil.showShort("开方成功");
        setResult(RESULT_OK);
        if (TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_4_4_2))
            ClinicOneFragment.saveDone();
        finish();
    }

    //手机开方失败
    @Override
    public void phoneClinicFailed() {
        ToastUtil.showLong("开方失败");
        //开方失败也返回去，但愿不会执行
        setResult(RESULT_OK);
        if (TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_4_4_2))
            ClinicOneFragment.saveDone();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String selectedList;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SYMP:
                    selectedList = data.getStringExtra(SelectDialogActivity.KEY_DATA);
                    edDescription.setText(edDescription.getText().append(selectedList));
                    edDescription.setSelection(edDescription.getText().length());
                    break;
                case REQUEST_TEMP:
                    selectedList = data.getStringExtra(SelectDialogActivity.KEY_DATA);
                    edSuggest.setText(edSuggest.getText().append(selectedList));
                    edSuggest.setSelection(edSuggest.getText().length());
                    break;
                case REQUEST_SREC:
                    List<SignService> list = (ArrayList) data.getSerializableExtra("result");
                    serviceList.clear();
                    serviceList.addAll(list);
                    serviceAdapter.notifyDataSetChanged();
                    break;
                //从打印机连接页面回来
                case REQUEST_SCAN:
                    if (MyApplication.isBentuUsbPrinterConnect) {
                        tvIsPrintConn.setText("奔图打印机\n已连接");
                        tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
                    } else if (MyApplication.isNetPrinterConnnect) {
                        tvIsPrintConn.setText("电脑客户端\n已连接");
                        tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
                    } else if (mPrintUtils.getPrinterDevice() != null) {
                        tvIsPrintConn.setText("热敏打印机\n已连接");
                        tvIsPrintConn.setTextColor(getResources().getColor(R.color.bright_blue));
                    } else {
                        tvIsPrintConn.setText("无打印机\n点击连接");
                        tvIsPrintConn.setTextColor(getResources().getColor(R.color.red));
                    }
                    break;
                //使用USB打印机时的生成打印图片
                case REQUEST_USB_PRINTER:
                    printByBenTu();
                    break;
                //使用网络打印机时的生成打印图片
                case REQUEST_NET_PRINTER:
                    printByNet();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isPrinting = false;
                        if (!isFinishing()) {
                            mDialog.dismiss();
                        }
                        ToastUtil.showLong("打印超时，请拔插打印机");
                    }
                });
            }
        }

    };

    @Override
    protected void onStart() {
        super.onStart();
        isStart = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isStart = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
        EventBus.getDefault().unregister(this);
        isStart = false;
    }
}
