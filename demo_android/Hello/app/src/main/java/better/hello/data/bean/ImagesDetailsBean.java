package better.hello.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by better on 2016/10/19.
 */

public class ImagesDetailsBean implements Parcelable {
    private String title;
    private String src;

    public ImagesDetailsBean(String title, String src) {
        this.title = title;
        this.src = src;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.src);
    }

    public ImagesDetailsBean() {
    }

    protected ImagesDetailsBean(Parcel in) {
        this.title = in.readString();
        this.src = in.readString();
    }

    public static final Parcelable.Creator<ImagesDetailsBean> CREATOR = new Parcelable.Creator<ImagesDetailsBean>() {
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
