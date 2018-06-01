# 原生库名称
APP_MODULES := hellojni

# 指定机器指令集
APP_ABI := armeabi-v7a arm64-v8a x86 x86_64
# Android NDK: The armeabi ABI is no longer supported. Use armeabi-v7a.
# Android NDK: NDK Application 'local' targets unknown ABI(s): armeabi mips mips64
# Android NDK: MIPS and MIPS64 are no longer supported.
# Android NDK: Please fix the APP_ABI definition in D:/WP/DAndroid/course_android/NdkDemo/ndkbuild/src/main/jni/Application.mk

# Android NDK: APP_PLATFORM not set. Defaulting to minimum supported version android-14.
# http://blog.ready4go.com/blog/2013/05/18/resolve-android-ndk-warning-app-platform-android-14-is-larger-than-android-minsdkversion-8/
APP_PLATFORM := android-14