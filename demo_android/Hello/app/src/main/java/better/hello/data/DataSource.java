package better.hello.data;

import android.content.Context;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import better.hello.data.db.DbHelper;
import rx.schedulers.Schedulers;

/**
 * Created by better on 2017/1/20.
 */

public abstract class DataSource {
    protected BriteDatabase db;

    public DataSource(Context context) {
        SqlBrite sqlBrite = SqlBrite.create();
        db = sqlBrite.wrapDatabaseHelper(new DbHelper(context), Schedulers.io());
    }
}
