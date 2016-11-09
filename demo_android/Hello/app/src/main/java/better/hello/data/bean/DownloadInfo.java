package better.hello.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Des 请求的下载信息
 * Create By better on 2016/11/9 21:12.
 */
public class DownloadInfo implements Parcelable {
    private String title, url, fileName;

    public DownloadInfo(String title, String url, String fileName) {
        this.title = title;
        this.url = url;
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.fileName);
    }

    protected DownloadInfo(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        this.fileName = in.readString();
    }

    public static final Parcelable.Creator<DownloadInfo> CREATOR = new Parcelable.Creator<DownloadInfo>() {
        @Override
        public DownloadInfo createFromParcel(Parcel source) {
            return new DownloadInfo(source);
        }

        @Override
        public DownloadInfo[] newArray(int size) {
            return new DownloadInfo[size];
        }
    };
}
