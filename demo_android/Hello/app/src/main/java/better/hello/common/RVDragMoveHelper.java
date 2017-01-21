package better.hello.common;

import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;
import java.util.List;

/**
 * RV 的一个拖动滑动删除
 * Created by better on 2017/1/19.
 */

public class RVDragMoveHelper extends ItemTouchHelper {
    /**
     * Des Grid拖动
     * Create By better on 2017/1/20 16:11.
     */
    public static RVDragMoveHelper get() {
        return get(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.ACTION_STATE_IDLE);
    }

    public static RVDragMoveHelper get(int drag, int move) {
        return new RVDragMoveHelper(new RVDragMoveHelper.Callback(drag, move));
    }

    private Callback mCallback;

    public RVDragMoveHelper(Callback callback) {
        super(callback);
        mCallback = callback;
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        super.attachToRecyclerView(recyclerView);
        if (null != mCallback) mCallback.attachToRecyclerView(recyclerView);
    }

    public static class Callback extends SimpleCallback {
        private RecyclerView mRecyclerView;

        /**
         * Creates a Callback for the given drag and swipe allowance. These values serve as
         * defaults
         * and if you want to customize behavior per ViewHolder, you can override
         * {@link #getSwipeDirs(RecyclerView, RecyclerView.ViewHolder)}
         * and / or {@link #getDragDirs(RecyclerView, RecyclerView.ViewHolder)}.
         *
         * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
         *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
         *                  #END},
         *                  {@link #UP} and {@link #DOWN}.
         * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
         *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
         *                  #END},
         *                  {@link #UP} and {@link #DOWN}.
         */
        public Callback(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            if (null == recyclerView.getAdapter()) return false;
            DragDataListener dragDataListener = (DragDataListener) recyclerView.getAdapter();
            if (null == dragDataListener || null == dragDataListener.getActionData()) return false;
            int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
            int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
            if (fromPosition < toPosition) {
                //分别把中间所有的item的位置重新交换
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(dragDataListener.getActionData(), i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(dragDataListener.getActionData(), i, i - 1);
                }
            }
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            //返回true表示执行拖动
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (null == mRecyclerView) return;
            int position = viewHolder.getAdapterPosition();
            DragDataListener dragDataListener = (DragDataListener) mRecyclerView.getAdapter();
            if (null == dragDataListener || null == dragDataListener.getActionData()) return;
            dragDataListener.getActionData().remove(position);
            mRecyclerView.getAdapter().notifyItemRemoved(position);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                //滑动时改变Item的透明度
                final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            }
        }

        public void attachToRecyclerView(RecyclerView recyclerView) {
            this.mRecyclerView = recyclerView;
        }
    }

    public interface DragDataListener {
        List<?> getActionData();
    }
}
