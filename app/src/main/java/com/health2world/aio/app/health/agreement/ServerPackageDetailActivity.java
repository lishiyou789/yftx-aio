package com.health2world.aio.app.health.agreement;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.app.adapter.ServicePackageDetailAdapter;
import com.health2world.aio.bean.ServiceItem;
import com.health2world.aio.bean.SignService;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.util.TitleBarUtil;
import com.health2world.aio.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Runnlin
 * @date 2018/7/26/0026
 */
public class ServerPackageDetailActivity extends MVPBaseActivity<ServerPackageContract.Presenter>
        implements ServerPackageContract.View {

    private TitleBar titleBar;
    private RecyclerView recyclerView;
    private ServicePackageDetailAdapter pkgDetailAdapter;
    private int serviceId;//测试用
    private TextView pkgName, pkgFee, pkgFeeAll, pkgPeople, pkgTerm, pkgTag;
    private List<ServiceItem> pkgDetailList;

    @Override
    protected ServerPackageContract.Presenter getPresenter() {
        return new ServerPackagePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_package_detail;
    }

    @Override
    protected void initView() {
        titleBar = findView(R.id.titleBar);
        TitleBarUtil.setAttr(this, getString(R.string.service_package_detail), "", titleBar);

        serviceId = getIntent().getIntExtra("serviceId", -1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findView(R.id.service_detail_recycler);
        recyclerView.setLayoutManager(layoutManager);

        pkgName = findView(R.id.tv_service_pkg_name);
        pkgFee = findView(R.id.tv_detail_fee);
        pkgFeeAll = findView(R.id.tv_service_pkg_prize);
        pkgPeople = findView(R.id.tv_pkg_people);
        pkgTerm = findView(R.id.tv_pkg_time);
        pkgTag = findView(R.id.tv_detail_tag);

    }

    @Override
    protected void initData() {
        serviceId = getIntent().getIntExtra("serviceId", 92);
        mPresenter.packageDetail(serviceId);//传给presenter实现
        pkgDetailList = new ArrayList<>();
        pkgDetailAdapter = new ServicePackageDetailAdapter(pkgDetailList);
        recyclerView.setAdapter(pkgDetailAdapter);
    }

    @Override
    protected void initListener() {
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void packageDetailSuccess(SignService detailBean) {
        if (detailBean != null) {
            pkgName.setText(detailBean.getServiceName());
            pkgTag.setText(detailBean.getTagName());
            if (TextUtils.isEmpty(detailBean.getCouponPrice() + "")) {
                pkgFee.setText("" + detailBean.getPrice());
            } else {
                pkgFee.setText("" + detailBean.getCouponPrice());
            }
            pkgFeeAll.setText(String.format("(原价: ¥%s)", detailBean.getPrice()));
            pkgPeople.setText(String.format("适用人群: %s", detailBean.getTagName()));
            pkgTerm.setText(String.format("有效期: %s%s", detailBean.getServiceTerm(), detailBean.getServiceTermUnit()));
            pkgDetailList.addAll(detailBean.getServiceItems());
            pkgDetailAdapter.notifyDataSetChanged();
        }
    }
}
