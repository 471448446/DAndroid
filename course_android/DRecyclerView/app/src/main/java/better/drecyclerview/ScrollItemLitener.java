package better.drecyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Des 模拟网易栏目移动
 * Create By better on 2017/1/19 14:59.
 */
public class ScrollItemLitener implements RecyclerView.OnItemTouchListener {
    private int downChildView = -1;
    private MotionEvent downE;
    private View childV;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downE = e;
                childV = rv.findChildViewUnder(e.getX(), e.getY());
                downChildView = getChildViewApapterP(rv, e.getX(), e.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (null != childV && null != downE) {
                    l("移动");
                    childV.scrollBy((int) (e.getX() - downE.getX()), (int) (e.getY() - downE.getY()));
                }
                break;
            case MotionEvent.ACTION_UP:
                int upP = getChildViewApapterP(rv, e.getX(), e.getY());
                if (upP > 0 && upP != downChildView) {
                    if (null != rv.getAdapter())
                        rv.getAdapter().notifyDataSetChanged();
                }
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    private int getChildViewApapterP(RecyclerView rv, float x, float y) {
        View view = rv.findChildViewUnder(x, y);
        return null != view ? rv.getChildAdapterPosition(view) : -1;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private void l(String msg) {
        Log.d("Better", msg);
    }
}
