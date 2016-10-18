package better.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Des http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0504/4208.html
 * Create By better on 2016/10/12 10:29.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        useCall();
//        useOkClient();
        useRx();
    }

    private void useRx() {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl("http://c.m.163.com/").build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.contributorsBySimpleGetCallRx("headline", "T1348647909107", 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<News>() {
            @Override
            public void onCompleted() {
                l("列表"+"完成");

            }

            @Override
            public void onError(Throwable e) {
                l("列表"+"error："+e.getMessage());
            }

            @Override
            public void onNext(News news) {
                if (null!=news){
                    l("列表"+news.getT1348647909107().size());
                }else {
                    l("列表是空的");
                }
                setText();
            }
        });

    }



    private void useCall() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://c.m.163.com/").build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> call = apiService.contributorsBySimpleGetCall("headline", "T1348647909107", 0);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("MainActivity", new String(response.body().bytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void useOkClient() {
        //OKHttp Callback改在了UiThread
        Request request=new Request.Builder().url("http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html").build();
        OkHttpClient client=new OkHttpClient.Builder().build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                l("keyide 成功");
                setText();
            }
        });
    }

    private void setText() {
        findViewById(R.id.txt).setAlpha(0.1f);
    }

    private void l(String 成功) {
        Log.d("MainActivity",成功);
    }
}
