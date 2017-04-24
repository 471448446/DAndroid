package better.matrixdemo;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MatrixImageView mMatrixImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMatrixImageView = (MatrixImageView) findViewById(R.id.matrix);
        findViewById(R.id.trans).setOnClickListener(this);
        findViewById(R.id.recover).setOnClickListener(this);
        findViewById(R.id.roate).setOnClickListener(this);
        findViewById(R.id.scale).setOnClickListener(this);
        findViewById(R.id.skew).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trans:
                Matrix matrix = new Matrix();
                matrix.postTranslate(mMatrixImageView.getBitmap().getWidth(), mMatrixImageView.getBitmap().getHeight());
                mMatrixImageView.setImageMatrix(matrix);
                logH(matrix);
                break;
            case R.id.recover:
                Matrix matrix1 = new Matrix();
                mMatrixImageView.setImageMatrix(matrix1);
                logH(matrix1);
                break;
            case R.id.roate:
                Matrix matrix2 = new Matrix();
//                matrix2.postRotate(90f);
                matrix2.postTranslate(mMatrixImageView.getBitmap().getWidth(), mMatrixImageView.getBitmap().getHeight());

                matrix2.postRotate(60f, mMatrixImageView.getBitmap().getWidth()*3/2, mMatrixImageView.getBitmap().getHeight()*3/2);
                mMatrixImageView.setImageMatrix(matrix2);
                logH(matrix2);
                break;
            case R.id.scale:
                Matrix matrix3 = new Matrix();
                matrix3.postTranslate(mMatrixImageView.getBitmap().getWidth(), mMatrixImageView.getBitmap().getHeight());

                matrix3.postScale(2, 2);
                mMatrixImageView.setImageMatrix(matrix3);
                logH(matrix3);
                break;
            case R.id.skew:
                Matrix matrix4 = new Matrix();
                matrix4.postTranslate(mMatrixImageView.getBitmap().getWidth(), mMatrixImageView.getBitmap().getHeight());

                matrix4.postSkew(0.5f,0.5f);
                mMatrixImageView.setImageMatrix(matrix4);
                logH(matrix4);
                break;
        }
    }

    private void logH(Matrix matrix3) {
        float[] arrays = new float[9];
        matrix3.getValues(arrays);
        for (int i = 0; i < 3; ++i) {
            String temp = new String();
            for (int j = 0; j < 3; ++j) {
                temp += arrays[3 * i + j] + "\t";
            }
            Log.d("Main", temp);
        }
    }
}
