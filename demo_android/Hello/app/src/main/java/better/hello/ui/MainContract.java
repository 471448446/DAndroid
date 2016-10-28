package better.hello.ui;

import better.hello.common.BasePresenter;
import better.hello.common.BaseView;

/**
 * Created by better on 2016/10/18.
 */

public interface MainContract {
    interface presenter extends BasePresenter {
        void asyncPlashImage();
    }

    interface view extends BaseView {
    }
}
