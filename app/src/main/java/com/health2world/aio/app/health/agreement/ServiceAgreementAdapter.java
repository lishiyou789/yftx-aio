package com.health2world.aio.app.health.agreement;

import android.support.annotation.Nullable;

import com.health2world.aio.R;
import com.health2world.aio.bean.AgreementBean;

import java.text.SimpleDateFormat;
import java.util.List;

import aio.health2world.brvah.BaseQuickAdapter;
import aio.health2world.brvah.BaseViewHolder;

/**
 * @author Administrator
 * @date 2018/7/31/0031.
 */

public class ServiceAgreementAdapter extends BaseQuickAdapter<AgreementBean, BaseViewHolder> {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public ServiceAgreementAdapter(List<AgreementBean> data) {
        super(R.layout.item_agreement_list, data);
    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    @Override
    protected void convert(BaseViewHolder helper, AgreementBean item) {

        helper.setText(R.id.tvTime, format.format(item.getStartTime()));
        String name = item.getName();
        if (name.contains("解约协议")) {
            helper.setText(R.id.tvTag1, "解");
        } else {
            helper.setText(R.id.tvTag1, "签");
        }
        if (item.isCheck()) {
            helper.setBackgroundColor(R.id.item_agreement, mContext.getResources().getColor(R.color.blue_light));
        } else {
            helper.setBackgroundColor(R.id.item_agreement, mContext.getResources().getColor(R.color.white));
        }

    }

}