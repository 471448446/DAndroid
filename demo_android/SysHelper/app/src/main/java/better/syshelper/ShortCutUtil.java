package better.syshelper;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

/**
 * Created by better on 2016/10/17.
 */

public class ShortCutUtil {

    public static void addShortCut(Context context,int nameRes,int iconRes,Class des) {
        //创建一个添加快捷方式的Intent
        Intent addSC = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //快捷键的标题
        String title=context.getResources().getString(nameRes);
        //快捷键的图标
        Parcelable icon=Intent.ShortcutIconResource.fromContext(context, iconRes);
        //设置快捷键的标题
        addSC.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        //设置快捷键的图标
        addSC.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //设置单击此快捷键启动的程序
        addSC.putExtra(Intent.EXTRA_SHORTCUT_INTENT,new Intent(context,des));

        //不允许重复创建
        addSC.putExtra("duplicate", false);

        context.sendBroadcast(addSC);
    }

}
