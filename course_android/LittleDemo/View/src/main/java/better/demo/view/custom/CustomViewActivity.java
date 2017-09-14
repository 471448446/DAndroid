package better.demo.view.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import better.demo.view.R;
import better.utils.ForWordUtil;

/**
 * https://github.com/GcsSloop/AndroidNote/blob/master/CustomView/Advance/%5B06%5DPath_Bezier.md
 * http://www.html-js.com/article/1628
 * Create By better on 2017/9/14 15:37.
 */
public class CustomViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
    }

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
