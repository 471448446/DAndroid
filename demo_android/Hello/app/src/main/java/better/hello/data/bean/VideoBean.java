package better.hello.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by better on 2016/11/2.
 */

public class VideoBean implements Parcelable {

    /**
     * cover : http://vimg3.ws.126.net/image/snapshot/2016/10/S/M/VC3PFG2SM.jpg
     * alt : 山西两女子和一条狗围攻老人 警方：正调查
     * mp4_url : http://flv2.bn.netease.com/videolib3/1610/31/vSkze3141/SD/vSkze3141-mobile.mp4
     */

    private String cover;
    private String alt;
    private String mp4_url;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getMp4_url() {
        return mp4_url;
    }

    public void setMp4_url(String mp4_url) {
        this.mp4_url = mp4_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cover);
        dest.writeString(this.alt);
        dest.writeString(this.mp4_url);
    }

    public VideoBean() {
    }

    public VideoBean(String mp4_url) {
        this.mp4_url = mp4_url;
    }

    public VideoBean(String cover, String alt, String mp4_url) {
        this.cover = cover;
        this.alt = alt;
        this.mp4_url = mp4_url;
    }

    protected VideoBean(Parcel in) {
        this.cover = in.readString();
        this.alt = in.readString();
        this.mp4_url = in.readString();
    }

    public static final Parcelable.Creator<VideoBean> CREATOR = new Parcelable.Creator<VideoBean>() {
        @Override
        public VideoBean createFromParcel(Parcel source) {
            return new VideoBean(source);
        }

        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }
    };
}
