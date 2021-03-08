package com.better.calendarlunarday.utils;

public class AlmanacCommentUtils {
    private static AlmanacCommentUtils a;

    public static AlmanacCommentUtils getInstance() {
        Class v0 = AlmanacCommentUtils.class;
        synchronized (v0) {
            if (AlmanacCommentUtils.a == null) {
                AlmanacCommentUtils.a = new AlmanacCommentUtils();
            }
        }

        return AlmanacCommentUtils.a;
    }

    public String a(int arg18) {
        int v3 = (arg18 + 6) % 12;
        return "冲" + new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"}[v3] + "(" + new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"}[(arg18 + 4) % 10] + new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"}[v3] + ")煞" + new String[]{"东", "北", "西", "南"}[(arg18 + 3) % 4];
    }

    public String a(int arg5, int arg6) {
        return new String[]{"建", "除", "满", "平", "定", "执", "破", "危", "成", "收", "开", "闭"}[(arg6 - arg5 % 12) % 12] + "日";
    }

    public String b(int arg10) {
        return "冲" + new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"}[(arg10 + 6) % 12] + " 煞" + new String[]{"东", "北", "西", "南"}[(arg10 + 3) % 4];
    }

    public String b(int arg7, int arg8) {
        return new String[]{"青龙", "明堂", "天刑", "朱雀", "金匮", "天德", "白虎", "玉堂", "天牢", "玄武", "司命", "勾陈"}[(arg8 + 4 - arg7 % 60 * 2) % 12];
    }

    public String c(int arg11) {
        String[] v1 = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        String[] v7 = new String[]{"东", "北", "西", "南"};
        Object[] v8 = new Object[]{v1[(arg11 + 6) % 12], null, null};
        int v11 = (arg11 + 3) % 4;
        v8[1] = v7[v11];
        v8[2] = v7[v11];
        return String.format("本日对属%1$s的人不太有利。\n本日煞神方位在%2$s方，向%3$s方行事要小心。", v8);
    }

    public String c(int arg19, int arg20) {
        return new String[]{"海中", "炉中", "大林", "路旁", "剑锋", "山头", "涧下", "城头", "白蜡", "杨柳", "井泉", "屋上", "霹雳", "松柏", "长流", "沙石", "山下", "平地", "壁上", "金箔", "覆灯", "天河", "大驿", "钗钏", "桑拓", "大溪", "沙中", "天上", "石榴", "大海"}[arg20 % 60 / 2] + new String[]{"土", "木", "金", "水", "火"}[(new int[]{1, 1, 2, 2, 3, 3, 4, 4, 5, 5}[arg20 % 10] + new int[]{1, 1, 2, 2, 3, 3, 1, 1, 2, 2, 3, 3}[arg20 % 12]) % 5] + " " + new String[]{"开", "闭", "建", "除", "满", "平", "定", "执", "破", "危", "成", "收"}[(arg20 - (arg19 - 2) % 12) % 12] + "执位";
    }

    public String d(int arg16) {
        return new String[]{"甲不开仓财物耗散", "乙不栽植千株不长", "丙不修灶必见灾殃", "丁不剃头头必生疮", "戊不受田田主不祥", "己不破券二比并亡", "庚不经络织机虚张", "辛不合酱主人不尝", "壬不汲水更难提防", "癸不词讼理弱敌强"}[arg16 % 10] + " " + new String[]{"子不问卜自惹祸殃", "丑不冠带主不还乡", "寅不祭祀神鬼不尝", "卯不穿井水泉不香", "辰不哭泣必主重丧", "巳不远行财物伏藏", "午不苫盖屋主更张", "未不服药毒气入肠", "申不安床鬼祟入房", "酉不会客醉坐颠狂", "戌不吃犬作怪上床", "亥不嫁娶不利新郎"}[arg16 % 12];
    }

    public String e(int arg11) {
        String[] v1 = new String[]{"占门", "碓磨", "厨灶", "仓库", "房床"};
        String[] v8 = new String[]{"碓", "厕", "炉", "门", "栖", "床"};
        int v0 = arg11 % 60;
        String v2 = "外正南";
        if (v0 < 2) {
            v2 = "外东南";
        } else if (v0 >= 7) {
            if (v0 < 14) {
                v2 = "外西南";
            } else if (v0 >= 16) {
                if (v0 < 18) {
                    v2 = "外正西";
                } else if (v0 < 24) {
                    v2 = "外西北";
                } else if (v0 < 29) {
                    v2 = "外正北";
                } else if (v0 < 34) {
                    v2 = "房内北";
                } else if (v0 < 40) {
                    v2 = "房内南";
                } else if (v0 < 45) {
                    v2 = "房内东";
                } else if (v0 < 51) {
                    v2 = "外东北";
                } else {
                    v2 = v0 >= 56 ? "外东南" : "外正东";
                }
            }
        }

        String v11 = v1[arg11 % 5] + v8[arg11 % 6];
        if (v11.equals("占门门")) {
            return "占大门" + v2;
        }

        if (v11.equals("碓磨碓")) {
            return "占碓磨" + v2;
        }

        if (v11.equals("房床床")) {
            return "占房床" + v2;
        }

        if (v11.equals("占门栖")) {
            v11 = "门鸡栖";
        }

        return v11 + v2;
    }

    public String f(int arg6) {
        return new String[]{"东方角木蛟-吉", "东方亢金龙-凶", "东方氐土貉-凶", "东方房日兔-吉", "东方心月狐-凶", "东方尾火虎-吉", "东方箕水豹-吉", "北方斗木獬-吉", "北方牛金牛-凶", "北方女土蝠-凶", "北方虚日鼠-凶", "北方危月燕-凶", "北方室火猪-吉", "北方壁水貐-吉", "西方奎木狼-凶", "西方娄金狗-吉", "西方胃土雉-吉", "西方昴日鸡-凶", "西方毕月乌-吉", "西方觜火猴-凶", "西方参水猿-吉", "南方井木犴-吉", "南方鬼金羊-凶", "南方柳土獐-凶", "南方星日马-凶", "南方张月鹿-吉", "南方翼火蛇-凶", "南方轸水蚓-吉"}[(arg6 - 6) % 28];
    }

    public String g(int arg4) {
        return this.f(arg4).substring(2, 5) + "宿星";
    }

    /**
     * 财神、福神、喜神、贵神
     * https://www.jianshu.com/p/27e5709e7350 看起来是有口诀的
     *
     * @param arg23 index
     * @return 五大神方位
     */
    public String wuShenFangwei(int arg23) {
        String[] fiveGod = new String[]{"财神", "喜神", "福神", "阳贵", "阴贵"};
        int v8 = arg23 % 10;
        return fiveGod[0] + "—" + new String[]{"东北", "西南", "正西", "正西", "正北", "正北", "正东", "正东", "正南", "正南"}[v8] + "&" +
                fiveGod[1] + "—" + new String[]{"东北", "西北", "西南", "正南", "东南", "东北", "西北", "西南", "正南", "东南"}[v8] + "&" +
                fiveGod[2] + "—" + new String[]{"正北", "西南", "西北", "东南", "东北", "正北", "西南", "西北", "东南", "东北"}[v8] + "&" +
                fiveGod[3] + "—" + new String[]{"西南", "西南", "正西", "西北", "东北", "正北", "东北", "东北", "正东", "东南"}[v8] + "&" +
                fiveGod[4] + "—" + new String[]{"东北", "正北", "西北", "正西", "西南", "西南", "西南", "正南", "东南", "正东"}[v8];
    }
}


