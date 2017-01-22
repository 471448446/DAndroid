package better.hello.ui.news;

import java.util.List;

import better.hello.common.BasePresenter;
import better.hello.common.BaseView;
import better.hello.data.bean.NewsChannelBean;
import better.lib.waitpolicy.WaitPolicy;

/**
 * Created by better on 2016/10/19.
 */

public class NewsTabContract {
    interface presenter extends BasePresenter {
    }

    interface view extends BaseView {

        void setChannel(List<NewsChannelBean> data,boolean updata);

        void upDataChannel(int position);

        WaitPolicy getWait();
    }
}
