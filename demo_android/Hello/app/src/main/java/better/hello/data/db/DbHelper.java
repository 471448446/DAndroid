package better.hello.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import better.hello.util.FileUtils;

/**
 * Created by better on 2016/10/19.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Hello.db";

    public DbHelper(Context context) {
        super(context, FileUtils.getDBFileName(), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL(TableInfo.SQL_CREATE_NEWS_TABLE);
        db.execSQL(TableInfo.SQL_CREATE_NEWS_COLLECT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            createTable(db);
    }
}
