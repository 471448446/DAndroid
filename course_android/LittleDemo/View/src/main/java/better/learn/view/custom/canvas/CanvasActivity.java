package better.learn.view.custom.canvas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import better.learn.view.R;
import better.learn.view.custom.canvas.compose.path.HollowActivity;
import better.learn.view.custom.canvas.compose.path.HollowActivity2;
import better.learn.view.custom.canvas.compose.shader.ComposeShaderActivity;
import better.learn.view.custom.canvas.compose.xfermode.CanvasXFermodeBitmapActivity;
import better.learn.view.custom.canvas.compose.xfermode.SampleActivity;
import better.learn.view.custom.canvas.view.SwipeBitmapActivity;
import better.learn.view.custom.canvas.compose.xfermode.CanvasXFermodeActivity;
import better.library.base.ui.BaseActivity;
import better.library.utils.ForWordUtil;

/**
 * Canvas常用方法：
 * 1、基础的操作transLate，scale，rotate。skew
 * 2、drawXXX等绘制方法，画基本形状，画图片，画文字
 * 3、clipXXX等裁剪方法，
 * 4、还有就是saveXX和restoreXX
 * Create By better on 2017/9/25 15:40.
 */
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
            case R.id.canvas_btn_PathClip:
                ForWordUtil.to(this, ClipPathActivity.class);
                break;
            case R.id.canvas_btn_circle:
                ForWordUtil.to(this, RoundImageActivity.class);
                break;
            case R.id.canvas_btn_swipe:
                ForWordUtil.to(this, SwipeBitmapActivity.class);
                break;
            case R.id.canvas_compose_xfermode:
                startActivities(
                        new Intent[]{
                                new Intent(this, CanvasXFermodeActivity.class),
                                new Intent(this, CanvasXFermodeBitmapActivity.class),
                                new Intent(this, SampleActivity.class),
                        });
                break;
            case R.id.canvas_compose_path:
                startActivities(
                        new Intent[]{
                                new Intent(this, HollowActivity.class),
                                new Intent(this, HollowActivity2.class),
                        });
                break;
                case R.id.canvas_btn_compose_shader:
                startActivities(
                        new Intent[]{
                                new Intent(this, ComposeShaderActivity.class),
                        });
                break;
        }
    }
}
