package com.konsung.presenter;

import android.app.FragmentManager;
import android.content.Context;
import android.widget.BaseAdapter;

import com.konsung.bean.AppBean;
import com.konsung.bean.MeasureBean;
import com.konsung.listen.MeasureCompleteListen;

import java.util.List;

/**
 * 健康测量界面逻辑界面接口
 */

public class AppPresenter {
    /**
     * 软件设置界面数据显示
     */
    public interface View {

    }

    /**
     * 逻辑处理接口
     */
    public interface Presenter {
        /**
         * 获取健康测量itme数据
         *
         * @param context 上下文
         * @return item数据集合
         */
        List<AppBean> getItemBean(Context context);

        /**
         * 获取listView的adapter的方法
         * * @param context 上下文
         *
         * @return
         */
        BaseAdapter getItemAdapter(Context context);

        /**
         * 点击条目发生的事件
         *
         * @param position            索引
         * @param fragmentTransaction Fragment管理器
         */
        void clickItem(int position, FragmentManager fragmentTransaction);

        /**
         * 设置当前测量类
         *
         * @param bean 测量类
         */
        void setMeasure(MeasureBean bean);

        /**
         * 设置测量完成的监听类
         *
         * @param l 监听类
         */
        void setMeasureListen(MeasureCompleteListen l);
    }
}
