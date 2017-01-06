package better.hello.data.source;

import better.hello.data.bean.NewsListBean;

/**
 * Created by better on 2017/1/6.
 */

public interface NewsCollect {
    void delete(String key);

    void collect(NewsListBean bean);
}
