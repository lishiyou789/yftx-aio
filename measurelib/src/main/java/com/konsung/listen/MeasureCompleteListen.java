package com.konsung.listen;

import com.konsung.bean.MeasureBean;
import com.konsung.bean.StateBean;

/**
 * 测量完成的监听
 */

public interface MeasureCompleteListen {
    /**
     * 测量完成的时的完成
     *
     * @param param 测量的选项
     * @param bean  测量的bean
     */
    void onComplete(Measure param, MeasureBean bean);

    /**
     * 测量失败的回调
     *
     * @param param 测量的选项
     * @param mag   失败的原因
     */
    void onFail(Measure param, String mag);

    /**
     * 测量时血压的袖带压
     *
     * @param va
     */
    void NibpCuff(int va);

    /**
     * 状态监听的值
     *
     * @param bean
     */
    void onState(StateBean bean);
}
