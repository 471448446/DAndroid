package better.okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;

import better.http.HttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    String url = "https://c.m.163.com/nc/article/C88O7CDB000187V5/full.html";
    String urlFile = "http://testkf.easylines.cn/pic/images/share.png";


    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.txt);
        okHttp();
        netEasy();
        el();
    }

    private void el() {
        String p = "rUSM3nWIO88=";
        String url = "https://testpic.easylines.cn/pic/customer/ads/forStartup/android/1.7.1/?encrypt=true";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), p);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        HttpClient.getInstance(this).getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                mTextView.setText("失败了");
                Log.d("Better", "失败:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                mTextView.setText("成功了");
                Log.d("Better", "成功:" + response.body().string());
            }
        });
    }

    private void okHttp() {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new UserAgentInterceptor()).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                l(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                l(response.toString() + ",成功");
            }
        });
    }

    private void netEasy() {
        RequestQueue queue = Volley.newRequestQueue(this);
//        String url="https://c.m.163.com/nc/article/C87UMDC000097U82/full.html";
        queue.add(new StringRequest(this.url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                l("volley=" + s);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                l("volley= net Error");
            }
        }));
    }

    private void l(String msg) {
        Log.d("Better", msg);
    }

    /* This interceptor adds a custom User-Agent. */
    public class UserAgentInterceptor implements Interceptor {

        private final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";

//        public UserAgentInterceptor(String userAgent) {
//            this.userAgent = userAgent;
//        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request requestWithUserAgent = originalRequest.newBuilder()
                    .header("User-Agent", userAgent)
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }
}
