package better.demo.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import better.demo.view.custom.CustomViewActivity;
import better.demo.view.useage.EditTextActivity;
import better.utils.ForWordUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editText_search:
                ForWordUtil.to(this, EditTextActivity.class);
                break;
            case R.id.editText_bezier:
                ForWordUtil.to(this, CustomViewActivity.class);
                break;
        }
    }

}
