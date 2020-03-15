package com.better.learn.gl20.fuck;

import android.content.Context;

/**
 * Created by better on 2020/3/14 8:43 PM.
 */
public class Cube extends BShape {

    public Cube(Context context) {
        super(context);
    }

    @Override
    protected int getFragmentResourceId() {
        return 0;
    }

    @Override
    protected int getVertexResourceId() {
        return 0;
    }

    @Override
    protected void drawShape() {

    }
}
