package better.hello.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import better.hello.common.UIHelper;
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
    private String json;

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

    public NewsListBean() {
    }
    public static NewsListBean convert(NetEaseNewsListBean bean,boolean isNeedJsonStr){
        NewsListBean n=new NewsListBean();
        n.setTitle(bean.getTitle());
        n.setPub_date(bean.getPtime());
        n.setImgSrc(bean.getImgsrc());
        n.setNewsId(bean.getPostid());
        n.setImgs(UIHelper.getImage(bean.getImgextra(),bean.getAds()));
        if (isNeedJsonStr)n.setJson(JsonUtils.toJson(bean));
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
        dest.writeString(this.json);
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
        this.json = in.readString();
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
