package aio.health2world.pulltorefresh.loadmore;

import android.view.View;
import android.view.View.OnClickListener;

import aio.health2world.pulltorefresh.loadmore.ILoadMoreViewFactory.ILoadMoreView;

public interface LoadMoreHandler {

    /**
     * @param contentView
     * @param loadMoreView
     * @param onClickLoadMoreListener
     * @return 是否有 init ILoadMoreView
     */
    boolean handleSetAdapter(View contentView, ILoadMoreView loadMoreView, OnClickListener
            onClickLoadMoreListener);

    void setOnScrollBottomListener(View contentView, OnScrollBottomListener onScrollBottomListener);

    void removeFooter();

    void addFooter();
}
