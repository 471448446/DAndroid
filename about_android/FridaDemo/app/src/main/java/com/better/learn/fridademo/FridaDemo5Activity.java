package com.better.learn.fridademo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * https://11x256.github.io/Frida-hooking-android-part-5/
 * 假设加加密的key不是直接硬编码写在代码中的
 * 尝试打印加密参数
 *
 * @author Better
 * @date 2021/1/8 14:07
 */
public class FridaDemo5Activity extends AppCompatActivity {
    private static final String KEY_SECRET = "aaaaaaaaaaaaaaaa";
    private static final String KEY_GENERATE = "bbbbbbbbbbbbbbbb";
    private static final String KEY_CIPHER = "AES/CBC/PKCS5PADDING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frida_demo5);
        findViewById(R.id.hook_cipher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String post = enc("zhangshan:mima123");
                findViewById(R.id.hook_cipher).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String res = dec(post);
                        Toast.makeText(FridaDemo5Activity.this, res, Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }
        });
    }

    private static String enc(String data) {
        String result = null;
        try {
            Cipher cipher = Cipher.getInstance(KEY_CIPHER);
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(KEY_SECRET.getBytes("UTF-8"), "AES"),
                    new IvParameterSpec(KEY_GENERATE.getBytes("UTF-8"))
            );
            byte[] bytes = cipher.doFinal(data.getBytes());
            result = new String(Base64.encode(bytes, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String dec(String data) {
        String result = null;
        try {
            byte[] bytes = Base64.decode(data, Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(KEY_CIPHER);
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    new SecretKeySpec(KEY_SECRET.getBytes("UTF-8"), "AES"),
                    new IvParameterSpec(KEY_GENERATE.getBytes("UTF-8"))
            );
            bytes = cipher.doFinal(bytes);
            result = new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}