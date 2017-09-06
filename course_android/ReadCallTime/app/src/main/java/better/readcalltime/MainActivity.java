package better.readcalltime;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //可以直接观察状态变化
//        new CallListenHelper().register(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("better", "没有权限");
            return;
        }
        Date currentDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            currentDate = format.parse(format.format(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[]{
                        CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
                        CallLog.Calls.TYPE, CallLog.Calls.DATE,
                        CallLog.Calls.DURATION
                }, CallLog.Calls.NUMBER + " = ?" + " and " + CallLog.Calls.TYPE + " =?" + " and " + CallLog.Calls.DATE + " >?",
                new String[]{"10086", "2", String.valueOf(currentDate.getTime())}, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (null != cursor && cursor.getCount() > 0 && cursor.moveToNext()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            do {
                int callDuration = Integer.parseInt(cursor.getString(4));
                int min = callDuration / 60;
                int sec = callDuration % 60;
                String time = (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec);
                date.setTime(Long.parseLong(cursor.getString(3)));
                Log.d("Better",
                        cursor.getString(0) + "," +
                                cursor.getString(1) + "," +
                                cursor.getString(2) + "," +
                                sdf.format(date) + "," +
                                time);
            } while (cursor.moveToNext());
            cursor.close();
        }
        Log.d("better", "init____ok");

    }
}
