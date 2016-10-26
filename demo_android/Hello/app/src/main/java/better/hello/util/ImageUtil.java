package better.hello.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import better.hello.R;

/**
 * Created by Better on 2016/4/3.
 */
public class ImageUtil {
    public static void load(final Context context, final String url, final ImageView imageView) {
//        if(Setting.getBoolean(Setting.KEY_IMAGE_LOAD,context)){
//            imageView.setImageResource(R.drawable.img_click_load);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClickNews(View view) {
//                    Glide.with(context).load(url).into(imageView);
//                }
//            });
//        }else{
//            Glide.with(context).load(url).into(imageView);
//        }
        Glide.with(context).load(url).into(imageView);
    }
    public static void load(final String url, final ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).error(R.drawable.icon_downloading_err).into(imageView);
    }
}
