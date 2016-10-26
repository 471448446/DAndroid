package better.hello.ui;

import better.hello.common.BasePresenterProxy;

/**
 * Created by better on 2016/10/18.
 */

public class MainPresenter extends BasePresenterProxy<MainActivity> implements MainContract.presenter {

    public MainPresenter(MainActivity mView) {
        super(mView);
    }
}
