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

    public static final String SQL_CREATE_NEWS_TABLE =
            "CREATE TABLE " + NewsListTable.TABLE_NAME + " (" +
                    NewsListTable._ID + INTEGER_PRIMARY_AUTO + COMMA_SEP +
                    NewsListTable.TITLE + TEXT_TYPE + COMMA_SEP +
                    NewsListTable.TYPE_ID + TEXT_TYPE + COMMA_SEP +
                    NewsListTable.JSON + TEXT_TYPE +
                    " )";
//    public static final String SQL_GET_NEWS_ALL="select * from "+NewsListTable.TABLE_NAME;

    public interface NewsListTable extends BaseColumns {
        String TABLE_NAME = "newsList";
        String TITLE = "title";
        String TYPE_ID = "type_id";
        String JSON = "json";
    }

    public static String getNewsByType(String type){
        return "select * from "+NewsListTable.TABLE_NAME+" where "+NewsListTable.TYPE_ID+"='"+type+"'";
    }
    public static String deleteNewsByType(String type){
        return "delete from "+NewsListTable.TABLE_NAME+" where "+NewsListTable.TYPE_ID+"='"+type+"'";
    }
}
