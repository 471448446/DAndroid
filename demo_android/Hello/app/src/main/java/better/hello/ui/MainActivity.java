package better.hello.ui;

import android.os.Bundle;

import better.hello.R;
import better.hello.ui.base.BaseActivity;
import better.hello.ui.news.NewsTabFragment;

/**
 * Des http://stackoverflow.com/questions/36060883/how-to-implement-bottom-navigation-tab-as-per-the-google-new-guideline
 * 主页仿知乎，新闻是网页数据
 * Create By better on 2016/10/12 15:03.
 */
public class MainActivity extends BaseActivity implements MainContract.view {
    MainPresenter mainPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter=new MainPresenter(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_Content,new NewsTabFragment(),"tag")
                .commit();
    }

}
