package better.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by better on 2016/10/10.
 */

public interface ApiService {
    /**
     * Des http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
     * Create By better on 2016/10/10 16:04.
     */
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Call<ResponseBody> contributorsBySimpleGetCall(@Path("type") String type, @Path("id") String id,
                                                   @Path("startPage") int startPage);
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<News> contributorsBySimpleGetCallRx(@Path("type") String type, @Path("id") String id,
                                                           @Path("startPage") int startPage);
}
