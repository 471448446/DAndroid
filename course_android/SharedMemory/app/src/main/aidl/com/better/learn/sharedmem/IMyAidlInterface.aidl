// IMyAidlInterface.aidl
package com.better.learn.sharedmem;
import android.os.SharedMemory;
// Declare any non-default types here with import statements

interface IMyAidlInterface {
    SharedMemory get();
}