package better.demo.contentprovidersp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

// 主进程向其他进程提供查询,当主进程被kill掉后，其他进程是查不到数据的
public class SharePreProvider extends ContentProvider {
    public static final String URI = "content://com.better.demo.provider.sharepreference";
    public static final int LENGTH_URI = URI.length() + 1;

    private static String EXTRA_TYPE = "type";
    private static String EXTRA_KEY = "key";
    private static String EXTRA_VALUE = "value";
    private static String EXTRA_FILE_NAME = "file_name";

    private static final int TYPE_BOOLEAN = 1;
    private static final int TYPE_INT = 2;
    private static final int TYPE_LONG = 3;
    private static final int TYPE_STRING = 4;
    private static final int TYPE_FLOAT = 5;

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int nType = values.getAsInteger(EXTRA_TYPE);
        String res = "";
        if (nType == TYPE_STRING) {
            res += SharePrf.getString(getContext(),
                    values.getAsString(EXTRA_KEY));
        }
        Log.d("Better", "pross:" + getContext().getApplicationInfo().processName);
        return Uri.parse(URI + "/" + res);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int nType = values.getAsInteger(EXTRA_TYPE);
        switch (nType) {
            case TYPE_STRING:
                SharePrf.setString(getContext(), values.getAsString(EXTRA_KEY), values.getAsString(EXTRA_VALUE));
                break;
        }
        return 0;
    }

    public static String getStr(String key) {
        ContentValues contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_STRING);
        contetvalues.put(EXTRA_KEY, key);
        Uri result = null;
        try {
            result = App.getInstance().getContentResolver().insert(Uri.parse(URI), contetvalues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Better", "result:" + result + "");
        if (null != result) {
            String res = result.toString();
            if (null != res && res.length() > LENGTH_URI) {
                return String.valueOf(result.toString().substring(LENGTH_URI));
            }
        }
        return null;
    }

    public static void setString(String key, String value) {
        ContentValues contetvalues = new ContentValues();
        contetvalues.put(EXTRA_TYPE, TYPE_STRING);
        contetvalues.put(EXTRA_KEY, key);
        contetvalues.put(EXTRA_VALUE, value);
        try {
            App.getInstance().getContentResolver().update(Uri.parse(URI), contetvalues, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
