package com.health2world.aio.common;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/4 0004.
 */

public class MsgEvent<T> implements Serializable {

    private int action;

    private T t;

    public MsgEvent(int action) {
        this.action = action;
    }

    public MsgEvent(int action, T t) {
        this.action = action;
        this.t = t;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
