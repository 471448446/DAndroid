#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring
//
// Created by Better on 2018/5/29.
//

JNICALL
Java_better_learn_ndkdemo_JNIUtil_sayHelloJNI(JNIEnv *env, jobject /* this */) {
    return env->NewStringUTF("Hello JNI from c++  by cmake");
}