package com.better.learn.apt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.better.learn.annoation.BindView;
import com.better.learn.annoation.OnClick;
import com.better.learn.library.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.txt1)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("Better", "bindView work? " + (null != mButton));
    }

    @OnClick(R.id.txt1)
    public void onClickTxt(View view) {
        Toast.makeText(this, "onClickTxt", Toast.LENGTH_SHORT).show();
        Log.e("onClick", "Txt = " + view.getId());
    }

    @OnClick(R.id.button)
    public void onClick() {
        Toast.makeText(this, "no pram", Toast.LENGTH_SHORT).show();
        Log.e("onClick", "click button  ");

    }
}
