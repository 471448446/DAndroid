package better.hello.data;

import better.hello.http.call.RequestInfo;
import rx.Subscription;

/**
 * Des 数据按照toDoApp,官方是一套接口，同时写了两个实现，一个本地，一个远程，然后由一个TasksRepository来包装数据。
 * 这样虽然清晰，不过又需要些很多类。所以直接写在一起，自己内部去维护。
 * Create By better on 2016/10/26 14:00.
 */
public interface DataSourceAsync<T/*数据类型*/> {
    /*直接获取,内部处理本地还是远程。*/
    Subscription get(RequestInfo<T> info);

    /**
     * Des 联网取远程,数据回调返回给P。
     * Create By better on 2016/10/26 16:41.
     */
    Subscription asyncUrlInfo(RequestInfo<T> info);

    /**
     * Des 获取保存信息
     * Create By better on 2016/10/26 16:42.
     */
    Subscription getLocalInfo(RequestInfo<T> info);

    void save(T bean,RequestInfo<T> info);

//    void save(List<T> beans);


}
