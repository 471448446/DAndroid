package better.hello.ui.splash;

import android.os.Bundle;

import better.hello.R;
import better.hello.ui.MainActivity;
import better.hello.ui.base.BaseActivity;

/**
 * Des http://news-at.zhihu.com/api/4/start-image/1080*1776
 * Create By better on 2016/10/26 11:08.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        forward(MainActivity.class);
    }
}
