package better.hello.common;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import better.hello.R;
import better.hello.common.dialog.DialogHelper;
import better.hello.data.bean.DownloadInfo;
import better.hello.data.bean.DownloadingInfo;
import better.hello.data.bean.ImagesDetailsBean;
import better.hello.data.bean.NetEaseNewsListBean;
import better.hello.data.bean.NetEasyImgBean;
import better.hello.data.bean.NewsDetailsBean;
import better.hello.http.HttpUtil;
import better.hello.http.call.RequestInfo;
import better.hello.http.download.DownLoadService;
import better.hello.util.C;
import better.hello.util.FileUtils;
import better.lib.utils.NetUtils;
import okhttp3.ResponseBody;
import rx.Emitter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by better on 2016/11/3.
 */

public class UIHelper {
    /**
     * Des 需要申明权限
     *
     * @param bean 确保文件夹已创建
     *             Create By better on 2016/11/9 16:47.
     */
    public static DownloadInfo downLoad(final Activity activity, final DownloadInfo bean) {
        if (NetUtils.is3gNet(activity)) {
            DialogHelper.getSimpleSubmitDialogFra(activity.getString(R.string.down_tip_3gp), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    downLoadWithNotify(activity, bean);
                }
            }).showDialog(((AppCompatActivity) activity).getSupportFragmentManager());
        } else {
            downLoadWithNotify(activity, bean);
        }
        return bean;
    }

    public static DownloadInfo downLoadWithNotify(Activity activity, final DownloadInfo bean) {
        if (PermissionGrantHelper.isGrantedThisPermissionOrGrantDirect(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionGrantHelper.REQ_CODE_LOCATION)) {
            Intent intent = new Intent(activity, DownLoadService.class);
            intent.putExtra(C.EXTRA_BEAN, bean);
            activity.startService(intent);
        }
        return bean;
    }

    /**
     * Des 下载文件到本地
     * Create By better on 2016/11/8 23:16.
     */
    public static Subscription downLoad(RequestInfo<DownloadingInfo> requestInfo, final String fileName, String mp4_url) {
        return HttpUtil.downFile(mp4_url).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).flatMap(new Func1<ResponseBody, Observable<DownloadingInfo>>() {
            @Override
            public Observable<DownloadingInfo> call(final ResponseBody responseBody) {
                return Observable.fromEmitter(new Action1<Emitter<DownloadingInfo>>() {
                    @Override
                    public void call(Emitter<DownloadingInfo> downloadInfoEmitter) {
                        FileUtils.writeFile(downloadInfoEmitter, responseBody, fileName);
                    }
                }, Emitter.BackpressureMode.BUFFER);
            }
        }).onBackpressureLatest().observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<>(requestInfo));
    }
//    public static Subscription downLoad(RequestInfo<DownloadingInfo> requestInfo, final String fileName, String mp4_url) {
//        return HttpUtil.downFile(mp4_url).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).flatMap(new Func1<ResponseBody, Observable<DownloadingInfo>>() {
//            @Override
//            public Observable<DownloadingInfo> call(final ResponseBody responseBody) {
//                return Observable.fromEmitter(new Action1<Emitter<DownloadingInfo>>() {
//                    @Override
//                    public void call(Emitter<DownloadingInfo> downloadInfoEmitter) {
//                        FileUtils.writeFile(downloadInfoEmitter, responseBody, fileName);
//                    }
//                }, Emitter.BackpressureMode.BUFFER);
//            }
//        }).onBackpressureBuffer().observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<>(requestInfo));
//    }

    public static List<ImagesDetailsBean> getImage(List<NetEaseNewsListBean.ImgextraBean> listImg, List<NetEaseNewsListBean.AdsBean> listAds) {
        ArrayList<ImagesDetailsBean> list = new ArrayList<>();
        if (null != listAds) {
            for (NetEaseNewsListBean.AdsBean im : listAds) {
                list.add(new ImagesDetailsBean(im.getTitle(), im.getImgsrc(), "", im.getUrl()));
            }
        } else if (null != listImg) {
            for (NetEaseNewsListBean.ImgextraBean im : listImg) {
                list.add(new ImagesDetailsBean("", im.getImgsrc()));
            }
        }
        return list;
    }

    public static List<ImagesDetailsBean> getImage(List<NewsDetailsBean.ImgBean> listImg) {
        ArrayList<ImagesDetailsBean> list = new ArrayList<>();
        if (null != listImg) {
            for (NewsDetailsBean.ImgBean im : listImg) {
                list.add(new ImagesDetailsBean("", im.getSrc()));
            }
        }
        return list;
    }

    public static List<ImagesDetailsBean> prase(NetEasyImgBean bean) {
        List<ImagesDetailsBean> list = new ArrayList<>();
        for (NetEasyImgBean.PhotosBean b : bean.getPhotos()) {
            /* 文章的标题  --better 2017/1/12 14:59. */
            list.add(new ImagesDetailsBean(bean.getSetname(), b.getImgurl(), b.getNote()));
        }
        return list;
    }
}
