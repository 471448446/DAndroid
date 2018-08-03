package better.demo.contentprovidersp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log("SharePrf.KEY_ONE:" + SharePrf.getString(SharePrf.KEY_ONE));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log("get two:" + SharePrf.getString(SharePrf.KEY_TWO) + " KEY_THREE=" + SharePrf.getString(SharePrf.KEY_THREE));
            }
        }).start();
    }

    private void log(String msg) {
        Log.d("Better", "MainActivity:" + msg);
    }
}
