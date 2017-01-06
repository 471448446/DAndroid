package better.hello.ui.aboutme.collect;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import better.hello.R;
import better.hello.data.bean.NewsListBean;
import better.hello.ui.base.adapter.BaseSlideAdapter;
import better.hello.ui.news.detail.NewsDetailsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Des 收藏adapter
 * Create By better on 2016/12/26 13:45.
 */
public class CollectAdapter extends BaseSlideAdapter<NewsListBean, CollectAdapter.Holder> {

    public CollectAdapter(Fragment ctx) {
        super(ctx);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final NewsListBean bean = getItem(position);
        if (null == bean) return;
        holder.title.setText(bean.getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDetailsActivity.start(mContext, bean);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CollectFragment)mFragment).delete(bean.getTitle(),position);
            }
        });
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_collect_title)
        TextView title;
        @BindView(R.id.item_collect_delete)
        TextView delete;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
