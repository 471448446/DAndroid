package better.learn.view.custom.canvas;

import android.os.Bundle;
import android.view.View;

import better.learn.view.R;
import better.learn.view.custom.bezier.BezierActivity;
import better.learn.view.custom.canvas.demo.SpiderActivity;
import better.library.base.ui.BaseActivity;
import better.library.utils.ForWordUtil;

public class PathActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.path_btn_graphics:
                ForWordUtil.to(this, PathGraphicsActivity.class);
                break;
            case R.id.path_btn_bezier:
                ForWordUtil.to(this, BezierActivity.class);
                break;
            case R.id.path_btn_spider:
                ForWordUtil.to(this, SpiderActivity.class);
                break;

        }
    }
}
