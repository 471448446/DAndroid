package better.learn.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import better.learn.databinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //省略了
//        setContentView(R.layout.activity_main);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        BookBean bookBean = new BookBean();
        bookBean.setName("沉默的大多数");
        bookBean.setAuther("王小波");
        bookBean.setDescription("这本杂文随笔集包括思想文化方面的文章，涉及知识分子的处境及思考，社会道德伦理，文化论争，国学与新儒家，民族主义等问题；包括从日常生活中发掘出来的各种真知灼见，涉及科学与邪道，女权主义等；包括对社会科学研究的评论，涉及性问题，生育问题，同性恋问题，社会研究的伦理问题和方法问题等；包括创作谈和文论，如写作的动机，作者的师承，作者对小说艺术的看法，作者对文体格调的看法，对影视的看法等；包括少量的书评，其中既有对文学经典的评论，也有对当代作家作品的一些看法；最后，还包括一些域外生活的杂感以及对某些社会现象的评点。");
        bookBean.setImgUrl("https://img1.doubanio.com/lpic/s1447349.jpg");

        binding.setBook(bookBean);
        binding.setHandler(new MyHandler());

        Glide.with(this).load(bookBean.getImgUrl()).into(binding.imageView);
    }

    public class MyHandler {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageView:
                    toast("沉默的大多数");
                    break;
                default:
                    toast("点击");
                    break;
            }
        }
    }

    private void toast(String ms) {
        Toast.makeText(MainActivity.this, ms, Toast.LENGTH_SHORT).show();

    }
}
