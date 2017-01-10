package better.hello.ui.news.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import better.hello.R;
import better.hello.data.bean.DownloadInfo;
import better.hello.data.bean.ImagesDetailsBean;
import better.hello.http.download.DownLoadService;
import better.hello.ui.base.BaseFragment;
import better.hello.util.C;
import better.hello.util.FileUtils;
import better.hello.util.Utils;
import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageDetailsFragment extends BaseFragment {
    @BindView(R.id.image_details)
    ImageView imageView;
    @BindView(R.id.image_details_txt)
    TextView desTv;
    private ImagesDetailsBean mBean;
    private PhotoViewAttacher mAttacher;
    private NewsPhotoDetailActivity mActivity;
    private boolean showDetail = true;
    private PhotoViewAttacher.OnPhotoTapListener mListener = new PhotoViewAttacher.OnPhotoTapListener() {
        @Override
        public void onPhotoTap(View view, float v, float v1) {
            if (showDetail) {
                Utils.setGone(desTv);
            } else {
                Utils.setVisible(desTv);
            }
            setDesInfo();
        }
    };

    public static ImageDetailsFragment getInstance(ImagesDetailsBean bean) {
        ImageDetailsFragment imageDetailsFragment = new ImageDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(C.EXTRA_BEAN, bean);
        imageDetailsFragment.setArguments(bundle);
        return imageDetailsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (NewsPhotoDetailActivity) getActivity();
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_image_detail;
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        if (null != getArguments())
            mBean = getArguments().getParcelable(C.EXTRA_BEAN);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mAttacher)
            mAttacher.cleanup();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != savedInstanceState) {
            mBean = savedInstanceState.getParcelable(C.EXTRA_BEAN);
        }
        if (null != mBean && !TextUtils.isEmpty(mBean.getSrc())) {
            /* fitCenter 填充宽高  --better 2017/1/10 11:05. */
            Glide.with(mContext).load(mBean.getSrc()).fitCenter().listener(getRequestListener()).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.icon_downloading_err);
        }
        setDesInfo();
    }

    @NonNull
    private RequestListener<String, GlideDrawable> getRequestListener() {
        return new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                mAttacher = new PhotoViewAttacher(imageView);
                mAttacher.setOnPhotoTapListener(mListener);
                mAttacher.update();
                return false;
            }
        };
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(C.EXTRA_BEAN, mBean);
    }

    @OnClick({R.id.image_details_down})
    public void onClick(View v) {
        DownLoadService.start(getContext(), new DownloadInfo(mBean.getTitle(), mBean.getSrc(), FileUtils.getImagePath(FileUtils.getImageFileNameByUrl(mBean.getSrc()))));
    }

    private void setDesInfo() {
        showDetail = desTv.getVisibility() == View.VISIBLE;
        mActivity.showToolBar(showDetail);
    }
}