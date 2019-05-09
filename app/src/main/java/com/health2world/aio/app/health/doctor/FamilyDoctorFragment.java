package com.health2world.aio.app.health.doctor;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.app.clinic.ClinicOneActivity;
import com.health2world.aio.app.health.HealthManagerActivity;
import com.health2world.aio.app.health.sign.SignActivity;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.bean.ServiceItem;
import com.health2world.aio.bean.SignService;
import com.health2world.aio.common.mvp.MVPBaseFragment;
import com.health2world.aio.util.Logger;
import com.konsung.bean.ResidentBean;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.ToastUtil;

/**
 * Created by lishiyou on 2018/8/6 0006.
 */

public class FamilyDoctorFragment extends MVPBaseFragment<FamilyDoctorContract.Presenter> implements FamilyDoctorContract.View,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {



    public static String advise = "";
    private ResidentBean resident;

    private LinearLayout layoutTop;

    private RecyclerView recyclerView, itemRecyclerView;
    private TextView tvEndTime, tvDoctor;

//    private PerformanceManager performanceManager;

    private ServicePackageListAdapter listAdapter;
    private ServicePackageItemAdapter itemAdapter;

    private static List<SignService> serviceList;
    private List<ServiceItem> itemList;

    private SwipeRefreshLayout refreshLayout;
    //默认选中第一个
    private int mPosition = 0, itemPosition = 0;
    public static ServiceItem serviceItem;

    private View view;

    public static String dataId = "";

    @Override
    protected FamilyDoctorContract.Presenter getPresenter() {
        return new FamilyDoctorPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_family_doctor;
    }

    @Override
    protected void initView() {
        tvEndTime = findView(R.id.tvEndTime);
        tvDoctor = findView(R.id.tvDoctor);
        layoutTop = findView(R.id.layoutTop);

        recyclerView = findView(R.id.recyclerView);
        itemRecyclerView = findView(R.id.itemRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        itemRecyclerView.setLayoutManager(layoutManager1);

        refreshLayout = findView(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        view = View.inflate(mContext, R.layout.layout_empty_view, null);
        view.findViewById(R.id.btnSign).setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        resident = (ResidentBean) getArguments().getSerializable("resident");

        serviceList = new ArrayList<>();
        listAdapter = new ServicePackageListAdapter(serviceList);
        recyclerView.setAdapter(listAdapter);

        itemList = new ArrayList<>();
        itemAdapter = new ServicePackageItemAdapter(itemList);
        itemRecyclerView.setAdapter(itemAdapter);
        itemAdapter.bindToRecyclerView(itemRecyclerView);
        itemAdapter.setEmptyView(view);
//        itemAdapter.setEmptyView(R.layout.layout_empty_view);

//        performanceManager = PerformanceManager.getInstance(mContext);

        //加载数据（签约的服务包）
        onRefresh();
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshListener(this);
        listAdapter.setOnItemClickListener(this);
        itemAdapter.setOnItemChildClickListener(this);
//        performanceManager.setListener(this);
        view.findViewById(R.id.btnSign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SignActivity.class);
                intent.putExtra(MainActivity.KEY_RESIDENT, ((HealthManagerActivity) getActivity()).getResidentList().get(0));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mContext = getActivity();
    }

    @Override
    public void onRefresh() {
        mPosition = 0;
        if (mContext instanceof HealthManagerActivity) {
            ((HealthManagerActivity) mContext).setLoading(true);
        }
        refreshLayout.setRefreshing(true);
        mPresenter.loadServicePackage(resident.getPatientId());
    }

    //点击查看每一项的服务包
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mPosition == position) {
            return;
        }
        SignService service = (SignService) adapter.getItem(position);
        serviceList.get(mPosition).setChecked(false);
        service.setChecked(true);
        listAdapter.notifyItemChanged(mPosition);
        listAdapter.notifyItemChanged(position);
        mPosition = position;

        itemList.clear();
        itemList.addAll(serviceList.get(position).getServiceItems());
        itemAdapter.notifyDataSetChanged();

        tvEndTime.setText(serviceList.get(position).getEndTime());
        tvDoctor.setText(serviceList.get(position).getSignDoctor());

    }

    //执行履约操作
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        itemPosition = position;
        serviceItem = (ServiceItem) adapter.getItem(position);
        //1启动测量类型的  服务类型：0:服务; 1:测量类

