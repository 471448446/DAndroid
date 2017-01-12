package better.hello.ui.news.detail;

import better.hello.common.BasePresenter;
import better.hello.common.BaseView;
import better.hello.data.bean.NetEasyImgBean;
import better.lib.waitpolicy.WaitPolicy;

/**
 * Created by better on 2017/1/12.
 */

public interface NewsPhotoDetailContract {
    interface presenter extends BasePresenter {
        void asyncPhoto(String id);

    }

    interface view extends BaseView {
        void showPhoto(NetEasyImgBean bean);
        void showPhotoError(String error);
        WaitPolicy getWait();
    }
}
