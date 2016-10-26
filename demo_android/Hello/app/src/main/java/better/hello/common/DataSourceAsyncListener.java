package better.hello.common;

import better.lib.http.RequestType;

/**
 * Created by better on 2016/10/18.
 * 列表数据请求操作,至于具体的请求数据(url,参数)，需要Presenter来提供
 */
public interface DataSourceAsyncListener {
    void asyncList(RequestType type);
}
