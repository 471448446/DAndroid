package better.http;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class HttpClient {
    static HttpClient instance;
    private final int TIME_OUT = 20;
    private OkHttpClient mClient, mNoRetryClient;
    private Context mContext;
    private Cache mCache;

    private HttpClient(Context context) {
        this.mContext = context;
        mCache = new Cache(new File(mContext.getCacheDir(), "cache"), 1024 * 1024 * 40);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(mCache)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        mClient = builder.build();
        mNoRetryClient = mClient.newBuilder().retryOnConnectionFailure(false).build();
    }

    public static HttpClient getInstance(Context context) {
        if (null == instance) {
            synchronized (HttpClient.class) {
                if (null == instance) {
                    instance = new HttpClient(context);
                }
            }
        }
        return instance;
    }

    public OkHttpClient getHttpClient() {
        return mClient;
    }

    public OkHttpClient getNoRetryClient() {
        return mNoRetryClient;
    }

    public static void cancel(OkHttpClient client, Object tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Cannot cancelAll with a null tag");
        }
        for (Call call : client.dispatcher().queuedCalls()) {
            if (call.request().tag().equals(tag))
                call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (call.request().tag().equals(tag))
                call.cancel();
        }
    }

}
