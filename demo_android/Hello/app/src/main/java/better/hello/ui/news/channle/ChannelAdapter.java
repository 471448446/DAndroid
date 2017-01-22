package better.hello.ui.news.channle;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import better.hello.R;
import better.hello.common.RVDragMoveHelper;
import better.hello.data.bean.NewsChannelBean;
import better.hello.ui.base.adapter.BaseRecyclerViewAdapter;
import better.hello.util.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by better on 2017/1/20.
 */

public class ChannelAdapter extends BaseRecyclerViewAdapter<NewsChannelBean, ChannelAdapter.Holder> implements RVDragMoveHelper.DragDataListener {
    private boolean isTop = false;
    private boolean isShowDelete = false;
    public boolean isChanged = false;

    public ChannelAdapter(List<NewsChannelBean> list, Activity context, boolean isTop) {
        super(list, context);
        this.isTop = isTop;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (isTop) {
            handleTop(holder, position);
        } else {
            handleBottom(holder, position);
        }
    }

    private void handleBottom(Holder holder, final int position) {
        Utils.setGone(holder.delete);
        holder.txt.setText(mList.get(position).getName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChanged = true;
                ((ChannelActivity) mContext).onClickBottomItem(mList.get(position), position);
            }
        });
    }

    private void handleTop(Holder holder, final int position) {
        if (isShowDelete) Utils.setVisible(holder.delete);
        else Utils.setInvisible(holder.delete);
        holder.txt.setText(mList.get(position).getName());
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isShowDelete = true;
                notifyDataSetChanged();
                return false;
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChannelActivity) mContext).onClickTopItem(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 == getItemCount()) {
                    Utils.toastShort(mContext, mContext.getString(R.string.str_empty_selected_channel));
                    return;
                }
                ((ChannelActivity) mContext).onDeleteTopItem(mList.get(position), position);
            }
        });
    }

    @Override
    public List<?> getActionData() {
        isChanged = true;
        return mList;
    }

    class Holder extends RecyclerView.ViewHolder {

        View item;
        @BindView(R.id.item_channel_delete)
        ImageView delete;
        @BindView(R.id.item_channel_txt)
        TextView txt;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            item = itemView;
        }
    }
}
