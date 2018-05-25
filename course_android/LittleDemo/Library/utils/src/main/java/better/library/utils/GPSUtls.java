package better.library.utils;

import android.content.Context;
import android.location.LocationManager;

public class GPSUtls {
    public static boolean isEnabled(Context context) {
        try {
            LocationManager manager = (LocationManager) context.getApplicationContext()
                    .getSystemService(Context.LOCATION_SERVICE);
            return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Throwable throwable) {
        }
        return false;
    }
}
