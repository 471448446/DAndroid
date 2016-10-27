package better.hello.data;

import android.content.Context;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import better.hello.data.db.DbHelper;
import better.hello.http.call.RequestInfo;
import better.lib.http.RequestType;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Des 列表数据，需要缓冲在本地db中
 * Create By better on 2016/10/27 09:37.
 */
public abstract class DataSourceDbImpl<T> implements DataSource<T> {
    protected BriteDatabase db;
    //avoid CallBack Hell 因为本地加载和网络加载用的一个回调
    protected boolean isLocalEmpty = false;
    protected boolean isLoadFromNetSuccess = false;

    /**
     * Des
     * Create By better on 2016/10/27 09:45.
     */
    public DataSourceDbImpl(Context context) {
        SqlBrite sqlBrite = SqlBrite.create();
        db = sqlBrite.wrapDatabaseHelper(new DbHelper(context), Schedulers.io());
    }

    @Override
    public Subscription get(RequestInfo<T> info) {
        if (info.getRequestTye() == RequestType.DATA_REQUEST_INIT) {
            return getLocalInfo(info);
        } else {
            return asyncUrlInfo(info);
        }
    }

    public boolean isNeedCache(RequestType type) {
        return type != RequestType.DATA_REQUEST_UP_REFRESH && isLoadFromNetSuccess;
    }

    /**
     * Des 只能用一次
     * Create By better on 2016/10/27 15:59.
     */
    public boolean isNeedLoadFromNet() {
        if (isLocalEmpty) {
            isLocalEmpty = false;
            return false;
        }
        return isLocalEmpty;
    }
}
