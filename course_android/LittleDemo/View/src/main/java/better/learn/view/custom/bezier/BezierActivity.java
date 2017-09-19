package better.learn.view.custom.bezier;

import android.os.Bundle;
import android.view.View;

import better.learn.view.R;
import better.library.base.ui.BaseActivity;
import better.library.utils.ForWordUtil;

/**
 * https://github.com/GcsSloop/AndroidNote/blob/master/CustomView/Advance/%5B06%5DPath_Bezier.md
 * http://www.html-js.com/article/1628
 * http://www.jianshu.com/p/55c721887568
 * Create By better on 2017/9/14 15:37.
 */
public class BezierActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bezier_two:
                ForWordUtil.to(this, BezierTwoOrderActivity.class);
                break;
            case R.id.bezier_three:
                ForWordUtil.to(this, BezierThreeOrderActivity.class);
                break;
            case R.id.bezier_heart:
                ForWordUtil.to(this, BezierHeartActivity.class);
                break;
        }
    }
}
