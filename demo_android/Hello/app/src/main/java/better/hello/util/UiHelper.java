package better.hello.util;

import java.util.ArrayList;
import java.util.List;

import better.hello.data.bean.ImagesDetailsBean;
import better.hello.data.bean.NewsDetailsBean;
import better.hello.data.bean.NewsListBean;

/**
 * Created by better on 2016/10/26.
 */

public class UiHelper {
    public static List<ImagesDetailsBean> getImage(List<NewsListBean.ImgextraBean> listImg, List<NewsListBean.AdsBean> listAds) {
        ArrayList<ImagesDetailsBean> list = new ArrayList<>();
        if (null != listAds) {
            for (NewsListBean.AdsBean im : listAds) {
                list.add(new ImagesDetailsBean(im.getTitle(), im.getImgsrc()));
            }
        } else if (null != listImg) {
            for (NewsListBean.ImgextraBean im : listImg) {
                list.add(new ImagesDetailsBean("", im.getImgsrc()));
            }
        }
        return list;
    }

    public static List<ImagesDetailsBean> getImage(List<NewsDetailsBean.ImgBean> listImg) {
        ArrayList<ImagesDetailsBean> list = new ArrayList<>();
        if (null != listImg) {
            for (NewsDetailsBean.ImgBean im : listImg) {
                list.add(new ImagesDetailsBean("", im.getSrc()));
            }
        }
        return list;
    }

}
