package better.learn.mobileinfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

public class DeviceConfig {

    public static String getMacByJavaAPI() {
        try {
            Enumeration var0 = NetworkInterface.getNetworkInterfaces();

            NetworkInterface var1;
            do {
                if (!var0.hasMoreElements()) {
                    return null;
                }

                var1 = (NetworkInterface) var0.nextElement();
            } while (!"wlan0".equals(var1.getName()) && !"eth0".equals(var1.getName()));

            byte[] var2 = var1.getHardwareAddress();
            if (var2 != null && var2.length != 0) {
                StringBuilder var3 = new StringBuilder();
                byte[] var4 = var2;
                int var5 = var2.length;

                for (int var6 = 0; var6 < var5; ++var6) {
                    byte var7 = var4[var6];
                    var3.append(String.format("%02X:", var7));
                }

                if (var3.length() > 0) {
                    var3.deleteCharAt(var3.length() - 1);
                }

                return var3.toString().toLowerCase(Locale.getDefault());
            } else {
                return null;
            }
        } catch (Throwable var8) {
            return null;
        }
    }

    public static String getMacShell() {
        try {
            String[] var0 = new String[]{"/sys/class/net/wlan0/address", "/sys/class/net/eth0/address", "/sys/devices/virtual/net/wlan0/address"};

            for (int var2 = 0; var2 < var0.length; ++var2) {
                try {
                    String var1 = reaMac(var0[var2]);
                    if (var1 != null) {
                        return var1;
                    }
                } catch (Throwable var4) {
                    var4.printStackTrace();
                }
            }
        } catch (Throwable var5) {
            var5.printStackTrace();
        }

        return null;
    }

    private static String reaMac(String var0) {
        String var1 = null;

        try {
            FileReader var2 = new FileReader(var0);
            BufferedReader var3 = null;
            if (var2 != null) {
                try {
                    var3 = new BufferedReader(var2, 1024);
                    var1 = var3.readLine();
                } finally {
                    if (var2 != null) {
                        try {
                            var2.close();
                        } catch (Throwable var14) {
                            ;
                        }
                    }

                    if (var3 != null) {
                        try {
                            var3.close();
                        } catch (Throwable var13) {
                            ;
                        }
                    }

                }
            }
        } catch (Throwable var16) {
            var16.printStackTrace();
        }

        return var1;
    }

}
