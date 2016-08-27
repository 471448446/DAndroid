package better.dtouchlistener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by better on 16/8/23.
 */
public class TestImage extends ImageView implements View.OnClickListener, View.OnTouchListener {
    public TestImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        setOnTouchListener(this);
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                l("hhh");
//            }
//        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        l("dispatchTouchEvent()");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        l("onTouchEvent");
        return super.onTouchEvent(event);
    }

    private void l(String msg){
        MainActivity.l("TestImage:"+msg);
    }

    @Override
    public void onClick(View view) {
        l("onClick()");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        l("onTouch()");
        return true;
    }
}
