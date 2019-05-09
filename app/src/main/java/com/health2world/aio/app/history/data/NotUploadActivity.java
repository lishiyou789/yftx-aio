package com.health2world.aio.app.history.data;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.util.Check;
import com.konsung.bean.MeasureBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.NetworkUtil;
import aio.health2world.utils.ToastUtil;

/**
 * Created by lishiyou on 2019/2/19 0019.
 */

public class NotUploadActivity extends MVPBaseActivity<NotUploadContract.Presenter> implements NotUploadContract.View,
        BaseQuickAdapter.OnItemChildClickListener {

    private RecyclerView recyclerView;

    private NotUploadAdapter uploadAdapter;

    private List<MeasureBean> dataList = new ArrayList<>();

    private ImageView ivClose;

    private TextView tvUploadAll;

    @Override
    protected NotUploadContract.Presenter getPresenter() {
        return new NotUploadPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        setFinishOnTouchOutside(false);
        return R.layout.activity_not_upload;
    }

    @Override
    protected void initView() {
        tvUploadAll = findView(R.id.tvUploadAll);
        ivClose = findView(R.id.ivClose);
        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        uploadAdapter = new NotUploadAdapter(dataList);
        recyclerView.setAdapter(uploadAdapter);
        uploadAdapter.bindToRecyclerView(recyclerView);
        uploadAdapter.setEmptyView(R.layout.layout_empty_view);

        List<MeasureBean> data = null;
        try {
            data = DBManager.getInstance().getMeasureDao().queryBuilder().where().eq("upload", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (data != null && data.size() > 0) {
            dataList.addAll(data);
        }
        uploadAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        ivClose.setOnClickListener(this);
        tvUploadAll.setOnClickListener(this);
        uploadAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (Check.isFastClick())
            return;
        MeasureBean measure = (MeasureBean) adapter.getItem(position);
        if (!measure.isUpload()) {
            if (NetworkUtil.isConnected(this)) {
                mPresenter.uploadMeasureData(position, measure);
            } else {
                ToastUtil.showShort("当前设备无网络请稍后再试");
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ivClose:
                finish();
                break;
            case R.id.tvUploadAll:
                uploadAllData();
                break;
        }
    }

    @Override
    public void uploadSuccess(int position) {
        MeasureBean bean = dataList.get(position);
        bean.setUpload(true);
        try {
            DBManager.getInstance().getMeasureDao().update(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        uploadAdapter.notifyItemChanged(position);
    }

    private void uploadAllData() {
        if (dataList.size() == 0) {
            ToastUtil.showShort("暂无未上传的数据");
            return;
        }
        if (!NetworkUtil.isConnected(this)) {
            ToastUtil.showShort("当前设备无网络请稍后再试");
            return;
        }
        for (int i = 0; i < dataList.size(); i++) {
            MeasureBean measure = dataList.get(i);
            if (!measure.isUpload())
                mPresenter.uploadMeasureData(i, measure);
        }
    }
}
