package com.health2world.aio.app.resident.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.health2world.aio.R;
import com.konsung.bean.ResidentBean;

import java.util.ArrayList;
import java.util.List;

import aio.health2world.DataEntity;
import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;
import aio.health2world.utils.ToastUtil;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class RelationSelectDialog extends Dialog implements BaseQuickAdapter.OnItemClickListener {

    private TextView tvTitle, tvOk;
    private RecyclerView mRvSelectRelation;
    private ResidentBean mResidentBean;
    private RelationSelectAdapter selectAdapter;
    private List<DataEntity> entityList;
    private DataEntity entity;
    private OnRelationSelectListener listener;

    public RelationSelectDialog(@NonNull Context context) {
        super(context, R.style.familyCreateDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_family_relation_sreach);
        initView();
    }


    public void setFirstResident(ResidentBean resident) {
        this.mResidentBean = resident;
    }

    private void initView() {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        mRvSelectRelation = findViewById(R.id.rv_select_relation);
        mRvSelectRelation.setLayoutManager(new GridLayoutManager(getContext(), 3));
        tvTitle = findViewById(R.id.tv_title);
        tvOk = findViewById(R.id.tvOk);
        setCanceledOnTouchOutside(true);
        tvTitle.setText(String.format(getContext().getResources().getString(R.string.select_relation_str),
                mResidentBean.getName()));
        entityList = new ArrayList<>();
        selectAdapter = new RelationSelectAdapter(entityList);
        mRvSelectRelation.setAdapter(selectAdapter);

        String[] relationArray = getContext().getResources().getStringArray(R.array.family_relation);
        for (String s : relationArray) {
            entityList.add(new DataEntity(0, s));
        }
        selectAdapter.notifyDataSetChanged();

        selectAdapter.setOnItemClickListener(this);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity == null) {
                    ToastUtil.showShort("请选择关系");
                    return;
                }
                if (listener != null)
                    listener.relationSelect(entity);
            }
        });

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DataEntity entity = (DataEntity) adapter.getItem(position);
        if (this.entity != null)
            this.entity.setChecked(false);
        entity.setChecked(true);
        this.entity = entity;
        selectAdapter.notifyDataSetChanged();

    }

    public void resetAllSelect() {
        for (DataEntity entity : entityList) {
            if (entity.isChecked())
                entity.setChecked(false);
        }
        selectAdapter.notifyDataSetChanged();
    }

    private class RelationSelectAdapter extends BaseQuickAdapter<DataEntity, BaseViewHolder> {

        public RelationSelectAdapter(@Nullable List<DataEntity> data) {
            super(R.layout.item_measure_setting, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DataEntity item) {

            ((TextView) helper.getView(R.id.tvItemName)).setText(item.getText());

            if (item.isChecked())
                ((CheckBox) helper.getView(R.id.checkBox)).setChecked(true);
            else
                ((CheckBox) helper.getView(R.id.checkBox)).setChecked(false);
        }
    }

    public interface OnRelationSelectListener {
        void relationSelect(DataEntity dataEntity);
    }

    public void setOnRelationSelectListener(OnRelationSelectListener listener) {
        this.listener = listener;
    }
}
