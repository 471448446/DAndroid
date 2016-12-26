package better.hello.ui.aboutme;


import android.view.View;

import better.hello.R;
import better.hello.ui.aboutme.collect.CollectActivity;
import better.hello.ui.base.BaseFragment;
import better.lib.utils.ForWord;
import butterknife.OnClick;

/**
 * Des 个人中心
 * Create By better on 2016/12/26 13:23.
 */
public class AboutMeFragment extends BaseFragment {

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_about_me;
    }

    @OnClick({R.id.aboutMe_collects})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aboutMe_collects:
                ForWord.to(mContext, CollectActivity.class);
                break;
        }
    }
}
