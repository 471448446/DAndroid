package better.popwindowdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((ListView) findViewById(R.id.list)).setAdapter(new Adapter(this, findViewById(R.id.list)));
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    private static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtilsPlus.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtilsPlus.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    static class Adapter extends BaseAdapter {
        Context mContext;
        View mRootView;

        public Adapter(Context context, View rootView) {
            mContext = context;
            mRootView = rootView;
        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder, parent, false);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDownPop(v);
                }
            });
            holder.right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPop(v, mRootView);
                }
            });
            return convertView;
        }

        private void showPop(View v, View root) {
            PopupWindow popupWindow = new PopupWindow(LayoutInflater.from(mContext).inflate(R.layout.layout_pop, null),
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int[] l = calculatePopWindowPos(v, root);

            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
            popupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, l[0] - 30, l[1]);
            popupWindow.update();

        }

        private void showDownPop(View v) {
            PopupWindow popupWindow = new PopupWindow(LayoutInflater.from(mContext).inflate(R.layout.layout_pop, null),
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
            popupWindow.showAsDropDown(v);
            popupWindow.update();

        }

        class Holder {
            TextView left, right;

            public Holder(View view) {
                left = (TextView) view.findViewById(R.id.item_pop_left);
                right = (TextView) view.findViewById(R.id.item_pop_right);
            }
        }
    }
}
