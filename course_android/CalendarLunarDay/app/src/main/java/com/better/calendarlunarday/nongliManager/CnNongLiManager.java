package com.better.calendarlunarday.nongliManager;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CnNongLiManager {
    public static String[] lunarDate = {"初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"};
    public static String[] lunarMonth = {"正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "冬月", "腊月"};
    public final String API = "http://git.etouch.cn/gitbucket/lijianguo/SSYDAPI/tree/master/Android_NongLiManager";
    private final String[] Animals = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
    private final String[] FangWei = {"东", "北", "西", "南"};
    private final String[] Gan = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    private final String[] Zhi = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    public String[] jieqi = {"小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"};
    public final String version = "1.6";
    public String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * 农历某年总天数
     *
     * @param year 农历年份
     * @return days
     */
    private int lYearDays(int year) {
        // 农历一个月是29天，一年12个月，所以是348
        int day = 348;
        // 12次，刚好跟阳历比农历多12天一致
        for (int i = 32768/*等于2^15*/; i > 8/*等于2^3*/; i >>= 1) {
            if ((CnNongLiData.lunarInfo[year - 1900] & ((long) i)) != 0) {
                day++;
            }
        }
        return day + leapDays(year);
    }

    /**
     * 生肖
     *
     * @param year 阳历年分
     * @return 生肖
     */
    public String AnimalsYear(int year) {
        return this.Animals[(year - 4) % 12];
    }

    /**
     * 十二时辰冲煞
     *
     * @param shichen 时辰
     * @return string
     */
    public String calChongSha(int shichen) {
        int i = (shichen + 6) % 12;
        return "冲" +
                this.Animals[i] +
                "(" +
                this.Gan[(shichen + 4) % 10] +
                this.Zhi[i] +
                ")煞" +
                this.FangWei[(shichen + 3) % 4];
    }

    /**
     * 获取公历某天的时辰凶吉
     *
     * @param year  公历年
     * @param month 月
     * @param day   日
     * @return 时辰冲煞、凶吉
     * 0:冲煞{@link #calChongSha(int)}
     * 1:凶（值为0）吉（值为1）
     */
    public ArrayList<int[]> calDayTimeCyclical(int year, int month, int day) {
        ArrayList<int[]> arrayList = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            arrayList.add(new int[]{0, 0});
        }
        int i5 = ((int) calGongliToNongli(year, month, day)[5]) % 60;
        switch (i5 % 10) {
            case 0:
            case 5:
                for (int i = 0; i < 12; i++) {
                    arrayList.get(i)[0] = i;
                }
                break;
            case 1:
            case 6:
                for (int i = 0; i < 12; i++) {
                    arrayList.get(i)[0] = i + 12;
                }
                break;
            case 2:
            case 7:
                for (int i = 0; i < 12; i++) {
                    arrayList.get(i)[0] = i + 24;
                }
                break;
            case 3:
            case 8:
                for (int i = 0; i < 12; i++) {
                    arrayList.get(i)[0] = i + 36;
                }
                break;
            case 4:
            case 9:
                for (int i = 0; i < 12; i++) {
                    arrayList.get(i)[0] = i + 48;
                }
                break;
        }
        int[] array = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        switch (i5 % 12) {
            case 0:
            case 6:
                array = new int[]{1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0};
                break;
            case 1:
            case 7:
                array = new int[]{0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1};
                break;
            case 2:
            case 8:
                array = new int[]{1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0};
                break;
            case 3:
            case 9:
                array = new int[]{1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0};
                break;
            case 4:
            case 10:
                array = new int[]{0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1};
                break;
            case 5:
            case 11:
                array = new int[]{0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1};
                break;
        }
        for (int i = 0; i < 12; i++) {
            arrayList.get(i)[1] = array[i];
        }
        return arrayList;
    }

    /**
     * 公历日期对应的农历日期
     * 它的算法中农历一年比公历少12天，所以一个月就少一天
     *
     * @param year  公历年份
     * @param month 月
     * @param day   日期
     * @return 农历日期
     * 0:年
     * 1:月
     * 2:日
     * 3:
     * 4:
     * 5:
     * 6:
     */
    public long[] calGongliToNongli(int year, int month, int day) {
        long dayTmp = 0;
        int yearTmp = 0;
        long[] result = new long[7];
        // 1900 和 2000 年两种，每一百年一种方式？？
        if (year > 2000) {
            result[4] = 14;
            long gongliOneYearDays = getGongliOneYearDays(2000) - 36;
            result[4] = result[4] + 10;
            for (int i = 2001; i < year; i++) {
                gongliOneYearDays += getGongliOneYearDays(i);
                result[4] = result[4] + 12;
            }
            for (int i = 1; i < month; i++) {
                gongliOneYearDays += getGongliOneMonthDays(year, i);
                result[4] = result[4] + 1;
            }
            //2000年到今日的所有天数
            long j2 = gongliOneYearDays + ((long) day);
            if (day >= getJieqi(year, (month - 1) * 2)) {
                result[4] = result[4] + 1;
            }
            result[5] = 29 + j2;
            dayTmp = j2;
            yearTmp = 2000;
            while (yearTmp < 2101) {
                long lYearDays = lYearDays(yearTmp);
                if (dayTmp < lYearDays) {
                    break;
                }
                dayTmp -= lYearDays;
                yearTmp++;
            }
        } else {
            try {
                result[4] = 13;
                long gongliOneYearDays2 = getGongliOneYearDays(CnNongLiData.minYear) - 31;
                result[4] = result[4] + 11;
                for (int i = 1901; i < year; i++) {
                    gongliOneYearDays2 += getGongliOneYearDays(i);
                    result[4] = result[4] + 12;
                }
                for (int i = 1; i < month; i++) {
                    gongliOneYearDays2 += getGongliOneMonthDays(year, i);
                    result[4] = result[4] + 1;
                }
                dayTmp = gongliOneYearDays2 + ((long) day);
                if (day >= getJieqi(year, (month - 1) * 2)) {
                    result[4] = result[4] + 1;
                }
                result[5] = 40 + dayTmp;
                yearTmp = CnNongLiData.minYear;
                while (yearTmp < 2101) {
                    long lYearDays = lYearDays(yearTmp);
                    if (dayTmp < lYearDays) {
                        break;
                    }
                    dayTmp -= lYearDays;
                    yearTmp++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result[3] = yearTmp - 1864;
        result[0] = yearTmp;
        int leapMonth = leapMonth(yearTmp);
        result[6] = 0;
        int monthTmp = 1;
        boolean z = false;
        while (true) {
            if (monthTmp >= 13) {
                break;
            }
            if (leapMonth > 0 && monthTmp == leapMonth + 1 && !z) {
                long leapDays = leapDays((int) result[0]);
                if (dayTmp < leapDays) {
                    result[6] = 1;
                    monthTmp--;
                    break;
                }
                dayTmp -= leapDays;
                monthTmp--;
                z = true;
            } else {
                long monthDays = monthDays((int) result[0], monthTmp);
                if (dayTmp < monthDays) {
                    break;
                }
                dayTmp -= monthDays;
            }
            monthTmp++;
        }
        result[1] = monthTmp;
        result[2] = dayTmp + 1;
        return result;
    }

    public ArrayList<long[]> calNongliByMonth(int i, int i2) {
        int i3;
        long j;
        int i4;
        int monthDays;
        ArrayList<long[]> arrayList = new ArrayList<>();
        if (i >= 1900 && i <= 2101) {
            int i5 = i2 - 1;
            try {
                int jieqi2 = getJieqi(i, i5 * 2);
                long[] calGongliToNongli = calGongliToNongli(i, i2, 1);
                char c = 0;
                if (calGongliToNongli[6] == 1) {
                    i3 = leapDays((int) calGongliToNongli[0]);
                } else {
                    i3 = monthDays((int) calGongliToNongli[0], (int) calGongliToNongli[1]);
                }
                arrayList.add(calGongliToNongli);
                Calendar instance = Calendar.getInstance();
                instance.set(i, i5, 2);
                for (int i6 = 2; instance.get(i6) + 1 == i2; i6 = 2) {
                    long[] jArr = new long[7];
                    if (calGongliToNongli[i6] >= ((long) i3) || instance.get(5) == jieqi2) {
                        long[] calGongliToNongli2 = calGongliToNongli(i, i2, instance.get(5));
                        j = 1;
                        if (calGongliToNongli[6] == 1) {
                            monthDays = leapDays((int) calGongliToNongli[c]);
                        } else {
                            monthDays = monthDays((int) calGongliToNongli[c], (int) calGongliToNongli[1]);
                        }
                        calGongliToNongli = calGongliToNongli2;
                        i4 = monthDays;
                    } else {
                        jArr[c] = calGongliToNongli[c];
                        jArr[1] = calGongliToNongli[1];
                        jArr[i6] = calGongliToNongli[i6] + 1;
                        jArr[3] = calGongliToNongli[3];
                        jArr[4] = calGongliToNongli[4];
                        jArr[5] = calGongliToNongli[5] + 1;
                        jArr[6] = calGongliToNongli[6];
                        calGongliToNongli = jArr;
                        i4 = i3;
                        j = 1;
                    }
                    arrayList.add(calGongliToNongli);
                    instance.add(5, 1);
                    i3 = i4;
                    c = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public int calNongliJiangeDays(int i, int i2, int i3, boolean z, int i4, int i5, int i6, boolean z2) {
        try {
            long[] nongliToGongli = nongliToGongli(i, i2, i3, z);
            long[] nongliToGongli2 = nongliToGongli(i4, i5, i6, z2);
            return (int) ((new Date((int) (nongliToGongli2[0] - 1900), (int) (nongliToGongli2[1] - 1), (int) nongliToGongli2[2]).getTime() - new Date((int) (nongliToGongli[0] - 1900), (int) (nongliToGongli[1] - 1), (int) nongliToGongli[2]).getTime()) / 86400000);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String cyclical(int i) {
        return cyclicalm((i - 1900) + 36);
    }

    public String cyclicalm(int i) {
        return this.Gan[i % 10] + this.Zhi[i % 12];
    }

    /**
     * 公历某月天数
     *
     * @param year  公历年份
     * @param month 月
     * @return day
     */
    public int getGongliOneMonthDays(int year, int month) {
        return month == 2 ? isGongliYearIsLeapYear(year) ? 29 : 28 : (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) ? 31 : 30;
    }

    /**
     * 公历某年的天数
     *
     * @param year 公历年份
     * @return day
     */
    public int getGongliOneYearDays(int year) {
        return isGongliYearIsLeapYear(year) ? 366 : 365;
    }

    public int getJieqi(int year, int p) {
        int i3;
        if (year < 1900 || year > 2101) {
            return 5;
        }
        try {
            int i4 = ((year - CnNongLiData.minYear) * 6) + (p / 4);
            int i5 = p % 4;
            if (i5 == 0) {
                i3 = ((int) (CnNongLiData.jieqi[i4] & -16777216)) >> 24;
            } else if (i5 == 1) {
                i3 = ((int) (CnNongLiData.jieqi[i4] & 16711680)) >> 16;
            } else if (i5 == 2) {
                i3 = ((int) (CnNongLiData.jieqi[i4] & 65280)) >> 8;
            } else if (i5 != 3) {
                return 5;
            } else {
                i3 = ((int) (CnNongLiData.jieqi[i4] & 255));
            }
            return i3;
        } catch (Exception e) {
            e.printStackTrace();
            return 5;
        }
    }

    /**
     * 公历某年是否是闰年。
     * 四年一闰，百年不闰，四百年再闰
     *
     * @param year 公历年份
     * @return true 是闰年
     */
    public boolean isGongliYearIsLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    /**
     * 某年的闰月天数？？
     *
     * @param year 年
     * @return days
     */
    public int leapDays(int year) {
        if (leapMonth(year) != 0) {
            return (CnNongLiData.lunarInfo[year + -1900] & 65536) != 0 ? 30 : 29;
        }
        return 0;
    }

    public int leapMonth(int i) {
        return (int) (CnNongLiData.lunarInfo[i - 1900] & 15);
    }

    public int monthDays(int year, int i2) {
        if (year < 1900) {
            return 30;
        }
        try {
            return (((long) (65536 >> i2)) & CnNongLiData.lunarInfo[year - CnNongLiData.minYear]) == 0 ? 29 : 30;
        } catch (Exception e) {
            e.printStackTrace();
            return 30;
        }
    }

    public long[] nongliToGongli(int i, int i2, int i3, boolean z) {
        long[] result = new long[3];
        long j = 0;
        for (int i4 = CnNongLiData.minYear; i4 < i; i4++) {
            try {
                j += lYearDays(i4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i5 = 1; i5 < i2; i5++) {
            j += monthDays(i, i5);
        }
        if (z && leapMonth(i) == i2) {
            j += monthDays(i, i2);
        }
        if (leapMonth(i) > 0 && leapMonth(i) < i2) {
            j += leapDays(i);
        }
        Date date = new Date(((new Date(0, 0, 31).getTime() / 86400000) + j + ((long) (i3 - 1))) * 86400000);
        result[0] = date.getYear() + CnNongLiData.minYear;
        result[1] = date.getMonth() + 1;
        result[2] = date.getDate();
        return result;
    }
}