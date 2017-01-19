package better.hello.data.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

import better.hello.common.UIHelper;
import better.hello.http.api.NewsSourceType;
import better.hello.util.C;
import better.hello.util.JsonUtils;

/**
 * Created by better on 2016/11/24.
 */

public class NewsListBean implements Parcelable {
    private String boardid;
    private String des;
    private String imgSrc;
    /*文章详情*/
    private String newsId;
    private String pub_date;
    private String source;
    private String title;
    private int type;
    private String url_3w;
    private List<ImagesDetailsBean> imgs;
    private List<ImagesDetailsBean> ads;
    /*保存第一手信息*/
    private String json;
    /*数据种类*/
    private int sourceType;
    private int isCollect;

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect ? C.YES : C.NO;
    }

    public boolean isCollect() {
        return C.YES == isCollect;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public List<ImagesDetailsBean> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImagesDetailsBean> imgs) {
        this.imgs = imgs;
    }

    public String getBoardid() {
        return boardid;
    }

    public void setBoardid(String boardid) {
        this.boardid = boardid;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getPub_date() {
        return pub_date;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl_3w() {
        return url_3w;
    }

    public void setUrl_3w(String url_3w) {
        this.url_3w = url_3w;
    }

    public List<ImagesDetailsBean> getAds() {
        return ads;
    }

    public void setAds(List<ImagesDetailsBean> ads) {
        this.ads = ads;
    }

    public NewsListBean() {
    }

    public static NewsListBean convert(NetEaseNewsListBean bean, boolean needJsonStr, boolean ads) {
        NewsListBean n = new NewsListBean();
        n.setTitle(bean.getTitle());
        n.setPub_date(bean.getPtime());
        n.setImgSrc(bean.getImgsrc());
        n.setNewsId(TextUtils.isEmpty(bean.getPhotosetID()) ? bean.getPostid() : bean.getPhotosetID());
        n.setSourceType(NewsSourceType.NETEASE);
        n.setImgs(UIHelper.getImage(bean.getImgextra(), null));
        if (needJsonStr) n.setJson(JsonUtils.toJson(bean));
        if (ads) {
            List<ImagesDetailsBean> adsList = UIHelper.getImage(null, bean.getAds());
            if (null != bean.getAds() && !bean.getAds().isEmpty()) {
                ImagesDetailsBean adsItem = new ImagesDetailsBean();
                /*skipId 图集 postId 文章   --better 2017/1/12 16:29. */
                adsItem.setUrl(!TextUtils.isEmpty(bean.getSkipID()) ? bean.getSkipID() : bean.getPostid());
                adsItem.setTitle(bean.getTitle());
                adsItem.setSrc(bean.getImgsrc());
                adsList.add(adsItem);
            }
            n.setAds(adsList);
        }
        return n;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.boardid);
        dest.writeString(this.des);
        dest.writeString(this.imgSrc);
        dest.writeString(this.newsId);
        dest.writeString(this.pub_date);
        dest.writeString(this.source);
        dest.writeString(this.title);
        dest.writeInt(this.type);
        dest.writeString(this.url_3w);
        dest.writeTypedList(this.imgs);
        dest.writeTypedList(this.ads);
        dest.writeString(this.json);
        dest.writeInt(this.sourceType);
        dest.writeInt(this.isCollect);
    }

    protected NewsListBean(Parcel in) {
        this.boardid = in.readString();
        this.des = in.readString();
        this.imgSrc = in.readString();
        this.newsId = in.readString();
        this.pub_date = in.readString();
        this.source = in.readString();
        this.title = in.readString();
        this.type = in.readInt();
        this.url_3w = in.readString();
        this.imgs = in.createTypedArrayList(ImagesDetailsBean.CREATOR);
        this.ads = in.createTypedArrayList(ImagesDetailsBean.CREATOR);
        this.json = in.readString();
        this.sourceType = in.readInt();
        this.isCollect = in.readInt();
    }

    public static final Creator<NewsListBean> CREATOR = new Creator<NewsListBean>() {
        @Override
        public NewsListBean createFromParcel(Parcel source) {
            return new NewsListBean(source);
        }

        @Override
        public NewsListBean[] newArray(int size) {
            return new NewsListBean[size];
        }
    };
}
