package better.hello.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Des sourceType->考虑在这个页面集成其他的资讯，比如讲网易，知乎新闻几种在一起
 * Create By better on 2016/10/19 14:00.
 */
public class NewsChannelBean implements Parcelable {
    private String name;
    private String channelId;
    private String type;
    private int sourceType;
    private boolean isSelect;

    public NewsChannelBean(String name, String channelId, String type) {
        this.name = name;
        this.channelId = channelId;
        this.type = type;
    }

    public NewsChannelBean(String name, String channelId, String type, int sourceType) {
        this.name = name;
        this.channelId = channelId;
        this.type = type;
        this.sourceType = sourceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.channelId);
        dest.writeString(this.type);
        dest.writeInt(this.sourceType);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    protected NewsChannelBean(Parcel in) {
        this.name = in.readString();
        this.channelId = in.readString();
        this.type = in.readString();
        this.sourceType = in.readInt();
        this.isSelect = in.readByte() != 0;
    }

    public static final Parcelable.Creator<NewsChannelBean> CREATOR = new Parcelable.Creator<NewsChannelBean>() {
        @Override
        public NewsChannelBean createFromParcel(Parcel source) {
            return new NewsChannelBean(source);
        }

        @Override
        public NewsChannelBean[] newArray(int size) {
            return new NewsChannelBean[size];
        }
    };
}
