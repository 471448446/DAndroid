package better.hello.http;

import java.util.List;
import java.util.Map;

import better.hello.data.bean.NewsDetailsBean;
import better.hello.data.bean.NewsListBean;
import better.hello.data.bean.SplashZhiHuBean;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
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
    Observable<Map<String, List<NewsListBean>>> asyncNewsList(@Path("type") String type, @Path("id") String id, @Path("startPage") int startPage);

    /**
     * Des 新闻详情
     * https://c.m.163.com/nc/article/C3NRQ1J8000187VE/full.html
     * Create By better on 2016/10/19 13:30.
     */
    @GET("nc/article/{postId}/full.html")
    Observable<Map<String, NewsDetailsBean>> getNewsDetail(@Path("postId") String postId);

    @GET
    Observable<SplashZhiHuBean> getSplashBean(@Url String url);

    @GET
    @Streaming
    Observable<ResponseBody> getFileStream(@Url String url);
}
