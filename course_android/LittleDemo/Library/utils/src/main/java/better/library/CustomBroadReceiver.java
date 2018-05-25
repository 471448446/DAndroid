package better.library;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

public abstract class CustomBroadReceiver extends BroadcastReceiver {
    public abstract IntentFilter getIntentFilter();
}
