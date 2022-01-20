package com.test.rv.d1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.rv.databinding.ActivityDemo1Binding

/**
 * topic: 滑动和回收关系
 *
 * 线性布局，一个页面显示3个item（近乎三个，第三个没有显示完），最终只创建了7个Holder，其它的位置都复用了前面创建Holder
2021-11-25 14:01:35.382 18686-18686/com.test.rv D/Demo1: onCreateViewHolder() 184694545
2021-11-25 14:01:35.383 18686-18686/com.test.rv D/Demo1: Bind() 0 holder:184694545 create from index: 0
2021-11-25 14:01:35.389 18686-18686/com.test.rv D/Demo1: onCreateViewHolder() 128378487
2021-11-25 14:01:35.389 18686-18686/com.test.rv D/Demo1: Bind() 1 holder:128378487 create from index: 1
2021-11-25 14:01:35.394 18686-18686/com.test.rv D/Demo1: onCreateViewHolder() 54670157
2021-11-25 14:01:35.394 18686-18686/com.test.rv D/Demo1: Bind() 2 holder:54670157 create from index: 2
2021-11-25 14:01:41.077 18686-18686/com.test.rv D/Demo1: onCreateViewHolder() 55445121
2021-11-25 14:01:41.078 18686-18686/com.test.rv D/Demo1: Bind() 3 holder:55445121 create from index: 3
2021-11-25 14:01:41.341 18686-18686/com.test.rv D/Demo1: onCreateViewHolder() 117064486
2021-11-25 14:01:41.341 18686-18686/com.test.rv D/Demo1: Bind() 4 holder:117064486 create from index: 4
2021-11-25 14:01:52.426 18686-18686/com.test.rv D/Demo1: onCreateViewHolder() 241720167
2021-11-25 14:01:52.426 18686-18686/com.test.rv D/Demo1: Bind() 5 holder:241720167 create from index: 5
2021-11-25 14:02:02.292 18686-18686/com.test.rv D/Demo1: onCreateViewHolder() 116555028
2021-11-25 14:02:02.292 18686-18686/com.test.rv D/Demo1: Bind() 6 holder:116555028 create from index: 6
2021-11-25 14:02:05.538 18686-18686/com.test.rv D/Demo1: Bind() 7 holder:184694545 create from index: 0
2021-11-25 14:03:09.388 18686-18686/com.test.rv D/Demo1: Bind() 8 holder:128378487 create from index: 1
2021-11-25 14:03:11.838 18686-18686/com.test.rv D/Demo1: Bind() 9 holder:54670157 create from index: 2
2021-11-25 14:03:12.822 18686-18686/com.test.rv D/Demo1: Bind() 10 holder:55445121 create from index: 3
2021-11-25 14:03:14.122 18686-18686/com.test.rv D/Demo1: Bind() 11 holder:117064486 create from index: 4
2021-11-25 14:03:14.871 18686-18686/com.test.rv D/Demo1: Bind() 12 holder:241720167 create from index: 5
2021-11-25 14:03:15.172 18686-18686/com.test.rv D/Demo1: Bind() 13 holder:116555028 create from index: 6
2021-11-25 14:03:15.787 18686-18686/com.test.rv D/Demo1: Bind() 14 holder:184694545 create from index: 0
2021-11-25 14:03:15.904 18686-18686/com.test.rv D/Demo1: Bind() 15 holder:128378487 create from index: 1
2021-11-25 14:03:16.087 18686-18686/com.test.rv D/Demo1: Bind() 16 holder:54670157 create from index: 2
2021-11-25 14:03:16.572 18686-18686/com.test.rv D/Demo1: Bind() 17 holder:55445121 create from index: 3
2021-11-25 14:03:16.705 18686-18686/com.test.rv D/Demo1: Bind() 18 holder:117064486 create from index: 4
2021-11-25 14:03:16.820 18686-18686/com.test.rv D/Demo1: Bind() 19 holder:241720167 create from index: 5
 */
class Demo1Activity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDemo1Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.list.apply {
//            layoutManager = GridLayoutManager(this@Demo1Activity, 2)
            layoutManager = LinearLayoutManager(this@Demo1Activity)
            adapter = Adapter().also {
                it.listData.addAll(
                    (1..20).toList()
                )
            }
        }
    }
}