package better.hello.ui.splash;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import better.hello.R;
import better.hello.common.BaseSchedulerTransformer;
import better.hello.common.dialog.DialogHelper;
import better.hello.ui.MainActivity;
import better.hello.ui.base.BaseActivity;
import better.hello.util.FileUtils;
import better.hello.util.PermissionGrantHelper;
import better.hello.util.Utils;
import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Des http://news-at.zhihu.com/api/4/start-image/1080*1776
 * Create By better on 2016/10/26 11:08.
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.activity_splash_img)
    ImageView img;
    private static final int MSG_START_MAIN_ACTIVITY = 1;

    private static final long TIME_LOAD_MIN = 2000; // 页面最短停留时间
    private static final long TIME_LOAD_MAX = 5000; // 页面最长留时间
    private long loadTime = 0, startTime; // 记录启动新页面之前，花费的总时间


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_MAIN_ACTIVITY:
                    startMainActivity();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionGrantHelper.REQ_CODE_STORAGE:
                if (PermissionGrantHelper.isGranted(grantResults[0])) {
//                    if (!PermissionGrantHelper.isGrantedThisPermission(mContext, Manifest.permission.READ_PHONE_STATE)) {
//                        PermissionGrantHelper.grantPermission(mContext, PermissionGrantHelper.REQ_CODE_PHONE, Manifest.permission.READ_PHONE_STATE);
//                    } else {
//                        preStartMainActivity();
//                    }
                    loadSplashImage();
                } else {
                    showPermissionMsg(R.string.str_permission_refuse_file);
                }
                break;
            case PermissionGrantHelper.REQ_CODE_PHONE:
                if (PermissionGrantHelper.isGranted(grantResults[0])) {
                    preStartMainActivity();
                } else {
                    showPermissionMsg(R.string.str_permission_refuse_phoneStatus);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();
        prePermissionGrant();
    }

    private void prePermissionGrant() {
        if (!Utils.isBelowAndroidVersion(Build.VERSION_CODES.N)) {
            loadSplashImage();
        } else {
            if (!PermissionGrantHelper.isGrantedThisPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                PermissionGrantHelper.grantPermission(mContext, PermissionGrantHelper.REQ_CODE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }/* else if (!PermissionGrantHelper.isGrantedThisPermission(mContext, Manifest.permission.READ_PHONE_STATE)) {
                PermissionGrantHelper.grantPermission(mContext, PermissionGrantHelper.REQ_CODE_PHONE, Manifest.permission.READ_PHONE_STATE);
            } */ else {
                loadSplashImage();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void loadSplashImage() {
        if (!FileUtils.isLoadTodaySplash(mContext)) {
            img.setBackgroundResource(R.drawable.splash);
            mHandler.sendEmptyMessage(MSG_START_MAIN_ACTIVITY);
        } else {
            Observable.just(FileUtils.getTodaySplashImagePath(mContext)).compose(new BaseSchedulerTransformer<String>()).map(new Func1<String, Bitmap>() {
                @Override
                public Bitmap call(String s) {
                    return BitmapFactory.decodeFile(s);
                }
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Bitmap>() {
                @Override
                public void onCompleted() {
                    mHandler.sendEmptyMessage(MSG_START_MAIN_ACTIVITY);
                }

                @Override
                public void onError(Throwable e) {
                    toast(e.getMessage());
                    l(e.getMessage());
                    mHandler.sendEmptyMessage(MSG_START_MAIN_ACTIVITY);
                }

                @Override
                public void onNext(Bitmap bitmap) {
                    img.setImageBitmap(bitmap);
                }
            });
        }
    }

    private void showPermissionMsg(int msgId) {
        DialogHelper.getPermissionDeny(mContext, getString(msgId), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, null).showDialog(getSupportFragmentManager());
    }

    private void startMainActivity() {
        loadTime = System.currentTimeMillis() - startTime;
        if (TIME_LOAD_MIN > loadTime) {
            mHandler.sendEmptyMessageDelayed(MSG_START_MAIN_ACTIVITY, (TIME_LOAD_MIN - loadTime));
        } else {
            forwardMainActivity();
        }
    }

    private void preStartMainActivity() {
        mHandler.sendEmptyMessage(MSG_START_MAIN_ACTIVITY);
    }

    private void forwardMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
