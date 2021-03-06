package better.hello.http;

import java.util.List;
import java.util.Map;

import better.hello.data.bean.NetEasyImgBean;
import better.hello.data.bean.NewsDetailsBean;
import better.hello.data.bean.NetEaseNewsListBean;
import better.hello.data.bean.SplashZhiHuBean;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Des 网络请求
 * Create By better on 2016/10/17 14:54.
 */
public interface ApiService {
    /**
     * Des 新闻列表
     * https://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
     * Create By better on 2016/10/17 15:11.
     */
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NetEaseNewsListBean>>> asyncNewsList(@Path("type") String type, @Path("id") String id, @Path("startPage") int startPage);

    /**
     * Des 新闻详情
     * https://c.m.163.com/nc/article/C3NRQ1J8000187VE/full.html
     * Create By better on 2016/10/19 13:30.
     */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("nc/article/{postId}/full.html")
    Observable<Map<String, NewsDetailsBean>> getNewsDetail(@Path("postId") String postId);

    /**
     * Des Banner图片详情
     * http://c.m.163.com/photo/api/set/0001/2227344.json
     * Create By better on 2017/1/12 10:03.
     */
    @GET("photo/api/set/0001/{id}.json")
    Observable<NetEasyImgBean> getNewsImageDetail(@Path("id") String postId);

    @GET
    Observable<SplashZhiHuBean> getSplashBean(@Url String url);

    @GET
    @Streaming
    @Headers("Cache-Control: public, max-age=60")
    Observable<ResponseBody> getFileStream(@Url String url);
}
