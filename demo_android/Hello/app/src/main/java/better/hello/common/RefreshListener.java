package better.hello.common;

/**
 * Created by better on 2016/10/18.
 * 列表的一些常用事件
 */

public interface RefreshListener {
    /*重试 RecyclerView */
    void onRetryListEmptyView();

    void onRetryFootEmptyView();

    /*下拉刷新 RecyclerView*/
    void onRefreshList();

    /*上拉刷新 RecyclerView*/
    void onBottomList();
}
