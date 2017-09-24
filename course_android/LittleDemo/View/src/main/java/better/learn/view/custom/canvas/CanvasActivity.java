package better.learn.view.custom.canvas;

import android.os.Bundle;
import android.view.View;

import better.learn.view.R;
import better.library.base.ui.BaseActivity;
import better.library.utils.ForWordUtil;

public class CanvasActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.canvas_btn_translate:
                ForWordUtil.to(this, TranslateActivity.class);
                break;
            case R.id.canvas_btn_scale:
                ForWordUtil.to(this, ScaleActivity.class);
                break;
            case R.id.canvas_btn_rotate:
                ForWordUtil.to(this, RotateActivity.class);
                break;
            case R.id.canvas_btn_skew:
                ForWordUtil.to(this, SkewActivity.class);
                break;
            case R.id.canvas_btn_graphicsBasic:
                ForWordUtil.to(this, GraphicsBasicActivity.class);
                break;
            case R.id.canvas_btn_Bitmap:
                ForWordUtil.to(this, BitmapActivity.class);
                break;
            case R.id.canvas_btn_Text:
                ForWordUtil.to(this, TextActivity.class);
                break;
            case R.id.canvas_btn_Path:
                ForWordUtil.to(this, PathActivity.class);
                break;
        }
    }
}
