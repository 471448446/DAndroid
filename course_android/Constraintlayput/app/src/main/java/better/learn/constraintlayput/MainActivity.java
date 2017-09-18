package better.learn.constraintlayput;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_Act:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btn_login_Feed:
                startActivity(new Intent(this, FeedActivity.class));
                break;
        }
    }
}
