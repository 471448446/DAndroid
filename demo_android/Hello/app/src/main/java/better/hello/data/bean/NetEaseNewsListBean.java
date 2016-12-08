package better.hello.data.bean;

import java.util.List;

/**
* Des 网易新闻详情
* Create By better on 2016/10/17 15:01.
*/
public class NetEaseNewsListBean {

    /**
     * postid : PHOT239S3000100A
     * hasCover : false
     * hasHead : 1
     * replyCount : 1521
     * hasImg : 1
     * digest :
     * hasIcon : false
     * docid : 9IG74V5H00963VRO_C3J6T68RluowenwenupdateDoc
     * title : 神舟十一号整流罩残骸在陕西榆林找到
     * order : 1
     * priority : 350
     * lmodify : 2016-10-17 13:47:53
     * boardid : photoview_bbs
     * ads : [{"title":"来京人员排队办北京居住证 用砖头占位","tag":"photoset","imgsrc":"http://cms-bucket.nosdn.127.net/05b78ea88a4f48d39d78508acdfda38b20161017113625.jpeg","subtitle":"","url":"00AP0001|2205544"},{"title":"台风\u201c沙莉嘉\u201d来袭 琼州海峡全线停航","tag":"photoset","imgsrc":"http://cms-bucket.nosdn.127.net/e1a93d7a34a84431a5907d947f95f7d920161017133937.jpeg","subtitle":"","url":"00AP0001|2205570"},{"docid":"C3IRQHJ305219PC7","title":"看着心疼！农民工无座票睡洗手台","tag":"doc","imgsrc":"http://cms-bucket.nosdn.127.net/38f99aed780844cf81dda191081a01e520161017131233.jpeg","subtitle":"","url":"C3IRQHJ305219PC7"},{"title":"中国首艘核潜艇退出现役 进驻海军博物馆","tag":"photoset","imgsrc":"http://cms-bucket.nosdn.127.net/7eacf2b265804bbda72b1b2c9515a43920161017090336.jpeg","subtitle":"","url":"00AP0001|2205453"},{"title":"Q&A:如何在空间站度过一个美好的早晨","tag":"link","imgsrc":"http://cms-bucket.nosdn.127.net/5992d122c75841419bcc65d7a6852aff20161016211646.jpeg","subtitle":"","url":"http://c.m.163.com/news/l/95689.html"}]
     * photosetID : 00AN0001|2205571
     * template : normal1
     * votecount : 1134
     * skipID : 00AN0001|2205571
     * alias : Top News
     * skipType : photoset
     * cid : C1348646712614
     * hasAD : 1
     * imgextra : [{"imgsrc":"http://cms-bucket.nosdn.127.net/c975f620f5ce46858d70a3fff472818f20161017134033.jpeg"},{"imgsrc":"http://cms-bucket.nosdn.127.net/0c10e9a199cd4de7a72b40ed2974736120161017134033.jpeg"}]
     * source : 网易原创
     * ename : androidnews
     * imgsrc : http://cms-bucket.nosdn.127.net/a92521635617400597af61b28406f53d20161017134033.jpeg
     * tname : 头条
     * ptime : 2016-10-17 13:41:08
     */

    private String postid;
    private boolean hasCover;
    private int hasHead;
    private int replyCount;
    private int hasImg;
    private String digest;
    private boolean hasIcon;
    private String docid;
    private String title;
    private String ltitle;
    private int order;
    private int priority;
    private String lmodify;
    private String boardid;
    private String photosetID;
    private String template;
    private int votecount;
    private String skipID;
    private String alias;
    private String skipType;
    private String cid;
    private int hasAD;
    private String source;
    private String ename;
    private String imgsrc;
    private String tname;
    private String ptime;
    /**
     * title : 来京人员排队办北京居住证 用砖头占位
     * tag : photoset
     * imgsrc : http://cms-bucket.nosdn.127.net/05b78ea88a4f48d39d78508acdfda38b20161017113625.jpeg
     * subtitle :
     * url : 00AP0001|2205544
     */

    private List<AdsBean> ads;
    /**
     * imgsrc : http://cms-bucket.nosdn.127.net/c975f620f5ce46858d70a3fff472818f20161017134033.jpeg
     */

    private List<ImgextraBean> imgextra;

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isHasCover() {
        return hasCover;
    }

    public void setHasCover(boolean hasCover) {
        this.hasCover = hasCover;
    }

    public int getHasHead() {
        return hasHead;
    }

    public void setHasHead(int hasHead) {
        this.hasHead = hasHead;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getHasImg() {
        return hasImg;
    }

    public void setHasImg(int hasImg) {
        this.hasImg = hasImg;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public boolean isHasIcon() {
        return hasIcon;
    }

    public void setHasIcon(boolean hasIcon) {
        this.hasIcon = hasIcon;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getLmodify() {
        return lmodify;
    }

    public void setLmodify(String lmodify) {
        this.lmodify = lmodify;
    }

    public String getBoardid() {
        return boardid;
    }

    public void setBoardid(String boardid) {
        this.boardid = boardid;
    }

    public String getPhotosetID() {
        return photosetID;
    }

    public void setPhotosetID(String photosetID) {
        this.photosetID = photosetID;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getVotecount() {
        return votecount;
    }

    public void setVotecount(int votecount) {
        this.votecount = votecount;
    }

    public String getSkipID() {
        return skipID;
    }

    public void setSkipID(String skipID) {
        this.skipID = skipID;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSkipType() {
        return skipType;
    }

    public void setSkipType(String skipType) {
        this.skipType = skipType;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getHasAD() {
        return hasAD;
    }

    public void setHasAD(int hasAD) {
        this.hasAD = hasAD;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public List<AdsBean> getAds() {
        return ads;
    }

    public void setAds(List<AdsBean> ads) {
        this.ads = ads;
    }

    public List<ImgextraBean> getImgextra() {
        return imgextra;
    }

    public void setImgextra(List<ImgextraBean> imgextra) {
        this.imgextra = imgextra;
    }

    public String getLtitle() {
        return ltitle;
    }

    public void setLtitle(String ltitle) {
        this.ltitle = ltitle;
    }

    public static class AdsBean {
        private String title;
        private String tag;
        private String imgsrc;
        private String subtitle;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class ImgextraBean {
        private String imgsrc;

        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }
    }
}
