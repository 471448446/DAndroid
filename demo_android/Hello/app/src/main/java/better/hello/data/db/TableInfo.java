package better.hello.data.db;

import android.provider.BaseColumns;

import better.hello.AppConfig;

/**
 * 注意where 语句的写法''两个字符开头结尾最好不要写空格。
 * Created by better on 2016/10/26.
 */

public class TableInfo {
    private static final String TEXT_TYPE = " TEXT";
    private static final String TEXT_INTEGER = " INT";
    private static final String INTEGER_PRIMARY_AUTO = " INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String COMMA_SEP = ",";
    //    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

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
    public static final String SQL_CREATE_NEWS_CHANNEL_TABLE =
            CREATE_TABLE_IF_NOT_EXISTS + NewsChannelTable.TABLE_NAME + " (" +
                    NewsChannelTable._ID + INTEGER_PRIMARY_AUTO + COMMA_SEP +
                    NewsChannelTable.SELECT + TEXT_INTEGER + COMMA_SEP +
                    NewsChannelTable.TYPE + TEXT_INTEGER + COMMA_SEP +
                    NewsChannelTable.NAME + TEXT_TYPE + COMMA_SEP +
                    NewsChannelTable.POST_ID + TEXT_TYPE + COMMA_SEP +
                    NewsChannelTable.JSON + TEXT_TYPE +
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

    public interface NewsChannelTable extends BaseColumns {
        String TABLE_NAME = "newsChannel";
        String SELECT = "selected";
        String NAME = "name";
        String POST_ID = "post_id";
        String TYPE = "type";
        String JSON = "json";
    }

    public static String deleteTable(String tableName) {
        return DROP_TABLE_IF_EXISTS + tableName;
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

    /**
     * Des select * from NewsCollects limit 8,8
     * Create By better on 2017/1/3 15:58.
     */
    public static String getCollects(int start) {
        return "select * from " + NewsCollectTable.TABLE_NAME + " limit " + start + "," + AppConfig.PAGES_SIZE;
    }

    public static String getCollect(String key) {
        return "select * from " + NewsCollectTable.TABLE_NAME + " where " + NewsCollectTable.TITLE + " = '" + key + "'";
    }

    public static String getChannel() {
        return "select * from " + NewsChannelTable.TABLE_NAME;
    }

    public static String deleteChannelAll() {
        return "delete from " + NewsChannelTable.TABLE_NAME ;
    }
}
