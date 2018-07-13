package better.learn.proccess.demo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import better.learn.proccess.demo.R.id.main_txt
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showProcess(this)
        showMemory(this)
        main_txt.setOnClickListener {
            startActivity(Intent(this@MainActivity, RemoteActivity::class.java))
        }
    }

}
