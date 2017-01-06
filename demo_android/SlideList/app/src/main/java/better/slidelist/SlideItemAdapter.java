package better.slidelist;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemSlideHelper.Callback {

    private RecyclerView mRecyclerView;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide, parent, false);
        return new TextVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        String text = "item: " + position;
        TextVH textVH = (TextVH) holder;
        textVH.textView.setText(text);
        textVH.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Better", "left响应===>" + position);

            }
        });
        textVH.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        textVH.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Better", "菜单响应===>" + position);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
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
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return 20;
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

class TextVH extends RecyclerView.ViewHolder {

    TextView textView, delete;

    public TextVH(View itemView) {
        super(itemView);

        textView = (TextView) itemView.findViewById(R.id.tv_text);
        delete = (TextView) itemView.findViewById(R.id.tv_delete);
    }
}