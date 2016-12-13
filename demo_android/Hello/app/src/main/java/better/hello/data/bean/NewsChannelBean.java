package better.hello.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Des sourceType->考虑在这个页面集成其他的资讯，比如讲网易，知乎新闻几种在一起
 * Create By better on 2016/10/19 14:00.
 */
public class NewsChannelBean implements Parcelable {
    private String name;
    private int sourceType;
    private boolean isSelect;
    private NetEaseChannel mNetEaseChannel;

    //    public NewsChannelBean(String name, String channelId, String type) {
    //        this.name = name;
    //        this.channelId = channelId;
    //        this.type = type;
    //    }
    //
    //    public NewsChannelBean(String name, String channelId, String type, int sourceType) {
    //        this.name = name;
    //        this.channelId = channelId;
    //        this.type = type;
    //        this.sourceType = sourceType;
    //    }
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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public NetEaseChannel getNetEaseChannel() {
        return mNetEaseChannel;
    }

    public void setNetEaseChannel(NetEaseChannel netEaseChannel) {
        this.mNetEaseChannel = netEaseChannel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.sourceType);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.mNetEaseChannel, flags);
    }

    public NewsChannelBean() {
    }

    protected NewsChannelBean(Parcel in) {
        this.name = in.readString();
        this.sourceType = in.readInt();
        this.isSelect = in.readByte() != 0;
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

    public static class NetEaseChannel implements Parcelable {
        private String channelId;
        private String type;

        public NetEaseChannel(String channelId, String type) {
            this.channelId = channelId;
            this.type = type;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.channelId);
            dest.writeString(this.type);
        }

        protected NetEaseChannel(Parcel in) {
            this.channelId = in.readString();
            this.type = in.readString();
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

}
