package better.dviewdraghelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class YouTubeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);
        findViewById(R.id.viewHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(YouTubeActivity.this,"头部",Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.viewDesc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(YouTubeActivity.this,"内容",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
