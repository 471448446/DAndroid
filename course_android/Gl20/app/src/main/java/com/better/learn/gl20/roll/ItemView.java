package com.better.learn.gl20.roll;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.better.learn.gl20.R;


public class ItemView extends LinearLayout {
    public interface Roll {
        int left = 0;
        int right = 1;
        int top = 2;
        int bottom = 3;
    }

    private Context context;

    private Roll3DView roll3DView;
    private BitmapDrawable bgDrawable1, bgDrawable2, bgDrawable3, bgDrawable4, bgDrawable5;

    public ItemView(Context context) {
        super(context);
        init(context);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View.inflate(context, R.layout.demo_item, this);
        roll3DView = (Roll3DView) findViewById(R.id.three_d_view);

        bgDrawable1 = (BitmapDrawable) getResources().getDrawable(R.drawable.img1);
        bgDrawable2 = (BitmapDrawable) getResources().getDrawable(R.drawable.img2);
        bgDrawable3 = (BitmapDrawable) getResources().getDrawable(R.drawable.img3);
        bgDrawable4 = (BitmapDrawable) getResources().getDrawable(R.drawable.img4);
        bgDrawable5 = (BitmapDrawable) getResources().getDrawable(R.drawable.img5);


        Bitmap bitmap1 = bgDrawable1.getBitmap();
        Bitmap bitmap2 = bgDrawable2.getBitmap();
        Bitmap bitmap3 = bgDrawable3.getBitmap();
        Bitmap bitmap4 = bgDrawable4.getBitmap();
        Bitmap bitmap5 = bgDrawable5.getBitmap();

        roll3DView.addImageBitmap(bitmap1);
        roll3DView.addImageBitmap(bitmap2);
        roll3DView.addImageBitmap(bitmap3);
        roll3DView.addImageBitmap(bitmap4);
        roll3DView.addImageBitmap(bitmap5);

        roll3DView.setRollMode(Roll3DView.RollMode.Whole3D);
    }

    public void rollTo(int roll) {
        switch (roll) {
            case Roll.left:
                roll3DView.setRollDirection(0);
                roll3DView.toPre();
                break;
            case Roll.right:
                roll3DView.setRollDirection(0);
                roll3DView.toNext();
                break;
            case Roll.top:
                roll3DView.setRollDirection(1);
                roll3DView.toPre();
                break;
            case Roll.bottom:
                roll3DView.setRollDirection(1);
                roll3DView.toNext();
                break;
        }
    }

    public Roll3DView getRoll3DView() {
        return roll3DView;
    }
}
