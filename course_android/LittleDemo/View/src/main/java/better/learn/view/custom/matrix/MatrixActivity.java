package better.learn.view.custom.matrix;

import android.os.Bundle;
import android.view.View;

import better.learn.view.R;
import better.library.base.ui.BaseActivity;
import better.library.utils.ForWordUtil;


public class MatrixActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.matrix_btn_basic:
                ForWordUtil.to(this, BasicMatrixActivity.class);
                break;
        }
    }
}
