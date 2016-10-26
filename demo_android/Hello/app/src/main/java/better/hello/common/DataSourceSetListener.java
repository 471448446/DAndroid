package better.hello.common;

import java.util.ArrayList;
import java.util.List;

import better.lib.http.RequestType;

/**
 * Created by better on 2016/10/18.
 * 列表数据请求后，展示操作
 */

public interface DataSourceSetListener<E/*data*/> {
    void postRequestSuccess(RequestType requestType, List<E> list, String requestMeg);
    void postRequestError(RequestType requestType, ArrayList<E> list, String requestMeg);
}
