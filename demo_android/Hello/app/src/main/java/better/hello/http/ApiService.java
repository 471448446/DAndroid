package better.hello.http;

import java.util.List;
import java.util.Map;

import better.hello.data.bean.NewsListBean;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Des 网络请求
 * Create By better on 2016/10/17 14:54.
 */
public interface ApiService {
    /**
    * Des 新闻列表
    * Create By better on 2016/10/17 15:11.
    */
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String,List<NewsListBean>>> asyncNewsList(@Path("type") String type, @Path("id") String id, @Path("startPage") int startPage);

}
