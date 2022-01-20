package com.test.handwardtest;

public class Utils {
    static Boolean isX86;

    public static boolean isX86Device() {
        if (isX86 == null) {
            try {
                String response = SystemProperties.get("ro.product.cpu.abi");
                isX86 = response.contains("x86") || response.contains("x32");
            } catch (Throwable e1) {
                isX86 = false;
            }
        }
        return isX86;
    }

}
