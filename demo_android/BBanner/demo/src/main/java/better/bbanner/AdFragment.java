package better.bbanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdFragment extends Fragment {
    private ImageView adView;
    private ViewGroup container;

    public void setAdView(ImageView adView) {
        this.adView = adView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container = (ViewGroup) view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null == adView) {
            return;
        }
        ViewParent parent = adView.getParent();
        if (null != parent) {
            ((ViewGroup) parent).removeView(adView);
        }
        container.addView(adView);
    }
}