        //3直接执行履约操作与后台交互的类型的
        if (serviceItem.getServiceType() == 0) {
            Intent intent = new Intent(mContext, HealthGuideActivity.class);
            intent.putExtra("serviceItem", serviceItem);
            getActivity().startActivityForResult(intent, HealthManagerActivity.REQUEST_CODE_SERVICE);
        }

        //启动测量页面
        if (serviceItem.getServiceType() == 1) {
            Intent intent = new Intent(mContext, ClinicOneActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(MainActivity.KEY_RESIDENT, resident);
            intent.putExtra("openFrom", 0);
            getActivity().startActivityForResult(intent, HealthManagerActivity.REQUEST_CODE_EXECUTE);
        }

        //2无需启动 测量类型的 设置测量完成之后的回调监听

    }

//    //测量完成之后的回调
//    @Override
//    public void measureSuccess(Measure measure, MeasureBean measureBean) {
//        //这里进行测量数据的上传
//        //这里需要区分类型 根据返回的测量类型在列表中找到 再进行服务记录的添加
//
////        mPresenter.uploadMeasureData("", resident.getPatientId(), 0, measureBean);
//    }

    //获取医生建议
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            //履约服务回来
            case HealthManagerActivity.REQUEST_CODE_SERVICE:
                Logger.i("zrl","REQUEST_CODE_SERVICE_Fragment");
                advise = data.getStringExtra("advise");
                addServiceRecord();
                break;
            //履约测量回来
            case HealthManagerActivity.REQUEST_CODE_EXECUTE:
                Logger.i("zrl","FamilyDoctorFragment");
                dataId = data.getStringExtra("dataId");

                Intent intent = new Intent(mContext, HealthGuideActivity.class);
                intent.putExtra("serviceItem", serviceItem);
                getActivity().startActivityForResult(intent, HealthManagerActivity.REQUEST_CODE_MEASURE);
                break;

            //履约测量后填写医嘱回来
            case HealthManagerActivity.REQUEST_CODE_MEASURE:
                advise = data.getStringExtra("advise");
                addServiceRecord();
                break;
        }
    }


    //数据加载成功
    @Override
    public void loadPackageSuccess(List<SignService> list) {
        ((HealthManagerActivity) mContext).setLoading(false);
        refreshLayout.setRefreshing(false);
        serviceList.clear();
        //签约的服务包列表 过滤失效的服务包
        for (SignService service : list) {
            if (service.getDelFlag().equals("0")) {
                serviceList.add(service);
            }
        }
        if (serviceList.size() == 0) {
            layoutTop.setVisibility(View.INVISIBLE);
            itemList.clear();
        } else {
            //控件的展示
            layoutTop.setVisibility(View.VISIBLE);
            tvEndTime.setText(list.get(0).getEndTime());
            tvDoctor.setText(list.get(0).getSignDoctor());

            serviceList.get(0).setChecked(true);
            //默认展示第一个服务包的细项
            itemList.clear();
            itemList.addAll(list.get(0).getServiceItems());
        }
        listAdapter.notifyDataSetChanged();
        itemAdapter.notifyDataSetChanged();
    }

    //数据加载失败
    @Override
    public void loadPackageError(Throwable throwable) {
        ((HealthManagerActivity) mContext).setLoading(false);
        refreshLayout.setRefreshing(false);
        layoutTop.setVisibility(View.INVISIBLE);
    }

    //履约操作成功回调
    @Override
    public void serviceRecordSuccess() {
        dataId = "";
        ToastUtil.showShort(getString(R.string.action_success));
        ServiceItem item = itemList.get(itemPosition);
        item.setExecuteNum(item.getExecuteNum() + 1);
        itemAdapter.notifyItemChanged(itemPosition);
    }

    //履约操作失败回调
    @Override
    public void serviceRecordError() {
        ToastUtil.showShort(getString(R.string.action_error));
    }

//    //测量数据上传成功之后的回调
//    @Override
//    public void uploadMeasureDataSuccess(String dataId) {
////        this.dataId = dataId;
//        //测量数据上传成功之后 填写指导意见 完成履约操作
//        addServiceRecord(dataId);
//    }

    /**
     * 添加服务记录
     */
    public void addServiceRecord() {
        mPresenter.addServiceRecord(resident,
                serviceList.get(mPosition).getSignId(),
                serviceItem.getItemId(),
                serviceItem.getServiceType(),
                dataId,
                advise);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
//        performanceManager = null;
    }
}
