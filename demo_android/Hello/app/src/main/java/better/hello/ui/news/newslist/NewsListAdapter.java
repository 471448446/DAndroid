package better.hello.ui.news.newslist;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import better.hello.App;
import better.hello.R;
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

public class NewsListAdapter extends BaseRecyclerViewAdapter<NewsListBean, RecyclerView.ViewHolder> {
    private final int TYPE_NEWS = 1;
    private final int TYPE_PHOTO = TYPE_NEWS + 1;
    private NewsListFragment.NewsItemClickListener newsItemClickListener;

    public NewsListAdapter(Fragment context, NewsListFragment.NewsItemClickListener listener) {
        super(context);
        this.newsItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (null == mList) return super.getItemViewType(position);
        if (!TextUtils.isEmpty(mList.get(position).getDigest())) {
            return TYPE_NEWS;
        } else {
            return TYPE_PHOTO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_NEWS:
                view = getView(parent, R.layout.item_news);
                final ItemViewHolder itemViewHolder = new ItemViewHolder(view);
                return itemViewHolder;
            case TYPE_PHOTO:
                view = getView(parent, R.layout.item_news_photo);
                final PhotoViewHolder photoItemViewHolder = new PhotoViewHolder(view);
                return photoItemViewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
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
        String ptime = newsSummary.getPtime();

        holder.mNewsSummaryTitleTv.setText(title);
        holder.mNewsSummaryPtimeTv.setText(ptime);
        holder.itemView.setOnClickListener(new ItemClickListener(true, newsSummary));

        setImageViewList(holder, newsSummary);
    }

    private void setNewsItem(ItemViewHolder holder, int position) {
        NewsListBean newsSummary = mList.get(position);
        String title = newsSummary.getLtitle();
        if (title == null) {
            title = newsSummary.getTitle();
        }
        String pTime = newsSummary.getPtime();
        String digest = newsSummary.getDigest();
        String imgSrc = newsSummary.getImgsrc();
        holder.itemView.setOnClickListener(new ItemClickListener(false, newsSummary));

        holder.mNewsSummaryTitleTv.setText(title);
        holder.mNewsSummaryPtimeTv.setText(pTime);
        holder.mNewsSummaryDigestTv.setText(digest);
        ImageUtil.load(App.getApplication(), newsSummary.getImgsrc(), holder.mNewsSummaryPhotoIv);
    }

    private void setImageViewList(PhotoViewHolder holder, NewsListBean newsSummary) {
        int PhotoThreeHeight = ViewUtil.dp2px(App.getApplication(), 90f);
        int PhotoTwoHeight = ViewUtil.dp2px(App.getApplication(), 120);
        int PhotoOneHeight = ViewUtil.dp2px(App.getApplication(), 150);

        String imgSrcLeft = null;
        String imgSrcMiddle = null;
        String imgSrcRight = null;

        ViewGroup.LayoutParams layoutParams = holder.mNewsSummaryPhotoIvGroup.getLayoutParams();

        if (newsSummary.getAds() != null) {
            List<NewsListBean.AdsBean> adsBeanList = newsSummary.getAds();
            int size = adsBeanList.size();
            if (size >= 3) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();
                imgSrcMiddle = adsBeanList.get(1).getImgsrc();
                imgSrcRight = adsBeanList.get(2).getImgsrc();

                layoutParams.height = PhotoThreeHeight;

                holder.mNewsSummaryTitleTv.setText(App.getApplication()
                        .getString(R.string.photo_collections, adsBeanList.get(0).getTitle()));
            } else if (size >= 2) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();
                imgSrcMiddle = adsBeanList.get(1).getImgsrc();

                layoutParams.height = PhotoTwoHeight;
            } else if (size >= 1) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();

                layoutParams.height = PhotoOneHeight;
            }
        } else if (newsSummary.getImgextra() != null) {
            int size = newsSummary.getImgextra().size();
            if (size >= 3) {
                imgSrcLeft = newsSummary.getImgextra().get(0).getImgsrc();
                imgSrcMiddle = newsSummary.getImgextra().get(1).getImgsrc();
                imgSrcRight = newsSummary.getImgextra().get(2).getImgsrc();

                layoutParams.height = PhotoThreeHeight;
            } else if (size >= 2) {
                imgSrcLeft = newsSummary.getImgextra().get(0).getImgsrc();
                imgSrcMiddle = newsSummary.getImgextra().get(1).getImgsrc();

                layoutParams.height = PhotoTwoHeight;
            } else if (size >= 1) {
                imgSrcLeft = newsSummary.getImgextra().get(0).getImgsrc();

                layoutParams.height = PhotoOneHeight;
            }
        } else {
            imgSrcLeft = newsSummary.getImgsrc();

            layoutParams.height = PhotoOneHeight;
        }

        setPhotoImageView(holder, imgSrcLeft, imgSrcMiddle, imgSrcRight);
        holder.mNewsSummaryPhotoIvGroup.setLayoutParams(layoutParams);
    }

    private void setPhotoImageView(PhotoViewHolder holder, String imgSrcLeft, String imgSrcMiddle, String imgSrcRight) {
        if (imgSrcLeft != null) {
            showAndSetPhoto(holder.mNewsSummaryPhotoIvLeft, imgSrcLeft);
        } else {
            hidePhoto(holder.mNewsSummaryPhotoIvLeft);
        }

        if (imgSrcMiddle != null) {
            showAndSetPhoto(holder.mNewsSummaryPhotoIvMiddle, imgSrcMiddle);
        } else {
            hidePhoto(holder.mNewsSummaryPhotoIvMiddle);
        }

        if (imgSrcRight != null) {
            showAndSetPhoto(holder.mNewsSummaryPhotoIvRight, imgSrcRight);
        } else {
            hidePhoto(holder.mNewsSummaryPhotoIvRight);
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
        @BindView(R.id.news_summary_photo_iv)
        ImageView mNewsSummaryPhotoIv;
        @BindView(R.id.news_summary_title_tv)
        TextView mNewsSummaryTitleTv;
        @BindView(R.id.news_summary_digest_tv)
        TextView mNewsSummaryDigestTv;
        @BindView(R.id.news_summary_ptime_tv)
        TextView mNewsSummaryPtimeTv;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_summary_title_tv)
        TextView mNewsSummaryTitleTv;
        @BindView(R.id.news_summary_photo_iv_group)
        LinearLayout mNewsSummaryPhotoIvGroup;
        @BindView(R.id.news_summary_photo_iv_left)
        ImageView mNewsSummaryPhotoIvLeft;
        @BindView(R.id.news_summary_photo_iv_middle)
        ImageView mNewsSummaryPhotoIvMiddle;
        @BindView(R.id.news_summary_photo_iv_right)
        ImageView mNewsSummaryPhotoIvRight;
        @BindView(R.id.news_summary_ptime_tv)
        TextView mNewsSummaryPtimeTv;

        public PhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
