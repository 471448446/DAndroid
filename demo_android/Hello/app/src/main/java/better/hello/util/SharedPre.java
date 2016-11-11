package better.hello.util;

import better.hello.App;
import better.lib.utils.BaseSharedPref;

/**
 * Created by better on 2016/11/11.
 */

public class SharedPre extends BaseSharedPref {
    public SharedPre() {
        super(App.getApplication().getClass().getSimpleName());
    }
}
