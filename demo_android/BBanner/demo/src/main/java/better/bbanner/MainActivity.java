package better.bbanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.better.banner.BBanner;
import com.better.banner.ItemAdapter;
import com.better.banner.OnClickItemListener;
import com.better.banner.transformer.TransitionEffect;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

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
        setClicl(ba,ba2);
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
        return new ItemAdapter() {
            @Override
            public Fragment getItem(int p) {
                return PagerImageDetails.getInstance(getList().get(p));
            }

            @Override
            public int getCount() {
                return getList().size();
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
        list.add("http://file27.mafengwo.net/M00/B2/12/wKgB6lO0ahWAMhL8AAV1yBFJDJw20.jpeg");
        list.add("http://file25.mafengwo.net/M00/0A/AC/wKgB4lMC26CAWsKoAALb5778DWg60.rbook_comment.w1024.jpeg");
        list.add("http://s3.lvjs.com.cn/trip/original/20140818131519_1500748202.jpg");
        list.add("http://s3.lvjs.com.cn/trip/original/20140818131550_1792868513.jpg");
        list.add("http://file25.mafengwo.net/M00/0A/AC/wKgB4lMC26CAWsKoAALb5778DWg60.rbook_comment.w1024.jpeg");

        return list;
    }
    private List<String> getList2() {
        List<String> list = new ArrayList<>();
        list.add("http://pic.qiantucdn.com/58pic/18/13/67/72w58PICshJ_1024.jpg");
        list.add("http://pic24.nipic.com/20121008/3822951_094451200000_2.jpg");
        list.add("http://pic38.nipic.com/20140228/3822951_135521683000_2.jpg");
        list.add("http://pic.58pic.com/58pic/13/87/55/39s58PICSRI_1024.jpg");
        return list;
    }

    private void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
