package better.hello.data.bean;

/**
 * Created by better on 2017/1/12.
 */

public class ShareBean {
    private String title;
    private String shareUrl;

    public ShareBean(String title, String shareUrl) {
        this.title = title;
        this.shareUrl = shareUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
