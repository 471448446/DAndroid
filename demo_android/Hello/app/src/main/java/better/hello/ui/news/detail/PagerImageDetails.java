package better.hello.ui.news.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import better.hello.R;
import better.hello.ui.base.BaseFragment;
import better.hello.util.ImageUtil;
import butterknife.BindView;

/**
 * Des 首页的banner图片
 * Create By better on 2017/1/12 10:25.
 */
public class PagerImageDetails extends BaseFragment {
    public static final String URL = "url", TITLE = "title";
    @BindView(R.id.banner_detail_img)
    ImageView imageView;
    @BindView(R.id.banner_detail_img_des)
    TextView desTv;
    private String mUrl, mTitle;

    public static PagerImageDetails getInstance(String title, String url) {
        PagerImageDetails imageDetails = new PagerImageDetails();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putString(TITLE, title);
        imageDetails.setArguments(bundle);
        return imageDetails;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        imageView = new ImageView(getActivity());
//        imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        imageView.setScaleType(ScaleType.FIT_XY);
//        imageView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//        return imageView;
//    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_banner_img;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUrl = savedInstanceState == null ? getArguments().getString(URL) : savedInstanceState.getString(URL);
        mTitle = savedInstanceState == null ? getArguments().getString(TITLE) : savedInstanceState.getString(TITLE);
        if (TextUtils.isEmpty(mUrl)) {
            imageView.setImageResource(R.drawable.icon_downloading_err);
        } else {
            ImageUtil.load(mUrl, imageView);
        }
        desTv.setText(TextUtils.isEmpty(mTitle) ? "" : mTitle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(URL, mUrl);
        outState.putSerializable(TITLE, mTitle);
    }

}