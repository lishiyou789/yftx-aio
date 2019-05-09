package com.health2world.aio.app.health.sign;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.adapter.HealthFamilyMemberAdapter;
import com.health2world.aio.app.health.agreement.ServerPackageDetailActivity;
import com.health2world.aio.app.health.protocol.ProtocolActivity;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.app.setting.InputSettingActivity;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.bean.SettingBean;
import com.health2world.aio.bean.SignMember;
import com.health2world.aio.bean.SignService;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.util.ActivityUtil;
import com.health2world.aio.util.TitleBarUtil;
import com.health2world.aio.view.TitleBar;
import com.j256.ormlite.support.DatabaseConnection;
import com.konsung.bean.ResidentBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.ToastUtil;

/**
 * 家庭签约
 * Created by lishiyou on 2018/8/8 0008.
 */

public class SignActivity extends MVPBaseActivity<SignContract.Presenter> implements SignContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final int REQUEST_IDENTITY_CARD = 0x10;
    public static final int REQUEST_PROTOCOL = 0x11;
    //区分加载服务包数据的标签 1居民加载成功之后才会加载服务包 2 签约成功后刷新居民  默认1
    private boolean loadType = true;

    private TitleBar titleBar;
    private Button btnOk, btnReset;
    private LinearLayout llBottom;
    //登陆者信息
    private DoctorBean doctor;
    //当前选择的居民
    private ResidentBean resident;
    //家人列表数据集合
    private List<ResidentBean> residentList;
    //数据展示控件
    private RecyclerView recyclerView, recyclerViewPackage;
    //适配器
    private HealthFamilyMemberAdapter memberAdapter;
    //当前居民选中的位置
    private int mPosition = 0;
    //服务包数据集合 组合数据用来分组显示
    private List<SignServiceSection> serviceList = new ArrayList<>();
    //当前页面内存保存一份服务包集合数据
    private static Collection<SignService> serviceAllList = new ArrayList<>();
    //适配器
    private PackageListAdapter packageListAdapter;
    //刷新控件
    private SwipeRefreshLayout refreshLayout;
    //存储居民所选中的服务包
    private HashMap<ResidentBean, List<SignServiceSection>> hashMap = new HashMap<>();

    @Override
    protected SignContract.Presenter getPresenter() {
        return new SignPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initView() {
        titleBar = findView(R.id.titleBar);
        btnOk = findView(R.id.btnOk);
        btnReset = findView(R.id.btnReset);
        llBottom = findView(R.id.llBottom);
        TitleBarUtil.setAttr(this, "家医/公卫", "", titleBar);
        refreshLayout = findView(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //家庭成员列表布局
        recyclerView = findView(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //服务包展示布局
        recyclerViewPackage = findView(R.id.recyclerViewPackage);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerViewPackage.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void initData() {
        resident = (ResidentBean) getIntent().getSerializableExtra(MainActivity.KEY_RESIDENT);
        doctor = DBManager.getInstance().getCurrentDoctor();

        residentList = new ArrayList<>();
        memberAdapter = new HealthFamilyMemberAdapter(residentList);
        recyclerView.setAdapter(memberAdapter);

        packageListAdapter = new PackageListAdapter(serviceList);
        recyclerViewPackage.setAdapter(packageListAdapter);

        /**加载家庭成员列表数据*/
        mPresenter.loadFamilyMember(resident.getPatientId());
    }

    @Override
    protected void initListener() {

        btnOk.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        packageListAdapter.setOnItemClickListener(this);
        packageListAdapter.setOnItemChildClickListener(this);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.addAction(new TitleBar.TextAction("公共卫生") {
            @Override
            public void performAction(View view) {
                //当前居民身份证信息为空 则先补充
                if (TextUtils.isEmpty(resident.getIdentityCard()))
                    editIdentityCard();
                else
                    ActivityUtil.enterPublicHealth(mContext, 0, 0x01);
            }
        });

        memberAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == mPosition)
                    return;
                residentList.get(mPosition).setCheck(false);
                residentList.get(position).setCheck(true);
                memberAdapter.notifyItemChanged(mPosition);
                memberAdapter.notifyItemChanged(position);

                //如果是多签则保存所有居民选中的服务包 否则不需要保存
                //需要注意的是最后一个没点击的 则不会存储所选的服务包
                if (doctor.getSignMode() == 3)
                    saveSelectServicePackage(resident);

                //设置当前居民 和当前位置
                resident = (ResidentBean) adapter.getItem(position);
                mPosition = position;
                if (!TextUtils.isEmpty(resident.getIdentityCard()))
                    MyApplication.getInstance().setCurrentIdentityCard(resident.getIdentityCard());
                else
                    MyApplication.getInstance().setCurrentIdentityCard("");

                handleData(serviceAllList);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (residentList.size() == 0) {
            mPresenter.loadFamilyMember(resident.getPatientId());
        } else {
            refreshLayout.setRefreshing(true);
            mPresenter.loadServicePackage();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnOk:
                signMethod();
                break;
            case R.id.btnReset:
                resetAllSelect();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        //更新该居民的身份证信息
        if (requestCode == REQUEST_IDENTITY_CARD) {
            String identityCard = data.getStringExtra("data");
            mPresenter.updatePatientIdentityCard(residentList.get(0).getPatientId(), resident.getPatientId(), identityCard);
        }
        //签约完成之后刷新界面
        if (requestCode == REQUEST_PROTOCOL) {
            loadType = false;
            memberList.clear();
            //家庭成员以第一个为基准
            mPresenter.loadFamilyMember(residentList.get(0).getPatientId());

            String qrInfo = data.getStringExtra("data");
            if (!TextUtils.isEmpty(qrInfo))
                showQRInfo(qrInfo);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //如果该居民身份证信息为空 则先补充身份证信息
        if (TextUtils.isEmpty(resident.getIdentityCard())) {
            editIdentityCard();
            return;
        }
        SignServiceSection service = (SignServiceSection) adapter.getItem(position);
        //如果是强建档则先查询该居民在公卫是否有健康档案
        if (doctor.getIsArchives() == 1 && TextUtils.isEmpty(resident.getHealthFileNo())) {
            mPresenter.queryHealthFile(resident.getIdentityCard(), service, position);
        } else {
            changeSelectionStatus(service, position);
        }
    }

    private void changeSelectionStatus(SignServiceSection service, int position) {
        //头部不支持操作
        if (service.isHeader)
            return;
        //已经签约的不支持操作
        if (service.t.isSigned())
            return;
        //服务包签约数量已达上限的不支持操作
        if (service.t.getLimit() == 1) {
            ToastUtil.showShort("该服务包签约数量已达上限");
            return;
        }
        if (service.t.isChecked())
            service.t.setChecked(false);
        else
            service.t.setChecked(true);

        packageListAdapter.notifyItemChanged(position);
    }

    /***
     * 查看服务包详情
     * @param adapter
     * @param view     The view whihin the ItemView that was clicked
     * @param position The position of the view int the adapter
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        SignServiceSection section = (SignServiceSection) adapter.getItem(position);
        if (section.isHeader)
            return;
        Intent intent = new Intent(SignActivity.this, ServerPackageDetailActivity.class);
        intent.putExtra("serviceId", section.t.getServiceId());
        startActivity(intent);
    }

    /**
     * 家庭成员数据加载成功
     *
     * @param list
     */
    @Override
    public void loadFamilyMemberSuccess(List<ResidentBean> list) {
        if (list.size() == 0)
            return;
        residentList.clear();
        residentList.addAll(list);
        memberAdapter.notifyDataSetChanged();

        //设置当前居民
        resident = residentList.get(mPosition);
        resident.setCheck(true);
        if (!TextUtils.isEmpty(resident.getIdentityCard()))
            MyApplication.getInstance().setCurrentIdentityCard(resident.getIdentityCard());
        else
            MyApplication.getInstance().setCurrentIdentityCard("");

        //保存全部居民信息到本地数据库
        saveFamilyMember(residentList);

        //加载服务包数据
        if (loadType) {
            onRefresh();
        } else {
            resetAllSelect();
            handleData(serviceAllList);
        }
    }

    /**
     * 家庭成员数据加载失败
     *
     * @param e
     */
    @Override
    public void loadFamilyMemberError(Throwable e) {
        refreshLayout.setRefreshing(false);
    }

    /**
     * 服务包数据加载成功
     *
     * @param list
     */
    @Override
    public void loadServicePackageSuccess(List<SignService> list) {
        refreshLayout.setRefreshing(false);
        serviceAllList.clear();
        serviceAllList.addAll(list);

        llBottom.setVisibility(View.VISIBLE);

        handleData(serviceAllList);
    }

    /**
     * 服务包数据加载失败
     *
     * @param e
     */
    @Override
    public void loadServicePackageError(Throwable e) {
        refreshLayout.setRefreshing(false);
    }

    /**
     * 当前居民 身份证信息更新成功
     *
     * @param identityCard
     */
    @Override
    public void updateIdentityCardSuccess(String identityCard) {
        resident.setIdentityCard(identityCard);
        //将该居民信息保存到本地
        try {
            MyApplication.getInstance().setCurrentIdentityCard(identityCard);
            DBManager.getInstance().getResidentDao().createOrUpdate(resident);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 健康档案查询成功
     *
     * @param resident
     * @param section
     * @param position
     */
    @Override
    public void queryHealthFileSuccess(ResidentBean resident, SignServiceSection section, int position) {
        //当前居民没有建档
        if (TextUtils.isEmpty(resident.getHealthFileNo())) {
            //建档提示操作
            showPublicHealthDialog();
        } else {
            //设置当前居民的健康档案编号
            this.resident.setHealthFileNo(resident.getHealthFileNo());
            changeSelectionStatus(section, position);
        }
    }

    /**
     * 取消所有选择(已经签约的不可编辑) 刷新界面
     */
    private void resetAllSelect() {
        hashMap.clear();
        for (SignServiceSection section : serviceList) {
            if (!section.isHeader && !section.t.isSigned() && section.t.getLimit() != 1) {
                section.t.setChecked(false);
            }
        }
        packageListAdapter.notifyDataSetChanged();
    }

    /**
     * 存储该居民已经选中的服务包
     *
     * @param resident
     */
    private void saveSelectServicePackage(ResidentBean resident) {
        List<SignServiceSection> sectionList = new ArrayList<>();
        for (SignServiceSection section : serviceList) {
            if (!section.isHeader && section.t.isChecked() && !section.t.isSigned() && section.t.getLimit() != 1)
                sectionList.add(section);
        }
        hashMap.put(resident, sectionList);
    }


    /**
     * 将居民信息保存到本地
     */
    private void saveFamilyMember(List<ResidentBean> list) {
        try {
            DatabaseConnection connection = DBManager.getInstance().getResidentDao().startThreadConnection();
            Savepoint savepoint = connection.setSavePoint(null);
            for (ResidentBean resident : list) {
                if (TextUtils.isEmpty(resident.getIdentityCard()))
                    continue;
                DBManager.getInstance().getResidentDao().createOrUpdate(resident);
            }
            connection.commit(savepoint);
            DBManager.getInstance().getResidentDao().endThreadConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入编辑界面 完善居民身份证信息
     */
    private void editIdentityCard() {
        SettingBean settingBean = new SettingBean();
        settingBean.setName("请完善 " + resident.getName() + " 的身份证信息");
        settingBean.setValue("");
        Intent intent = new Intent(this, InputSettingActivity.class);
        intent.putExtra(InputSettingActivity.SETTING_BEAN, settingBean);
        intent.putExtra("isIdCard", true);
        startActivityForResult(intent, REQUEST_IDENTITY_CARD);
    }

    /**
     * 建档提示框
     */
    private void showPublicHealthDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.create_file_tips))
                .setMessage(getString(R.string.create_file_tips_content))
                .setPositiveButton(getString(R.string.go_create_file), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityUtil.enterPublicHealth(mContext, 0, 0x01);
                    }
                })
                .create()
                .show();
    }

    /**
     * 整理数据
     *
     * @param collection
     */
    private void handleData(Collection<SignService> collection) {
        //这里不能直接addAll 请参考Java中如何克隆集合
        //http://www.importnew.com/10761.html
        Iterator<SignService> iterator = collection.iterator();
        Collection<SignService> copy = new ArrayList<>(collection.size());
        while (iterator.hasNext()) {
            copy.add(iterator.next().clone());
        }
        //1、将该居民已经签约的服务包进行选中标记 并且不可再次选中和取消
        if (!TextUtils.isEmpty(resident.getServiceIds())) {
            List<String> serviceIdsList = Arrays.asList(resident.getServiceIds().split(","));
            for (SignService service : copy) {
                if (serviceIdsList.contains(service.getServiceId() + "")) {
                    service.setChecked(true);
                    service.setSigned(true);
                }
                //标记已达到上限的服务包
                if (service.getLimit() == 1) {
                    service.setChecked(true);
                }
            }
        } else {
            for (SignService service : copy) {
                //标记已达到上限的服务包
                if (service.getLimit() == 1) {
                    service.setChecked(true);
                }
            }
        }

        //定义两个集合装载两种类型的服务包
        List<SignServiceSection> list1 = new ArrayList<>();
        List<SignServiceSection> list2 = new ArrayList<>();
        //添加头部操作
        list1.add(new SignServiceSection(true, getString(R.string.recommend_service)));
        list2.add(new SignServiceSection(true, getString(R.string.other_service)));
        //2、根据居民标签匹配服务包加入服务包推荐
        if (!TextUtils.isEmpty(resident.getTagIds())) {
            List<String> listTags = Arrays.asList(resident.getTagIds().split(","));
            for (SignService service : copy) {
                if (listTags.contains(service.getTagId() + ""))
                    list1.add(new SignServiceSection(service));
                else
                    list2.add(new SignServiceSection(service));
            }
        } else {
            //没有推荐的服务包，全部是其他服务包
            for (SignService service : copy) {
                list2.add(new SignServiceSection(service));
            }
        }
        serviceList.clear();
        serviceList.addAll(list1);
        serviceList.addAll(list2);
        //恢复当前选中居民所选的服务包
        List<SignServiceSection> listSelect = hashMap.get(resident);
        if (listSelect != null && listSelect.size() > 0) {
            for (SignServiceSection s : listSelect) {
                for (SignServiceSection section : serviceList) {
                    if (!section.isHeader && s.t.getServiceId() == section.t.getServiceId()) {
                        section.t.setChecked(true);
                        break;
                    }
                }
            }
        }
        packageListAdapter.notifyDataSetChanged();
        copy.clear();
    }


    /**
     * 单签和多签操作
     */
    private void signMethod() {
        //获取选中的服务包
        List<SignServiceSection> list = new ArrayList<>();
        //过滤头部 已经签约的 而且签约数达到上限的
        for (SignServiceSection section : serviceList) {
            if (!section.isHeader && section.t.isChecked() && !section.t.isSigned() && section.t.getLimit() != 1) {
                list.add(section);
            }
        }
        //1、单签操作 判断当前
//        if (doctor.getSignMode() == 2) {
//            if (list.size() == 0) {
//                ToastUtil.showShort("请先选择服务包");
//                return;
//            }
//            memberList.clear();
//            packageSignServiceData(resident, list);
//        }
        //2、多签操作 将当前居民手动添加到集合 判断所有
        if (doctor.getSignMode() == 3) {
            hashMap.put(resident, list);
            boolean isAdd = false;
            //判断HashMap里面所有的集合 是否为空
            for (Map.Entry<ResidentBean, List<SignServiceSection>> entry : hashMap.entrySet()) {
                List<SignServiceSection> sectionList = entry.getValue();
                if (sectionList != null && sectionList.size() > 0) {
                    isAdd = true;
                    break;
                }
            }
            if (!isAdd) {
                ToastUtil.showShort("请先选择服务包");
                return;
            }
            memberList.clear();
            for (Map.Entry<ResidentBean, List<SignServiceSection>> entry : hashMap.entrySet()) {
                if (entry.getValue().size() == 0)
                    continue;
                packageSignServiceData(entry.getKey(), entry.getValue());
            }
        } else {//单签操作
            if (list.size() == 0) {
                ToastUtil.showShort("请先选择服务包");
                return;
            }
            memberList.clear();
            packageSignServiceData(resident, list);
        }

        //如果当前户主已经选择了服务包则不处理 如果没有选择服务包 则将当前户主加入到memberList集合中
        if (doctor.getSignMode() == 3
                && hashMap.get(residentList.get(0)) == null
                && residentList.size() > 1) {
            SignMember member = new SignMember();
            member.setPatientId(residentList.get(0).getPatientId());
            member.setPatientName(residentList.get(0).getName());
            member.setTelphone(residentList.get(0).getTelPhone());
            member.setRelation(residentList.get(0).getRelation());
            member.setIdentityCard(residentList.get(0).getIdentityCard());
            memberList.add(0, member);
        }
        Intent intent = new Intent(SignActivity.this, ProtocolActivity.class);
        intent.putExtra(ProtocolActivity.KEY_TYPE, ProtocolActivity.CODE_SIGN);
        intent.putExtra(ProtocolActivity.KEY_RESIDENT, residentList.get(0));
        intent.putExtra(ProtocolActivity.KEY_DATA, (Serializable) memberList);
        startActivityForResult(intent, REQUEST_PROTOCOL);
    }

    /**
     * 签约数据的集合
     */
    private List<SignMember> memberList = new ArrayList<>();

    private void packageSignServiceData(ResidentBean resident, List<SignServiceSection> list) {
        //组装签约数据
        SignMember signMember = new SignMember();
        signMember.setPatientName(resident.getName());
        signMember.setIdentityCard(resident.getIdentityCard());
        signMember.setPatientId(resident.getPatientId());
        signMember.setTelphone(resident.getTelPhone());
        signMember.setRelation(resident.getRelation());

        double totalPrice = 0.0;
        String servicesIds = "";
        String serviceNames = "";
        String tagIds = "";
        for (SignServiceSection section : list) {
            totalPrice += section.t.getPrice();
            servicesIds += section.t.getServiceId() + ";";
            tagIds += section.t.getTagId() + ";";
            serviceNames += section.t.getServiceName() + ";";
        }
        signMember.setTotalPrice(String.valueOf(totalPrice));
        signMember.setServiceIds(servicesIds.substring(0, servicesIds.length() - 1));
        signMember.setServiceNames(serviceNames.substring(0, serviceNames.length() - 1));
        signMember.setTagIds(tagIds.substring(0, tagIds.length() - 1));
        memberList.add(signMember);
    }


    /**
     * 签约成功显示二维码
     */
    private void showQRInfo(String qrInfo) {
        try {
            JSONArray array = new JSONArray(qrInfo);
            if (array == null || array.length() == 0) {
                return;
            }
            List<SignMember> list = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                SignMember member = new SignMember();
                member.setPatientId(object.optInt("patientId") + "");
                member.setPatientName(object.optString("patientName"));
                member.setTelphone(object.optString("telphone"));
                member.setMembers(object.optString("ticket"));
                list.add(member);
            }
            Intent intent = new Intent(SignActivity.this, SignSuccessActivity.class);
            intent.putExtra("data", (Serializable) list);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
