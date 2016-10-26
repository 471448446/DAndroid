package better.hello.data.bean;

/**
 * Created by better on 2016/10/19.
 */

public class NewsChannleBean {
    private String name;
    private String channleId;
    private String type;
    private boolean isSelect;

    public NewsChannleBean(String name, String channleId, String type) {
        this.name = name;
        this.channleId = channleId;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannleId() {
        return channleId;
    }

    public void setChannleId(String channleId) {
        this.channleId = channleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
