package better.app.chinawisdom

import android.preference.PreferenceManager
import android.text.TextUtils
import better.app.chinawisdom.data.bean.Bookbean
import better.app.chinawisdom.support.constant.ColorBgEnum
import better.app.chinawisdom.support.constant.TextSizeEnum
import better.app.chinawisdom.support.constant.TextTypeEnum
import better.app.chinawisdom.support.utils.SharePref
import better.app.chinawisdom.widget.book.BookAnimEnum

/**
 * Created by better on 2017/8/17 11:43.
 */
object SettingConfig {
    val books: ArrayList<Bookbean> = arrayListOf(
            Bookbean("论语", "lunyu"),
            Bookbean("易经", "yijing"),
            Bookbean("大学", "daxue"),
            Bookbean("中庸", "zhongyong"),
            Bookbean("止学", "zhixue"),
            Bookbean("孟子", "mengzi"),
            Bookbean("庄子", "zhuangzi"),
            Bookbean("老子", "zhuangzi"),
            Bookbean("左传", "zuozhuan"),
            Bookbean("史记", "shiji"),
            Bookbean("汉书", "hanshu"),
            Bookbean("管子", "guanzi"),
            Bookbean("荀子", "xunzi"),
            Bookbean("素书", "sushu"),
            Bookbean("冰鉴", "bingjian"),
            Bookbean("罗织经", "luozhijing"),
            Bookbean("荣枯鉴", "rongkujian"),
            Bookbean("鬼谷子", "guiguzi"),
            Bookbean("韩非子", "hanfeizi"),
            Bookbean("韬晦术", "taohuishui"),
            Bookbean("菜根谭", "caigentan"),
            Bookbean("战国策", "zhanguoce"),
            Bookbean("三国志", "sanguozhi"),
            Bookbean("商君书", "shangjunshu"),
            Bookbean("水经注", "shuijingzhu"),
            Bookbean("厚黑学", "houheixue"),
            Bookbean("三十六计", "sanshiliuji"),
            Bookbean("孙子兵法", "sunzibingfa"),
            Bookbean("太公六韬", "taigongliutao"),
            Bookbean("权谋残卷", "quanmoucanjuan"),
            Bookbean("资治通鉴", "zizhitongjian"),
            Bookbean("贞观政要", "zhenguanzhengyao"),
            Bookbean("黄帝内经", "huangdineijing"),
            Bookbean("晏子春秋", "yanzichunqiu"),
            Bookbean("古文观止", "guwenguanzhi"),
            Bookbean("王阳明全集", "wangyangmingquanji"),
            Bookbean("曾国藩家书", "zengguofanjiashu"),
            Bookbean("反经", "fanjing"),
            Bookbean("骗经", "pianjing"),
            Bookbean("智囊（上）", "zhinangquanjishang"),
            Bookbean("智囊（中）", "zhinangquanjizhong"),
            Bookbean("智囊（下）", "zhinangquanjixia"),
            Bookbean("中国寓言", "zhongguoyuyan"),
            Bookbean("资治通鉴文白对照", "baihuazizhitongjian")
    )
    var isLog = true
    var bookSelected = 0
    var chapterSelected = 0
    var configTextType = TextTypeEnum.Italics
    var configTextFace = configTextType.createTypeFace()
    var configBookAnimation = BookAnimEnum.COVER
    var configBgType = ColorBgEnum.BG0
    var configTextSize = TextSizeEnum.Normal

    fun init() {
        isLog = true
        bookSelected = SharePref.getInt(SharePref.KEY_BOOK_SELECT)
        chapterSelected = SharePref.getInt(SharePref.KEY_BOOK_CHAPTER_SELECT)
        recoverFromSetting()
    }

    fun recoverFromSetting() {
        configBookAnimation = BookAnimEnum.parse(PreferenceManager.getDefaultSharedPreferences(App.instance).getString(App.instance.getString(R.string.key_setting_slide), configBookAnimation.id.toString()).toInt())
        configBgType = ColorBgEnum.parse(PreferenceManager.getDefaultSharedPreferences(App.instance).getString(App.instance.getString(R.string.key_setting_bg), configBgType.ordinal.toString()).toInt())
        configTextType = TextTypeEnum.parse(PreferenceManager.getDefaultSharedPreferences(App.instance).getString(App.instance.getString(R.string.key_setting_textType), configTextType.id.toString()).toInt())
        configTextSize = TextSizeEnum.parse(PreferenceManager.getDefaultSharedPreferences(App.instance).getString(App.instance.getString(R.string.key_setting_textSize), configTextSize.ordinal.toString()).toInt())
        configTextFace = configTextType.createTypeFace()
    }

    fun rememberBookSelect(p: Int) {
        bookSelected = p
        SharePref.put(SharePref.KEY_BOOK_SELECT, p)
    }

    fun rememberBookChapterSelect(p: Int) {
        chapterSelected = p
        SharePref.put(SharePref.KEY_BOOK_CHAPTER_SELECT, p)
    }

    fun rememberBookChapterRead(chapter: String, p: Int) {
        SharePref.put(SharePref.KEY_BOOK_CHAPTER_SELECT_NAME, chapter)
        SharePref.put(SharePref.KEY_BOOK_CHAPTER_SELECT_READ, p)
    }

    fun getRememberBookChapterRead(chapter: String): Int =
            if (TextUtils.equals(chapter, (SharePref.getString(SharePref.KEY_BOOK_CHAPTER_SELECT_NAME)))) {
                SharePref.getInt(SharePref.KEY_BOOK_CHAPTER_SELECT_READ)
            } else {
                0
            }

}