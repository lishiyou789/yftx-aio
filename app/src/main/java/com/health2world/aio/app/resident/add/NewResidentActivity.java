package com.health2world.aio.app.resident.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.health2world.aio.R;
import com.health2world.aio.app.adapter.ResidentTagListAdapter;
import com.health2world.aio.app.home.MainActivity;
import com.health2world.aio.app.resident.view.FamilyRelationView;
import com.health2world.aio.bean.TagInfo;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.util.ActivityUtil;
import com.health2world.aio.util.DefaultTextWatcher;
import com.health2world.aio.util.IdCardUtil;
import com.health2world.aio.util.TitleBarUtil;
import com.health2world.aio.view.TitleBar;
import com.konsung.bean.ResidentBean;
import com.konsung.listen.IdCardListen;
import com.konsung.util.MeasureUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aio.health2world.pickeview.TimePickerView;
import aio.health2world.pickeview.lib.WheelView;
import aio.health2world.utils.AppUtils;
import aio.health2world.utils.DateUtil;
import aio.health2world.utils.ToastUtil;

/**
 * Created by Administrator on 2018/7/16 0016.
 */

/**
 * 添加居民，添加家人同一个界面
 */
public class NewResidentActivity extends MVPBaseActivity<NewResidentContract.Presenter> implements NewResidentContract.View,
        IdCardListen {
    /**
     * const
     */
    public static final int FLAG_ADD_RESIDENT = 321;//添加居民
    public static final int FLAG_ADD_FAMILY = 322;//添加家人
    public static final String KEY_ADD_TYPE = "ADD_TYPE";
    public static final String KEY_PATIENT_ID = "PATIENT_ID";
    public static final String KEY_FAMILY_ID = "FAMILY_ID";
    /**
     * UI
     */
    private TitleBar titleBar;
    private TextView mTvNavOne, mTvNavTwo, mTvNavThree;
    private TextView mTvBackBase, mTvBackDetails;
    private View mViewOne, mViewTwo;
    private Button mBtnBaseNext, mBtnDetails, mBtnFinish;
    private ViewGroup mLayoutResidentBase, mLayoutResidentDetails, mLayoutResidentHealth;
    private LinearLayout mLayoutRelation;
    private TimePickerView mTimePickerView;
    private EditText mEditResidentBirthday, mEditResidentPhone;
    private CheckBox mCbBindAccount;
    private EditText mEditName, mEditIdCard, mEditLane;
    private EditText mEditSocietyNum, mEditAddress;
    private TagFlowLayout mTagLayout;
    private RadioButton mRadioMale, mRadioFemale;
    private EditText mEditAge;
    private ResidentTagListAdapter mTagAdapter;
    private int mAddType = FLAG_ADD_RESIDENT;
    private EditText mEditRelation, mEdAllergy, mEditRemarks;
    private FamilyRelationView mFamilyRelationView;
    private View mViewLineRelation;
    private ImageView mImgDate;
    private int familyId;
    private TextView mRemarkLength;
    private TextView mAllergyLength;
    /**
     * data
     */
    private List<TagInfo> mTagList = new ArrayList<>();
    private String mPatientId = "";//患者id  添加家属需要

    @Override
    protected NewResidentContract.Presenter getPresenter() {
        return new NewResidentPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_resident;
    }

    @Override
    protected void initView() {
        initNavView();
        titleBar = findView(R.id.titleBar);
        TitleBarUtil.setAttr(this, "新增居民", "", titleBar);
        mViewLineRelation = findView(R.id.view_line_relation);
        mBtnFinish = findView(R.id.btn_finish);
        mEditLane = findView(R.id.edit_lane);
        mRadioMale = findView(R.id.radio_male);
        mRadioFemale = findView(R.id.radio_female);
        mEditSocietyNum = findView(R.id.edit_society_num);
        mEditAddress = findView(R.id.edit_address);
        mLayoutRelation = findView(R.id.layout_relation);
        mEditAge = (EditText) findViewById(R.id.edit_age);
        mEditRelation = findView(R.id.edit_relation);
        mEdAllergy = findView(R.id.edit_allergy);
        mEditRemarks = findView(R.id.edit_remarks);
        mImgDate = findView(R.id.img_see_date);
        mRemarkLength = findView(R.id.tv_remarks_length);
        mAllergyLength = findView(R.id.tv_allergy_length);
    }

    private void initNavView() {
        mTvNavOne = findView(R.id.tv_nav_one);
        mTvNavTwo = findView(R.id.tv_nav_two);
        mTvNavThree = findView(R.id.tv_nav_three);

        mViewOne = findView(R.id.view_line_one);
        mViewTwo = findView(R.id.view_line_two);

        mBtnBaseNext = findView(R.id.btn_base_next);
        mBtnBaseNext.setEnabled(false);
        mBtnDetails = findView(R.id.btn_details_next);

        mLayoutResidentBase = findView(R.id.layout_resident_base);
        mLayoutResidentDetails = findView(R.id.layout_resident_details);
        mLayoutResidentHealth = findView(R.id.layout_resident_health);

        mTvBackBase = findView(R.id.tv_back_base);
        mTvBackDetails = findView(R.id.tv_back_detail);

        mEditResidentBirthday = findView(R.id.edit_resident_birthdeta);
        mCbBindAccount = findView(R.id.cb_bind_account);
        mCbBindAccount.setEnabled(false);

        mEditResidentPhone = findView(R.id.edit_resident_phone);
        mEditIdCard = findView(R.id.edit_idcard);
        mEditName = findView(R.id.edit_name);
        mTagLayout = findView(R.id.tagLayout);
        mFamilyRelationView = new FamilyRelationView(this);
        AppUtils.setEditTextInhibitInputSpeChat(mEditName, 10);

    }

    @Override
    protected void initData() {
        mTagAdapter = new ResidentTagListAdapter(this, mTagList);
        mTagLayout.setAdapter(mTagAdapter);
        mAddType = getIntent().getIntExtra(KEY_ADD_TYPE, FLAG_ADD_RESIDENT);

        mPresenter.loadTagInfo();

        switchAddPage();

        //首页刷身份证判断无档案 带过来的信息
        if (getIntent().hasExtra(MainActivity.KEY_RESIDENT)) {
            ResidentBean resident = (ResidentBean) getIntent().getSerializableExtra(MainActivity.KEY_RESIDENT);
            setResident(resident);
        }
        if (getIntent().hasExtra(KEY_FAMILY_ID)) {
            familyId = getIntent().getIntExtra(KEY_FAMILY_ID, 0);
        }
    }

    private void switchAddPage() {
        if (mAddType == FLAG_ADD_RESIDENT) {
            mLayoutRelation.setVisibility(View.GONE);//新建居民隐藏关系选择控件
            titleBar.setLeftText("新增居民");
            mViewLineRelation.setVisibility(View.GONE);
        }
        if (mAddType == FLAG_ADD_FAMILY) {
            titleBar.setLeftText("新增家属");
            mLayoutRelation.setVisibility(View.VISIBLE);
            mViewLineRelation.setVisibility(View.VISIBLE);
            mPatientId = getIntent().getStringExtra(KEY_PATIENT_ID);
        }
    }

    @Override
    protected void initListener() {
        MeasureUtils.setIdCardListen(this);
        mImgDate.setOnClickListener(this);
        mBtnBaseNext.setOnClickListener(this);
        mBtnDetails.setOnClickListener(this);
        mTvBackBase.setOnClickListener(this);
        mTvBackDetails.setOnClickListener(this);
        mEditResidentBirthday.setOnClickListener(this);
        mBtnFinish.setOnClickListener(this);
        mEditRelation.setOnClickListener(this);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * 选择日期后回调
         */
        mTimePickerView = initTimePickerView(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = DateUtil.getTime(date);
                mEditResidentBirthday.setText(time);
                mEditAge.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - Integer.valueOf(time.substring(0, 4))));
            }
        });
        /**
         * 对手机号输入监听
         */
        mEditResidentPhone.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                boolean isCheck = !TextUtils.isEmpty(s.toString());
                mCbBindAccount.setEnabled(isCheck);
