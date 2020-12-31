package com.better.learn.fridademo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * https://11x256.github.io/Frida-hooking-android-part-4/
 * <p>
 * 假设客户端验证了不能通过admin登录，设法跳过
 *
 * @author Better
 * @date 2020/12/31 14:31
 */
public class FridaDemo4Activity extends AppCompatActivity {
    EditText userNameEt;
    EditText userPasswordEt;
    TextView loginMessageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frida_demo4);
        userNameEt = findViewById(R.id.user_name);
        userPasswordEt = findViewById(R.id.user_password);
        loginMessageTv = findViewById(R.id.login_message);
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if ("admin".compareTo(userNameEt.getText().toString()) == 0) {
                    loginMessageTv.setText("You cannot login as admin");
                    loginMessageTv.setTextColor(Color.RED);
                    return;
                }

                loginMessageTv.setTextColor(Color.GRAY);
                loginMessageTv.setText("Send To Server:" +
                        Base64.encodeToString(
                                (userNameEt.getText().toString() + ":" + userPasswordEt.getText().toString()).getBytes(),
                                Base64.DEFAULT
                        ));
            }
        });
    }
}