package better.okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import better.http.HttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.txt);
        String p="rUSM3nWIO88=";
        String url = "https://testpic.easylines.cn/pic/customer/ads/forStartup/android/1.7.1/?encrypt=true";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), p);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        HttpClient.getInstance(this).getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                mTextView.setText("失败了");
                Log.d("Better", "失败:"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                mTextView.setText("成功了");
                Log.d("Better", "成功:"+response.body().string());
            }
        });
    }
}
