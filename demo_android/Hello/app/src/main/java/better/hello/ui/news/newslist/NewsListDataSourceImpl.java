package better.hello.ui.news.newslist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import better.hello.common.BaseSchedulerTransformer;
import better.hello.common.BaseSubscriber;
import better.hello.data.DataSourceDbImpl;
import better.hello.data.bean.NetEaseNewsListBean;
import better.hello.data.bean.NewsListBean;
import better.hello.data.db.TableInfo;
import better.hello.http.HttpUtil;
import better.hello.http.api.NewsSourceType;
import better.hello.http.call.RequestInfo;
import better.hello.util.C;
import better.hello.util.JsonUtils;
import better.lib.http.RequestType;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Des 新闻Model
 * Create By better on 2016/10/26 14:24.
 */
public class NewsListDataSourceImpl extends DataSourceDbImpl<List<NewsListBean>> {
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
    public Subscription asyncUrlInfo(final RequestInfo<List<NewsListBean>> info) {
        if (NewsSourceType.NETEASE == (int) info.getPrams().get(C.EXTRA_SOURCE_TYPE)) {
            return HttpUtil.getNewsList((String) info.getPrams().get(C.EXTRA_FIRST), (String) info.getPrams().get(C.EXTRA_SECOND), (int) info.getPrams().get(C.EXTRA_THIRD))
                    .map(new Func1<Map<String, List<NetEaseNewsListBean>>, List<NetEaseNewsListBean>>() {
                        @Override
                        public List<NetEaseNewsListBean> call(Map<String, List<NetEaseNewsListBean>> stringListMap) {
                            String key = null;
                            /* 房地产返回的key是汉字  --better 2017/1/18 10:39. */
                            Set<String> k = stringListMap.keySet();
                            if (null != k && !k.isEmpty()) {
                                for (String fadeKey : k) {
                                    key = fadeKey;
                                }
                            }
                            if (TextUtils.isEmpty(key))
                                key = (String) info.getPrams().get(C.EXTRA_SECOND);
                            List<NetEaseNewsListBean> list = stringListMap.get(key);
//                        isLoadFromNetSuccess=null!=list&&!list.isEmpty();
                            return list;
                        }
                    }).map(new Func1<List<NetEaseNewsListBean>, List<NewsListBean>>() {
                        @Override
                        public List<NewsListBean> call(List<NetEaseNewsListBean> netEaseNewsListBeen) {
                            return getNewsListFromNet(netEaseNewsListBeen, isNeedSave(info.getRequestTye()), isNeedSave(info.getRequestTye()));
                        }
                    }).doOnNext(new Action1<List<NewsListBean>>() {
                        @Override
                        public void call(List<NewsListBean> list) {
                            if (isNeedSave(info.getRequestTye())) {
                                save(list, info);
                            }
                        }
                    }).subscribe(new BaseSubscriber<>(info));
        } else {
            return null;
        }
    }

    @Override
    public Subscription getLocalInfo(RequestInfo<List<NewsListBean>> info) {
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
                }).map(new Func1<List<NetEaseNewsListBean>, List<NewsListBean>>() {
                    @Override
                    public List<NewsListBean> call(List<NetEaseNewsListBean> netEaseNewsListBeen) {
                        return getNewsListFromNet(netEaseNewsListBeen, false, true);
                    }
                })
                .compose(new BaseSchedulerTransformer<List<NewsListBean>>())
                .subscribe(new BaseSubscriber<>(info));
    }


    @Override
    public void save(List<NewsListBean> beans, RequestInfo<List<NewsListBean>> info) {
//        if (!isNeedCache(info.getRequestTye()))return;
        if (null == beans || beans.isEmpty()) return;
        BriteDatabase.Transaction transaction = db.newTransaction();
        String type_id = (String) info.getPrams().get(C.EXTRA_SECOND);
        try {
            db.execute(TableInfo.deleteNewsByType(type_id));
            for (NewsListBean bean : beans) {
                ContentValues values = new ContentValues();
                values.put(TableInfo.NewsListTable.TITLE, bean.getTitle());
                values.put(TableInfo.NewsListTable.TYPE_ID, type_id);
                values.put(TableInfo.NewsListTable.JSON, bean.getJson());
                db.insert(TableInfo.NewsListTable.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    /**
     * Des 主要是想保存各个咨询返回的所有信息在本地
     * Create By better on 2016/12/13 13:39.
     */
    private boolean isNeedSave(RequestType type) {
        return RequestType.DATA_REQUEST_UP_REFRESH != type;
    }

    private List<NewsListBean> getNewsListFromNet(List<NetEaseNewsListBean> netEaseNewsListBeen, boolean isNeedJsonStr, boolean isBanner) {
        List<NewsListBean> list = new ArrayList<>();
        if (null != netEaseNewsListBeen)
            for (NetEaseNewsListBean b : netEaseNewsListBeen) {
                list.add(NewsListBean.convert(b, isNeedJsonStr, isBanner));
            }
        return list;
    }
}
