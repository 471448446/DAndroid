package better.learn.blur.renderscript;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

/**
 * Created by better on 2017/9/25 13:18.
 */

public class RenderScriptGaussianBlur {
    private RenderScript renderScript;

    public RenderScriptGaussianBlur(@NonNull Context context) {
        this.renderScript = RenderScript.create(context);
    }

    public Bitmap gaussianBlur(@IntRange(from = 1, to = 25) int radius, Bitmap original) {
        Allocation input = Allocation.createFromBitmap(renderScript, original);
        Allocation output = Allocation.createTyped(renderScript, input.getType());
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setRadius(radius);
        scriptIntrinsicBlur.setInput(input);
        scriptIntrinsicBlur.forEach(output);
        output.copyTo(original);
        return original;
    }

}
