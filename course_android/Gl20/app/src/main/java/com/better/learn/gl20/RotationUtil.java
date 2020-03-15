//package com.better.learn.gl20;
//
//import java.util.Arrays;
//
///**
// * Created by better on 2020/3/15 5:03 PM.
// */
//public class RotationUtil {
//
//    /**
//     * Calculates a rotation vector from the gyroscope angular speed values.
//     * <p>
//     * http://developer.android.com/reference/android/hardware/SensorEvent.html#values
//     *
//     * @param previousRotationVector the last known orientation to which the new rotation will be applied.
//     * @param rateOfRotation         the rotation measurement
//     * @param dt                     the period of time over which the rotation measurement took place in units of seconds
//     * @param epsilon                minimum rotation vector magnitude required to get the axis for normalization
//     * @return A Quaternion representing the orientation.
//     */
//    public static float[] integrateGyroscopeRotation(Quaternion previousRotationVector, float[] rateOfRotation, float dt, float epsilon) {
//        // Calculate the angular speed of the sample
//        float magnitude = (float) Math.sqrt(Math.pow(rateOfRotation[0], 2)
//                + Math.pow(rateOfRotation[1], 2) + Math.pow(rateOfRotation[2], 2));
//
//        // Normalize the rotation vector if it's big enough to get the axis
//        if (magnitude > epsilon) {
//            rateOfRotation[0] /= magnitude;
//            rateOfRotation[1] /= magnitude;
//            rateOfRotation[2] /= magnitude;
//        }
//
//        // Integrate around this axis with the angular speed by the timestep
//        // in order to get a delta rotation from this sample over the timestep
//        // We will convert this axis-angle representation of the delta rotation
//        // into a quaternion before turning it into the rotation matrix.
//        float thetaOverTwo = magnitude * dt / 2.0f;
//        float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
//        float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
//
//        double[] deltaVector = new double[4];
//
//        deltaVector[0] = sinThetaOverTwo * rateOfRotation[0];
//        deltaVector[1] = sinThetaOverTwo * rateOfRotation[1];
//        deltaVector[2] = sinThetaOverTwo * rateOfRotation[2];
//        deltaVector[3] = cosThetaOverTwo;
//
//        // Since it is a unit quaternion, we can just multiply the old rotation
//        // by the new rotation delta to integrate the rotation.
//        return previousRotationVector.multiply(new Quaternion(deltaVector[3], Arrays.copyOfRange(
//                deltaVector, 0, 3)));
//    }
//
//}
