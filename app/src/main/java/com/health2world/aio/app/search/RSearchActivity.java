package com.health2world.aio.app.search;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.app.resident.add.NewResidentActivity;
import com.health2world.aio.app.resident.view.ResidentQRCodeDialog;
import com.health2world.aio.bean.HistoryAccount;
import com.health2world.aio.bean.TagInfo;
import com.health2world.aio.common.mvp.MVPBaseActivity;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.util.ActivityUtil;
import com.health2world.aio.util.DefaultTextWatcher;
import com.j256.ormlite.support.DatabaseConnection;
import com.konsung.bean.ResidentBean;
import com.konsung.listen.IdCardListen;
import com.konsung.util.MeasureUtils;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.utils.MatchUtil;
import aio.health2world.utils.ToastUtil;

import static com.health2world.aio.app.home.MainActivity.KEY_CODE_URL;
import static com.health2world.aio.app.home.MainActivity.KEY_RESIDENT;
import static com.health2world.aio.app.home.MainActivity.REQUEST_CODE_ADD_RESIDENT;
import static com.health2world.aio.config.AppConfig.PAGE_SIZE;

/**
 * 居民搜索
 * Created by lishiyou on 2018/7/16 0016.
 */

public class RSearchActivity extends MVPBaseActivity<RSearchContract.Presenter> implements RSearchContract.View,
        SearchDrawerView.FilterCallBack, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener
        , BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener, SearchHistoryView.OnSelectListener, IdCardListen, DrawerLayout.DrawerListener {

    private ImageView ivBack, ivClear;
    private EditText edInput;
    private TextView tvSearch, tvFilter, tvLeftTitle, tvAddResident;
    private Button btnLastPage, btnNextPage;
    private SearchDrawerView drawerView;
    private DrawerLayout drawerLayout;
    private LinearLayout layoutTitle;

    private RecyclerView recyclerView;
    private FrameLayout btnFrame;
    private ResidentListAdapter listAdapter;
    private List<ResidentBean> residentList;
    private SwipeRefreshLayout refreshLayout;

    private SearchHistoryView historyView;
    //二维码弹出框
    private ResidentQRCodeDialog qrCodeDialog;

    private int pageIndex = 1;
    private HashMap<String, Object> filterMap;
    //0 添加家人  1 选择居民
    private int flag = 1;

    @Override
    protected RSearchContract.Presenter getPresenter() {
        return new RSearchPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_resident_search;
    }

    @Override
    protected void initView() {
        ivBack = findView(R.id.ivBack);
        ivClear = findView(R.id.ivClear);
        edInput = findView(R.id.edInput);
        tvLeftTitle = findView(R.id.tvLeftTitle);
        tvSearch = findView(R.id.tvSearch);
        tvFilter = findView(R.id.tvFilter);
        drawerView = findView(R.id.drawerView);
        layoutTitle = findView(R.id.layout_title);
        tvAddResident = findView(R.id.tvNewResident);
        btnLastPage = findView(R.id.btn_last_page);
        btnNextPage = findView(R.id.btn_next_page);

        drawerLayout = findView(R.id.drawer_layout);
        drawerLayout.setAlpha(0.8f);
        btnFrame = findView(R.id.layout_page);

        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout = findView(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    @Override
    protected void initData() {

        if (getIntent().hasExtra("flag")) {
            flag = getIntent().getIntExtra("flag", 0);
        }
        setResult(RESULT_CANCELED);

        edInput.setSingleLine(true);
        edInput.setFilters(new InputFilter[]{typeFilter, new InputFilter.LengthFilter(20)});

        residentList = new ArrayList<>();
        listAdapter = new ResidentListAdapter(residentList);
        recyclerView.setAdapter(listAdapter);

        historyView = new SearchHistoryView(this);
        listAdapter.bindToRecyclerView(recyclerView);
        listAdapter.setEmptyView(historyView);

        mPresenter.screeningLabel();


    }

    @Override
    protected void initListener() {
        MeasureUtils.setIdCardListen(this);
        ivBack.setOnClickListener(this);
        ivClear.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        tvFilter.setOnClickListener(this);
        tvLeftTitle.setOnClickListener(this);
        drawerView.setFilterCallBack(this);
        refreshLayout.setOnRefreshListener(this);
        listAdapter.setOnLoadMoreListener(this, recyclerView);
        listAdapter.setOnItemClickListener(this);
        listAdapter.setOnItemChildClickListener(this);
        historyView.setOnSelectListener(this);
        tvAddResident.setOnClickListener(this);
        btnLastPage.setOnClickListener(this);
        btnNextPage.setOnClickListener(this);
        edInput.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    ivClear.setVisibility(View.VISIBLE);
                else
                    ivClear.setVisibility(View.INVISIBLE);
            }
        });

        edInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onRefresh();
                    return true;
                }
                return false;
            }
        });
        edInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(drawerView)) {
                    drawerLayout.closeDrawer(drawerView);
                }
            }
        });

        drawerLayout.addDrawerListener(this);

