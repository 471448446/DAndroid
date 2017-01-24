package better.wmoney;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Des 学习一下AccessibilityService
 * 某天逛知乎的时候看见这个：
 * https://www.zhihu.com/question/28152693
 * Create By better on 2017/1/22 14:40.
 */
public class MainActivity extends AppCompatActivity implements AccessibilityManager.AccessibilityStateChangeListener {
    @Bind(R.id.main_start)
    Button tvAccessibility;
    private AccessibilityManager accessibilityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //监听AccessibilityService 变化
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(this);
        updateServiceStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessibilityManager.removeAccessibilityStateChangeListener(this);
    }

    private void updateServiceStatus() {
        if (isServiceEnabled()) {
            tvAccessibility.setText(R.string.service_off);
        } else {
            tvAccessibility.setText(R.string.service_on);
        }
    }

    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            l(info.getId());
            // 日志打出来是这个：better.wmoney/.HelpAccessibilityService
            if (info.getId().equals(getPackageName() + "/.HelpAccessibilityService")) {
                return true;
            }
        }
        return false;
    }

    @OnClick({R.id.main_start})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_start:
                startActivity(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
        }
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        updateServiceStatus();
    }

    private void l(String msg) {
        Log.d("MainActivity", msg);
    }
}
