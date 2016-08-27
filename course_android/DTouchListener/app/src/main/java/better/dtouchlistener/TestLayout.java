package better.dtouchlistener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Created by better on 16/8/23.
 */
public class TestLayout extends FrameLayout implements View.OnClickListener, View.OnTouchListener {
    public TestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        setOnTouchListener(this);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        l("dispatchTouchEvent()");
        return super.dispatchTouchEvent(event);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        l("onInterceptTouchEvent()");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        l("onTouchEvent");
        return super.onTouchEvent(event);
    }

    private void l(String msg){
        MainActivity.l("TestLayout:"+msg);
    }

    @Override
    public void onClick(View view) {
        l("onClick()");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        l("onTouch()");
        return false;
    }
}
