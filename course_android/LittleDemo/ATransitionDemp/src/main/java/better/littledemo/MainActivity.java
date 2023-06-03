package better.littledemo;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView image, image2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.imageTest);
        image2 = (ImageView) findViewById(R.id.imageTest2);
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.parseColor("#303F9F")), new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.smile))});
        transitionDrawable.startTransition(1000);
        TransitionDrawable transitionDrawable2 = (TransitionDrawable) ContextCompat.getDrawable(this, R.drawable.smile_drawable);
        transitionDrawable2.startTransition(2000);

        image.setImageDrawable(transitionDrawable);

        image2.setImageDrawable(transitionDrawable2);
    }
}
