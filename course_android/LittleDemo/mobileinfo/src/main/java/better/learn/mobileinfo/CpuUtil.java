package better.learn.mobileinfo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

public class CpuUtil {
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
            Log.d("Better",type.getAbsolutePath()+"::::"+FileUtil.readFileContent(type).toLowerCase());

            if (type.exists() && FileUtil.readFileContent(type).toLowerCase().contains(FILE_CONTENT_CPU_SPEC)) {
                String fileContent = FileUtil.readFirstLine(new File(thermalDir, TEMP_FILE_NAME));
                Log.d("Better","find:"+fileContent);
                if (fileContent.matches("^\\d+$")) {
                    try {
                        int temp = Integer.parseInt(fileContent);
                        if (temp > 1 && temp < 100) {
                            return temp;
                        }
                        if(temp > 1000 && temp < 1000 * 100){
                            return temp / 1000;
                        }
                        return 0;
                    } catch (Throwable e) {
                        return 0;
                    }
                }else{
                    return 0;
                }
            }
        }
        return 0;
    }

}
