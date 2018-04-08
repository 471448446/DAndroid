package better.learn.dagger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //old
//        mPresenter =new MainPresenter(this);
        //dagger  ,Component将Model管理的依赖注入到Activity中，实现Presenter的初始化
        DaggerMainPresenterComponent
                .builder()
                .mainPresenterModel(new MainPresenterModel(this))
                .build()
                .injectActivity(this);

        Log.d(this.getClass().getSimpleName(), String.valueOf(null == mPresenter) + ";" + mPresenter.getMsg());
    }
}
