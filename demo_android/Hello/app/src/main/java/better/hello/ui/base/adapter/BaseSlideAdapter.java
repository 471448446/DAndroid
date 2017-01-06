package better.hello.ui.base.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import better.hello.common.ItemSlideHelper;
import better.hello.util.Utils;

/**
 * Des 线性布局，加两个子布局，第二个为侧滑菜单。
 * 注意BRecyclerView 的Adapter是包裹的一个adapter
 * Create By better on 2017/1/6 13:45.
 */
public abstract class BaseSlideAdapter<E, T extends RecyclerView.ViewHolder> extends BaseRecyclerViewAdapter<E, T> implements ItemSlideHelper.Callback {

    private RecyclerView mRecyclerView;

    public BaseSlideAdapter(Activity ctx) {
        super(ctx);
    }

    public BaseSlideAdapter(Fragment fr) {
        super(fr);
    }

    public BaseSlideAdapter(Activity context, Fragment fragment) {
        super(context, fragment);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Utils.d("ItemSlideHelper", "attached");
        mRecyclerView = recyclerView;
        mRecyclerView.addOnItemTouchListener(new ItemSlideHelper(mRecyclerView.getContext(), this));
    }

    @Override
    public int getHorizontalRange(RecyclerView.ViewHolder holder) {
        if (holder.itemView instanceof LinearLayout) {
            ViewGroup viewGroup = (ViewGroup) holder.itemView;
            if (viewGroup.getChildCount() == 2) {
                return viewGroup.getChildAt(1).getLayoutParams().width;
            }
        } else {
            throw new IllegalArgumentException("LinearLayout 为父布局");
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getChildViewHolder(View childView) {
        return mRecyclerView.getChildViewHolder(childView);
    }

    @Override
    public View findTargetView(float x, float y) {
        return mRecyclerView.findChildViewUnder(x, y);
    }

}
