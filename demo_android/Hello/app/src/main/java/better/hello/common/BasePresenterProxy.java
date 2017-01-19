package better.hello.common;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by better on 2016/10/18.
 * 这个类只是用来抽象常用Presenter操作
 */
public class BasePresenterProxy<T extends BaseView> implements BasePresenter {

    protected CompositeSubscription mSubscriptions;
    protected T mView;

    public BasePresenterProxy(T mView) {
        this.mView = mView;
        mSubscriptions =new CompositeSubscription();
    }

    @Override
    public void onDestroyAction() {
        mSubscriptions.clear();
        mView=null;
    }
}
