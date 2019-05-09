package com.health2world.aio.app.task;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.app.clinic.recipe.SelectDialogActivity;
import com.health2world.aio.app.clinic.result.CommonResultActivity;
import com.health2world.aio.app.clinic.result.EcgResultActivity;
import com.health2world.aio.app.history.data.HistoryDataAdapter;
import com.health2world.aio.bean.HistoryData;
import com.health2world.aio.bean.MedicalData;
import com.health2world.aio.bean.TaskDetail;
import com.health2world.aio.bean.TaskInfo;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.config.MedicalConstant;
import com.health2world.aio.util.DefaultTextWatcher;
import com.health2world.aio.util.TitleBarUtil;
import com.health2world.aio.view.TitleBar;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.ToastUtil;

/**
 * Created by lishiyou on 2018/7/26 0026.
 */

public class TaskServiceActivity extends MVPBaseActivity<TaskServiceContract.Presenter> implements
        TaskServiceContract.View, BaseQuickAdapter.OnItemClickListener {

    private static final int REQUEST_SYMP = 0x11;
    private static final int REQUEST_TEMP = 0x22;

    private TitleBar titleBar;

    private EditText edDescription, edSuggest;

    private TextView tvSymptom, tvDescriptionCount, tvTemplate, tvSuggestCount;

    private Button btnOver, btnKeep;

    private RecyclerView recyclerView;

    private TaskServiceAdapter serviceAdapter;

    private List<HistoryData> dataList = new ArrayList<>();

    private TaskDetail detail;

    private TaskInfo taskBean;

    private ResidentBean resident;

    //0继续监测  1 结束任务
    private int serviceStatus = 1;

    @Override
    protected TaskServiceContract.Presenter getPresenter() {
        return new TaskServicePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_task_service;
    }

    @Override
    protected void initView() {
        titleBar = findView(R.id.titleBar);
        edDescription = findView(R.id.edDescription);
        edSuggest = findView(R.id.edSuggest);
        tvSymptom = findView(R.id.tvSymptom);
        tvDescriptionCount = findView(R.id.tvDescriptionCount);
        tvTemplate = findView(R.id.tvTemplate);
        tvSuggestCount = findView(R.id.tvSuggestCount);
        btnOver = findView(R.id.btnOver);
        btnKeep = findView(R.id.btnKeep);
        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("taskBean")) {
            taskBean = (TaskInfo) getIntent().getSerializableExtra("taskBean");
            TitleBarUtil.setAttr(this, "服务TA", "", titleBar);
        }

        if (getIntent().hasExtra("resident"))
            resident = (ResidentBean) getIntent().getSerializableExtra("resident");

        //待开方界面的底部按钮有区别
        if (taskBean.getTaskStatus().equals("1")) {
            btnKeep.setVisibility(View.GONE);
            btnOver.setText(getString(R.string.save));
        }

        serviceAdapter = new TaskServiceAdapter(dataList);
        recyclerView.setAdapter(serviceAdapter);
        serviceAdapter.bindToRecyclerView(recyclerView);
        serviceAdapter.setEmptyView(R.layout.layout_empty_view);

        mPresenter.getTaskDetail(String.valueOf(taskBean.getTaskId()));

    }

    @Override
    protected void initListener() {
        setOnClickListener(tvSymptom);
        setOnClickListener(tvTemplate);
        setOnClickListener(btnOver);
        setOnClickListener(btnKeep);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

        serviceAdapter.setOnItemClickListener(this);
    }

    //加载任务详细信息成功回调
    @Override
    public void loadTaskDetailSuccess(TaskDetail detail) {
        if (detail != null) {
            this.detail = detail;
            edDescription.setText(detail.getIllnessContent());
            edSuggest.setText(detail.getAdviceDoctor());
            titleBar.setTitle(detail.getPatientName());
        }
        dataList.addAll(detail.getRecords());
        serviceAdapter.notifyDataSetChanged();
    }

    //任务处理成功回调
    @Override
    public void executeTaskSuccess() {
        ToastUtil.showShort(getString(R.string.action_success));
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        HistoryData history = (HistoryData) adapter.getItem(position);
        //血脂 尿常规 白细胞 糖化血红蛋白
        if (history.getCheckKindCode().equals(MedicalConstant.BLOOD_FAT)
                || history.getCheckKindCode().equals(MedicalConstant.URINE)
                || history.getCheckKindCode().equals(MedicalConstant.WBC)
                || history.getCheckKindCode().equals(MedicalConstant.INFLAMMATION)
                || history.getCheckKindCode().equals(MedicalConstant.MYOCARDIUMCARDIAC)
                || history.getCheckKindCode().equals(MedicalConstant.GHB)) {
            List<MedicalData> list = history.getCheckDataOuts();
            if (list.size() == 0)
                return;
            Intent intent = new Intent(this, CommonResultActivity.class);
            intent.putExtra("data", (Serializable) list);
            startActivity(intent);
        }
        //心电图
        if (history.getCheckKindCode().equals(MedicalConstant.LEAD_ECG)) {
            List<MedicalData> list = history.getCheckDataOuts();
            String filePath = "";
            String result = "";
            for (MedicalData data : list) {
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

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
                intent1.putExtra(SelectDialogActivity.KEY_TYPE, 1);
                intent1.putExtra(SelectDialogActivity.KEY_ARRAY, templates);
                startActivityForResult(intent1, REQUEST_TEMP);
                break;
            case R.id.btnOver:
                if (TextUtils.isEmpty(edSuggest.getText().toString().trim())) {
                    ToastUtil.showShort("请填写医嘱建议");
                    return;
                }
                serviceStatus = 1;
                mPresenter.taskExecute(detail, serviceStatus, edDescription.getText().toString().trim(),
                        edSuggest.getText().toString().trim());
                break;
            case R.id.btnKeep:
                if (TextUtils.isEmpty(edSuggest.getText().toString().trim())) {
                    ToastUtil.showShort("请填写医嘱建议");
                    return;
                }
                serviceStatus = 0;
                mPresenter.taskExecute(detail, serviceStatus, edDescription.getText().toString().trim(),
                        edSuggest.getText().toString().trim());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        String selectedString;
        switch (requestCode) {
            case REQUEST_SYMP:
                selectedString = data.getStringExtra(SelectDialogActivity.KEY_DATA);
                edDescription.setText(edDescription.getText().append(selectedString));
                edDescription.setFocusable(true);
                edDescription.setSelection(edDescription.getText().length());
                break;
            case REQUEST_TEMP:
                selectedString = data.getStringExtra(SelectDialogActivity.KEY_DATA);
                edSuggest.setText(edSuggest.getText().append(selectedString));
                edSuggest.setFocusable(true);
                edSuggest.setSelection(edSuggest.getText().length());
                break;
        }
    }

}
