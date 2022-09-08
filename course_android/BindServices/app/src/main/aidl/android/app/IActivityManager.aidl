//import android.app.IApplicationThread;
import android.os.Binder;
import android.content.Intent;

interface IActivityManager {
    ComponentName startService(in String caller, in Intent service,
            in String resolvedType, boolean requireForeground, in String callingPackage,
            in String callingFeatureId, int userId);
}