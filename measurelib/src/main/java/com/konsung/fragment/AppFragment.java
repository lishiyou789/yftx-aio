package com.konsung.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.konsung.R;
import com.konsung.bean.MeasureBean;
import com.konsung.listen.MeasureCompleteListen;
import com.konsung.presenter.impl.AppPresenterImpl;


/**
 * AppFragment 类
 * 加载康尚App
 *
 * @author ouyangfan
 * @version 0.0.1
 * @date 2015-01-30 15:32
 */

/**
 * AppFragment 类
 * 加载康尚应用的页面
 *
 * @author yuchunhui
 * @version 0.0.2
 */
public class AppFragment extends BaseFragment<AppPresenterImpl> implements AdapterView
        .OnItemClickListener {
    private MeasureBean measureBean; //记录测量数据的bean
    private ListView lvApp;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        measureBean = (MeasureBean) getArguments().getSerializable("bean");
    }

    /**
     * 构造方法
     *
     * @param bean 测量数据
     */
    public static AppFragment getInstance(MeasureBean bean) {
        AppFragment appFragment = new AppFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        appFragment.setArguments(bundle);
        return appFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_app, container, false);
        initViewData();
        initBindEvent();
        return view;
    }

    /**
     * 初始化事件的方法
     */
    private void initBindEvent() {
        lvApp.setOnItemClickListener(this);
    }

    @Override
    public AppPresenterImpl initPresenter() {
        return new AppPresenterImpl();
    }

    /**
     * 初始化界面的方法
     */
    private void initViewData() {
        lvApp = (ListView) view.findViewById(R.id.lv_app);
        lvApp.setAdapter(presenter.getItemAdapter(getActivity()));
        presenter.setMeasure(measureBean);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.clickItem(position, getFragmentManager());
    }

    /**
     * 设置测量完成的监听
     *
     * @param l 测量完成的监听类
     */
    public void setMeasureComPleteListen(MeasureCompleteListen l) {
        presenter.setMeasureListen(l);
    }
}
