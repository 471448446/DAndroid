package better.shortcut;

import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.yzzy.elt.passenger.MainApp;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by better on 2017/2/15.
 */

public class ShortCutsUtils {
    public static WeakReference<ShortcutManager> mShortcutManagerWeakReference;

    public static ShortcutManager getShortcutManager(Context context) {
        if (null == mShortcutManagerWeakReference || null == mShortcutManagerWeakReference.get()) {
            mShortcutManagerWeakReference == new WeakReference<>((ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE))
        }
        return mShortcutManagerWeakReference.get();
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public static void addShortCuts(Context context, ShortcutInfo shortcutInfo) {
        List<ShortcutInfo> infos = mShortcutManagerWeakReference.get().getDynamicShortcuts();
        if (getShortcutManager(context).getMaxShortcutCountPerActivity() > infos.size()) {
            infos.add(shortcutInfo);
            getShortcutManager(context).addDynamicShortcuts(infos);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public static void removeShortCuts(Context context, String id) {
        List<String> list = new ArrayList<>();
        list.add(id);
        removeShortCuts(context, list);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public static void removeShortCuts(Context context, List<String> list) {
        getShortcutManager(context).removeDynamicShortcuts(list);
    }
}
