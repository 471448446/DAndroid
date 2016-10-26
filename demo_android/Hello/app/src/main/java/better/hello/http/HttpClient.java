package better.hello.http;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import better.hello.util.C;
import better.hello.util.Utils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by better on 2016/10/17.
 */

public class HttpClient {
    static HttpClient instance;
    private final int TIME_OUT = 15;
    private OkHttpClient mOkHttpClient;
    private final Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Utils.d(C.HTTP_TAG, String.format("---->Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Utils.d(C.HTTP_TAG, String.format(Locale.getDefault(), "---->Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
//            if (response.body()!=null&&response.body().bytes()!=null){
//                Utils.d(C.HTTP_TAG,String.valueOf(response.body().bytes()));
//            }
            return response;
        }
    };

    private HttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(mLoggingInterceptor);
        mOkHttpClient = builder.build();
    }

    public static HttpClient getInstance() {
        if (null == instance) {
            synchronized (HttpClient.class) {
                if (null == instance) {
                    instance = new HttpClient();
                }
            }
        }
        return instance;
    }

    public OkHttpClient getHttpClient() {
        return mOkHttpClient;
    }
}
