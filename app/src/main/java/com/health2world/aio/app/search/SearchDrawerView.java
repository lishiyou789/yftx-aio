package com.health2world.aio.app.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.health2world.aio.R;
import com.health2world.aio.app.adapter.ResidentTagListAdapter;
import com.health2world.aio.bean.TagInfo;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by lishiyou on 2018/7/23 0023.
 */

public class SearchDrawerView extends FrameLayout implements View.OnClickListener {

    private Context mContext;
    private Button btnReset, btnOk;
    /***筛选条件的条件项布局*/
    private TagFlowLayout flowLayout0, flowLayout1, flowLayout2, flowLayout3, flowLayout4;
    //状态 类型 标签 家医服务包 个性服务包
    private List<TagInfo> tagList0 = new ArrayList<>(), tagList1 = new ArrayList<>(),
            tagList2 = new ArrayList<>(), tagList3 = new ArrayList<>(), tagList4 = new ArrayList<>();

    private ResidentTagListAdapter tagAdapter0, tagAdapter1, tagAdapter2, tagAdapter3, tagAdapter4;

    private FilterCallBack callBack;

    public SearchDrawerView(@NonNull Context context) {
        this(context, null);
    }

    public SearchDrawerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initData();
        initListener();
    }

    private void initView() {
        View.inflate(mContext, R.layout.layout_filter_drawlayout, this);
        btnReset = findViewById(R.id.btnReset);
        btnOk = findViewById(R.id.btnOk);
        flowLayout0 = findViewById(R.id.flowLayout0);
        flowLayout1 = findViewById(R.id.flowLayout1);
        flowLayout2 = findViewById(R.id.flowLayout2);
        flowLayout3 = findViewById(R.id.flowLayout3);
        flowLayout4 = findViewById(R.id.flowLayout4);
    }

    private void initData() {
        tagList0.add(new TagInfo(1, "已签约"));
        tagList0.add(new TagInfo(0, "未签约"));
        tagList1.add(new TagInfo(1, "已注册"));
        tagList1.add(new TagInfo(0, "未注册"));

        tagAdapter0 = new ResidentTagListAdapter(mContext, tagList0);
        flowLayout0.setAdapter(tagAdapter0);
        tagAdapter1 = new ResidentTagListAdapter(mContext, tagList1);
        flowLayout1.setAdapter(tagAdapter1);
        tagAdapter2 = new ResidentTagListAdapter(mContext, tagList2);
        flowLayout2.setAdapter(tagAdapter2);
        tagAdapter3 = new ResidentTagListAdapter(mContext, tagList3);
        flowLayout3.setAdapter(tagAdapter3);
        tagAdapter4 = new ResidentTagListAdapter(mContext, tagList4);
        flowLayout4.setAdapter(tagAdapter4);
    }

    private void initListener() {
        btnReset.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    public void init(List<TagInfo> tagList, List<TagInfo> listP1, List<TagInfo> listP2) {
        if (tagList != null) {
            tagList2.addAll(tagList);
            tagAdapter2.notifyDataChanged();
        }
        if (listP1 != null) {
            tagList3.addAll(listP1);
            tagAdapter3.notifyDataChanged();
        }
        if (listP2 != null) {
            tagList4.addAll(listP2);
            tagAdapter4.notifyDataChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReset:
                resetSearchFilter();
                break;
            case R.id.btnOk:
                if (callBack != null) {
                    callBack.filterData(getSearchFilter());
                }
                break;
        }
    }

    /**
     * 所有条件重置
     */
    private void resetSearchFilter() {
        flowLayout0.getSelectedList().clear();
        flowLayout1.getSelectedList().clear();
        flowLayout2.getSelectedList().clear();
        flowLayout3.getSelectedList().clear();
        flowLayout4.getSelectedList().clear();
        tagAdapter0.notifyDataChanged();
        tagAdapter1.notifyDataChanged();
        tagAdapter2.notifyDataChanged();
        tagAdapter3.notifyDataChanged();
        tagAdapter4.notifyDataChanged();
    }

    //获取刷选条件
    private HashMap<String, Object> getSearchFilter() {
        HashMap<String, Object> map = new HashMap<>();
        //是否签约
        Set<Integer> set0 = flowLayout0.getSelectedList();
        if (set0 != null & set0.size() > 0) {
            for (Integer i : set0) {
                map.put("signType", tagList0.get(i).getTagId()+"");
            }
        }
        //是否注册
        Set<Integer> set1 = flowLayout1.getSelectedList();
        if (set1 != null && set1.size() > 0) {
            for (Integer i : set1) {
                map.put("isRegister", tagList1.get(i).getTagId()+"");
            }
        }

        //取选中的标签
        Set<Integer> set2 = flowLayout2.getSelectedList();
        if (set2 != null && set2.size() > 0) {
            String tagId = "";
            for (Integer i : set2) {
                tagId += tagList2.get(i).getTagId() + ",";
            }
            tagId = tagId.substring(0, tagId.length() - 1);
            map.put("tagId", tagId);
        }
        //取选中的服务包(家医服务包和个性化服务包)
        String serviceId = "";
        //家医服务包
        Set<Integer> set3 = flowLayout3.getSelectedList();
        if (set3 != null && set3.size() > 0) {
            for (Integer i : set3) {
                serviceId += tagList3.get(i).getTagId() + ",";
            }
        }
        //个性化服务包
        Set<Integer> set4 = flowLayout4.getSelectedList();
        if (set4 != null && set4.size() > 0) {
            for (Integer i : set4) {
                serviceId += tagList4.get(i).getTagId() + ",";
            }
        }
        if (!TextUtils.isEmpty(serviceId)) {
            serviceId = serviceId.substring(0, serviceId.length() - 1);
            map.put("serviceId", serviceId);
        }
        return map;
    }

    interface FilterCallBack {
        void filterData(HashMap<String, Object> map);
    }

    public void setFilterCallBack(FilterCallBack callBack) {
        this.callBack = callBack;
    }
}
