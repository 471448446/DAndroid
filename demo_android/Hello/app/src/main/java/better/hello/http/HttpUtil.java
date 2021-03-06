package better.hello.http;

import java.util.List;
import java.util.Map;

import better.hello.common.BaseSchedulerTransformer;
import better.hello.data.bean.NetEasyImgBean;
import better.hello.data.bean.NewsDetailsBean;
import better.hello.data.bean.NetEaseNewsListBean;
import better.hello.data.bean.SplashZhiHuBean;
import better.hello.http.api.HostType;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by better on 2016/10/17.
 */

public class HttpUtil {
    /**
     * Des 新闻列表
     * Create By better on 2016/10/17 16:14.
     */
    public static Observable<Map<String, List<NetEaseNewsListBean>>> getNewsList(String type, final String id, int startPage) {
        return RetrofitHelper.getInstance(HostType.NETEASE_NEWS_VIDEO).getApiService().asyncNewsList(type, id, startPage)
                .compose(new BaseSchedulerTransformer<Map<String, List<NetEaseNewsListBean>>>());
    }

    /**
     * Des 新闻详情
     * Create By better on 2016/10/26 13:42.
     */
    public static Observable<Map<String, NewsDetailsBean>> getNewDetail(String postId) {
        return RetrofitHelper.getInstance(HostType.NETEASE_NEWS_VIDEO).getApiService().getNewsDetail(postId)
                .compose(new BaseSchedulerTransformer<Map<String, NewsDetailsBean>>());
    }

    /**
     * Des 图片详情
     * Create By better on 2017/1/12 10:03.
     */
    public static Observable<NetEasyImgBean> getNewImgDetails(String id) {
        return RetrofitHelper.getInstance(HostType.NETEASE_NEWS_VIDEO).getApiService().getNewsImageDetail(id)
                .compose(new BaseSchedulerTransformer<NetEasyImgBean>());
    }

    /**
     * Des 得到文件流
     * Create By better on 2016/10/28 09:28.
     */
    public static Observable<ResponseBody> downFile(String url) {
        return RetrofitHelper.getInstance(HostType.NETEASE_NEWS_VIDEO).getApiService().getFileStream(url);
//                .compose(new BaseSchedulerTransformer<ResponseBody>());
    }

    /**
     * Des 下载欢迎页图片
     * Create By better on 2016/10/28 09:28.
     */
    public static Observable<SplashZhiHuBean> getSplashBean(String url) {
        return RetrofitHelper.getInstance(HostType.NETEASE_NEWS_VIDEO).getApiService().getSplashBean(url)
                .compose(new BaseSchedulerTransformer<SplashZhiHuBean>());
    }
}
