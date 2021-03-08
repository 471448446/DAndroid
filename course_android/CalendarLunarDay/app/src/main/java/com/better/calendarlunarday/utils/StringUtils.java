package com.better.calendarlunarday.utils;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtils {
    private static final String[] lunarDays = new String[]{
            "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
            "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"
    };
    private static final String[] lunarMonth = new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

    /**
     * 在第11周等字符串中有使用
     *输出的结果就会右对齐前面空一空格。
     * @param number number
     * @return 格式化后的字符串
     */
    public static String formatNumber(int number) {
        return String.format(Locale.getDefault(), "%02d", number);
    }

    public static String a(boolean arg4, int arg5, int arg6, boolean arg7) {
        if (!arg4) {
            int v4 = 12;
            if (12 > arg5) {
                return arg7 ? String.format("%02d:%02d 上午", ((int) arg5), ((int) arg6)) : String.format("%02d:%02d", ((int) arg5), ((int) arg6));
            }

            int v5 = Math.abs(12 - arg5);
            if (v5 != 0) {
                v4 = v5;
            }

            return arg7 ? String.format("%02d:%02d 下午", ((int) v4), ((int) arg6)) : String.format("%02d:%02d", ((int) v4), ((int) arg6));
        }

        return String.format("%02d:%02d", ((int) arg5), ((int) arg6));
    }

    public static boolean a(CharSequence arg0) {
        return arg0 == null || arg0.length() == 0;
    }

    public static boolean a(String arg1) {
        return Pattern.compile("^([a-z0-9_A-Z]+[-|\\.]?)+[a-z0-9_A-Z]@([a-z0-9_A-Z]+(-[a-z0-9_A-Z]+)?\\.)+[a-zA-Z_]{2,}$").matcher(arg1).matches();
    }

    public static int[] a(String arg6, String arg7) {
        int[] v0 = new int[]{0, 0};
        if (!StringUtils.a(arg6) && !StringUtils.a(arg7)) {
            int v2;
            for (v2 = 0; true; ++v2) {
                String[] v3 = StringUtils.lunarDays;
                if (v2 >= v3.length) {
                    break;
                }

                if (v3[v2].equals(arg7)) {
                    v0[1] = v2 + 1;
                    break;
                }
            }

            if (arg6.contains("正月")) {
                v0[0] = 1;
                return v0;
            }

            if (arg6.contains("冬月")) {
                v0[0] = 11;
                return v0;
            }

            if (arg6.contains("腊月")) {
                v0[0] = 12;
                return v0;
            }

            if (arg6.contains("闰")) {
                arg6 = arg6.replace("闰", "");
            }

            int v7;
            for (v7 = 0; true; ++v7) {
                String[] v2_1 = StringUtils.lunarMonth;
                if (v7 >= v2_1.length) {
                    return v0;
                }

                if (v2_1[v7].equals(arg6)) {
                    v0[0] = v7 + 1;
                    return v0;
                }
            }
        }

        return v0;
    }

    public static String b(String arg3) {
        if (arg3 == null) {
            return "";
        }

        if (arg3.startsWith("+86")) {
            arg3 = arg3.substring(3);
        }

        if (arg3.startsWith("86")) {
            arg3 = arg3.substring(2);
        }

        if (arg3.startsWith("17951")) {
            arg3 = arg3.substring(5);
        }

        if (arg3.startsWith("12593")) {
            arg3 = arg3.substring(5);
        }

        if (arg3.startsWith("17911")) {
            arg3 = arg3.substring(5);
        }

        return arg3.replaceAll(" ", "");
    }

    public static String c(String arg3) {
        return String.format(Locale.getDefault(), "%07d", ((int) (((int) Integer.valueOf(arg3)))));
    }

    public static int d(String arg5) {
        String[] v5 = arg5.split("\\.");
        StringBuilder v0 = new StringBuilder();
        if (v5.length >= 1) {
            v0.append(v5[0]);
        }

        if (v5.length >= 2 && !StringUtils.a(v5[1])) {
            v0.append(v5[1].substring(0, 1));
        }

        String v5_1 = v0.toString();
        int v0_1 = -1;
        switch (v5_1.hashCode()) {
            case 76: {
                if (v5_1.equals("L")) {
                    v0_1 = 0;
                }

                break;
            }
            case 77: {
                if (v5_1.equals("M")) {
                    v0_1 = 1;
                }

                break;
            }
            case 78: {
                if (v5_1.equals("N")) {
                    v0_1 = 2;
                }
            }
        }

        if (v0_1 != 0) {
            if (v0_1 != 1) {
                if (v0_1 != 2) {
                    try {
                        return Integer.parseInt(v5_1);
                    } catch (NumberFormatException v5_2) {
                        v5_2.printStackTrace();
                        return 44;
                    }
                }

                return 70;
            }

            return 60;
        }

        return 50;
    }

    public static int e(String arg3) {
        int v3_1;
        if (StringUtils.a(arg3)) {
            return 6;
        }

        try {
            v3_1 = (int) Integer.valueOf(arg3);
        } catch (Exception v3) {
            v3.printStackTrace();
            return 6;
        }

        if (v3_1 <= 50 && v3_1 >= 0) {
            return 0;
        }

        if (v3_1 > 50 && v3_1 <= 100) {
            return 1;
        }

        if (v3_1 > 100 && v3_1 <= 150) {
            return 2;
        }

        if (v3_1 > 150 && v3_1 <= 200) {
            return 3;
        }

        if (v3_1 > 200 && v3_1 <= 300) {
            return 4;
        }

        return v3_1 <= 300 || v3_1 > 500 ? 6 : 5;
    }

    public static int[] f(String arg4) {
        try {
            return new int[]{Integer.parseInt(arg4.substring(0, 2)), Integer.parseInt(arg4.substring(2, 4))};
        } catch (Exception v4) {
            v4.printStackTrace();
            return null;
        }
    }

    public static int[] g(String arg7) {
        try {
            return new int[]{Integer.parseInt(arg7.substring(0, 4)), Integer.parseInt(arg7.substring(4, 6)), Integer.parseInt(arg7.substring(6, 8))};
        } catch (Exception v7) {
            v7.printStackTrace();
            Calendar v7_1 = Calendar.getInstance();
            return new int[]{v7_1.get(1), v7_1.get(2) + 1, v7_1.get(5)};
        }
    }

    public static String h(String arg2) {
        return arg2 == null ? "" : arg2.trim().replaceAll("-| |\\+86", "");
    }
}
