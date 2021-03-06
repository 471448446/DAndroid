package better.hello.data;

import android.content.Context;

import better.hello.http.call.RequestInfo;
import better.lib.http.RequestType;
import rx.Subscription;

/**
 * Des 列表数据，需要缓冲在本地db中
 * http://blog.chengyunfeng.com/?p=991
 * Create By better on 2016/10/27 09:37.
 */
public abstract class DataSourceAsyncRemoteLocal<T> extends DataSource implements DataSourceAsync<T> {
    //avoid CallBack Hell 因为本地加载和网络加载用的一个回调
    protected boolean isLocalEmpty = false;
//    protected boolean isLoadFromNetSuccess = false;

    /**
     * Des
     * Create By better on 2016/10/27 09:45.
     */
    public DataSourceAsyncRemoteLocal(Context context) {
        super(context);
    }

    @Override
    public Subscription get(RequestInfo<T> info) {
        if (info.getRequestTye() == RequestType.DATA_REQUEST_INIT) {
            return getLocalInfo(info);
        } else {
            return asyncUrlInfo(info);
        }
    }

//    public boolean isNeedCache(RequestType type) {
//        return type != RequestType.DATA_REQUEST_UP_REFRESH && isLoadFromNetSuccess;
//    }

    /**
     * Des 只能用一次
     * Create By better on 2016/10/27 15:59.
     */
    public boolean isNeedLoadFromNet() {
        if (isLocalEmpty) {
            isLocalEmpty = false;
            return true;
        }
        return isLocalEmpty;
    }
}
