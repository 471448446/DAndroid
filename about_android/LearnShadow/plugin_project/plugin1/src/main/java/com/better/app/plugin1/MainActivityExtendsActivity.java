package com.better.app.plugin1;

import android.app.Activity;

/**
 * 只有继承于Activity的类才会被直接替换成ShadowActivity。见：com.tencent.shadow.core.transform.specific.ActivityTransform
 * 而AppCompatActivity的父类，最终是有一个直接继承于Activity的，转换的时候这个父类要被转换成继承于ShadowActivity。
 * 所以AppCompatActivity也相当于被转换了。
 * 这个点没有get到，所以写了改类{@link MainActivityExtendsActivity}，直接继承于Activity，发现编译出的插件确实是被转换了。
 */
public class MainActivityExtendsActivity extends Activity {
}
