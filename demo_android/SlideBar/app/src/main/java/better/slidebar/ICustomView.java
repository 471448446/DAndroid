package better.slidebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * Des 自定义View流程
 * Create By better on 16/7/12 上午10:31.
 */
public interface ICustomView {
    /**
     * 默认的配置
     */
    void initDefaultAttr(Context context);

    /**
     * 自定义
     */
    void initCustomAttr(Context context, AttributeSet attrs);

    /**
     * 自定义获取值
     */
    void initCustomAttrDetail(int attr, TypedArray typedArray);

    /**
     * 设置相应属性
     */
    void initView(Context context);
}
