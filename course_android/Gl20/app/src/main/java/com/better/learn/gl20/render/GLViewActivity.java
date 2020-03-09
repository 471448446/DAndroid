package com.better.learn.gl20.render;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.better.learn.gl20.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GLViewActivity extends AppCompatActivity {

    private GLView mGLView;
    private int lastX, lastY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fglview);
        mGLView = findViewById(R.id.gl_view);

        mGLView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getX();
                        lastY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        int dx = x - lastX;
                        int dy = y - lastY;
                        lastX = x;
                        lastY = y;
                        mGLView.rotate(dy / 10f, dx / 10f, 0f);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }
}
