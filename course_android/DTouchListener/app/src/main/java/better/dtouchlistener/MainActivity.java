package better.dtouchlistener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 点击图片:
 * 08-24 15:27:58.341 12040-12040/better.dtouchlistener D/Better: MainActivity:dispatchTouchEvent()
 * 08-24 15:27:58.341 12040-12040/better.dtouchlistener D/Better: TestLayout:dispatchTouchEvent()
 * 08-24 15:27:58.341 12040-12040/better.dtouchlistener D/Better: TestLayout:onInterceptTouchEvent()
 * 08-24 15:27:58.341 12040-12040/better.dtouchlistener D/Better: TestImage:dispatchTouchEvent()
 * 08-24 15:27:58.341 12040-12040/better.dtouchlistener D/Better: TestImage:onTouch()
 * 08-24 15:27:58.471 12040-12040/better.dtouchlistener D/Better: MainActivity:dispatchTouchEvent()
 * 08-24 15:27:58.471 12040-12040/better.dtouchlistener D/Better: TestLayout:dispatchTouchEvent()
 * 08-24 15:27:58.471 12040-12040/better.dtouchlistener D/Better: TestLayout:onInterceptTouchEvent()
 * 08-24 15:27:58.471 12040-12040/better.dtouchlistener D/Better: TestImage:dispatchTouchEvent()
 * 08-24 15:27:58.471 12040-12040/better.dtouchlistener D/Better: TestImage:onTouch()
 * 点击布局:
 * 08-24 15:39:07.061 12040-12040/better.dtouchlistener D/Better: MainActivity:dispatchTouchEvent()
 * 08-24 15:39:07.061 12040-12040/better.dtouchlistener D/Better: TestLayout:dispatchTouchEvent()
 * 08-24 15:39:07.061 12040-12040/better.dtouchlistener D/Better: TestLayout:onInterceptTouchEvent()
 * 08-24 15:39:07.061 12040-12040/better.dtouchlistener D/Better: TestLayout:onTouch()
 * 08-24 15:39:07.061 12040-12040/better.dtouchlistener D/Better: TestLayout:onTouchEvent
 * 08-24 15:39:07.091 12040-12040/better.dtouchlistener D/Better: MainActivity:dispatchTouchEvent()
 * 08-24 15:39:07.091 12040-12040/better.dtouchlistener D/Better: TestLayout:dispatchTouchEvent()
 * 08-24 15:39:07.091 12040-12040/better.dtouchlistener D/Better: TestLayout:onTouch()
 * 08-24 15:39:07.091 12040-12040/better.dtouchlistener D/Better: TestLayout:onTouchEvent
 * 08-24 15:39:07.091 12040-12040/better.dtouchlistener D/Better: TestLayout:onClick()
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static void l(String msg) {
        Log.d("Better", msg);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        l("MainActivity:dispatchTouchEvent()");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        l("MainActivity:onTouchEvent()");
        return super.onTouchEvent(event);
    }
}
