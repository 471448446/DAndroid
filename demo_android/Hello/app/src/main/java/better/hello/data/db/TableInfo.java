package better.hello.data.db;

import android.provider.BaseColumns;

/**
 * Created by better on 2016/10/26.
 */

public class TableInfo {
    private static final String TEXT_TYPE = " TEXT";
    private static final String TEXT_INTGER = " INTEGER";
    private static final String INTEGER_PRIMARY_AUTO = " INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String COMMA_SEP = ",";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";

    public static final String SQL_CREATE_NEWS_TABLE =
            CREATE_TABLE_IF_NOT_EXISTS + NewsListTable.TABLE_NAME + " (" +
                    NewsListTable._ID + INTEGER_PRIMARY_AUTO + COMMA_SEP +
                    NewsListTable.TITLE + TEXT_TYPE + COMMA_SEP +
                    NewsListTable.TYPE_ID + TEXT_TYPE + COMMA_SEP +
                    NewsListTable.JSON + TEXT_TYPE +
                    " )";
    public static final String SQL_CREATE_NEWS_COLLECT_TABLE =
            CREATE_TABLE_IF_NOT_EXISTS + NewsCollectTable.TABLE_NAME + " (" +
                    NewsCollectTable._ID + INTEGER_PRIMARY_AUTO + COMMA_SEP +
                    NewsCollectTable.TITLE + TEXT_TYPE + COMMA_SEP +
                    NewsCollectTable.TYPE_ID + TEXT_TYPE + COMMA_SEP +
                    NewsCollectTable.JSON + TEXT_TYPE +
                    " )";

    public interface NewsListTable extends BaseColumns {
        String TABLE_NAME = "newsList";
        String TITLE = "title";
        String TYPE_ID = "type_id";
        String JSON = "json";
    }

    public interface NewsCollectTable extends BaseColumns {
        String TABLE_NAME = "newsCollects";
        String TITLE = "title";
        String TYPE_ID = "type_id";
        String JSON = "json";
    }

    public static String getNewsByType(String type) {
        return "select * from " + NewsListTable.TABLE_NAME + " where " + NewsListTable.TYPE_ID + "='" + type + "'";
    }

    public static String deleteNewsByType(String type) {
        return "delete from " + NewsListTable.TABLE_NAME + " where " + NewsListTable.TYPE_ID + "='" + type + "'";
    }

    public static String getCollects() {
        return "select * from " + NewsCollectTable.TABLE_NAME;
    }
}
