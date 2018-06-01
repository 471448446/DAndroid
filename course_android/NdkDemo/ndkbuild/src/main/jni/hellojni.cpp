//
// Created by Better on 2018/5/31.
//

#include "better_learn_ndkbuild_MainActivity.h"

JNIEXPORT jstring JNICALL Java_better_learn_ndkbuild_MainActivity_helloJni
        (JNIEnv *env, jobject){
    return  env->NewStringUTF("Hello JNI from C++ by ndk-build");
}
JNIEXPORT jstring JNICALL JNINAME(helloJni2)(JNIEnv *env, jobject instance) {
    return env->NewStringUTF("Hello JNI from C++ by ndk-build");
}

