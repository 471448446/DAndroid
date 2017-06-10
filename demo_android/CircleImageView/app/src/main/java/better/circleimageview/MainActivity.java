package better.circleimageview;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import better.lib.circleimage.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private android.os.Handler mHander = new android.os.Handler();
    private CircleImageView mCircleImageView, mCircleImageView2, mCircleImageView3, mCircleImageView4;

    /* 高=宽  --better 2017/5/15 10:08. */
    // String url = "http://img2.woyaogexing.com/2017/05/12/ce9319c2463dbbd8!200x200.jpg";
    /* 高>宽  --better 2017/5/15 10:07. */
    String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494824051756&di=a6b081560c72f211a40ccc1c27829992&imgtype=0&src=http%3A%2F%2Fd.5857.com%2Fsjw_150713%2F001.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleImageView = (CircleImageView) findViewById(R.id.circleImage);
        mCircleImageView2 = (CircleImageView) findViewById(R.id.circleImage2);
        mCircleImageView3 = (CircleImageView) findViewById(R.id.circleImage3);
        mCircleImageView4 = (CircleImageView) findViewById(R.id.circleImage4);


        mHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCircleImageView.setImageResource(R.drawable.user1);
                mCircleImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.user1));

            }
        }, 3000);

        mCircleImageView3.setImageResource(R.drawable.user);
        Glide.with(this)
                .load(url)
                .asBitmap()
//                .dontAnimate()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mCircleImageView4);
    }
}
