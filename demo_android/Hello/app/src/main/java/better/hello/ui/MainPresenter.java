package better.hello.ui;

import java.io.IOException;

import better.hello.common.BasePresenterProxy;
import better.hello.data.bean.SplashZhiHuBean;
import better.hello.http.HttpUtil;
import better.hello.http.api.Api;
import better.hello.util.FileUtils;
import better.hello.util.Utils;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by better on 2016/10/18.
 */

public class MainPresenter extends BasePresenterProxy<MainActivity> implements MainContract.presenter {
    public MainPresenter(MainActivity mView) {
        super(mView);
    }

    @Override
    public void asyncPlashImage() {
        if (FileUtils.isLoadTodaySplash(mView)) {
            return;
        }
        HttpUtil.getSplashBean(Api.SPLASH_IMG).subscribe(new Action1<SplashZhiHuBean>() {
            @Override
            public void call(SplashZhiHuBean splashZhihuBean) {
                down(splashZhihuBean.getImg());
            }
        });
//        down("https://pic2.zhimg.com/v2-c2dba330e3513fabe71a15e512ad2bbd.jpg");

    }

    private void down(String path) {
        HttpUtil.getImage(path).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).map(new Func1<ResponseBody, Boolean>() {
            @Override
            public Boolean call(ResponseBody inputStream) {
                try {
                    FileUtils.writeSplash(mView, inputStream.byteStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage(), e);
                }
                return FileUtils.isLoadTodaySplash(mView);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Utils.d("Better", "首页图片eror=" + e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Utils.toastShort(mView, "首页图片准备好了"+String.valueOf(aBoolean));
                Utils.d("Better", "首页图片 首页图片准备好了");
            }
        });
    }
}
