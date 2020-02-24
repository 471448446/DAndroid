package com.better.learn.magnetictest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends Activity {
        private ImageView compass;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compass = (ImageView) findViewById(R.id.compass);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor AccelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(sensorEventListener, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorEventListener, AccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        float[] accelerometerValues = new float[3];
        float[] maneticValues = new float[3];
        private float lastDegree;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = sensorEvent.values.clone();
                Log.e("Better", "change TYPE_ACCELEROMETER");
            } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                maneticValues = sensorEvent.values.clone();
                Log.e("Better", "change TYPE_MAGNETIC_FIELD");
            }
            float[] R = new float[9];
            float[] values = new float[3];
            SensorManager.getRotationMatrix(R, null, accelerometerValues, maneticValues);
            SensorManager.getOrientation(R, values);
            float rotateDegree = -(float) Math.toDegrees(values[0]);
            if (Math.abs(rotateDegree - lastDegree) > 1) {
                RotateAnimation animation = new RotateAnimation(lastDegree, rotateDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setFillAfter(true);
                compass.startAnimation(animation);
                lastDegree = rotateDegree;
                Log.e("Better", "lastDegree?  rotateDegree?" + lastDegree + "," + rotateDegree);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onDestroy() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
        super.onDestroy();
    }

}
