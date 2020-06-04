package com.better.demclassloader;

import com.better.demclassloader.loader.DiskClassLoader;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void justLoadClass() {
        //动态加载的 class 的路径。
        DiskClassLoader diskClassLoader = new DiskClassLoader("file:///Users/better/Documents/WorkSpace/git/DJava/JavaTest/src/lg/test/");
        try {
            // 加载的文件不能有 包名 声明
            Class<?> loadClass = diskClassLoader.loadClass("Secret");
            if (null != loadClass) {
                Object newInstance = loadClass.newInstance();
                Method declaredMethod = loadClass.getDeclaredMethod("main");
                declaredMethod.invoke(newInstance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}