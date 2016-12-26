package better.hello.ui.aboutme.collect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

import better.hello.common.BaseSchedulerTransformer;
import better.hello.common.BaseSubscriber;
import better.hello.data.DataSourceDbImpl;
import better.hello.data.bean.NewsListBean;
import better.hello.data.db.TableInfo;
import better.hello.http.call.RequestInfo;
import better.hello.util.JsonUtils;
import better.hello.util.Utils;
import better.lib.http.RequestType;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by better on 2016/12/26.
 */

public class CollectDataSourceImp extends DataSourceDbImpl<List<NewsListBean>> {
    private Func1<Cursor, NewsListBean> mapper = new Func1<Cursor, NewsListBean>() {
        @Override
        public NewsListBean call(Cursor cursor) {
            return JsonUtils.fromJson(cursor.getString(cursor.getColumnIndexOrThrow(TableInfo.NewsCollectTable.JSON)), NewsListBean.class);
        }
    };

    /**
     * Des
     * Create By better on 2016/10/27 09:45.
     *
     * @param context
     */
    public CollectDataSourceImp(Context context) {
        super(context);
    }


    @Override
    public Subscription asyncUrlInfo(RequestInfo<List<NewsListBean>> info) {
        return getLocalInfo(info);
    }

    @Override
    public Subscription getLocalInfo(final RequestInfo<List<NewsListBean>> info) {
        return db.createQuery(TableInfo.NewsCollectTable.TABLE_NAME, TableInfo.getCollects()).mapToList(mapper).doOnNext(new Action1<List<NewsListBean>>() {
            @Override
            public void call(List<NewsListBean> list) {
                Utils.d("better","----->");
                if (null == list || list.isEmpty()) {
                    isLocalEmpty = true;
                    if (info.getRequestTye() != RequestType.DATA_REQUEST_UP_REFRESH) {
                        throw new IllegalArgumentException("暂无收藏");
                    } else {
                        throw new IllegalArgumentException("已全部加载");
                    }
                }
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

    public void save(NewsListBean bean, RequestInfo<List<NewsListBean>> info) {
        ArrayList<NewsListBean> listBeen = new ArrayList<>();
        listBeen.add(bean);
        save(listBeen, info);
    }

    public void delete(String key) {
        db.delete(TableInfo.NewsCollectTable.TABLE_NAME, TableInfo.NewsCollectTable.TITLE + " = '" + key + "'");
    }
}
