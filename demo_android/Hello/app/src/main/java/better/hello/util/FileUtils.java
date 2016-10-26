package better.hello.util;

import java.io.InputStream;

import better.hello.App;

/**
 * Created by better on 2016/10/19.
 */

public class FileUtils {

    public static InputStream readFileFromRaw(int fileID) {
        return App.getApplication().getResources()
                .openRawResource(fileID);
    }
}