//                mCbBindAccount.setChecked(isCheck);
            }
        });
        /**
         * 是否绑定手机号码为账号进行监听
         */
        mCbBindAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCbBindAccount.setText(R.string.binded);
                } else {
                    mCbBindAccount.setText(R.string.unbind);
                }
                //校验手机号
                String phone = mEditResidentPhone.getText().toString();
                boolean isLeaglePhone = !TextUtils.isEmpty(phone) && (!phone.startsWith("1") || phone.length() != 11);
                if (isChecked && isLeaglePhone) {
                    ToastUtil.showShort("绑定手机时，请填写正确的手机号码");
                    return;
                }
                if (isChecked) {
                    mPresenter.validateTelphone(phone);
                }

            }
        });
        mTagLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (view instanceof FrameLayout) {
                    TagInfo tagInfoBean = mTagList.get(position);
                    tagInfoBean.setChecked(!tagInfoBean.isChecked());
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
        mFamilyRelationView.setSelectRelationListener(new FamilyRelationView.RelationListener() {
            @Override
            public void onClick(String relation) {
                mEditRelation.setText(relation);
            }
        });
        /**
         *   第一次打开键盘为数字键盘
         */
        mEditIdCard.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'X', 'x'};
                return chars;
            }

            @Override
            public int getInputType() {
                return 3;
            }
        });
        mEditLane.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '—'};
                return chars;
            }

            @Override
            public int getInputType() {
                return 3;
            }
        });
        /**
         *监听
         */
        mEditName.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String name = s.toString();
                boolean isBlankName = !TextUtils.isEmpty(name);
                mBtnBaseNext.setEnabled(isBlankName);
            }
        });

        mEditRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                mRemarkLength.setText(mEditRemarks.getText().length() + "/200");
                if (mEditRemarks.getText().length() > 190)
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
                if (mEdAllergy.getText().length()>19)
                    mAllergyLength.setTextColor(Color.RED);
                else
                    mAllergyLength.setTextColor(getResources().getColor(R.color.blackD));
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                ActivityUtil.finishActivity(this);
                break;
            case R.id.btn_base_next:
                if (checkBaseData()) {
                    String idCard = mEditIdCard.getText().toString();
                    if (!TextUtils.isEmpty(idCard)) {
//                        mPresenter.validateIdCard(idCard);
                        mPresenter.validateIdentityCard(familyId, idCard, mPatientId);
                    } else {
                        toResidentDetails();
                    }
                }
                break;
            case R.id.btn_details_next:
                if (checkDetailsData()) {
                    toResidentHealth();
                }
                break;
            case R.id.tv_back_base:
                backResidentBase();
                break;
            case R.id.tv_back_detail:
                backResidentDetails();
                break;
            case R.id.edit_resident_birthdeta:
            case R.id.img_see_date:
                mTimePickerView.show();
                break;
            case R.id.btn_finish:
                if (AppUtils.isFastClick()) {
                    ResidentBean residentBean = getResidentBean();
                    if (mAddType == FLAG_ADD_RESIDENT) {
                        mPresenter.postNewResident(residentBean);//添加新居民
                    }
                    if (mAddType == FLAG_ADD_FAMILY) {
                        mPatientId = getIntent().getStringExtra(KEY_PATIENT_ID);
                        mPresenter.postNewFamilyMember(mPatientId, residentBean);//添加新家属
                    }
                } else {
                    ToastUtil.showShort("点击太快");
                }

                break;
            case R.id.edit_relation:
                mFamilyRelationView.showPopupWindow(mEditRelation);
                break;
        }
    }

    /**
     * 界面切换到详细页面
     */
    @Override
    public void toResidentDetails() {
        mTvNavOne.setBackgroundResource(R.drawable.shape_add_resident_nav_select);
        mTvNavTwo.setBackgroundResource(R.drawable.shape_add_resident_nav_select);
        mViewOne.setBackgroundResource(R.color.tag_bg_color);
        mLayoutResidentBase.setVisibility(View.GONE);
        mLayoutResidentDetails.setVisibility(View.VISIBLE);
    }


    /**
     * 界面切换到健康页面
     */
    @Override
    public void toResidentHealth() {
        mTvNavTwo.setBackgroundResource(R.drawable.shape_add_resident_nav_select);
        mTvNavThree.setBackgroundResource(R.drawable.shape_add_resident_nav_select);
        mViewTwo.setBackgroundResource(R.color.tag_bg_color);
        mLayoutResidentDetails.setVisibility(View.GONE);
        mLayoutResidentHealth.setVisibility(View.VISIBLE);
    }

    /**
     * 返回详情
     */
    @Override
    public void backResidentDetails() {
        mTvNavTwo.setBackgroundResource(R.drawable.shape_add_resident_nav_select);
        mTvNavThree.setBackgroundResource(R.drawable.shape_add_resident_nav_unselect);
        mViewTwo.setBackgroundResource(R.color.blackC);
        mLayoutResidentDetails.setVisibility(View.VISIBLE);
        mLayoutResidentHealth.setVisibility(View.GONE);
    }

    /**
     * 返回基础资料
     */
    @Override
    public void backResidentBase() {
        mTvNavOne.setBackgroundResource(R.drawable.shape_add_resident_nav_select);
        mTvNavTwo.setBackgroundResource(R.drawable.shape_add_resident_nav_unselect);
        mViewOne.setBackgroundResource(R.color.tag_bg_color);
        mLayoutResidentBase.setVisibility(View.VISIBLE);
        mLayoutResidentDetails.setVisibility(View.GONE);
    }

    @Override
    public void loadTagInfoSuccess(List<TagInfo> data) {
        mTagList.addAll(data);
        mTagAdapter.notifyDataChanged();
    }

    @Override
    public List<TagInfo> getCheckTag() {
        List<TagInfo> checkTagInfo = new ArrayList<>();
        for (TagInfo tag : mTagList) {
            if (tag.isChecked()) {
                checkTagInfo.add(tag);
            }
        }
        return checkTagInfo;
    }

    @Override
    public ResidentBean getResidentBean() {
        ResidentBean residentBean = new ResidentBean();
        String name = mEditName.getText().toString();
        residentBean.setName(name);
        String phone = mEditResidentPhone.getText().toString();
        residentBean.setTelPhone(phone);

        //添加家属时，获取关系
        if (mAddType == FLAG_ADD_FAMILY) {
            String relation = mEditRelation.getText().toString();
            residentBean.setRelation(relation);
        }
        residentBean.setIsBindingAccount(mCbBindAccount.isChecked() ? "1" : "0");
        String idCard = mEditIdCard.getText().toString();
        residentBean.setIdentityCard(idCard);
        String Lane = mEditLane.getText().toString();
        residentBean.setTelPhoneUrgent(Lane);
        String birthday = mEditResidentBirthday.getText().toString();
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
        List<TagInfo> checkTag = getCheckTag();
        String tagIds = "";
        for (int i = 0; i < checkTag.size(); i++) {
            TagInfo tagInfoBean = checkTag.get(i);
            if (i == checkTag.size() - 1) {
                tagIds += tagInfoBean.getTagId();
            } else {
                tagIds += tagInfoBean.getTagId() + ",";
            }
        }
        residentBean.setTagIds(tagIds);
        //过敏史
        residentBean.setAllergy(mEdAllergy.getText().toString());
        //备注
        residentBean.setRemark(mEditRemarks.getText().toString());

        return residentBean;
    }

    @Override
    public void onListen(String name, String idCard, int sex, int type, String birthday, String picture, String address) {
        ResidentBean resident = IdCardUtil.createResident(name, idCard, address);
        setResident(resident);
    }

    /**
     * 首页刷身份在带进来的居民信息
     */
    private void setResident(ResidentBean resident) {
        if (resident == null)
            return;
        mEditName.setText(resident.getName());
        mEditIdCard.setText(resident.getIdentityCard());
        String birthday = resident.getBirthday();
        if (!TextUtils.isEmpty(birthday)) {
            StringBuilder build = new StringBuilder(birthday);//扫描身份证得来的出生日期 进行特殊处理
            build.insert(4, "-");
            build.insert(7, "-");
            mEditResidentBirthday.setText(build.toString());
        }
        if (resident.getSexy() == 0)
            mRadioFemale.setChecked(true);
        else
            mRadioMale.setChecked(true);
        mEditAge.setText(resident.getAge() + "");
        mEditAddress.setText(resident.getAddress());
        mBtnBaseNext.setEnabled(true);
    }

    @Override
    public void addResidentSuccess(ResidentBean residentBean, String residentCodeUrl) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.KEY_RESIDENT, residentBean);
        intent.putExtra(MainActivity.KEY_CODE_URL, residentCodeUrl);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void addFamilySuccess(ResidentBean residentBean, String residentCodeUrl) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.KEY_RESIDENT, residentBean);
        intent.putExtra(MainActivity.KEY_CODE_URL, residentCodeUrl);
        setResult(Activity.RESULT_OK, intent);
        finish();
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


    @Override
    public boolean checkBaseData() {
        String name = mEditName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showShort("请输入姓名");
            return false;
        }
        String relation = mEditRelation.getText().toString();
        if (mAddType == FLAG_ADD_FAMILY && TextUtils.isEmpty(relation)) {
            ToastUtil.showShort("请选择关系");
            return false;
        }
        //校验身份证
        String IdCard = mEditIdCard.getText().toString();
        if (!TextUtils.isEmpty(IdCard)) {
            IdCardUtil idCardUtil = new IdCardUtil(IdCard);
            if (idCardUtil.isCorrect() != 0) {
                ToastUtil.showShort(idCardUtil.getErrMsg());
                return false;
            }
        }
        //校验手机号
        String phone = mEditResidentPhone.getText().toString();
        if (!TextUtils.isEmpty(phone) && (!phone.startsWith("1") || phone.length() != 11)) {
            ToastUtil.showShort("手机号格式错误");
            return false;
        }
        String lane = mEditLane.getText().toString();
        if (!TextUtils.isEmpty(lane) && lane.length() < 5) {
            ToastUtil.showShort("请输入5-12位固定电话");
            return false;
        }
        return true;
    }

    @Override
    public boolean checkDetailsData() {
        String age = mEditAge.getText().toString();
        if (!TextUtils.isEmpty(age) && Integer.parseInt(age) > 120) {
            ToastUtil.showShort("请输入真实年龄");
            return false;
        }
        //校验社保卡号
        String societyNum = mEditSocietyNum.getText().toString();
        if (!TextUtils.isEmpty(societyNum) && societyNum.length() < 6) {
            ToastUtil.showShort("社保号格式错误");
            return false;
        }
        return true;
    }

    private ResidentBean resident99;

    @Override
    public void validateCallBack(ResidentBean residentBean) {
        if (residentBean != null) {
            resident99 = residentBean;
            resident99.setFamilyId(familyId);
            resident99.setRelation(mEditRelation.getText().toString());
            mPresenter.relevancyPatientInfo(mPatientId, residentBean.getPatientId(), mEditRelation.getText().toString());
        }
    }

    @Override
    public void validateIdentityCardSuccess(final String patientId) {
        //检验通过进行下一步
        if (TextUtils.isEmpty(patientId)) {
            toResidentDetails();
        } else {
            if (familyId != 0) {
                //弹出关联的窗口
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.action_tips))
                        .setMessage(getString(R.string.family_member_exist))
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.relation), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.validateIdCard(mEditIdCard.getText().toString().trim());
                            }
                        })
                        .create()
                        .show();
            }
        }
    }


    @Override
    public void valildateTelphoneCallBack(boolean isExit) {
        if (isExit) {
            mCbBindAccount.setText(R.string.unbind);
            mCbBindAccount.setChecked(false);
        }
    }

    @Override
    public void relevancyPatientInfoSuccess() {
        addResidentSuccess(resident99, "");
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
            mEditResidentBirthday.setText(build.toString());
            int age = idCardUtil.getAge();
            mEditAge.setText(String.valueOf(age));
        }
    }

    private InputFilter typeFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern p = Pattern.compile("[a-zA-Z|\u4e00-\u9fa5]+");
            Matcher m = p.matcher(source.toString());
            if (!m.matches()) return "";
            return null;
        }
    };

}
