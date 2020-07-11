package com.better.learn.flagsecure;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 最开始的时候发现打开支付宝，无法使用ADB，后面发现是Flyme系统的支付保护
 *
 * @author Better
 * @date 2020/7/11 9:50 PM
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 页面不能被截图
         */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }
}