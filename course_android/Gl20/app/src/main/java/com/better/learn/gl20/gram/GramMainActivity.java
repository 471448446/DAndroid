package com.better.learn.gl20.gram;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by better on 2020/3/20 10:48 PM.
 */
public class GramMainActivity extends AppCompatActivity {

    private HexagramSurfaceView mHexagramSurfaceView;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHexagramSurfaceView = new HexagramSurfaceView(this);
        mHexagramSurfaceView.requestFocus();
        mHexagramSurfaceView.setFocusableInTouchMode(true);
        setContentView(mHexagramSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHexagramSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHexagramSurfaceView.onPause();
    }
}
