package better.learn.dagger;

import javax.inject.Inject;

/**
 * Created by better on 2018/2/12 16:57.
 */

public class MainPresenter {

    MainActivity mMainActivity;

    //Error:(12, 12) 错误: Types may only contain one @Inject constructor.
    @Inject
    public MainPresenter(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

//    @Inject
//    public MainPresenter() {
//    }

    public String getMsg() {
        return "mMainActivity==null?" + String.valueOf(mMainActivity == null);
    }
}
