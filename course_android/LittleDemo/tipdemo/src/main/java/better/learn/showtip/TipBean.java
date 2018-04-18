package better.learn.showtip;

import android.graphics.Bitmap;

/**
 * Created by better on 2018/1/7 16:43.
 */

public class TipBean {
    public int topMargin;
    public Bitmap bitmap;

    public TipBean() {
    }

    public TipBean(int topMargin, Bitmap bitmap) {
        this.topMargin = topMargin;
        this.bitmap = bitmap;
    }
}
