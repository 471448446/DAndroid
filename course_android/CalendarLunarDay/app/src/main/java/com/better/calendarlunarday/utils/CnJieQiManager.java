package com.better.calendarlunarday.utils;

import com.better.calendarlunarday.nongliManager.CnNongLiManager;

import java.util.Calendar;
import java.util.Date;

/**
 * 用来计算某个年的 数伏、节气
 * cn.etouch.ecalendar.provider.CnJieQiManager
 */
public class CnJieQiManager {
    private Calendar chufu = Calendar.getInstance();
    private Date dongzhi = new Date();
    private Calendar mofu = Calendar.getInstance();
    private int month = 0;
    private CnNongLiManager nongLiManager;
    private String[] shujiu = {"一九", "二九", "三九", "四九", "五九", "六九", "七九", "八九", "九九"};
    private String[] shuzi = {"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    private int year = 0;

    public CnJieQiManager(int year, int month) {
        this.year = year;
        this.month = month;
        this.nongLiManager = new CnNongLiManager();
        int i3 = this.month;
        if (i3 == 7 || i3 == 8) {
            int jieqi = this.nongLiManager.getJieqi(this.year, 11);
            int i4 = ((int) this.nongLiManager.calGongliToNongli(this.year, 6, jieqi)[5]) % 10;
            int i5 = i4 <= 6 ? 6 - i4 : (6 - i4) + 10;
            this.chufu = Calendar.getInstance();
            this.chufu.set(this.year, 5, jieqi, 0, 0);
            this.chufu.add(5, i5 + 20);
            if (this.month == 8) {
                int jieqi2 = this.nongLiManager.getJieqi(this.year, 14);
                int i6 = ((int) this.nongLiManager.calGongliToNongli(this.year, 8, jieqi2)[5]) % 10;
                int i7 = i6 <= 6 ? 6 - i6 : (6 - i6) + 10;
                this.mofu = Calendar.getInstance();
                this.mofu.set(this.year, 7, jieqi2, 0, 0);
                this.mofu.add(5, i7);
            }
        } else if (i3 == 12 || i3 == 1 || i3 == 2 || i3 == 3) {
            this.dongzhi = new Date(this.month == 12 ? this.year - 1900 : (this.year - 1900) - 1, 11, this.nongLiManager.getJieqi(this.month == 12 ? this.year : this.year - 1, 23));
        }
    }

    public static boolean isHaveShuJiuOrShufu(int i, int i2) {
        return i2 == 12 || i2 == 1 || i2 == 2 || i2 == 3 || i2 == 7 || i2 == 8;
    }

    public String getJieQi(int day) {
        int jieqi = this.nongLiManager.getJieqi(this.year, (this.month - 1) * 2);
        int jieqi2 = this.nongLiManager.getJieqi(this.year, ((this.month - 1) * 2) + 1);
        if (day == jieqi) {
            return CnNongLiManager.jieqi[(this.month - 1) * 2];
        }
        return day == jieqi2 ? CnNongLiManager.jieqi[((this.month - 1) * 2) + 1] : "";
    }

    public String[] getShuJiuOrShufu(int day) {
        int time;
        String[] strArr = {"", ""};
        int i2 = this.month;
        if (i2 == 7) {
            if (day >= this.chufu.get(5)) {
                int i3 = day - this.chufu.get(5);
                int i4 = i3 / 10;
                if (i4 == 0) {
                    int i5 = i3 % 10;
                    if (i5 == 0) {
                        strArr[0] = "初伏";
                        strArr[1] = "初伏第" + this.shuzi[i5 + 1] + "天";
                    } else {
                        strArr[0] = "";
                        strArr[1] = "初伏第" + this.shuzi[i5 + 1] + "天";
                    }
                } else if (i4 == 1 && i3 % 10 == 0) {
                    strArr[0] = "中伏";
                    strArr[1] = "中伏第" + this.shuzi[(i3 - 10) + 1] + "天";
                } else {
                    strArr[0] = "";
                    strArr[1] = "中伏第" + this.shuzi[(i3 - 10) + 1] + "天";
                }
            }
        } else if (i2 == 8) {
            if (day < this.mofu.get(5)) {
                int i6 = ((day + 31) - this.chufu.get(5)) - 10;
                if (i6 == 0) {
                    strArr[0] = "中伏";
                } else {
                    strArr[0] = "";
                }
                strArr[1] = "中伏第" + this.shuzi[i6 + 1] + "天";
            } else if (day < this.mofu.get(5) + 10) {
                if (day == this.mofu.get(5)) {
                    strArr[0] = "末伏";
                    strArr[1] = "末伏第" + this.shuzi[(day - this.mofu.get(5)) + 1] + "天";
                } else {
                    strArr[0] = "";
                    strArr[1] = "末伏第" + this.shuzi[(day - this.mofu.get(5)) + 1] + "天";
                }
            }
        } else if ((i2 == 12 || i2 == 1 || i2 == 2 || i2 == 3) && (time = (int) ((new Date(this.year - 1900, this.month - 1, day).getTime() - this.dongzhi.getTime()) / 86400000)) >= 0 && time < 81) {
            int i7 = time % 9;
            if (i7 == 0) {
                strArr[0] = this.shujiu[time / 9];
            }
            strArr[1] = this.shujiu[time / 9] + "第" + this.shuzi[i7 + 1] + "天";
        }
        return strArr;
    }
}