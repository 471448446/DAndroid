package better.slidebar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * https://github.com/saiwu-bigkoo
 */
public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SlideBarView slideBarView;
    TextView textView;

    HashMap<String, Integer> letterAndPosotion = new HashMap<>();
    ArrayList<String> listLetter = new ArrayList<>();
    List<CityBean> citys = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        slideBarView = (SlideBarView) findViewById(R.id.slideBar);
        textView = (TextView) findViewById(R.id.txt);

        textView.setVisibility(View.GONE);
        slideBarView.setListener(new SlideBarView.OnScrollListener() {
            @Override
            public void onChoose(int p, String letter) {
                textView.setText(letter);
                if (letterAndPosotion.containsKey(letter)) {
                    if (listLetter.indexOf(letter) == listLetter.size() - 1) {
                        recyclerView.scrollToPosition(citys.size() - 1);
                    } else{
                        recyclerView.scrollToPosition(letterAndPosotion.get(letter));
                    }
                }
            }

            @Override
            public void onUp(boolean isUp) {
                if (!isUp) {
                    textView.setVisibility(View.VISIBLE);
                } else
                    textView.setVisibility(View.GONE);
            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                slideBarView.setABC(getAbc());
//
//            }
//        }, 1000);
        for (CityBean bean : getCity()) {
            if (!TextUtils.isEmpty(bean.getLetter()) && !letterAndPosotion.containsKey(bean.getLetter())) {
                citys.add(new CityBean("", "", bean.getLetter()));
                letterAndPosotion.put(bean.getLetter(), citys.size() - 1);
                listLetter.add(bean.getLetter());
            }
            citys.add(bean);
        }
        slideBarView.setABC(listLetter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new CityAdapter(citys));

    }

    private List<CityBean> getCity() {
        List<CityBean> list = new ArrayList<>();
//        list.add(new CityBean("", "", "C"));
        list.add(new CityBean("成都", "C", ""));
        list.add(new CityBean("成都3", "C", ""));
        list.add(new CityBean("成都4", "C", ""));
        list.add(new CityBean("成都6", "C", ""));
        list.add(new CityBean("成都0", "C", ""));
        list.add(new CityBean("成都010", "C", ""));
//        list.add(new CityBean("", "", "B"));
        list.add(new CityBean("北京", "B", ""));
        list.add(new CityBean("北京1", "B", ""));
        list.add(new CityBean("北京12", "B", ""));
        list.add(new CityBean("北京123", "B", ""));
        list.add(new CityBean("北京1234", "B", ""));
        list.add(new CityBean("北京12345", "B", ""));
        list.add(new CityBean("北京123456", "B", ""));
        list.add(new CityBean("北京1234567", "B", ""));
//        list.add(new CityBean("", "", "G"));
        list.add(new CityBean("广安", "G", ""));
        list.add(new CityBean("广安南", "G", ""));
        list.add(new CityBean("广安东", "G", ""));
//        list.add(new CityBean("", "", "H"));
        list.add(new CityBean("杭州", "H", ""));
        list.add(new CityBean("杭州2", "H", ""));
        list.add(new CityBean("杭州3", "H", ""));
        list.add(new CityBean("金山", "J", ""));
        list.add(new CityBean("金到", "J", ""));
        list.add(new CityBean("金看看", "J", ""));
//        list.add(new CityBean("", "", "S"));
        list.add(new CityBean("深圳", "S", ""));
        list.add(new CityBean("山东", "S", ""));
        list.add(new CityBean("新疆", "X", ""));
        list.add(new CityBean("绍兴", "X", ""));
        list.add(new CityBean("邵东", "X", ""));
        list.add(new CityBean("延安", "Y", ""));
        list.add(new CityBean("延安dd", "Y", ""));
        list.add(new CityBean("延安西", "Y", ""));
        return list;
    }

    private List<String> getAbc() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        list.add("G");
        list.add("H");
        list.add("I");
        list.add("J");
        list.add("K");
        list.add("L");
        list.add("M");
        list.add("N");
        list.add("O");
        list.add("P");
        list.add("Q");
        list.add("R");
        list.add("S");
        list.add("T");
        list.add("U");
        list.add("V");
        list.add("W");
        list.add("X");
        list.add("Y");
        list.add("Z");
//        list.add("Y");
//        list.add("Z");
// list.add("Y");
//        list.add("Z");
// list.add("Y");
//        list.add("Z");
        return list;
    }
}