//        tvSearch.performClick();
    }

    @Override
    public void onSelect(String keyWord) {
        edInput.setText(keyWord);
        edInput.setSelection(keyWord.length());
        onRefresh();

    }

    @Override
    public void screeningLabelSuccess(List<TagInfo> listTag, List<TagInfo> listP1, List<TagInfo> listP2) {
        drawerView.init(listTag, listP1, listP2);
    }

    @Override
    public void filterData(HashMap<String, Object> map) {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawers();
        }
        this.filterMap = map;
        onRefresh();
    }

    @Override
    public void onListen(String name, String idCard, int sex, int type, String birthday, String picture, String address) {
        edInput.setText(idCard);
        edInput.setSelection(idCard.length());
        tvSearch.performClick();
    }

    @Override
    public void loadResidentSuccess(List<ResidentBean> list) {
        refreshLayout.setRefreshing(false);
        if (pageIndex == 1)
            residentList.clear();
        residentList.addAll(list);
        listAdapter.notifyDataSetChanged();
        if (list.size() >= PAGE_SIZE) {
            listAdapter.loadMoreComplete();
        } else {
            listAdapter.loadMoreEnd();
        }
        if (list.size() == 0) {
            layoutTitle.setVisibility(View.GONE);
            btnFrame.setVisibility(View.GONE);

            historyView.refresh();
        } else {
            layoutTitle.setVisibility(View.VISIBLE);
            btnFrame.setVisibility(View.VISIBLE);
        }
        saveFamilyMember(list);
    }

    @Override
    public void loadResidentFailed(Throwable throwable) {
        refreshLayout.setRefreshing(false);
        if (pageIndex > 1) {
            pageIndex--;
            listAdapter.loadMoreFail();
        }
    }

    @Override
    public void onRefresh() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
        pageIndex = 1;
        refreshLayout.setRefreshing(true);
        //请求数据
        mPresenter.residentQuery(pageIndex, edInput.getText().toString().trim(), filterMap);
        //保存历史搜索记录
        if (!TextUtils.isEmpty(edInput.getText().toString().trim())) {
            HistoryAccount historyAccount = new HistoryAccount(edInput.getText().toString().trim(), System.currentTimeMillis());
            try {
                DBManager.getInstance().getHistoryDao().createOrUpdate(historyAccount);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoadMoreRequested() {
        pageIndex++;
        mPresenter.residentQuery(pageIndex, edInput.getText().toString().trim(), filterMap);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        onItemChildClick(adapter, view, position);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ResidentBean resident = (ResidentBean) adapter.getItem(position);
        //如果是设置居民，设置为全局居民
        if (flag == 1)
            MyApplication.getInstance().setResident(resident);
        if (resident.getDoctor() == 1) {//区域内
            Intent intent = new Intent();
            intent.putExtra(KEY_RESIDENT, resident);
            setResult(RESULT_OK, intent);
            finish();
        }
        if (resident.getDoctor() == 0) {//全平台居民 (先添加到我管辖之内 再返回该居民)
            mPresenter.doctorAddPatientInfo(resident);
        }
    }

    @Override
    public void addInfoSuccess(ResidentBean resident) {
        Intent intent = new Intent();
        intent.putExtra(KEY_RESIDENT, resident);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ivBack:
            case R.id.tvLeftTitle:
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    ActivityUtil.finishActivity(this);
                }
                break;
            case R.id.tvFilter://筛选
                ActivityUtil.closeKeyboard(this);
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;
            case R.id.tvSearch://搜索
                ActivityUtil.closeKeyboard(this);

                if (!TextUtils.isEmpty(edInput.getText().toString().trim()) &&
                        MatchUtil.containSpace(edInput.getText().toString().trim())) {
                    ToastUtil.showShort("不能包含空格");
                    return;
                }
                if (MatchUtil.isSpecialChar(edInput.getText().toString().trim())) {
                    ToastUtil.showShort("不能含有特殊字符");
                    return;
                }
                filterMap = null;
                onRefresh();
                break;
            case R.id.ivClear:
                edInput.getText().clear();
                break;

            case R.id.tvNewResident://新增居民
                startActivityForResult(new Intent(this, NewResidentActivity.class), REQUEST_CODE_ADD_RESIDENT);
                break;

            case R.id.btn_last_page://上一页
                recyclerView.scrollBy(0, (int) getResources().getDimension(R.dimen.dp__50) * 8);
                break;

            case R.id.btn_next_page://下一页
                recyclerView.scrollBy(0, 400);
                break;


            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //添加居民成功
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_ADD_RESIDENT) {
            tvSearch.performClick();
            ResidentBean _residentBean = (ResidentBean) data.getSerializableExtra(KEY_RESIDENT);
            String codeUrl = data.getStringExtra(KEY_CODE_URL);

            if (!TextUtils.isEmpty(codeUrl)) {
                showQRCodeDialog(_residentBean, codeUrl);
            }
        }
    }

    //居民添加成功弹出二维码
    private void showQRCodeDialog(ResidentBean pResident, String url) {
        qrCodeDialog = new ResidentQRCodeDialog(this);
        qrCodeDialog.setResidentQrUrl(url);
        qrCodeDialog.setResidentCode(pResident.getResidentCode());
        qrCodeDialog.setType(0);
        qrCodeDialog.show();
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            finish();
        }
    }

    private InputFilter typeFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern p = Pattern.compile("[a-zA-Z0-9|\u4e00-\u9fa5]+");
            Matcher m = p.matcher(source.toString());
            if (!m.matches()) return "";
            return null;
        }
    };

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        btnFrame.setVisibility(View.GONE);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (residentList.size() > 0)
            btnFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }
}
