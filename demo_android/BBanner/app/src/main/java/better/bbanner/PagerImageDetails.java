package better.bbanner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.bumptech.glide.Glide;

public class PagerImageDetails extends Fragment {
    public static final String URL = "url";
    private ImageView imageView;
    private String mUrl;

    public static PagerImageDetails getInstance(String url) {
        PagerImageDetails imageDetails = new PagerImageDetails();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        imageDetails.setArguments(bundle);
        return imageDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUrl = savedInstanceState == null ? getArguments().getString(URL) : savedInstanceState.getString(URL);
        if (TextUtils.isEmpty(mUrl)) {
            imageView.setBackgroundColor(Color.parseColor("#ebebeb"));
        } else {
            Glide.with(getActivity()).load(mUrl).into(imageView);
//            imageView.setImageResource(R.drawable.sp);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(URL, mUrl);
    }

}