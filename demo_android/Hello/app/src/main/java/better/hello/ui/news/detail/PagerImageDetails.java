package better.hello.ui.news.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import better.hello.R;
import better.hello.util.ImageUtil;


public class PagerImageDetails extends Fragment {
    public static final String POSITION = "postion", URL = "url", RECIEVER_ACTION = "action";
    private ImageView imageView;
    private String mUrl, maction;
//    private int mP;

    public static PagerImageDetails getInstance(String url) {
        PagerImageDetails imageDetails = new PagerImageDetails();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
//        bundle.putInt(POSITION, relP);
//        bundle.putString(RECIEVER_ACTION, action);
        imageDetails.setArguments(bundle);
        return imageDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ScaleType.FIT_XY);
//        mP = getArguments().getInt(POSITION);
        maction = getArguments().getString(RECIEVER_ACTION);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return imageView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUrl = savedInstanceState == null ? getArguments().getString(URL) :
                savedInstanceState.getString(URL);
//        mP = savedInstanceState == null ? getArguments().getInt(POSITION) :
//                savedInstanceState.getInt(POSITION);
        if (TextUtils.isEmpty(mUrl)) {
            imageView.setImageResource(R.drawable.icon_downloading_err);
//            imageView.setImageResource(R.color.banner_default_color);
        } else {
			ImageUtil.load(mUrl,imageView);
//            ImageCacheManger.loadImageWithColor(mUrl, imageView);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(URL, mUrl);
//        outState.putSerializable(POSITION, mP);
    }

}