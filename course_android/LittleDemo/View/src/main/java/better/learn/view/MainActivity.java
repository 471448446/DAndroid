package better.learn.view;

import android.os.Bundle;
import android.view.View;

import better.learn.view.custom.bezier.BezierActivity;
import better.learn.view.custom.canvas.CanvasActivity;
import better.learn.view.useage.EditTextActivity;
import better.library.base.ui.BaseActivity;
import better.library.utils.ForWordUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_search:
                ForWordUtil.to(this, EditTextActivity.class);
                break;
            case R.id.main_btn_canvas:
                ForWordUtil.to(this, CanvasActivity.class);
                break;
            case R.id.custom_btn_bezier:
                ForWordUtil.to(this, BezierActivity.class);
                break;
        }
    }
}
