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
import better.hello.data.bean.NetEaseNewsListBean;
import better.hello.data.db.TableInfo;
import better.hello.http.HttpUtil;
import better.hello.http.call.RequestInfo;
import better.hello.util.C;
import better.hello.util.JsonUtils;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Des 新闻Model
 * Create By better on 2016/10/26 14:24.
 */
public class NewsListDataSourceImpl extends DataSourceDbImpl<List<NetEaseNewsListBean>> {
    private Func1<Cursor, NetEaseNewsListBean> mapper = new Func1<Cursor, NetEaseNewsListBean>() {
        @Override
        public NetEaseNewsListBean call(Cursor cursor) {

            return JsonUtils.fromJson(cursor.getString(cursor.getColumnIndexOrThrow(TableInfo.NewsListTable.JSON)), NetEaseNewsListBean.class);
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
    public Subscription asyncUrlInfo(final RequestInfo<List<NetEaseNewsListBean>> info) {
        return HttpUtil.getNewsList((String) info.getPrams().get(C.EXTRA_FIRST), (String) info.getPrams().get(C.EXTRA_SECOND), (int) info.getPrams().get(C.EXTRA_THIRD))
                .map(new Func1<Map<String, List<NetEaseNewsListBean>>, List<NetEaseNewsListBean>>() {
                    @Override
                    public List<NetEaseNewsListBean> call(Map<String, List<NetEaseNewsListBean>> stringListMap) {
                        List<NetEaseNewsListBean> list= stringListMap.get(info.getPrams().get(C.EXTRA_SECOND));
                        isLoadFromNetSuccess=null!=list&&!list.isEmpty();
                        return list;
                    }
                }).subscribe(new BaseSubscriber<>(info));
    }

    @Override
    public Subscription getLocalInfo(RequestInfo<List<NetEaseNewsListBean>> info) {
//        Utils.d("Better", "getLocalInfo() " + info.getRequestTye());
        return db.createQuery(TableInfo.NewsListTable.TABLE_NAME, TableInfo.getNewsByType((String) info.getPrams().get(C.EXTRA_SECOND)))
                .mapToList(mapper)
                .doOnNext(new Action1<List<NetEaseNewsListBean>>() {
                    @Override
                    public void call(List<NetEaseNewsListBean> listBeen) {
                        isLocalEmpty = listBeen.isEmpty();
                        if (isLocalEmpty) {
                            throw new IllegalArgumentException("本地列表 null");
                        }
                    }
                })
                .compose(new BaseSchedulerTransformer<List<NetEaseNewsListBean>>())
                .subscribe(new BaseSubscriber<>(info));
    }


    @Override
    public void save(List<NetEaseNewsListBean> beans, RequestInfo<List<NetEaseNewsListBean>> info) {
        if (!isNeedCache(info.getRequestTye()))return;
        BriteDatabase.Transaction transaction = db.newTransaction();
        String type_id=(String) info.getPrams().get(C.EXTRA_SECOND);
        try {
            db.execute(TableInfo.deleteNewsByType(type_id));
            for (NetEaseNewsListBean bean : beans) {
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
