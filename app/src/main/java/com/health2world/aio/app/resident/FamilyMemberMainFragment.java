package com.health2world.aio.app.resident;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.app.adapter.FamilyMemberAdapter;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.app.resident.add.NewResidentActivity;
import com.health2world.aio.app.resident.info.FamilyMemberInfoFragment;
import com.health2world.aio.app.resident.view.FamilyCreateTipDialog;
import com.health2world.aio.app.resident.view.RelationSelectDialog;
import com.health2world.aio.app.resident.view.ResidentQRCodeDialog;
import com.health2world.aio.app.search.RSearchActivity;
import com.health2world.aio.common.DataServer;
import com.health2world.aio.common.mvp.MVPBaseFragment;
import com.konsung.bean.ResidentBean;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.DataEntity;
import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.ToastUtil;

/**
 * Created by Administrator on 2018/7/18 0018.
 */

public class FamilyMemberMainFragment extends MVPBaseFragment<FamilyMemberMainContract.Presenter> implements
        FamilyMemberMainContract.View, BaseQuickAdapter.OnItemClickListener, RelationSelectDialog.OnRelationSelectListener {
    public static final int REQUEST_CODE_FAMILY = 0x78;
    public static final int REQUEST_CODE_SEARCH = 0x12;
    private FamilyMemberAdapter mAdapter;
    private FragmentManager mChildFragmentManager;
    public static final String KEY_RELATION = "relation";
    private LinearLayout mLayoutContent;
    private FamilyMemberInfoFragment mCurFamilyMemberInfoFragment;
    private Button mAddFamily;
    private SparseArray<FamilyMemberInfoFragment> mFamilyMemberInfoView = new SparseArray<>();
    private List<ResidentBean> mFamilyMemberList = new ArrayList<>();
    private RecyclerView mLvFamilyMember;
    private int mCurPos = 0;
    private FamilyCreateTipDialog mCreateTipDialog;
    private ResidentBean mResidentBean;//当前主页的患者对象
    private RelationSelectDialog mRelationSelectDialog;
    private int mPosition = 0;

    private View emptyView;

    //被添加的患者对象
    private ResidentBean residentAdd;

    @Override
    protected FamilyMemberMainContract.Presenter getPresenter() {
        return new FamilyMemberMainPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_family_member;
    }

    @Override
    protected void initView() {
        mChildFragmentManager = getChildFragmentManager();
        mLayoutContent = findView(R.id.layout_resident_info);
        mLvFamilyMember = findView(R.id.lv_family_member);
        mLvFamilyMember.setLayoutManager(new LinearLayoutManager(mActivity));
        mAddFamily = findView(R.id.btn_add_family_member);
        mAdapter = new FamilyMemberAdapter(mFamilyMemberList);
        mLvFamilyMember.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mLvFamilyMember);
        emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_view, null);
        mAdapter.setEmptyView(emptyView);
        mCreateTipDialog = new FamilyCreateTipDialog(mActivity);
        mRelationSelectDialog = new RelationSelectDialog(mActivity);
    }

    @Override
    protected void initData() {
        mResidentBean = (ResidentBean) getArguments().getSerializable(MainActivity.KEY_RESIDENT);
//        mResidentBean = MyApplication.getInstance().getResident();
        mPresenter.loadFamilyMember(mResidentBean);
    }

    public void reloadData() {
        onCreate(null);
    }

    /**
     * 预加载fragment
     */
    private void initFirstFragment() {
        FragmentTransaction fragmentTransaction = mChildFragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.KEY_RESIDENT, mResidentBean);
        mCurFamilyMemberInfoFragment = new FamilyMemberInfoFragment();
        mFamilyMemberInfoView.put(0, mCurFamilyMemberInfoFragment);
        mCurFamilyMemberInfoFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.layout_content, mCurFamilyMemberInfoFragment);
        mCurFamilyMemberInfoFragment.showRelation(View.GONE);
        fragmentTransaction.commitAllowingStateLoss();
        mCurFamilyMemberInfoFragment.startLoadResidentInfo(mResidentBean);
    }

    @Override
    protected void initListener() {
        mAddFamily.setOnClickListener(this);
        mCreateTipDialog.setListener(this);
        mAdapter.setOnItemClickListener(this);
        mRelationSelectDialog.setOnRelationSelectListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mPosition = position;
        FamilyMemberInfoFragment familyMemberInfoFragment = mFamilyMemberInfoView.get(position);
        if (familyMemberInfoFragment == null) {
            familyMemberInfoFragment = new FamilyMemberInfoFragment();
            mFamilyMemberInfoView.put(position, familyMemberInfoFragment);
        }
        changeFamilyMemberView(position);
        FragmentTransaction fragmentTransaction = mChildFragmentManager.beginTransaction();
        hideFamilyMember(mCurFamilyMemberInfoFragment, fragmentTransaction);
        showFamilyMember(familyMemberInfoFragment, fragmentTransaction, position);
    }

    @Override
    public void relationSelect(DataEntity dataEntity) {
        residentAdd.setRelation(dataEntity.getText());
        mRelationSelectDialog.resetAllSelect();
        mRelationSelectDialog.dismiss();
        //关联一个已经存在的居民
        mPresenter.relevancyPatientInfo(mResidentBean.getPatientId(), residentAdd.getPatientId(), residentAdd.getRelation());
    }

    @Override
    public void relevancyPatientInfoSuccess(int familyId) {
        ToastUtil.showShort("添加成功");
        residentAdd.setFamilyId(familyId);
        mFamilyMemberList.add(residentAdd);
        mAdapter.notifyDataSetChanged();
        for (ResidentBean bean : mFamilyMemberList) {
            bean.setFamilyId(familyId);
        }
        for (int i = 0; i < mFamilyMemberInfoView.size(); i++) {
            FamilyMemberInfoFragment fragment = mFamilyMemberInfoView.get(i);
            if (fragment != null) {
                fragment.refreshView(mFamilyMemberList.get(i).getFamilyId() > 0);
            }
        }
    }

    public List<ResidentBean> getFamilyMemberList() {
        return mFamilyMemberList;
    }

    public void relieveFamilyRelation(int index) {
        mPosition = 0;
        if (mFamilyMemberList.size() == 1) {
            initFirstFragment();
        } else {
            mFamilyMemberList.remove(index);
            mCurPos = 0;
            mResidentBean = mFamilyMemberList.get(mCurPos);
            mFamilyMemberList.get(mCurPos).setCheck(true);
            mAdapter.notifyDataSetChanged();

            FamilyMemberInfoFragment infoFragment = mFamilyMemberInfoView.get(index);
            getChildFragmentManager().beginTransaction().remove(infoFragment).commit();
            mFamilyMemberInfoView.remove(index);

            FamilyMemberInfoFragment familyMemberInfoFragment = mFamilyMemberInfoView.get(0);
            if (familyMemberInfoFragment == null) {
                initFirstFragment();
            } else {
                showFamilyMember(familyMemberInfoFragment, getChildFragmentManager().beginTransaction(), 0);
            }
            if (mFamilyMemberList.size() == 1) {
                changeResident(0);
                familyMemberInfoFragment.refreshView(false);
            }
        }
    }

    /**
     * 改变家庭成员选中状态
     *
     * @param position
     */
    private void changeFamilyMemberView(int position) {
        ResidentBean lastResidentBean = mFamilyMemberList.get(mCurPos);
        lastResidentBean.setCheck(false);
        ResidentBean curResidentBean = mFamilyMemberList.get(position);
        curResidentBean.setCheck(true);
        mCurPos = position;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadFamilyMemberSuccess(List<ResidentBean> data) {
        mFamilyMemberList.clear();
        mFamilyMemberList.addAll(data);
        mResidentBean = mFamilyMemberList.get(0);
        mResidentBean.setCheck(true);
        mAdapter.notifyDataSetChanged();
        mAddFamily.setVisibility(View.VISIBLE);
        initFirstFragment();
    }

    @Override
    public void loadFamilyMemberError() {
        TextView tvMsg = emptyView.findViewById(R.id.tvMsg);
        ImageView ivImage = emptyView.findViewById(R.id.ivImage);
        tvMsg.setText("数据加载失败\n点击重试加载");
        tvMsg.setOnClickListener(this);
        ivImage.setOnClickListener(this);
    }

    private void showFamilyMember(final FamilyMemberInfoFragment familyMemberInfoFragment, FragmentTransaction fragmentTransaction, int position) {
        if (!familyMemberInfoFragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(MainActivity.KEY_RESIDENT, mResidentBean);
            bundle.putInt("index", position);
            final ResidentBean residentBean = mFamilyMemberList.get(mCurPos);
            bundle.putSerializable(KEY_RELATION, residentBean.getRelation());
            familyMemberInfoFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.layout_content, familyMemberInfoFragment);
            fragmentTransaction.commitAllowingStateLoss();
            /**
             * 请求网络太快了，导致对话还没实例就回调展示加载对话框,嵌套fragment生命周期引起
             */
            mLayoutContent.postDelayed(new Runnable() {
                @Override
                public void run() {
                    familyMemberInfoFragment.startLoadResidentInfo(residentBean);//开始加载数据患者资料
                }
            }, 200);
        } else {
            fragmentTransaction.show(familyMemberInfoFragment);
            familyMemberInfoFragment.refreshView(mFamilyMemberList.get(0).getFamilyId() > 0);
            fragmentTransaction.commitAllowingStateLoss();
        }
        mCurFamilyMemberInfoFragment = familyMemberInfoFragment;
        mAdapter.notifyDataSetChanged();
    }

    private void hideFamilyMember(FamilyMemberInfoFragment infoFragment, FragmentTransaction ft) {
        ft.hide(infoFragment);
    }

    public void changeResident(int index) {
        ResidentBean resident = mFamilyMemberList.get(index);
        String labelNames = DataServer.getTagShortName(resident.getTagIds());
        resident.setLabelNames(labelNames);
        ((MainActivity) getActivity()).setResident(false, resident);
    }

    public void updateMseeage(String tagIds, int index) {
        ResidentBean resident = mFamilyMemberList.get(index);
        resident.setTagIds(tagIds);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_family_member:
                mCreateTipDialog.show();
                break;
            case R.id.tv_no://添加居民
                Intent intent = new Intent(mActivity, NewResidentActivity.class);
                intent.putExtra(NewResidentActivity.KEY_FAMILY_ID, mResidentBean.getFamilyId());
                intent.putExtra(NewResidentActivity.KEY_PATIENT_ID, mFamilyMemberList.get(mPosition).getPatientId());
                intent.putExtra(NewResidentActivity.KEY_ADD_TYPE, NewResidentActivity.FLAG_ADD_FAMILY);
                startActivityForResult(intent, REQUEST_CODE_FAMILY);
                break;
            case R.id.tv_ok://去搜索然后进行添加
                Intent intent1 = new Intent(mActivity, RSearchActivity.class);
                intent1.putExtra("flag", 0);//添加家人flag为0
                startActivityForResult(intent1, REQUEST_CODE_SEARCH);
                break;
            case R.id.tvMsg:
            case R.id.ivImage:
                //居民信息没有展示时，按图像和文字都会进行加载
                ToastUtil.showShort("加载中，请稍等");
                mPresenter.loadFamilyMember(mResidentBean);
                break;

        }
    }

    /**
     * @param newResidentBean
     */
    public void updateAdapter(ResidentBean newResidentBean) {
        final ResidentBean residentBean = mFamilyMemberList.get(mCurPos);
        residentBean.setName(newResidentBean.getName());
        residentBean.setLabelNames(newResidentBean.getLabelNames());
        mAdapter.notifyDataSetChanged();
        FragmentActivity activity = getActivity();
//        if (mCurPos == 0 && activity instanceof MainActivity) {
//            ((MainActivity) activity).setResident(true, newResidentBean);
//        }
        if (activity instanceof MainActivity) {
            if (((MainActivity) activity).getResident() != null)
                if (mFamilyMemberList.get(mCurPos).getPatientId().equals(((MainActivity) activity).getResident().getPatientId())) {
                    ((MainActivity) activity).setResident(true, newResidentBean);
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        //添加居民的两种情况  1平台不存在  2 平台存在该居民
        /**
         *1来自新增添加患者家属
         */
        if (requestCode == REQUEST_CODE_FAMILY) {
            residentAdd = (ResidentBean) data.getSerializableExtra(MainActivity.KEY_RESIDENT);
            mFamilyMemberList.add(residentAdd);
            mAdapter.notifyDataSetChanged();
            String qrCodeUrl = data.getStringExtra(MainActivity.KEY_CODE_URL);
            if (!TextUtils.isEmpty(qrCodeUrl)) {
                ResidentQRCodeDialog codeDialog = new ResidentQRCodeDialog(mContext);
                codeDialog.setPatientId(residentAdd.getPatientId());
                codeDialog.setResidentCode(residentAdd.getResidentCode());
                codeDialog.setResidentQrUrl(qrCodeUrl);
                codeDialog.setType(0);
                codeDialog.show();
            }
        }
        /**
         * 2 来自平台已经存在的居民
         */
        if (requestCode == REQUEST_CODE_SEARCH) {//来自搜索添加患者家属
            residentAdd = (ResidentBean) data.getSerializableExtra(MainActivity.KEY_RESIDENT);
            if (residentAdd.getPatientId().equals(mResidentBean.getPatientId())) {
                ToastUtil.showShort("不能添加自己为家人哦~");
                return;
            }
            mRelationSelectDialog.setFirstResident(mResidentBean);
            mRelationSelectDialog.show();
        }
    }
}
