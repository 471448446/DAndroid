package better.learn.ndkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * 使用cmake工具构建
 * 1.对build文件进行修改
 * 2.添加构建文件CMakeLists.txt
 * 3.添加C++ Source
 * 最后运行或者构建工程就可以生成so。
 * https://developer.android.com/studio/projects/add-native-code?utm_source=android-studio#create-sources
 * Create by Better 2018/5/29 15:46
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.mainTxt);
        textView.setText(new JNIUtil().sayHelloJNI());
    }
}
