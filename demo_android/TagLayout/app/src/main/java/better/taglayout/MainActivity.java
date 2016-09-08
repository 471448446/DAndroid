package better.taglayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import better.library.FlowTagLayout;

public class MainActivity extends AppCompatActivity {
    FlowTagLayout flowTagLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flowTagLayout = (FlowTagLayout) findViewById(R.id.tagLayout);
        flowTagLayout.setAdapter(new TagsAdapter(getList(), this));
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                tagLayout.setAdapter(new TagsAdapter(getList2(), MainActivity.this));
//            }
//        },3000);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ((TagsAdapter)tagLayout.getAdapter()).setList(getList2());
//            }
//        },3000);
        List<String> list=new ArrayList<>();
        list.add("傻逼1");
        list.add("傻逼2");
        list.add("傻逼3");
        ((FlowTagLayout)findViewById(R.id.tagLayout2)).setAdapter(new ArrayAdapter<>(
                MainActivity.this,R.layout.dialog_item_unsubreason2, R.id.dialog_unsub_reason_item_tv,list));
    }

    private List<String> getList() {
        List<String> list = new ArrayList<>();
        list.add("不买了");
        list.add("不买了,完全的完全");
        list.add("买多了");
        list.add("按时打算发");
        list.add("按法师打发第三方");
        list.add("开口道方法方法哦哦我");
        return list;
    }
    private List<String> getList2() {
        List<String> list = new ArrayList<>();
        list.add("不买了");
        list.add("不买了,完全的完全");
        list.add("买多了");
        return list;
    }
}
