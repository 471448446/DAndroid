package better.hello.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import better.hello.http.api.Api;

/**
 * Des sourceType->考虑在这个页面集成其他的资讯，比如讲网易，知乎新闻几种在一起
 * Create By better on 2016/10/19 14:00.
 */
public class NewsChannelBean implements Parcelable {
    public static final int UN_SELECT = 0;
    public static final int SELECTED = UN_SELECT + 1;
    private String name;
    private int sourceType;
    private int select;
    private NetEaseChannel mNetEaseChannel;

    public NewsChannelBean(String name, int sourceType, int select) {
        this.name = name;
        this.sourceType = sourceType;
        this.select = select;
    }

    public NewsChannelBean(String name, int sourceType, NetEaseChannel netEaseChannel) {
        this.name = name;
        this.sourceType = sourceType;
        this.mNetEaseChannel = netEaseChannel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public boolean isSelect() {
        return isSelect(this.select);
    }

    public static boolean isSelect(int p) {
        return p == SELECTED;
    }

    public NetEaseChannel getNetEaseChannel() {
        return mNetEaseChannel;
    }

    public void setNetEaseChannel(NetEaseChannel netEaseChannel) {
        this.mNetEaseChannel = netEaseChannel;
    }

    public NewsChannelBean() {
    }

    public static class NetEaseChannel implements Parcelable {
        private String channelId;

        public NetEaseChannel(String channelId) {
            this.channelId = channelId;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getType() {
            return Api.getType(channelId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.channelId);
        }

        protected NetEaseChannel(Parcel in) {
            this.channelId = in.readString();
        }

        public static final Creator<NetEaseChannel> CREATOR = new Creator<NetEaseChannel>() {
            @Override
            public NetEaseChannel createFromParcel(Parcel source) {
                return new NetEaseChannel(source);
            }

            @Override
            public NetEaseChannel[] newArray(int size) {
                return new NetEaseChannel[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.sourceType);
        dest.writeInt(this.select);
        dest.writeParcelable(this.mNetEaseChannel, flags);
    }

    protected NewsChannelBean(Parcel in) {
        this.name = in.readString();
        this.sourceType = in.readInt();
        this.select = in.readInt();
        this.mNetEaseChannel = in.readParcelable(NetEaseChannel.class.getClassLoader());
    }

    public static final Creator<NewsChannelBean> CREATOR = new Creator<NewsChannelBean>() {
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
