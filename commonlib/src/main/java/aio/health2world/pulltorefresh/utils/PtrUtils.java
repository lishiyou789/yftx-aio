package aio.health2world.pulltorefresh.utils;

import android.content.Context;

import aio.health2world.library.R;
import aio.health2world.pulltorefresh.PtrClassicFrameLayout;
import aio.health2world.pulltorefresh.PtrFrameLayout;
import aio.health2world.pulltorefresh.header.MaterialHeader;
import aio.health2world.pulltorefresh.header.StoreHouseHeader;

/**
 * Created by lishiyou on 2016/11/14.
 */

public class PtrUtils {
    /**
     * 设置MaterialDesign风格
     */
    public static void setMaterialDesign(Context context, PtrClassicFrameLayout mPtrFrameLayout) {
        MaterialHeader header = new MaterialHeader(context);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setColorSchemeColors(new int[]{context.getResources().getColor(R.color.appThemeColor)});
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0,
                PtrLocalDisplay.dp2px(10));
        header.setPtrFrameLayout(mPtrFrameLayout);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);

        // 下拉刷新支持时间
        mPtrFrameLayout.setLastUpdateTimeRelateObject(false);
        // 设置下拉过程中，content的内容布局保持不动
        mPtrFrameLayout.setPinContent(true);
        // 下拉刷新一些设置 详情参考文档
        mPtrFrameLayout.setResistance(1.7f);
        mPtrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrameLayout.setDurationToClose(200);
        mPtrFrameLayout.setDurationToCloseHeader(800);
        // default is false
        mPtrFrameLayout.setPullToRefresh(false);
        // default is true
        mPtrFrameLayout.setKeepHeaderWhenRefresh(true);
        mPtrFrameLayout.setAutoLoadMoreEnable(true);
    }

    /**
     * 设置StoreHouse风格
     **/

    public static void setStoreHouse(Context context, PtrClassicFrameLayout mPtrFrameLayout) {
        StoreHouseHeader header = new StoreHouseHeader(context);
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0,
                PtrLocalDisplay.dp2px(10));
        header.initWithString("LSY");
        header.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);

        // 下拉刷新支持时间
        mPtrFrameLayout.setLastUpdateTimeRelateObject(false);
        // 设置下拉过程中，content的内容布局保持不动
        mPtrFrameLayout.setPinContent(false);
        // default is false
        mPtrFrameLayout.setPullToRefresh(false);
        // default is true
        mPtrFrameLayout.setKeepHeaderWhenRefresh(true);
        mPtrFrameLayout.setAutoLoadMoreEnable(false);
    }

}
