package better.learn.shortcuto;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShortCut(MainActivity.this);
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteShortCut(MainActivity.this);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void deleteShortCut(MainActivity context) {
        ShortcutManager manager = (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
        if (!manager.isRequestPinShortcutSupported()) {
            return;
        }
        manager.disableShortcuts(Arrays.asList("The only id"));
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void addShortCut(Context context) {
//        ShortcutManagerCompat
        ShortcutManager manager = (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
        if (!manager.isRequestPinShortcutSupported()) {
            return;
        }
        Intent i = new Intent();
        i.setClass(context, Main2Activity.class);
        i.setAction(Intent.ACTION_MAIN);
//        i.setAction(Intent.ACTION_VIEW);
        //不是必须
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        ShortcutInfo info = new ShortcutInfo.Builder(context.getApplicationContext(), "快捷键名称")
                .setIcon(Icon.createWithResource(context, R.drawable.ic_launcher))
                .setShortLabel("快捷键名称")
                .setIntent(i)
                .build();

        PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(context, 0,
                new Intent(context, ShortcutInOReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);

        manager.requestPinShortcut(info, shortcutCallbackIntent.getIntentSender());

//        Log.d("Better","___"+manager.addDynamicShortcuts(Arrays.asList(info)));
    }
}
