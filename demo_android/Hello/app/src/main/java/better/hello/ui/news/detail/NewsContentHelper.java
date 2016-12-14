package better.hello.ui.news.detail;

import android.app.Activity;
import android.text.TextUtils;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import better.hello.common.BaseSubscriber;
import better.hello.data.bean.ImagesDetailsBean;
import better.hello.data.bean.NewsDetailsBean;
import better.hello.http.HttpUtil;
import better.hello.http.api.NewsSourceType;
import better.hello.http.call.RequestCallback;
import better.hello.http.call.RequestInfo;
import better.hello.util.C;
import better.hello.util.Utils;
import better.lib.waitpolicy.DialogPolicy;
import rx.functions.Func1;

/**
 * Des 咨询显示类
 * Create By better on 2016/12/13 14:14.
 */
public class NewsContentHelper {
    private Activity mActivity;
    private int sourceType;
    private String newsId;
    private String link;
    private WebView mWebView;
    //
    private List<ImagesDetailsBean> listImgs = new ArrayList<>();
    private NewsDetailsBean mNewsDetailsBean;

    public List<ImagesDetailsBean> getListImgs() {
        return listImgs;
    }

    public NewsDetailsBean getNewsDetailsBean() {
        return mNewsDetailsBean;
    }

    public NewsContentHelper(Activity activity, int sourceType, String newsId, WebView webView) {
        mActivity = activity;
        this.sourceType = sourceType;
        this.newsId = newsId;
        mWebView = webView;
    }

    private void parseData(NewsDetailsBean newsContent) {
        mNewsDetailsBean = newsContent;
        //test video
//        NewsDetailsBean.VideoBean bean = new NewsDetailsBean.VideoBean();
//        bean.setCover("http://vimg3.ws.126.net/image/snapshot/2016/10/S/M/VC3PFG2SM.jpg");
//        bean.setRef("<!--VIDEO#0-->");
//        List<NewsDetailsBean.VideoBean> list = new ArrayList<>();
//        list.add(bean);
//        newsContent.setVideo(list);
//        mNewsDetailsBean.setBody(mNewsDetailsBean.getBody() + "<!--VIDEO#0-->");
        String body = newsContent.getBody();
        String title = newsContent.getTitle();
        String source = newsContent.getSource();
        String shareLink = newsContent.getShareLink();
        if (!TextUtils.isEmpty(shareLink)) {
            this.link = shareLink;
        }
        List<NewsDetailsBean.LinkBean> link = newsContent.getLink();
        if (null != link && !link.isEmpty()) {
            shareLink = body;
            for (NewsDetailsBean.LinkBean linkBean : link) {
                CharSequence ref = linkBean.getRef();
                String href = linkBean.getHref();
                shareLink = shareLink.replace(ref, "<a href=\"" + href + "\">" + linkBean.getTitle() + "</a>");
            }
            body = shareLink;
        }
        shareLink = newsContent.getPtime();
        String substring = shareLink.substring(5, shareLink.length() - 3);
        List<NewsDetailsBean.ImgBean> img = newsContent.getImg();
        listImgs.clear();
        if (null != img && !img.isEmpty()) {
            shareLink = body;
            for (NewsDetailsBean.ImgBean img2 : img) {
                listImgs.add(new ImagesDetailsBean(img2.getAlt(), img2.getSrc()));
                shareLink = shareLink.replace(img2.getRef(), "<img src=\"" + img2.getSrc() + "\"" + getImageClickListener(null) + "width=\"100%\" height=\"auto\">");
//                shareLink = shareLink.replace(img2.getRef(), "<img src=\"" + img2.getSrc() + "\" width=\"100%\" height=\"auto\">");
            }
            body = shareLink;
        }
        List<NewsDetailsBean.VideoBean> video = newsContent.getVideo();
        if (null != video && !video.isEmpty()) {
            shareLink = body;
            for (NewsDetailsBean.VideoBean video2 : video) {
//                shareLink = shareLink.replace(video2.getRef(), "<video width=\"95%\" height=\"auto\" poster=\"" + video2.getCover() + "\" controls=\"controls\">" + "<source src=\"" + video2.getUrl_mp4() + "\"type=\"video/mp4\" /></video>");
                shareLink = shareLink.replace(video2.getRef(), getVideoLink(video2.getCover()));
            }
            body = shareLink;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>").append("<head>").append("<link rel=\"stylesheet\" type=\"text/css\" href=\"netease_news_content_style.css\"/>").append("</head>").append("<body>").append("<div class=\"title\">").append("<h3>").append(title).append("</h3>").append("<div class=\"meta\">").append("<span class=\"source\">").append(source).append(" </span><span class=\"time\">").append(substring).append("</span>\n").append("</div>").append("</div>").append("<div class=\"content\">").append(body).append("</div>").append("</body>").append("</html>");
//        if (ComUtils.isCellularData() && PrefUtil.getBoolean("isNoImage", false)) {
//            stringBuilder.append(Constant.HTMLJSNOIMAGE);
//        }
        Utils.d("Better", stringBuilder.toString());
        this.mWebView.loadDataWithBaseURL("file:///android_asset/", stringBuilder.toString(), "text/html", "UTF-8", null);
    }

    private CharSequence getVideoLink(String url) {
        return "       <div class=\"video-holder\">" +
                "         <img src=\"" + url + "\" width=\"100%\" height=\"auto\"" + getImageClickListener(url + C.MAP4) + "/>" +
                "         <img class=\"play-icon\" src=\"file:///android_asset/video_play.png\"" + getImageClickListener(url + C.MAP4) + "/>" +
                "       </div>";
    }

    private String getImageClickListener(String url) {
        if (TextUtils.isEmpty(url))
            return "onclick=\"window.app.openImage(this.src)\"";
        else {
            return "onclick=\"window.app.openImage('" + url + "')\"";
        }
    }

    public void showContent() {
        switch (this.sourceType) {
            case NewsSourceType.NETEASE:
                HttpUtil.getNewDetail(newsId).map(new Func1<Map<String, NewsDetailsBean>, NewsDetailsBean>() {
                    @Override
                    public NewsDetailsBean call(Map<String, NewsDetailsBean> stringNewsDetailsBeanMap) {
                        return stringNewsDetailsBeanMap.get(newsId);
                    }
                }).subscribe(new BaseSubscriber<>(new RequestInfo<>(new RequestCallback<NewsDetailsBean>() {
                    @Override
                    public void onError(RequestInfo<NewsDetailsBean> requestInfo, String msg) {

                    }

                    @Override
                    public void onSuccess(RequestInfo<NewsDetailsBean> requestInfo, NewsDetailsBean data, Object o) {
                        parseData(data);
                    }

                    @Override
                    public void onStart(RequestInfo<NewsDetailsBean> requestInfo) {

                    }

                    @Override
                    public void onComplete(RequestInfo<NewsDetailsBean> requestInfo) {
                    }
                }, new DialogPolicy(mActivity))));
                break;
        }
    }
}
