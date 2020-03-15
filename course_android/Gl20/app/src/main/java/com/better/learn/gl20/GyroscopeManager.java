package com.better.learn.gl20;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.kircherelectronics.fsensor.filter.gyroscope.OrientationGyroscope;

import org.apache.commons.math3.complex.Quaternion;

/**
 * https://developer.android.com/guide/topics/sensors/sensors_motion?hl=zh-cn
 * https://developer.android.com/reference/android/hardware/SensorEvent.html#values
 * Created by better on 2020/3/15 4:50 PM.
 */
public class GyroscopeManager implements SensorEventListener {
    private static final float NS2S = 1.0f / 1000000000.0f;
    private static final float EPSILON = 0.000000001f;

    private final float[] deltaRotationVector = new float[4];

    private double MAX_ANGLE = Math.PI / 2;

    private SensorManager sensorManager;
    private Sensor sensor;

    private long timestamp;
    private double xAngle, yAngle, zAngle;
    private OnRotateChange onRotateChange;
    private float[] rotation = new float[3];


    private OrientationGyroscope orientationGyroscope = new OrientationGyroscope();


    public GyroscopeManager(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public void setOnRotateChange(OnRotateChange onRotateChange) {
        this.onRotateChange = onRotateChange;
    }

    public void onResume() {
        if (null == sensorManager) {
            return;
        }
        if (null == sensor) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            timestamp = 0;
        }
        //灵敏度从快到慢 可选择: SENSOR_DELAY_FASTEST; SENSOR_DELAY_GAME; SENSOR_DELAY_NORMAL; SENSOR_DELAY_UI
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onPause() {
        if (null == sensorManager) {
            return;
        }
        sensorManager.unregisterListener(this, sensor);
        sensor = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_GYROSCOPE) {
            return;
        }
        if (!orientationGyroscope.isBaseOrientationSet()) {
            orientationGyroscope.setBaseOrientation(Quaternion.IDENTITY);
        } else {
            // Android reuses events, so you probably want a copy
            System.arraycopy(event.values, 0, rotation, 0, event.values.length);

            float[] fusedOrientation = orientationGyroscope.calculateOrientation(rotation, event.timestamp);

            if (null != onRotateChange) {
                onRotateChange.onRotateChanged(mapTo(fusedOrientation[0]), mapTo(fusedOrientation[1]), mapTo(fusedOrientation[2]));
            }
        }
        /*if (timestamp > 0) {
            float dt = (event.timestamp - timestamp) * NS2S * 2.0f;
            double x = event.values[0] * dt;
            double y = event.values[1] * dt;
            double z = event.values[2] * dt;
            xAngle += x;
            yAngle += y;
            zAngle += z;
            if (xAngle > MAX_ANGLE) {
                xAngle = MAX_ANGLE;
            }
            if (xAngle < -MAX_ANGLE) {
                xAngle = -MAX_ANGLE;
            }
            if (yAngle > MAX_ANGLE) {
                yAngle = MAX_ANGLE;
            }
            if (yAngle < -MAX_ANGLE) {
                yAngle = -MAX_ANGLE;
            }
            if (zAngle > MAX_ANGLE) {
                zAngle = MAX_ANGLE;
            }
            if (zAngle < -MAX_ANGLE) {
                zAngle = -MAX_ANGLE;
            }
            x /= MAX_ANGLE;
            y /= MAX_ANGLE;
            z /= MAX_ANGLE;

            if (null != onRotateChange) {
                onRotateChange.onRotateChanged(mapTo(x), mapTo(y), mapTo(z));
            }
        }*/
        timestamp = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private float mapTo(double number) {
        try {
            @SuppressLint("DefaultLocale") String format = String.format("%.1f", Math.toDegrees((number + 360) % 360));
            return Float.parseFloat(format);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return 0;
    }

    public interface OnRotateChange {
        void onRotateChanged(double x, double y, double z);
    }
}
