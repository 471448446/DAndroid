package better.hello.data.bean;

/**
 * Created by better on 2016/11/3.
 */

public class DownloadingInfo {
    //    private double progress;
    private long readFileSize;
    private long totalFileSize;
    private String title;

    public DownloadingInfo(long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public DownloadingInfo(long readFileSize, long totalFileSize) {
        this.readFileSize = readFileSize;
        this.totalFileSize = totalFileSize;
    }

    public long getReadFileSize() {
        return readFileSize;
    }

    public void setReadFileSize(long readFileSize) {
        this.readFileSize = readFileSize;
    }

    public long getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
