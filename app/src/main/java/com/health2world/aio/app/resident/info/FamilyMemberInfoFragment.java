package com.health2world.aio.app.resident.info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.app.adapter.ResidentTagListAdapter;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.app.resident.FamilyMemberMainFragment;
import com.health2world.aio.app.resident.view.FamilyRelationView;
import com.health2world.aio.app.resident.view.ResidentQRCodeDialog;
import com.health2world.aio.bean.TagInfo;
import com.health2world.aio.common.DataServer;
import com.health2world.aio.common.mvp.MVPBaseFragment;
import com.health2world.aio.util.DefaultTextWatcher;
import com.health2world.aio.util.IdCardUtil;
import com.health2world.aio.util.Logger;
import com.health2world.aio.util.StringUtils;
import com.konsung.bean.ResidentBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import aio.health2world.pickeview.TimePickerView;
import aio.health2world.pickeview.lib.WheelView;
import aio.health2world.utils.AppUtils;
import aio.health2world.utils.DateUtil;
import aio.health2world.utils.ToastUtil;

/**
 * Created by Administrator on 2018/7/18 0018.
 */

public class FamilyMemberInfoFragment extends MVPBaseFragment<FamilyMemberInfoContract.Presenter> implements FamilyMemberInfoContract.View {
    /**
     * UI
     */
    private TimePickerView mTimePickerView;
    private EditText mEditResidentBirthdeta, mEditResidentPhone;
    private CheckBox mCbBindAccount;
    private EditText mEditName, mEditIdCard, mEditLane;
    private EditText mEditSocietyNum, mEditAddress;
    private TagFlowLayout mTagLayout;
    private LinearLayout mLayoutRelation;
    private RadioButton mRadioMale, mRadioFemale;
    private EditText mEditRelation;
    private FamilyRelationView mFamilyRelationView;
    private EditText mEditAge;
    private TextView mTvRegister, tvChange;
    private Button mBtnSave, btnRelieve;
    private TextView mTvTitle;
    private LinearLayout mLayoutResientCode;
    private ResidentQRCodeDialog mQRCodeDialog;
    private EditText mEditResidentCode;
    private EditText mEdAllergy, mEditRemark;
    private View mViewLineCode;
    private ResidentTagListAdapter mTagAdapter;
    private List<TagInfo> mTagList = new ArrayList<>();
    private List<TagInfo> mCurrentTagBeanList = new ArrayList<>();
    private ResidentBean mFirstResidentBean;//家属列表第一个家属信息
    private ResidentBean mCurResidentBean;
    private int mShowRelation = View.VISIBLE;
    private ImageView mImgDate;
    private int index = 0;
    private TextView mRemarkLength;
    private TextView mAllergyLength;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_family_member;
    }


    @Override
    protected void initView() {
        tvChange = findView(R.id.tvChange);
        btnRelieve = findView(R.id.btnRelieve);
        mEditResidentBirthdeta = findView(R.id.edit_resident_birthdeta);
        mCbBindAccount = findView(R.id.cb_bind_account);
        mEditResidentPhone = findView(R.id.edit_resident_phone);
        mEditIdCard = findView(R.id.edit_idcard);
        mEditName = findView(R.id.edit_name);
        mTagLayout = findView(R.id.tagLayout);
        mLayoutResientCode = findView(R.id.layout_resident_code);
        mEditLane = findView(R.id.edit_lane);
        mRadioMale = findView(R.id.radio_male);
        mRadioFemale = findView(R.id.radio_female);
        mEditSocietyNum = findView(R.id.edit_society_num);
        mEditAddress = findView(R.id.edit_address);
        mLayoutRelation = findView(R.id.layout_relation);
        mEditRelation = findView(R.id.edit_relation);
        mTvTitle = findView(R.id.tv_title);
        mEditResidentCode = findView(R.id.edit_resident_code);
        mBtnSave = findView(R.id.tv_save);
        mEdAllergy = findView(R.id.edit_allergy);
        mEditRemark = findView(R.id.edit_remarks);
        mImgDate = findView(R.id.img_see_date);
        mQRCodeDialog = new ResidentQRCodeDialog(mActivity);
        mEditAge = findView(R.id.edit_age);
        mFamilyRelationView = new FamilyRelationView(mActivity);
        mTvRegister = findView(R.id.tv_register);
        mViewLineCode = findView(R.id.view_line_code);
        mLayoutRelation.setVisibility(mShowRelation);
        mLayoutResientCode.setVisibility(View.VISIBLE);
        mViewLineCode.setVisibility(View.VISIBLE);
        mCbBindAccount.setVisibility(View.GONE);
        mRemarkLength = findView(R.id.tv_remarks_length);
        mAllergyLength = findView(R.id.tv_allergy_length);
    }

    @Override
    protected void initData() {
        AppUtils.setEditTextInhibitInputSpeChat(mEditName, 10);
        mFirstResidentBean = (ResidentBean) getArguments().getSerializable(MainActivity.KEY_RESIDENT);
        index = getArguments().getInt("index", 0);
        String relation = getArguments().getString(FamilyMemberMainFragment.KEY_RELATION);
        setRelation(relation);
        mTagAdapter = new ResidentTagListAdapter(mActivity, mTagList);
        mTagLayout.setAdapter(mTagAdapter);
        mPresenter.loadTagInfo();
        if (index == 0) {
            btnRelieve.setVisibility(View.INVISIBLE);
        } else {
            refreshView(false);
        }
    }

    public void refreshView(boolean v) {
        Logger.i("lsy", "refreshView");
        List<ResidentBean> list = ((FamilyMemberMainFragment) getParentFragment()).getFamilyMemberList();
        if (list.size() == 1) {
            tvChange.setVisibility(View.INVISIBLE);
        } else {
            tvChange.setVisibility(View.VISIBLE);
        }
        if (list.get(index).getFamilyId() > 0 && index != 0) {
            btnRelieve.setVisibility(View.VISIBLE);
        } else {
            btnRelieve.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvChange:
                ((FamilyMemberMainFragment) getParentFragment()).changeResident(index);
                break;
            case R.id.btnRelieve:
                showRelieveDialog();
                break;
            case R.id.edit_relation:
                mFamilyRelationView.showPopupWindow(mEditRelation);
                break;
            case R.id.layout_resident_code:
                mQRCodeDialog.setType(1);//设置对话框类型为查看居民二维码
                mQRCodeDialog.setResidentCode(mCurResidentBean.getResidentCode());
                mQRCodeDialog.setPatientId(mCurResidentBean.getPatientId());
                mQRCodeDialog.show();
                break;
            case R.id.edit_resident_birthdeta:
            case R.id.img_see_date:
                mTimePickerView.show();
                break;
            case R.id.tv_save:
                boolean b = checkData();
                if (!b)
                    return;
                ResidentBean residentBean = getResidentBean(mCurResidentBean);
                String firstPatientId = mFirstResidentBean.getPatientId();
                String addTagIds = splicingTagIds(getAddTagInfo());//获取添加标签和并且拼接tagId
                String delTagIds = splicingTagIds(getDelTagInfo());
                mPresenter.updateResidentBean(firstPatientId, residentBean, addTagIds, delTagIds);
                break;
        }

    }

    @Override
    public void relieveSuccess() {
        ((FamilyMemberMainFragment) getParentFragment()).relieveFamilyRelation(index);
    }

    @Override
    public void startLoadResidentInfo(ResidentBean residentBean) {
        mPresenter.loadResidentInfo(residentBean);
    }

    /**
     * 退出当前家庭的确认对话框
     */
    private void showRelieveDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.action_tips))
                .setMessage(getString(R.string.exit_family_group))
                .setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.relieveFamilyRelation(mCurResidentBean.getPatientId());
                    }
                })
                .setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();

    }

    /**
     * 通过身份证号 设置 性别，生日，年龄
     *
     * @param idCard
     */
    private void autoGetPersonInfoByIdCard(String idCard) {
        if (idCard.length() == 18 || idCard.length() == 15) {
            IdCardUtil idCardUtil = new IdCardUtil(idCard);
            int correct = idCardUtil.isCorrect();
            if (correct != 0) {
                return;
            }
            String sex = idCardUtil.getSex();//男
            if (sex.equals("男")) {
                mRadioFemale.setChecked(false);
                mRadioMale.setChecked(true);
            } else {
                mRadioMale.setChecked(false);
                mRadioFemale.setChecked(true);
            }
            String birthday = idCardUtil.getBirthday();
            StringBuilder build = new StringBuilder(birthday);//扫描身份证得来的出生日期 进行特殊处理
            build.insert(4, "-");
            build.insert(7, "-");
            mEditResidentBirthdeta.setText(build.toString());
            int age = idCardUtil.getAge();
            mEditAge.setText(String.valueOf(age));
        }
    }

    /**
     * 设置界面上患者信息
     *
     * @param resident
     */
    @Override
    public void setResident(ResidentBean resident) {
        mCurResidentBean = resident;
        mEditName.setText(resident.getName());
        mEditIdCard.setText(resident.getIdentityCard());
        mEditResidentPhone.setText(resident.getTelPhone());
        mEditLane.setText(resident.getTelPhoneUrgent());
        mEditResidentPhone.setText(resident.getTelPhone());
        mEditLane.setText(resident.getTelPhoneUrgent());
        mEdAllergy.setText(resident.getAllergy());
        mEditRemark.setText(resident.getRemark());
        mEditResidentBirthdeta.setText(resident.getBirthday());
        if (resident.getAge() != -1) {
            mEditAge.setText(String.valueOf(resident.getAge()));
        } else if (!"".equals(resident.getBirthday())) {
            mEditAge.setText(String.valueOf(
                    Calendar.getInstance().get(Calendar.YEAR) -
                            Integer.valueOf(resident.getBirthday().substring(0, 4))));
        } else if (!"".equals(resident.getIdentityCard())) {
            autoGetPersonInfoByIdCard(resident.getIdentityCard());
        }
        mEditResidentCode.setText(resident.getResidentCode());

        if (resident.getSexy() == 0) {
            mRadioFemale.setChecked(true);
        } else if (resident.getSexy() == 1) {
            mRadioMale.setChecked(true);
        } else {
            mRadioFemale.setChecked(false);
            mRadioMale.setChecked(false);
        }
        mEditAddress.setText(resident.getLocalAddress());
        mTvTitle.setText(resident.getName() + "的资料");
        mEditSocietyNum.setText(resident.getMedicareCard());
        mTvRegister.setText(resident.getRegister() == 1 ? "(已注册)" : "(未注册)");
        setTagInfo(resident.getTagIds());
        isEditableResidentInfo(resident);//判断是否可以编辑患者信息
    }

    /**
     * @param resident
     */
    private void isEditableResidentInfo(ResidentBean resident) {
        String identityCard = resident.getIdentityCard();
        String phone = resident.getTelPhone();
        String telphoneUrgent = resident.getTelPhoneUrgent();
        String birthday = resident.getBirthday();
        String address = resident.getLocalAddress();
        String medicareCard = resident.getMedicareCard();
        int sexy = resident.getSexy();
        mEditName.setEnabled(isEditRegister(resident.getName()));
        mEditRelation.setEnabled(isEditRegister(resident.getRelation()));
        mEditIdCard.setEnabled(isEditRegister(identityCard));
        mEditResidentPhone.setEnabled(isEditRegister(phone));
        mEditLane.setEnabled(isEditRegister(telphoneUrgent));
        mEditResidentBirthdeta.setEnabled(isEditRegister(birthday));
        if (!isEditRegister(birthday)) {
            mImgDate.setVisibility(View.INVISIBLE);
        }
        if (resident.getRegister() == 1 && (sexy == 1 || sexy == 0)) {
            mRadioFemale.setEnabled(false);
            mRadioMale.setEnabled(false);
        } else {
            mRadioFemale.setEnabled(true);
            mRadioMale.setEnabled(true);
        }
        if (resident.getAge() > 0) {
            mEditAge.setEnabled(isEditRegister(String.valueOf(resident.getAge())));
        }
        mEditAddress.setEnabled(isEditRegister(address));
        mEditSocietyNum.setEnabled(isEditRegister(medicareCard));
    }

    private boolean isEditRegister(String str) {
        return StringUtils.isEmpty(str) || mCurResidentBean.getRegister() == 0;
    }

    public ResidentBean getResidentBean(ResidentBean residentBean) {
        String name = mEditName.getText().toString();
        residentBean.setName(name);
        String idCard = mEditIdCard.getText().toString();
        residentBean.setIdentityCard(idCard);
        String telephone = mEditResidentPhone.getText().toString();
        residentBean.setTelPhone(telephone);
        String Lane = mEditLane.getText().toString();
        residentBean.setTelPhoneUrgent(Lane);
        String birthday = mEditResidentBirthdeta.getText().toString();
        String relation = mEditRelation.getText().toString();
        residentBean.setRelation(relation);
        residentBean.setBirthday(birthday);
        int sex = 2;// 2-未知 1-男 0-女
        if (mRadioFemale.isChecked()) {
            sex = 0;
        }
        if (mRadioMale.isChecked()) {
            sex = 1;
        }
        residentBean.setSexy(sex);
        String age = mEditAge.getText().toString();
        if (!TextUtils.isEmpty(age)) {
            residentBean.setAge(Integer.parseInt(age));

        }
        String address = mEditAddress.getText().toString();
        residentBean.setLocalAddress(address);
        String societyNum = mEditSocietyNum.getText().toString();

        residentBean.setMedicareCard(societyNum);
        String allergy = mEdAllergy.getText().toString();
        residentBean.setAllergy(allergy);
        String remark = mEditRemark.getText().toString();
        residentBean.setRemark(remark);
        return residentBean;
    }

    @Override
    public void loadAddTagInfoSuccess(List<TagInfo> data) {
        mTagList.addAll(data);
        mTagAdapter.notifyDataChanged();
    }

    @Override
    public void updateResidentSuccess(String tip) {
        ToastUtil.showShort(tip);
        isEditableResidentInfo(mCurResidentBean);
        mCurResidentBean.setLabelNames(DataServer.getTagShortName(getTagIds()));
        String name = mCurResidentBean.getName();
        mEditName.setText(name);
        mTvTitle.setText(mCurResidentBean.getName());
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof FamilyMemberMainFragment) {
            ((FamilyMemberMainFragment) parentFragment).updateMseeage(getTagIds(), index);
            ((FamilyMemberMainFragment) parentFragment).updateAdapter(mCurResidentBean);
        }
    }

    private void setTagInfo(String tagIdsStr) {
        if (!TextUtils.isEmpty(tagIdsStr)) {
            String[] strTagIdsArrays = tagIdsStr.split(",");
            for (TagInfo tagBean : mTagList) {
                for (int i = 0; i < strTagIdsArrays.length; i++) {
                    String TagId = strTagIdsArrays[i];
                    if (String.valueOf(tagBean.getTagId()).equals(TagId) && !mCurrentTagBeanList.contains(tagBean)) {
                        tagBean.setChecked(true);
                        mCurrentTagBeanList.add(tagBean);
                    }
                }
            }
        }
        mTagAdapter.notifyDataChanged();
    }

    public List<TagInfo> getAddTagInfo() {
        List<TagInfo> addTagList = new ArrayList<>();
        for (TagInfo tagBean : mTagList) {
            if (tagBean.isChecked()) {
                addTagList.add(tagBean);//当前选择标签
            }
        }
        addTagList.removeAll(mCurrentTagBeanList);
        System.out.println("添加标签个数：" + addTagList.size());
        return addTagList;
    }

    public String splicingTagIds(List<TagInfo> data) {
        String tagIds = "";
        for (int i = 0; i < data.size(); i++) {
            String tagId = String.valueOf(data.get(i).getTagId());
            if (i == data.size() - 1) {
                tagIds += tagId;
            } else {
                tagIds += tagId + ",";
            }
        }
        return tagIds;

    }

    public String getTagIds() {
        String tadIds = "";
        for (TagInfo info : mTagList) {
            if (info.isChecked())
                tadIds += info.getTagId() + ",";
        }
        if (!TextUtils.isEmpty(tadIds))
            tadIds = tadIds.substring(0, tadIds.length() - 1);
        return tadIds;
    }

    public List<TagInfo> getDelTagInfo() {
        List<TagInfo> delTagList = new ArrayList<>();
        for (TagInfo tagBean : mTagList) {
            if (!tagBean.isChecked()) {
                delTagList.add(tagBean);//当前未选择
            }
        }
        delTagList.retainAll(mCurrentTagBeanList);
        System.out.println("删除标签个数：" + delTagList.size());
        return delTagList;
    }

    /**
     * 设置是否展示关系控件
     *
     * @param visibility
     */
    public void showRelation(int visibility) {
        this.mShowRelation = visibility;
    }

    /**
     * 设置关系控件
     *
     * @param relation
     */
    public void setRelation(String relation) {
        mEditRelation.setText(relation);
    }


    @Override
    protected void initListener() {
        tvChange.setOnClickListener(this);
        btnRelieve.setOnClickListener(this);
        mEditRelation.setOnClickListener(this);
        mEditResidentBirthdeta.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mImgDate.setOnClickListener(this);
        mLayoutResientCode.setOnClickListener(this);
        mLayoutRelation.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mFamilyRelationView.setSelectRelationListener(new FamilyRelationView.RelationListener() {
            @Override
            public void onClick(String relation) {
                mEditRelation.setText(relation);
            }
        });
        /**
         * 选择日期后回调
         */
        mTimePickerView = initTimePickerView(mActivity, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = DateUtil.getTime(date);
                mEditResidentBirthdeta.setText(time);
                mEditAge.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - Integer.valueOf(time.substring(0, 4))));
            }
        });
        mTagLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (view instanceof FrameLayout) {
                    TagInfo tagInfoBean = mTagList.get(position);
                    tagInfoBean.setChecked(!tagInfoBean.isChecked());
                    mTagAdapter.notifyDataChanged();
                }
                return true;
            }
        });
        /**
         * 通过用户输入获取身份证号 拿到对应的信息
         */
        mEditIdCard.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String idCard = s.toString();
                autoGetPersonInfoByIdCard(idCard);
            }
        });

        mEditRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                mRemarkLength.setText(mEditRemark.getText().length() + "/200");
                if (mEditRemark.getText().length() > 190)
                    mRemarkLength.setTextColor(Color.RED);
                else
                    mRemarkLength.setTextColor(getResources().getColor(R.color.blackD));
            }
        });


        mEdAllergy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAllergyLength.setText(mEdAllergy.getText().length() + "/20");
                if (mEdAllergy.getText().length() > 19)
                    mAllergyLength.setTextColor(Color.RED);
                else
                    mAllergyLength.setTextColor(getResources().getColor(R.color.blackD));
            }
        });


    }

    @Override
    protected FamilyMemberInfoContract.Presenter getPresenter() {
        return new FamilyMemberInfoPresenter(this);
    }

    private boolean checkData() {
        String name = mEditName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showShort("姓名不能为空");
            return false;
        }
        //校验手机号
        String phone = mEditResidentPhone.getText().toString();
        if (!TextUtils.isEmpty(phone) && (!phone.startsWith("1") || phone.length() != 11)) {
            ToastUtil.showShort("手机号格式错误");
            return false;
        }
        //校验社保卡号
        String societyNum = mEditSocietyNum.getText().toString();
        if (!TextUtils.isEmpty(societyNum) && societyNum.length() < 6) {
            ToastUtil.showShort("社保号格式错误");
            return false;
        }
        //身份证号码校验
        String idCard = mEditIdCard.getText().toString();
        if (!TextUtils.isEmpty(idCard)) {
            IdCardUtil util = new IdCardUtil(idCard);
            if (util.isCorrect() != 0) {
                ToastUtil.showShort("身份证号码格式错误");
                return false;
            }
        }
        //固话校验
        String lane = mEditLane.getText().toString();
        if (!TextUtils.isEmpty(lane) && lane.length() < 5) {
            ToastUtil.showShort("请输入5-12位固定电话");
            return false;
        }
        return true;
    }

    /***
     * 日期对话框
     * @param context
     * @param listener
     * @return
     */
    public TimePickerView initTimePickerView(Context context, TimePickerView.OnTimeSelectListener listener) {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1920, 0, 23);
        Calendar endDate = Calendar.getInstance();
        //时间选择器
        TimePickerView pvTime = new TimePickerView
                .Builder(context, listener)
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
                .isCenterLabel(false)
                .isDialog(true)
                .setTitleSize(22)
                .setContentSize(22)
                .setCancelText("取消")
                .setSubmitText("确定")
                .setTitleColor(context.getResources().getColor(R.color.colorPrimary))
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimary))
                .setDividerColor(context.getResources().getColor(R.color.colorPrimary))
                .setTextColorCenter(context.getResources().getColor(R.color.colorPrimary))
                .setDividerType(WheelView.DividerType.WRAP)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .build();
        return pvTime;
    }
}
