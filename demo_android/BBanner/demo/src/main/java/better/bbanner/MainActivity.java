package better.bbanner;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.better.banner.BBanner;
import com.better.banner.ItemAdapter;
import com.better.banner.OnClickItemListener;
import com.better.banner.transformer.TransitionEffect;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BBanner ba = (BBanner) findViewById(R.id.banner1);
        BBanner ba2 = (BBanner) findViewById(R.id.banner2);
//        setClicl(ba, ba2);

        ba.setTransitionEffect(TransitionEffect.Depth);
        ba.setData(getSupportFragmentManager(), getItemAdapter());
        ba2.setData(getSupportFragmentManager(), getItemAdapter2());
        setClicl(ba, ba2);


        banner3();


    }

    private void banner3() {
        final List<String> list3 = getList3();
        final List<ImageView> imageViews = new ArrayList<>(list3.size());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final CountDownLatch countDownLatch = new CountDownLatch(list3.size());
                for (final String s : list3) {
                    final ImageView imageView = new ImageView(MainActivity.this);
                    imageViews.add(imageView);
                    try {
                        Glide.with(MainActivity.this)
                                .load(s)
                                .addListener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        Log.e("Better", "onLoadFailed:" + s);
                                        countDownLatch.countDown();
                                        return true;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        Log.e("Better", "onResourceReady:" + s);
                                        imageView.setBackgroundDrawable(resource);
                                        countDownLatch.countDown();
                                        return true;
                                    }
                                })
                                .submit().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    countDownLatch.await(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("Better", "___banner3");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BBanner banner3 = findViewById(R.id.banner3);
                        banner3.setData(getSupportFragmentManager(), new ItemAdapter() {
                            @Override
                            public Fragment getItem(int p) {
                                AdFragment adFragment = new AdFragment();
                                adFragment.setAdView(imageViews.get(p));
                                return adFragment;
                            }

                            @Override
                            public int getCount() {
                                return list3.size();
                            }
                        });
                    }
                });
            }
        };

        new Thread(runnable).start();
    }

    @NonNull
    private ItemAdapter getItemAdapter2() {
        return new ItemAdapter(getList2()) {
            @Override
            public Fragment getItem(int p) {
                return PagerImageDetails.getInstance(getList2().get(p));
            }
        };
    }

    @NonNull
    private ItemAdapter getItemAdapter() {
        final List<Fragment> fragments = new ArrayList<>();
        final List<String> list = getList();
        for (String s : list) {
            fragments.add(PagerImageDetails.getInstance(s));
        }
        return new ItemAdapter() {
            @Override
            public Fragment getItem(int p) {
                return PagerImageDetails.getInstance(list.get(p));
//                return fragments.get(p);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };
    }

    public void setClicl(BBanner... bans) {
        for (BBanner ba :
                bans) {
            ba.setOnItemClickListener(new OnClickItemListener() {
                @Override
                public void onClick(int p) {
                    Log.d("MainActivity", "click===" + p);
                    toast("" + p);
                }
            });
        }
    }

    private List<String> getList() {
        List<String> list = new ArrayList<>();
        list.add("https://images.pexels.com/photos/4614516/pexels-photo-4614516.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        list.add("https://images.pexels.com/photos/425160/pexels-photo-425160.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        list.add("http://s3.lvjs.com.cn/trip/original/20140818131519_1500748202.jpg");
        list.add("http://s3.lvjs.com.cn/trip/original/20140818131550_1792868513.jpg");
        list.add("https://images.pexels.com/photos/4328298/pexels-photo-4328298.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");

        return list;
    }

    private List<String> getList2() {
        List<String> list = new ArrayList<>();
        list.add("https://images.pexels.com/photos/4614516/pexels-photo-4614516.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        list.add("https://images.pexels.com/photos/425160/pexels-photo-425160.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        list.add("http://s3.lvjs.com.cn/trip/original/20140818131519_1500748202.jpg");
        list.add("http://s3.lvjs.com.cn/trip/original/20140818131550_1792868513.jpg");
        list.add("https://images.pexels.com/photos/4328298/pexels-photo-4328298.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");

        return list;
    }

    private List<String> getList3() {
        List<String> list = new ArrayList<>();
        list.add("https://images.pexels.com/photos/4614516/pexels-photo-4614516.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        list.add("https://images.pexels.com/photos/425160/pexels-photo-425160.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
//        list.add("http://s3.lvjs.com.cn/trip/original/20140818131519_1500748202.jpg");
//        list.add("http://s3.lvjs.com.cn/trip/original/20140818131550_1792868513.jpg");
//        list.add("https://images.pexels.com/photos/4328298/pexels-photo-4328298.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        return list;
    }

    private void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
