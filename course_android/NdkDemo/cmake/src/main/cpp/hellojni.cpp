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
/*=====动态注册*/
/**
 * 实现者
 */
jint sayHello() {
    return 1024;
}

/**
 * 需要注册的函数列表，放在JNINativeMethod 类型的数组中，
 * 以后如果需要增加函数，只需在这里添加就行了
 * 参数：
 * 1.java中用native关键字声明的函数名
 * 2.签名（传进来参数类型和返回值类型的说明）
 * 3.C/C++中对应函数的函数名（地址）
*/
static JNINativeMethod getMethods[] = {
        {"sayHelloJInt", "()I", (void *) sayHello},
};

//此函数通过调用RegisterNatives方法来注册我们的函数
static int registerNativeMethods(JNIEnv* env, const char* className,JNINativeMethod* getMethods,int methodsNum){
    jclass clazz;
    //找到声明native方法的类
    clazz = env->FindClass(className);
    if(clazz == NULL){
        return JNI_FALSE;
    }
    //注册函数 参数：java类 所要注册的函数数组 注册函数的个数
    if(env->RegisterNatives(clazz,getMethods,methodsNum) < 0){
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

static int registerNatives(JNIEnv* env){
    //指定类的路径，通过FindClass 方法来找到对应的类
    const char* className  = "better/learn/ndkdemo/JNIUtil";
    return registerNativeMethods(env,className,getMethods, sizeof(getMethods)/ sizeof(getMethods[0]));
}
//回调函数
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved){
    JNIEnv* env = NULL;
    //获取JNIEnv
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    //注册函数 registerNatives ->registerNativeMethods ->env->RegisterNatives
    if(!registerNatives(env)){
        return -1;
    }
    //返回jni 的版本
    return JNI_VERSION_1_6;
}