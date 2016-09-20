package better.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Des https://developer.android.com/studio/build/multidex.html#dev-build
 * aar 的引用
 * Create By better on 16/9/20 11:14.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
