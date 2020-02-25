package com.better.learn.gyroscopetest;

import android.os.Bundle;

import com.better.learn.gyroscopetest.gyroscope.GyroscopeImageView;
import com.better.learn.gyroscopetest.gyroscope.GyroscopeManager;
import com.better.learn.gyroscopetest.gyroscope.GyroscopeTransFormation;
import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;

/**
 * https://www.cnblogs.com/kimmy/p/5034150.html
 * https://github.com/JY39/GyroscopeImageDemo
 * https://muyangmin.github.io/glide-docs-cn/doc/transformations.html
 */
public class MainActivity extends AppCompatActivity {

    private static final String IMAGE_URL = "https://farm6.staticflickr.com/5210/5321285316_f9d942d8a9_o.jpg";
//    private static final String IMAGE_URL = "http://vrlab-public.ljcdn.com//release//vradmin//1000000020129136//images//FF41C450.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GyroscopeImageView imageView = findViewById(R.id.gyroscope);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                Glide.with(MainActivity.this)
                        .load(IMAGE_URL)
                        .transform(new GyroscopeTransFormation(imageView.getWidth(),
                                imageView.getHeight()))
                        .into(imageView);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        GyroscopeManager.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GyroscopeManager.getInstance().unregister(this);
    }
}
