package com.health2world.aio.app.clinic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.health2world.aio.R;
import com.health2world.aio.bean.MeasureItem;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.StateBean;
import com.konsung.fragment.BloodGluFragment;
import com.konsung.fragment.Ecg12Fragment;
import com.konsung.fragment.NibpFragment;
import com.konsung.fragment.Spo2Fragment;
import com.konsung.fragment.TempFragment;
import com.konsung.fragment.UrineFourttenFragment;
import com.konsung.listen.Measure;
import com.konsung.listen.MeasureCompleteListen;

/**
 * Created by lishiyou on 2017/8/3 0003.
 */

public class MeasureItemActivity extends FragmentActivity implements MeasureCompleteListen {
    private MeasureItem measureItem;
    private MeasureBean measureBean;
    private boolean success;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置取消点击外部退出  --0320  去除限制 --0325
        setFinishOnTouchOutside(true);
        setContentView(R.layout.activity_measure);

        if (getIntent().hasExtra("measureBean")) {
            measureBean = (MeasureBean) getIntent().getSerializableExtra("measureBean");
        }
        if (getIntent().hasExtra("measureItem")) {
            measureItem = (MeasureItem) getIntent().getSerializableExtra("measureItem");
        }
        if (measureItem.getType().equals(Measure.NIBP)) {
            NibpFragment nibpFragment = NibpFragment.getInstance(measureBean);
            getFragmentManager().beginTransaction().replace(R.id.container, nibpFragment, "nibp")
                    .commit();
            nibpFragment.setMeasureListen(this);
        }
        if (measureItem.getType().equals(Measure.SPO2)) {
            Spo2Fragment spo2Fragment = Spo2Fragment.getInstance(measureBean);
            getFragmentManager().beginTransaction().replace(R.id.container, spo2Fragment, "spo2")
                    .commit();
            spo2Fragment.setMeasureListen(this);
        }
        if (measureItem.getType().equals(Measure.ECG) ) {
            Ecg12Fragment ecg12Fragment = Ecg12Fragment.getInstance(measureBean);
            getFragmentManager().beginTransaction().replace(R.id.container, ecg12Fragment, "ecg12")
                    .commit();
            ecg12Fragment.setMeasureListen(this);
        }
        if (measureItem.getType().equals(Measure.URINE)) {
            UrineFourttenFragment urineFourttenFragment = UrineFourttenFragment.getInstance(measureBean);
            getFragmentManager().beginTransaction().replace(R.id.container, urineFourttenFragment, "urineFourtten")
                    .commit();
            urineFourttenFragment.setMeasureListen(this);
        }
        if (measureItem.getType().equals(Measure.TEMP)) {
            TempFragment tempFragment = TempFragment.getInstance(measureBean);
            getFragmentManager().beginTransaction().replace(R.id.container, tempFragment, "temp")
                    .commit();
            tempFragment.setMeasureListen(this);
        }
        if (measureItem.getType().equals(Measure.GLU)) {
            BloodGluFragment bloodGluFragment = BloodGluFragment.getInstance(measureBean);
            getFragmentManager().beginTransaction().replace(R.id.container, bloodGluFragment, "bloodGlu")
                    .commit();
            bloodGluFragment.setMeasureListen(this);
        }

    }

    @Override
    public void onComplete(Measure measure, MeasureBean bean) {
        success = true;
        measureBean = bean;
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onFail(Measure param, String mag) {
        success = false;
    }

    @Override
    public void NibpCuff(int va) {

    }


    @Override
    public void onState(StateBean bean) {
    }

    //防止内存泄漏  --0320
    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                Intent intent = new Intent();
                intent.putExtra("measureBean", measureBean);
                intent.putExtra("type", measureItem.getType());
                intent.putExtra("success", success);
                setResult(Activity.RESULT_OK, intent);
                MeasureItemActivity.this.finish();
            }
            return true;
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
