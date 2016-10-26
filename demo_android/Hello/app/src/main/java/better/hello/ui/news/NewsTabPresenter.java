package better.hello.ui.news;

import better.hello.common.BasePresenterProxy;

/**
 * Created by better on 2016/10/19.
 */

public class NewsTabPresenter extends BasePresenterProxy<NewsTabFragment> implements NewsTabContract.presenter {
    public NewsTabPresenter(NewsTabFragment mView) {
        super(mView);
    }
}
