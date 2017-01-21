package better.hello.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

import better.hello.common.BaseSchedulerTransformer;
import better.hello.common.BaseSubscriber;
import better.hello.data.DataSource;
import better.hello.data.SourceHelper;
import better.hello.data.bean.NewsChannelBean;
import better.hello.data.db.TableInfo;
import better.hello.http.api.NewsSourceType;
import better.hello.http.call.RequestInfo;
import better.hello.util.C;
import better.hello.util.JsonUtils;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by better on 2017/1/20.
 */

public class NewsChannelDataSource extends DataSource {
    /**
     * Des
     * Create By better on 2016/10/27 09:45.
     */
    public NewsChannelDataSource(Context context) {
        super(context);
    }

    private void initChannel() {
        List<NewsChannelBean> list = SourceHelper.getNewsChannel();
        save(list);
    }

    public void save(List<NewsChannelBean> list) {
        if (null == list || list.isEmpty()) return;
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (NewsChannelBean bean : list) {
                ContentValues values = new ContentValues();
                values.put(TableInfo.NewsChannelTable.NAME, bean.getName());
                values.put(TableInfo.NewsChannelTable.TYPE, bean.getSourceType());
                values.put(TableInfo.NewsChannelTable.SELECT, bean.getSelect());
                values.put(TableInfo.NewsChannelTable.JSON, JsonUtils.toJson(bean));
                if (bean.getSourceType() == NewsSourceType.NETEASE) {
                    values.put(TableInfo.NewsChannelTable.POST_ID, bean.getNetEaseChannel().getChannelId());
                }
                db.insert(TableInfo.NewsChannelTable.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    Observable<List<NewsChannelBean>> getChannel() {
        return db.createQuery(TableInfo.NewsChannelTable.TABLE_NAME, TableInfo.getChannel()).mapToList(new Func1<Cursor, NewsChannelBean>() {
            @Override
            public NewsChannelBean call(Cursor cursor) {
                return JsonUtils.fromJson(cursor.getString(cursor.getColumnIndexOrThrow(TableInfo.NewsChannelTable.JSON)), NewsChannelBean.class);
            }
        }).doOnNext(new Action1<List<NewsChannelBean>>() {
            @Override
            public void call(List<NewsChannelBean> newsChannelBeen) {
                if (null == newsChannelBeen || newsChannelBeen.isEmpty()) {
                    initChannel();
                }
            }
        });
    }

    public Subscription getChannelAll(RequestInfo<List<NewsChannelBean>> requestInfo) {
        return getChannel().compose(new BaseSchedulerTransformer<List<NewsChannelBean>>()).subscribe(new BaseSubscriber<>(requestInfo));
    }

    public Subscription getChannel(final RequestInfo<List<NewsChannelBean>> requestInfo) {
        return getChannel().map(new Func1<List<NewsChannelBean>, List<NewsChannelBean>>() {
            @Override
            public List<NewsChannelBean> call(List<NewsChannelBean> newsChannelBeen) {
                List<NewsChannelBean> list = new ArrayList<>();
                for (NewsChannelBean b : newsChannelBeen) {
                    if (null != requestInfo && null != requestInfo.getPrams()) {
                        /* 选中和没选中  --better 2017/1/20 14:29. */
                        int select = (Integer) requestInfo.getPrams().get(C.EXTRA_FIRST);
                        if (NewsChannelBean.isSelect(select) && b.isSelect()) {
                            list.add(b);
                        } else if (!NewsChannelBean.isSelect(select) && !b.isSelect()) {
                            list.add(b);
                        }
                    }
                }
                return list;
            }
        }).compose(new BaseSchedulerTransformer<List<NewsChannelBean>>()).subscribe(new BaseSubscriber<>(requestInfo));
    }

    public void preSave() {
        db.execute(TableInfo.deleteChannelAll());
    }
}
