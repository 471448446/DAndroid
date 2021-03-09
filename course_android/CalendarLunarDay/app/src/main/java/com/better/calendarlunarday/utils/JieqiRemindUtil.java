//package com.better.calendarlunarday.utils;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.support.p007v4.app.NotificationCompat;
//import android.text.TextUtils;
//import com.xiaomi.mipush.sdk.Constants;
//import java.util.ArrayList;
//import java.util.Calendar;
//import p093cn.etouch.ecalendar.bean.EcalendarNoticeLightBean;
//import p093cn.etouch.ecalendar.bean.EcalendarTableDataBean;
//import p093cn.etouch.ecalendar.common.p121i.StringUtil;
//import p093cn.etouch.ecalendar.manager.C1781Ia;
//import p093cn.etouch.ecalendar.nongliManager.CnNongLiManager;
//import p093cn.etouch.ecalendar.service.NoticesReceiver;
//
//public class JieqiRemindUtil {
//
//    /* renamed from: a */
//    public static int f6711a = -200;
//
//    /* renamed from: b */
//    public static int f6712b = -224;
//
//    /* renamed from: c */
//    public static int f6713c = -225;
//
//    /* renamed from: a */
//    public static void m5257a(Context context) {
//        String str;
//        m5263b(context);
//        if (m5265c(context)) {
//            int[] j = C1781Ia.m9084j();
//            CnNongLiManager cnNongLiManager = new CnNongLiManager();
//            int jieqi = cnNongLiManager.getJieqi(j[0], (j[1] - 1) * 2);
//            int jieqi2 = cnNongLiManager.getJieqi(j[0], ((j[1] - 1) * 2) + 1);
//            int i = -1;
//            if (j[2] == jieqi) {
//                i = (j[1] - 1) * 2;
//                if (i != 6) {
//                    str = cnNongLiManager.jieqi[i];
//                } else {
//                    return;
//                }
//            } else if (j[2] == jieqi2) {
//                i = ((j[1] - 1) * 2) + 1;
//                if (i != 6) {
//                    str = cnNongLiManager.jieqi[i];
//                } else {
//                    return;
//                }
//            } else {
//                str = "";
//            }
//            if (!TextUtils.isEmpty(str)) {
//                m5258a(context, f6711a - i);
//            }
//        }
//    }
//
//    /* renamed from: b */
//    public static void m5263b(Context context) {
//        int c = new MyPreferencesJustForJieqi(context).mo12260c();
//        if (c != -1) {
//            NoticesReceiver.m12547a(context, c);
//        }
//    }
//
//    /* renamed from: c */
//    private static boolean m5265c(Context context) {
//        MyPreferencesJustForJieqi kbVar = new MyPreferencesJustForJieqi(context);
//        if (!kbVar.mo12258a()) {
//            return false;
//        }
//        int[] j = C1781Ia.m9084j();
//        String b = kbVar.mo12259b();
//        if (b.equals(j[0] + Constants.ACCEPT_TIME_SEPARATOR_SERVER + j[1] + Constants.ACCEPT_TIME_SEPARATOR_SERVER + j[2])) {
//            return false;
//        }
//        return true;
//    }
//
//    /* renamed from: d */
//    private static String m5266d(int i) {
//        try {
//            CnNongLiManager cnNongLiManager = new CnNongLiManager();
//            return cnNongLiManager.jieqi[f6711a - i];
//        } catch (Exception unused) {
//            return "";
//        }
//    }
//
//    /* renamed from: b */
//    public static EcalendarTableDataBean m5259b(int i, int i2, int i3) {
//        String str;
//        CnNongLiManager cnNongLiManager = new CnNongLiManager();
//        int i4 = (i2 - 1) * 2;
//        int jieqi = cnNongLiManager.getJieqi(i, i4);
//        int i5 = i4 + 1;
//        int jieqi2 = cnNongLiManager.getJieqi(i, i5);
//        if (i3 <= jieqi) {
//            str = cnNongLiManager.jieqi[i4];
//        } else if (i3 <= jieqi2) {
//            str = cnNongLiManager.jieqi[i5];
//            i4 = i5;
//            jieqi = jieqi2;
//        } else {
//            jieqi = 1;
//            str = "";
//            i4 = -1;
//        }
//        int i6 = !TextUtils.isEmpty(str) ? f6711a - i4 : -1;
//        if (i6 != -1) {
//            return m5253a(i6, i, i2, jieqi);
//        }
//        return null;
//    }
//
//    /* renamed from: c */
//    public static EcalendarNoticeLightBean m5264c(int i) {
//        int[] j = C1781Ia.m9084j();
//        EcalendarNoticeLightBean ecalendarNoticeLightBean = new EcalendarNoticeLightBean();
//        ecalendarNoticeLightBean.f5616a = i;
//        ecalendarNoticeLightBean.f5638l = 2;
//        ecalendarNoticeLightBean.f5657z = 0;
//        ecalendarNoticeLightBean.f5626f = 2;
//        ecalendarNoticeLightBean.f5615Z = 999;
//        ecalendarNoticeLightBean.f5642n = 1;
//        ecalendarNoticeLightBean.f5649r = j[3];
//        ecalendarNoticeLightBean.f5654w = ecalendarNoticeLightBean.f5649r;
//        ecalendarNoticeLightBean.f5650s = j[4];
//        ecalendarNoticeLightBean.f5655x = ecalendarNoticeLightBean.f5650s;
//        ecalendarNoticeLightBean.f5628g = m5266d(i);
//        ecalendarNoticeLightBean.f5644o = j[0];
//        ecalendarNoticeLightBean.f5646p = j[1];
//        ecalendarNoticeLightBean.f5648q = j[2];
//        int i2 = ecalendarNoticeLightBean.f5644o;
//        ecalendarNoticeLightBean.f5651t = i2;
//        int i3 = ecalendarNoticeLightBean.f5646p;
//        ecalendarNoticeLightBean.f5652u = i3;
//        int i4 = ecalendarNoticeLightBean.f5648q;
//        ecalendarNoticeLightBean.f5653v = i4;
//        ecalendarNoticeLightBean.f5603N = i2;
//        ecalendarNoticeLightBean.f5604O = i3;
//        ecalendarNoticeLightBean.f5605P = i4;
//        return ecalendarNoticeLightBean;
//    }
//
//    /* renamed from: b */
//    public static EcalendarTableDataBean m5260b(int i, int i2, int i3, int i4) {
//        EcalendarNoticeLightBean ecalendarNoticeLightBean = new EcalendarNoticeLightBean();
//        ecalendarNoticeLightBean.f5616a = i;
//        ecalendarNoticeLightBean.f5638l = 2;
//        ecalendarNoticeLightBean.f5657z = 0;
//        ecalendarNoticeLightBean.f5626f = 2;
//        ecalendarNoticeLightBean.f5615Z = 999;
//        ecalendarNoticeLightBean.f5642n = 1;
//        C1448pb a = C1448pb.m6897a(ApplicationManager.f6626h);
//        ecalendarNoticeLightBean.f5649r = a.mo12440c(1);
//        ecalendarNoticeLightBean.f5654w = ecalendarNoticeLightBean.f5649r;
//        ecalendarNoticeLightBean.f5650s = a.mo12440c(2);
//        ecalendarNoticeLightBean.f5655x = ecalendarNoticeLightBean.f5650s;
//        ecalendarNoticeLightBean.f5628g = m5261b(i);
//        ecalendarNoticeLightBean.f5644o = i2;
//        ecalendarNoticeLightBean.f5646p = i3;
//        ecalendarNoticeLightBean.f5648q = i4;
//        int i5 = ecalendarNoticeLightBean.f5644o;
//        ecalendarNoticeLightBean.f5651t = i5;
//        int i6 = ecalendarNoticeLightBean.f5646p;
//        ecalendarNoticeLightBean.f5652u = i6;
//        int i7 = ecalendarNoticeLightBean.f5648q;
//        ecalendarNoticeLightBean.f5653v = i7;
//        ecalendarNoticeLightBean.f5603N = i5;
//        ecalendarNoticeLightBean.f5604O = i6;
//        ecalendarNoticeLightBean.f5605P = i7;
//        return ecalendarNoticeLightBean;
//    }
//
//    /* renamed from: a */
//    private static void m5258a(Context context, int i) {
//        if (m5265c(context)) {
//            new MyPreferencesJustForJieqi(context).mo12255a(i);
//            Intent a = BroadcastFor8Utils.m7271a(context, "cn.etouch.ecalendar_ACTION_SUISENT_ECALENDAR_ShowNotice");
//            a.putExtra("id", i);
//            PendingIntent broadcast = PendingIntent.getBroadcast(context, i, a, 268435456);
//            C1448pb a2 = C1448pb.m6897a(context);
//            Calendar instance = Calendar.getInstance();
//            instance.set(11, a2.mo12440c(1));
//            instance.set(12, a2.mo12440c(2));
//            instance.set(13, 0);
//            C1781Ia.m8950a((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM), 0, instance.getTimeInMillis(), broadcast);
//        }
//    }
//
//    /* renamed from: a */
//    public static EcalendarTableDataBean m5253a(int i, int i2, int i3, int i4) {
//        EcalendarNoticeLightBean ecalendarNoticeLightBean = new EcalendarNoticeLightBean();
//        ecalendarNoticeLightBean.f5616a = i;
//        ecalendarNoticeLightBean.f5638l = 2;
//        ecalendarNoticeLightBean.f5657z = 0;
//        ecalendarNoticeLightBean.f5626f = 2;
//        ecalendarNoticeLightBean.f5615Z = 999;
//        ecalendarNoticeLightBean.f5642n = 1;
//        C1448pb a = C1448pb.m6897a(ApplicationManager.f6626h);
//        ecalendarNoticeLightBean.f5649r = a.mo12440c(1);
//        ecalendarNoticeLightBean.f5654w = ecalendarNoticeLightBean.f5649r;
//        ecalendarNoticeLightBean.f5650s = a.mo12440c(2);
//        ecalendarNoticeLightBean.f5655x = ecalendarNoticeLightBean.f5650s;
//        ecalendarNoticeLightBean.f5628g = m5266d(i);
//        ecalendarNoticeLightBean.f5644o = i2;
//        ecalendarNoticeLightBean.f5646p = i3;
//        ecalendarNoticeLightBean.f5648q = i4;
//        int i5 = ecalendarNoticeLightBean.f5644o;
//        ecalendarNoticeLightBean.f5651t = i5;
//        int i6 = ecalendarNoticeLightBean.f5646p;
//        ecalendarNoticeLightBean.f5652u = i6;
//        int i7 = ecalendarNoticeLightBean.f5648q;
//        ecalendarNoticeLightBean.f5653v = i7;
//        ecalendarNoticeLightBean.f5603N = i5;
//        ecalendarNoticeLightBean.f5604O = i6;
//        ecalendarNoticeLightBean.f5605P = i7;
//        return ecalendarNoticeLightBean;
//    }
//
//    /* renamed from: b */
//    public static String m5261b(int i) {
//        return i == f6713c ? "数九" : "三伏";
//    }
//
//    /* renamed from: b */
//    public static ArrayList<EcalendarTableDataBean> m5262b() {
//        Calendar instance = Calendar.getInstance();
//        int i = instance.get(1);
//        if (instance.get(2) == 0) {
//            i--;
//        }
//        ArrayList<EcalendarTableDataBean> arrayList = new ArrayList<>();
//        CnNongLiManager cnNongLiManager = new CnNongLiManager();
//        int i2 = 0;
//        while (i2 < 12) {
//            i2++;
//            int i3 = (i2 % 12) + 1;
//            int i4 = i2 >= 12 ? i + 1 : i;
//            int i5 = (i3 - 1) * 2;
//            int jieqi = cnNongLiManager.getJieqi(i4, i5);
//            int i6 = i5 + 1;
//            int jieqi2 = cnNongLiManager.getJieqi(i4, i6);
//            arrayList.add(m5253a(f6711a - i5, i4, i3, jieqi));
//            arrayList.add(m5253a(f6711a - i6, i4, i3, jieqi2));
//        }
//        return arrayList;
//    }
//
//    /* renamed from: a */
//    public static EcalendarTableDataBean m5254a(String str, int i, int i2, int i3) {
//        EcalendarNoticeLightBean ecalendarNoticeLightBean = new EcalendarNoticeLightBean();
//        if (StringUtil.m6830a((CharSequence) "三伏", (CharSequence) str)) {
//            ecalendarNoticeLightBean.f5616a = f6712b;
//        } else {
//            ecalendarNoticeLightBean.f5616a = f6713c;
//        }
//        ecalendarNoticeLightBean.f5638l = 2;
//        ecalendarNoticeLightBean.f5657z = 0;
//        ecalendarNoticeLightBean.f5626f = 2;
//        ecalendarNoticeLightBean.f5615Z = 999;
//        ecalendarNoticeLightBean.f5642n = 1;
//        C1448pb a = C1448pb.m6897a(ApplicationManager.f6626h);
//        ecalendarNoticeLightBean.f5649r = a.mo12440c(1);
//        ecalendarNoticeLightBean.f5654w = ecalendarNoticeLightBean.f5649r;
//        ecalendarNoticeLightBean.f5650s = a.mo12440c(2);
//        ecalendarNoticeLightBean.f5655x = ecalendarNoticeLightBean.f5650s;
//        ecalendarNoticeLightBean.f5628g = m5261b(ecalendarNoticeLightBean.f5616a);
//        ecalendarNoticeLightBean.f5644o = i;
//        ecalendarNoticeLightBean.f5646p = i2;
//        ecalendarNoticeLightBean.f5648q = i3;
//        int i4 = ecalendarNoticeLightBean.f5644o;
//        ecalendarNoticeLightBean.f5651t = i4;
//        int i5 = ecalendarNoticeLightBean.f5646p;
//        ecalendarNoticeLightBean.f5652u = i5;
//        int i6 = ecalendarNoticeLightBean.f5648q;
//        ecalendarNoticeLightBean.f5653v = i6;
//        ecalendarNoticeLightBean.f5603N = i4;
//        ecalendarNoticeLightBean.f5604O = i5;
//        ecalendarNoticeLightBean.f5605P = i6;
//        return ecalendarNoticeLightBean;
//    }
//
//    /* renamed from: a */
//    public static ArrayList<EcalendarTableDataBean> m5256a(int i, int i2) {
//        ArrayList<EcalendarTableDataBean> arrayList = new ArrayList<>();
//        CnNongLiManager cnNongLiManager = new CnNongLiManager();
//        int i3 = (i2 - 1) * 2;
//        int jieqi = cnNongLiManager.getJieqi(i, i3);
//        int i4 = i3 + 1;
//        int jieqi2 = cnNongLiManager.getJieqi(i, i4);
//        if (i3 != 6) {
//            arrayList.add(m5253a(f6711a - i3, i, i2, jieqi));
//        }
//        if (i4 != 6) {
//            arrayList.add(m5253a(f6711a - i4, i, i2, jieqi2));
//        }
//        return arrayList;
//    }
//
//    /* renamed from: a */
//    public static ArrayList<EcalendarTableDataBean> m5255a() {
//        Calendar instance = Calendar.getInstance();
//        int i = instance.get(1) + 1;
//        if (instance.get(2) == 0) {
//            i--;
//        }
//        ArrayList<EcalendarTableDataBean> arrayList = new ArrayList<>();
//        CnNongLiManager cnNongLiManager = new CnNongLiManager();
//        int i2 = 0;
//        while (i2 < 12) {
//            i2++;
//            int i3 = (i2 % 12) + 1;
//            int i4 = i2 >= 12 ? i + 1 : i;
//            int i5 = (i3 - 1) * 2;
//            int jieqi = cnNongLiManager.getJieqi(i4, i5);
//            int i6 = i5 + 1;
//            int jieqi2 = cnNongLiManager.getJieqi(i4, i6);
//            arrayList.add(m5253a(f6711a - i5, i4, i3, jieqi));
//            arrayList.add(m5253a(f6711a - i6, i4, i3, jieqi2));
//        }
//        return arrayList;
//    }
//
//    /* renamed from: a */
//    public static int m5252a(int i, int i2, int i3) {
//        int i4 = (i2 - 1) * 2;
//        if (i3 != new CnNongLiManager().getJieqi(i, i4)) {
//            i4++;
//        }
//        return f6711a - i4;
//    }
//
//    /* renamed from: a */
//    public static int m5251a(int i) {
//        if (i == 7 || i == 8) {
//            return f6712b;
//        }
//        return f6713c;
//    }
//}