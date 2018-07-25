package better.learn.shortcut;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import better.shortcut.R;

/**
 * Des
 * https://developer.android.com/guide/topics/ui/shortcuts.html
 * 译文：http://www.10tiao.com/html/169/201610/2650821321/1.html
 * Create By better on 2017/2/15 13:50.
 */
public class MainActivity extends AppCompatActivity {
    private final String EXTRA_01 = "extra_01";

    private ShortcutManager manager;

    private static final String id_other = "com.test.01";
    private static final String id_self = "快捷键名称";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (null != intent) {
            log(intent.getStringExtra(EXTRA_01));
        } else {
            log("没有extra");
        }

        init();
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private void init() {
        manager = (ShortcutManager) getSystemService(Context.SHORTCUT_SERVICE);

        findViewById(R.id.button_addDynamicOther).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    addDynamicShortCutsOther();
                }
            }
        });
        findViewById(R.id.button_removeDynamicOther).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    removeDynamicShortCutsOther();
                }
            }
        });
        findViewById(R.id.button_addDynamicSelf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    addDynamicShortCutsSelf();
                }
            }
        });
        findViewById(R.id.button_removeDynamicSelf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    removeDynamicShortCutsSelf();
                }
            }
        });

        findViewById(R.id.button_pinned_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    addPinnedShortCut(MainActivity.this);
                }
            }
        });
        findViewById(R.id.button_pinned_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deletePinnedShortCut(MainActivity.this);
                }
            }
        });
    }

    private void log(String stringExtra) {
        Log.d("MainActivity", "" + stringExtra);
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private void addDynamicShortCutsOther() {
        List<ShortcutInfo> list = new ArrayList<>();

        Intent intentCall = new Intent(Intent.ACTION_CALL);
        intentCall.setData(Uri.parse("tel:" + "17000000001"));
        ShortcutInfo info = new ShortcutInfo.Builder(this, id_other)
                .setShortLabel("打电话")
                .setLongLabel("打电话Long")
                .setDisabledMessage("打电话不能用")
                .setIntent(intentCall)
                .build();
        list.add(info);

        manager.addDynamicShortcuts(list);
        log("count=" + manager.getMaxShortcutCountPerActivity() + ",当前=" + manager.getDynamicShortcuts().size());
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void removeDynamicShortCutsOther() {
        List<String> ids = new ArrayList<>();
        ids.add(id_other);
        manager.removeDynamicShortcuts(ids);
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private void addDynamicShortCutsSelf() {
        ShortcutManager manager = (ShortcutManager) getSystemService(Context.SHORTCUT_SERVICE);
        List<ShortcutInfo> list = new ArrayList<>();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(EXTRA_01, "test01的数据");


        ShortcutInfo info = new ShortcutInfo.Builder(this, id_self)
                .setShortLabel("打开主页")
                .setLongLabel("打开主页Long")
                .setDisabledMessage("打开主页不能用")
                .setIntent(intent)
                .build();
        list.add(info);

        manager.addDynamicShortcuts(list);
        log("count=" + manager.getMaxShortcutCountPerActivity() + ",当前=" + manager.getDynamicShortcuts().size());
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void removeDynamicShortCutsSelf() {
        List<String> ids = new ArrayList<>();
        ids.add(id_self);
        manager.removeDynamicShortcuts(ids);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void deletePinnedShortCut(MainActivity context) {
        ShortcutManager manager = (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
        if (!manager.isRequestPinShortcutSupported()) {
            return;
        }
        manager.disableShortcuts(Arrays.asList("The only id"));
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void addPinnedShortCut(Context context) {
//        ShortcutManagerCompat
        ShortcutManager manager = (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
        if (!manager.isRequestPinShortcutSupported()) {
            return;
        }
        Intent i = new Intent();
        i.setClass(context, SettingActivity.class);
        i.setAction(Intent.ACTION_MAIN);
//        i.setAction(Intent.ACTION_VIEW);
        //不是必须
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        ShortcutInfo info = new ShortcutInfo.Builder(context.getApplicationContext(), "id快捷键")
                .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                .setShortLabel("快捷键名称")
                .setIntent(i)
                .build();

        PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(context, 0,
                new Intent(context, SettingActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        manager.requestPinShortcut(info, shortcutCallbackIntent.getIntentSender());

//        Log.d("Better","___"+manager.addDynamicShortcuts(Arrays.asList(info)));
    }
}
