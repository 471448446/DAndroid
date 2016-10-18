package better.hello.http;

import android.util.SparseArray;

import better.hello.http.api.Api;
import better.hello.http.api.HostType;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by better on 2016/10/17.
 */

public class RetrofitHelper {
    private ApiService mApiService;
    private static SparseArray<RetrofitHelper> mRetrofitManager = new SparseArray<>(HostType.TYPE_COUNT);

    private RetrofitHelper(int hostType) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.getHost(hostType)).client(HttpClient.getInstance().getHttpClient()).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        mApiService = retrofit.create(ApiService.class);
    }

    public static RetrofitHelper getInstance(int hostType) {
        RetrofitHelper helper = mRetrofitManager.get(hostType);
        if (null == helper) {
            synchronized (RetrofitHelper.class){
                if (null==helper){
                    helper = new RetrofitHelper(hostType);
                    mRetrofitManager.put(hostType, helper);
                }
            }
        }
        return helper;
    }

    public ApiService getApiService() {
        return this.mApiService;
    }

}
