package better.hello.data.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by better on 2016/10/19.
 */

public class ImagesDetailsBean implements Parcelable, Serializable {
    private static final long serialVersionUID = -1365453290538231699L;
    private String title;
    private String src;
    private String des;
    /* 跳转url  --better 2017/1/12 10:59. */
    private String url;

    public ImagesDetailsBean(String title, String src) {
        this.title = title;
        this.src = src;
    }

    public ImagesDetailsBean(String title, String src, String des) {
        this.title = title;
        this.src = src;
        this.des = des;
    }

    public ImagesDetailsBean(String title, String src, String des, String url) {
        this.title = title;
        this.src = src;
        this.des = des;
        this.url = url;
    }

    public String getDes() {
        return TextUtils.isEmpty(des) ? "" : des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public ImagesDetailsBean() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.src);
        dest.writeString(this.des);
        dest.writeString(this.url);
    }

    protected ImagesDetailsBean(Parcel in) {
        this.title = in.readString();
        this.src = in.readString();
        this.des = in.readString();
        this.url = in.readString();
    }

    public static final Creator<ImagesDetailsBean> CREATOR = new Creator<ImagesDetailsBean>() {
        @Override
        public ImagesDetailsBean createFromParcel(Parcel source) {
            return new ImagesDetailsBean(source);
        }

        @Override
        public ImagesDetailsBean[] newArray(int size) {
            return new ImagesDetailsBean[size];
        }
    };
}
