package better.learn.view.custom.canvas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import better.learn.view.R;

public class TranslateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        findViewById(R.id.canvas_view_translate).invalidate();
    }

}
