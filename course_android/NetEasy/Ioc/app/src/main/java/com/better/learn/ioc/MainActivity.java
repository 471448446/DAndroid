package com.better.learn.ioc;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.better.learn.ioc.lib.Bind;
import com.better.learn.ioc.lib.ContentView;
import com.better.learn.ioc.lib.InjectManager;
import com.better.learn.ioc.lib.OnClick;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.txt)
    TextView txt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectManager.inject(this);
        Log.e("Better", "null == txt ? " + (null == txt));
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @OnClick({R.id.txt})
    public void click(View view) {
        toast("click txt");
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
