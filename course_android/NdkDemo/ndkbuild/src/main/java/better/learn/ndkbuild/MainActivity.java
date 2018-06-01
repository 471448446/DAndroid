package better.learn.ndkbuild;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * https://juejin.im/post/5a67dcdb518825732c53b338
 * 创建 javah、ndkBuild tools
 * Create by Better 2018/5/31 10:29
 */
public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("hellojni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.mainTxt)).setText(helloJni() + "\n" + helloJni2());
    }

    public native String helloJni();

    public native String helloJni2();
}
