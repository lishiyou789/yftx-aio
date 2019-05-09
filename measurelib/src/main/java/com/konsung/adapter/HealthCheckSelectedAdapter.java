package com.konsung.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.konsung.R;
import com.konsung.bean.AppBean;

import java.util.List;


/**
 * this file is built by utf-8
 * 健康测量选项卡适配器
 * 设置测量项目的三种状态
 * Created by JustRush on 2015/9/1.
 */
public class HealthCheckSelectedAdapter extends BaseAdapter {
    private List<AppBean> list; //显示数据的bean
    private Context context; //上下文

    /**
     * 构造函数
     *
     * @param context  上下文
     * @param appBeens item的bean
     */
    public HealthCheckSelectedAdapter(Context context, List<AppBean> appBeens) {
        super();
        this.context = context;

        this.list = appBeens;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.mapp_list_item, null);
            mViewHolder.ivPicture = (ImageView) convertView.findViewById(R.id.iv_picture);
            mViewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        AppBean appBean = list.get(position);
        mViewHolder.ivPicture.setImageResource(appBean.getPicture());
        mViewHolder.tvName.setText(appBean.getName());
        int backColor = R.color.no_click; //当前背景颜色
        //根据是否点击背景给不同的颜色
        if (appBean.isClick()) {
            backColor = R.color.click;
            mViewHolder.ivPicture.setImageResource(appBean.getPictureSel());
        }
        convertView.setBackgroundColor(context.getResources().getColor(backColor));
        return convertView;
    }

    /**
     * 显示的控制器
     */
    public class ViewHolder {
        TextView tvName;
        ImageView ivPicture;
    }
}
