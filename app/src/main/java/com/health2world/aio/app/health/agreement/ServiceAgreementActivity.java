package com.health2world.aio.app.health.agreement;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.health2world.aio.R;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.bean.AgreementBean;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.util.TitleBarUtil;
import com.health2world.aio.view.TitleBar;
import com.konsung.bean.ResidentBean;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.recyclerview.DividerItemDecoration;

/**
 * Created by Administrator on 2018/7/31/0031.
 */

public class ServiceAgreementActivity extends MVPBaseActivity<ServiceAgreementContract.Presenter>
        implements ServiceAgreementContract.View, BaseQuickAdapter.OnItemClickListener {

    private ResidentBean mCurResident;
    private TitleBar titleBar;
    private RecyclerView recyclerView;
    private ArrayList<AgreementBean> agreementBeanList;
    private ServiceAgreementAdapter serviceAgreementAdapter;
    private int mCurPos = 0;
    private WebView webSignDetail;
    private View llAgreement, emptyView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_agreement;
    }

    @Override
    protected ServiceAgreementContract.Presenter getPresenter() {
        return new ServiceAgreementPresenter(this);
    }

    @Override
    protected void initView() {
        llAgreement = findView(R.id.llAgreement);
        emptyView = findView(R.id.emptyView);
        titleBar = findView(R.id.titleBar);

        webSignDetail = new WebView(this);
        webSignDetail = findView(R.id.agreementDetail);
        webSignDetail.setWebViewClient(new WebViewClient());
        webSignDetail.getSettings().setJavaScriptEnabled(true);
        webSignDetail.getSettings().setDefaultTextEncodingName("UTF-8");
        recyclerView = findView(R.id.rv_service_agreement);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    protected void initData() {
        TitleBarUtil.setAttr(this, getString(R.string.service_agreement), "", titleBar);
        mCurResident = (ResidentBean) getIntent().getSerializableExtra(MainActivity.KEY_RESIDENT);
        agreementBeanList = new ArrayList<>();
        serviceAgreementAdapter = new ServiceAgreementAdapter(agreementBeanList);
        recyclerView.setAdapter(serviceAgreementAdapter);
        if (mCurResident != null)
            mPresenter.serviceAgreement(mCurResident.getPatientId());

    }

    private void initWebView(String url) {
        webSignDetail.loadData(url, "text/html; charset=UTF-8", null);
        webSignDetail.reload();
        String title = webSignDetail.getTitle();
        System.out.println("title = " + title);
    }

    @Override
    protected void initListener() {
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        serviceAgreementAdapter.setOnItemClickListener(this);
    }

    //更变协议选中状态 && 切换当前使用bean && 改颜色
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mCurPos != position) {
            AgreementBean lastAgreementBean = agreementBeanList.get(mCurPos);
            lastAgreementBean.setCheck(false);
            AgreementBean curAgreementBean = agreementBeanList.get(position);
            curAgreementBean.setCheck(true);
            serviceAgreementAdapter.notifyItemChanged(mCurPos);
            serviceAgreementAdapter.notifyItemChanged(position);
            mCurPos = position;
            initWebView(curAgreementBean.getContent());
        }
    }

    @Override
    public void serviceAgreementSuccess(List<AgreementBean> agreementList) {
        if (agreementList.size() > 0) {
            agreementBeanList.addAll(agreementList);
            agreementBeanList.get(0).setCheck(true);
            serviceAgreementAdapter.notifyDataSetChanged();
            initWebView(agreementBeanList.get(0).getContent());
        } else {
            llAgreement.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }
}

