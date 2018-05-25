package better.learn.mobileinfo;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {
    /**
     * 读取文件中所有内容
     *
     * @param file
     * @return
     */
    public static String readFileContent(File file) {
        if (file != null && file.exists()) {
            try {
                return readContentFromStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    /**
     * 读取内容
     *
     * @param is
     * @return
     */
    public static String readContentFromStream(InputStream is) {
        if (is != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            try {
                String lineTxt;
                while ((lineTxt = br.readLine()) != null) {
                    sb.append(lineTxt).append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(br);
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 关闭流
     *
     * @param pCloseable
     */
    public static void close(Closeable pCloseable) {
        if (pCloseable != null) {
            try {
                pCloseable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件中第一行的内容
     *
     * @param file
     * @return
     */
    public static String readFirstLine(File file) {
        return readLine(file, 1);
    }
    /**
     * 从文件中读取第n行的内容
     *
     * @param file
     * @param lineNum
     * @return
     */
    public static String readLine(File file, int lineNum) {
        if (file != null && file.exists()) {
            try {
                return readLine(new FileInputStream(file), lineNum);
            } catch (Throwable ignored) {
            }
        }
        return "";
    }

    /**
     * 读取第一行内容
     *
     * @param is
     * @return
     */
    public static String readFirstLine(InputStream is) {
        return readLine(is, 1);
    }

    /**
     * 读取第n行的内容
     *
     * @param is
     * @param lineNum 行号计数由1开始
     * @return
     */
    public static String readLine(InputStream is, int lineNum) {
        if (is == null || lineNum < 1) {
            return "";
        }
        BufferedReader br = null;
        try {
            int count = lineNum;
            br = new BufferedReader(new InputStreamReader(is));
            String lineTxt;
            while ((lineTxt = br.readLine()) != null) {
                if (--count == 0) {
                    return lineTxt;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(br);
        }
        return "";
    }

}
