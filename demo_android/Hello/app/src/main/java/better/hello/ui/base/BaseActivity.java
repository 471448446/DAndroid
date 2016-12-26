package better.hello.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import better.hello.R;
import better.hello.util.PermissionGrantHelper;
import better.hello.util.Utils;
import better.lib.waitpolicy.dialog.WaitDialog;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by better on 2016/10/17.
 */

public class BaseActivity extends AppCompatActivity {
    public static final int REQ_NONE = -100;
    protected Subscription mSubscription;

    public Activity mContext;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (null == grantResults && grantResults.length == 0) {
            l("授权---->onRequestPermissionsResult() null");
            return;
        }
        if (PermissionGrantHelper.isGranted(grantResults[0])) {
            onPerMissionGranted(requestCode, permissions[0], grantResults[0]);
        } else {
            onPerMissionRefuse(requestCode, permissions[0], grantResults[0]);
            if (!PermissionGrantHelper.shouldShowRequestPermissionRationale(mContext, permissions[0])) {
                onShouldShowRequestPermissionRationale(requestCode, permissions[0], grantResults[0]);
            }
        }
    }

    protected void onPerMissionGranted(int requestCode, String permissions, int grantResults) {
        if (PermissionGrantHelper.REQ_CODE_PHONE == requestCode) {
        }
    }

    protected void onPerMissionRefuse(int requestCode, String permissions, int grantResults) {

    }

    protected void onShouldShowRequestPermissionRationale(int requestCode, String permissions, int grantResults) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WaitDialog.destroyDialog(mContext);
        if (null != mSubscription) mSubscription.unsubscribe();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        getArgs();
        initData();
    }

    protected void l(String msg) {
        Utils.d(this.getClass().getSimpleName(), msg);
    }

    protected void toast(String msg) {
        Utils.toastShort(mContext, msg);
    }

    /**
     * 参数传递
     */
    protected void getArgs() {
    }

    protected void initData() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (preBackExitPage()) {
                return true;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void forward(Class<?> d) {
        forward(d, null);
    }

    protected void forward(Class<?> d, Bundle b) {
        forward(d, b, REQ_NONE);
    }

    protected void forward(Class<?> d, Bundle b, int req) {
        Intent i = new Intent(this, d);
        if (null != b) i.putExtras(b);
        if (REQ_NONE == req)
            startActivity(i);
        else
            startActivityForResult(i, req);
    }

    protected Toolbar setBackToolBar(Toolbar toolBar) {
        return setBackToolBar(toolBar, "");
    }

    protected Toolbar setBackToolBar(int toolBar) {
        return setBackToolBar(toolBar, "");
    }

    protected Toolbar setBackToolBar(Toolbar toolBar, int msg) {
        return setBackToolBar(toolBar, getString(msg));
    }

    protected Toolbar setBackToolBar(int toolBar, int msg) {
        return setBackToolBar((Toolbar) findViewById(toolBar), getString(msg));
    }

    protected Toolbar setBackToolBar(int toolBar, String msg) {
        return setBackToolBar((Toolbar) findViewById(toolBar), msg);
    }

    protected Toolbar setBackToolBar(Toolbar toolBar, String msg) {
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolBar.setNavigationIcon(R.drawable.icon_navgation_back_white);
        getSupportActionBar().setTitle(msg);
        return toolBar;
    }

    /**
     * 退出之前，如果需要额外处理，调用此方法
     *
     * @return {@link #onKeyDown(int, KeyEvent) onKeyDown}后续执行
     * </br><b>true</b>：	直接返回，停留在当前页面；
     * </br><b>false</b>：	继续执行父类操作
     * </br>{@link #onBackButtonClick(View) onBackButtonClick}后续执行
     * </br><b>true</b>：	直接返回，停留在当前页面；
     * </br><b>false</b>：	继续执行退出后续操作。
     * @see #onKeyDown(int, KeyEvent)
     * @see #onBackButtonClick(View)
     */
    protected boolean preBackExitPage() {
        return false;
    }

    /**
     * 标题返回，点击事件
     * xml文件onClick配置
     */
    public void onBackButtonClick(View view) {
        if (preBackExitPage()) {
            return;
        }
        finish();
    }

}
