package better.bindservices.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by better on 2017/6/6 21:54.
 */

public class MyMessage implements Parcelable {

    private String name;
    private String content;

    public MyMessage(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MyMessage{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.content);
    }

    public MyMessage() {
    }

    protected MyMessage(Parcel in) {
        this.name = in.readString();
        this.content = in.readString();
    }

    public static final Creator<MyMessage> CREATOR = new Creator<MyMessage>() {
        @Override
        public MyMessage createFromParcel(Parcel source) {
            return new MyMessage(source);
        }

        @Override
        public MyMessage[] newArray(int size) {
            return new MyMessage[size];
        }
    };
}
