package com.better.calendarlunarday.utils;

import android.text.TextUtils;

import com.better.calendarlunarday.nongliManager.CnNongLiManager;

import java.util.Calendar;

public class AlmanacUtil {

    /* renamed from: a */
//    public static int[] f16764a = {C12582R$drawable.home_img_aries, C12582R$drawable.home_img_taurus, C12582R$drawable.home_img_gemini, C12582R$drawable.home_img_cancer, C12582R$drawable.home_img_leo, C12582R$drawable.home_img_virgo, C12582R$drawable.home_img_libra, C12582R$drawable.home_img_scorpio, C12582R$drawable.home_img_sagittarius, C12582R$drawable.home_img_capricorn, C12582R$drawable.home_img_aquarius, C12582R$drawable.home_img_pisces};

    /* renamed from: a */
    public static String huangdiYearString(int year, int month, int day) {
        return "黄帝纪元" + huangdiYearString(huangdiYearNumber(year, month, day) + "") + "年";
    }

    /* renamed from: b */

    /**
     * 将皇帝纪年法的数字转换成大写字符串
     *
     * @param year 2697
     * @return 大写字符串
     */
    public static String huangdiYearString(String year) {
        String str2 = "";
        if (TextUtils.isEmpty(year)) {
            return str2;
        }
        String[] strArr = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] strArr2 = {"十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        int length = year.length();
        for (int i = 0; i < length; i++) {
            int charAt = year.charAt(i) - '0';
            str2 = (i == length - 1 || charAt == 0) ? str2 + strArr[charAt] : str2 + strArr[charAt] + strArr2[(length - 2) - i];
        }
        return str2;
    }

    /* renamed from: c */
    public static boolean m14961c(int i, int i2, int i3) {
        if (i == 0) {
            return false;
        }
        CnNongLiManager cnNongLiManager = new CnNongLiManager();
        int jieqi = cnNongLiManager.getJieqi(i, 2);
        int i4 = i * /*Constants.TEN_SECONDS_MILLIS*/10000;
        int i5 = i4 + 200 + jieqi;
        long[] nongliToGongli = cnNongLiManager.nongliToGongli(i, 1, 1, false);
        int i6 = ((int) nongliToGongli[2]) + (((int) nongliToGongli[0]) */*Constants.TEN_SECONDS_MILLIS*/10000) + (((int) nongliToGongli[1]) * 100);
        int i7 = i4 + (i2 * 100) + i3;
        if (i6 > i5) {
            i5 = i6;
            i6 = i5;
        } else if (i6 >= i5) {
            i6 = i5;
        }
        if (i7 >= i5 || i7 < i6) {
            return true;
        }
        return false;
    }

//    /* renamed from: a */
//    public static int m14957a(String str) {
//        if (StringUtil.m6830a((CharSequence) "love", (CharSequence) str)) {
//            return C12582R$drawable.icon_luck_heart_line;
//        }
//        if (StringUtil.m6830a((CharSequence) "career", (CharSequence) str)) {
//            return C12582R$drawable.icon_luck_star_line;
//        }
//        if (StringUtil.m6830a((CharSequence) "fortune", (CharSequence) str)) {
//            return C12582R$drawable.icon_luck_money_line;
//        }
//        return -1;
//    }

    /* renamed from: b */

    /**
     * 公历转换为皇帝纪年日期
     *
     * @param year  公历年份
     * @param month 公历月份
     * @param day   公历日
     * @return 皇帝纪年日期
     */
    public static int huangdiYearNumber(int year, int month, int day) {
        return ((int) new CnNongLiManager().calGongliToNongli(year, month, day)[0]) + 2697;
    }

}
