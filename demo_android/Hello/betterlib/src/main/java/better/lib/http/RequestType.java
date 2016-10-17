package better.lib.http;

/**
 * Created by Better on 2016/3/15.
 * list 的请求类型
 */
public enum RequestType {
    DATA_REQUEST_INIT, // 初始化（初始化创建了列表，与tail类似。主要区分加载失败处理）
    DATA_REQUEST_DOWN_REFRESH, // 下拉刷新(当前处理：重新加载)
    DATA_REQUEST_UP_REFRESH // 上拉加载更多
}
