package better.bbanner;

import android.os.Bundle;
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
        ba.setTransitionEffect(TransitionEffect.Depth);
        ba.setOnItemClickListener(new OnClickItemListener() {
            @Override
            public void onClick(int p) {
                Log.d("MainActivity", "click===" + p);
                toast("" + p);
            }
        });
        ba.setData(getSupportFragmentManager(), new ItemAdapter() {
            @Override
            public Fragment getItem(int p) {
                return PagerImageDetails.getInstance(getList().get(p));
            }

            @Override
            public int getCount() {
                return getList().size();
            }
        });
    }

    private List<String> getList() {
        List<String> list = new ArrayList<>();
        list.add("http://pic2.cxtuku.com/00/02/31/b945758fd74d.jpg");
        list.add("http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=214931719,1608091472&fm=23&gp=0.jpg");
        list.add("http://imgsrc.baidu.com/forum/pic/item/cefc1e178a82b9017ad72597738da9773912ef18.jpg");
        return list;
    }

    private void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
