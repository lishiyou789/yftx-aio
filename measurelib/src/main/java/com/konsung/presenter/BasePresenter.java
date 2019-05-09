package com.konsung.presenter;


/**
 * 所有逻辑处理类的基类
 *
 * @param <T> 订阅者的实例
 */

public class BasePresenter<T> {
    protected T lifeSubscription;

    /**
     * 设置订阅者
     *
     * @param mLifeSubscription 订阅者
     */
    public void setLifeSubscription(T mLifeSubscription) {
        this.lifeSubscription = mLifeSubscription;
    }

}
