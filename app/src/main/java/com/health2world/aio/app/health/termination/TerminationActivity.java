package com.health2world.aio.app.health.termination;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.health2world.aio.R;
import com.health2world.aio.app.health.protocol.ProtocolActivity;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.bean.SignMember;
import com.health2world.aio.bean.SignService;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.util.TitleBarUtil;
import com.health2world.aio.view.TitleBar;
import com.konsung.bean.ResidentBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.ToastUtil;


/**
 * 解约
 * Created by lishiyou on 2018/8/20 0020.
 */

public class TerminationActivity extends MVPBaseActivity<TerminationContract.Presenter>
        implements TerminationContract.View, BaseQuickAdapter.OnItemClickListener {

    private ResidentBean resident;
    private Button btnSure;
    private RecyclerView recyclerView;
    private TitleBar titleBar;

    private TerminationAdapter terminationAdapter;
    private List<SignService> serviceList = new ArrayList<>();

    @Override
    protected TerminationContract.Presenter getPresenter() {
        return new TerminationPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_termination;
    }

    @Override
    protected void initView() {
        titleBar = findView(R.id.titleBar);
        TitleBarUtil.setAttr(this, "解约", "", titleBar);
        btnSure = findView(R.id.btnSure);
        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        resident = (ResidentBean) getIntent().getSerializableExtra(MainActivity.KEY_RESIDENT);

        terminationAdapter = new TerminationAdapter(serviceList, resident);
        recyclerView.setAdapter(terminationAdapter);
        terminationAdapter.bindToRecyclerView(recyclerView);
        terminationAdapter.setEmptyView(R.layout.layout_empty_view);

        if (resident != null)
            mPresenter.loadServicePackage(resident.getPatientId());
    }

    @Override
    protected void initListener() {
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure.setOnClickListener(this);
        terminationAdapter.setOnItemClickListener(this);
    }


    @Override
    public void loadServicePackageSuccess(List<SignService> list) {
        serviceList.clear();
        for (SignService service : list) {
            if (service.getDelFlag().equals("0"))
                serviceList.add(service);
        }
        terminationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        SignService service = (SignService) adapter.getItem(position);
        if (service.isChecked())
            service.setChecked(false);
        else
            service.setChecked(true);
        terminationAdapter.notifyItemChanged(position);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnSure) {
            List<SignService> list = new ArrayList<>();
            for (SignService service : serviceList) {
                if (service.isChecked())
                    list.add(service);
            }
            if (list.size() == 0) {
                ToastUtil.showShort("请选择解约的服务包");
                return;
            }
            termination(list);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.loadServicePackage(resident.getPatientId());
//        setResult(RESULT_OK);
//        finish();
    }

    private void termination(List<SignService> list) {

        List<SignMember> memberList = new ArrayList<>();

        SignMember signMember = new SignMember();
        signMember.setPatientName(resident.getName());
        signMember.setIdentityCard(resident.getIdentityCard());
        signMember.setPatientId(resident.getPatientId());
        signMember.setTelphone(resident.getTelPhone());
        signMember.setRelation(resident.getRelation());
        double totalPrice = 0.0;
        String servicesIds = "";
        String serviceNames = "";
        for (SignService section : list) {
            totalPrice += section.getPrice();
            servicesIds += section.getServiceId() + ";";
            serviceNames += section.getServiceName() + ";";
        }
        signMember.setTotalPrice(String.valueOf(totalPrice));
        signMember.setServiceIds(servicesIds.substring(0, servicesIds.length() - 1));
        signMember.setServiceNames(serviceNames.substring(0, serviceNames.length() - 1));
        memberList.add(signMember);

        Intent intent = new Intent(TerminationActivity.this, ProtocolActivity.class);
        intent.putExtra(ProtocolActivity.KEY_TYPE, ProtocolActivity.CODE_UN_SIGN);
        intent.putExtra(ProtocolActivity.KEY_RESIDENT, resident);
        intent.putExtra(ProtocolActivity.KEY_DATA, (Serializable) memberList);
        startActivityForResult(intent, 0x01);
    }
}
