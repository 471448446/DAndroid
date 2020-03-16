package com.better.learn.gl20.training;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by better on 2020/3/15 9:50 PM.
 */
public class ResourceUtils {

    /**
     * 用于读取GLSL Shader文件内容
     */
    public static String readTextFromResource(Context context, int resId) {
        StringBuilder builder = new StringBuilder();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            inputStream = context.getResources().openRawResource(resId);
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                builder.append(nextLine);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

}
