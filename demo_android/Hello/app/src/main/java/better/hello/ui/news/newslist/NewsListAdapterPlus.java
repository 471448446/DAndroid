package better.hello.ui.news.newslist;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import better.hello.App;
import better.hello.R;
import better.hello.data.bean.ImagesDetailsBean;
import better.hello.data.bean.NewsListBean;
import better.hello.ui.base.adapter.BaseRecyclerViewAdapter;
import better.hello.util.ImageUtil;
import better.hello.util.Utils;
import better.lib.utils.ViewUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by better on 2016/10/18.
 */

public class NewsListAdapterPlus extends BaseRecyclerViewAdapter<NewsListBean, RecyclerView.ViewHolder> {
    private final int TYPE_NEWS = 1;
    private final int TYPE_PHOTO = TYPE_NEWS + 1;
    private final int TYPE_HEAD = TYPE_PHOTO + 1;
    private NewsListFragment.NewsItemClickListener newsItemClickListener;

    public NewsListAdapterPlus(Fragment context, NewsListFragment.NewsItemClickListener listener) {
        super(context);
        this.newsItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) return TYPE_HEAD;
        if (null == mList) return super.getItemViewType(position);
        if (null == mList.get(position) || mList.get(position).getImgs().isEmpty()) {
            return TYPE_NEWS;
        } else {
            return TYPE_PHOTO;
        }
    }

    public void collect(String key, boolean isCollect) {
        for (NewsListBean be : mList) {
            if (be.getTitle().equalsIgnoreCase(key)) {
                be.setIsCollect(isCollect);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NEWS:
                return new ItemViewHolder(getView(parent, R.layout.item_news));
            case TYPE_PHOTO:
                return new PhotoViewHolder(getView(parent, R.layout.item_news_photo));
            case TYPE_HEAD:
                return new HeaderView(getView(parent, R.layout.index_list_headview));
            default:
                throw new RuntimeException("there is no type that matches the type " + viewType +
                        " + make sure your using types correctly");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setNewsItem((ItemViewHolder) holder, position);
        } else if (holder instanceof PhotoViewHolder) {
            setPhotoItem((PhotoViewHolder) holder, position);
        }
    }

    private void setPhotoItem(PhotoViewHolder holder, int position) {
        NewsListBean newsSummary = mList.get(position);
        String title = newsSummary.getTitle();
        String time = newsSummary.getPub_date();

        holder.titleTv.setText(title);
        holder.timeTV.setText(time);
        holder.itemView.setOnClickListener(new ItemClickListener(true, newsSummary));

        setImageViewList(holder, newsSummary);
    }

    private void setNewsItem(ItemViewHolder holder, int position) {
        NewsListBean newsSummary = mList.get(position);
        String title = newsSummary.getTitle();
        if (title == null) {
            title = newsSummary.getTitle();
        }
        String pTime = newsSummary.getPub_date();
        holder.itemView.setOnClickListener(new ItemClickListener(false, newsSummary));
        holder.titleTv.setText(title);
        holder.timeTv.setText(pTime);
        ImageUtil.load(App.getApplication(), newsSummary.getImgSrc(), holder.img);
    }

    private void setImageViewList(PhotoViewHolder holder, NewsListBean bean) {
        int PhotoThreeHeight = ViewUtil.dp2px(App.getApplication(), 90f);
        int PhotoTwoHeight = ViewUtil.dp2px(App.getApplication(), 120);
        int PhotoOneHeight = ViewUtil.dp2px(App.getApplication(), 150);

        String imgSrcLeft = null;
        String imgSrcMiddle = null;
        String imgSrcRight = null;

        ViewGroup.LayoutParams layoutParams = holder.imgLay.getLayoutParams();

        if (bean.getImgs() != null) {
            List<ImagesDetailsBean> adsBeanList = bean.getImgs();
            int size = adsBeanList.size();
            if (size >= 3) {
                imgSrcLeft = adsBeanList.get(0).getSrc();
                imgSrcMiddle = adsBeanList.get(1).getSrc();
                imgSrcRight = adsBeanList.get(2).getSrc();

                layoutParams.height = PhotoThreeHeight;

                holder.titleTv.setText(App.getApplication()
                        .getString(R.string.photo_collections, adsBeanList.get(0).getTitle()));
            } else if (size >= 2) {
                imgSrcLeft = adsBeanList.get(0).getSrc();
                imgSrcMiddle = adsBeanList.get(1).getSrc();

                layoutParams.height = PhotoTwoHeight;
            } else if (size >= 1) {
                imgSrcLeft = adsBeanList.get(0).getSrc();

                layoutParams.height = PhotoOneHeight;
            }
        } else {
            imgSrcLeft = bean.getImgSrc();

            layoutParams.height = PhotoOneHeight;
        }

        setPhotoImageView(holder, imgSrcLeft, imgSrcMiddle, imgSrcRight);
        holder.imgLay.setLayoutParams(layoutParams);
    }

    private void setPhotoImageView(PhotoViewHolder holder, String imgSrcLeft, String imgSrcMiddle, String imgSrcRight) {
        if (imgSrcLeft != null) {
            showAndSetPhoto(holder.mNewsSummaryPhotoIvLeft, imgSrcLeft);
        } else {
            hidePhoto(holder.mNewsSummaryPhotoIvLeft);
        }

        if (imgSrcMiddle != null) {
            showAndSetPhoto(holder.imgMid, imgSrcMiddle);
        } else {
            hidePhoto(holder.imgMid);
        }

        if (imgSrcRight != null) {
            showAndSetPhoto(holder.imgRight, imgSrcRight);
        } else {
            hidePhoto(holder.imgRight);
        }
    }

    private void showAndSetPhoto(ImageView imageView, String imgSrc) {
        Utils.setVisible(imageView);
        ImageUtil.load(App.getApplication(), imgSrc, imageView);
    }

    private void hidePhoto(ImageView imageView) {
        Utils.setGone(imageView);
    }

    public class ItemClickListener implements View.OnClickListener {
        private boolean isPhoto;
        private NewsListBean bean;

        public ItemClickListener(boolean isPhoto, NewsListBean bean) {
            this.isPhoto = isPhoto;
            this.bean = bean;
        }

        @Override
        public void onClick(View v) {
            newsItemClickListener.onClickNews(isPhoto, bean);
        }
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_photo_iv)
        ImageView img;
        @BindView(R.id.news_title_tv)
        TextView titleTv;
        @BindView(R.id.news_ptime_tv)
        TextView timeTv;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_title_tv)
        TextView titleTv;
        @BindView(R.id.news_photo_iv_group)
        LinearLayout imgLay;
        @BindView(R.id.news_photo_iv_left)
        ImageView mNewsSummaryPhotoIvLeft;
        @BindView(R.id.news_photo_iv_middle)
        ImageView imgMid;
        @BindView(R.id.news_photo_iv_right)
        ImageView imgRight;
        @BindView(R.id.news_ptime_tv)
        TextView timeTV;

        public PhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class HeaderView extends RecyclerView.ViewHolder {

        public HeaderView(View itemView) {
            super(itemView);
        }
    }
}
