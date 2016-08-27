package better.jsjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Des http://blog.csdn.net/wangtingshuai/article/details/8631835
 * http://droidyue.com/blog/2014/09/20/interaction-between-java-and-javascript-in-android/index.html
 * Create By better on 16/8/22 15:55.
 */
public class MainActivity extends AppCompatActivity {

    private WebView contentWebView = null;
    private TextView msgView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentWebView = (WebView) findViewById(R.id.webview);
        msgView = (TextView) findViewById(R.id.msg);
        // 启用javascript
        contentWebView.getSettings().setJavaScriptEnabled(true);
        // ´从assets目录下面的加载html
        contentWebView.loadUrl("file:///android_asset/wst.html");
//        contentWebView.loadUrl("file:///android_asset/hello.html");

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(btnClickListener);
        //这不是js调本地代码的基础
        contentWebView.addJavascriptInterface(new JsInteration(), "wst");
    }

    View.OnClickListener btnClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            contentWebView.loadUrl("javascript:javacalljs()");
            contentWebView.loadUrl("javascript:javacalljswithargs(" + "'传递参数:hello world'" + ")");
        }
    };

    public class JsInteration {

        @JavascriptInterface
        public void startFunction() {
            Toast.makeText(MainActivity.this, "js调用了java函数", Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    msgView.setText(msgView.getText() + "\njs调用了java函数");
                    contentWebView.loadUrl("javascript:javacalljswithargs(" + "'传递参数:userCode'" + ")");
                }
            });
        }

        @JavascriptInterface
        public void startFunction(final String str) {
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    msgView.setText(msgView.getText() + "\njs调用了java函数传递参数：" + str);

                }
            });
        }
    }

//    @JavascriptInterface
//    public void startFunction() {
//        Toast.makeText(this, "js调用了java函数", Toast.LENGTH_SHORT).show();
//
//        runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                msgView.setText(msgView.getText() + "\njs调用了java函数");
//
//            }
//        });
//    }
//    @JavascriptInterface
//    public void startFunction(final String str) {
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
//        runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                msgView.setText(msgView.getText() + "\njs调用了java函数传递参数：" + str);
//
//            }
//        });
//    }
}
