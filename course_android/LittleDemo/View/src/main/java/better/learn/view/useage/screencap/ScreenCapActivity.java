package better.learn.view.useage.screencap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import better.learn.view.R;
import better.library.base.ui.BaseActivity;

public class ScreenCapActivity extends BaseActivity {

    Button mView;
    ScrollView mScrollView;

    private MediaHelper mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_cap);
        mView = (Button) findViewById(R.id.screenCap_View);
        mScrollView = (ScrollView) findViewById(R.id.screenCap_root);
        mManager = new MediaHelper(this, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.screenCap_View:
                mView.setDrawingCacheEnabled(true);
                mView.buildDrawingCache();
                ScreenCapHelper.saveBitmap(this, view.getDrawingCache(), new ScreenCapHelper.ISaveBitmap() {
                    @Override
                    public void onFinish() {
                        mView.setDrawingCacheEnabled(false);
                        mView.destroyDrawingCache();
                    }
                });
                break;
            case R.id.screenCap_activity:
                final View viewA = getWindow().getDecorView();
                viewA.setDrawingCacheEnabled(true);
                viewA.buildDrawingCache();
                ScreenCapHelper.saveBitmap(this, viewA.getDrawingCache(), new ScreenCapHelper.ISaveBitmap() {
                    @Override
                    public void onFinish() {
                        viewA.setDrawingCacheEnabled(false);
                        viewA.destroyDrawingCache();
                    }
                });
                break;
            case R.id.screenCap_ScrollView:
                ScreenCapHelper.saveBitmap(this, ScreenCapHelper.getScrollViewBitmap(mScrollView), null);
                break;
            case R.id.screenCap_Media:
                mManager.prepareMediaScreenCap(this, 100);
                break;
        }
    }
}
