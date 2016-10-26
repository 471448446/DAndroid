package better.hello.common;

/**
 * Created by better on 2016/10/18.
 * 列表的一些常用事件
 */

public interface RefreshListener {
    void onRetryListEmptyView();
    void onRetryFootEmptyView();
    void onRefreshList();
    void onBottomList();
}
