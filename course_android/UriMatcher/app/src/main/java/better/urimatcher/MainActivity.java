package better.urimatcher;

import android.content.ContentUris;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Des UriMatcher与ContentUris
 * android uri= content://authority/path/id
 * <p>
 * Create By better on 16/8/17 11:03.
 */
public class MainActivity extends AppCompatActivity {
    TextView txt;
    static String auther = "better.urimatcher";
    static final int Macher_ID_PEOPLE = 1;
    static final int Macher_ID_PERSON = Macher_ID_PEOPLE + 1;
    static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(auther, "/people", Macher_ID_PEOPLE);
        //这里的#代表匹配任意数字，另外还可以用*来匹配任意文本
        matcher.addURI(auther, "/person/#", Macher_ID_PERSON);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = (TextView) findViewById(R.id.txt);
        //准备一个Uri
        Uri uri = Uri.parse("content://" + auther + "/people");
        l("uri 1=" + uri);
        l("uri 1 match=" + getType(uri));
        Uri uri2 = Uri.parse("content://" + auther + "/person" + "/2");
        l("uri 2=" + uri2);
        l("uri 2 match=" + getType(uri2));
        //拼接Id
        l("uri 3 添加Id前 parse Id=" + ContentUris.parseId(uri2));
        Uri uri3 = ContentUris.withAppendedId(uri2, 999);
        l("uri 3=" + uri3);
        l("uri 3 parse Id=" + ContentUris.parseId(uri3));
    }

    private String getType(Uri uri) {
        int match = matcher.match(uri);
        switch (match) {
            case Macher_ID_PEOPLE:
                return "vnd.android.cursor.dir/people";
            case Macher_ID_PERSON:
                return "vnd.android.cursor.item/person";
            default:
                return null;
        }
    }

    private void l(String msg) {
        Log.d("MainActivity", msg);
        StringBuilder builder = new StringBuilder(txt.getText());
        builder.append("\n");
        builder.append(msg);
        txt.setText(builder.toString().trim());
    }
}
