package better.hello.data;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import better.hello.R;
import better.hello.data.bean.NewsChannleBean;
import better.hello.http.api.Api;
import better.hello.util.FileUtils;
import better.hello.util.Utils;

/**
 * Created by better on 2016/10/19.
 */

public class SourceHelper {

    public static List<NewsChannleBean> getNewsCHannle() {
        List<NewsChannleBean> list = new ArrayList<>();
        InputStream is = FileUtils.readFileFromRaw(R.raw.news_api);
        Document document = Utils.getDocmentByIS(is);
        if (null == document) return list;
        NodeList listName = document.getElementsByTagName("name");
        NodeList listId = document.getElementsByTagName("id");
        for (int i = 0, l = listName.getLength(); i < l; i++) {
            list.add(new NewsChannleBean(listName.item(i).getTextContent(), listId.item(i).getTextContent(), Api.getType(listId.item(i).getTextContent())));
        }
        return list;
    }

}