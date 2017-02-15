package better.shortcut;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Des
 * https://developer.android.com/guide/topics/ui/shortcuts.html
 * 译文：http://www.10tiao.com/html/169/201610/2650821321/1.html
 * Create By better on 2017/2/15 13:50.
 */
public class MainActivity extends AppCompatActivity {
    private final String EXTRA_01 = "extra_01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setShortCuts();
        Intent intent = getIntent();
        if (null != intent) {
            log(intent.getStringExtra(EXTRA_01));
        } else {
            log("没有extra");
        }
    }

    private void log(String stringExtra) {
        Log.d("MainActivity", "" + stringExtra);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void setShortCuts() {
        ShortcutManager manager = (ShortcutManager) getSystemService(Context.SHORTCUT_SERVICE);
        List<ShortcutInfo> list = new ArrayList<>();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(EXTRA_01, "test01的数据");

        Intent intentCall = new Intent(Intent.ACTION_CALL);
        intentCall.setData(Uri.parse("tel:" + "17000000001"));
        ShortcutInfo info = new ShortcutInfo.Builder(this, "com.test.01")
                .setShortLabel("test01")
                .setLongLabel("test01点击")
                .setDisabledMessage("test01不能用")
                .setIntent(intentCall)
                .build();
        list.add(info);

        manager.addDynamicShortcuts(list);
        log("count=" + manager.getMaxShortcutCountPerActivity() + ",当前=" + manager.getDynamicShortcuts().size());
    }
}
