package better.learn.dagger;

import dagger.Component;

/**
 * Created by better on 2018/2/12 17:00.
 */

@Component(modules = MainPresenterModel.class)
public interface MainPresenterComponent {
    //Error:(12, 19) 错误: Members injection methods may only return the injected type or void.
    MainActivity injectActivity(MainActivity activity);
//    MainPresenter injectActivity();
}
