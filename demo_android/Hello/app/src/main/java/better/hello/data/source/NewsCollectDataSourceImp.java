package better.hello.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import better.hello.App;
import better.hello.R;
import better.hello.common.BaseSchedulerTransformer;
import better.hello.common.BaseSubscriber;
import better.hello.data.DataSourceDbImpl;
import better.hello.data.bean.NewsListBean;
import better.hello.data.db.TableInfo;
import better.hello.http.call.RequestInfo;
import better.hello.util.C;
import better.hello.util.JsonUtils;
import better.lib.http.RequestType;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Des 收藏列表
 * Create By better on 2016/12/26 17:11.
 */
public class NewsCollectDataSourceImp extends DataSourceDbImpl<List<NewsListBean>> implements NewsCollect {
    private Func1<Cursor, NewsListBean> mapper = new Func1<Cursor, NewsListBean>() {
        @Override
        public NewsListBean call(Cursor cursor) {
            return JsonUtils.fromJson(cursor.getString(cursor.getColumnIndexOrThrow(TableInfo.NewsCollectTable.JSON)), NewsListBean.class);
        }
    };

    /**
     * Des
     * Create By better on 2016/10/27 09:45.
     */
    public NewsCollectDataSourceImp(Context context) {
        super(context);
    }

    @Override
    public Subscription asyncUrlInfo(RequestInfo<List<NewsListBean>> info) {
        return getLocalInfo(info);
    }

    @Override
    public Subscription getLocalInfo(final RequestInfo<List<NewsListBean>> info) {
        return db.createQuery(TableInfo.NewsCollectTable.TABLE_NAME, TableInfo.getCollects((Integer) info.getPrams().get(C.EXTRA_FIRST))).mapToList(mapper).doOnNext(new Action1<List<NewsListBean>>() {
            @Override
            public void call(List<NewsListBean> list) {
                if (null == list || list.isEmpty()) {
                    isLocalEmpty = true;
                    if (info.getRequestTye() != RequestType.DATA_REQUEST_UP_REFRESH) {
                        throw new IllegalArgumentException(App.getApplication().getString(R.string.str_no_collect));
                    } else {
                        throw new IllegalArgumentException(App.getApplication().getString(R.string.str_loading_header_all));
                    }
                } else {
                    Iterator iterable = list.iterator();
                    while (iterable.hasNext()) {
                        NewsListBean bean = (NewsListBean) iterable.next();
                        if (null == bean || TextUtils.isEmpty(bean.getTitle())) {
                            iterable.remove();
                        }
                    }
                }
                Collections.reverse(list);
            }
        }).compose(new BaseSchedulerTransformer<List<NewsListBean>>()).subscribe(new BaseSubscriber<>(info));
    }

    @Override
    public void save(List<NewsListBean> beans, RequestInfo<List<NewsListBean>> info) {
        if (null == beans || beans.isEmpty()) return;
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (NewsListBean bean : beans) {
                ContentValues values = new ContentValues();
                values.put(TableInfo.NewsListTable.TITLE, bean.getTitle());
                values.put(TableInfo.NewsListTable.TYPE_ID, "");
                values.put(TableInfo.NewsListTable.JSON, JsonUtils.toJson(bean));
                db.insert(TableInfo.NewsCollectTable.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void delete(String key) {
        db.delete(TableInfo.NewsCollectTable.TABLE_NAME, TableInfo.NewsCollectTable.TITLE + " = '" + key + "'");
    }

    @Override
    public void collect(NewsListBean bean) {
        ArrayList<NewsListBean> listBeen = new ArrayList<>();
        listBeen.add(bean);
        save(listBeen, null);
    }

    public Observable<List<NewsListBean>> getOne(String key) {
        /* mapToOne 容易造成异常 --better 2017/1/9 16:34. */
        return db.createQuery(TableInfo.NewsCollectTable.TABLE_NAME, TableInfo.getCollect(key)).mapToList(mapper).compose(new BaseSchedulerTransformer<List<NewsListBean>>());
    }
}
