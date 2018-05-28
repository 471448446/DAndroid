package better.learn.mobileinfo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;

import better.library.utils.Utils;

public class CpuUtil {

    /**
     * https://www.jianshu.com/p/6bf564f7cdf0
     * https://issuetracker.google.com/issues/37140047
     * Create by Better 2018/5/28 13:52
     */
    public static double getCpuLoad() {
        try {
            double[] idleOld = getCpuIdle();
            Thread.sleep(1000);
            double[] idleNew = getCpuIdle();
            //totalCPUUsageRate = (非空闲cpu时间2-非空闲cpu时间1)／(cpu总时间2-cpu总时间1)x100%
            double usage = (idleOld[1] - idleNew[1]) / ((idleOld[0] + idleOld[1]) - (idleNew[0] + idleNew[1])) * 100;
            return usage;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static double[] getCpuIdle() throws IOException {
        double[] idleAndNoUsage = new double[2];
        RandomAccessFile cpuFile = new RandomAccessFile("/proc/stat", "r");
        String line = cpuFile.readLine();
        Util.log(line);
        //-----user + nice + sys + idle + iowait + irq + softirq + stealstolen + guest + guest_nice
        //cpu  41366639 9110414 24861518 785715 5794 4506 1311443 0 0 0
        String[] times = line.split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times.length; i++) {
            builder.append(times[i] + ";");
        }
        Util.log(builder.toString());
        idleAndNoUsage[0] = Double.parseDouble(times[5]);
        idleAndNoUsage[1] = Double.parseDouble(times[2])
                + Double.parseDouble(times[3])
                + Double.parseDouble(times[4])
                + Double.parseDouble(times[6])
                + Double.parseDouble(times[7])
                + Double.parseDouble(times[8])
                + Double.parseDouble(times[9]);
        cpuFile.close();
        return idleAndNoUsage;
    }

    /**
     * https://blog.csdn.net/su749520/article/details/79215366
     * Create by Better 2018/5/7 11:22
     */
    public static float getCpuTemperature1() {
        float result = 0f;

        BufferedReader br = null;
        String line = null;

        try {
            br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone1/temp"));
            line = br.readLine();
            if (line != null) {
                long temperature = Long.parseLong(line);
                if (temperature < 0) {
                    result = 0f;
                } else {
                    result = temperature;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static final String THERMAL_SPEC = "thermal_zone";
    public static final String TYPE_FILE_NAME = "type";
    public static final String TEMP_FILE_NAME = "temp";
    public static final String FILE_CONTENT_CPU_SPEC = "cpu";

    public static float getCpuTemperature() {
        String[] probeDirs = new String[]{
                "/sys/class/thermal/",
                "/sys/devices/virtual/thermal/"
        };

        for (String dir : probeDirs) {
            File base = new File(dir);
            if (base.exists() && base.isDirectory()) {
                return probeInnerFolderCpuTemp(base);
            }
        }
        return 0;
    }

    private static float probeInnerFolderCpuTemp(File dir) {
        File[] thermalFolders = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.contains(THERMAL_SPEC);
            }
        });

        if (thermalFolders == null || thermalFolders.length == 0) {
            return 0;
        }

        for (File thermalDir : thermalFolders) {
            File type = new File(thermalDir, TYPE_FILE_NAME);
            Log.d("Better", type.getAbsolutePath() + "::::" + FileUtil.readFileContent(type).toLowerCase());

            if (type.exists() && FileUtil.readFileContent(type).toLowerCase().contains(FILE_CONTENT_CPU_SPEC)) {
                String fileContent = FileUtil.readFirstLine(new File(thermalDir, TEMP_FILE_NAME));
                Log.d("Better", "___find:" + fileContent);
                if (fileContent.matches("^\\d+$")) {
                    try {
                        int temp = Integer.parseInt(fileContent);
                        if (temp > 1 && temp < 100) {
                            return temp;
                        }
                        if (temp > 1000 && temp < 1000 * 100) {
                            return temp / 1000;
                        }
                        return 0;
                    } catch (Throwable e) {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

}
