package better.hello.ui.base;

import android.os.Bundle;
import android.view.View;

import better.hello.R;

/**
 * Created by Better on 2016/3/15.
 * 提供一个简单的tabLayout+viewPager
 */
public abstract class SimpleTabSlideFragment extends BaseTabSlideFragment {

    @Override
    protected int getRootViewId() {
        return R.layout.layout_simple_tab_sidle;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initTabSlide(R.id.simpleTabSlide_tabLayout,R.id.simpleTabSlide_Pager);
    }
}
