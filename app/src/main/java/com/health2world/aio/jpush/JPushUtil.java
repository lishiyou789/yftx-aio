package com.health2world.aio.jpush;

import com.health2world.aio.MyApplication;

/**
 * Created by Administrator on 2018/7/4 0004.
 */

public class JPushUtil {
    //设置别名
    public static void setJPushAlias(int action, String alias) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = action;
        tagAliasBean.alias = alias;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(MyApplication.getInstance(), TagAliasOperatorHelper.sequence, tagAliasBean);
    }
}
