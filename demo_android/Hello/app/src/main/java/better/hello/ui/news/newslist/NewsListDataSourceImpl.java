package better.hello.ui.news.newslist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;
import java.util.Map;

import better.hello.common.BaseSchedulerTransformer;
import better.hello.common.BaseSubscriber;
import better.hello.data.DataSourceDbImpl;
import better.hello.data.bean.NewsListBean;
import better.hello.data.db.TableInfo;
import better.hello.http.HttpUtil;
import better.hello.http.call.RequestInfo;
import better.hello.util.C;
import better.hello.util.JsonUtils;
import better.hello.util.Utils;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Des 新闻Model
 * Create By better on 2016/10/26 14:24.
 */
public class NewsListDataSourceImpl extends DataSourceDbImpl<List<NewsListBean>> {
    private Func1<Cursor, NewsListBean> mapper = new Func1<Cursor, NewsListBean>() {
        @Override
        public NewsListBean call(Cursor cursor) {

            return JsonUtils.fromJson(cursor.getString(cursor.getColumnIndexOrThrow(TableInfo.NewsListTable.JSON)), NewsListBean.class);
        }
    };

    /**
     * Des
     * Create By better on 2016/10/27 09:45.
     */
    public NewsListDataSourceImpl(Context context) {
        super(context);
    }

    @Override
    public Subscription asyncUrlInfo(final RequestInfo<List<NewsListBean>> info) {
        return HttpUtil.getNewsList((String) info.getPrams().get(C.EXTRA_FIRST), (String) info.getPrams().get(C.EXTRA_SECOND), (int) info.getPrams().get(C.EXTRA_THIRD))
                .map(new Func1<Map<String, List<NewsListBean>>, List<NewsListBean>>() {
                    @Override
                    public List<NewsListBean> call(Map<String, List<NewsListBean>> stringListMap) {
                        List<NewsListBean> list= stringListMap.get(info.getPrams().get(C.EXTRA_SECOND));
                        isLoadFromNetSuccess=null!=list&&!list.isEmpty();
                        return list;
                    }
                }).subscribe(new BaseSubscriber<>(info));
    }

    @Override
    public Subscription getLocalInfo(RequestInfo<List<NewsListBean>> info) {
        Utils.d("Better", "getLocalInfo() " + info.getRequestTye());
        return db.createQuery(TableInfo.NewsListTable.TABLE_NAME, TableInfo.getNewsByType((String) info.getPrams().get(C.EXTRA_SECOND)))
                .mapToList(mapper)
                .doOnNext(new Action1<List<NewsListBean>>() {
                    @Override
                    public void call(List<NewsListBean> listBeen) {
                        isLocalEmpty = listBeen.isEmpty();
                        if (isLocalEmpty) {
                            throw new IllegalArgumentException("本地列表 null");
                        }
                    }
                })
                .compose(new BaseSchedulerTransformer<List<NewsListBean>>())
                .subscribe(new BaseSubscriber<>(info));
    }


    @Override
    public void save(List<NewsListBean> beans, RequestInfo<List<NewsListBean>> info) {
        if (!isNeedCache(info.getRequestTye()))return;
        BriteDatabase.Transaction transaction = db.newTransaction();
        String type_id=(String) info.getPrams().get(C.EXTRA_SECOND);
        try {
            db.execute(TableInfo.deleteNewsByType(type_id));
            for (NewsListBean bean : beans) {
                ContentValues values = new ContentValues();
                values.put(TableInfo.NewsListTable.TITLE, bean.getTitle());
                values.put(TableInfo.NewsListTable.JSON, JsonUtils.toJson(bean));
                values.put(TableInfo.NewsListTable.TYPE_ID, type_id);
                db.insert(TableInfo.NewsListTable.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }
}
