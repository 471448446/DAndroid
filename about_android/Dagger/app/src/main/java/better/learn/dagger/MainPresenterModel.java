package better.learn.dagger;

import dagger.Module;
import dagger.Provides;

/**
 * 管理提供的依赖，这里是Presenter
 * Created by better on 2018/2/12 16:59.
 */

@Module
public class MainPresenterModel {
    MainActivity mMainActivity;

    public MainPresenterModel(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Provides
    MainActivity provideMainActivity() {
        return mMainActivity;
    }

    @Provides
    public MainPresenter providerMainPresenter() {
        return new MainPresenter(provideMainActivity());
    }
}
