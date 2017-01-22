package better.hello.ui.news.channle;

import java.util.List;

import better.hello.common.BasePresenter;
import better.hello.common.BaseView;
import better.hello.data.bean.NewsChannelBean;
import better.lib.waitpolicy.WaitPolicy;

/**
 * Created by better on 2017/1/20.
 */

public interface ChannelContract {
    interface presenter extends BasePresenter {
        void showTopChannel();

        void showBottomChannel();

        void save(List<NewsChannelBean> list);

        void preSave();
    }

    interface view extends BaseView {
        void showTopChannel(List<NewsChannelBean> list);

        void showBottomChannel(List<NewsChannelBean> list);

        void onClickBottomItem(NewsChannelBean bean, int p);

        void onDeleteTopItem(NewsChannelBean bean, int p);

        void onClickTopItem(int p);

        WaitPolicy getWait();
    }
}
