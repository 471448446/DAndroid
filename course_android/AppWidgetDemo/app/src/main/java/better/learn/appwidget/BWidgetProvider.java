package better.learn.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class BWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d("BWidgetProvider", "onUpdate appWidgetIds " + appWidgetIds.length);
        int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.desktop_layout);
            remoteViews.setOnClickPendingIntent(R.id.desktopImg1, pendingIntent);
//            remoteViews.setImageViewResource(R.id.desktopImg1, R.drawable.bg_2);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
