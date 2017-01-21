package better.hello.data;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import better.hello.R;
import better.hello.data.bean.NewsChannelBean;
import better.hello.http.api.NewsSourceType;
import better.hello.util.FileUtils;
import better.hello.util.Utils;

/**
 * Created by better on 2016/10/19.
 */

public class SourceHelper {

    public static List<NewsChannelBean> getNewsChannel() {
        List<NewsChannelBean> list = new ArrayList<>();
        InputStream is = FileUtils.readFileFromRaw(R.raw.news_api);
        Document document = Utils.getDocumentByIS(is);
        if (null == document) return list;
        NodeList listName = document.getElementsByTagName("name");
        NodeList listId = document.getElementsByTagName("id");
        NodeList listSelect = document.getElementsByTagName("select");
        NodeList listType = document.getElementsByTagName("source");
        for (int i = 0, l = listName.getLength(); i < l; i++) {
            NewsChannelBean bean = new NewsChannelBean(listName.item(i).getTextContent(), Integer.valueOf(listType.item(i).getTextContent()), Integer.valueOf(listSelect.item(i).getTextContent()));
            int type = Integer.valueOf(listType.item(i).getTextContent());
            switch (type) {
                case NewsSourceType.NETEASE:
                    bean.setNetEaseChannel(new NewsChannelBean.NetEaseChannel(listId.item(i).getTextContent()));
                    break;
            }
            list.add(bean);
        }
        return list;
    }

}
