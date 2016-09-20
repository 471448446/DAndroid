package better.rxd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Des
 * 工作现场和UI线程的切换有点点绕:http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/1006/3543.html
 * Create By better on 16/9/18 16:37.
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    NameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        adapter = new NameAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        helloRx();
    }

    private void helloRx() {
        Observable observable = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> observer) {
                observer.onNext("Hello RxJava");
                observer.onCompleted();
            }
        });
        observable.subscribe(new Observer() {
            @Override
            public void onCompleted() {
                toast("完成");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                toast((String) o);
            }
        });
        //简化创建 如果我们只关心onNext
        Observable.just("Hello RxJava").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                l("只关心 onNext()" + s);
            }
        });
        //操作符号
        Observable.just("Hello RrJava", "", "not null")/*耗时操作在io,显示在UI Thread*/.subscribeOn(Schedulers.newThread())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        l("我们将处理非空的数据:" + s);
                        if (TextUtils.isEmpty(s)) {
                            try {
                                Thread.sleep(6000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
//                if (TextUtils.isEmpty(s))throw new RuntimeException("先抛出一个错误:null String");
                    }
                }).observeOn(AndroidSchedulers.mainThread()).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return !TextUtils.isEmpty(s);
            }
        }).map(new Func1<String, Object>() {
            @Override
            public Object call(String s) {
                return s + " 转化一次变为object。";
            }
        }).map(new Func1<Object, String>() {
            @Override
            public String call(Object o) {
                return String.valueOf(o) + ",在转换一次。";
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                l("过程产生错误,程序终止,但不会crash:" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                l("结果数据:" + s);
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void l(String msg) {
        Log.d("MainActivity", msg);
    }
}
