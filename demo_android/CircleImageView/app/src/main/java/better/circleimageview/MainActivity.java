package better.circleimageview;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import better.lib.circleimage.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private android.os.Handler mHander = new android.os.Handler();
    private CircleImageView mCircleImageView, mCircleImageView2, mCircleImageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleImageView = (CircleImageView) findViewById(R.id.circleImage);
        mCircleImageView2 = (CircleImageView) findViewById(R.id.circleImage2);
        mCircleImageView3 = (CircleImageView) findViewById(R.id.circleImage3);


        mHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCircleImageView.setImageResource(R.drawable.user1);
                mCircleImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.user1));

            }
        }, 3000);

        mCircleImageView3.setImageResource(R.drawable.user1);
    }
}
