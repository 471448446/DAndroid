package better.dviewdraghelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * https://github.com/umano/AndroidSlidingUpPanel/blob/master/library/src/main/java/com/sothree/slidinguppanel/SlidingUpPanelLayout.java
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.txt1).setOnClickListener(this);
        findViewById(R.id.txt1).setOnTouchListener(this);
//        findViewById(R.id.txt2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt1:
                startActivity(new Intent(MainActivity.this,YouTubeActivity.class));
                break;
        }
    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        l("onTouch()");
        return false;
    }
    private void l(String msg){
        Log.d("Better","Main,"+msg);
    }
}
