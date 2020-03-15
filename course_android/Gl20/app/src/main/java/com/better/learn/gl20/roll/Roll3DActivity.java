package com.better.learn.gl20.roll;

import android.os.Bundle;
import android.util.Log;

import com.better.learn.gl20.GyroscopeManager;
import com.better.learn.gl20.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by better on 2020/3/15 9:10 AM.
 */
public class Roll3DActivity extends AppCompatActivity implements GyroscopeManager.OnRotateChange {

    ItemView itemView1, itemView2;
    private GyroscopeManager gyroscopeManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rool_3d);
        itemView1 = findViewById(R.id.roll_1);
        itemView2 = findViewById(R.id.roll_2);
        itemView1.getRoll3DView().setRollMode(Roll3DView.RollMode.Whole3D);
        itemView2.getRoll3DView().setRollMode(Roll3DView.RollMode.RollInTurn);
        itemView2.getRoll3DView().setPartNumber(9);
        gyroscopeManager = new GyroscopeManager(this);
        gyroscopeManager.setOnRotateChange(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gyroscopeManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroscopeManager.onPause();
    }

    private double lastX, lastY;

    @Override
    public void onRotateChanged(double x, double y, double z) {
//        Log.e("Better", "onSensorChanged x? y? z? " + x + "," + y + "," + z);

        double deltaX = x - lastX;
        double deltaY = y - lastY;

        Log.e("Better", "onSensorChanged deltaX? deltaX? " + deltaX + "," + deltaY);

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (Math.abs(deltaX) > 0.5) {
                if (deltaX >= 0) {
                    itemView1.rollTo(ItemView.Roll.right);
                    itemView2.rollTo(ItemView.Roll.right);
                } else {
                    itemView1.rollTo(ItemView.Roll.left);
                    itemView2.rollTo(ItemView.Roll.left);
                }
            }
        } else {
            if (Math.abs(deltaY) > 0.5) {
                if (deltaY >= 0) {
                    itemView1.rollTo(ItemView.Roll.top);
                    itemView2.rollTo(ItemView.Roll.top);
                } else {
                    itemView1.rollTo(ItemView.Roll.bottom);
                    itemView2.rollTo(ItemView.Roll.bottom);
                }
            }
        }
        lastX = x;
        lastY = y;
    }
}
