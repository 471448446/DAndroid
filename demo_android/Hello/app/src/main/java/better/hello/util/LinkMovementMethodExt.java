package better.hello.util;


import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Des https://www.ibm.com/developerworks/cn/web/1407_zhangqian_androidhtml/
 * Create By better on 2016/10/26 10:04.
 */
public class LinkMovementMethodExt extends LinkMovementMethod {
    public static final int MSG = 200;
    private Handler handler = null;
    private Class spanClass = null;
    private OnClickSpans onClickSpans;
    private OnClickImageSpan onClickImageSpans;

    public LinkMovementMethodExt(Class spanClass, Handler handler, OnClickSpans onClickSpans) {
        this.spanClass = spanClass;
        this.handler = handler;
        this.onClickSpans = onClickSpans;
    }

    public LinkMovementMethodExt(OnClickSpans onClickSpans) {
        this(ImageSpan.class, null, onClickSpans);
    }

    public LinkMovementMethodExt() {
        this(null);
    }

    private int x1;
    private int x2;
    private int y1;
    private int y2;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = (int) event.getX();
            y1 = (int) event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            x2 = (int) event.getX();
            y2 = (int) event.getY();

            if (Math.abs(x1 - x2) < 10 && Math.abs(y1 - y2) < 10) {

                x2 -= widget.getTotalPaddingLeft();
                y2 -= widget.getTotalPaddingTop();

                x2 += widget.getScrollX();
                y2 += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y2);
                int off = layout.getOffsetForHorizontal(line, x2);
                /**
                 * get you interest span
                 */
                Object[] spans = buffer.getSpans(off, off, spanClass);
                if (spans.length != 0) {
                    if (null != onClickSpans) onClickSpans.onClick(spans);
                    Selection.setSelection(buffer, buffer.getSpanStart(spans[0]), buffer.getSpanEnd(spans[0]));
                    if (null != handler) {
                        MessageSpan obj = new MessageSpan();
                        obj.setObj(spans);
                        obj.setView(widget);
                        Message message = handler.obtainMessage();
                        message.obj = obj;
                        message.what = MSG;
                        message.sendToTarget();
                    }
                    if (null != onClickSpans) onClickSpans.onClick(spans);
                    if (null != onClickImageSpans) {
                        for (Object span : spans) {
                            if (span instanceof ImageSpan) {
                                onClickImageSpans.onClick((ImageSpan) span);
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }


    public boolean canSelectArbitrarily() {
        return true;
    }

    public boolean onKeyUp(TextView widget, Spannable buffer, int keyCode, KeyEvent event) {
        return false;
    }

    public LinkMovementMethodExt setOnClickSpans(OnClickSpans onClickSpans) {
        this.onClickSpans = onClickSpans;
        return this;
    }

    public LinkMovementMethodExt setOnClickImageSpans(OnClickImageSpan onClickImageSpans) {
        this.onClickImageSpans = onClickImageSpans;
        return this;
    }

    public interface OnClickSpans {
        void onClick(Object[] spans);
    }

    public interface OnClickImageSpan {
        void onClick(ImageSpan span);
    }
}
