package com.better.learn.teststart.night;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.better.learn.teststart.R;

public class NightActivity extends AppCompatActivity {
    public Bitmap for_whiteBitnao;
    public Bitmap if_blackBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night);
        this.if_blackBitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_4444);
        this.for_whiteBitnao = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_4444);
        new Canvas(this.if_blackBitmap).drawColor(0xFF000000); // black
        new Canvas(this.for_whiteBitnao).drawColor(-1); // white
        ImageView rn = findViewById(R.id.rn_b);
        rn.setImageBitmap(if_blackBitmap);
        rn = findViewById(R.id.rn_w);
        rn.setImageBitmap(for_whiteBitnao);
        rn = findViewById(R.id.rn);
        boolean nightModel = Utils.nightModel(this);
        Log.i("Better", "nightModel:" + nightModel);
        if (nightModel) {
            rn.setImageBitmap(for_whiteBitnao);
        } else {
            rn.setImageBitmap(if_blackBitmap);
        }
    }
}