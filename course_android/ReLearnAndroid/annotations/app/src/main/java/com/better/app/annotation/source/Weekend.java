package com.better.app.annotation.source;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Weekend {
    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;

    @IntDef({SUNDAY, MONDAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Weekends {

    }

    /**
     * @param day 只能是{@link #SUNDAY}{@link #MONDAY} 这两个，不然会有编译提示
     */
    public static void set(@Weekends int day) {

    }

    public static void main(String[] args) {
        // 语法提示错误
        // 这个是接口IDE的功能@IntDef来实现的
        Weekend.set(1);
        Weekend.set(2);
        // 正确
        Weekend.set(SUNDAY);
    }
}
