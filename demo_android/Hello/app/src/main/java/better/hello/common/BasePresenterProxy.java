package better.hello.common;

import rx.Subscription;

/**
 * Created by better on 2016/10/18.
 * 这个类只是用来抽象常用Presenter操作
 */
public class BasePresenterProxy<T extends BaseView> implements BasePresenter {

    protected Subscription mSubscribe;
    protected T mView;

    public BasePresenterProxy(T mView) {
        this.mView = mView;
    }

    @Override
    public void onDestroyAction() {
        if (null!= mSubscribe) mSubscribe.unsubscribe();
        mView=null;
    }
}
