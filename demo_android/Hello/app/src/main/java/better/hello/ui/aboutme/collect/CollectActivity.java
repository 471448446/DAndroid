package better.hello.ui.aboutme.collect;

import android.os.Bundle;

import better.hello.R;
import better.hello.ui.base.BaseActivity;

public class CollectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        setBackToolBar(R.id.toolBar, R.string.collection);
    }
}
