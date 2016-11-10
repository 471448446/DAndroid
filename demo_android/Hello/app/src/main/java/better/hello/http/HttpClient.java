package better.hello.http;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import better.hello.App;
import better.hello.util.C;
import better.hello.util.FileUtils;
import better.hello.util.Utils;
import better.lib.utils.NetUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * cache:http://www.jianshu.com/p/9c3b4ea108a7
 * http://mushuichuan.com/2016/03/01/okhttpcache/
 * 缓存：默认不开启，手动配置开不开
 * Created by better on 2016/10/17.
 */

public class HttpClient {
    static HttpClient instance;
    private final int TIME_OUT = 20;
    private OkHttpClient mOkHttpClient;
    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
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
    private Cache mCache = new Cache(new File(FileUtils.getCacheFileDir(App.getApplication())), 1024 * 1024 * 40);
    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private final Interceptor mCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtils.isNetworkAvailable(App.getApplication())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetUtils.isNetworkAvailable(App.getApplication())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                CacheControl cacheControl = request.cacheControl();
                String cacheControlStr = cacheControl.toString();
                Response.Builder builder = originalResponse.newBuilder().removeHeader("Pragma");
                if (0 >= cacheControl.maxAgeSeconds()) {
                    return builder.removeHeader("Pragma").header("Cache-Control", "no-cache").build();
                } else {
                    return builder
                            .header("Cache-Control", cacheControlStr)
                            .build();
                }
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", cacheControlStr)
//                        .removeHeader("Pragma")
//                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    private HttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(mCacheControlInterceptor)
                .addNetworkInterceptor(mCacheControlInterceptor)
                .cache(mCache)
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
