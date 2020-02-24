package com.better.learn.magnetictest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity2 extends Activity {
    private ImageView compass;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compass = (ImageView) findViewById(R.id.compass);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(sensorEventListener, orientationSensor, SensorManager.SENSOR_DELAY_GAME);

    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        float[] accelerometerValues = new float[3];
        float[] maneticValues = new float[3];
        private float lastDegree;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float direction = sensorEvent.values[sensorManager.DATA_X];
            //此角度值为从北顺时针0-360
            Log.i("Better", sensorEvent.sensor.getName() + " 角度：" + direction);
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
