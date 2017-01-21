package better.hello.ui.news.detail;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import better.hello.R;
import better.hello.common.UIHelper;
import better.hello.data.bean.DownloadInfo;
import better.hello.data.bean.VideoBean;
import better.hello.ui.base.BaseActivity;
import better.hello.util.C;
import better.hello.util.FileUtils;
import better.hello.util.ImageUtil;
import better.hello.common.PermissionGrantHelper;
import better.hello.util.Utils;
import butterknife.BindView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Des https://github.com/lipangit/JieCaoVideoPlayer
 * 有些链接视频不能下载
 * Create By better on 2016/11/2 15:43.
 */
public class NewsVideoActivity extends BaseActivity {
    @BindView(R.id.custom_videoplayer_standard)
    JCVideoPlayerStandard video;
    @BindView(R.id.video_download)
    ImageView imgDownLoad;

    private JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private SensorManager sensorManager;
    private VideoBean mBean;
    private DownloadInfo mDownloadInfo;

    public static void start(Context ctx, VideoBean bean) {
        Intent intent = new Intent(ctx, NewsVideoActivity.class);
        intent.putExtra(C.EXTRA_BEAN, bean);
        ctx.startActivity(intent);
    }

    @Override
    protected void onPerMissionGranted(int requestCode, String permissions, int grantResults) {
        super.onPerMissionGranted(requestCode, permissions, grantResults);
        if (PermissionGrantHelper.isGranted(grantResults)) {
            if (null != mDownloadInfo) UIHelper.downLoad(mContext, mDownloadInfo);
        }
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        mBean = getIntent().getParcelableExtra(C.EXTRA_BEAN);
//        if (null == mBean) mBean = new VideoBean();
//        mBean = new VideoBean("http://vimg3.ws.126.net/image/snapshot/2016/10/J/7/VC3PDSOJ7.jpg", "ha", "http://flv2.bn.netease.com/videolib3/1610/31/GRiHf1453/HD/GRiHf1453-mobile.mp4");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_video);
        if (null == mBean || TextUtils.isEmpty(mBean.getMp4_url())) return;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        video.setUp(mBean.getMp4_url(), JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, mBean.getAlt());
        JCVideoPlayer.setJcUserAction(new UserAction());
        if (!TextUtils.isEmpty(mBean.getCover()))
            ImageUtil.load(mContext, mBean.getCover(), video.thumbImageView);
        //全屏
//        video.startWindowFullscreen();
//        JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class, url, "视频");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null!=sensorManager){
            Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null!=sensorManager){
            sensorManager.unregisterListener(sensorEventListener);
            JCVideoPlayer.releaseAllVideos();
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @OnClick({R.id.video_download})
    public void onClick(View view) {
        mDownloadInfo = UIHelper.downLoad(mContext, new DownloadInfo(mBean.getAlt(), mBean.getMp4_url(), FileUtils.getVideoFileName(mContext, mBean.getAlt())));
    }

    class UserAction implements JCUserActionStandard {

        @Override
        public void onEvent(int type, String url, int screen, Object... objects) {
            switch (type) {
                case JCUserActionStandard.ON_CLICK_START_THUMB:
                    Utils.setGone(imgDownLoad);
                    break;
                case JCUserActionStandard.ON_CLICK_BLANK:
                    if (imgDownLoad.getVisibility() == View.VISIBLE) {
                        Utils.setGone(imgDownLoad);
                    } else {
                        Utils.setVisible(imgDownLoad);
                    }
                    break;
                default:
                    Log.i("Buried_Point", "unknow");
                    break;
            }
        }
    }
}
